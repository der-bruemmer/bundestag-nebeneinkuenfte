package de.ulei.nebeneinkuenfte.ui;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.vaadin.terminal.gwt.server.ApplicationServlet;
import com.vaadin.terminal.gwt.server.CommunicationManager;
import com.vaadin.ui.Window;

import de.ulei.nebeneinkuenfte.model.IRDFExport;
import de.ulei.nebeneinkuenfte.model.RDFImport;

public class NebeneinkuenfteApplicationServlet extends ApplicationServlet {

	private static final long serialVersionUID = -3307775138115436329L;

	@Override
	protected boolean handleURI(CommunicationManager applicationManager, Window window, HttpServletRequest request,
			HttpServletResponse response) throws IOException {

		String requestURL = request.getRequestURL().toString();
		String fileFormat = requestURL.substring(requestURL.lastIndexOf(".") + 1);

		// validate fileFormat
		if (IRDFExport.FILETYPE.get(fileFormat) != null && !requestURL.contains("APP")) {
			try {
				doGet(request, response);
				return true;
			} catch (ServletException e) {
				e.printStackTrace();
			}
		}

		return super.handleURI(applicationManager, window, request, response);
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		String requestURL = request.getRequestURL().toString();
		String fileFormat = requestURL.substring(requestURL.lastIndexOf(".") + 1);

		// validate fileFormat
		if (IRDFExport.FILETYPE.get(fileFormat) != null) {

			// cut of fileFormat
			requestURL = requestURL.replaceAll(".".concat(fileFormat), "");
			// transform fileFormat into serialization constant
			fileFormat = IRDFExport.FILETYPE.get(fileFormat);

			// create requested graph
			ByteArrayOutputStream out = new RDFImport().querySubject(requestURL, fileFormat);

			// write graph
			PrintWriter writer = response.getWriter();
			writer.println(out.toString());
			writer.close();
			return;
		}

		super.doGet(request, response);
	}

}
