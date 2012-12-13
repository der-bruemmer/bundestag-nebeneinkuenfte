package de.ulei.nebeneinkuenfte.controller;

import de.ulei.nebeneinkuenfte.util.ActionEvent;
import de.ulei.nebeneinkuenfte.util.IActionListener;
import de.ulei.nebeneinkuenfte.view.PartyView;

public class PartyController extends AbstractController implements
		IActionListener {

	private static final long serialVersionUID = 8779244993116572692L;

	private PartyView partyView;

	public PartyController(PartyView partyView) {

		this.partyView = partyView;
		this.partyView.addListener(this);

	}

	@Override
	public void handleAction(ActionEvent event) {
		// TODO Auto-generated method stub

	}

}
