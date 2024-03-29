package de.ulei.nebeneinkuenfte.model.crawler;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.UnsupportedCharsetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;

import de.ulei.nebeneinkuenfte.ui.NebeneinkuenfteApplication;
import de.ulei.nebeneinkuenfte.ui.model.Abgeordneter;
import de.ulei.nebeneinkuenfte.ui.model.Nebentaetigkeit;
import de.ulei.nebeneinkuenfte.util.IConstants;

public class BundestagConverter {

	String startUri = null;
	int[] from = new int[2];
	int[] to = new int[2];
	List<String> cities = new ArrayList<String>();
	List<Abgeordneter> mdbs = new ArrayList<Abgeordneter>();
	private Properties properties;
	
	/**
	 * Konstruktor
	 * 
	 * @param URI
	 * @param scrape
	 *            Scrapen oder vom lokalen caching laden
	 */

	public BundestagConverter(String URI, boolean scrape) {
		this(URI, scrape, "./WebContent/abgeordnete/");
	}

	/**
	 * Konstruktor
	 * 
	 * @param URI
	 * @param scrape
	 *            Scrapen oder vom lokalen caching laden
	 * @param absoluter
	 *            Pfad zum lokalem Cache
	 */

	public BundestagConverter(String URI, boolean scrape, String path) {
		
		this.loadProperties();
		
		String[] tempFrom = properties.getProperty("from").split("\\.");
		String[] tempTo = properties.getProperty("to").split("\\.");

		this.from[0] = Integer.parseInt(tempFrom[0]);
		this.from[1] = Integer.parseInt(tempFrom[1]);
		this.to[0] = Integer.parseInt(tempTo[0]);
		this.to[1] = Integer.parseInt(tempTo[1]);
		
		this.startUri = URI;
		this.cities = this.readCityFile("./WebContent/external_data/staedte_osm");

		ArrayList<String> failed = new ArrayList<String>();
		
		if (scrape) {
			this.fillMdBList(URI);
			int count = 0;
			for (Abgeordneter mdb : this.mdbs) {
//				if(mdb.getLastname().toLowerCase().contains("leutheusser")) {
				count++;
				if (!this.parseMdB(mdb)) {
					// parse failed
					System.out.println("Trying again.");
					wait(2000);
					if (!this.parseMdB(mdb)) {
						failed.add(mdb.getURI());
					}
				}
				
				mdb.setFinalURI();
				this.writeMdBObjectToFile(path, mdb);
				System.out.println("Parsed Mdb " + count + " of " + this.mdbs.size());
				// if(count==50) break;
				wait(500);
//				}
			}
		} else {
			this.readMdbsFromFolder(path);
		}
		
	}
	
