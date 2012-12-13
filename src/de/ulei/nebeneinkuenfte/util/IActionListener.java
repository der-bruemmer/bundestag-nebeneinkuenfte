package de.ulei.nebeneinkuenfte.util;

import java.io.Serializable;

import de.ulei.nebeneinkuenfte.view.AbstractView;

/**
 * Interface for {@link AbstractView} listener.
 * 
 * @author Sebastian Lipperts
 * 
 */

public interface IActionListener extends Serializable {

	
	/**
	 * Handle event that represents an happened action 
	 * 
	 * @param event Fired event
	 */
	public void handleAction(ActionEvent event);

}
