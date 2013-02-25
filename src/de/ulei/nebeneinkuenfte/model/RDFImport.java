package de.ulei.nebeneinkuenfte.model;

import java.io.ByteArrayOutputStream;
import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.vocabulary.RDF;
import com.hp.hpl.jena.vocabulary.RDFS;
import com.vaadin.data.util.BeanItemContainer;

import de.ulei.nebeneinkuenfte.ui.model.Abgeordneter;
import de.ulei.nebeneinkuenfte.ui.model.FraktionAuftraggeber;
import de.ulei.nebeneinkuenfte.ui.model.Nebentaetigkeit;

public class RDFImport extends RDFModel implements Serializable {

	private static final long serialVersionUID = -6012788701065823101L;

	private String politicanURI = "politicanURI";
	private String fractionURI = "fractionURI";
	private String wahlkreisURI = "wahlkreisURI";
	private String nebenjobURI = "nebenjobURI";
	private String auftraggeberURI = "auftraggeberURI";
	private String placeURI = "placeURI";

	private Map<String, City> cityCache;

	public RDFImport() {
		super();
	}

	public RDFImport(String triplestoreURL) {
		super(triplestoreURL);
	}

	public BeanItemContainer<Abgeordneter> getPersonBasicContainer() {

		StringBuffer query = new StringBuffer();

		query.append("SELECT ?");
		query.append(politicanURI);
		query.append(" ?");

		/*
		 * atomic values
		 */
		query.append(propFirstName.getLocalName());
		query.append(" ?");
		query.append(propGivenName.getLocalName());
		query.append(" ?");
		query.append(propHomepage.getLocalName());
		query.append(" ?");
		query.append(propMbox.getLocalName());
		query.append(" ?");
		query.append(propNebeneinkuenfteAnzahl.getLocalName());
		query.append(" ?");
		query.append(propNebeneinkuenfteMinimum.getLocalName());
		query.append(" ?");
		query.append(propNebeneinkuenfteMaximum.getLocalName());

		/*
		 * fraction resource
		 */

		query.append(" ?");
		query.append(fractionURI);
		query.append(" ?");
		query.append(classFraktion.getLocalName());

		/*
		 * Wahlkreis resource
		 */

		query.append(" ?");
		query.append(wahlkreisURI);
		query.append(" ?");
		query.append(classWahlkreis.getLocalName());

		/*
		 * WHERE clause
		 */

		/*
		 * atomic values
		 */

		query.append("\n ");
		query.append("WHERE {");
		query.append("\n ");

		query.append("?");
		query.append(politicanURI);

		query.append(" <");
		query.append(RDF.type.getURI());
		query.append("> <");
		query.append(classAbgeordneter);
		query.append(">.\n ");

		query.append("?");
		query.append(politicanURI);
		query.append(" <");
		query.append(propFirstName.getURI());
		query.append("> ?");
		query.append(propFirstName.getLocalName());
		query.append(".\n ");

		query.append("?");
		query.append(politicanURI);
		query.append(" <");
		query.append(propGivenName.getURI());
		query.append("> ?");
		query.append(propGivenName.getLocalName());
		query.append(".\n ");

		query.append("?");
		query.append(politicanURI);
		query.append(" <");
		query.append(propHomepage.getURI());
		query.append("> ?");
		query.append(propHomepage.getLocalName());
		query.append(".\n ");

		query.append("?");
		query.append(politicanURI);
		query.append(" <");
		query.append(propMbox.getURI());
		query.append("> ?");
		query.append(propMbox.getLocalName());
		query.append(".\n ");

		query.append("?");
		query.append(politicanURI);
		query.append(" <");
		query.append(propNebeneinkuenfteAnzahl.getURI());
		query.append("> ?");
		query.append(propNebeneinkuenfteAnzahl.getLocalName());
		query.append(".\n ");

		query.append("?");
		query.append(politicanURI);
		query.append(" <");
		query.append(propNebeneinkuenfteMinimum.getURI());
		query.append("> ?");
		query.append(propNebeneinkuenfteMinimum.getLocalName());
		query.append(".\n ");

		query.append("?");
		query.append(politicanURI);
		query.append(" <");
		query.append(propNebeneinkuenfteMaximum.getURI());
		query.append("> ?");
		query.append(propNebeneinkuenfteMaximum.getLocalName());
		query.append(".\n ");

		/*
		 * get fraction resource and add its URI and the label
		 */

		query.append("?");
		query.append(politicanURI);
		query.append(" <");
		query.append(propIsPartOf.getURI());
		query.append("> ?");
		query.append(fractionURI);
		query.append(".\n ");

		query.append("?");
		query.append(fractionURI);
		query.append(" <");
		query.append(RDFS.label.getURI());
		query.append("> ?");
		query.append(classFraktion.getLocalName());
		query.append(".\n ");

		/*
		 * get wahlkreis resource and add its URI and the label
		 */

		query.append("?");
		query.append(politicanURI);
		query.append(" <");
		query.append(propHatWahlkreis.getURI());
		query.append("> ?");
		query.append(wahlkreisURI);
		query.append(".\n ");

		query.append("?");
		query.append(wahlkreisURI);
		query.append(" <");
		query.append(RDFS.label.getURI());
		query.append("> ?");
		query.append(classWahlkreis.getLocalName());
		query.append(".\n ");

		query.append("}");

		/*
		 * create list of politcans
		 */

		Abgeordneter politican;
		BeanItemContainer<Abgeordneter> container = new BeanItemContainer<Abgeordneter>(Abgeordneter.class);

		Query q = QueryFactory.create(query.toString());
		QueryExecution qexec = QueryExecutionFactory.sparqlService(triplestoreURL, q,
				INamespace.NAMSESPACE_MAP.get(INamespace.BTD));
		ResultSet rs = qexec.execSelect();

		// iterate over ResultSet and fill list with politicans
		while (rs.hasNext()) {

			QuerySolution qs = rs.next();

			politican = new Abgeordneter();
			politican.setURI(qs.getResource(politicanURI).getURI());
			politican.setForename(qs.getLiteral(propFirstName.getLocalName()).getString());
			politican.setLastname(qs.getLiteral(propGivenName.getLocalName()).getString());
			politican.setHomepage(qs.getResource(propHomepage.getLocalName()).getURI());
			politican.setEmail(qs.getResource(propMbox.getLocalName()).getURI());
			politican.setWahlkreisUri(qs.getResource(wahlkreisURI).getURI());
			politican.setWahlkreisName(qs.getLiteral(classWahlkreis.getLocalName()).getString());
			politican.setFraktionUri(qs.getResource(fractionURI).getURI());
			politican.setFraktion(qs.getLiteral(classFraktion.getLocalName()).getString());
			politican.setAnzahlNebeneinkuenfte(qs.getLiteral(propNebeneinkuenfteAnzahl.getLocalName()).getInt());
			politican.setMinZusatzeinkommen(qs.getLiteral(propNebeneinkuenfteMinimum.getLocalName()).getInt());
			politican.setMaxZusatzeinkommen(qs.getLiteral(propNebeneinkuenfteMaximum.getLocalName()).getInt());

			container.addBean(politican);

		}

		return container;

	}

