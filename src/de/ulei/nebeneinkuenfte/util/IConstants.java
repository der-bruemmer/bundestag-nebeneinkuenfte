package de.ulei.nebeneinkuenfte.util;

public interface IConstants {

	public static int PERSON_BASIC_VIEW = 0;
	public static int PERSON_PERSON_VIEW = 1;
	public static int PERSON_PARTY_VIEW = 2;
	public static int PERSON_ORIGIN_VIEW = 3;

	public static int BUTTON_WIDTH = 150;

	public static String[] ABGEORDNETER_VISIBLE_COLUMNS = new String[] {
			"forename", "lastname", "homepage", "email", "fraktion",
			"wahlkreisUri", "minZusatzeinkommen", "maxZusatzeinkommen" };

	public static String[] ABGEORDNETER_COLUMN_HEADER = new String[] {
			"Vorname", "Nachname", "Homepage", "Email", "Partei", "Wahlkreis",
			"Zusatzeinkommen (min)", "Zusatzeinkommen (max)" };

	public static String[] NEBENTAETIGKEIT_VISIBLE_COLUMNS = new String[] {
			"auftraggeber", "type", "place", "year", "stufe" };

	public static String[] NEBENTAETIGKEIT_COLUMN_HEADER = new String[] {
			"Auftraggeber", "Art", "Ort", "Jahr", "Stufe" };

	public static String[] FRAKTION_VISIBLE_COLUMNS = new String[] {
			"forenamePerson", "lastnamePerson", "auftraggeber", "type", "place", "year",
			"stufe" };

	public static String[] FRAKTION_COLUMN_HEADER = new String[] { "Vorname",
			"Nachname", "Auftraggeber", "Art", "Ort", "Jahr", "Stufe" };

}
