package de.ulei.nebeneinkuenfte.model;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import com.hp.hpl.jena.ontology.DatatypeProperty;
import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.ObjectProperty;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.ontology.OntResource;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.util.iterator.ExtendedIterator;
import com.hp.hpl.jena.vocabulary.RDF;
import com.hp.hpl.jena.vocabulary.RDFS;

import de.ulei.nebeneinkuenfte.model.crawler.BundestagConverter;
import de.ulei.nebeneinkuenfte.ui.model.Abgeordneter;
import de.ulei.nebeneinkuenfte.ui.model.Nebentaetigkeit;

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

	// prefix
	private final String PREFIX = "";

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

	/**
	 * TODO: identify same origins and give them identical IDs!!!
	 */

	private void createPersonResources(List<Abgeordneter> personList) {

		Resource politician;
		Resource sidelineJob;
		Resource origin;
		int index = 0;

		// create resource for each member of the parliament
		for (Abgeordneter mdb : personList) {

			politician = model.createResource(mdb.getURI(), classAbgeordneter);
			politician.addProperty(propFirstName, model.createLiteral(mdb.getForename()));
			politician.addProperty(propGivenName, model.createLiteral(mdb.getLastname()));
			politician.addProperty(propMbox, model.createLiteral(mdb.getEmail() != null ? mdb.getEmail() : ""));
			politician.addProperty(propHomepage, model.createLiteral(mdb.getHomepage() != null ? mdb.getHomepage() : ""));
			politician.addProperty(propNebeneinkuenfteAnzahl, model.createTypedLiteral(mdb.getAnzahlNebeneinkuenfte()));
			politician.addProperty(propNebeneinkuenfteMaximum, model.createTypedLiteral(mdb.getMaxZusatzeinkommen()));
			politician.addProperty(propNebeneinkuenfteMinimum, model.createTypedLiteral(mdb.getMinZusatzeinkommen()));
			politician.addProperty(propHatWahlkreis, createWahlkreisResource(mdb));
			politician.addProperty(propIsPartOf, createFraktionResource(mdb, politician));

			// create resource for each sideline job of the politician
			for (Nebentaetigkeit nt : mdb.getNebentaetigkeiten()) {

				// create resource for origin
				origin = model.createResource(model.getNsPrefixURI(INamespace.BTD) + "auftraggeber" + index,
						classAuftraggeber);
				origin.addProperty(RDFS.label,
						model.createLiteral(nt.getAuftraggeber() != null ? nt.getAuftraggeber() : "unbekannt"));

				// create resource for the job itself
				sidelineJob = model.createResource(model.getNsPrefixURI(INamespace.BTD) + "nebeneinkunft_" + index,
						classNebeneinkunft);
				sidelineJob.addProperty(propNebeneinkuenftStufe,
						model.createLiteral(nt.getStufe() != null ? nt.getStufe() : "unbekannt"));
				sidelineJob.addProperty(propNebeneinkuenftJahr,
						model.createLiteral(nt.getYear() != null ? nt.getYear() : "unbekannt"));
				sidelineJob.addProperty(propNebeneinkuenftTyp,
						model.createLiteral(nt.getType() != null ? nt.getType() : "unbekannt"));
				sidelineJob.addProperty(propNebeneinkuenftOrt,
						model.createLiteral(nt.getPlace() != null ? nt.getPlace() : "unbekannt"));

				// set origin and job in relation
				sidelineJob.addProperty(propHatAuftraggeber, origin);
				// set politician and origin in relation
				origin.addProperty(propBezahlt, politician);
				index++;

			}

		}

	}

	private Resource createWahlkreisResource(Abgeordneter abgeordneter) {

		String wahlkreisURI = abgeordneter.getWahlkreisUri();
		if (wahlkreisURI == null || wahlkreisURI.trim().isEmpty())
			wahlkreisURI = "http://keinwahlkreis.de";

		Resource wahlkreis = null;

		Individual wahlkreisInstance;
		ExtendedIterator<? extends OntResource> instances = classWahlkreis.listInstances();

		while (instances.hasNext()) {

			wahlkreisInstance = (Individual) instances.next();
			if (wahlkreisInstance.getURI() != null) {
				if (wahlkreisInstance.getURI().equals(abgeordneter.getWahlkreisUri())) {
					wahlkreis = wahlkreisInstance;
					break;
				}
			}
		}

		if (wahlkreis != null)
			return wahlkreis;

		wahlkreis = model.createResource(wahlkreisURI, classWahlkreis);
		wahlkreis.addProperty(RDFS.label,
				model.createTypedLiteral(abgeordneter.getWahlkreisName() != null ? abgeordneter.getWahlkreisName()
						: "unbekannt"));
		return wahlkreis;

	}

	private Resource createFraktionResource(Abgeordneter abgeordneter, Resource politician) {

		String fraktionURI = abgeordneter.getWahlkreisUri();
		if (fraktionURI == null || fraktionURI.trim().isEmpty())
			fraktionURI = "http://keinefaraktion.de";

		Resource fraktion = null;

		Individual fraktionInstance;
		ExtendedIterator<? extends OntResource> instances = classFraktion.listInstances();

		while (instances.hasNext()) {

			fraktionInstance = (Individual) instances.next();
			if (fraktionInstance.getURI() != null) {
				if (fraktionInstance.getURI().equals(abgeordneter.getFraktionUri())) {
					fraktion = fraktionInstance;
					break;
				}
			}
		}

		if (fraktion != null) {
			fraktion.addProperty(propMember, politician);
			return fraktion;
		}

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

	public static void main(String[] args) {

		BundestagConverter conv = new BundestagConverter(
				"http://www.bundestag.de/bundestag/abgeordnete17/alphabet/index.html", false);

		RDFModel model = new RDFModel();
		model.createModel(conv.getAbgeordnete());

		String path = System.getProperty("user.home") + "/Desktop/nebeneinkunft";
		model.fileExport(path, IRDFExport.N3);
		System.out.println("done");

	}

}
