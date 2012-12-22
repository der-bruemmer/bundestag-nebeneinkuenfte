package de.ulei.nebeneinkuenfte.ui.view;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.vaadin.data.Container;
import com.vaadin.data.Property;
import com.vaadin.data.Container.ItemSetChangeEvent;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.themes.Runo;

import de.ulei.nebeneinkuenfte.ui.table.BasicTable;
import de.ulei.nebeneinkuenfte.util.ActionEvent;
import de.ulei.nebeneinkuenfte.util.ActionType;
import de.ulei.nebeneinkuenfte.util.IActionListener;

/**
 * Abstract GridLayout with a {@link BasicTable} and EventHaendling.
 * 
 * @author Sebastian Lippert
 * 
 */

public abstract class AbstractView extends GridLayout implements Serializable {

	private static final long serialVersionUID = -3320849668047624738L;

	protected BasicTable basicTable;
	protected NavigationBar navigationBar;
	private Panel tablePanel;

	public AbstractView() {

		setImmediate(true);
		setColumnExpandRatio(0, 0.6f);
		setColumnExpandRatio(1, 1.2f);
		setColumnExpandRatio(2, 0.3f);
		setColumns(3);
		setSizeFull();

		createTableSection();
		createNavigationSection();

	}

	private void createTableSection() {

		tablePanel = new Panel("Gesamtübersicht");
		tablePanel.setStyleName(Runo.PANEL_LIGHT);
		tablePanel.setScrollable(true);
		tablePanel.setImmediate(true);
		tablePanel.setSizeFull();

		basicTable = new BasicTable();
		basicTable.setWidth("100%");
		basicTable.setHeight("100%");
		basicTable.setFooterVisible(true);

		basicTable.addListener(new Container.ItemSetChangeListener() {

			private static final long serialVersionUID = -5412002348163503472L;

			@Override
			public void containerItemSetChange(ItemSetChangeEvent event) {
				fireEvent(ActionType.FILTER);

			}
		});

		basicTable.addListener(new Property.ValueChangeListener() {

			private static final long serialVersionUID = 3455396944268723575L;

			@Override
			public void valueChange(ValueChangeEvent event) {

				fireEvent(ActionType.TABLE_SELECT);

			}
		});

		tablePanel.addComponent(basicTable);
		addComponent(tablePanel, 0, 0, 1, 0);

	}

	private void createNavigationSection() {

		navigationBar = new NavigationBar(this);
		// navigationBar.addListener(new IActionListener() {
		//
		// private static final long serialVersionUID = 3016750239145013536L;
		//
		// @Override
		// public void handleAction(ActionEvent event) {
		// fireEvent(event.getActionType());
		// }
		// });

		addComponent(navigationBar, 2, 0);

	}

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

	/**
	 * Get {@link BasicTable} of the controller
	 * 
	 * @return BaisTable
	 */

	public BasicTable getBasicTable() {
		return basicTable;
	}

	/**
	 * Set footer of the {@link BasicTable}
	 * 
	 * @param propertyId
	 *            Id of the column
	 * @param footer
	 *            Footer message
	 */

	public void setTableFooter(Object propertyId, String footer) {
		basicTable.setColumnFooter(propertyId, footer);
	}

	/**
	 * Set caption of the table containing panel
	 * 
	 * @param caption
	 *            Caption
	 */

	public void setPanelCaption(String caption) {
		tablePanel.setCaption(caption);
	}

}
