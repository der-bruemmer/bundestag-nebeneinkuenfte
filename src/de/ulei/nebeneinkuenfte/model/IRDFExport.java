package de.ulei.nebeneinkuenfte.model;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * Possible RDF serialization forms for export function
 * 
 * @author Sebastian Lippert
 * 
 */

public class IRDFExport {

	public static final String RDF_XML = "RDF/XML";
	public static final String RDF_XML_ABBREV = "RDF/XML-ABBREV";
	public static final String N3 = "N3";
	public static final String TURTLE = "TURTLE";
	public static final String N_TRIPLE = "N-TRIPLE";

	/**
	 * Map contains RDF serialization form as key and the corresponding file
	 * extension as value
	 */

	public final static Map<String, String> TYPES = new HashMap<String, String>() {

		private static final long serialVersionUID = -2912473482257575131L;

		{
			put(RDF_XML, "rdf");
			put(RDF_XML_ABBREV, "owl");
			put(N3, "n3");
			put(TURTLE, "ttl");
			put(N_TRIPLE, "nt");

		}
	};

}
