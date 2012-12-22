package de.ulei.nebeneinkuenfte.model.crawler;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;
import java.util.ArrayList;
import java.util.List;


public class SpendenParser {

	private List<ParteiSpende> spenden;
	
	public SpendenParser() {
		this.spenden = new ArrayList<ParteiSpende>();
	}
	
	public void parseSpenden(String sourceCsvFile) {
		
			ArrayList<String[]> csvmap=new ArrayList<String[]>();
			InputStream input;
			try {
				input = new FileInputStream(sourceCsvFile);
				Charset charset=Charset.forName("UTF-8");
				CsvReader reader=new CsvReader(input,";".charAt(0),charset);
				reader.skipLine();
				while(reader.readRecord()) {
					String[] s = reader.getValues();
					csvmap.add(s);
				}
			} catch (FileNotFoundException e) {
				System.out.println("CSV File for Parteispenden not found.");
			} catch (IOException ioe) {
				System.out.println("CSV File for Parteispenden could not be opened.");
			} catch (UnsupportedCharsetException uce) {
				System.out.println("CSV File for Parteispenden could not be parsed, System does not support UTF-8.");
			}
			
			if(!csvmap.isEmpty()) {
				int line = 1;
				for(String[] spende : csvmap) {
					line++;
					if(spende.length!=8){
						System.out.println("Not enough data on line " + line);
						continue;
					}
					ParteiSpende ps = new ParteiSpende();
					ps.setYear(Integer.parseInt(spende[0]));
					ps.setSpender(spende[1].trim());
					ps.setType(spende[2].trim());
					ps.setPlace(spende[3].trim());
					ps.setPartei(spende[6].trim());
					ps.setValue(this.parseDouble(spende[7]));
					this.spenden.add(ps);
				}
			}
	}

	public List<ParteiSpende> getSpenden() {
		return spenden;
	}
	
	private Double parseDouble(String doubleString) {
		
		if(doubleString.contains(".")) {
			String[] numberparts = doubleString.split("\\.");
			String toParse = "";
			for(int i = 0; i < numberparts.length; i++) {
				toParse+=numberparts[i];
			}
			toParse = toParse.replace(",", ".");
			return Double.parseDouble(toParse);
		} else {
			return Double.parseDouble(doubleString.replace(",", "."));
		}	
	}

	public void setSpenden(List<ParteiSpende> spenden) {
		this.spenden = spenden;
	}
	
}
