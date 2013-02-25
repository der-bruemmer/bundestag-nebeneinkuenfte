package de.ulei.nebeneinkuenfte.ui.model;

import java.io.Serializable;

public class FraktionAuftraggeber implements Serializable {

	private static final long serialVersionUID = -8650143789015140347L;

	private Abgeordneter abgeordneter;
	private Nebentaetigkeit nebentaetigkeit;

	private String fraktion;
	private String forename;
	private String lastname;

	private String auftraggeber;
	private String auftraggeberHomepage;
	private String auftragUri;

	private String type;
	private String place;
	private String placeURI;
	private float latitude;
	private float longitude;
	private String year;
	private String stufe;

	public FraktionAuftraggeber(Abgeordneter abgeordneter, Nebentaetigkeit nebentaetigkeit) {

		this.abgeordneter = abgeordneter;
		this.nebentaetigkeit = nebentaetigkeit;
		this.fraktion = abgeordneter.getFraktion();
		this.forename = abgeordneter.getForename();
		this.lastname = abgeordneter.getLastname();
		this.auftraggeber = nebentaetigkeit.getAuftraggeber();
		this.auftraggeberHomepage = nebentaetigkeit.getAuftraggeberHomepage();
		this.auftragUri = nebentaetigkeit.getAuftragUri();
		this.type = nebentaetigkeit.getType();
		this.place = nebentaetigkeit.getPlace();
		this.placeURI = nebentaetigkeit.getPlaceUri();
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

	public String getPlaceURI() {
		return placeURI;
	}

	public void setPlaceURI(String placeURI) {
		this.placeURI = placeURI;
	}

	public float getLatitude() {
		return latitude;
	}

	public void setLatitude(float latitude) {
		this.latitude = latitude;
	}

	public float getLongitude() {
		return longitude;
	}

	public void setLongitude(float longitude) {
		this.longitude = longitude;
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

	public String getAuftraggeberHomepage() {
		return auftraggeberHomepage;
	}

	public void setAuftraggeberHomepage(String auftraggeberHomepage) {
		this.auftraggeberHomepage = auftraggeberHomepage;
	}

	public String getAuftragUri() {
		return auftragUri;
	}

	public void setAuftragUri(String auftragUri) {
		this.auftragUri = auftragUri;
	}

}
