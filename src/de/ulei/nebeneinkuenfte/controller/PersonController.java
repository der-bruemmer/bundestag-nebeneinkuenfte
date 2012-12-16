package de.ulei.nebeneinkuenfte.controller;

import de.ulei.nebeneinkuenfte.model.Abgeordneter;
import de.ulei.nebeneinkuenfte.util.ActionEvent;
import de.ulei.nebeneinkuenfte.util.IActionListener;
import de.ulei.nebeneinkuenfte.view.PersonView;

public class PersonController extends AbstractPersonController implements
		IActionListener {

	private static final long serialVersionUID = 7065013836922459523L;

	private PersonView personView;

	public PersonController(PersonView personView) {

		this.personView = personView;
		this.personView.addListener(this);

	}

	@Override
	public void handleAction(ActionEvent event) {

		switch (event.getActionType()) {
		case OPEN_PERSON_BASIC:
			fireEvent(event.getActionType());
			break;
		case GO_BACK:
			fireEvent(event.getActionType());
			break;
		case EXPORT:
			break;
		case FILTER:
			// setTableFooter();
			break;
		case OPEN_PERSON_PARTY:
			setActualPerson((Abgeordneter) personView.getBasicTable()
					.getValue());
			fireEvent(event.getActionType());
			break;
		case OPEN_PERSON_PERSON:
			setActualPerson((Abgeordneter) personView.getBasicTable()
					.getValue());
			fireEvent(event.getActionType());
			break;
		default:
			break;
		}
	}
}