	public BeanItemContainer<Nebentaetigkeit> getPersonPersonContainer(String mdbURI) {

		/*
		 * collect information for sidelinejobs
		 */

		StringBuffer query = new StringBuffer();

		/*
		 * atomic values
		 */

		query.append("SELECT ?");
		query.append(nebenjobURI);
		query.append(" ?");
		query.append(propNebeneinkuenftJahr.getLocalName());
		query.append(" ?");
		query.append(propNebeneinkuenftStufe.getLocalName());
		query.append(" ?");
		query.append(propNebeneinkuenftTyp.getLocalName());

		/*
		 * get auftraggeber resource and add its properties
		 */

		query.append(" ?");
		query.append(auftraggeberURI);
		query.append(" ?");
		query.append(classAuftraggeber.getLocalName());
		query.append(" ?");
		query.append(propHomepage.getLocalName());

		/*
		 * get place resource
		 */

		query.append(" ?");
		query.append(classPlace.getLocalName());

		/*
		 * WHERE clause
		 */

		query.append("\n ");
		query.append("WHERE {");
		query.append("\n ");

		/*
		 * atomic values
		 */

		query.append("<");
		query.append(mdbURI);
		query.append("> <");
		query.append(propHatNebeneinkunft.getURI());
		query.append("> ?");
		query.append(nebenjobURI);
		query.append(".\n ");

		query.append("?");
		query.append(nebenjobURI);
		query.append(" <");
		query.append(propNebeneinkuenftJahr.getURI());
		query.append("> ?");
		query.append(propNebeneinkuenftJahr.getLocalName());
		query.append(".\n ");

		query.append("?");
		query.append(nebenjobURI);
		query.append(" <");
		query.append(propNebeneinkuenftStufe.getURI());
		query.append("> ?");
		query.append(propNebeneinkuenftStufe.getLocalName());
		query.append(".\n ");

		query.append("?");
		query.append(nebenjobURI);
		query.append(" <");
		query.append(propNebeneinkuenftTyp.getURI());
		query.append("> ?");
		query.append(propNebeneinkuenftTyp.getLocalName());
		query.append(".\n ");

		/*
		 * get auftraggeber resource and add its properties
		 */

		query.append("?");
		query.append(nebenjobURI);
		query.append(" <");
		query.append(propHatAuftraggeber.getURI());
		query.append("> ?");
		query.append(auftraggeberURI);
		query.append(".\n ");

		query.append("?");
		query.append(auftraggeberURI);
		query.append(" <");
		query.append(RDFS.label.getURI());
		query.append("> ?");
		query.append(classAuftraggeber.getLocalName());
		query.append(".\n ");

		query.append("?");
		query.append(auftraggeberURI);
		query.append(" <");
		query.append(propHomepage.getURI());
		query.append("> ?");
		query.append(propHomepage.getLocalName());
		query.append(".\n ");

		/*
		 * get place resource
		 */

		query.append("?");
		query.append(nebenjobURI);
		query.append(" <");
		query.append(propNebeneinkuenftOrt.getURI());
		query.append("> ?");
		query.append(classPlace.getLocalName());
		query.append(".\n ");

		query.append("}");

		BeanItemContainer<Nebentaetigkeit> container = new BeanItemContainer<Nebentaetigkeit>(Nebentaetigkeit.class);
		Nebentaetigkeit sidelineJob;

		Query q = QueryFactory.create(query.toString());
		QueryExecution qexec = QueryExecutionFactory.sparqlService(triplestoreURL, q,
				INamespace.NAMSESPACE_MAP.get(INamespace.BTD));
		ResultSet rs = qexec.execSelect();

		// iterate over ResultSet and add sidelineJobs for every politican
		while (rs.hasNext()) {

			QuerySolution qs = rs.next();

			sidelineJob = new Nebentaetigkeit();
			sidelineJob.setType(qs.getLiteral(propNebeneinkuenftTyp.getLocalName()).getString());
			sidelineJob.setYear(qs.getLiteral(propNebeneinkuenftJahr.getLocalName()).getString());
			sidelineJob.setStufe(qs.getLiteral(propNebeneinkuenftStufe.getLocalName()).getString());
			sidelineJob.setAuftraggeberHomepage(qs.getResource(propHomepage.getLocalName()).getURI());
			sidelineJob.setAuftraggeber(qs.getLiteral(classAuftraggeber.getLocalName()).getString());
			sidelineJob.setAuftragUri(qs.getResource(auftraggeberURI).getURI());
			sidelineJob.setPlaceUri(qs.getResource(classPlace.getLocalName()).getURI());

			container.addBean(sidelineJob);

		}

		query = new StringBuffer();
		query.append("SELECT ?label ?lat ?long WHERE {");
		query.append(" <");
		query.append(placeURI);
		query.append("> <http://www.w3.org/2003/01/geo/wgs84_pos#lat> ?lat ;");
		query.append(" <http://www.w3.org/2003/01/geo/wgs84_pos#long> ?long ;");
		query.append("  <http://www.w3.org/2000/01/rdf-schema#label> ?label ;");
		query.append(" FILTER(langMatches(lang(?label),'DE')) . }");

		// get pace information from dbpedia
		queryCityInformation(container.getItemIds());

		return container;
	}

