package de.ulei.nebeneinkuenfte.model;

import java.io.ByteArrayOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

import com.hp.hpl.jena.ontology.DatatypeProperty;
import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.ObjectProperty;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.ontology.OntResource;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.sparql.engine.http.QueryEngineHTTP;
import com.hp.hpl.jena.util.iterator.ExtendedIterator;
import com.hp.hpl.jena.vocabulary.RDF;
import com.hp.hpl.jena.vocabulary.RDFS;

import de.ulei.nebeneinkuenfte.model.crawler.BundestagConverter;
import de.ulei.nebeneinkuenfte.ui.model.Abgeordneter;
import de.ulei.nebeneinkuenfte.ui.model.Nebentaetigkeit;
import de.ulei.nebeneinkuenfte.util.IConstants;

public class RDFModel {

	private final String SPD_FRAKTION = "http://www.spdfraktion.de/";
	private final String SPD_LABEL = "SPD";

	private final String CDU_CSU_FRAKTION = "http://www.cducsu.de/";
	private final String CDU_CSU_LABEL = "CDU/CSU";

	private final String DIE_LINKE_FRAKTION = "http://www.linksfraktion.de/";
	private final String DIE_LINKE_LABEL = "Die Linke";

	private final String FDP_FRAKTION = "http://www.fdp-fraktion.de/";
	private final String FDP_LABEL = "FDP";

	private final String GRUENE_FRAKTION = "http://www.gruene-bundestag.de/";
	private final String GRUENE_LABEL = "Bündnis 90/Die Grünen";

	// model
	private OntModel model;

	// classes
	private OntClass classAbgeordneter;
	private OntClass classFraktion;
	private OntClass classNebeneinkunft;
	private OntClass classAuftraggeber;
	private OntClass classWahlkreis;

	private OntClass classPerson;
	private OntClass classOrganization;
	private OntClass classPlace;
	private OntClass classDocument;

	// DatatypeProperties
	private DatatypeProperty propFirstName;
	private DatatypeProperty propGivenName;
	private DatatypeProperty propTitle;
	private DatatypeProperty propNebeneinkuenfteAnzahl;
	private DatatypeProperty propNebeneinkuenfteMinimum;
	private DatatypeProperty propNebeneinkuenfteMaximum;
	private DatatypeProperty propNebeneinkuenftStufe;
	private DatatypeProperty propNebeneinkuenftJahr;
	private DatatypeProperty propNebeneinkuenftTyp;
	private DatatypeProperty propNebeneinkuenftOrt;
	private DatatypeProperty propGeoLat;
	private DatatypeProperty propGeoLong;

	// ObjectProperties
	private ObjectProperty propHomepage;
	private ObjectProperty propMbox;
	private ObjectProperty propMember;
	private ObjectProperty propHatNebeneinkunft;
	private ObjectProperty propHatAuftraggeber;
	private ObjectProperty propBezahlt;
	private ObjectProperty propIsPartOf;
	private ObjectProperty propHatWahlkreis;

	public RDFModel() {

		// create model and set namespaceMap
		this.model = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM);
		this.model.setNsPrefixes(INamespace.NAMSESPACE_MAP);

		// create classes and properties
		createClasses();
		createDatatypeProperties();
		createObjectProperties();

