package de.ulei.nebeneinkuenfte.model;

import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class Test {

	/**
	 * @param args
	 * @throws UnsupportedEncodingException
	 */
	public static void main(String[] args) throws UnsupportedEncodingException {
		StringWriter writer = new StringWriter();

		StringBuffer bug = new StringBuffer();
		String url = "https://www.bundestag.de/service/kontakt/kontakt/index.jsp?mailReceiver=U2FsdGVkX1/rvfCBtbjxRiJ8xjm4ZOoiiQE2lWT/1eOastk6iQ2d4NSmNivpthyI41WGsVqhC9ua\\u000A8CZi1UoQjQ==&receiver=Dr.%2BAndreas%2BScheuer";
		writer.append("https://www.bundestag.de/service/kontakt/kontakt/index.jsp?mailReceiver=U2FsdGVkX1%2FrvfCBtbjxRiJ8xjm4ZOoiiQE2lWT%2F1eOastk6iQ2d4NSmNivpthyI41WGsVqhC9ua8CZi1UoQjQ==&receiver=Dr.%20Andreas%20Scheuer");
		writer.flush();
		System.out.println(writer.getBuffer().toString());

	}

}