	public BeanItemContainer<FraktionAuftraggeber> getPersonOriginContainer(String originURI) {
		/*
		 * collect information for FraktionAuftraggeber
		 */

		StringBuffer query = new StringBuffer();

		/*
		 * atomic values
		 */

		query.append("SELECT ?");
		query.append(nebenjobURI);
		query.append(" ?");
		query.append(politicanURI);
		query.append(" ?");
		query.append(propNebeneinkuenftJahr.getLocalName());
		query.append(" ?");
		query.append(propNebeneinkuenftStufe.getLocalName());
		query.append(" ?");
		query.append(propNebeneinkuenftTyp.getLocalName());
		query.append(" ?");
		query.append(propFirstName.getLocalName());
		query.append(" ?");
		query.append(propGivenName.getLocalName());
		query.append(" ?");
		query.append(auftraggeberURI);
		query.append(" ?");
		query.append(classAuftraggeber.getLocalName());

		/*
		 * get place resource
		 */

		query.append(" ?");
		query.append(classPlace.getLocalName());

		/*
		 * get party resource
		 */

		query.append(" ?");
		query.append(fractionURI);
		query.append(" ?");
		query.append(classFraktion.getLocalName());

		/*
		 * WHERE clause
		 */

		/*
		 * politican infos
		 */

		query.append("\n ");
		query.append("WHERE {");
		query.append("\n ");

		query.append("<");
		query.append(originURI);
		query.append("> <");
		query.append(propBezahlt.getURI());
		query.append("> ?");
		query.append(politicanURI);
		query.append(".\n ");

		query.append("?");
		query.append(politicanURI);
		query.append(" <");
		query.append(propGivenName.getURI());
		query.append("> ?");
		query.append(propGivenName.getLocalName());
		query.append(".\n ");

		query.append("?");
		query.append(politicanURI);
		query.append(" <");
		query.append(propFirstName.getURI());
		query.append("> ?");
		query.append(propFirstName.getLocalName());
		query.append(".\n ");

		query.append("?");
		query.append(politicanURI);
		query.append(" <");
		query.append(propIsPartOf.getURI());
		query.append("> ?");
		query.append(fractionURI);
		query.append(".\n ");

		query.append("?");
		query.append(fractionURI);
		query.append(" <");
		query.append(RDFS.label.getURI());
		query.append("> ?");
		query.append(classFraktion.getLocalName());
		query.append(".\n ");

		/*
		 * sideline infos
		 */

		query.append("?");
		query.append(politicanURI);
		query.append(" <");
		query.append(propHatNebeneinkunft.getURI());
		query.append("> ?");
		query.append(nebenjobURI);
		query.append(".\n ");

		query.append("?");
		query.append(nebenjobURI);
		query.append(" <");
		query.append(propNebeneinkuenftJahr.getURI());
		query.append("> ?");
		query.append(propNebeneinkuenftJahr.getLocalName());
		query.append(".\n ");

		query.append("?");
		query.append(nebenjobURI);
		query.append(" <");
		query.append(propNebeneinkuenftStufe.getURI());
		query.append("> ?");
		query.append(propNebeneinkuenftStufe.getLocalName());
		query.append(".\n ");

		query.append("?");
		query.append(nebenjobURI);
		query.append(" <");
		query.append(propNebeneinkuenftTyp.getURI());
		query.append("> ?");
		query.append(propNebeneinkuenftTyp.getLocalName());
		query.append(".\n ");

		query.append("?");
		query.append(nebenjobURI);
		query.append(" <");
		query.append(propNebeneinkuenftOrt.getURI());
		query.append("> ?");
		query.append(classPlace.getLocalName());
		query.append(".\n ");

		query.append("?");
		query.append(nebenjobURI);
		query.append(" <");
		query.append(propHatAuftraggeber.getURI());
		query.append("> <");
		query.append(originURI);
		query.append(">.\n ");

		query.append("?");
		query.append(nebenjobURI);
		query.append(" <");
		query.append(propHatAuftraggeber.getURI());
		query.append("> ?");
		query.append(auftraggeberURI);
		query.append(".\n ");

		query.append("?");
		query.append(auftraggeberURI);
		query.append(" <");
		query.append(RDFS.label.getURI());
		query.append("> ?");
		query.append(classAuftraggeber.getLocalName());
		query.append(".\n ");

		query.append("?");
		query.append(auftraggeberURI);
		query.append(" <");
		query.append(propHomepage.getURI());
		query.append("> ?");
		query.append(propHomepage.getLocalName());
		query.append(".\n ");

		query.append("}");

		Nebentaetigkeit sidelineJob;
		Abgeordneter politican;
		BeanItemContainer<FraktionAuftraggeber> container = new BeanItemContainer<FraktionAuftraggeber>(
				FraktionAuftraggeber.class);

		Query q = QueryFactory.create(query.toString());
		QueryExecution qexec = QueryExecutionFactory.sparqlService(triplestoreURL, q,
				INamespace.NAMSESPACE_MAP.get(INamespace.BTD));
		ResultSet rs = qexec.execSelect();

		// iterate over ResultSet
		while (rs.hasNext()) {

			QuerySolution qs = rs.next();

			sidelineJob = new Nebentaetigkeit();
			sidelineJob.setAuftraggeber(qs.getLiteral(classAuftraggeber.getLocalName()).getString());
			sidelineJob.setType(qs.getLiteral(propNebeneinkuenftTyp.getLocalName()).getString());
			sidelineJob.setYear(qs.getLiteral(propNebeneinkuenftJahr.getLocalName()).getString());
			sidelineJob.setStufe(qs.getLiteral(propNebeneinkuenftStufe.getLocalName()).getString());
			sidelineJob.setPlaceUri(qs.getResource(classPlace.getLocalName()).getURI());

			politican = new Abgeordneter();
			politican.setURI(qs.getResource(politicanURI).getURI());
			politican.setForename(qs.getLiteral(propFirstName.getLocalName()).getString());
			politican.setLastname(qs.getLiteral(propGivenName.getLocalName()).getString());
			politican.setFraktionUri(qs.getResource(fractionURI).getURI());
			politican.setFraktion(qs.getLiteral(classFraktion.getLocalName()).getString());

			container.addBean(new FraktionAuftraggeber(politican, sidelineJob));

		}

		query = new StringBuffer();
		query.append("SELECT ?label ?lat ?long WHERE {");
		query.append(" <");
		query.append(placeURI);
		query.append("> <http://www.w3.org/2003/01/geo/wgs84_pos#lat> ?lat ;");
		query.append(" <http://www.w3.org/2003/01/geo/wgs84_pos#long> ?long ;");
		query.append("  <http://www.w3.org/2000/01/rdf-schema#label> ?label ;");
		query.append(" FILTER(langMatches(lang(?label),'DE')) . }");

		// get pace information from dbpedia
		for (FraktionAuftraggeber fracJob : container.getItemIds()) {

			q = QueryFactory.create(query.toString().replace(placeURI, fracJob.getPlaceURI()));
			qexec = QueryExecutionFactory.sparqlService("http://dbpedia.org/sparql", q);
			rs = qexec.execSelect();

			// iterate over ResultSet and add sidelineJobs for every politican
			while (rs.hasNext()) {

				QuerySolution qs = rs.next();

				fracJob.setPlace(qs.get("label").asLiteral().getString());
				fracJob.setLatitude(qs.get("lat").asLiteral().getFloat());
				fracJob.setLongitude(qs.get("long").asLiteral().getFloat());

			}

		}
		return container;
	}

