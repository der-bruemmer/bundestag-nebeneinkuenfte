package de.ulei.nebeneinkuenfte.controller;

import java.util.Stack;

import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.terminal.ThemeResource;

import de.ulei.nebeneinkuenfte.MainFrameWindow;
import de.ulei.nebeneinkuenfte.NebeneinkuenfteApplication;
import de.ulei.nebeneinkuenfte.crawler.BundestagConverter;
import de.ulei.nebeneinkuenfte.model.Abgeordneter;
import de.ulei.nebeneinkuenfte.model.Fraktion;
import de.ulei.nebeneinkuenfte.model.Nebentaetigkeit;
import de.ulei.nebeneinkuenfte.util.ActionEvent;
import de.ulei.nebeneinkuenfte.util.IActionListener;
import de.ulei.nebeneinkuenfte.util.IConstants;
import de.ulei.nebeneinkuenfte.view.AbstractView;
import de.ulei.nebeneinkuenfte.view.BasicView;
import de.ulei.nebeneinkuenfte.view.OriginView;
import de.ulei.nebeneinkuenfte.view.PartyView;
import de.ulei.nebeneinkuenfte.view.PersonView;

public class MainController implements IActionListener {

	private static final long serialVersionUID = -2602673556758294975L;

	private Stack<Object[]> historyStack;

	private BasicController basicController;
	private PersonController personController;
	private PartyController partyController;
	private OriginController originController;

	private BasicView basicView;
	private PersonView personView;
	private PartyView partyView;
	private OriginView originView;

	private AbstractView actualPersonView;
	private AbstractController actualController;

	private MainFrameWindow mainFrame;

	public MainController(MainFrameWindow mainFrame) {

		this.mainFrame = mainFrame;
		this.historyStack = new Stack<Object[]>();

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

		// open basic view as app entry
		mainFrame.addTab(basicView, "Person", new ThemeResource(
				"icons/16/user.png"));
		actualPersonView = basicView;
		openPersonBasicView();
		basicController.refreshTableFooter();

	}

	@Override
	public void handleAction(ActionEvent event) {

		switch (event.getActionType()) {
		case GO_BACK:
			handleBack();
			break;
		case OPEN_PERSON_BASIC:
			openPersonBasicView();
			break;
		case OPEN_PERSON_PARTY:
			openPersonPartyView();
			break;
		case OPEN_PERSON_PERSON:
			openPersonPersonView();
			break;
		default:
			break;
		}

	}

	private void handleBack() {

		if (historyStack.size() >= 2) {
			historyStack.pop();
			goBack(historyStack.peek());
		}

	}

	private void openPersonBasicView() {

		String path = NebeneinkuenfteApplication.getInstance().getContext()
				.getBaseDirectory()
				+ "/abgeordnete";
		BundestagConverter conv = new BundestagConverter(
				"http://www.bundestag.de/bundestag/abgeordnete17/alphabet/index.html",
				false, path);

		BeanItemContainer<Abgeordneter> container = new BeanItemContainer<Abgeordneter>(
				Abgeordneter.class);

		for (Abgeordneter mdb : conv.getAbgeordnete())
			container.addItem(mdb);

		path = null;
		conv = null;

		basicView.setPersonContainerDataSource(container);

		setActualPersonView(basicView, basicController);
		goForward(IConstants.PERSON_BASIC_VIEW, null);

	}

	private void openPersonPersonView() {

		Abgeordneter actualPerson = null;

		// click was done in view controlled by AbstractPersonController
		if (getActualController() instanceof AbstractPersonController) {

			AbstractPersonController controller = (AbstractPersonController) getActualController();
			actualPerson = controller.getActualPerson();

			if (actualPerson != null) {

				// open PersonView
				openPersonPersonView(actualPerson);

				// save state
				goForward(IConstants.PERSON_PERSON_VIEW, actualPerson);

			}

		}
	}

	private void openPersonPersonView(Abgeordneter person) {

		BeanItemContainer<Nebentaetigkeit> container = new BeanItemContainer<Nebentaetigkeit>(
				Nebentaetigkeit.class);

		for (Nebentaetigkeit nt : person.getNebentaetigkeiten())
			container.addItem(nt);

		String caption = "";
		caption = caption.concat(person.getForename());
		caption = caption.concat(" ");
		caption = caption.concat(person.getLastname());
		caption = caption.concat(", ");
		caption = caption.concat(person.getFraktion());

		personView.setSidelineJobContainerDataSource(container);
		personView.setPanelCaption(caption);
		setActualPersonView(personView, personController);

	}

	private void openPersonPartyView() {

		Abgeordneter actualPerson = null;

		// click was done in view controlled by AbstractPersonController
		if (getActualController() instanceof AbstractPersonController) {

			AbstractPersonController controller = (AbstractPersonController) getActualController();
			actualPerson = controller.getActualPerson();

			if (actualPerson != null) {

				// null check for fraktion
				if (actualPerson.getFraktion() != null) {

					// open PartyView
					openPersonPartyView(actualPerson.getFraktion());

					// save state
					goForward(IConstants.PERSON_PARTY_VIEW,
							actualPerson.getFraktion());
				}
			}

		}

	}

	private void openPersonPartyView(String party) {

		BeanItemContainer<Fraktion> container = new BeanItemContainer<Fraktion>(
				Fraktion.class);

		String path = NebeneinkuenfteApplication.getInstance().getContext()
				.getBaseDirectory()
				+ "/abgeordnete";
		BundestagConverter conv = new BundestagConverter(
				"http://www.bundestag.de/bundestag/abgeordnete17/alphabet/index.html",
				false, path);

		for (Abgeordneter mdb : conv.getAbgeordnete())
			if (mdb.getFraktion().equals(party))
				for (Nebentaetigkeit nt : mdb.getNebentaetigkeiten())
					container.addItem(new Fraktion(mdb, nt));

		partyView.setPartyContainerDataSource(container);
		partyView.setPanelCaption(party);
		setActualPersonView(partyView, partyController);

	}

	private AbstractView getActualPersonView() {
		return actualPersonView;
	}

	private void setActualPersonView(AbstractView actualView,
			AbstractController controller) {

		// remove actual view
		int tabIndex = mainFrame.getTabIndex(getActualPersonView());
		mainFrame.removeTab(getActualPersonView());

		// add new view
		mainFrame.addTab(actualView, "Person", new ThemeResource(
				"icons/16/user.png"), tabIndex);

		// select new view
		mainFrame.selectTab(actualView);

		// set actual view and controller
		this.actualPersonView = actualView;
		this.setActualController(controller);

	}

	public AbstractController getActualController() {
		return actualController;
	}

	public void setActualController(AbstractController actualController) {
		this.actualController = actualController;
	}

	private void goBack(Object[] state) {

		if (state != null) {

			int view = (Integer) state[0];

			switch (view) {
			case IConstants.PERSON_BASIC_VIEW:
				openPersonBasicView();
				break;
			case IConstants.PERSON_PERSON_VIEW:
				openPersonPersonView((Abgeordneter) state[1]);
				break;
			case IConstants.PERSON_PARTY_VIEW:
				openPersonPartyView((String) state[1]);
				break;
			case IConstants.PERSON_ORIGIN_VIEW:
				break;

			default:
				break;
			}

		}

	}

	private void goForward(int view, Object object) {
		historyStack.push(new Object[] { view, object });
	}

}
