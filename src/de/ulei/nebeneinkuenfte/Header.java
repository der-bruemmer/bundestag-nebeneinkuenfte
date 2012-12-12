package de.ulei.nebeneinkuenfte;

import com.vaadin.event.MouseEvents;
import com.vaadin.terminal.ThemeResource;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.ui.CustomLayout;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.Label;

import de.ulei.nebeneinkuenfte.solitaire.Solitaire;

public class Header extends CustomLayout {

	private static final long serialVersionUID = 7752183498622282356L;

	private static String VERSION = "Version: 0.1";

	public Header() {

		setTemplateName("HeaderLayout");

		// icon with open hand
		Embedded leftLogo = new Embedded("", new ThemeResource(
				"icons/256/hand.png"));

		// icon with money
		Embedded rightLogo = new Embedded("", new ThemeResource(
				"icons/256/dollars.png"));
		rightLogo.addListener(new MouseEvents.ClickListener() {

			private static final long serialVersionUID = -1980118697005316895L;

			@Override
			public void click(MouseEvents.ClickEvent event) {

				if (event.isDoubleClick())
					playSolitaire();

			}
		});

		addComponent(leftLogo, "leftLogo");
		addComponent(rightLogo, "rightLogo");
		addComponent(new Label(VERSION), "version");

	}

	private void playSolitaire() {

		AbsoluteLayout layout = new AbsoluteLayout();
		layout.setWidth("1024px");
		layout.setHeight("768px");

		Solitaire s = new Solitaire();
		getApplication().getMainWindow().addWindow(s.getSolitaireWindow());

	}

}
