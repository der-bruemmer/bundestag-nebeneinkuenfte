package de.ulei.nebeneinkuenfte.model;

import java.util.HashMap;
import java.util.Map;

import de.ulei.nebeneinkuenfte.util.IConstants;

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
	 * prefix for http://www.w3.org/2001/XMLSchema#
	 */
	public static final String XSD = "xsd";
	/**
	 * prefix for http://www.w3.org/2003/01/geo/wgs84_pos#
	 */
	public static final String GEO = "geo";
	/**
	 * Map contains prefix as key and uri as value
	 */
	public static final Map<String, String> NAMSESPACE_MAP = new HashMap<String, String>() {

		private static final long serialVersionUID = 1267409796567714741L;

		{

			put(FOAF, "http://xmlns.com/foaf/0.1/");
			put(BTD, IConstants.NAMESPACE.concat("/"));
			put(DBPR, "http://dbpedia.org/resource/");
			put(DBPO, "http://dbpedia.org/ontology/");
			put(DC, "http://purl.org/dc/elements/1.1/");
			put(XSD, "http://www.w3.org/2001/XMLSchema#");
			put(GEO, "http://www.w3.org/2003/01/geo/wgs84_pos#");

		}

	};

}