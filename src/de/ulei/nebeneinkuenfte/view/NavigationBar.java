package de.ulei.nebeneinkuenfte.view;

import com.vaadin.terminal.ThemeResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.themes.Runo;

import de.ulei.nebeneinkuenfte.util.ActionType;

public class NavigationBar extends AbstractView {

	private static final long serialVersionUID = -22603405987137766L;
	private static int BUTTON_WIDTH = 150;

	private GridLayout buttonLayout;

	private Button home;
	private Label separator;
	private Button export;

	public NavigationBar() {

		setSizeFull();
		createButtonSection();

	}

	private void createButtonSection() {

		Panel panel = new Panel(" ");
		panel.setStyleName(Runo.PANEL_LIGHT);
		panel.setScrollable(true);
		panel.setImmediate(true);

		buttonLayout = new GridLayout();
		buttonLayout.setSpacing(true);
		buttonLayout.setWidth(100, UNITS_PERCENTAGE);

		export = new Button("Export");
		export.setImmediate(true);
		export.setWidth(BUTTON_WIDTH, UNITS_PIXELS);
		export.setIcon(new ThemeResource("icons/16/document-save-as.png"));
		export.addListener(new Button.ClickListener() {

			private static final long serialVersionUID = -6896210990876900350L;

			@Override
			public void buttonClick(ClickEvent event) {
			}
		});

		separator = new Label("<hr/>", Label.CONTENT_XHTML);
		separator.setWidth(BUTTON_WIDTH, UNITS_PIXELS);

		home = new Button("Home");
		home.setImmediate(true);
		home.setWidth(BUTTON_WIDTH, UNITS_PIXELS);
		home.setIcon(new ThemeResource("icons/16/home.png"));
		home.addListener(new Button.ClickListener() {

			private static final long serialVersionUID = -6896210990876900350L;

			@Override
			public void buttonClick(ClickEvent event) {
				fireEvent(ActionType.HOME);
			}
		});

		buttonLayout.addComponent(home);
		buttonLayout.setComponentAlignment(home, Alignment.TOP_CENTER);
		buttonLayout.addComponent(separator);
		buttonLayout.setComponentAlignment(separator, Alignment.TOP_CENTER);
		buttonLayout.addComponent(export);
		buttonLayout.setComponentAlignment(export, Alignment.TOP_CENTER);

		panel.addComponent(buttonLayout);
		addComponent(panel);

	}

	public void addButton(Button button) {

		button.setWidth(BUTTON_WIDTH, UNITS_PIXELS);
		buttonLayout.addComponent(button);
		buttonLayout.setComponentAlignment(button, Alignment.TOP_CENTER);

	}

}