	public BeanItemContainer<FraktionAuftraggeber> getPersonFractionContainer(String fractionURI) {

		/*
		 * collect information for FraktionAuftraggeber
		 */

		StringBuffer query = new StringBuffer();

		/*
		 * atomic values
		 */

		query.append("SELECT ?");
		query.append(nebenjobURI);
		query.append(" ?");
		query.append(politicanURI);
		query.append(" ?");
		query.append(propNebeneinkuenftJahr.getLocalName());
		query.append(" ?");
		query.append(propNebeneinkuenftStufe.getLocalName());
		query.append(" ?");
		query.append(propNebeneinkuenftTyp.getLocalName());
		query.append(" ?");
		query.append(propFirstName.getLocalName());
		query.append(" ?");
		query.append(propGivenName.getLocalName());
		query.append(" ?");
		query.append(auftraggeberURI);
		query.append(" ?");
		query.append(classAuftraggeber.getLocalName());
		query.append(" ?");
		query.append(propHomepage.getLocalName());

		/*
		 * get place resource
		 */

		query.append(" ?");
		query.append(classPlace.getLocalName());

		/*
		 * get party resource
		 */

		query.append(" ?");
		query.append(classFraktion.getLocalName());

		/*
		 * WHERE clause
		 */

		/*
		 * politican infos
		 */

		query.append("\n ");
		query.append("WHERE {");
		query.append("\n ");

		query.append("?");
		query.append(politicanURI);
		query.append(" <");
		query.append(propIsPartOf.getURI());
		query.append("> <");
		query.append(fractionURI);
		query.append(">.\n ");

		query.append("?");
		query.append(politicanURI);
		query.append(" <");
		query.append(propGivenName.getURI());
		query.append("> ?");
		query.append(propGivenName.getLocalName());
		query.append(".\n ");

		query.append("?");
		query.append(politicanURI);
		query.append(" <");
		query.append(propFirstName.getURI());
		query.append("> ?");
		query.append(propFirstName.getLocalName());
		query.append(".\n ");

		query.append("<");
		query.append(fractionURI);
		query.append("> <");
		query.append(RDFS.label.getURI());
		query.append("> ?");
		query.append(classFraktion.getLocalName());
		query.append(".\n ");

		/*
		 * sideline infos
		 */

		query.append("?");
		query.append(politicanURI);
		query.append(" <");
		query.append(propHatNebeneinkunft.getURI());
		query.append("> ?");
		query.append(nebenjobURI);
		query.append(".\n ");

		query.append("?");
		query.append(nebenjobURI);
		query.append(" <");
		query.append(propNebeneinkuenftJahr.getURI());
		query.append("> ?");
		query.append(propNebeneinkuenftJahr.getLocalName());
		query.append(".\n ");

		query.append("?");
		query.append(nebenjobURI);
		query.append(" <");
		query.append(propNebeneinkuenftStufe.getURI());
		query.append("> ?");
		query.append(propNebeneinkuenftStufe.getLocalName());
		query.append(".\n ");

		query.append("?");
		query.append(nebenjobURI);
		query.append(" <");
		query.append(propNebeneinkuenftTyp.getURI());
		query.append("> ?");
		query.append(propNebeneinkuenftTyp.getLocalName());
		query.append(".\n ");

		query.append("?");
		query.append(nebenjobURI);
		query.append(" <");
		query.append(propNebeneinkuenftOrt.getURI());
		query.append("> ?");
		query.append(classPlace.getLocalName());
		query.append(".\n ");

		query.append("?");
		query.append(nebenjobURI);
		query.append(" <");
		query.append(propHatAuftraggeber.getURI());
		query.append("> ?");
		query.append(auftraggeberURI);
		query.append(".\n ");

		query.append("?");
		query.append(auftraggeberURI);
		query.append(" <");
		query.append(RDFS.label.getURI());
		query.append("> ?");
		query.append(classAuftraggeber.getLocalName());
		query.append(".\n ");

		query.append("?");
		query.append(auftraggeberURI);
		query.append(" <");
		query.append(propHomepage.getURI());
		query.append("> ?");
		query.append(propHomepage.getLocalName());
		query.append(".\n ");

		query.append("}");
		Nebentaetigkeit sidelineJob;
		Abgeordneter politican;

		Map<Abgeordneter, Nebentaetigkeit> resultMap = new HashMap<Abgeordneter, Nebentaetigkeit>();

		Query q = QueryFactory.create(query.toString());
		QueryExecution qexec = QueryExecutionFactory.sparqlService(triplestoreURL, q,
				INamespace.NAMSESPACE_MAP.get(INamespace.BTD));
		ResultSet rs = qexec.execSelect();

		// iterate over ResultSet
		while (rs.hasNext()) {

			QuerySolution qs = rs.next();

			sidelineJob = new Nebentaetigkeit();
			sidelineJob.setAuftraggeber(qs.getLiteral(classAuftraggeber.getLocalName()).getString());
			sidelineJob.setType(qs.getLiteral(propNebeneinkuenftTyp.getLocalName()).getString());
			sidelineJob.setYear(qs.getLiteral(propNebeneinkuenftJahr.getLocalName()).getString());
			sidelineJob.setStufe(qs.getLiteral(propNebeneinkuenftStufe.getLocalName()).getString());
			sidelineJob.setPlaceUri(qs.getResource(classPlace.getLocalName()).getURI());
			sidelineJob.setAuftraggeberHomepage(qs.getResource(propHomepage.getLocalName()).getURI());

			politican = new Abgeordneter();
			politican.setURI(qs.getResource(politicanURI).getURI());
			politican.setForename(qs.getLiteral(propFirstName.getLocalName()).getString());
			politican.setLastname(qs.getLiteral(propGivenName.getLocalName()).getString());
			politican.setFraktionUri(fractionURI);
			politican.setFraktion(qs.getLiteral(classFraktion.getLocalName()).getString());

			resultMap.put(politican, sidelineJob);

		}

		queryCityInformation(resultMap.values());
		BeanItemContainer<FraktionAuftraggeber> container = new BeanItemContainer<FraktionAuftraggeber>(
				FraktionAuftraggeber.class);

		for (Entry<Abgeordneter, Nebentaetigkeit> entry : resultMap.entrySet()) {
			container.addBean(new FraktionAuftraggeber(entry.getKey(), entry.getValue()));
		}

		return container;

	}