		// create Resources for all known factions
		createFraktionResources();

	}

	private void createClasses() {

		// existing classes
		classPerson = model.createClass(model.getNsPrefixURI(INamespace.FOAF) + "Person");
		classOrganization = model.createClass(model.getNsPrefixURI(INamespace.FOAF) + "Organization");
		classPlace = model.createClass(model.getNsPrefixURI(INamespace.DBPO) + "PopulatedPlace");
		classDocument = model.createClass(model.getNsPrefixURI(INamespace.FOAF) + "Document");

		// self defined classes
		classWahlkreis = model.createClass(model.getNsPrefixURI(INamespace.BTD) + "Wahlkreis");
		classFraktion = model.createClass(model.getNsPrefixURI(INamespace.BTD) + "Fraktion");
		classFraktion.addProperty(RDF.type, classOrganization);
		classNebeneinkunft = model.createClass(model.getNsPrefixURI(INamespace.BTD) + "Nebeneinkunft");
		classAuftraggeber = model.createClass(model.getNsPrefixURI(INamespace.BTD) + "Auftraggeber");
		classAbgeordneter = model.createClass(model.getNsPrefixURI(INamespace.BTD) + "Abgeordneter");
		classAbgeordneter.addProperty(RDF.type, classPerson);

	}

	private void createDatatypeProperties() {

		// existing properties
		propFirstName = model.createDatatypeProperty(model.getNsPrefixURI(INamespace.FOAF) + "firstName");
		propGivenName = model.createDatatypeProperty(model.getNsPrefixURI(INamespace.FOAF) + "givenName");
		propTitle = model.createDatatypeProperty(model.getNsPrefixURI(INamespace.FOAF) + "title");
		propGeoLat = model.createDatatypeProperty(model.getNsPrefixURI(INamespace.GEO) + "lat");
		propGeoLong = model.createDatatypeProperty(model.getNsPrefixURI(INamespace.GEO) + "long");

		// self defined properties
		propNebeneinkuenfteAnzahl = model.createDatatypeProperty(model.getNsPrefixURI(INamespace.BTD)
				+ "anzahlNebeneinkuenfte");
		propNebeneinkuenfteMinimum = model.createDatatypeProperty(model.getNsPrefixURI(INamespace.BTD)
				+ "minNebeneinkuenfte");
		propNebeneinkuenfteMaximum = model.createDatatypeProperty(model.getNsPrefixURI(INamespace.BTD)
				+ "maxNebeneinkuenfte");
		propNebeneinkuenftStufe = model.createDatatypeProperty(model.getNsPrefixURI(INamespace.BTD)
				+ "stufeNebeneinkunft");
		propNebeneinkuenftJahr = model.createDatatypeProperty(model.getNsPrefixURI(INamespace.BTD)
				+ "jahrNebeneinkunft");
		propNebeneinkuenftTyp = model.createDatatypeProperty(model.getNsPrefixURI(INamespace.BTD) + "typNebeneinkunft");
		propNebeneinkuenftOrt = model.createDatatypeProperty(model.getNsPrefixURI(INamespace.BTD) + "ortNebeneinkunft");
	}

	private void createObjectProperties() {

		// existing properties
		propHomepage = model.createObjectProperty(model.getNsPrefixURI(INamespace.FOAF) + "homepage");
		propMbox = model.createObjectProperty(model.getNsPrefixURI(INamespace.FOAF) + "mbox");
		propHatWahlkreis = model.createObjectProperty(model.getNsPrefixURI(INamespace.BTD) + "wahlkreis");
		propMember = model.createObjectProperty(model.getNsPrefixURI(INamespace.FOAF) + "member");
		propIsPartOf = model.createObjectProperty(model.getNsPrefixURI(INamespace.DC) + "isPartOf");

		// self defined properties
		propHatNebeneinkunft = model.createObjectProperty(model.getNsPrefixURI(INamespace.BTD) + "hatNebeneinkunft");
		propHatNebeneinkunft.setDomain(classAbgeordneter);
		propHatNebeneinkunft.setRange(classNebeneinkunft);

		propHatAuftraggeber = model.createObjectProperty(model.getNsPrefixURI(INamespace.BTD) + "hatAuftraggeber");
		propHatAuftraggeber.setDomain(classNebeneinkunft);
		propHatAuftraggeber.setRange(classAuftraggeber);

		propBezahlt = model.createObjectProperty(model.getNsPrefixURI(INamespace.BTD) + "bezahlt");
		propBezahlt.setDomain(classAuftraggeber);
		propBezahlt.setRange(classAbgeordneter);

		propHatWahlkreis = model.createObjectProperty(model.getNsPrefixURI(INamespace.BTD) + "hatWahlkreis");
		propHatWahlkreis.setDomain(classAbgeordneter);
		propHatWahlkreis.setRange(classWahlkreis);

	}

	private void createFraktionResources() {

		Resource partei = null;

		partei = model.createResource(SPD_FRAKTION, classFraktion);
		partei.addProperty(RDFS.label, SPD_LABEL);

		partei = model.createResource(CDU_CSU_FRAKTION, classFraktion);
		partei.addProperty(RDFS.label, CDU_CSU_LABEL);

		partei = model.createResource(DIE_LINKE_FRAKTION, classFraktion);
		partei.addProperty(RDFS.label, DIE_LINKE_LABEL);

		partei = model.createResource(FDP_FRAKTION, classFraktion);
		partei.addProperty(RDFS.label, FDP_LABEL);

		partei = model.createResource(GRUENE_FRAKTION, classFraktion);
		partei.addProperty(RDFS.label, GRUENE_LABEL);

	}

	public void createModel(List<Abgeordneter> personList) {
		createPersonResources(personList);
	}

	private void createPersonResources(List<Abgeordneter> personList) {

		Resource politician;
		Resource sidelineJob;
		Resource origin;
		int index = 0;

		// create resource for each member of the parliament
		for (Abgeordneter mdb : personList) {

			politician = model.createResource(mdb.getURI(), classAbgeordneter);
			politician.addProperty(propFirstName, model.createTypedLiteral(mdb.getForename()));
			politician.addProperty(propGivenName, model.createTypedLiteral(mdb.getLastname()));

			// create Resource for mail
			if (mdb.getEmail() != null && !mdb.getEmail().trim().isEmpty())
				politician.addProperty(propMbox, createDocumentResource(mdb.getEmail()));

			// create Resource for homepage
			if (mdb.getHomepage() != null && !mdb.getHomepage().trim().isEmpty())
				politician.addProperty(propHomepage, createDocumentResource(mdb.getHomepage()));

			politician.addProperty(propNebeneinkuenfteAnzahl, model.createTypedLiteral(mdb.getAnzahlNebeneinkuenfte()));
			politician.addProperty(propNebeneinkuenfteMaximum, model.createTypedLiteral(mdb.getMaxZusatzeinkommen()));
			politician.addProperty(propNebeneinkuenfteMinimum, model.createTypedLiteral(mdb.getMinZusatzeinkommen()));

			politician.addProperty(propHatWahlkreis, createWahlkreisResource(mdb, index));
			politician.addProperty(propIsPartOf, createFraktionResource(mdb, politician));

			// create resource for each sideline job of the politician
			for (Nebentaetigkeit nt : mdb.getNebentaetigkeiten()) {

				// create resource for origin
				origin = createOriginResource(nt, index);

				// create resource for the job itself
				sidelineJob = model.createResource(model.getNsPrefixURI(INamespace.BTD) + "sideline/" + index,
						classNebeneinkunft);
				sidelineJob.addProperty(propNebeneinkuenftStufe,
						model.createTypedLiteral(nt.getStufe() != null ? nt.getStufe() : "unbekannt"));
				sidelineJob.addProperty(propNebeneinkuenftJahr,
						model.createTypedLiteral(nt.getYear() != null ? nt.getYear() : "unbekannt"));
				sidelineJob.addProperty(propNebeneinkuenftTyp,
						model.createTypedLiteral(nt.getType() != null ? nt.getType() : "unbekannt"));
				sidelineJob.addProperty(propNebeneinkuenftOrt, createPlaceResource(nt, index));

				// set origin and job in relation
				sidelineJob.addProperty(propHatAuftraggeber, origin);
				// set politican and sideline job in relation
				politician.addProperty(propHatNebeneinkunft, sidelineJob);
				// set politician and origin in relation
				origin.addProperty(propBezahlt, politician);
				index++;

			}

		}

	}

	private Resource createDocumentResource(String homepageURI) {

		int cutIndex = homepageURI.lastIndexOf("&receiver=") + 10;

		String urlEncoded;
		try {
			urlEncoded = URLEncoder.encode(homepageURI.substring(cutIndex), "UTF-8");
			homepageURI = homepageURI.substring(0, cutIndex).concat(urlEncoded);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		homepageURI = homepageURI.replaceAll("\\u000A", "");
		homepageURI = homepageURI.replaceAll("%2F", "/");
		homepageURI = homepageURI.replace("\u00E4", "%C3%A4");

		Resource homepageDocument = null;

		Individual homepageDocumentInstance;
		ExtendedIterator<? extends OntResource> instances = classDocument.listInstances();

		// iterate over all instances and compare URIs
		while (instances.hasNext()) {

			homepageDocumentInstance = (Individual) instances.next();
			if (homepageDocumentInstance.getURI() != null) {
				if (homepageDocumentInstance.getURI().equals(homepageURI)) {
					homepageDocument = homepageDocumentInstance;
					break;
				}
			}
		}

		// return found instance
		if (homepageDocument != null)
			return homepageDocument;

		// create new Resource cause nothing was found
		homepageDocument = model.createResource(homepageURI, classDocument);

		return homepageDocument;
	}

	private Resource createOriginResource(Nebentaetigkeit nebentaetigkeit, int index) {

		// get URI or set default one
		String originURI = nebentaetigkeit.getAuftragUri();
		if (originURI == null || originURI.trim().isEmpty()) {

			originURI = IConstants.NAMESPACE;
			originURI = originURI.concat("#");
			originURI = originURI.concat(IConstants.PERSON_ORIGIN_VIEW_FRAG);
			originURI = originURI.concat("/keinauftraggeber/");
			originURI = originURI.concat(String.valueOf(index));

		}

		Resource auftraggeber = null;

		Individual originInstance;
		ExtendedIterator<? extends OntResource> instances = classAuftraggeber.listInstances();

		// iterate over all instances and compare URIs
		while (instances.hasNext()) {

			originInstance = (Individual) instances.next();
			if (originInstance.getURI() != null) {
				if (originInstance.getURI().equals(nebentaetigkeit.getAuftragUri())) {
					auftraggeber = originInstance;
					break;
				}
			}
		}

		// return found instance
		if (auftraggeber != null)
			return auftraggeber;

		// create new Resource cause nothing was found
		auftraggeber = model.createResource(originURI, classAuftraggeber);
		auftraggeber.addProperty(RDFS.label,
				model.createTypedLiteral(nebentaetigkeit.getAuftraggeber() != null ? nebentaetigkeit.getAuftraggeber()
						: "unbekannt"));
		if (nebentaetigkeit.getAuftraggeberHomepage() != null
				&& !nebentaetigkeit.getAuftraggeberHomepage().trim().isEmpty())
			auftraggeber.addProperty(propHomepage, createDocumentResource(nebentaetigkeit.getAuftraggeberHomepage()));

		return auftraggeber;
	}

	private Resource createWahlkreisResource(Abgeordneter abgeordneter, int index) {

		// get URI or set default one
		String wahlkreisURI = abgeordneter.getWahlkreisUri();
		if (wahlkreisURI == null || wahlkreisURI.trim().isEmpty()) {

			wahlkreisURI = IConstants.NAMESPACE;
			wahlkreisURI = wahlkreisURI.concat("/keinwahlkreis/");
			wahlkreisURI = wahlkreisURI.concat(String.valueOf(index));

		}

		Resource wahlkreis = null;

		Individual wahlkreisInstance;
		ExtendedIterator<? extends OntResource> instances = classWahlkreis.listInstances();

		// iterate over all instances and compare URIs
		while (instances.hasNext()) {

			wahlkreisInstance = (Individual) instances.next();
			if (wahlkreisInstance.getURI() != null) {
				if (wahlkreisInstance.getURI().equals(abgeordneter.getWahlkreisUri())) {
					wahlkreis = wahlkreisInstance;
					break;
				}
			}
		}

		// return found instance
		if (wahlkreis != null)
			return wahlkreis;

		// create new Resource cause nothing was found
		wahlkreis = model.createResource(wahlkreisURI, classWahlkreis);
		wahlkreis.addProperty(RDFS.label,
				model.createTypedLiteral(abgeordneter.getWahlkreisName() != null ? abgeordneter.getWahlkreisName()
						: "unbekannt"));
		return wahlkreis;

	}

	private Resource createPlaceResource(Nebentaetigkeit nebentaetigkeit, int index) {

		// get URI or set default one
		String placeURI = nebentaetigkeit.getPlaceUri();
		if (placeURI == null || placeURI.trim().isEmpty()) {

			placeURI = IConstants.NAMESPACE;
			placeURI = placeURI.concat("/keinort/");
			placeURI = placeURI.concat(String.valueOf(index));

		}

		Resource place = null;

		Individual placeInstance;
		ExtendedIterator<? extends OntResource> instances = classPlace.listInstances();

		// iterate over all instances and compare URIs
		while (instances.hasNext()) {

			placeInstance = (Individual) instances.next();
			if (placeInstance.getURI() != null) {
				if (placeInstance.getURI().equals(nebentaetigkeit.getPlaceUri())) {
					place = placeInstance;
					break;
				}
			}
		}

		// return found instance
		if (place != null)
			return place;

		// create new Resource cause nothing was found
		place = model.createResource(placeURI, classPlace);
		place.addProperty(RDFS.label,
				model.createTypedLiteral(nebentaetigkeit.getPlace() != null ? nebentaetigkeit.getPlace() : "unbekannt"));

		if (nebentaetigkeit.getLatitude() != 0.0 && nebentaetigkeit.getLongitude() != 0.0) {

			place.addProperty(propGeoLat, model.createTypedLiteral(nebentaetigkeit.getLatitude()));
			place.addProperty(propGeoLong, model.createTypedLiteral(nebentaetigkeit.getLongitude()));

		}
		return place;
	}

	private Resource createFraktionResource(Abgeordneter abgeordneter, Resource politician) {

		// get URI or set default one
		String fraktionURI = abgeordneter.getFraktionUri();
		if (fraktionURI == null || fraktionURI.trim().isEmpty())
			fraktionURI = "http://keinefaraktion.de";

		Resource fraktion = null;

		Individual fraktionInstance;
		ExtendedIterator<? extends OntResource> instances = classFraktion.listInstances();

		// iterate over all instances and compare URIs
		while (instances.hasNext()) {

			fraktionInstance = (Individual) instances.next();
			if (fraktionInstance.getURI() != null) {
				if (fraktionInstance.getURI().equals(abgeordneter.getFraktionUri())) {
					fraktion = fraktionInstance;
					break;
				}
			}
		}

		// return found instance
		if (fraktion != null) {
			fraktion.addProperty(propMember, politician);
			return fraktion;
		}

		// create new Resource cause nothing was found
		fraktion = model.createResource(fraktionURI, classFraktion);
		fraktion.addProperty(RDFS.label,
				model.createTypedLiteral(abgeordneter.getFraktion() != null ? abgeordneter.getFraktion() : "unbekannt"));
		fraktion.addProperty(propMember, politician);
		return fraktion;

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
	 * @param url
	 *            SPARQL endpoint adress
	 * 
	 * @return true if everything was correct, else false
	 * 
	 */

	public int tripleStoreExport(String graph, String url) throws Exception {

		boolean graphExists = false;

		// graph = "http://mygraph.com";
		// url = "http://127.0.0.1:8890/sparql";
		String q = "select  distinct ?g  where { GRAPH ?g { ?s ?p ?o } }";

		Query query = QueryFactory.create(q);
		QueryExecution qexec = QueryExecutionFactory.sparqlService(url, query);

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
			QueryEngineHTTP qeh = new QueryEngineHTTP(url, q);
			qeh.execSelect();
			qeh.close();

		}
		// clear graph
		else {

			q = "CLEAR GRAPH '" + graph + "'";
			QueryEngineHTTP qeh = new QueryEngineHTTP(url, q);
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
			qeh = new QueryEngineHTTP(url, writer.toString());
			qeh.execSelect();
			qeh.close();

		}

		return result;
	}

	public ByteArrayOutputStream runSubjectQuery(String resourceURI, String rdfType) {
	
		System.out.println("queryURI: " +resourceURI);
		String queryString = "SELECT * WHERE {<".concat(resourceURI).concat("> ?p ?o}");
		Query query = QueryFactory.create(queryString);

		QueryExecution qexec = QueryExecutionFactory.sparqlService("http://127.0.0.1:8890/sparql", query,
				INamespace.NAMSESPACE_MAP.get(INamespace.BTD));
		ResultSet rs = qexec.execSelect();
		qexec.close();

		// create OntModel to serialize graph
		OntModel model = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM);
		model.setNsPrefixes(INamespace.NAMSESPACE_MAP);
		Resource subject = model.createResource(resourceURI);
		RDFNode predicate;
		RDFNode object;
		ByteArrayOutputStream out = new ByteArrayOutputStream();

		// iterate over ResultSet and fill graph
		while (rs.hasNext()) {

			QuerySolution qs = rs.next();
			predicate = qs.get("p");
			object = qs.get("o");
			subject.addProperty(model.createProperty(predicate.toString()), object);

		}
		model.write(out, rdfType);

		return out;
	}

	public static void main(String[] args) {

		BundestagConverter conv = new BundestagConverter(
				"http://www.bundestag.de/bundestag/abgeordnete17/alphabet/index.html", false);

		RDFModel model = new RDFModel();
		model.createModel(conv.getAbgeordnete());

		// // RDFModel model = new RDFModel();
		// model.createModel(conv.getAbgeordnete());
		model.fileExport(System.getProperty("user.home") + "/Desktop/nb", IRDFExport.N3);
		//
		try {
			model.tripleStoreExport(INamespace.NAMSESPACE_MAP.get(INamespace.BTD), "http://127.0.0.1:8890/sparql");
		} catch (Exception e) {
			e.printStackTrace();
		}

//		ByteArrayOutputStream out = model
//				.runSubjectQuery("http://localhost:8080/nebeneinkuenfte/b09#mdb/merkel_angela", );
		System.out.println("done");

	}

}
