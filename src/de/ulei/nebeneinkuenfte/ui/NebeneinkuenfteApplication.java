package de.ulei.nebeneinkuenfte.ui;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.vaadin.Application;
import com.vaadin.terminal.FileResource;
import com.vaadin.terminal.gwt.server.HttpServletRequestListener;
import com.vaadin.ui.UriFragmentUtility;
import com.vaadin.ui.UriFragmentUtility.FragmentChangedEvent;
import com.vaadin.ui.UriFragmentUtility.FragmentChangedListener;

import de.ulei.nebeneinkuenfte.model.IRDFExport;
import de.ulei.nebeneinkuenfte.model.RDFModel;
import de.ulei.nebeneinkuenfte.ui.controller.MainController;
import de.ulei.nebeneinkuenfte.util.IConstants;

public class NebeneinkuenfteApplication extends Application implements HttpServletRequestListener {

	private static final long serialVersionUID = 8818244775327742466L;

	private MainController mainController;
	private UriFragmentUtility urifu = new UriFragmentUtility();

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
					handleFragment(fragment);
				}
			}

		});

	}

	private void handleFragment(String fragment) {
		
		String fileFormat = fragment.substring(fragment.lastIndexOf(".") + 1);

		if (IRDFExport.FILETYPE.get(fileFormat) != null) {

			// build URI
			System.out.println(fileFormat);
			fragment = IConstants.NAMESPACE.concat("/").concat(fragment);
			fragment = fragment.replaceAll(".".concat(fileFormat), "");

			// create tmp file
			File file = new File(System.getProperty("java.io.tmpdir").concat("/serialization.").concat(fileFormat));
			fileFormat = IRDFExport.FILETYPE.get(fileFormat);

			// execute query
			RDFModel model = new RDFModel();
			ByteArrayOutputStream out = model.runSubjectQuery(fragment, fileFormat);

			// write file
			FileWriter fileWriter;
			try {
				fileWriter = new FileWriter(file);
				fileWriter.write(out.toString());
				fileWriter.flush();
				fileWriter.close();
			} catch (IOException e) {
				e.printStackTrace();
			}

			getMainWindow().open(new FileResource(file, NebeneinkuenfteApplication.getInstance()));

		} else
			mainController.handleURIFragment(fragment);
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
		setURIFragment(request.getParameter("fr"));

	}

	@Override
	public void onRequestEnd(HttpServletRequest request, HttpServletResponse response) {

		// removing application instance from thread local
		threadLocal.remove();

	}

	public void setURIFragment(String newFragment, boolean fireEvent) {
		urifu.setFragment(newFragment.toLowerCase(), fireEvent);
	}

	public void setURIFragment(String newFragment) {

		if (newFragment == null || newFragment.trim().isEmpty())
			return;

		if (newFragment.toLowerCase().equals(getURIFragment()))
			urifu.setFragment(newFragment.toLowerCase(), false);

		urifu.setFragment(newFragment.toLowerCase(), true);
	}

	public String getURIFragment() {
		return urifu.getFragment() != null ? urifu.getFragment().toLowerCase() : "";
	}

	public URL getURL() {
		return super.getURL();
	}

	public MainController getMainController() {
		return mainController;
	}

}
