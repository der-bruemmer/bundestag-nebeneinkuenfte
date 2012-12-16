package de.ulei.nebeneinkuenfte.model;

import java.io.Serializable;

public class Nebentaetigkeit implements Serializable {
	
	private static final long serialVersionUID = -2834030362807009074L;
	
	private String auftraggeber;
	private String type;
	private String place;
	private String year;
	private String stufe;
	private String sourceString;
	
	public Nebentaetigkeit() {
		
	}
	
	public String getSourceString() {
		return sourceString;
	}
	
	public void setSourceString(String source) {
		this.sourceString = source;
	}
	
	public String getPlace() {
		return place;
	}

	public void setPlace(String place) {
		this.place = place;
	}

	public String getAuftraggeber() {
		return auftraggeber;
	}

	public void setAuftraggeber(String auftraggeber) {
		this.auftraggeber = auftraggeber;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	public void appendType(String type) {
		if(type == null) {
			this.type = type;
		} else {
			this.type += " " + type;
		}
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}

	public String getStufe() {
		return stufe;
	}

	public void setStufe(String stufe) {
		this.stufe = stufe;
	}
	
}
