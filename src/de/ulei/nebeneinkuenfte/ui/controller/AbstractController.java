package de.ulei.nebeneinkuenfte.ui.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import de.ulei.nebeneinkuenfte.util.ActionEvent;
import de.ulei.nebeneinkuenfte.util.ActionType;
import de.ulei.nebeneinkuenfte.util.IActionListener;

public abstract class AbstractController implements Serializable {

	private static final long serialVersionUID = 1526145583438863070L;
	/**
	 * List of IActionListener
	 */

	private List<IActionListener> listeners = null;

	/**
	 * 
	 * Fire an ActionEvent that represents that last happened action
	 * 
	 * @param actionType
	 *            Type of the Action
	 */

	public void fireEvent(ActionType actionType) {
		if (listeners != null) {
			for (IActionListener listener : listeners) {
				listener.handleAction(new ActionEvent(actionType));
			}
		}
	}

	/**
	 * Add an IActionListener
	 * 
	 * @param actionListener
	 *            IActionListener that should be added
	 */

	public void addListener(IActionListener actionListener) {
		if (listeners == null)
			listeners = new ArrayList<IActionListener>();

		listeners.add(actionListener);

	}

	/**
	 * Remove an IActionListener
	 * 
	 * @param actionListener
	 *            IActionListener that should be removed
	 */

	public void removeListener(IActionListener actionListener) {
		if (listeners != null)
			listeners.remove(actionListener);

	}

	/**
	 * Remove all IActionListener
	 * 
	 */

	public void removeAllListener() {
		if (listeners != null)
			listeners.clear();

	}

}
