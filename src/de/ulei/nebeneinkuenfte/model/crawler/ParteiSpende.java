package de.ulei.nebeneinkuenfte.model.crawler;

public class ParteiSpende {
	
	int year = 0;
	String spender;
	String type;
	String place;
	String partei;
	double value;
	
	public ParteiSpende() {
		
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public String getSpender() {
		return spender;
	}

	public void setSpender(String spender) {
		this.spender = spender;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getPlace() {
		return place;
	}

	public void setPlace(String place) {
		this.place = place;
	}

	public String getPartei() {
		return partei;
	}

	public void setPartei(String partei) {
		this.partei = partei;
	}

	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}
	
}
