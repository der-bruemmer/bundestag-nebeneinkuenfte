package de.ulei.nebeneinkuenfte.ui.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.terminal.ThemeResource;

import de.ulei.nebeneinkuenfte.model.crawler.BundestagConverter;
import de.ulei.nebeneinkuenfte.ui.MainFrameWindow;
import de.ulei.nebeneinkuenfte.ui.NebeneinkuenfteApplication;
import de.ulei.nebeneinkuenfte.ui.model.Abgeordneter;
import de.ulei.nebeneinkuenfte.ui.model.FraktionAuftraggeber;
import de.ulei.nebeneinkuenfte.ui.model.Nebentaetigkeit;
import de.ulei.nebeneinkuenfte.ui.view.AboutProjectView;
import de.ulei.nebeneinkuenfte.ui.view.AbstractView;
import de.ulei.nebeneinkuenfte.ui.view.BasicView;
import de.ulei.nebeneinkuenfte.ui.view.ImpressumView;
import de.ulei.nebeneinkuenfte.ui.view.OriginView;
import de.ulei.nebeneinkuenfte.ui.view.FractionView;
import de.ulei.nebeneinkuenfte.ui.view.PersonView;
import de.ulei.nebeneinkuenfte.util.ActionEvent;
import de.ulei.nebeneinkuenfte.util.IActionListener;
import de.ulei.nebeneinkuenfte.util.IConstants;

public class MainController implements IActionListener {

	private static final long serialVersionUID = -2602673556758294975L;

	private List<Abgeordneter> mdbList;
	private String actualObjectURI;

	private BasicController basicController;
	private PersonController personController;
	private FractionController partyController;
	private OriginController originController;

	private ImpressumView impressumView;
	private AboutProjectView aboutProjectView;

	private BasicView basicView;
	private PersonView personView;
	private FractionView partyView;
	private OriginView originView;

	private AbstractView actualPersonView;
	private AbstractController actualController;

	private MainFrameWindow mainFrame;

	public MainController(MainFrameWindow mainFrame) {

		String path = NebeneinkuenfteApplication.getInstance().getContext().getBaseDirectory() + "/abgeordnete";
		BundestagConverter conv = new BundestagConverter(
				"http://www.bundestag.de/bundestag/abgeordnete17/alphabet/index.html", false, path);
		mdbList = conv.getAbgeordnete();

		this.mainFrame = mainFrame;

		// creates views
		impressumView = new ImpressumView();
		aboutProjectView = new AboutProjectView();

		basicView = new BasicView();
		personView = new PersonView();
		partyView = new FractionView();
		originView = new OriginView();

		// create locale view controllers
		basicController = new BasicController(basicView);
		personController = new PersonController(personView);
		partyController = new FractionController(partyView);
		originController = new OriginController(originView);

		// add this controller as listener
		basicController.addListener(this);
		personController.addListener(this);
		partyController.addListener(this);
		originController.addListener(this);

		// open basic view as app entry
		mainFrame.addTab(aboutProjectView, "Das Projekt", new ThemeResource("icons/16/about.png"));
		mainFrame.addTab(basicView, "Abgeordnete", new ThemeResource("icons/16/user.png"));
		mainFrame.addTab(impressumView, "Impressum", null);
		mainFrame.selectTab(0);
		actualPersonView = basicView;
		// openPersonBasicView();
		basicController.refreshTableFooter();

	}

	@Override
	public void handleAction(ActionEvent event) {

		switch (event.getActionType()) {

		case OPEN_PERSON_BASIC:
			openPersonBasicView();
			break;
		case OPEN_PERSON_PARTY:
			openPersonFractionView();
			break;
		case OPEN_PERSON_PERSON:
			openPersonPersonView();
			break;
		case OPEN_PERSON_ORIGIN:
			openPersonOriginView();
			break;
		default:
			break;
		}

	}

	private void openPersonBasicView() {

		BeanItemContainer<Abgeordneter> container = new BeanItemContainer<Abgeordneter>(Abgeordneter.class);

		for (Abgeordneter mdb : mdbList)
			container.addItem(mdb);

		basicView.setPersonContainerDataSource(container);
		setActualPersonView(basicView, basicController);

		// set URI fragment
		NebeneinkuenfteApplication.getInstance().setURIFragment(IConstants.PERSON_BASIC_VIEW_FRAG);

	}

