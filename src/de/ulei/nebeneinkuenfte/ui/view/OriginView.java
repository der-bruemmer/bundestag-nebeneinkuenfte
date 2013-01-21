package de.ulei.nebeneinkuenfte.ui.view;

import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.ui.Button;

import de.ulei.nebeneinkuenfte.ui.model.FraktionAuftraggeber;
import de.ulei.nebeneinkuenfte.util.ActionType;
import de.ulei.nebeneinkuenfte.util.IConstants;

public class OriginView extends AbstractView {

	private static final long serialVersionUID = -1127511264494383725L;

	private Button openPersonButton;
	private Button openPartyButton;

	public OriginView() {

		setCaption(String.valueOf(IConstants.PERSON_ORIGIN_VIEW));
		createNavigationSection();
		addTableListener();

	}

	private void addTableListener() {

		basicTable.addListener(new ItemClickListener() {

			private static final long serialVersionUID = 1912522754142525003L;

			@Override
			public void itemClick(ItemClickEvent event) {

				fireEvent(ActionType.TABLE_SELECT);

				if (event.isDoubleClick()) {
					if (event.getPropertyId().equals("fraktion")) {
						fireEvent(ActionType.OPEN_PERSON_PARTY);
					} else if (event.getPropertyId().equals("forename") || event.getPropertyId().equals("lastname")) {
						fireEvent(ActionType.OPEN_PERSON_PERSON);
					}
				}

			}

		});

	}

	private void createNavigationSection() {

		openPersonButton = ButtonFactory.getButton(ButtonFactory.OPEN_PERSON_BUTTON, this);
		openPersonButton.setEnabled(false);

		openPartyButton = ButtonFactory.getButton(ButtonFactory.OPEN_PARTY_BUTTON, this);
		openPartyButton.setEnabled(false);

		navigationBar.addButton(openPersonButton);
		navigationBar.addButton(openPartyButton);

	}

	public void addOrigin(FraktionAuftraggeber origin) {

		basicTable.addItem(origin);
		updateTable();

	}

	public void setOriginContainerDataSource(BeanItemContainer<FraktionAuftraggeber> container) {

		basicTable.setContainerDataSource(container);
		updateTable();

	}

	private void updateTable() {

		basicTable.setVisibleColumns(IConstants.AUFTRAGGEBER_VISIBLE_COLUMNS);
		basicTable.setColumnHeaders(IConstants.AUFTRAGGEBER_COLUMN_HEADER);

	}

	public void enableOpenPersonButton(boolean isEnabled) {
		openPersonButton.setEnabled(isEnabled);
	}

	public void enableOpenPartyButton(boolean isEnabled) {
		openPartyButton.setEnabled(isEnabled);
	}

}
