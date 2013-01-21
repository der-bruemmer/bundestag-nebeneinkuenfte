package de.ulei.nebeneinkuenfte.ui.view;

import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.ui.Button;

import de.ulei.nebeneinkuenfte.ui.model.Nebentaetigkeit;
import de.ulei.nebeneinkuenfte.util.ActionType;
import de.ulei.nebeneinkuenfte.util.IConstants;

public class PersonView extends AbstractView {

	private static final long serialVersionUID = 7516205383090694072L;

	private Button openOriginButton;
	private Button openMapButton;
	
	public PersonView() {

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
					if (event.getPropertyId().equals("auftraggeber")) {
						fireEvent(ActionType.OPEN_PERSON_ORIGIN);
					}
				}

			}

		});

	}

	private void createNavigationSection() {

		openMapButton = ButtonFactory.getButton(ButtonFactory.OPEN_MAP_BUTTON, this);

		openOriginButton = ButtonFactory.getButton(ButtonFactory.OPEN_ORIGIN_BUTTON, this);
		openOriginButton.setEnabled(false);

		navigationBar.addButton(openMapButton);
		navigationBar.addButton(openOriginButton);

	}

	public void addSidelineJob(Nebentaetigkeit sideLine) {

		basicTable.addItem(sideLine);
		updateTable();

	}

	public void setSidelineJobContainerDataSource(BeanItemContainer<Nebentaetigkeit> container) {

		basicTable.setContainerDataSource(container);
		updateTable();

	}

	private void updateTable() {

		basicTable.setVisibleColumns(IConstants.NEBENTAETIGKEIT_VISIBLE_COLUMNS);
		basicTable.setColumnHeaders(IConstants.NEBENTAETIGKEIT_COLUMN_HEADER);

	}

	public void enableOpenOriginButton(boolean isEnabled) {
		openOriginButton.setEnabled(isEnabled);
	}

}
