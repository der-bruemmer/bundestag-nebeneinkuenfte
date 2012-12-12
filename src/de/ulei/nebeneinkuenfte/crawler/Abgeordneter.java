package de.ulei.nebeneinkuenfte.crawler;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringEscapeUtils;

public class Abgeordneter implements Serializable {
	
	private String uri;
	private String label;
	private String forename;
	private String lastname;
	private String homepage;
	private String email;
	private String wahlkreis;
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
		if(values.length == 3) {
			this.setLastname(StringEscapeUtils.unescapeHtml4(values[0]));
			this.forename = StringEscapeUtils.unescapeHtml4(values[1]).trim();
			this.fraktion = StringEscapeUtils.unescapeHtml4(values[2]).trim();
			this.label = this.forename + "+" + this.lastname + "+" + this.fraktion.replaceAll(" ", "+");
		} else {
			System.out.println("Problem setting attributes from: " + linkText);
		}
	}

	public String getURI() {
		return uri;
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
		if(lastname.contains("(")) {
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

	public String getWahlkreis() {
		return wahlkreis;
	}

	public void setWahlkreis(String wahlkreis) {
		this.wahlkreis = wahlkreis;
	}
	
	public String getFraktion() {
		return fraktion;
	}

	public void setFraktion(String fraktion) {
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

	public void setNebentaetigkeiten(List<Nebentaetigkeit> nebentaetigkeiten) {
		this.nebentaetigkeiten = nebentaetigkeiten;
		boolean canMax = true;
		for(Nebentaetigkeit nt : nebentaetigkeiten) {
			if(nt.getStufe()!=null) {
				if(nt.getStufe().contains("1")) {
					this.minZusatzeinkommen+=1000;
					this.maxZusatzeinkommen+=3499;
				} else if(nt.getStufe().contains("2")) {
					this.minZusatzeinkommen+=3500;
					this.maxZusatzeinkommen+=6999;
				} else if(nt.getStufe().contains("3")) {
					this.minZusatzeinkommen+=7000;
					canMax=false;
				}
			} else {
				this.maxZusatzeinkommen+=1000;
			}
		}
		if(!canMax) {
			this.maxZusatzeinkommen=-1;
		}
		this.setAnzahlNebeneinkuenfte(nebentaetigkeiten.size());
	}
	
	
	
}
