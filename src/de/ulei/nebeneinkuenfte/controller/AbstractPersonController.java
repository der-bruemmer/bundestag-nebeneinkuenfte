package de.ulei.nebeneinkuenfte.controller;

import java.io.Serializable;

import de.ulei.nebeneinkuenfte.model.Abgeordneter;

public abstract class AbstractPersonController extends AbstractController
		implements Serializable {

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
