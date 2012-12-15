package de.ulei.nebeneinkuenfte.util;

/**
 * Enum Class with possible event types
 * 
 * 
 * @author Sebastian Lippert
 * 
 */

public enum ActionType {

	/**
	 * Home button was called to get back to basic table
	 */
	HOME_PERSON,

	/**
	 * Table was filtered
	 */

	FILTER,

	/**
	 * Export table content
	 */

	EXPORT,

	/**
	 * click on person occurred
	 */

	CLICK_PERSON,

	/**
	 * click on party occurred
	 */

	CLICK_PARTY,

	/**
	 * item from table was selected
	 */

	TABLE_SELECT, 
	
	/**
	 * open a selected person
	 */
	
	OPEN_PERSON, 
	
	/**
	 * open party of selected person
	 */
	
	OPEN_PARTY
}
