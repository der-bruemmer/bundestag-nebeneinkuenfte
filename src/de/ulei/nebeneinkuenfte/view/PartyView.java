package de.ulei.nebeneinkuenfte.view;

import com.vaadin.data.Container;
import com.vaadin.data.Property;
import com.vaadin.data.Container.ItemSetChangeEvent;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.terminal.ThemeResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.themes.Runo;

import de.ulei.nebeneinkuenfte.model.Fraktion;
import de.ulei.nebeneinkuenfte.table.BasicTable;
import de.ulei.nebeneinkuenfte.util.ActionEvent;
import de.ulei.nebeneinkuenfte.util.ActionType;
import de.ulei.nebeneinkuenfte.util.IActionListener;
import de.ulei.nebeneinkuenfte.util.IConstants;

public class PartyView extends AbstractView {

	private static final long serialVersionUID = -2940244509197959534L;

	private BasicTable basicTable;
	private Panel tablePanel;

	private Button back;
	private Button personDetails;

	public PartyView() {

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

			private static final long serialVersionUID = 1912522754142525003L;

			@Override
			public void itemClick(ItemClickEvent event) {

				fireEvent(ActionType.TABLE_SELECT);

				if (event.isDoubleClick()) {
					if (event.getPropertyId().equals("auftraggeber")) {
						fireEvent(ActionType.OPEN_PERSON_ORIGIN);
					} else if (event.getPropertyId().equals("forenamePerson")
							|| event.getPropertyId().equals("lastnamePerson")) {
						fireEvent(ActionType.OPEN_PERSON_PERSON);
					}
				}

			}

		});

		basicTable.addListener(new Container.ItemSetChangeListener() {

			private static final long serialVersionUID = -2051968644801920450L;

			@Override
			public void containerItemSetChange(ItemSetChangeEvent event) {
				fireEvent(ActionType.FILTER);

			}
		});

		basicTable.addListener(new Property.ValueChangeListener() {

			private static final long serialVersionUID = -5251038571316350154L;

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

		back = new Button("Return");
		back.setIcon(new ThemeResource("icons/16/edit-undo.png"));
		back.addListener(new Button.ClickListener() {

			private static final long serialVersionUID = -6745818656464476271L;

			@Override
			public void buttonClick(ClickEvent event) {
				fireEvent(ActionType.GO_BACK);

			}
		});

		NavigationBar bar = new NavigationBar();
		bar.addButton(back);
		bar.addButton(personDetails);
		bar.addListener(new IActionListener() {

			private static final long serialVersionUID = 3016750239145013536L;

			@Override
			public void handleAction(ActionEvent event) {
				fireEvent(event.getActionType());

			}
		});

		addComponent(bar, 2, 0);

	}

	private void createTableSection() {

		tablePanel = new Panel("Übersicht");
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

	public void addParty(Fraktion fraktion) {

		basicTable.addItem(fraktion);
		updateTable();

	}

	public void setPartyContainerDataSource(
			BeanItemContainer<Fraktion> container) {

		basicTable.setContainerDataSource(container);
		updateTable();

	}

	public void setTableFooter(Object propertyId, String footer) {
		basicTable.setColumnFooter(propertyId, footer);
	}

	private void updateTable() {

		basicTable.setVisibleColumns(IConstants.FRAKTION_VISIBLE_COLUMNS);
		basicTable.setColumnHeaders(IConstants.FRAKTION_COLUMN_HEADER);
		enablePersonDetailsButton(false);

	}

	public BasicTable getBasicTable() {
		return basicTable;
	}

	public void setPanelCaption(String caption) {
		tablePanel.setCaption(caption);
	}

	public void enablePersonDetailsButton(boolean isEnabled) {
		personDetails.setEnabled(isEnabled);
	}

}
