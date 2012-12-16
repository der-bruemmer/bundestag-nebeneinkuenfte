package de.ulei.nebeneinkuenfte.view;

import com.vaadin.data.Container;
import com.vaadin.data.Container.ItemSetChangeEvent;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.terminal.ThemeResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.themes.Runo;

import de.ulei.nebeneinkuenfte.model.Nebentaetigkeit;
import de.ulei.nebeneinkuenfte.table.BasicTable;
import de.ulei.nebeneinkuenfte.util.ActionEvent;
import de.ulei.nebeneinkuenfte.util.ActionType;
import de.ulei.nebeneinkuenfte.util.IActionListener;
import de.ulei.nebeneinkuenfte.util.IConstants;

public class PersonView extends AbstractView {

	private static final long serialVersionUID = 7516205383090694072L;

	private BasicTable basicTable;
	private Panel tablePanel;

	private Button back;

	public PersonView() {

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

		basicTable.addListener(new Container.ItemSetChangeListener() {

			private static final long serialVersionUID = -5412002348163503472L;

			@Override
			public void containerItemSetChange(ItemSetChangeEvent event) {
				fireEvent(ActionType.FILTER);

			}
		});

	}

	private void createNavigationSection() {

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

	public void addSidelineJob(Nebentaetigkeit sideLine) {

		basicTable.addItem(sideLine);
		updateTable();

	}

	public void setSidelineJobContainerDataSource(
			BeanItemContainer<Nebentaetigkeit> container) {

		basicTable.setContainerDataSource(container);
		updateTable();

	}

	public void setTableFooter(Object propertyId, String footer) {
		basicTable.setColumnFooter(propertyId, footer);
	}

	private void updateTable() {

		basicTable
				.setVisibleColumns(IConstants.NEBENTAETIGKEIT_VISIBLE_COLUMNS);
		basicTable.setColumnHeaders(IConstants.NEBENTAETIGKEIT_COLUMN_HEADER);

	}

	public BasicTable getBasicTable() {
		return basicTable;
	}

	public void setPanelCaption(String caption) {
		tablePanel.setCaption(caption);
	}

}
