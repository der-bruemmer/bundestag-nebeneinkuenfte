package de.ulei.nebeneinkuenfte.table;

import org.tepi.filtertable.FilterGenerator;

import com.vaadin.data.Container.Filter;
import com.vaadin.terminal.ThemeResource;
import com.vaadin.ui.AbstractField;
import com.vaadin.ui.ComboBox;

public class TableFilterGenerator implements FilterGenerator {

	@Override
	public Filter generateFilter(Object propertyId, Object value) {
		return null;
	}

	@Override
	public AbstractField getCustomFilterComponent(Object propertyId) {

		if (propertyId.equals("fraktion")) {

			ComboBox box = new ComboBox();
			box.setNullSelectionAllowed(false);
			box.addItem("");
			box.setItemCaption("", "Alle");
			box.addItem("Die Linke");
			box.addItem("Bündnis 90/Die Grünen");
			box.setItemIcon("Bündnis 90/Die Grünen", new ThemeResource(
					"icons/16/green.png"));
			box.addItem("CDU/CSU");
			box.setItemIcon("CDU/CSU", new ThemeResource("icons/16/black.png"));
			box.addItem("Die Linke");
			box.setItemIcon("Die Linke", new ThemeResource("icons/16/red.png"));
			box.addItem("FDP");
			box.setItemIcon("FDP", new ThemeResource("icons/16/yellow.png"));
			box.addItem("SPD");
			box.setItemIcon("SPD", new ThemeResource("icons/16/red.png"));

			box.select("");
			return box;

		}
		return null;
	}

	@Override
	public void filterRemoved(Object propertyId) {

	}

	@Override
	public void filterAdded(Object propertyId,
			Class<? extends Filter> filterType, Object value) {

	}

}