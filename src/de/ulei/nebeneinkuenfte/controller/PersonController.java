package de.ulei.nebeneinkuenfte.controller;

import de.ulei.nebeneinkuenfte.util.ActionEvent;
import de.ulei.nebeneinkuenfte.util.IActionListener;
import de.ulei.nebeneinkuenfte.view.PersonView;

public class PersonController extends AbstractController implements
		IActionListener {

	private static final long serialVersionUID = 7065013836922459523L;

	private PersonView personView;

	public PersonController(PersonView personView) {

		this.personView = personView;
		this.personView.addListener(this);

	}

	@Override
	public void handleAction(ActionEvent event) {
		// TODO Auto-generated method stub

	}

}
