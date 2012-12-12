package de.ulei.nebeneinkuenfte;

import java.util.Locale;

import com.vaadin.Application;

public class NebeneinkuenfteApplication extends Application {

	private static final long serialVersionUID = 8818244775327742466L;

	@Override
	public void init() {

		// choosing theme
		setTheme("nebeneinkuenfte");
		setLocale(Locale.GERMANY);

		MainFrameWindow mainFrame = new MainFrameWindow("login");
		
		super.addWindow(mainFrame);
		setMainWindow(mainFrame);

	}

}