	private void loadProperties() {
		this.properties = new Properties();
		BufferedInputStream stream;
		try {
			stream = new BufferedInputStream(new FileInputStream(NebeneinkuenfteApplication.getInstance().getContext()
					.getBaseDirectory()
					+ "/external_data/config.properties"));
//			stream = new BufferedInputStream(new FileInputStream("./WebContent/external_data/config.properties"));
			this.properties.load(stream);
			stream.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void fillMdBList(String URI) {

		try {
			Document doc = Jsoup.connect(URI)
					.userAgent("Screenscraper Abgeordnete: Uni-Leipzig, Institut für Informatik").timeout(20000).get();
			Elements abgeordnetenLinks = doc.getElementsByClass("linkIntern");
			Iterator<Element> it = abgeordnetenLinks.iterator();
			String baseUri = URI;

			while (it.hasNext()) {
				Element mdbElement = it.next();
				String linkHtml = mdbElement.html();
				// extracting the relative URI from the href
				int linkStart = linkHtml.indexOf("\"") + 1;
				int linkEnd = linkHtml.lastIndexOf("\"");
				String relLink = linkHtml.substring(linkStart, linkEnd);
				String mdbUri = makeAbsoluteURI(baseUri, relLink);
				Abgeordneter mdb = new Abgeordneter();
				mdb.setURI(mdbUri);
				mdb.setAttributesFromLinkText(mdbElement.text());
				// this means the MdB isn't part of the Bundestag anymore (*) or
				// died (+), only add valid members
				if (!mdb.getFraktion().contains("*") && !mdb.getFraktion().contains("+")) {
					mdbs.add(mdb);
				}
			}

		} catch (IOException ioe) {
			System.out.println("URL " + URI + " could not be retrieved. Skipping.");

		}

	}

	private String makeAbsoluteURI(String docUri, String relUri) {
		String absUri = null;

		try {
			URL base = new URL(docUri);
			URL full = new URL(base, relUri);
			absUri = full.toString();
		} catch (Exception e) {
			System.out.println("ERROR: URL " + docUri + " " + relUri + "malformed!");
			return null;
		}
		return absUri;
	}

	private boolean parseMdB(Abgeordneter mdb) {

		boolean success = false;

		try {
			Document doc = Jsoup.connect(mdb.getURI())
					.userAgent("Screenscraper Abgeordnete: Uni-Leipzig, Institut für Informatik").timeout(20000).get();
			Elements contextBox = doc.getElementsByClass("contextBox");
			Elements contextChildren = contextBox.select(".standardBox .standardLinkliste .linkExtern");
			if (contextChildren.first() != null) {
				
				String homepage = contextChildren.first().child(0).attr("href");
				mdb.setHomepage(homepage);
			}
			

			Elements mail = doc.getElementsByClass("linkEmail");

			if (mail.first() != null) {
				String email = mail.first().attr("href");
				mdb.setEmail(email);
			}

			Elements wahlkreis = doc.getElementsByClass("linkIntern");

			if (wahlkreis != null) {
				Iterator<Element> wahlkreisIt = wahlkreis.iterator();
				while (wahlkreisIt.hasNext()) {
					Element wahlkreisEl = wahlkreisIt.next();
					if (wahlkreisEl.text().toLowerCase().contains("wahlkreis")) {
						mdb.setWahlkreisUri(this.makeAbsoluteURI(wahlkreisEl.baseUri(),
								wahlkreisEl.child(0).attr("href")));
						mdb.setWahlkreisName(wahlkreisEl.child(0).text());
					}
				}
			}

			Element voa = doc.getElementsByClass("voa").first();
			this.parseNebentaetigkeiten(mdb, voa);
			success = true;

		} catch (IOException ioe) {
			System.out.println("URL " + mdb.getURI() + " could not be retrieved. Skipping.");

		}

		return success;

	}

	public void writeMdBObjectToFile(String path, Abgeordneter mdb) {
		ObjectOutputStream outputStream = null;

		try {

			// Construct the LineNumberReader object
			FileOutputStream fileOut = new FileOutputStream(path + mdb.getLastname() + "_" + mdb.getForename());
			outputStream = new ObjectOutputStream(fileOut);
			outputStream.writeObject(mdb);
			outputStream.flush();

		} catch (FileNotFoundException ex) {
			ex.printStackTrace();
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			// Close the ObjectOutputStream
			try {
				if (outputStream != null) {
					outputStream.flush();
					outputStream.close();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}

	private void readMdbsFromFolder(String path) {
		FileInputStream fis = null;
		ObjectInputStream ois = null;

		if (this.mdbs.size() != 0) {
			this.mdbs.clear();
		}

		try {
			File mdbFolder = new File(path);
			for (File mdbFile : mdbFolder.listFiles()) {
				fis = new FileInputStream(mdbFile);
				ois = new ObjectInputStream(fis);
				Abgeordneter mdb = null;
				while (fis.available() > 0) {
					if ((mdb = (Abgeordneter) ois.readObject()) != null) {
						this.mdbs.add(mdb);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// Close the ObjectOutputStream
			try {
				if (ois != null) {
					ois.close();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}

	}

	public static void wait(int k) {
		long time0, time1;
		time0 = System.currentTimeMillis();
		do {
			time1 = System.currentTimeMillis();
		} while ((time1 - time0) < k);
	}

	private void parseNebentaetigkeiten(Abgeordneter mdb, Element voa) {
		Elements children = voa.select(".standardBox .voa_tab1");
		List<Nebentaetigkeit> nts = new ArrayList<Nebentaetigkeit>();

		// System.out.println(mdb.getLastname());

		for (int i = 0; i < children.size(); i++) {

			Element el = children.get(i);
			String elType = el.nodeName().trim();

			if (el.hasClass("voa_abstand")) {

				String taetigkeitText = "";

				for (int j = i; j < children.size(); j++) {

					Element taetigkeitEl = children.get(j);

					// next taetigkeit begins || next type of taetigkeit || last
					// taetigkeit parsed
					if (taetigkeitEl.hasClass("voa_abstand")) {

						if (j == i) {
							taetigkeitText += taetigkeitEl.text();
						} else if (j > i) {

							nts.addAll(this.parseTaetigkeitenText(taetigkeitText));
							i = j - 1;
							break;
						}

					} else {

						taetigkeitText += "\n" + taetigkeitEl.text();
						if (j == children.size() - 1) {
							nts.addAll(this.parseTaetigkeitenText(taetigkeitText));
						}
					}
				}
			}
		}

		mdb.setNebentaetigkeiten(nts);
		// System.out.println("Mdb " + mdb.getLastname() + " has " + nts.size()
		// + " entgeltliche Nebentaetigkeiten");

	}

	private List<Nebentaetigkeit> parseTaetigkeitenText(String taetigkeitText) {
		
		List<Nebentaetigkeit> nts = new ArrayList<Nebentaetigkeit>();

		String[] lines = taetigkeitText.split("\n");
		String auftragGeber = "";

		boolean semicolon = false;

		Pattern p = Pattern.compile("Stufe (1|2|3)");
		Matcher m = p.matcher(taetigkeitText);

		if (!m.find()) {
			return nts;
		}

		Nebentaetigkeit nt = new Nebentaetigkeit();
		nt.setJobStart(this.from);
		nt.setJobEnd(this.to);
		String source = "";
		boolean parsed = false;
		for (int i = 0; i < lines.length; i++) {
			
			if (!lines[i].toLowerCase().contains("stufe 1") && !lines[i].toLowerCase().contains("stufe 2")
					&& !lines[i].toLowerCase().contains("stufe 3")) {

				auftragGeber += lines[i] + " ";
				source += lines[i] + " ";
				if (parsed == true) {
					System.out.println("Lines after stufe parsed: " + lines[i]);
					int[] end = this.findEndOfJob(lines[i]);
					if(end != null) {
						if(end[0]!=-1 && end[1]!=-1) {
							nt.setJobEnd(end);
						}
					}
					int[] start = this.findStartOfJob(lines[i]);
					if(start != null) {
						if(start[0]!=-1 && start[1]!=-1) {
							nt.setJobStart(start);
						}
					}
//					System.out.println(nt.getJobStart()[0]+" "+nt.getJobStart()[1]+" bis "+nt.getJobEnd()[0]+" "+nt.getJobEnd()[1]);
//					System.out.println();
				}

			} else {
				source += lines[i] + " ";
				nt = new Nebentaetigkeit();
				nt.setJobStart(this.from);
				nt.setJobEnd(this.to);
				if (lines.length == 1) {
					this.parseAuftraggeber(nt, taetigkeitText.trim());
				} else {
					this.parseAuftraggeber(nt, auftragGeber);
				}
				
				//find start and end dates
				int[] end = this.findEndOfJob(lines[i]);
				if(end != null) {
					if(end[0]!=-1 && end[1]!=-1) {
						nt.setJobEnd(end);
					}
				}
				int[] start = this.findStartOfJob(lines[i]);
				if(start != null) {
					if(start[0]!=-1 && start[1]!=-1) {
						nt.setJobStart(start);
					}
				}
				
				// possibility of multiple jobs in one line
				if (lines[i].contains(";")) {

					String[] semiSplit = lines[i].split(";");
					for (int j = 0; j < semiSplit.length; j++) {

						Nebentaetigkeit temp = this.parseInfoOfTaetigkeit(nt, semiSplit[j]);
						if(temp == null) {
							continue;
						}
						// if there is no type and the auftraggeber of both
						// taetigkeiten are identical, use the type of the
						// former taetigkeit
						if (temp.getType() == null) {
							if (nts.size() > 0) {
								if (nts.get(nts.size() - 1).getAuftraggeber().equals(temp.getAuftraggeber())) {
									temp.setType(nts.get(nts.size() - 1).getType());
								}
							}
						}

						nts.add(temp);
					}
				} else {
					Nebentaetigkeit temp = this.parseInfoOfTaetigkeit(nt, lines[i]);
					if(temp == null) {
						continue;
					}
					// if there is no type and the auftraggeber of both
					// taetigkeiten are identical, use the type of the former
					// taetigkeit
					if (temp.getType() == null) {
						if (nts.size() > 0) {
							if (nts.get(nts.size() - 1).getAuftraggeber().equals(temp.getAuftraggeber())) {
								temp.setType(nts.get(nts.size() - 1).getType());
							}
						}
					}

					nts.add(temp);
				}

				parsed = true;

			}
		}

		return nts;
	}

	private void writeNebentaetigkeitenToFile(List<Abgeordneter> mdbs) {
		BufferedWriter out = null;

		try {

			FileWriter fileOut = new FileWriter("nebentaetigkeiten");
			out = new BufferedWriter(fileOut);
			int minGesamt = 0;
			int maxGesamt = 0;
			int overMax = 0;
			int totalNoJobCount = 0;
			int totalJobCount = 0;
			int cdu = 0;
			int fdp = 0;
			int grün = 0;
			int links = 0;
			int spd = 0;
			for (Abgeordneter mdb : mdbs) {
				if(mdb.getFraktion().toLowerCase().contains("cdu")) {
					cdu+=mdb.getMinZusatzeinkommen();
				} else if(mdb.getFraktion().toLowerCase().contains("fdp")) {
					fdp+=mdb.getMinZusatzeinkommen();
				} else if(mdb.getFraktion().toLowerCase().contains("grün")) {
					grün+=mdb.getMinZusatzeinkommen();
				} else if(mdb.getFraktion().toLowerCase().contains("link")) {
					links+=mdb.getMinZusatzeinkommen();
				} else if(mdb.getFraktion().toLowerCase().contains("spd")) {
					spd+=mdb.getMinZusatzeinkommen();
				}
				if (mdb.getAnzahlNebeneinkuenfte() != 0) {
					totalJobCount++;
					out.write(mdb.getForename() + " " + mdb.getLastname() + " Partei:" + mdb.getFraktion());

					out.write(" Anzahl Tätigkeiten: " + mdb.getAnzahlNebeneinkuenfte() + " min: "
							+ mdb.getMinZusatzeinkommen() + " max " + mdb.getMaxZusatzeinkommen());
					out.newLine();
					out.write("URI: "+mdb.getURI());
					out.newLine();
					out.write("FraktionURI: "+mdb.getFraktionUri());
					out.newLine();
					out.write("Wahlkreis: "+mdb.getWahlkreisName());
					out.newLine();
					out.newLine();
					out.write("______Nebentätigkeiten:______\n");
					out.newLine();
					out.flush();
					for (Nebentaetigkeit neben : mdb.getNebentaetigkeiten()) {
						int months = -1;
						
						if(neben.getJobStart()!=null && neben.getJobEnd()!=null) {
							int startYear = neben.getJobStart()[1];
							int endYear = neben.getJobEnd()[1];
							int startMonth = neben.getJobStart()[0];
							int endMonth = neben.getJobEnd()[0];
							out.write("startmonth:"+startMonth+" startyear:"+startYear+" endmonth:"+endMonth+" endYear:"+endYear);
							out.newLine();
							months = (endYear-startYear)*12 + (endMonth - startMonth) + 1;
						} else {
							out.write("HULLABULLA");
						}
						
						out.write("Auftraggeber: " + neben.getAuftraggeber() + "\nUri: " + neben.getAuftragUri()
								+ "\nOrt: " + neben.getPlace() + "\nOrt Uri: " + neben.getPlaceUri());
						out.newLine();
						out.write("Lat: " + neben.getLatitude() + " Long: " + neben.getLongitude());
						out.newLine();
						out.write("Typ: " + neben.getType() + "\nJahr: " + neben.getYear());
						out.newLine();
						out.write("Stufe: " + neben.getStufe());
						out.newLine();
						out.write("Jährlich: " + neben.isYearly() + "\nMonatlich: " + neben.isMonthly());
						out.newLine();
						out.write("Dauer: " + months);
						if (neben.getSourceString() != null) {
							out.write(neben.getSourceString());
							out.newLine();
						}
						out.newLine();
						out.flush();
					}
					out.write("___________________________________________________\n");
					out.newLine();
					out.flush();
					minGesamt += mdb.getMinZusatzeinkommen();
					if (mdb.getMaxZusatzeinkommen() != 2500000) {
						maxGesamt += mdb.getMaxZusatzeinkommen();
					} else {
						overMax++;
					}
				} else {
					totalNoJobCount++;
				}

			}
			out.write("cdu: " + cdu +" fdp: "+fdp+" spd: "+spd+" grüne: "+grün+" linke: "+links);
			out.newLine();
			out.write(mdbs.size() + " Abgeordnete, Einkünfte min gesamt: " + minGesamt + " max gesamt: " + maxGesamt);
			out.newLine();
			out.write(overMax + " Abgeordnete haben mindestens eine Tätigkeit über 7000 Euro.");
			out.newLine();
			out.write(totalJobCount + " Abgeordnete haben mindestens eine Nebentätigkeit.");
			out.newLine();
			out.write(totalNoJobCount + " Abgeordnete haben keine Nebentätigkeit.");

		} catch (FileNotFoundException ex) {
			ex.printStackTrace();
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			// Close the ObjectOutputStream
			try {
				if (out != null) {
					out.flush();
					out.close();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}

	}
	
//	private int getMonthsOnJobNumber(Nebentaetigkeit nt) {
//		int months = 0;
//		int startYear = nt.getJobStart()[1];
//		int endYear = nt.getJobEnd()[1];
//		int startMonth = nt.getJobStart()[0];
//		int endMonth = nt.getJobEnd()[0];
//		months = (endYear-startYear)*12 + (endMonth - startMonth) + 1;
//		return months;
//	}
//	
//	private int getYearsOnJobNumber(Nebentaetigkeit nt) {
//		int years = 0;
//		int startYear = nt.getJobStart()[1];
//		int endYear = nt.getJobEnd()[1];
//		years = endYear-startYear;
//		return years;
//	}
//	
//	private void writeAsCSV(List<Abgeordneter> mdbs) {
//		BufferedWriter out = null;
//
//		try {
//
//			FileWriter fileOut = new FileWriter("nebentaetigkeiten.csv");
//			out = new BufferedWriter(fileOut);
//
//			out.write("Name,Fraktion,monatlichStufe0,monatlichStufe1,monatlichStufe2,monatlichStufe3," +
//					"jährlichStufe0,jährlichStufe1,jährlichStufe2,jährlichStufe3," +
//					"einmaligStufe0,einmaligStufe1,einmaligStufe2,einmaligStufe3," +
//					"monatlichBundStufe0,monatlichBundStufe1,monatlichBundStufe2,monatlichBundStufe3," +
//					"jährlichBundStufe0,jährlichBundStufe1,jährlichBundStufe2,jährlichBundStufe3,minimumGesamt,minimumBund\n");
//			for (Abgeordneter mdb : mdbs) {
//
//					out.write(mdb.getForename() + " " + mdb.getLastname() + "," + mdb.getFraktion()+",");
//					int[] monatlich = new int[4];
//					monatlich[0] = 0;
//					monatlich[1] = 0;
//					monatlich[2] = 0;
//					monatlich[3] = 0;
//					int[] jaehrlich = new int[4];
//					jaehrlich[0] = 0;
//					jaehrlich[1] = 0;
//					jaehrlich[2] = 0;
//					jaehrlich[3] = 0;
//					int[] onetime = new int[4];
//					onetime[0] = 0;
//					onetime[1] = 0;
//					onetime[2] = 0;
//					onetime[3] = 0;
//					int[] monatlichBund = new int[4];
//					monatlichBund[0] = 0;
//					monatlichBund[1] = 0;
//					monatlichBund[2] = 0;
//					monatlichBund[3] = 0;
//					int[] jaehrlichBund = new int[4];
//					jaehrlichBund[0] = 0;
//					jaehrlichBund[1] = 0;
//					jaehrlichBund[2] = 0;
//					jaehrlichBund[3] = 0;
//					
//					for (Nebentaetigkeit neben : mdb.getNebentaetigkeiten()) {
//						if(neben.isMonthly()) {
//							int stufe = 0;
//							if(neben.getStufe()!=null) {
//								stufe = Integer.parseInt(neben.getStufe().split(" ")[1]);
//							}
//							if(neben.isGouvernmentJob()) {
//								monatlichBund[stufe]++;
//							} else {
//								monatlich[stufe]++;
//							}
//						} else if(neben.isYearly()) {
//							int stufe = 0;
//							if(neben.getStufe()!=null) {
//								stufe = Integer.parseInt(neben.getStufe().split(" ")[1]);
//							}
//							if(neben.isGouvernmentJob()) {
//								jaehrlichBund[stufe]++;
//							} else {
//								jaehrlich[stufe]++;
//							}
//						} else {
//							int stufe = 0;
//							if(neben.getStufe()!=null) {
//								stufe = Integer.parseInt(neben.getStufe().split(" ")[1]);
//							}
//							
//							onetime[stufe]++;
//						}
//						
//					}
//					
//					int minZusatzBund = 0;
//					
//					for (Nebentaetigkeit nt : mdb.getNebentaetigkeiten()) {
//						if(nt.isGouvernmentJob()) {
//						
//							if (nt.getStufe().contains("1")) {
//								if (nt.isMonthly()) {
//									minZusatzBund += this.getMonthsOnJobNumber(nt)*1000;
//									
//								} else if (nt.isYearly()) {
//									minZusatzBund += this.getYearsOnJobNumber(nt)*1000;
//								
//								} else {
//									minZusatzBund += 1000;
//									
//								}
//							} else if (nt.getStufe().contains("2")) {
//								if (nt.isMonthly()) {
//									minZusatzBund += this.getMonthsOnJobNumber(nt)*3501;
//									
//								} else if (nt.isYearly()) {
//									minZusatzBund += this.getYearsOnJobNumber(nt)*3501;
//									
//								} else {
//									minZusatzBund += 3501;
//								
//								}
//							} else if (nt.getStufe().contains("3")) {
//								if (nt.isMonthly()) {
//									minZusatzBund += this.getMonthsOnJobNumber(nt)*7001;
//								} else if (nt.isYearly()) {
//									minZusatzBund += this.getYearsOnJobNumber(nt)*7001;
//								} else {
//									minZusatzBund += 7001;
//								}
//							
//							}
//						}
//					}
//					
//					
//					out.write(monatlich[0]+","+monatlich[1]+","+monatlich[2]+","+monatlich[3]+","+
//							jaehrlich[0]+","+jaehrlich[1]+","+jaehrlich[2]+","+jaehrlich[3]+","+
//							onetime[0]+","+onetime[1]+","+onetime[2]+","+onetime[3]+","+
//							monatlichBund[0]+","+monatlichBund[1]+","+monatlichBund[2]+","+monatlichBund[3]+","+
//							jaehrlichBund[0]+","+jaehrlichBund[1]+","+jaehrlichBund[2]+","+jaehrlichBund[3]+","+mdb.getMinZusatzeinkommen()+","+minZusatzBund);
//					out.newLine();
//					out.flush();
//					
//				}
//
//
//		} catch (FileNotFoundException ex) {
//			ex.printStackTrace();
//		} catch (IOException ex) {
//			ex.printStackTrace();
//		} finally {
//			// Close the ObjectOutputStream
//			try {
//				if (out != null) {
//					out.flush();
//					out.close();
//				}
//			} catch (IOException ex) {
//				ex.printStackTrace();
//			}
//		}
//
//	}

	private Nebentaetigkeit parseInfoOfTaetigkeit(Nebentaetigkeit nt, String text) {
		
		//System.out.println("Parsing info of taetigkeit: " + text);
		// copying values of Nebentaetigkeit in parameter, because there could
		// be more than one with the same auftraggeber
		Nebentaetigkeit taetigkeitFound = new Nebentaetigkeit(nt);
		boolean intervalMonthly = false;
		boolean intervalYearly = false;
		boolean gouvernmentJob = false;
		String[] split = text.split(",");
		for (int i = 0; i < split.length; i++) {

			String trimmed = split[i].replace(String.valueOf((char) 160), " ").trim();

			if (trimmed == null)
				continue;

			if (trimmed.contains("Stufe")) {
				taetigkeitFound.setStufe(trimmed);
				// a year
			} else if (trimmed.matches("(1|2)[0-9]{3}")) {
				taetigkeitFound.setYear(trimmed);
				// this is buggy
			} else if (trimmed.contains("monatlich")) {
				intervalMonthly = true;
			} else if (trimmed.contains("jährlich")) {
				intervalYearly = true;
			} else if (trimmed.contains("Bundes")) {
				gouvernmentJob = true;
				taetigkeitFound.appendType(trimmed);
			} else if (isCity(trimmed)) {
				taetigkeitFound.setPlace(trimmed);
			} else {
				taetigkeitFound.appendType(trimmed);
				if (!trimmed.toLowerCase().equals("gewinn")) {
					nt.appendType(trimmed);
				}

			}
		}

		taetigkeitFound.setMonthly(intervalMonthly);
		taetigkeitFound.setYearly(intervalYearly);
		taetigkeitFound.setGouvernmentJob(gouvernmentJob);
		return taetigkeitFound;

	}

	private void parseAuftraggeber(Nebentaetigkeit nt, String text) {

		// get rid of "," at the end of the string
		if (text.trim().lastIndexOf(",") == text.length() - 2) {
			text = text.substring(0, text.length() - 2);
		}
		String[] split = text.split(",");

		boolean cityFound = false;

		for (int i = 0; i < split.length; i++) {

			String trimmed = split[i].trim();
			if (cityFound) {
				if (!trimmed.toLowerCase().contains("monatlich") && !trimmed.toLowerCase().contains("jährlich")
						&& !trimmed.toLowerCase().contains("stufe")) {
					nt.appendType(trimmed);
				}
				continue;

			}

			if (!isCity(trimmed)) {
				nt.appendAuftraggeber(trimmed);
			} else {
				nt.setPlace(trimmed);
				cityFound = true;
				continue;
			}

		}

	}

	public boolean isCity(String cityname) {

		return this.cities.contains(cityname);
	}

	// fix this!

	public List<String[]> getGermanCityNames() {

		List<String[]> places = new ArrayList<String[]>();

		String queryString = "";
		String endpoint = "http://dbpedia.org/sparql";

		queryString += "PREFIX geo: <http://www.w3.org/2003/01/geo/wgs84_pos#> PREFIX dbprop: <http://dbpedia.org/property/> PREFIX dbowl: <http://dbpedia.org/ontology/> PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>"
				+ "SELECT *  WHERE {?s a dbowl:PopulatedPlace; dbowl:country <http://dbpedia.org/resource/Germany>; rdfs:label ?o; geo:lat ?lat; geo:long ?long. FILTER(langMatches(lang(?o),'DE')).}";

		Query query = QueryFactory.create(queryString);
		QueryExecution qe = QueryExecutionFactory.sparqlService(endpoint, query);

		System.out.println("Querying DBPedia for German cities.");
		try {
			ResultSet rs = qe.execSelect();
			while (rs.hasNext()) {
				QuerySolution sol = rs.next();
				String[] placeValues = new String[4];
				// name
				placeValues[0] = sol.get("o").asLiteral().getString();
				// uri
				placeValues[1] = sol.get("s").toString();
				placeValues[2] = sol.get("lat").asLiteral().getString();
				placeValues[3] = sol.get("long").asLiteral().getString();
				places.add(placeValues);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("Done.");
		return places;

	}

	public void setSinglePlaceUri(List<String[]> places, Nebentaetigkeit nt) {
		String place = nt.getPlace();

		if (place != null) {

			// standard case: german city, place string matches city name
			for (String[] dbpPlace : places) {

				if (place.trim().equals(dbpPlace[0].trim())) {
					nt.setPlaceUri(dbpPlace[1]);
					if(dbpPlace[2]!="NAN" && dbpPlace[3]!="NAN" ) {
						nt.setLatitude(Float.parseFloat(dbpPlace[2]));
						nt.setLongitude(Float.parseFloat(dbpPlace[3]));
					}
					return;
				}
			}
			// irregular case: place string may be contained in on of the city
			// names
			for (String[] dbpPlace : places) {
				if (dbpPlace[0].trim().contains(place)) {
					nt.setPlaceUri(dbpPlace[1]);
					if(dbpPlace[2]!="NAN" && dbpPlace[3]!="NAN" ) {
						nt.setLatitude(Float.parseFloat(dbpPlace[2]));
						nt.setLongitude(Float.parseFloat(dbpPlace[3]));
					}
					return;
				}
			}
			// irregular case 2: place string contains non word chars like "/".
			// place string is splittet at this char and all parts are compared
			// to city name
			for (String[] dbpPlace : places) {
				String[] placeparts = place.split("\\W");
				boolean fits = true;
				for (int i = 0; i < placeparts.length; i++) {
					if (!dbpPlace[0].trim().contains(placeparts[i].trim())) {
						fits = false;
					}
				}
				if (fits) {
					nt.setPlaceUri(dbpPlace[1]);
					if(dbpPlace[2]!="NAN" && dbpPlace[3]!="NAN" ) {
						nt.setLatitude(Float.parseFloat(dbpPlace[2]));
						nt.setLongitude(Float.parseFloat(dbpPlace[3]));
					}
					return;
				}
			}
		}

		System.out.println("place not found: " + place);
	}

	public List<Abgeordneter> setAllPlaceUris(List<Abgeordneter> mdbs, List<String[]> places) {

		for (Abgeordneter mdb : mdbs) {

			if (mdb.getNebentaetigkeiten().size() > 0) {
				System.out.println("Matching places for: " + mdb.getForename() + " " + mdb.getLastname());
				for (Nebentaetigkeit neben : mdb.getNebentaetigkeiten()) {
					setSinglePlaceUri(places, neben);
				}
			}
		}

		return mdbs;
	}

	/*
	 * here be dragons
	 */

	private void changeIngosHomepage() {

		out: {
			for (Abgeordneter mdb : mdbs)
				if (mdb.getURI().equals(IConstants.NAMESPACE.concat("/mdb/gaedechens_ingo"))) {
					mdb.setHomepage("http://www.xn--ingo-gdechens-gfb.de/");
					writeMdBObjectToFile("./WebContent/abgeordnete/", mdb);
					break out;
				}
		}
	}

	private List<Abgeordneter> setAllSourceUris(List<Abgeordneter> mdbs) {

		/*
		 * merge all different captions for one URI and put result to map
		 */
		Map<String, String> captionMap = new HashMap<String, String>();

		for (Abgeordneter mdb : mdbs) {
			for (Nebentaetigkeit nt : mdb.getNebentaetigkeiten()) {
				captionMap.put(nt.getAuftragUri(), nt.getAuftraggeber());
			}
		}

		/*
		 * set new URI, homepage and caption for each sideline job
		 */

		String actualURI;
		Map<String, Integer> indexMap = new HashMap<String, Integer>();

		// indexID for resources without an URI for the source
		// index count is higher than captionMapsize to avoid conflicts
		int unKnownIndexID = captionMap.size() + 1;
		int knownIndexID = 0;

		for (Abgeordneter mdb : mdbs) {
			for (Nebentaetigkeit nt : mdb.getNebentaetigkeiten()) {

				actualURI = "";
				actualURI = actualURI.concat(IConstants.NAMESPACE);
				actualURI = actualURI.concat("/");
				actualURI = actualURI.concat(IConstants.PERSON_ORIGIN_VIEW_FRAG);
				actualURI = actualURI.concat("/");

				// no URI was found for source URI
				if (nt.getAuftragUri() == null) {

					actualURI = actualURI.concat(String.valueOf(unKnownIndexID++));
					nt.setAuftragUri(actualURI);
					nt.setAuftraggeber("unbekannt");
					continue;

				}

				// source URI is not in the map so use indexID and increment it
				if (indexMap.get(nt.getAuftragUri()) == null) {

					indexMap.put(nt.getAuftragUri(), knownIndexID);
					actualURI = actualURI.concat(String.valueOf(knownIndexID++));

				}

				// source URI is already in the map so get and set the according
				// indexID
				else {
					actualURI = actualURI.concat(String.valueOf(indexMap.get(nt.getAuftragUri())));
				}

				// set values
				nt.setAuftraggeberHomepage(nt.getAuftragUri());
				nt.setAuftraggeber(captionMap.get(nt.getAuftragUri()));
				nt.setAuftragUri(actualURI);

			}
		}

		return mdbs;

	}

	// private String formatStringForUnicode(String city) {
	//
	// city = city.replaceAll("ü", "\u00FC");
	// city = city.replaceAll("Ü", "\u00DC");
	// city = city.replaceAll("ä", "\u00E4");
	// city = city.replaceAll("Ä", "\u00C4");
	// city = city.replaceAll("ö", "\u00F6");
	// city = city.replaceAll("Ö", "\u00D6");
	// city = city.replaceAll("ß", "\u00df");
	// city = city.replaceAll(" ", "_");
	// return city;
	// }
	//
	// private String formatStringForUTF8(String city) {
	//
	// city = city.replaceAll("ü", "%C3%BC");
	// city = city.replaceAll("Ü", "%C3%9C");
	// city = city.replaceAll("ä", "%C3%A4");
	// city = city.replaceAll("Ä", "%C3%84");
	// city = city.replaceAll("ö", "%C3%B6");
	// city = city.replaceAll("Ö", "%C3%96");
	// city = city.replaceAll("ß", "%C3%9F");
	// city = city.replaceAll(" ", "_");
	// return city;
	// }

	public String matchSingleAuftraggeber(String auftrag, String ort, Abgeordneter mdb) {

		String auftraggeber = "";

		try {

			String encodedAuftrag = URLEncoder.encode(auftrag, "UTF-8");
			String encodedOrt = "";
			if (ort != null) {
				encodedOrt = URLEncoder.encode(ort, "UTF-8");
			} else {
				encodedOrt = "";
			}

			// Document doc = Jsoup
			// .connect("http://www.bing.com/search?q="+encodedAuftrag+"+"+encodedOrt)
			// .userAgent("Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:17.0) Gecko/20100101 Firefox/17.0")
			// .timeout(20000)
			// .get();
			URL url = null;
			if (auftrag.toLowerCase().contains("selbständig") || auftrag.toLowerCase().contains("rechtsanw")
					|| auftrag.toLowerCase().contains("landwirt")) {
				url = new URL("http://www.bing.com/search?q=" + encodedAuftrag + "+" + encodedOrt + "+"
						+ URLEncoder.encode(mdb.getForename(), "UTF-8") + "+"
						+ URLEncoder.encode(mdb.getLastname(), "UTF-8"));
			} else {
				url = new URL("http://www.bing.com/search?q=" + encodedAuftrag + "+" + encodedOrt);
			}

			// connecting via proxy, saving the html to a string, parsing it
			// with jsoup
			Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("202.171.253.108", 83));
			HttpURLConnection uc = (HttpURLConnection) url.openConnection(proxy);
			uc.setConnectTimeout(20000);
			uc.connect();
			String line = null;
			StringBuffer tmp = new StringBuffer();
			BufferedReader in = new BufferedReader(new InputStreamReader(uc.getInputStream()));

			while ((line = in.readLine()) != null) {
				tmp.append(line);
			}

			Document doc = Jsoup.parse(String.valueOf(tmp));

			Element searchResults = doc.getElementById("results");
			Element firstHit = searchResults.select("div.sb_tlst h3 a").first();
			// System.out.println(firstHit.html());
			if (firstHit != null) {
				return firstHit.attr("href");
			} else {
				System.out.println(url);
			}

		} catch (ConnectException conex) {
			System.out.println("failed (ConnectException), trying again");
			matchSingleAuftraggeber(auftrag, ort, mdb);
		} catch (SocketTimeoutException ste) {
			System.out.println("failed (SocketTimeoutException), trying again");
			matchSingleAuftraggeber(auftrag, ort, mdb);
		} catch (IOException ioe) {
		
			System.out.println("Failed to match " + auftrag);
			matchSingleAuftraggeber(auftrag, ort, mdb);
			
		} catch (UnsupportedCharsetException uce) {
			System.out.println("Charset UTF-8 not supported by System");
		} catch(Exception e) {
			matchSingleAuftraggeber(auftrag, ort, mdb);
		}

		return auftraggeber;
	}

	public List<Abgeordneter> matchAllAuftraggeber(List<Abgeordneter> mdbs) {

		int count = 0;
		int total = mdbs.size();
		for (Abgeordneter mdb : mdbs) {
			
			if (mdb.getNebentaetigkeiten().size() > 0) {
				count++;
				// if(count>50) {
				System.out.println("Matching Auftraggeber " + count + " of " + total +" "+ mdb.getLastname());
				String tempAuftrag = "";
				String tempUri = "";
				int taetigkeiten = 0;
				int totaltaet = mdb.getNebentaetigkeiten().size();
				for (Nebentaetigkeit neben : mdb.getNebentaetigkeiten()) {
					taetigkeiten++;
					System.out.println("\tNebentätigkeit " + taetigkeiten + " of " + totaltaet);
					String auftragUri = "";

					if (!tempAuftrag.equals(neben.getAuftraggeber())) {

						auftragUri = matchSingleAuftraggeber(neben.getAuftraggeber(), neben.getPlace(), mdb);
						tempAuftrag = neben.getAuftraggeber();
						tempUri = auftragUri;
						wait(500);
					} else {
						auftragUri = tempUri;
					}

					if (auftragUri.length() != 0)
						neben.setAuftragUri(auftragUri);
				}
				// }
			}
		}

		return mdbs;

	}

	public List<Abgeordneter> getAbgeordnete() {
		return this.mdbs;
	}

	private List<String> readCityFile(String path) {
		List<String> cities = new ArrayList<String>();
		try {

			FileInputStream fstream = new FileInputStream(path);

			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String strLine;

			while ((strLine = br.readLine()) != null) {
				cities.add(strLine.trim());
			}
			// Close the input stream
			in.close();
		} catch (Exception e) {// Catch exception if any
			System.err.println("Error: " + e.getMessage());
		}
		return cities;
	}
	
	private int[] findEndOfJob(String jobDescription) {
		int[] dateArray = null;
		Matcher matcher = Pattern.compile( "bis(.| )*?(1|2)[0-9]{3}" ).matcher( jobDescription );
		String date = null;
		if(matcher.find()) {
			date = matcher.group();
		} else {
			return null;
		}

		dateArray = this.parseDateString(date.trim());
		
		return dateArray;
	}
	
	private int[] findStartOfJob(String jobDescription) {
		int[] dateArray = null;
		Matcher matcher = Pattern.compile( "(von|vom|seit)(.| )*?(1|2)[0-9]{3}" ).matcher( jobDescription );
		String date = null;
		if(matcher.find()) {
			date = matcher.group();
		} else {
			return null;
		}

		dateArray = this.parseDateString(date.trim());
		return dateArray;
	}
	
	private int[] parseDateString(String date) {
		int[] dateArray = new int[2];
		dateArray[0] = -1;
		dateArray[1] = -1;
		Matcher matcher = Pattern.compile( "[0-9]{2}\\.[0-9]{2}\\.(1|2)[0-9]{3}" ).matcher( date );
		//if normal date format dd.mm.yyyy
		if(matcher.find()) {
			date = matcher.group();
			String[] dateSplit = date.split("\\.");
			//only use month and year
			dateArray[0] = Integer.parseInt(dateSplit[1]);
			dateArray[1] = Integer.parseInt(dateSplit[2]);
			
			return dateArray;
		//if format is dd. Month yyyy or Month yyyy or yyyy
		} else {
			//Month yyyy
			if(date.matches(".*? [0-9]{4}")) {
				date = date.toLowerCase();
				List<String> months = Arrays.asList("januar", "februar", "märz", "april", "mai", "juni","juli","august","september","oktober","november","dezember");

				for(int i = 0; i < months.size(); i++) {
					String month = months.get(i);

					if(date.contains(month)) {
						dateArray[0] = i+1;
					} 
				}

				matcher = Pattern.compile( "(1|2)[0-9]{3}" ).matcher( date );
				if(matcher.find()) {
					dateArray[1] = Integer.parseInt(matcher.group());
				} else {
					return null;
				}
				//no month found, only a year given. decrement year, set month to 12
				if(dateArray[0]==-1) {
					dateArray[0] = 12;
					dateArray[1]--;
				}
						
			}	
		}
		
		return dateArray;
	}

	public static void main(String[] args) throws UnsupportedEncodingException {

		/*
		 * crawl all parliament members
		 */
		BundestagConverter conv = new BundestagConverter(
				"http://www.bundestag.de/bundestag/abgeordnete17/alphabet/index.html", true);
		List<Abgeordneter> mdbs = conv.getAbgeordnete();

		/*
		 * try to find latitude and longitude data for given citys
		 */

//		List<String[]> places = conv.getGermanCityNames();
//		mdbs = conv.setAllPlaceUris(mdbs, places);
//		for (Abgeordneter mdb : mdbs) {
//			conv.writeMdBObjectToFile("./WebContent/abgeordnete/", mdb);
//		}
		
		//conv.writeAsCSV(mdbs);

		/*
		 * try to match sources
		 */
//
//		mdbs = conv.matchAllAuftraggeber(mdbs);
//		for (Abgeordneter mdb : mdbs) {
//			conv.writeMdBObjectToFile("./WebContent/abgeordnete/", mdb);
//			
//		}
		//conv.writeNebentaetigkeitenToFile(mdbs);
		/*
		 * try to set source URIs
		 */

//		mdbs = conv.setAllSourceUris(mdbs);

		/*
		 * try to change homepage of Ingo Gädechens
		 */

//		conv.changeIngosHomepage();
//		for (Abgeordneter mdb : mdbs) {
//			mdb.setFraktion(mdb.getFraktion());
//			conv.writeMdBObjectToFile("./WebContent/abgeordnete/", mdb);
////			
//		}
		System.out.println("Finished data crawling");
	}
}
