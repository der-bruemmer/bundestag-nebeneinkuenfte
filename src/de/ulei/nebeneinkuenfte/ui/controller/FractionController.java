package de.ulei.nebeneinkuenfte.ui.controller;

import de.ulei.nebeneinkuenfte.ui.model.Abgeordneter;
import de.ulei.nebeneinkuenfte.ui.model.FraktionAuftraggeber;
import de.ulei.nebeneinkuenfte.ui.view.FractionView;
import de.ulei.nebeneinkuenfte.util.ActionEvent;
import de.ulei.nebeneinkuenfte.util.IActionListener;

public class FractionController extends AbstractFraktionAuftraggeberController implements IActionListener {

	private static final long serialVersionUID = 8779244993116572692L;

	private FractionView partyView;

	public FractionController(FractionView partyView) {

		this.partyView = partyView;
		this.partyView.addListener(this);

	}

	@Override
	public void handleAction(ActionEvent event) {

		FraktionAuftraggeber fraktion = null;

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
			fraktion = (FraktionAuftraggeber) partyView.getBasicTable().getValue();

			if (fraktion != null) {

				setActualPerson(fraktion.getAbgeordneter());
				setActualSidelineJob(fraktion.getNebentaetigkeit());
				fireEvent(event.getActionType());

			}
			break;
		case OPEN_PERSON_ORIGIN:
			fraktion = (FraktionAuftraggeber) partyView.getBasicTable().getValue();

			if (fraktion != null) {

				setActualPerson(fraktion.getAbgeordneter());
				setActualSidelineJob(fraktion.getNebentaetigkeit());
				fireEvent(event.getActionType());

			}
			break;
		case TABLE_SELECT:
			fraktion = (FraktionAuftraggeber) partyView.getBasicTable().getValue();
			if (fraktion != null)
				setActualPerson(fraktion.getAbgeordneter());
			break;
		default:
			break;
		}

	}

	@Override
	public void setActualPerson(Abgeordneter actualPerson) {

		boolean enable = actualPerson != null;

		partyView.enableOpenPersonButton(enable);
		partyView.enableOpenOriginButton(enable);

		super.setActualPerson(actualPerson);

	}

}
