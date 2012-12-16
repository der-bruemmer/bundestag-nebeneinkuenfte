package de.ulei.nebeneinkuenfte.view;

import com.vaadin.data.Container;
import com.vaadin.data.Container.ItemSetChangeEvent;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.terminal.ThemeResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.themes.Runo;

import de.ulei.nebeneinkuenfte.model.Abgeordneter;
import de.ulei.nebeneinkuenfte.table.BasicTable;
import de.ulei.nebeneinkuenfte.util.ActionEvent;
import de.ulei.nebeneinkuenfte.util.ActionType;
import de.ulei.nebeneinkuenfte.util.IActionListener;
import de.ulei.nebeneinkuenfte.util.IConstants;

public class BasicView extends AbstractView {

	private static final long serialVersionUID = -7926845225077002139L;

	private BasicTable basicTable;
	private Button personDetails;
	private Button partyDetails;

	public BasicView() {

		setImmediate(true);
		setColumnExpandRatio(0, 0.6f);
		setColumnExpandRatio(1, 1.2f);
		setColumnExpandRatio(2, 0.3f);
		setColumns(3);
		setSizeFull();

		createTableSection();
		createNavigationSection();
		addTableListener();

	}

	private void addTableListener() {

		basicTable.addListener(new ItemClickListener() {

			private static final long serialVersionUID = -793315320795502970L;

			@Override
			public void itemClick(ItemClickEvent event) {

				fireEvent(ActionType.TABLE_SELECT);

				if (event.isDoubleClick()) {
					if (event.getPropertyId().equals("fraktion")) {
						fireEvent(ActionType.OPEN_PERSON_PARTY);
					} else if (event.getPropertyId().equals("forename")
							|| event.getPropertyId().equals("lastname")) {
						fireEvent(ActionType.OPEN_PERSON_PERSON);
					}
				}

			}

		});

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

	}

	private void createNavigationSection() {

		personDetails = new Button("Open Person");
		personDetails.setImmediate(true);
		personDetails.setIcon(new ThemeResource("icons/16/user.png"));
		personDetails.setEnabled(false);
		personDetails.addListener(new Button.ClickListener() {

			private static final long serialVersionUID = 5261319030685442466L;

			@Override
			public void buttonClick(ClickEvent event) {
				fireEvent(ActionType.OPEN_PERSON_PERSON);
			}
		});

		partyDetails = new Button("Open Party");
		partyDetails.setIcon(new ThemeResource("icons/16/users.png"));
		partyDetails.setEnabled(false);
		partyDetails.addListener(new Button.ClickListener() {

			private static final long serialVersionUID = 5261319030685442466L;

			@Override
			public void buttonClick(ClickEvent event) {
				fireEvent(ActionType.OPEN_PERSON_PARTY);
			}
		});

		NavigationBar bar = new NavigationBar();
		bar.addListener(new IActionListener() {

			private static final long serialVersionUID = 3016750239145013536L;

			@Override
			public void handleAction(ActionEvent event) {
				fireEvent(event.getActionType());
			}
		});

		bar.addButton(personDetails);
		bar.addButton(partyDetails);

		addComponent(bar, 2, 0);

	}

	private void createTableSection() {

		Panel tablePanel = new Panel("Gesamtübersicht");
		tablePanel.setStyleName(Runo.PANEL_LIGHT);
		tablePanel.setScrollable(true);
		tablePanel.setImmediate(true);
		tablePanel.setSizeFull();

		basicTable = new BasicTable();
		basicTable.setWidth("100%");
		basicTable.setHeight("100%");
		basicTable.setFooterVisible(true);

		tablePanel.addComponent(basicTable);
		addComponent(tablePanel, 0, 0, 1, 0);

	}

	public void addPerson(Abgeordneter person) {

		basicTable.addItem(person);
		updateTable();

	}

	public void setPersonContainerDataSource(
			BeanItemContainer<Abgeordneter> container) {

		basicTable.setContainerDataSource(container);
		updateTable();

	}

	public void setTableFooter(Object propertyId, String footer) {
		basicTable.setColumnFooter(propertyId, footer);
	}

	private void updateTable() {

		basicTable.setVisibleColumns(IConstants.ABGEORDNETER_VISIBLE_COLUMNS);
		basicTable.setColumnHeaders(IConstants.ABGEORDNETER_COLUMN_HEADER);
		basicTable.setColumnCollapsed("wahlkreisUri", true);
		basicTable.setColumnCollapsed("email", true);
		enablePersonDetailsButton(false);
		enablePartyDetailsButton(false);
		
	}

	public void enablePersonDetailsButton(boolean isEnabled) {
		personDetails.setEnabled(isEnabled);
	}

	public void enablePartyDetailsButton(boolean isEnabled) {
		partyDetails.setEnabled(isEnabled);
	}

	public BasicTable getBasicTable() {
		return basicTable;
	}

}
