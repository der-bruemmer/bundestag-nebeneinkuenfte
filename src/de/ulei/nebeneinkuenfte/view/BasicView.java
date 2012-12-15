package de.ulei.nebeneinkuenfte.view;

import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.terminal.ExternalResource;
import com.vaadin.ui.Panel;
import com.vaadin.ui.themes.Runo;

import de.ulei.nebeneinkuenfte.NebeneinkuenfteApplication;
import de.ulei.nebeneinkuenfte.crawler.Abgeordneter;
import de.ulei.nebeneinkuenfte.table.BasicTable;
import de.ulei.nebeneinkuenfte.util.ActionEvent;
import de.ulei.nebeneinkuenfte.util.IActionListener;

public class BasicView extends AbstractView {

	private static final long serialVersionUID = -7926845225077002139L;

	private BasicTable basicTable;

	public BasicView() {

		setColumnExpandRatio(0, 0.6f);
		setColumnExpandRatio(1, 1.2f);
		setColumnExpandRatio(2, 0.3f);
		setColumns(3);
		setSizeFull();

		createTableSection();
		createNavigationSection();
		// addTableListener();

	}

	private void addTableListener() {

		basicTable.addListener(new ItemClickListener() {

			private static final long serialVersionUID = -793315320795502970L;

			@Override
			public void itemClick(ItemClickEvent event) {
				// TODO Auto-generated method stub
				
			}

		});

	}

	private void createNavigationSection() {

		NavigationBar bar = new NavigationBar();
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

		Panel tablePanel = new Panel("Gesamtübersicht");
		tablePanel.setStyleName(Runo.PANEL_LIGHT);
		tablePanel.setScrollable(true);
		tablePanel.setImmediate(true);
		tablePanel.setSizeFull();

		// table for user data
		basicTable = new BasicTable();
		basicTable.setWidth("100%");
		basicTable.setHeight("100%");
		// basicTable.setPageLength(15);

		tablePanel.addComponent(basicTable);
		addComponent(tablePanel, 0, 0, 1, 0);

	}

	public void addPerson(Abgeordneter person) {

		basicTable.addItem(person);
		basicTable.setVisibleColumns(new String[] { "forename", "lastname",
				"homepage", "email", "fraktion", "minZusatzeinkommen",
				"maxZusatzeinkommen" });
		basicTable.setColumnHeaders(new String[] { "Vorname", "Nachname",
				"Homepage", "Email", "Partei", "Zusatzeinkommen (min)",
				"Zusatzeinkommen (max)" });

	}

	public void setPersonContainerDataSource(
			BeanItemContainer<Abgeordneter> container) {

		basicTable.setContainerDataSource(container);
		basicTable.setVisibleColumns(new String[] { "forename", "lastname",
				"homepage", "email", "fraktion", "minZusatzeinkommen",
				"maxZusatzeinkommen" });
		basicTable.setColumnHeaders(new String[] { "Vorname", "Nachname",
				"Homepage", "Email", "Partei", "Zusatzeinkommen (min)",
				"Zusatzeinkommen (max)" });

	}

}
