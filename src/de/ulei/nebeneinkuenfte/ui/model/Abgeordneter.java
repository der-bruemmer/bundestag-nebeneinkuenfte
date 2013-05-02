package de.ulei.nebeneinkuenfte.ui.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringEscapeUtils;

import de.ulei.nebeneinkuenfte.util.IConstants;

public class Abgeordneter implements Serializable {

	@Override
	public String toString() {
		return "Abgeordneter [uri=" + uri + ", forename=" + forename + ", lastname=" + lastname + "]";
	}

	private static final long serialVersionUID = 1447162980745311414L;

	private String uri;
	private String label;
	private String forename;
	private String lastname;
	private String homepage;
	private String email;
	private String wahlkreisUri;
	private String wahlkreisName;
	private String fraktionUri;
	private String fraktion;
	private int anzahlNebeneinkuenfte = 0;
	private int minZusatzeinkommen = 0;
	private int maxZusatzeinkommen = 0;
	private List<Nebentaetigkeit> nebentaetigkeiten;

	public Abgeordneter() {
		nebentaetigkeiten = new ArrayList<Nebentaetigkeit>();
	}

	public void setAttributesFromLinkText(String linkText) {
		String[] values = linkText.split(",");
		if (values.length == 3) {
			setLastname(StringEscapeUtils.unescapeHtml4(values[0]));
			setForename(StringEscapeUtils.unescapeHtml4(values[1]).trim());
			setFraktion(StringEscapeUtils.unescapeHtml4(values[2]).trim());
			setLabel(this.forename + "+" + this.lastname + "+" + this.fraktion.replaceAll(" ", "+"));
		} else {
			System.out.println("Problem setting attributes from: " + linkText);
		}
	}

	public String getURI() {
		return uri;
	}

	public void setFinalURI() {

		String finalURI = "";

		finalURI = finalURI.concat(IConstants.NAMESPACE);
		finalURI = finalURI.concat("/");
		finalURI = finalURI.concat(IConstants.PERSON_PERSON_VIEW_FRAG);
		finalURI = finalURI.concat(getURI().substring(getURI().lastIndexOf("/")));

		setURI(finalURI);

	}

	public void setURI(String uri) {
		this.uri = uri;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
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
		if (lastname.contains("(")) {
			lastname = lastname.substring(0, lastname.indexOf("(")).trim();
		}
		this.lastname = lastname;
	}

	public String getHomepage() {
		return homepage;
	}

