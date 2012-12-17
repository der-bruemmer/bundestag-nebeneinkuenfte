package de.ulei.nebeneinkuenfte.controller;

import de.ulei.nebeneinkuenfte.model.Nebentaetigkeit;

public class AbstractSidelineJobController extends AbstractController {

	private static final long serialVersionUID = -7462769753233331894L;
	private Nebentaetigkeit actualSidelineJob;

	public Nebentaetigkeit getActualSidelineJob() {
		return actualSidelineJob;
	}

	public void setActualSidelineJob(Nebentaetigkeit actualSidelineJob) {
		if (actualSidelineJob != null)
			this.actualSidelineJob = actualSidelineJob;
	}

}
