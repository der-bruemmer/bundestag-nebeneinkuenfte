package de.ulei.nebeneinkuenfte.controller;

import com.vaadin.terminal.ThemeResource;
import com.vaadin.ui.Label;

import de.ulei.nebeneinkuenfte.MainFrameWindow;
import de.ulei.nebeneinkuenfte.util.ActionEvent;
import de.ulei.nebeneinkuenfte.util.IActionListener;
import de.ulei.nebeneinkuenfte.view.BasicView;
import de.ulei.nebeneinkuenfte.view.OriginView;
import de.ulei.nebeneinkuenfte.view.PartyView;
import de.ulei.nebeneinkuenfte.view.PersonView;

public class MainController implements IActionListener {

	private static final long serialVersionUID = -2602673556758294975L;

	private BasicController basicController;
	private PersonController personController;
	private PartyController partyController;
	private OriginController originController;

	private BasicView basicView;
	private PersonView personView;
	private PartyView partyView;
	private OriginView originView;

	private MainFrameWindow mainFrame;

	public MainController(MainFrameWindow mainFrame) {

		this.mainFrame = mainFrame;

		// creates views
		basicView = new BasicView();
		personView = new PersonView();
		partyView = new PartyView();
		originView = new OriginView();

		// create locale view controllers
		basicController = new BasicController(basicView);
		personController = new PersonController(personView);
		partyController = new PartyController(partyView);
		originController = new OriginController(originView);

		// add this controller as listener
		basicController.addListener(this);
		personController.addListener(this);
		partyController.addListener(this);
		originController.addListener(this);

		mainFrame.addTab(basicView, "Person", new ThemeResource(
				"icons/16/user.png"));

		mainFrame.addTab(new Label("In Bearbeitung"), "Partei",
				new ThemeResource("icons/16/users.png"));

	}

	@Override
	public void handleAction(ActionEvent event) {

		switch (event.getActionType()) {
		case HOME:
			break;

		default:
			break;
		}

	}

}