package de.ulei.nebeneinkuenfte.ui.controller;

import java.util.Collection;
import java.util.Iterator;

import de.ulei.nebeneinkuenfte.ui.model.Abgeordneter;
import de.ulei.nebeneinkuenfte.ui.view.BasicView;
import de.ulei.nebeneinkuenfte.util.ActionEvent;
import de.ulei.nebeneinkuenfte.util.IActionListener;

public class BasicController extends AbstractPersonController implements
		IActionListener {

	private static final long serialVersionUID = 3304961841215841349L;

	private BasicView basicView;

	public BasicController(BasicView basicView) {

		this.basicView = basicView;
		this.basicView.addListener(this);

		refreshTableFooter();
	}

	public void refreshTableFooter() {
		
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

	@Override
	public void handleAction(ActionEvent event) {

		switch (event.getActionType()) {
		case OPEN_PERSON_BASIC:
			fireEvent(event.getActionType());
			break;
		case EXPORT:
			break;
		case FILTER:
			refreshTableFooter();
			setActualPerson(null);
			break;
		case OPEN_PERSON_PARTY:
			setActualPerson((Abgeordneter) basicView.getBasicTable().getValue());
			fireEvent(event.getActionType());
			break;
		case OPEN_PERSON_PERSON:
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

		basicView.enableOpenPersonButton(actualPerson != null);
		basicView.enableOpenPartyButton(actualPerson != null);

		super.setActualPerson(actualPerson);
	}

}
