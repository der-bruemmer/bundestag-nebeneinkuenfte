package de.ulei.nebeneinkuenfte.model;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.io.StringWriter;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.sparql.engine.http.QueryEngineHTTP;

public class RDFExport extends RDFModel implements Serializable {

	private static final long serialVersionUID = 3181657099199403221L;

	public RDFExport() {
		super();
	}

	public RDFExport(String triplestoreURL) {
		super(triplestoreURL);
	}

	/**
	 * File export of the generated model.
	 * 
	 * 
	 * @param path
	 *            absolute path to target file
	 * @param rdfType
	 *            Type for RDF format
	 */

	public void fileExport(String path, String rdfType) {

		try {
			model.write(new FileWriter(path + "." + IRDFExport.TYPES.get(rdfType)), rdfType);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * 
	 * Export model into triple store
	 * 
	 * @param graph
	 *            Name of the graph
	 * 
	 * @return true if everything was correct, else false
	 * 
	 */

	public int tripleStoreExport(String graph) throws Exception {

		boolean graphExists = false;

		// graph = "http://mygraph.com";
		// url = triplestoreURL;
		String q = "select  distinct ?g  where { GRAPH ?g { ?s ?p ?o } }";

		Query query = QueryFactory.create(q);
		QueryExecution qexec = QueryExecutionFactory.sparqlService(triplestoreURL, query);

		// graph already exists?
		ResultSet results = qexec.execSelect();
		while (results.hasNext()) {

			if (results.next().getResource("g").toString().equals(graph)) {

				graphExists = true;
				break;

			}

		}

		// create graph
		if (!graphExists) {

			q = "CREATE GRAPH '" + graph + "'";
			QueryEngineHTTP qeh = new QueryEngineHTTP(triplestoreURL, q);
			qeh.execSelect();
			qeh.close();

		}
		// clear graph
		else {

			q = "CLEAR GRAPH '" + graph + "'";
			QueryEngineHTTP qeh = new QueryEngineHTTP(triplestoreURL, q);
			qeh.execSelect();
			qeh.close();

		}

		/*
		 * workaround to HTTP length limit
		 * 
		 * every statement will be treated as new OntModel and is inserted to
		 * the given graph by single HTTP request instead of creating one
		 * statement for whole model. Not fast but it works.
		 */

		QueryEngineHTTP qeh;
		OntModel statementModel;
		StringWriter writer;
		Statement statement;
		StmtIterator it = model.listStatements();
		int result = 0;

		while (it.hasNext()) {

			statement = it.next();
			statementModel = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM);
			statementModel.add(statement);

			writer = new StringWriter();
			writer.append("INSERT DATA IN GRAPH '" + graph + "'" + " {");
			statementModel.write(writer, "N-TRIPLE");
			writer.append(" }");
			writer.flush();

			// open connection and run statement
			qeh = new QueryEngineHTTP(triplestoreURL, writer.toString());
			qeh.execSelect();
			qeh.close();

		}

		return result;
	}

}