	public void setHomepage(String homepage) {
		this.homepage = homepage;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getWahlkreisUri() {
		return wahlkreisUri;
	}

	public void setWahlkreisUri(String wahlkreisUri) {
		this.wahlkreisUri = wahlkreisUri;
	}

	public String getWahlkreisName() {
		return wahlkreisName;
	}

	public void setWahlkreisName(String wahlkreisName) {
		this.wahlkreisName = wahlkreisName;
	}

	public String getFraktionUri() {
		return fraktionUri;
	}

	public void setFraktionUri(String fraktionUri) {
		this.fraktionUri = fraktionUri;
	}

	public String getFraktion() {
		return fraktion;
	}

	public void setFraktion(String fraktion) {

		if (fraktion == null) {
			this.fraktion = "unbekannt";
			return;
		}
		System.out.println(fraktion);
		if (fraktion.equals(IConstants.SPD_LABEL))
			setFraktionUri(IConstants.SPD_FRAKTION);
		else if (fraktion.equals(IConstants.CDU_CSU_LABEL))
			setFraktionUri(IConstants.CDU_CSU_FRAKTION);
		else if (fraktion.equals(IConstants.DIE_LINKE_LABEL))
			setFraktionUri(IConstants.DIE_LINKE_FRAKTION);
		else if (fraktion.equals(IConstants.FDP_LABEL))
			setFraktionUri(IConstants.FDP_FRAKTION);
		else if (fraktion.equals(IConstants.GRUENE_LABEL))
			setFraktionUri(IConstants.GRUENE_FRAKTION);
		else
			setFraktionUri(IConstants.NO_FRAKTION);

		this.fraktion = fraktion;

	}

	public int getAnzahlNebeneinkuenfte() {
		return anzahlNebeneinkuenfte;
	}

	public void setAnzahlNebeneinkuenfte(int anzahlNebeneinkuenfte) {
		this.anzahlNebeneinkuenfte = anzahlNebeneinkuenfte;
	}

	public int getMinZusatzeinkommen() {
		return minZusatzeinkommen;
	}

	public void setMinZusatzeinkommen(int minZusatzeinkommen) {
		this.minZusatzeinkommen = minZusatzeinkommen;
	}

	public int getMaxZusatzeinkommen() {
		return maxZusatzeinkommen;
	}

	public void setMaxZusatzeinkommen(int maxZusatzeinkommen) {
		this.maxZusatzeinkommen = maxZusatzeinkommen;
	}

	public List<Nebentaetigkeit> getNebentaetigkeiten() {
		return nebentaetigkeiten;
	}
	
	private int getMonthsOnJobNumber(Nebentaetigkeit nt) {
		int months = 0;
		int startYear = nt.getJobStart()[1];
		int endYear = nt.getJobEnd()[1];
		int startMonth = nt.getJobStart()[0];
		int endMonth = nt.getJobEnd()[0];
		months = (endYear-startYear)*12 + (endMonth - startMonth) + 1;
		return months;
	}
	
	private int getYearsOnJobNumber(Nebentaetigkeit nt) {
		int years = 0;
		int startYear = nt.getJobStart()[1];
		int endYear = nt.getJobEnd()[1];
		years = endYear-startYear;
		return years;
	}

	// incorporate start and stop dates
	// 1 legislaturperiode = 4 years
	public void setNebentaetigkeiten(List<Nebentaetigkeit> nebentaetigkeiten) {
		this.nebentaetigkeiten = nebentaetigkeiten;
		boolean canMax = true;
		for (Nebentaetigkeit nt : nebentaetigkeiten) {
			if (nt.getStufe() != null) {
				if (nt.getStufe().contains("1")) {
					if (nt.isMonthly()) {
						this.minZusatzeinkommen += this.getMonthsOnJobNumber(nt)*1000;
						this.maxZusatzeinkommen += this.getMonthsOnJobNumber(nt)*3500;
					} else if (nt.isYearly()) {
						this.minZusatzeinkommen += this.getYearsOnJobNumber(nt)*1000;
						this.maxZusatzeinkommen += this.getYearsOnJobNumber(nt)*3500;
					} else {
						this.minZusatzeinkommen += 1000;
						this.maxZusatzeinkommen += 3500;
					}
				} else if (nt.getStufe().contains("2")) {
					if (nt.isMonthly()) {
						this.minZusatzeinkommen += this.getMonthsOnJobNumber(nt)*3501;
						this.maxZusatzeinkommen += this.getMonthsOnJobNumber(nt)*7000;
					} else if (nt.isYearly()) {
						this.minZusatzeinkommen += this.getYearsOnJobNumber(nt)*3501;
						this.maxZusatzeinkommen += this.getYearsOnJobNumber(nt)*7000;
					} else {
						this.minZusatzeinkommen += 3501;
						this.maxZusatzeinkommen += 7000;
					}
				} else if (nt.getStufe().contains("3")) {
					if (nt.isMonthly()) {
						this.minZusatzeinkommen += this.getMonthsOnJobNumber(nt)*7001;
					} else if (nt.isYearly()) {
						this.minZusatzeinkommen += this.getYearsOnJobNumber(nt)*7001;
					} else {
						this.minZusatzeinkommen += 7001;
					}
					canMax = false;
				}
			} else {
				this.maxZusatzeinkommen += 999;
			}
		}
		if (!canMax) {
			this.maxZusatzeinkommen = 2000000;
		}
		this.setAnzahlNebeneinkuenfte(nebentaetigkeiten.size());
	}

}
