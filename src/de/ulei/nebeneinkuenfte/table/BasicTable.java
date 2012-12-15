package de.ulei.nebeneinkuenfte.table;

import org.tepi.filtertable.FilterTable;

import com.vaadin.data.Container;
import com.vaadin.data.Property;
import com.vaadin.terminal.ExternalResource;
import com.vaadin.ui.Component;
import com.vaadin.ui.Field;
import com.vaadin.ui.Link;
import com.vaadin.ui.TableFieldFactory;

public class BasicTable extends FilterTable {

	private static final long serialVersionUID = 1037222921294665317L;

	public BasicTable() {

		setFilterGenerator(new TableFilterGenerator());
		setFilterDecorator(new TableFilterDecorator());
		setFilterBarVisible(true);
		setTableFieldFactory(new TableFieldFactory() {

			private static final long serialVersionUID = -3519781089291834981L;

			@Override
			public Field createField(Container container, Object itemId,
					Object propertyId, Component uiContext) {
				// TODO Auto-generated method stub
				return null;
			}
		});
		setEditable(true);
		setSelectable(true);
		setSizeFull();

	}

	@Override
	protected Object getPropertyValue(Object rowId, Object colId,
			Property property) {

		// if return column equals url return link object
		if (colId.equals("homepage")) {

			/*
			 * link must start with protocol definition http or https. otherwise
			 * the link can not opened.
			 */
			String url = (String) property.getValue();
			if (url != null) {
				if (url.startsWith("http"))
					return new Link(url, new ExternalResource(url));
				else
					return new Link(url, new ExternalResource("http://" + url));
			}
		} else if (colId.equals("email")) {

			/*
			 * handle email addresses
			 */
			String mail = (String) property.getValue();
			if (mail != null) {
				return new Link(mail, new ExternalResource("mailto:" + mail));
			}

		}
		return super.getPropertyValue(rowId, colId, property);
	}

}
