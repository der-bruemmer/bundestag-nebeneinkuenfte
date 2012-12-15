package de.ulei.nebeneinkuenfte.util;

public interface IConstants {
	
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

}