	public ByteArrayOutputStream getRessource(String resourceURI, String rdfType) {

		String queryString = "SELECT * WHERE {<".concat(resourceURI).concat("> ?p ?o}");
		Query query = QueryFactory.create(queryString);

		QueryExecution qexec = QueryExecutionFactory.sparqlService(triplestoreURL, query,
				INamespace.NAMSESPACE_MAP.get(INamespace.BTD));
		ResultSet rs = qexec.execSelect();

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
		qexec.close();

		return out;
	}

	public ByteArrayOutputStream querySubject(String resourceURI, String rdfType) {

		String queryString = "SELECT * WHERE {<".concat(resourceURI).concat("> ?p ?o}");
		Query query = QueryFactory.create(queryString);

		QueryExecution qexec = QueryExecutionFactory.sparqlService(triplestoreURL, query,
				INamespace.NAMSESPACE_MAP.get(INamespace.BTD));
		ResultSet rs = qexec.execSelect();

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
		qexec.close();

		return out;
	}

	private void queryCityInformation(Collection<Nebentaetigkeit> container) {

		Query q;
		QueryExecution qexec;
		ResultSet rs;

		City city;
		StringBuffer query;
		StringBuffer cityBuffer;

		Set<String> citySet = new HashSet<String>();
		for (Nebentaetigkeit nebentaetigkeit : container)
			citySet.add(nebentaetigkeit.getPlaceUri());

		boolean run = true;
		int count = 0;

		while (run) {

			cityBuffer = new StringBuffer();
			cityBuffer.append("{");
			cityBuffer.append("<");
			cityBuffer.append(placeURI);
			cityBuffer.append("> <http://www.w3.org/2003/01/geo/wgs84_pos#lat> ?lat ;");
			cityBuffer.append(" <http://www.w3.org/2003/01/geo/wgs84_pos#long> ?long ;");
			cityBuffer.append(" <http://www.w3.org/2000/01/rdf-schema#label> ?label .");
			cityBuffer.append("?uri <http://www.w3.org/2000/01/rdf-schema#label> ?label");
			cityBuffer.append("} UNION ");

			query = new StringBuffer();
			query = new StringBuffer();
			query.append("SELECT ?uri ?label ?lat ?long WHERE {");

			Iterator<String> it = citySet.iterator();
			while (it.hasNext()) {
				query.append(cityBuffer.toString().replace(placeURI, it.next()));
				it.remove();
				if (count++ == 50) {
					count = 0;
					break;
				}
			}

			if (citySet.size() <= 0)
				run = false;

			query.replace(query.length() - 6, query.length(), "");
			query.append(" FILTER(langMatches(lang(?label),'DE')) . ");
			query.append("}");

			q = QueryFactory.create(query.toString());
			qexec = QueryExecutionFactory.sparqlService("http://dbpedia.org/sparql", q);
			rs = qexec.execSelect();

			cityCache = new HashMap<String, City>();

			// iterate over ResultSet and get city informations
			while (rs.hasNext()) {

				QuerySolution qs = rs.next();

				city = new City();
				city.setLabel(qs.get("label").asLiteral().getString());
				city.setLatitude(qs.get("lat").asLiteral().getFloat());
				city.setLongitude(qs.get("long").asLiteral().getFloat());

				cityCache.put(qs.getResource("uri").getURI().toString(), city);
				city = null;

			}

		}
		// update container
		for (Nebentaetigkeit nebentaetigkeit : container) {

			city = cityCache.get(nebentaetigkeit.getPlaceUri());

			if (city != null) {

				nebentaetigkeit.setPlace(city.getLabel());
				nebentaetigkeit.setLatitude(city.getLatitude());
				nebentaetigkeit.setLongitude(city.getLongitude());
			}
		}

	}

	private class City {

		private String label;
		private float longitude;
		private float latitude;

		public String getLabel() {
			return label;
		}

		public float getLongitude() {
			return longitude;
		}

		public float getLatitude() {
			return latitude;
		}

		public void setLabel(String label) {
			this.label = label;
		}

		public void setLongitude(float longitude) {
			this.longitude = longitude;
		}

		public void setLatitude(float latitude) {
			this.latitude = latitude;
		}

	}
}
