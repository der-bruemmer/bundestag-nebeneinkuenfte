package de.ulei.nebeneinkuenfte.crawler;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


public class BundestagConverter {
	
	String startUri = null;
	List<Abgeordneter> mdbs = new ArrayList<Abgeordneter>();
	
	/**
	 * Konstruktor
	 * @param URI
	 * @param scrape Scrapen oder vom lokalen caching laden
	 */
	
	public BundestagConverter(String URI, boolean scrape) {
		this.startUri = URI;
		if(scrape) {
			this.fillMdBList(URI);
			int count = 0;
			for(Abgeordneter mdb : this.mdbs) {
				count++;
				if(this.parseMdB(mdb)!=null) {
					//parse failed
					System.out.println("Trying again.");
					wait(2);
					this.parseMdB(mdb);
				}
				
				this.writeMdBObjectToFile(mdb);
				System.out.println("Parsed Mdb " + count + " of " + this.mdbs.size());
				wait(2);
			}
		} else {
			this.readMdbsFromFolder();
		}
	}
	
		 
	private void fillMdBList(String URI) {

		try {
			Document doc = Jsoup.connect(URI)
					.userAgent("Screenscraper Abgeordnete: Uni-Leipzig, Institut für Informatik")
					.get();
			Elements abgeordnetenLinks = doc.getElementsByClass("linkIntern");
			Iterator<Element> it = abgeordnetenLinks.iterator();
			String baseUri = URI;

			while(it.hasNext()) {
				Element mdbElement = it.next();
				String linkHtml = mdbElement.html();
				//extracting the relative URI from the href
				int linkStart = linkHtml.indexOf("\"")+1;
				int linkEnd = linkHtml.lastIndexOf("\"");
				String relLink = linkHtml.substring(linkStart,linkEnd);
				String mdbUri = makeAbsoluteURI(baseUri, relLink);
				Abgeordneter mdb = new Abgeordneter();
				mdb.setURI(mdbUri);
				mdb.setAttributesFromLinkText(mdbElement.text());
				//this means the MdB isn't part of the Bundestag anymore (*) or died (+), only add valid members
				if(!mdb.getFraktion().contains("*") && !mdb.getFraktion().contains("+")) {
					mdbs.add(mdb);
				}
			}			
			
		} catch(IOException ioe) {
			System.out.println("URL " + URI + " could not be retrieved. Skipping.");

		}

	}
	
