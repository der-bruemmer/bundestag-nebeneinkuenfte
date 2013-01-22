package de.ulei.nebeneinkuenfte.ui.view;

import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.ui.Button;

import de.ulei.nebeneinkuenfte.ui.model.Abgeordneter;
import de.ulei.nebeneinkuenfte.util.ActionType;
import de.ulei.nebeneinkuenfte.util.IConstants;

public class BasicView extends AbstractView {

	private static final long serialVersionUID = -7926845225077002139L;

	private Button openPersonButton;
	private Button openPartyButton;

	public BasicView() {

		super();
		setCaption(String.valueOf(IConstants.PERSON_BASIC_VIEW));
		buildNavigationSection();
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

	}

	private void buildNavigationSection() {

		openPersonButton = ButtonFactory.getButton(
				ButtonFactory.OPEN_PERSON_BUTTON, this);
		openPersonButton.setEnabled(false);

		openPartyButton = ButtonFactory.getButton(
				ButtonFactory.OPEN_PARTY_BUTTON, this);
		openPartyButton.setEnabled(false);

		navigationBar.addButton(openPersonButton);
		navigationBar.addButton(openPartyButton);

//		addComponent(navigationBar, 2, 0);

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

	private void updateTable() {

		basicTable.setVisibleColumns(IConstants.ABGEORDNETER_VISIBLE_COLUMNS);
		basicTable.setColumnHeaders(IConstants.ABGEORDNETER_COLUMN_HEADER);
		basicTable.setSortContainerPropertyId("minZusatzeinkommen");
		basicTable.setSortAscending(false);
		basicTable.setColumnCollapsed("wahlkreisUri", true);
//		basicTable.setColumnCollapsed("email", true);
		enableOpenPersonButton(false);
		enableOpenPartyButton(false);

	}

	public void enableOpenPersonButton(boolean isEnabled) {
		openPersonButton.setEnabled(isEnabled);
	}

	public void enableOpenPartyButton(boolean isEnabled) {
		openPartyButton.setEnabled(isEnabled);
	}

}
