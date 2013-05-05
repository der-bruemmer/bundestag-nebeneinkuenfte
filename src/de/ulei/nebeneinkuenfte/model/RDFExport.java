package de.ulei.nebeneinkuenfte.model;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

import com.hp.hpl.jena.datatypes.xsd.XSDDatatype;
import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.ontology.OntResource;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.sparql.engine.http.QueryEngineHTTP;
import com.hp.hpl.jena.util.iterator.ExtendedIterator;
import com.hp.hpl.jena.vocabulary.RDFS;

import de.ulei.nebeneinkuenfte.model.crawler.BundestagConverter;
import de.ulei.nebeneinkuenfte.ui.model.Abgeordneter;
import de.ulei.nebeneinkuenfte.ui.model.Nebentaetigkeit;
import de.ulei.nebeneinkuenfte.util.IConstants;

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
			
		// create model and set namespaceMap
		this.model = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM);
		this.model.setNsPrefixes(INamespace.NAMSESPACE_MAP);
		
		// create Resources for all known factions
		createFraktionResources();
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
			if (mdb.getHomepage() != null && !mdb.getHomepage().trim().isEmpty()) {
				politician.addProperty(propHomepage, createDocumentResource(mdb.getHomepage()));
			}
			
			//add bundestag.de source uri
			politician.addProperty(propBundestagPage, createDocumentResource(this.getBundestagURI(mdb)));		
			
			politician.addProperty(propNebeneinkuenfteAnzahl, model.createTypedLiteral(mdb.getAnzahlNebeneinkuenfte()));
			politician.addProperty(propNebeneinkuenfteMaximum, model.createTypedLiteral(mdb.getMaxZusatzeinkommen()));
			politician.addProperty(propNebeneinkuenfteMinimum, model.createTypedLiteral(mdb.getMinZusatzeinkommen()));

			politician.addProperty(propHatWahlkreis, createWahlkreisResource(mdb, index));
			politician.addProperty(propIsPartOf, createFraktionResource(mdb, politician));
			politician.addProperty(propCreator, model.createTypedLiteral("Created by opendata-bundestag.de by automatic parsing of www.bundestag.de. Correctness of data can not be guaranteed."));
			politician.addProperty(propProvenance, model.createTypedLiteral("Data obtained from www.bundestag.de."));
			politician.addProperty(propLicence, model.createTypedLiteral("Original data is © Deutscher Bundestag, this dataset CC0; http://creativecommons.org/publicdomain/zero/1.0/"));

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
				String from = nt.getJobStart()[1]+"-"+nt.getJobStart()[0]+"-01";
				String end = nt.getJobEnd()[1]+"-"+nt.getJobEnd()[0];
				switch(nt.getJobEnd()[0]) {
					case 1:
					case 3:
					case 5:
					case 7:
					case 8:
					case 10:
					case 12: 	end+="-31"; 
								break;
					case 2: 	end+="-28";
								break;
					default:	end+="-30";
								break;
				}
				
				sidelineJob.addProperty(propNebeneinkuenftFrom,
						model.createTypedLiteral(from,XSDDatatype.XSDdate));
				sidelineJob.addProperty(propNebeneinkuenftTo,
						model.createTypedLiteral(end,XSDDatatype.XSDdate));
				sidelineJob.addProperty(propNebeneinkuenftOrt, createPlaceResource(nt, index));
				if(nt.getSourceString()!= null) {
					sidelineJob.addProperty(propNebeneinkuenftSourceString, model.createTypedLiteral(nt.getSourceString()));
				}
				sidelineJob.addProperty(propCreator, model.createTypedLiteral("Created by opendata-bundestag.de by automatic parsing of www.bundestag.de. Correctness of data can not be guaranteed."));
				sidelineJob.addProperty(propProvenance, model.createTypedLiteral("Data obtained from www.bundestag.de."));
				sidelineJob.addProperty(propLicence, model.createTypedLiteral("Original data is © Deutscher Bundestag, this dataset CC0; http://creativecommons.org/publicdomain/zero/1.0/"));
				// set origin and job in relation
				sidelineJob.addProperty(propHatAuftraggeber, origin);
				// set politican and sideline job in relation
				politician.addProperty(propHatNebeneinkunft, sidelineJob);
				// set politician and origin in relation
				origin.addProperty(propBezahlt, politician);
				origin.addProperty(propCreator, model.createTypedLiteral("Created by opendata-bundestag.de by automatic parsing of www.bundestag.de. Correctness of data can not be guaranteed."));
				origin.addProperty(propProvenance, model.createTypedLiteral("Data obtained from www.bundestag.de."));
				origin.addProperty(propLicence, model.createTypedLiteral("Original data is © Deutscher Bundestag, this dataset CC0; http://creativecommons.org/publicdomain/zero/1.0/"));
				
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
			//it makes no sense that the empty resource should be indexed
			//wahlkreisURI = wahlkreisURI.concat(String.valueOf(index));

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
			//it makes no sense that the empty resource should be indexed
			//placeURI = placeURI.concat(String.valueOf(index));

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
			fraktionURI = "http://fraktionslos.de";

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
	 * THIS IS A HACK. A DIRTY ONE. BOO!
	 * @author Mb
	 * TODO: change this in the abgeordneten objects, too lazy to crawl again now
	 */
	
	private String getBundestagURI(Abgeordneter mdb) {
		String uriName = mdb.getURI().substring(mdb.getURI().lastIndexOf("/")+1);
		String bundestagUri = "";
		String baseUri = "http://www.bundestag.de/bundestag/abgeordnete17/biografien/";
		bundestagUri = baseUri + uriName.substring(0,1).toUpperCase() + "/" + uriName + ".html";
		
		return bundestagUri;
	}


	public static void main(String[] args) {
		
		BundestagConverter conv = new BundestagConverter(
		"http://www.bundestag.de/bundestag/abgeordnete17/alphabet/index.html", false);
		
		//RDFModel model = new RDFModel("http://127.0.0.1:8890/sparql");
		RDFExport export = new RDFExport();
		export.createModel(conv.getAbgeordnete());
		
		export.fileExport(System.getProperty("user.home") + "/Desktop/bundestagdata_05_05_13", IRDFExport.TURTLE);

//		try {
//			model.tripleStoreExport(INamespace.NAMSESPACE_MAP.get(INamespace.BTD));
//		} catch (Exception e) {
//			e.printStackTrace();
//		}

		System.out.println("done");
		
	}

}
