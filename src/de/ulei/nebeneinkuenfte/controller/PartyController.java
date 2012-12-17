package de.ulei.nebeneinkuenfte.controller;

import de.ulei.nebeneinkuenfte.model.Abgeordneter;
import de.ulei.nebeneinkuenfte.model.Fraktion;
import de.ulei.nebeneinkuenfte.util.ActionEvent;
import de.ulei.nebeneinkuenfte.util.IActionListener;
import de.ulei.nebeneinkuenfte.view.PartyView;

public class PartyController extends AbstractPersonController implements
		IActionListener {

	private static final long serialVersionUID = 8779244993116572692L;

	private PartyView partyView;

	public PartyController(PartyView partyView) {

		this.partyView = partyView;
		this.partyView.addListener(this);

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
			setActualPerson(null);
			break;
		case OPEN_PERSON_PERSON:
			Fraktion fraktion = (Fraktion) partyView.getBasicTable().getValue();
			if (fraktion != null) {
				setActualPerson(fraktion.getAbgeordneter());
				fireEvent(event.getActionType());
			}
			break;
		case TABLE_SELECT:
			Fraktion frak = (Fraktion) partyView.getBasicTable().getValue();
			if (frak != null)
				setActualPerson(frak.getAbgeordneter());
			break;
		default:
			break;
		}

	}
	
	@Override
	public void setActualPerson(Abgeordneter actualPerson) {

		partyView.enableOpenPersonButton(actualPerson != null);
		super.setActualPerson(actualPerson);
		
	}

}
