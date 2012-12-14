package de.ulei.nebeneinkuenfte.table;

import org.tepi.filtertable.FilterTable;

public class BasicTable extends FilterTable {

	private static final long serialVersionUID = 1037222921294665317L;

	public BasicTable() {
		
		setFilterGenerator(new TableFilterGenerator());
		setFilterDecorator(new TableFilterDecorator());
		setFilterBarVisible(true);

		setSelectable(true);
		setSizeFull();

	}

}
