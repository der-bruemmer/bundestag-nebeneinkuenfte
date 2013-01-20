package de.ulei.nebeneinkuenfte.ui;

import java.net.URL;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.vaadin.Application;
import com.vaadin.terminal.gwt.server.HttpServletRequestListener;
import com.vaadin.ui.UriFragmentUtility;
import com.vaadin.ui.UriFragmentUtility.FragmentChangedEvent;
import com.vaadin.ui.UriFragmentUtility.FragmentChangedListener;

import de.ulei.nebeneinkuenfte.ui.controller.MainController;

public class NebeneinkuenfteApplication extends Application implements HttpServletRequestListener {

	private static final long serialVersionUID = 8818244775327742466L;

	private MainController mainController;
	private UriFragmentUtility urifu = new UriFragmentUtility();

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

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

		// urifu = new MyHackedUpUriFragmentUtility();
		MainFrameWindow mainFrame = new MainFrameWindow("mainFrame");
		mainController = new MainController(mainFrame);
		mainFrame.addComponent(urifu);

		super.addWindow(mainFrame);
		setMainWindow(mainFrame);

		urifu.addListener(new FragmentChangedListener() {

			private static final long serialVersionUID = 1951376846032472745L;

			public void fragmentChanged(FragmentChangedEvent source) {

				String fragment = source.getUriFragmentUtility().getFragment();
				if (fragment != null) {
					mainController.handleURIFragment(fragment);
				}
			}
		});

	}

	public static NebeneinkuenfteApplication getInstance() {

		if (threadLocal.get() == null)
			threadLocal = new ThreadLocal<NebeneinkuenfteApplication>();

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
	public void onRequestStart(HttpServletRequest request, HttpServletResponse response) {

		// setting this instance of NebeneinkuenfteApplication as ThreadLocal
		NebeneinkuenfteApplication.setInstance(this);
		// get initial URI fragment at app init
		urifu.setFragment(request.getParameter("fr"), true);

	}

	@Override
	public void onRequestEnd(HttpServletRequest request, HttpServletResponse response) {

		// removing application instance from thread local
		threadLocal.remove();

	}

	public void setURIFragment(String newFragment) {
		urifu.setFragment(newFragment.toLowerCase());
	}

	public String getURIFragment() {
		return urifu.getFragment().toLowerCase();
	}

	public URL getURL() {
		return super.getURL();
	}

}
