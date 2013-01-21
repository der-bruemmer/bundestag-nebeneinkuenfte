package de.ulei.nebeneinkuenfte.model;

public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		String url = "http://www.bundestag.de/bundestag/abgeordnete17/biografien/G/gabriel_sigmar.html";
		System.out.println(url.substring(url.lastIndexOf("/") , url.lastIndexOf(".")));

	}

}
