package de.ulei.nebeneinkuenfte;

import java.util.Locale;

import com.vaadin.Application;

import de.ulei.nebeneinkuenfte.controller.BasicController;
import de.ulei.nebeneinkuenfte.controller.MainController;
import de.ulei.nebeneinkuenfte.controller.OriginController;
import de.ulei.nebeneinkuenfte.controller.PartyController;
import de.ulei.nebeneinkuenfte.controller.PersonController;

public class NebeneinkuenfteApplication extends Application {

	private static final long serialVersionUID = 8818244775327742466L;

	@Override
	public void init() {
		
		// choosing theme
		setTheme("nebeneinkuenfte");
		setLocale(Locale.GERMANY);

		MainFrameWindow mainFrame = new MainFrameWindow("mainFrame");
		MainController controller = new MainController(mainFrame);

		super.addWindow(mainFrame);
		setMainWindow(mainFrame);

	}

}
