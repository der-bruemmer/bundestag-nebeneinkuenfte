package de.ulei.nebeneinkuenfte.table;

import java.text.DateFormat;

import org.tepi.filtertable.FilterDecorator;
import org.tepi.filtertable.numberfilter.NumberFilterPopupConfig;

import com.vaadin.terminal.Resource;

public class TableFilterDecorator implements FilterDecorator {

	@Override
	public String getEnumFilterDisplayName(Object propertyId, Object value) {
		return null;
	}

	@Override
	public Resource getEnumFilterIcon(Object propertyId, Object value) {
		return null;
	}

	@Override
	public String getBooleanFilterDisplayName(Object propertyId, boolean value) {
		return null;
	}

	@Override
	public Resource getBooleanFilterIcon(Object propertyId, boolean value) {
		return null;
	}

	@Override
	public String getFromCaption() {
		return "Startdatum:";
	}

	@Override
	public String getToCaption() {
		return "Enddatum:";
	}

	@Override
	public String getSetCaption() {
		// return null = use default caption
		return "Ok";
	}

	@Override
	public String getClearCaption() {
		// return null = use default caption
		return "Löschen";
	}

	@Override
	public boolean isTextFilterImmediate(Object propertyId) {
		return false;
	}

	@Override
	public int getTextChangeTimeout(Object propertyId) {
		return 0;
	}

	@Override
	public int getDateFieldResolution(Object propertyId) {
		return 4;
	}

	@Override
	public DateFormat getDateFormat(Object propertyId) {
		return null;
	}

	@Override
	public String getAllItemsVisibleString() {
		return null;
	}

	@Override
	public NumberFilterPopupConfig getNumberFilterPopupConfig() {

		NumberFilterPopupConfig config = new NumberFilterPopupConfig();
		config.setEqPrompt("gleich");
		config.setGtPrompt("größer");
		config.setLtPrompt("kleiner");
		config.setOkCaption("Ok");
		config.setResetCaption("Löschen");

		return config;
	}

	@Override
	public boolean usePopupForNumericProperty(Object propertyId) {
		return true;
	}

}