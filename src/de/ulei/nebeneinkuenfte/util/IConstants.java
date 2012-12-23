package de.ulei.nebeneinkuenfte.util;

public interface IConstants {
	
	/*
	 * IDs for different views
	 */

	public static int PERSON_BASIC_VIEW = 0;
	public static int PERSON_PERSON_VIEW = 1;
	public static int PERSON_PARTY_VIEW = 2;
	public static int PERSON_ORIGIN_VIEW = 3;

	/*
	 * common button length
	 */
	
	public static int BUTTON_WIDTH = 150;

	
	/*
	 * label and URI for political partys
	 */
	
	public final String SPD_FRAKTION = "http://www.spdfraktion.de/";
	public final String SPD_LABEL = "SPD";

	public final String CDU_CSU_FRAKTION = "http://www.cducsu.de/";
	public final String CDU_CSU_LABEL = "CDU/CSU";

	public final String DIE_LINKE_FRAKTION = "http://www.linksfraktion.de/";
	public final String DIE_LINKE_LABEL = "Die Linke";

	public final String FDP_FRAKTION = "http://www.fdp-fraktion.de/";
	public final String FDP_LABEL = "FDP";

	public final String GRUENE_FRAKTION = "http://www.gruene-bundestag.de/";
	public final String GRUENE_LABEL = "Bündnis 90/Die Grünen";
	
	/*
	 * column order and headers for tables in according views
	 */

	public static String[] ABGEORDNETER_VISIBLE_COLUMNS = new String[] { "forename", "lastname", "homepage", "email",
			"fraktion", "wahlkreisUri", "minZusatzeinkommen", "maxZusatzeinkommen" };

	public static String[] ABGEORDNETER_COLUMN_HEADER = new String[] { "Vorname", "Nachname", "Homepage", "Email",
			"Fraktion", "Wahlkreis", "Zusatzeinkommen (min)", "Zusatzeinkommen (max)" };

	public static String[] NEBENTAETIGKEIT_VISIBLE_COLUMNS = new String[] { "auftraggeber", "type", "place", "year",
			"stufe" };

	public static String[] NEBENTAETIGKEIT_COLUMN_HEADER = new String[] { "Auftraggeber", "Art", "Ort", "Jahr", "Stufe" };

	public static String[] FRAKTION_VISIBLE_COLUMNS = new String[] { "forename", "lastname", "auftraggeber", "type",
			"place", "year", "stufe" };

	public static String[] FRAKTION_COLUMN_HEADER = new String[] { "Vorname", "Nachname", "Auftraggeber", "Art", "Ort",
			"Jahr", "Stufe" };

	public static String[] AUFTRAGGEBER_VISIBLE_COLUMNS = new String[] { "forename", "lastname", "fraktion", "type",
			"place", "year", "stufe" };

	public static String[] AUFTRAGGEBER_COLUMN_HEADER = new String[] { "Vorname", "Nachname", "Fraktion", "Art", "Ort",
			"Jahr", "Stufe" };

}
