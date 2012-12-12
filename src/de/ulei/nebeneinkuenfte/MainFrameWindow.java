package de.ulei.nebeneinkuenfte;

import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

public class MainFrameWindow extends Window {

	private static final long serialVersionUID = 1433635810021375674L;

	private Header header = null;
	private UserLoginPanel loginPanel = null;

	public MainFrameWindow(String caption) {
	
		setName(caption);
		initUI();
		
	}

	private void initUI() {

		VerticalLayout vl = new VerticalLayout();
		header = new Header();
		loginPanel = new UserLoginPanel();

		vl.addComponent(header);
		vl.addComponent(loginPanel);

		setContent(vl);
	}
}
