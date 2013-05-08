package de.ulei.nebeneinkuenfte.ui.view;

import java.io.Serializable;

import de.ulei.nebeneinkuenfte.util.IConstants;

public class AboutProjectView extends AbstractTextView implements Serializable {

	private static final long serialVersionUID = -6253578581349325665L;

	public AboutProjectView() {

		super(IConstants.ABOUT_PROJECT_VIEW);
		addHeadline("In Arbeit", 1);

	}

}
