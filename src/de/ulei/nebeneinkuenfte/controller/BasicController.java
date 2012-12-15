package de.ulei.nebeneinkuenfte.controller;

import java.util.Collection;
import java.util.Iterator;

import com.vaadin.data.util.BeanItemContainer;

import de.ulei.nebeneinkuenfte.NebeneinkuenfteApplication;
import de.ulei.nebeneinkuenfte.crawler.Abgeordneter;
import de.ulei.nebeneinkuenfte.crawler.BundestagConverter;
import de.ulei.nebeneinkuenfte.util.ActionEvent;
import de.ulei.nebeneinkuenfte.util.IActionListener;
import de.ulei.nebeneinkuenfte.view.BasicView;

public class BasicController extends AbstractPersonController implements
		IActionListener {

	private static final long serialVersionUID = 3304961841215841349L;

	private BasicView basicView;
	private BeanItemContainer<Abgeordneter> personContainer;

	public BasicController(BasicView basicView) {

		this.basicView = basicView;
		this.basicView.addListener(this);

		fetchPersons();
		setTableFooter();
	}

	private void setTableFooter() {

		Collection<?> list = basicView.getBasicTable().getItemIds();

		int min = 0;
		int max = 0;

		basicView.setTableFooter("forename", "Gesamt:");
		basicView.setTableFooter("lastname", String.valueOf(list.size()));

		Abgeordneter a;
		@SuppressWarnings("unchecked")
		Iterator<Abgeordneter> it = (Iterator<Abgeordneter>) list.iterator();
		while (it.hasNext()) {

			a = it.next();

			min += a.getMinZusatzeinkommen();
			max += a.getMaxZusatzeinkommen();

		}

		basicView.setTableFooter("minZusatzeinkommen", String.valueOf(min));
		basicView.setTableFooter("maxZusatzeinkommen", String.valueOf(max));

	}

	private void fetchPersons() {

		String path = NebeneinkuenfteApplication.getInstance().getContext()
				.getBaseDirectory()
				+ "/abgeordnete";
		BundestagConverter conv = new BundestagConverter(
				"http://www.bundestag.de/bundestag/abgeordnete17/alphabet/index.html",
				false, path);

		personContainer = new BeanItemContainer<Abgeordneter>(
				Abgeordneter.class);
		for (Abgeordneter mdb : conv.getAbgeordnete()) {
			personContainer.addItem(mdb);
		}

		basicView.setPersonContainerDataSource(personContainer);

	}

	@Override
	public void handleAction(ActionEvent event) {

		switch (event.getActionType()) {
		case HOME_PERSON:
			fireEvent(event.getActionType());
			break;

		case EXPORT:
			break;
		case FILTER:
			setTableFooter();
			setActualPerson(null);
			break;
		case CLICK_PARTY:
			setActualPerson((Abgeordneter) basicView.getBasicTable().getValue());
			fireEvent(event.getActionType());
			break;
		case CLICK_PERSON:
			setActualPerson((Abgeordneter) basicView.getBasicTable().getValue());
			fireEvent(event.getActionType());
			break;
		case TABLE_SELECT:
			setActualPerson((Abgeordneter) basicView.getBasicTable().getValue());
			break;
		default:
			break;
		}

	}

	@Override
	public void setActualPerson(Abgeordneter actualPerson) {

		basicView.enablePersonDetailsButton(actualPerson != null);
		basicView.enablePartyDetailsButton(actualPerson != null);

		super.setActualPerson(actualPerson);
	}

}
