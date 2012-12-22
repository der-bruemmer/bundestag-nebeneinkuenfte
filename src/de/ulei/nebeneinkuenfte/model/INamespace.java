package de.ulei.nebeneinkuenfte.model;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * Used namespaces and corresponding prefixes.
 * 
 * @author Sebastian Lippert
 * 
 */

public interface INamespace {

	/**
	 * self defined project prefix http://nebeneinkuenfte.asv.uni-leipzig.de/
	 */
	public static final String BTD = "btd";
	/**
	 * prefix for http://xmlns.com/foaf/0.1/
	 */
	public static final String FOAF = "foaf";
	/**
	 * prefix for http://dbpedia.org/resource/
	 */
	public static final String DBPR = "dbpr";
	/**
	 * prefix for http://dbpedia.org/ontology/
	 */
	public static final String DBPO = "dbpo";
	/**
	 * prefix for http://purl.org/dc/elements/1.1/
	 */
	public static final String DC = "dc";
	/**
	 * Map contains prefix as key and uri as value
	 */
	public static final Map<String, String> NAMSESPACE_MAP = new HashMap<String, String>() {

		private static final long serialVersionUID = 1267409796567714741L;

		{

			put(FOAF, "http://xmlns.com/foaf/0.1/");
			put(BTD, "http://nebeneinkuenfte.asv.uni-leipzig.de/");
			put(DBPR, "http://dbpedia.org/resource/");
			put(DBPO, "http://dbpedia.org/ontology/");
			put(DC, "http://purl.org/dc/elements/1.1/");

		}

	};

}