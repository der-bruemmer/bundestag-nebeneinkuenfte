package de.ulei.nebeneinkuenfte;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.vaadin.Application;
import com.vaadin.terminal.gwt.server.HttpServletRequestListener;

import de.ulei.nebeneinkuenfte.controller.MainController;

public class NebeneinkuenfteApplication extends Application implements
		HttpServletRequestListener {

	private static final long serialVersionUID = 8818244775327742466L;

	/*
	 * thread local creates a static NebeneinkuenfteApplication instance of the
	 * current session which is accessible with
	 * NebeneinkuenfteApplication.getInstance().publicMethod()
	 */
	private static ThreadLocal<NebeneinkuenfteApplication> threadLocal = null;

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

	public static NebeneinkuenfteApplication getInstance() {
		if (threadLocal.get() == null) {
			threadLocal = new ThreadLocal<NebeneinkuenfteApplication>();
		}
		return threadLocal.get();
	}

	public static void setInstance(NebeneinkuenfteApplication app) {

		threadLocal = new ThreadLocal<NebeneinkuenfteApplication>();
		threadLocal.set(app);
	}

	/*
	 * Methods for accessing the servlet session (start and end of the session)
	 * with browser specific options such as locale.
	 */
	@Override
	public void onRequestStart(HttpServletRequest request,
			HttpServletResponse response) {

		// setting this instance of NebeneinkuenfteApplication as ThreadLocal
		NebeneinkuenfteApplication.setInstance(this);
	}

	@Override
	public void onRequestEnd(HttpServletRequest request,
			HttpServletResponse response) {

		// removing application instance from thread local
		threadLocal.remove();
	}

}
