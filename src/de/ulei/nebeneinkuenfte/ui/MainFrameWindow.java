package de.ulei.nebeneinkuenfte.ui;

import com.vaadin.terminal.Resource;
import com.vaadin.ui.Component;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

public class MainFrameWindow extends Window {

	private static final long serialVersionUID = 1433635810021375674L;

	private Header header;
	private Panel panel;
	private TabSheet tabSheet;

	public MainFrameWindow(String caption) {

		setName(caption);
		setSizeFull();
		initUI();

	}

	private void initUI() {

		tabSheet = new TabSheet();
		tabSheet.setSizeFull();

		VerticalLayout vl = new VerticalLayout();

		header = new Header();
		panel = new Panel("Nebeneinkuenfte");
		panel.setSizeFull();

		panel.addComponent(tabSheet);

		vl.addComponent(header);
		vl.addComponent(panel);

		setContent(vl);
	}

	public void addTab(Component component, String caption, Resource icon) {
		tabSheet.addTab(component, caption, icon);
	}

	public void addTab(Component component, String caption, Resource icon,
			int position) {
		tabSheet.addTab(component, caption, icon, position);
	}

	public void replaceTab(Component oldComponent, Component newComponent) {
		tabSheet.replaceComponent(oldComponent, newComponent);
	}

	public void removeTab(Component component) {
		tabSheet.removeComponent(component);
	}

	public int getTabIndex(Component component) {
		return tabSheet.getTabPosition(tabSheet.getTab(component));
	}

	public void selectTab(int index) {
		tabSheet.setSelectedTab(index);
	}

	public void selectTab(Component component) {
		tabSheet.setSelectedTab(component);
	}

}
