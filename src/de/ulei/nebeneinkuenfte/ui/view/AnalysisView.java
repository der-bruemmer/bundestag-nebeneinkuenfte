package de.ulei.nebeneinkuenfte.ui.view;

import java.io.Serializable;

import de.ulei.nebeneinkuenfte.util.IConstants;

public class AnalysisView extends AbstractTextView implements Serializable {

	private static final long serialVersionUID = -3207647187091883100L;

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

	}

}
