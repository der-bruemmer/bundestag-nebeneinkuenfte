package de.ulei.nebeneinkuenfte.controller;

import java.io.File;

import com.vaadin.data.util.BeanItemContainer;

import de.ulei.nebeneinkuenfte.crawler.Abgeordneter;
import de.ulei.nebeneinkuenfte.crawler.BundestagConverter;
import de.ulei.nebeneinkuenfte.util.ActionEvent;
import de.ulei.nebeneinkuenfte.util.IActionListener;
import de.ulei.nebeneinkuenfte.view.BasicView;

public class BasicController extends AbstractController implements
		IActionListener {

	private static final long serialVersionUID = 3304961841215841349L;

	private BasicView basicView;
	private BeanItemContainer<Abgeordneter> personContainer;

	public BasicController(BasicView view) {

		this.basicView = view;
		this.basicView.addListener(this);

		fetchPersons();
		view.setPersonContainerDataSource(personContainer);

	}

	private void fetchPersons() {

		BundestagConverter conv = new BundestagConverter(
				"http://www.bundestag.de/bundestag/abgeordnete17/alphabet/index.html",
				false);
		// conv.writeNebentaetigkeitenToFile();

		personContainer = new BeanItemContainer<Abgeordneter>(
				Abgeordneter.class);
		for (Abgeordneter mdb : conv.getAbgeordnete()) {
			personContainer.addItem(mdb);
		}

	}

	@Override
	public void handleAction(ActionEvent event) {
		switch (event.getActionType()) {
		case HOME:
			fireEvent(event.getActionType());
			break;
		default:
			break;
		}

	}

	public static void main(String[] args) {
		File mdbFolder = new File("./abgeordnete");
		for (File mdbFile : mdbFolder.listFiles()) {
			System.out.println(mdbFile);
		}
	}

}