	private void openPersonOriginView() {

		Nebentaetigkeit actualSideJob = null;

		// click was done in view controlled by AbstractSidelineController
		if (getActualController() instanceof AbstractSidelineJobController) {

			AbstractSidelineJobController controller = (AbstractSidelineJobController) getActualController();
			actualSideJob = controller.getActualSidelineJob();

			if (actualSideJob != null) {

				// null check for origin
				if (actualSideJob.getAuftraggeber() != null)
					// open PartyView
					openPersonOriginView(actualSideJob.getAuftragUri());

			}

		}

		else // click was done in view controlled by
				// AbstractFraktionAuftraggeberController
		if (getActualController() instanceof AbstractFraktionAuftraggeberController) {

			AbstractFraktionAuftraggeberController controller = (AbstractFraktionAuftraggeberController) getActualController();
			actualSideJob = controller.getActualSidelineJob();

			if (actualSideJob != null) {

				// null check for origin
				if (actualSideJob.getAuftraggeber() != null)
					// open PartyView
					openPersonOriginView(actualSideJob.getAuftragUri());

			}

		}
	}

	private void openPersonOriginView(String originURI) {

		BeanItemContainer<FraktionAuftraggeber> container = new BeanItemContainer<FraktionAuftraggeber>(
				FraktionAuftraggeber.class);

		String caption = "";
		for (Abgeordneter mdb : mdbList)
			for (Nebentaetigkeit nt : mdb.getNebentaetigkeiten()) {
				if (nt.getAuftragUri() != null && nt.getAuftragUri().equals(originURI)){
					caption = nt.getAuftraggeber();
					container.addItem(new FraktionAuftraggeber(mdb, nt));
				}
			}

		originView.setOriginContainerDataSource(container);
		originView.setPanelCaption(caption);
		setActualPersonView(originView, originController);

		// set URI fragment
		setActualObjectURI(originURI.substring(originURI.indexOf("/b09/") + 5));
		NebeneinkuenfteApplication.getInstance().setURIFragment(getActualObjectURI());

	}

	private void openPersonPersonView() {

		Abgeordneter actualPerson = null;

		// click was done in view controlled by AbstractPersonController
		if (getActualController() instanceof AbstractPersonController) {

			AbstractPersonController controller = (AbstractPersonController) getActualController();
			actualPerson = controller.getActualPerson();

			if (actualPerson != null)
				// open PersonView
				openPersonPersonView(actualPerson);

		} else

		// click was done in view controlled by
		// AbstractFraktionAuftraggeberController
		if (getActualController() instanceof AbstractFraktionAuftraggeberController) {

			AbstractFraktionAuftraggeberController controller = (AbstractFraktionAuftraggeberController) getActualController();
			actualPerson = controller.getActualPerson();

			if (actualPerson != null)
				// open PersonView
				openPersonPersonView(actualPerson);

		}
	}

	private void openPersonPersonView(String personURI) {

		for (Abgeordneter mdb : mdbList)
			if (mdb.getURI().equals(personURI))
				openPersonPersonView(mdb);

	}

	private void openPersonPersonView(Abgeordneter person) {

		BeanItemContainer<Nebentaetigkeit> container = new BeanItemContainer<Nebentaetigkeit>(Nebentaetigkeit.class);

		for (Nebentaetigkeit nt : person.getNebentaetigkeiten())
			container.addItem(nt);

		// create caption for view
		String caption = "";
		caption = caption.concat(person.getForename());
		caption = caption.concat(" ");
		caption = caption.concat(person.getLastname());
		caption = caption.concat(", ");
		caption = caption.concat(person.getFraktion());

		// update container for table and caption
		personView.setSidelineJobContainerDataSource(container);
		personView.setPanelCaption(caption);
		setActualPersonView(personView, personController);

		setActualObjectURI(person.getURI().substring(person.getURI().indexOf("/b09/") + 5));
		NebeneinkuenfteApplication.getInstance().setURIFragment(getActualObjectURI());

	}

