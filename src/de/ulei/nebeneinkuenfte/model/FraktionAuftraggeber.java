package de.ulei.nebeneinkuenfte.model;

import java.io.Serializable;

public class FraktionAuftraggeber implements Serializable {

	private static final long serialVersionUID = -8650143789015140347L;

	private Abgeordneter abgeordneter;
	private Nebentaetigkeit nebentaetigkeit;

	private String fraktion;
	private String forename;
	private String lastname;

	private String auftraggeber;
	private String type;
	private String place;
	private String year;
	private String stufe;

	public FraktionAuftraggeber(Abgeordneter abgeordneter,
			Nebentaetigkeit nebentaetigkeit) {

		this.abgeordneter = abgeordneter;
		this.nebentaetigkeit = nebentaetigkeit;
		this.fraktion = abgeordneter.getFraktion();
		this.forename = abgeordneter.getForename();
		this.lastname = abgeordneter.getLastname();
		this.auftraggeber = nebentaetigkeit.getAuftraggeber();
		this.type = nebentaetigkeit.getType();
		this.place = nebentaetigkeit.getPlace();
		this.year = nebentaetigkeit.getYear();
		this.stufe = nebentaetigkeit.getStufe();
	}

	public FraktionAuftraggeber() {

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

	public String getFraktion() {
		return fraktion;
	}

	public void setFraktion(String fraktion) {
		this.fraktion = fraktion;
	}

	public String getForename() {
		return forename;
	}

	public void setForename(String forename) {
		this.forename = forename;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
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

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
