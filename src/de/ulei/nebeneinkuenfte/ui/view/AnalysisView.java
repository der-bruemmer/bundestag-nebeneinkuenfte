package de.ulei.nebeneinkuenfte.ui.view;

import java.util.ArrayList;
import java.util.List;

import de.ulei.nebeneinkuenfte.util.IConstants;

public class AnalysisView extends AbstractTextView {

	private static final long serialVersionUID = -8531164488190420084L;

	public AnalysisView() {

		super(IConstants.ANALYSIS_VIEW);

		setSizeFull();
		setImmediate(true);
		setColumns(1);
		setMargin(true);
		setSpacing(true);
		
		addHeadline("Grenzen der statistischen Auswertung", 1);

		addHeadline("Probleme des Stufensystems", 2);
		addParagraph("1_probleme","analysis");

		addHeadline("Mangelnde Kennzeichnung von monatlichen Einkünften?", 2);
		addParagraph("2_kennzeichnung","analysis");

		addHeadline("Wie sich die hier angegebenen Zahlen lesen lassen", 2);
		addParagraph("3_zahlendeutung","analysis");

		addHeadline("Die Mindestnebeneinkünfte und ihre Verteilung", 1);
		addParagraph("4_verteilung","analysis");

		addTable("table_1", "Der 17. Bundestag – Alle Fraktionen", false);
		addTable("table_2", "CDU/CSU", false);
		addTable("table_3", "SPD", false);
		addTable("table_4", "FDP", false);
		addTable("table_5", "Die Linke", false);
		addTable("table_6", "Bündnis 90/Die Grünen", false);

//		List<String> fileNames = new ArrayList<String>();
//		fileNames.add("Boxplot.png");
//		
//		List<String> fileCaptions = new ArrayList<String>();
//		fileCaptions.add("Boxplot der Verteilung der Nebeneinkünfte");
//
//		
//		addImages(fileNames, fileCaptions);
		
		

	}

}
