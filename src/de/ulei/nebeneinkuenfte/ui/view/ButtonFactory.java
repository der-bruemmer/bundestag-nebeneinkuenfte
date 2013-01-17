package de.ulei.nebeneinkuenfte.ui.view;

import com.vaadin.terminal.Sizeable;
import com.vaadin.terminal.ThemeResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;

import de.ulei.nebeneinkuenfte.util.ActionType;
import de.ulei.nebeneinkuenfte.util.IConstants;

public class ButtonFactory {

	public static final int OPEN_PERSON_BUTTON = 0;
	public static final int OPEN_PARTY_BUTTON = 1;
	public static final int OPEN_ORIGIN_BUTTON = 2;
	public static final int OPEN_BASIC_BUTTON = 3;
	public static final int OPEN_MAP_BUTTON = 4;
	public static final int GO_BACK_BUTTON = 5;

	public static Button getButton(int buttonType, AbstractView view) {

		switch (buttonType) {
		case OPEN_PERSON_BUTTON:
			return getOpenPersonButton(view);
		case OPEN_PARTY_BUTTON:
			return getOpenPartyButton(view);
		case OPEN_ORIGIN_BUTTON:
			return getOpenOriginButton(view);
		case OPEN_BASIC_BUTTON:
			return getOpenBasicButton(view);
		case OPEN_MAP_BUTTON:
			return getOpenMapButton(view);
		case GO_BACK_BUTTON:
			return getGoBackButton(view);
		default:
			return null;
		}

	}

	private static Button getGoBackButton(final AbstractView view) {

		Button goBackButton = new Button("Return");
		goBackButton.setIcon(new ThemeResource("icons/16/edit-undo.png"));
		goBackButton.addListener(new Button.ClickListener() {

			private static final long serialVersionUID = -6745818656464476271L;

			@Override
			public void buttonClick(ClickEvent event) {
				view.fireEvent(ActionType.GO_BACK);

			}
		});

		return goBackButton;
	}

	private static Button getOpenMapButton(final AbstractView view) {

		Button home = new Button("Show on map");
		home.setImmediate(true);
		home.setWidth(IConstants.BUTTON_WIDTH, Sizeable.UNITS_PIXELS);
		home.setIcon(new ThemeResource("icons/16/globe.png"));
		home.addListener(new Button.ClickListener() {

			private static final long serialVersionUID = -6896210990876900350L;

			@Override
			public void buttonClick(ClickEvent event) {
				view.fireEvent(ActionType.OPEN_MAP);
			}
		});

		return home;
	}

	private static Button getOpenBasicButton(final AbstractView view) {

		Button home = new Button("Home");
		home.setImmediate(true);
		home.setWidth(IConstants.BUTTON_WIDTH, Sizeable.UNITS_PIXELS);
		home.setIcon(new ThemeResource("icons/16/home.png"));
		home.addListener(new Button.ClickListener() {

			private static final long serialVersionUID = -6896210990876900350L;

			@Override
			public void buttonClick(ClickEvent event) {
				view.fireEvent(ActionType.OPEN_PERSON_BASIC);
			}
		});

		return home;
	}

	private static Button getOpenOriginButton(final AbstractView view) {

		Button openOriginButton = new Button("Open Source");
		openOriginButton.setImmediate(true);
		openOriginButton.setIcon(new ThemeResource("icons/16/origin.png"));
		openOriginButton.setEnabled(false);
		openOriginButton.addListener(new Button.ClickListener() {

			private static final long serialVersionUID = 5261319030685442466L;

			@Override
			public void buttonClick(ClickEvent event) {
				view.fireEvent(ActionType.OPEN_PERSON_ORIGIN);
			}
		});
		return openOriginButton;
	}

	private static Button getOpenPartyButton(final AbstractView view) {

		Button openPartyButton = new Button("Open Fraction");
		openPartyButton.setIcon(new ThemeResource("icons/16/users.png"));
		openPartyButton.setEnabled(false);
		openPartyButton.addListener(new Button.ClickListener() {

			private static final long serialVersionUID = 5261319030685442466L;

			@Override
			public void buttonClick(ClickEvent event) {
				view.fireEvent(ActionType.OPEN_PERSON_PARTY);
			}
		});
		return openPartyButton;
	}

	private static Button getOpenPersonButton(final AbstractView view) {

		Button openPersonButton = new Button("Open Person");
		openPersonButton.setImmediate(true);
		openPersonButton.setIcon(new ThemeResource("icons/16/user.png"));
		openPersonButton.addListener(new Button.ClickListener() {

			private static final long serialVersionUID = 5261319030685442466L;

			@Override
			public void buttonClick(ClickEvent event) {
				view.fireEvent(ActionType.OPEN_PERSON_PERSON);
			}
		});

		return openPersonButton;
	}

}
