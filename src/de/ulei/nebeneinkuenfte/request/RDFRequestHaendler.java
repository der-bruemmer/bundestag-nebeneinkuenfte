package de.ulei.nebeneinkuenfte.request;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import de.ulei.nebeneinkuenfte.model.IRDFExport;
import de.ulei.nebeneinkuenfte.model.RDFModel;

/**
 * Servlet implementation class Filehaendler
 */
@WebServlet("/Filehaendler")
public class RDFRequestHaendler extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public RDFRequestHaendler() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		String requestURL = request.getRequestURL().toString();
		String fileFormat = requestURL.substring(requestURL.lastIndexOf(".") + 1);

		// validate fileFormat
		if (IRDFExport.FILETYPE.get(fileFormat) != null) {

			// cut of fileFormat
			requestURL = requestURL.replaceAll(".".concat(fileFormat), "");
			// transform fileFormat into serialization constant 
			fileFormat = IRDFExport.FILETYPE.get(fileFormat);
		
			RDFModel model = new RDFModel();
			ByteArrayOutputStream out = model.runSubjectQuery(requestURL, fileFormat);

			PrintWriter writer = response.getWriter();
			writer.println(out.toString());
			writer.close();

		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException,
			IOException {
		// TODO Auto-generated method stub
	}

}
