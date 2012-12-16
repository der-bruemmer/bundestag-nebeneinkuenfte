package de.ulei.nebeneinkuenfte.model;

public class Fraktion {

	private Abgeordneter abgeordneter;
	private Nebentaetigkeit nebentaetigkeit;

	private String fraktionName;
	private String forenamePerson;
	private String lastnamePerson;
	private String auftraggeber;
	private String type;
	private String place;
	private String year;
	private String stufe;

	public Fraktion(Abgeordneter abgeordneter, Nebentaetigkeit nebentaetigkeit) {

		this.abgeordneter = abgeordneter;
		this.nebentaetigkeit = nebentaetigkeit;
		this.fraktionName = abgeordneter.getFraktion();
		this.forenamePerson = abgeordneter.getForename();
		this.lastnamePerson = abgeordneter.getLastname();
		this.auftraggeber = nebentaetigkeit.getAuftraggeber();
		this.type = nebentaetigkeit.getType();
		this.place = nebentaetigkeit.getPlace();
		this.year = nebentaetigkeit.getYear();
		this.stufe = nebentaetigkeit.getStufe();
	}
	
	public Fraktion(){
		
	}

	public String getFraktionName() {
		return fraktionName;
	}

	public void setFraktionName(String fraktionName) {
		this.fraktionName = fraktionName;
	}

	public String getForenamePerson() {
		return forenamePerson;
	}

	public void setForenamePerson(String forenamePerson) {
		this.forenamePerson = forenamePerson;
	}

	public String getLastnamePerson() {
		return lastnamePerson;
	}

	public void setLastnamePerson(String lastnamePerson) {
		this.lastnamePerson = lastnamePerson;
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

	public String getPlace() {
		return place;
	}

	public void setPlace(String place) {
		this.place = place;
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

	public Abgeordneter getAbgeordneter() {
		return abgeordneter;
	}

	public void setAbgeordneter(Abgeordneter abgeordneter) {
		this.abgeordneter = abgeordneter;
	}

	public Nebentaetigkeit getNebentaetigkeit() {
		return nebentaetigkeit;
	}

	public void setNebentaetigkeit(Nebentaetigkeit nebentaetigkeit) {
		this.nebentaetigkeit = nebentaetigkeit;
	}

}
