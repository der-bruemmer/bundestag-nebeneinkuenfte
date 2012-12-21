package de.ulei.nebeneinkuenfte.controller;

import de.ulei.nebeneinkuenfte.model.Abgeordneter;
import de.ulei.nebeneinkuenfte.model.FraktionAuftraggeber;
import de.ulei.nebeneinkuenfte.util.ActionEvent;
import de.ulei.nebeneinkuenfte.util.IActionListener;
import de.ulei.nebeneinkuenfte.view.OriginView;

public class OriginController extends AbstractFraktionAuftraggeberController
		implements IActionListener {

	private static final long serialVersionUID = -2305074349480901224L;

	private OriginView originView;

	public OriginController(OriginView originView) {

		this.originView = originView;
		this.originView.addListener(this);

	}

	@Override
	public void handleAction(ActionEvent event) {

		FraktionAuftraggeber origin = null;
		switch (event.getActionType()) {
		case OPEN_PERSON_BASIC:
			fireEvent(event.getActionType());
			break;
		case GO_BACK:
			fireEvent(event.getActionType());
			break;
		case FILTER:
			setActualSidelineJob(null);
			setActualPerson(null);
			break;
		case OPEN_PERSON_PERSON:
			origin = (FraktionAuftraggeber) originView.getBasicTable()
					.getValue();

			if (origin != null) {

				setActualSidelineJob(origin.getNebentaetigkeit());
				setActualPerson(origin.getAbgeordneter());
				fireEvent(event.getActionType());

			}
			break;
		case OPEN_PERSON_PARTY:
			origin = (FraktionAuftraggeber) originView.getBasicTable()
					.getValue();

			if (origin != null) {

				setActualSidelineJob(origin.getNebentaetigkeit());
				setActualPerson(origin.getAbgeordneter());
				fireEvent(event.getActionType());

			}
			break;
		case TABLE_SELECT:
			origin = (FraktionAuftraggeber) originView.getBasicTable()
					.getValue();
			if (origin != null) {

				setActualSidelineJob(origin.getNebentaetigkeit());
				setActualPerson(origin.getAbgeordneter());

			}
			break;
		default:
			break;
		}

	}

	@Override
	public void setActualPerson(Abgeordneter actualPerson) {

		boolean enable = actualPerson != null;

		originView.enableOpenPersonButton(enable);
		originView.enableOpenPartyButton(enable);

		super.setActualPerson(actualPerson);

	}

}
