package de.ulei.nebeneinkuenfte.ui.controller;

import de.ulei.nebeneinkuenfte.ui.model.Nebentaetigkeit;
import de.ulei.nebeneinkuenfte.ui.view.PersonView;
import de.ulei.nebeneinkuenfte.util.ActionEvent;
import de.ulei.nebeneinkuenfte.util.IActionListener;

public class PersonController extends AbstractSidelineJobController implements
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
		case FILTER:
			// setTableFooter();
			break;
		case OPEN_PERSON_ORIGIN:
			setActualSidelineJob((Nebentaetigkeit) personView.getBasicTable()
					.getValue());
			fireEvent(event.getActionType());
			break;
		case TABLE_SELECT:
			setActualSidelineJob((Nebentaetigkeit) personView.getBasicTable()
					.getValue());
			break;
		default:
			break;
		}
	}

	@Override
	public void setActualSidelineJob(Nebentaetigkeit actualSidelineJob) {

		personView.enableOpenOriginButton(actualSidelineJob != null);
		super.setActualSidelineJob(actualSidelineJob);
		
	}
}
