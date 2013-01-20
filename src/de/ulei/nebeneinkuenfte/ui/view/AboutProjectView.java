package de.ulei.nebeneinkuenfte.ui.view;

import java.io.Serializable;

import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;

import de.ulei.nebeneinkuenfte.util.IConstants;

public class AboutProjectView extends GridLayout implements Serializable {

	private static final long serialVersionUID = -6253578581349325665L;

	public AboutProjectView() {

		setCaption(String.valueOf(IConstants.ABOUT_PROJECT_VIEW));
		addComponent(new Label("In Arbeit"));

	}

}
