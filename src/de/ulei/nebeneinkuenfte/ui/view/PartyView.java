package de.ulei.nebeneinkuenfte.ui.view;

import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.ui.Button;

import de.ulei.nebeneinkuenfte.ui.model.FraktionAuftraggeber;
import de.ulei.nebeneinkuenfte.util.ActionType;
import de.ulei.nebeneinkuenfte.util.IConstants;

public class PartyView extends AbstractView {

	private static final long serialVersionUID = -2940244509197959534L;

	private Button goBackButton;
	private Button openPersonButton;
	private Button openOriginButton;

	public PartyView() {

		super();
		setCaption(String.valueOf(IConstants.PERSON_PARTY_VIEW));
		buildNavigationSection();
		addTableListener();

	}

	private void addTableListener() {

		basicTable.addListener(new ItemClickListener() {

			private static final long serialVersionUID = 1912522754142525003L;

			@Override
			public void itemClick(ItemClickEvent event) {

				fireEvent(ActionType.TABLE_SELECT);

				if (event.isDoubleClick()) {
					if (event.getPropertyId().equals("auftraggeber")) {
						fireEvent(ActionType.OPEN_PERSON_ORIGIN);
					} else if (event.getPropertyId().equals("forename") || event.getPropertyId().equals("lastname")) {
						fireEvent(ActionType.OPEN_PERSON_PERSON);
					}
				}

			}

		});

	}

	private void buildNavigationSection() {

		goBackButton = ButtonFactory.getButton(ButtonFactory.GO_BACK_BUTTON, this);

		openPersonButton = ButtonFactory.getButton(ButtonFactory.OPEN_PERSON_BUTTON, this);
		openPersonButton.setEnabled(false);

		openOriginButton = ButtonFactory.getButton(ButtonFactory.OPEN_ORIGIN_BUTTON, this);
		openOriginButton.setEnabled(false);

		navigationBar.addButton(goBackButton);
		navigationBar.addButton(openPersonButton);
		navigationBar.addButton(openOriginButton);

	}

	public void addParty(FraktionAuftraggeber fraktion) {

		basicTable.addItem(fraktion);
		updateTable();

	}

	public void setPartyContainerDataSource(BeanItemContainer<FraktionAuftraggeber> container) {

		basicTable.setContainerDataSource(container);
		updateTable();

	}

	private void updateTable() {

		basicTable.setVisibleColumns(IConstants.FRAKTION_VISIBLE_COLUMNS);
		basicTable.setColumnHeaders(IConstants.FRAKTION_COLUMN_HEADER);
		enableOpenPersonButton(false);

	}

	public void enableOpenPersonButton(boolean isEnabled) {
		openPersonButton.setEnabled(isEnabled);
	}

	public void enableOpenOriginButton(boolean isEnabled) {
		openOriginButton.setEnabled(isEnabled);
	}

}
