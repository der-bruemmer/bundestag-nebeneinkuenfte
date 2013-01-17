package de.ulei.nebeneinkuenfte.ui;

import com.vaadin.terminal.ThemeResource;
import com.vaadin.ui.CustomLayout;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.Label;

public class Header extends CustomLayout {

	private static final long serialVersionUID = 7752183498622282356L;

	private static String VERSION = "Version: 0.1";

	public Header() {

		setTemplateName("HeaderLayout");

		// icon with open hand
		Embedded leftLogo = new Embedded("", new ThemeResource(
				"icons/256/bundestag_2.png"));

		// icon with money
//		Embedded rightLogo = new Embedded("", new ThemeResource(
//				"icons/256/dollars.png"));

		addComponent(leftLogo, "leftLogo");
//		addComponent(rightLogo, "rightLogo");
		addComponent(new Label(VERSION), "version");

	}

}
