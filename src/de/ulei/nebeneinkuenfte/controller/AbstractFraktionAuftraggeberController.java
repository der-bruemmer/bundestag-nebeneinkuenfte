package de.ulei.nebeneinkuenfte.controller;

import de.ulei.nebeneinkuenfte.model.Abgeordneter;
import de.ulei.nebeneinkuenfte.model.Nebentaetigkeit;

public abstract class AbstractFraktionAuftraggeberController extends
		AbstractController {

	private static final long serialVersionUID = -4571990585854309497L;
	private Abgeordneter actualPerson;
	private Nebentaetigkeit actualSidelineJob;

	public Abgeordneter getActualPerson() {
		return actualPerson;
	}

	public void setActualPerson(Abgeordneter actualPerson) {
		if (actualPerson != null)
			this.actualPerson = actualPerson;
	}

	public Nebentaetigkeit getActualSidelineJob() {
		return actualSidelineJob;
	}

	public void setActualSidelineJob(Nebentaetigkeit actualSidelineJob) {
		if (actualSidelineJob != null)
			this.actualSidelineJob = actualSidelineJob;
	}

}
