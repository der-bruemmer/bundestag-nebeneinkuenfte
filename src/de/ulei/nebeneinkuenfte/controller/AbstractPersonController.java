package de.ulei.nebeneinkuenfte.controller;

import de.ulei.nebeneinkuenfte.model.Abgeordneter;

public abstract class AbstractPersonController extends AbstractController {

	private static final long serialVersionUID = -2140021613485837387L;
	private Abgeordneter actualPerson;

	public Abgeordneter getActualPerson() {
		return actualPerson;
	}

	public void setActualPerson(Abgeordneter actualPerson) {
		if (actualPerson != null)
			this.actualPerson = actualPerson;
	}

}