	private String makeAbsoluteURI(String docUri, String relUri) {
		String absUri = null;
		
		try {
			URL base = new URL(docUri);
			URL full = new URL(base, relUri);
			absUri = full.toString();
		} catch(Exception e) {
			System.out.println("ERROR: URL " + docUri + " " + relUri + "malformed!");
			return null;
		}
		return absUri;
	}
	
	
	private String parseMdB(Abgeordneter mdb) {
		
		String failUri = null;
		
		try {
			Document doc = Jsoup.connect(mdb.getURI())
							.userAgent("Screenscraper Abgeordnete: Uni-Leipzig, Institut für Informatik")
							.get();
			Elements externeLinks = doc.getElementsByClass("linkExtern");
			//Element kontakt = doc.getElementsByClass("standardBox").html();
			
			if(externeLinks.first() != null) {
				String homepage = doc.getElementsByClass("linkExtern").first().child(0).attr("href");
				mdb.setHomepage(homepage);
			}
			
			Element voa = doc.getElementsByClass("voa").first();
			this.parseNebentaetigkeiten(mdb, voa);
			
		} catch(IOException ioe) {
			System.out.println("URL " + mdb.getURI() + " could not be retrieved. Skipping.");
			failUri = mdb.getURI();
		}
		
		return failUri;
		
	}
	
	
	private void writeMdBObjectToFile(Abgeordneter mdb) {
		ObjectOutputStream outputStream = null;
        
        try {
            
            //Construct the LineNumberReader object
        	FileOutputStream fileOut = new FileOutputStream("./abgeordnete/"+mdb.getLastname()+"_"+mdb.getForename());
            outputStream = new ObjectOutputStream(fileOut);
            outputStream.writeObject(mdb);
            outputStream.flush();
      
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            //Close the ObjectOutputStream
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
	
	private void readMdbsFromList() {
		FileInputStream fis = null;
		ObjectInputStream ois = null;
		
		if(this.mdbs.size()!=0) {
			this.mdbs.clear();
		}
		
		try {
			fis = new FileInputStream("../abgeordnete");
			ois = new ObjectInputStream(fis);
			Abgeordneter mdb = null;
			while(fis.available()>0) {
				if((mdb = (Abgeordneter) ois.readObject())!= null) {
					this.mdbs.add(mdb);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
            //Close the ObjectOutputStream
            try {
                if (ois != null) {
                    ois.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
		
	}
	
	private void readMdbsFromFolder() {
		FileInputStream fis = null;
		ObjectInputStream ois = null;
		
		if(this.mdbs.size()!=0) {
			this.mdbs.clear();
		}
		
		try {
			File mdbFolder = new File("/home/sip/projects/bundestag-nebeneinkuenfte/abgeordnete");
			for(File mdbFile : mdbFolder.listFiles()) {
				fis = new FileInputStream(mdbFile);
				ois = new ObjectInputStream(fis);			
				Abgeordneter mdb = null;
				while(fis.available()>0) {
					if((mdb = (Abgeordneter) ois.readObject())!= null) {
						this.mdbs.add(mdb);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
            //Close the ObjectOutputStream
            try {
                if (ois != null) {
                    ois.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
		
	}
	
	public static void wait (int k){
		long time0, time1;
		time0 = System.currentTimeMillis();
		do{
		time1 = System.currentTimeMillis();
		}
		while ((time1 - time0) < k * 1000);
	}
	
	
	private void parseNebentaetigkeiten(Abgeordneter mdb, Element voa) {
		Elements children = voa.select(".standardBox").first().children();
		List<Nebentaetigkeit> nts = new ArrayList<Nebentaetigkeit>();
		
		for(int i = 0; i < children.size(); i++) {
			Element el = children.get(i);
			String elType = el.nodeName().trim();
			//this is the type of the nebentaetigkeit
			if(elType.equals("h3")) {

				//these are "Entgeltliche Tätigkeiten neben dem Mandat"
				if(el.text().trim().startsWith("2")) {

					for(int j = i+1; j < children.size(); j++) {
						Element taetigkeit = children.get(j);

						//no heading so it is a taetigkeit
						if(!taetigkeit.nodeName().trim().equals("h3")) {
							
							//this is the auftraggeber
							if(taetigkeit.hasClass("voa_abstand")) {
								Nebentaetigkeit nt = new Nebentaetigkeit();
								this.parseAuftraggeber(nt, taetigkeit.text());
								this.parseInfoOfTaetigkeit(nt, children.get(j+1).text());
								nt.setSourceString(taetigkeit.text()+"\n"+children.get(j+1).text());
								nts.add(nt);
								j++;
							}
						} else {
							i = j-1;
							break;
						}
					}
					
				}
			}
				
		}
		mdb.setNebentaetigkeiten(nts);
		//System.out.println("Mdb " + mdb.getLastname() + " has " + nts.size() + " entgeltliche Nebentaetigkeiten");
		
	}
	
	private void writeNebentaetigkeitenToFile() {
		BufferedWriter out = null;
        
        try {

        	FileWriter fileOut = new FileWriter("nebentaetigkeiten");
            out = new BufferedWriter(fileOut);
            int minGesamt = 0;
            int maxGesamt = 0;
            int overMax = 0;
            for(Abgeordneter mdb : this.mdbs) {
            	out.write(mdb.getForename()+" "+mdb.getLastname());
            	out.newLine();
            	out.write("Anzahl Tätigkeiten: "+mdb.getAnzahlNebeneinkuenfte()+" min: "+mdb.getMinZusatzeinkommen()+" max "+mdb.getMaxZusatzeinkommen());
            	out.newLine();
            	out.write("______Nebentätigkeiten:______");
            	out.newLine();
            	out.flush();
            	for(Nebentaetigkeit neben : mdb.getNebentaetigkeiten()) {
            		out.write("Auftraggeber: " + neben.getAuftraggeber() + " ;Ort: " + neben.getPlace());
            		out.newLine();
            		out.write("Typ: " + neben.getType() + " ;Jahr: " + neben.getYear());
            		out.newLine();
            		out.write("Stufe: " + neben.getStufe()); 
            		out.newLine();
            		if(neben.getSourceString() != null) {
            			out.write(neben.getSourceString());
            			out.newLine();
            		}
            		out.flush();
            	}
            	out.write("_____________________________");
            	out.newLine();
            	out.flush();
            	minGesamt += mdb.getMinZusatzeinkommen();
            	if(mdb.getMaxZusatzeinkommen()!=-1) {
            		maxGesamt += mdb.getMaxZusatzeinkommen();
            	} else {
            		overMax++;
            	}
            }
            
            out.write(mdbs.size() + " Abgeordnete, Einkünfte min gesamt: " + minGesamt + " max gesamt: " + maxGesamt);
            out.newLine();
            out.write(overMax + " Abgeordnete haben mindestens eine Tätigkeit über 7000 Euro.");
            
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            //Close the ObjectOutputStream
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
	
	//TODO: include intervals; fix types. This will need a lot of debugging and clever heuristics
	private void parseInfoOfTaetigkeit(Nebentaetigkeit nt, String text) {
		if(!text.contains(";")) {
			String[] split = text.split(",");
			for(int i = 0; i < split.length; i++) {
				String trimmed = split[i].trim();
				if(split[i].contains("Stufe")) {
					nt.setStufe(trimmed);
				//a year
				} else if(trimmed.matches("(1|2)[0-9]{3}")) {
					nt.setYear(trimmed);
				//this is buggy
				} else {
					nt.appendType(trimmed);
				}
			}
		} else {
			String[] infoSplit = text.split(";");
			//this is a crutch: first part of split String parsed by this method recursively
			this.parseInfoOfTaetigkeit(nt, infoSplit[0]);
		}
	}

	private void parseAuftraggeber(Nebentaetigkeit nt, String text) {
		String[] split = text.split(",");
		if(split.length==2) {
			nt.setAuftraggeber(split[0]);
			nt.setPlace(split[1]);
		}
	}
	
	public List<Abgeordneter> getAbgeordnete() {
		return this.mdbs;
	}
	
	
	public static void main(String[] args) {
		BundestagConverter conv = new BundestagConverter("http://www.bundestag.de/bundestag/abgeordnete17/alphabet/index.html",true);	
		//conv.writeNebentaetigkeitenToFile();
		for(Abgeordneter mdb : conv.getAbgeordnete()) {
			System.out.println(mdb.getHomepage());
		}
		
//		SpendenParser spend = new SpendenParser();
//		spend.parseSpenden("spenden.csv");
//		double CDU = 0;
//		for(ParteiSpende spende : spend.getSpenden()) {
//			System.out.println(spende.getPartei());
//			if(spende.getPartei().equals("CDU")) {
//				CDU+=spende.getValue();
//			}
//		}
//		System.out.println("CDU spenden: " + CDU);
		
	}

}
