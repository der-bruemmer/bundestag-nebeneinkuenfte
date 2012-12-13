package de.ulei.nebeneinkuenfte.util;

public class ActionEvent {

	private final ActionType actionType;

	public ActionEvent(ActionType actionType) {
		this.actionType = actionType;
	}

	public ActionType getActionType() {
		return actionType;
	}

}
