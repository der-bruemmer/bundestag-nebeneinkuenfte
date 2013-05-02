package de.ulei.nebeneinkuenfte.ui.model;

import java.io.Serializable;

public class Nebentaetigkeit implements Serializable {

	private static final long serialVersionUID = -2834030362807009074L;

	private String auftraggeber;
	private String auftraggeberHomepage;
	private String auftragUri;

	private String type;
	private String place;
	private String placeUri;
	private float latitude;
	private float longitude;
	private String year;
	private boolean isMonthly = false;
	private boolean isYearly = false;
	private boolean isGouvernmentJob = false;
	private String stufe;
	private String sourceString;
	
	private int[] jobStart;
	private int[] jobEnd;

	public Nebentaetigkeit() {

	}

	public Nebentaetigkeit(Nebentaetigkeit nt) {
		this.auftraggeber = nt.getAuftraggeber();
		this.auftraggeberHomepage = nt.getAuftraggeberHomepage();
		this.auftragUri = nt.getAuftragUri();
		this.type = nt.getType();
		this.place = nt.getPlace();
		this.placeUri = nt.getPlaceUri();
		this.year = nt.getYear();
		this.isMonthly = nt.isMonthly();
		this.isYearly = nt.isYearly();
		this.isGouvernmentJob = nt.isGouvernmentJob();
		this.stufe = nt.getStufe();
		this.sourceString = nt.getSourceString();
		this.jobEnd = nt.getJobEnd();
		this.jobStart = nt.getJobStart();
	}

	public boolean isYearly() {
		return isYearly;
	}
	
	public boolean isGouvernmentJob() {
		return isGouvernmentJob;
	}

	public void setGouvernmentJob(boolean isGouvernmentJob) {
		this.isGouvernmentJob = isGouvernmentJob;
	}

	public int[] getJobStart() {
		return jobStart;
	}

	public void setJobStart(int[] jobStart) {
		this.jobStart = jobStart;
	}

	public int[] getJobEnd() {
		return jobEnd;
	}

	public void setJobEnd(int[] jobEnd) {
		this.jobEnd = jobEnd;
	}

	public void setYearly(boolean isYearly) {
		this.isYearly = isYearly;
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

	public String getPlaceUri() {
		return placeUri;
	}

	public void setPlaceUri(String placeUri) {
		this.placeUri = placeUri;
	}

	public String getAuftragUri() {
		return auftragUri;
	}

	public void setAuftragUri(String auftragUri) {
		this.auftragUri = auftragUri;
	}

	public String getAuftraggeberHomepage() {
		return auftraggeberHomepage;
	}

	public void setAuftraggeberHomepage(String auftraggeberHomepage) {
		this.auftraggeberHomepage = auftraggeberHomepage;
	}

	public boolean isMonthly() {
		return isMonthly;
	}

	public void setMonthly(boolean monthly) {
		this.isMonthly = monthly;
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

	public void appendAuftraggeber(String auftraggeber) {
		if (this.auftraggeber == null) {
			this.auftraggeber = auftraggeber;
		} else {
			this.auftraggeber += ", " + auftraggeber;
		}
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void appendType(String type) {
		if (type != null) {
			if (this.type == null) {
				this.type = type;
			} else {
				this.type += ", " + type;
			}
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
