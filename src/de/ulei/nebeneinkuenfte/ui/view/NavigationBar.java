package de.ulei.nebeneinkuenfte.ui.view;

import com.vaadin.terminal.ThemeResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.themes.Runo;

import de.ulei.nebeneinkuenfte.util.IConstants;

public class NavigationBar extends GridLayout {

	private static final long serialVersionUID = -22603405987137766L;

	private GridLayout buttonLayout;

	private Button home;
	private Label separator;

	public NavigationBar(AbstractView view) {

		setSizeFull();
		createButtonSection(view);

	}

	private void createButtonSection(AbstractView view) {

		Panel panel = new Panel(" ");
		panel.setStyleName(Runo.PANEL_LIGHT);
		panel.setScrollable(true);
		panel.setImmediate(true);

		buttonLayout = new GridLayout();
		buttonLayout.setSpacing(true);
		buttonLayout.setWidth(100, UNITS_PERCENTAGE);

		separator = new Label("<hr/>", Label.CONTENT_XHTML);
		separator.setWidth(IConstants.BUTTON_WIDTH, UNITS_PIXELS);

		home = ButtonFactory.getButton(ButtonFactory.OPEN_BASIC_BUTTON, view);

		buttonLayout.addComponent(home);
		buttonLayout.setComponentAlignment(home, Alignment.TOP_CENTER);
		buttonLayout.addComponent(separator);
		buttonLayout.setComponentAlignment(separator, Alignment.TOP_CENTER);

		panel.addComponent(buttonLayout);
		addComponent(panel);

	}

	public void addButton(Button button) {

		button.setWidth(IConstants.BUTTON_WIDTH, UNITS_PIXELS);
		buttonLayout.addComponent(button);
		buttonLayout.setComponentAlignment(button, Alignment.TOP_CENTER);

	}

	public void setHomeButtonCaption(String caption) {
		home.setCaption(caption);
	}

	public void setHomeButtonIcon(ThemeResource icon) {
		home.setIcon(icon);
	}

}
