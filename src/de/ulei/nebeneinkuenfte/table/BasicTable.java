package de.ulei.nebeneinkuenfte.table;

import org.tepi.filtertable.FilterTable;

import com.vaadin.data.Container;
import com.vaadin.data.Property;
import com.vaadin.terminal.ExternalResource;
import com.vaadin.terminal.ThemeResource;
import com.vaadin.ui.Component;
import com.vaadin.ui.Field;
import com.vaadin.ui.Link;
import com.vaadin.ui.TableFieldFactory;

import de.ulei.nebeneinkuenfte.model.Abgeordneter;

public class BasicTable extends FilterTable {

	private static final long serialVersionUID = 1037222921294665317L;

	private ThemeResource mailResource = new ThemeResource("icons/16/mail.png");

	public BasicTable() {

		setImmediate(true);
		setFilterGenerator(new TableFilterGenerator());
		setFilterDecorator(new TableFilterDecorator());
		setFilterBarVisible(true);

		setEditable(true);
		setTableFieldFactory(new TableFieldFactory() {

			private static final long serialVersionUID = -3519781089291834981L;

			@Override
			public Field createField(Container container, Object itemId,
					Object propertyId, Component uiContext) {

				return null;
			}
		});

		setColumnCollapsingAllowed(true);
		setAlwaysRecalculateColumnWidths(true);
		setSelectable(true);
		setSizeFull();

	}

	@Override
	protected Object getPropertyValue(Object rowId, Object colId,
			Property property) {

		// handle homepage link
		if (colId.equals("homepage")) {

			Link link;
			String url = (String) property.getValue();
			if (url != null) {

				link = new Link(url, new ExternalResource(url));
				link.setTargetName("_blank");
				return link;

			}

		}
		// handle email link
		else if (colId.equals("email")) {

			Link link;
			String url = (String) property.getValue();
			if (url != null) {

				link = new Link(url, new ExternalResource(url));
				link.setCaption("Email senden");
				link.setIcon(mailResource);
				link.setTargetName("_blank");
				return link;

			}

		}
		// handle wahlkreis link
		else if (colId.equals("wahlkreisUri")) {

			Abgeordneter abgeordneter = (Abgeordneter) rowId;
			Link link = null;

			String url = (String) property.getValue();
			if (url != null) {

				link = new Link(url, new ExternalResource(url));
				link.setCaption(abgeordneter.getWahlkreisName());
				link.setTargetName("_blank");
				return link;

			}

		}
		return super.getPropertyValue(rowId, colId, property);
	}
}
