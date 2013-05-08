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

		addHeadline("Probleme des Stufensystems", 2);
		addParagraph("1_probleme");

		addHeadline("Mangelnde Kennzeichnung von monatlichen Einkünften?", 2);
		addParagraph("2_kennzeichnung");

		addHeadline("Wie sich die hier angegebenen Zahlen lesen lassen", 2);
		addParagraph("3_zahlendeutung");

		addHeadline("Die Mindestnebeneinkünfte und ihre Verteilung", 2);
		addParagraph("4_verteilung");

		addTable("table_1", "Der 17. Bundestag – Alle Fraktionen", false);

		List<String> fileNames = new ArrayList<String>();
		fileNames.add("hand.png");
		fileNames.add("dollars.png");
		
		List<String> fileCaptions = new ArrayList<String>();
		fileCaptions.add("hand.png");
		fileCaptions.add("dollars.png");
		
		addImages(fileNames, fileCaptions);
		
		

	}

}