	private void openPersonFractionView() {

		Abgeordneter actualPerson = null;

		// click was done in view controlled by AbstractPersonController
		if (getActualController() instanceof AbstractPersonController) {

			AbstractPersonController controller = (AbstractPersonController) getActualController();
			actualPerson = controller.getActualPerson();

			if (actualPerson != null) {

				// null check for fraktion
				if (actualPerson.getFraktionUri() != null)

					// open PartyView
					openPersonFractionView(actualPerson.getFraktionUri());

			}

		} else
		// click was done in view controlled by
		// AbstractFraktionAuftraggeberController
		if (getActualController() instanceof AbstractFraktionAuftraggeberController) {

			AbstractFraktionAuftraggeberController controller = (AbstractFraktionAuftraggeberController) getActualController();
			actualPerson = controller.getActualPerson();

			if (actualPerson != null) {

				// null check for fraktion
				if (actualPerson.getFraktionUri() != null)

					// open PartyView
					openPersonFractionView(actualPerson.getFraktionUri());

			}

		}

	}

	private void openPersonFractionView(String fractionURI) {
		BeanItemContainer<FraktionAuftraggeber> container = new BeanItemContainer<FraktionAuftraggeber>(
				FraktionAuftraggeber.class);

		String caption = "";

		for (Abgeordneter mdb : mdbList)
			if (mdb.getFraktionUri().equals(fractionURI)) {
				caption = mdb.getFraktion();
				for (Nebentaetigkeit nt : mdb.getNebentaetigkeiten())
					container.addItem(new FraktionAuftraggeber(mdb, nt));
			}
		partyView.setPartyContainerDataSource(container);
		partyView.setPanelCaption(caption);
		setActualPersonView(partyView, partyController);

		// set URI fragment
		setActualObjectURI(fractionURI.substring(fractionURI.indexOf("/b09/") + 5));
		NebeneinkuenfteApplication.getInstance().setURIFragment(getActualObjectURI());

	}

	private AbstractView getActualPersonView() {
		return actualPersonView;
	}

	private void setActualPersonView(AbstractView actualView, AbstractController controller) {

		mainFrame.removeTabListener();
		// remove actual view
		int tabIndex = mainFrame.getTabIndex(getActualPersonView());
		mainFrame.removeTab(getActualPersonView());

		// add new view
		mainFrame.addTab(actualView, "Abgeordnete", new ThemeResource("icons/16/user.png"), tabIndex);

		// select new view
		mainFrame.selectTab(actualView);

		// set actual view and controller
		this.actualPersonView = actualView;
		this.setActualController(controller);
		mainFrame.addTabListener();

	}

	public AbstractController getActualController() {
		return actualController;
	}

	public void setActualController(AbstractController actualController) {
		this.actualController = actualController;
	}

	public void handleURIFragment(String fragment) {

		try {

			// split fragment by '/'
			String[] frag = fragment.split("/");
			if (frag.length > 0) {

				// first part targets tabSheet
				String tabSheet = frag[0];

				// member view
				if (tabSheet.equals(IConstants.PERSON_BASIC_VIEW_FRAG)) {
					openPersonBasicView();

				}
				// open fraction view
				else if (tabSheet.equals(IConstants.PERSON_FRACTION_VIEW_FRAG)) {
					if (frag.length > 1)
						openPersonFractionView(IConstants.NAMESPACE + "/" + fragment);
				}

				// open person view
				else if (tabSheet.equals(IConstants.PERSON_PERSON_VIEW_FRAG)) {
					if (frag.length > 1)
						openPersonPersonView(IConstants.NAMESPACE.concat("/").concat(frag[0]).concat("/")
								.concat(URLEncoder.encode(frag[1], "UTF-8")));
				}

				// open source view
				else if (tabSheet.equals(IConstants.PERSON_ORIGIN_VIEW_FRAG)) {
					if (frag.length > 1)
						openPersonOriginView(IConstants.NAMESPACE.concat("/").concat(frag[0]).concat("/")
								.concat(URLEncoder.encode(frag[1], "UTF-8")));
				}

				// open impressum view
				else if (tabSheet.equals(IConstants.IMPRESSUM_VIEW_FRAG)) {
					mainFrame.selectTab(impressumView);
					// open about view
				} else if (tabSheet.equals(IConstants.ABOUT_PROJECT_VIEW_FRAG)) {
					mainFrame.selectTab(aboutProjectView);
				}

			} else {
				mainFrame.selectTab(aboutProjectView);
			}

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

	}

	public String getActualObjectURI() {
		return actualObjectURI;
	}

	public void setActualObjectURI(String actualObjectURI) {
		this.actualObjectURI = actualObjectURI;
	}
}
