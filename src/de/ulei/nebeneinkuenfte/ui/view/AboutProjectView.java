package de.ulei.nebeneinkuenfte.ui.view;

import de.ulei.nebeneinkuenfte.util.IConstants;

public class AboutProjectView extends AbstractTextView {

	private static final long serialVersionUID = -6253578581349325665L;

	public AboutProjectView() {

		super(IConstants.ABOUT_PROJECT_VIEW);
		
		setSizeFull();
		setImmediate(true);
		setColumns(1);
		setMargin(true);
		setSpacing(true);
		
		addHeadline("Motivation", 1);
		addParagraph("1_motivation","about");
		addHeadline("Nutzung", 1);
		addParagraph("2_nutzung","about");
		

	}

}
