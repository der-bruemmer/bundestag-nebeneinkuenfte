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

import de.ulei.nebeneinkuenfte.crawler.Abgeordneter;

public class BasicTable extends FilterTable {

	private static final long serialVersionUID = 1037222921294665317L;

	private ThemeResource mailResource = new ThemeResource("icons/16/mail.png");
	private ThemeResource placeResource = new ThemeResource(
			"icons/16/map-compass.png");

	public BasicTable() {

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
		setSelectable(true);
		setSizeFull();

	}

	@Override
	protected Object getPropertyValue(Object rowId, Object colId,
			Property property) {

		// handle homepage
		if (colId.equals("homepage") || colId.equals("mail")) {

			/*
			 * link must start with protocol definition http or https. otherwise
			 * the link can not opened.
			 */
			String url = (String) property.getValue();
			if (url != null)
				return new Link(url, new ExternalResource(url));

		}
		// handle email
		else if (colId.equals("email")) {

			Link link;
			String url = (String) property.getValue();
			if (url != null) {

				link = new Link(url, new ExternalResource(url));
				link.setCaption("Email senden");
				link.setIcon(mailResource);
				return link;

			}

		}
		// handle wahlkreis
		else if (colId.equals("wahlkreis")) {

			Abgeordneter abgeordneter = (Abgeordneter) rowId;
			Link link = null;

			String url = (String) property.getValue();
			if (url != null) {

				link = new Link(url, new ExternalResource(url));
				link.setCaption("Wahlkreis");
				link.setIcon(placeResource);
				return link;

			}

		}
		return super.getPropertyValue(rowId, colId, property);
	}
}
