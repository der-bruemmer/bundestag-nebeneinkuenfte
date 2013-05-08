package de.ulei.nebeneinkuenfte.ui.view;

import de.ulei.nebeneinkuenfte.util.IConstants;

public class ImpressumView extends AbstractTextView {

	private static final long serialVersionUID = -3207647187091883100L;

	public ImpressumView() {

		super(IConstants.IMPRESSUM_VIEW);
		setSizeFull();
		setImmediate(true);
		setColumns(1);
		setMargin(true);
		setSpacing(true);
		
		addHeadline("Über uns", 1);
		addParagraph("1_about_us","impressum");
		addHeadline("Wir sind weder Web-Designer, noch Journalisten.", 3);
		addParagraph("2_disclaimer","impressum");
		addHeadline("Angaben gemäß § 5 TMG:", 3);
		addParagraph("3_tmg","impressum");

	}

}
