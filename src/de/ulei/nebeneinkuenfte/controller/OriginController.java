package de.ulei.nebeneinkuenfte.controller;

import de.ulei.nebeneinkuenfte.util.ActionEvent;
import de.ulei.nebeneinkuenfte.util.IActionListener;
import de.ulei.nebeneinkuenfte.view.OriginView;

public class OriginController extends AbstractController implements
		IActionListener {

	private static final long serialVersionUID = -2305074349480901224L;

	private OriginView originView;

	public OriginController(OriginView originView) {

		this.originView = originView;
		this.originView.addListener(this);

	}

	@Override
	public void handleAction(ActionEvent event) {
		// TODO Auto-generated method stub

	}

}
