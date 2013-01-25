package de.ulei.nebeneinkuenfte.util;

public interface IConstants {

	/*
	 * placeholder integer value for infinite and character
	 */

	public static int INFINITE_VALUE = 2500000;
	public static String INFINITE_SIGN = String.valueOf('\u221e');

	/*
	 * IDs for different views
	 */

	public static int PERSON_BASIC_VIEW = 0;
	public static int PERSON_PERSON_VIEW = 1;
	public static int PERSON_FRACTION_VIEW = 2;
	public static int PERSON_ORIGIN_VIEW = 3;
	public static int IMPRESSUM_VIEW = 4;
	public static int ABOUT_PROJECT_VIEW = 5;

	/*
	 * URI fragments
	 */
	public static String PERSON_BASIC_VIEW_FRAG = "all";
	public static String PERSON_PERSON_VIEW_FRAG = "mdb";
	public static String PERSON_FRACTION_VIEW_FRAG = "fraction";
	public static String PERSON_ORIGIN_VIEW_FRAG = "source";
	public static String IMPRESSUM_VIEW_FRAG = "impressum";
	public static String ABOUT_PROJECT_VIEW_FRAG = "about";

	/*
	 * common button length
	 */

	public static int BUTTON_WIDTH = 150;

	/*
	 * label and URI for political partys
	 */

	public final String SPD_FRAKTION = IConstants.NAMESPACE.concat("/").concat(IConstants.PERSON_FRACTION_VIEW_FRAG)
			.concat("/").concat("spd");
	public final String SPD_LABEL = "SPD";

	public final String CDU_CSU_FRAKTION = IConstants.NAMESPACE.concat("/")
			.concat(IConstants.PERSON_FRACTION_VIEW_FRAG).concat("/").concat("cducsu");
	public final String CDU_CSU_LABEL = "CDU/CSU";

	public final String DIE_LINKE_FRAKTION = IConstants.NAMESPACE.concat("/")
			.concat(IConstants.PERSON_FRACTION_VIEW_FRAG).concat("/").concat("dielinke");
	public final String DIE_LINKE_LABEL = "Die Linke";

	public final String FDP_FRAKTION = IConstants.NAMESPACE.concat("/").concat(IConstants.PERSON_FRACTION_VIEW_FRAG)
			.concat("/").concat("fdp");
	public final String FDP_LABEL = "FDP";

	public final String GRUENE_FRAKTION = IConstants.NAMESPACE.concat("/").concat(IConstants.PERSON_FRACTION_VIEW_FRAG)
			.concat("/").concat("diegruenen");
	public final String GRUENE_LABEL = "Bündnis 90/Die Grünen";

	public final String NO_FRAKTION = IConstants.NAMESPACE.concat("/").concat(IConstants.PERSON_FRACTION_VIEW_FRAG)
			.concat("/").concat("keinefraktion");
	public final String NO_LABEL = "keine Fraktion";

	/*
	 * column order and headers for tables in according views
	 */

	public static String[] ABGEORDNETER_VISIBLE_COLUMNS = new String[] { "forename", "lastname", "homepage", "email",
			"fraktion", "wahlkreisUri", "minZusatzeinkommen", "maxZusatzeinkommen" };

	public static String[] ABGEORDNETER_COLUMN_HEADER = new String[] { "Vorname", "Nachname", "Homepage", "Email",
			"Fraktion", "Wahlkreis", "Zusatzeinkommen (min)", "Zusatzeinkommen (max)" };

	public static String[] NEBENTAETIGKEIT_VISIBLE_COLUMNS = new String[] { "auftraggeber", "auftraggeberHomepage",
			"type", "place", "year", "stufe" };

	public static String[] NEBENTAETIGKEIT_COLUMN_HEADER = new String[] { "Auftraggeber", "Weiterführendes", "Art",
			"Ort", "Jahr", "Stufe" };

	public static String[] FRAKTION_VISIBLE_COLUMNS = new String[] { "forename", "lastname", "auftraggeber",
			"auftraggeberHomepage", "type", "place", "year", "stufe" };

	public static String[] FRAKTION_COLUMN_HEADER = new String[] { "Vorname", "Nachname", "Auftraggeber",
			"Weiterführendes", "Art", "Ort", "Jahr", "Stufe" };

	public static String[] AUFTRAGGEBER_VISIBLE_COLUMNS = new String[] { "forename", "lastname", "fraktion", "type",
			"place", "year", "stufe" };

	public static String[] AUFTRAGGEBER_COLUMN_HEADER = new String[] { "Vorname", "Nachname", "Fraktion", "Art", "Ort",
			"Jahr", "Stufe" };

	/*
	 * misc
	 */

	public static String LINE_SEPARATOR = "<br>";
	public static String NAMESPACE = "http://localhost:8080/Nebeneinkuenfte/b09";

}
