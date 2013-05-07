package de.ulei.nebeneinkuenfte.ui.table;

import java.io.Serializable;

import org.tepi.filtertable.FilterGenerator;

import com.vaadin.data.Item;
import com.vaadin.data.Container.Filter;
import com.vaadin.terminal.ThemeResource;
import com.vaadin.ui.AbstractField;
import com.vaadin.ui.ComboBox;

import de.ulei.nebeneinkuenfte.ui.model.Abgeordneter;

public class TableFilterGenerator implements FilterGenerator, Serializable {

	private static final long serialVersionUID = 4329855437898067359L;

	@Override
	public Filter generateFilter(Object propertyId, Object value) {

		final String filter = value.toString().trim().toLowerCase();

		if (propertyId.equals("wahlkreisUri")) {

			return new Filter() {

				private static final long serialVersionUID = -5348588096462989475L;

				@Override
				public boolean passesFilter(Object itemId, Item item) throws UnsupportedOperationException {

					if (filter.isEmpty())
						return true;

					Abgeordneter abgeordneter = (Abgeordneter) itemId;
					if (abgeordneter.getWahlkreisName().toLowerCase().contains(filter.toString().toLowerCase()))
						return true;

					return false;
				}

				@Override
				public boolean appliesToProperty(Object propertyId) {
					return false;
				}
			};
		}
		return null;
	}

	@Override
	public AbstractField getCustomFilterComponent(Object propertyId) {

		if (propertyId.equals("fraktion")) {

			ComboBox box = new ComboBox();
			box.setNullSelectionAllowed(false);
			box.setImmediate(true);

			box.addItem("");
			box.setItemCaption("", "Alle");
			box.addItem("Die Linke");
			box.addItem("Bündnis 90/Die Grünen");
			box.setItemIcon("Bündnis 90/Die Grünen", new ThemeResource("icons/16/green.png"));
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

		else if (propertyId.equals("stufe")) {

			ComboBox box = new ComboBox();
			box.setNullSelectionAllowed(false);
			box.setImmediate(true);

			box.addItem("");
			box.setItemCaption("", "Alle");
			box.addItem("Stufe 1");
			box.addItem("Stufe 2");
			box.addItem("Stufe 3");

			box.select("");
			return box;

		}
		return null;
	}

	@Override
	public void filterRemoved(Object propertyId) {

	}

	@Override
	public void filterAdded(Object propertyId, Class<? extends Filter> filterType, Object value) {

	}

}