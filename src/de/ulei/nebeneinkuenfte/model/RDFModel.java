package de.ulei.nebeneinkuenfte.model;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import com.hp.hpl.jena.ontology.DatatypeProperty;
import com.hp.hpl.jena.ontology.ObjectProperty;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.vocabulary.RDF;

import de.ulei.nebeneinkuenfte.ui.NebeneinkuenfteApplication;

public abstract class RDFModel {

	protected final String SPD_FRAKTION = "http://www.spdfraktion.de/";
	protected final String SPD_LABEL = "SPD";

	protected final String CDU_CSU_FRAKTION = "http://www.cducsu.de/";
	protected final String CDU_CSU_LABEL = "CDU/CSU";

	protected final String DIE_LINKE_FRAKTION = "http://www.linksfraktion.de/";
	protected final String DIE_LINKE_LABEL = "Die Linke";

	protected final String FDP_FRAKTION = "http://www.fdp-fraktion.de/";
	protected final String FDP_LABEL = "FDP";

	protected final String GRUENE_FRAKTION = "http://www.gruene-bundestag.de/";
	protected final String GRUENE_LABEL = "Bündnis 90/Die Grünen";

	// triplestore param
	protected String triplestoreURL;

	// model
	protected OntModel model;

	// classes
	protected OntClass classAbgeordneter;
	protected OntClass classFraktion;
	protected OntClass classNebeneinkunft;
	protected OntClass classAuftraggeber;
	protected OntClass classWahlkreis;

	protected OntClass classPerson;
	protected OntClass classOrganization;
	protected OntClass classPlace;
	protected OntClass classDocument;

	// DatatypeProperties
	protected DatatypeProperty propFirstName;
	protected DatatypeProperty propGivenName;
	protected DatatypeProperty propTitle;
	protected DatatypeProperty propNebeneinkuenfteAnzahl;
	protected DatatypeProperty propNebeneinkuenfteMinimum;
	protected DatatypeProperty propNebeneinkuenfteMaximum;
	protected DatatypeProperty propNebeneinkuenftStufe;
	protected DatatypeProperty propNebeneinkuenftJahr;
	protected DatatypeProperty propNebeneinkuenftFrom;
	protected DatatypeProperty propNebeneinkuenftTo;
	protected DatatypeProperty propNebeneinkuenfteInterval;
	protected DatatypeProperty propCreator;
	protected DatatypeProperty propProvenance;
	protected DatatypeProperty propLicence;
	protected DatatypeProperty propNebeneinkuenftSourceString;
	protected DatatypeProperty propNebeneinkuenftTyp;
	protected DatatypeProperty propNebeneinkuenftOrt;
	protected DatatypeProperty propGeoLat;
	protected DatatypeProperty propGeoLong;

	// ObjectProperties
	protected ObjectProperty propHomepage;
	protected ObjectProperty propBundestagPage;
	protected ObjectProperty propMbox;
	protected ObjectProperty propMember;
	protected ObjectProperty propHatNebeneinkunft;
	protected ObjectProperty propHatAuftraggeber;
	protected ObjectProperty propBezahlt;
	protected ObjectProperty propIsPartOf;
	protected ObjectProperty propHatWahlkreis;

	public RDFModel() {

		Properties properties = new Properties();
		BufferedInputStream stream;
		try {
			stream = new BufferedInputStream(new FileInputStream(NebeneinkuenfteApplication.getInstance().getContext()
					.getBaseDirectory()
					+ "/external_data/config.properties"));
//			stream = new BufferedInputStream(new FileInputStream("./WebContent/external_data/config.properties"));
			properties.load(stream);
			stream.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		this.triplestoreURL = properties.getProperty("triplestoreURL");
		init();

	}

	public RDFModel(String triplestoreURL) {
		this.triplestoreURL = triplestoreURL;
		init();
	}

	private void init() {

		// create model and set namespaceMap
		this.model = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM);
		this.model.setNsPrefixes(INamespace.NAMSESPACE_MAP);

		// create classes and properties
		createClasses();
		createDatatypeProperties();
		createObjectProperties();
		// createFraktionResources();
	}

	private void createClasses() {

		// existing classes
		classPerson = model.createClass(model.getNsPrefixURI(INamespace.FOAF) + "Person");
		classOrganization = model.createClass(model.getNsPrefixURI(INamespace.FOAF) + "Organization");
		classPlace = model.createClass(model.getNsPrefixURI(INamespace.DBPO) + "PopulatedPlace");
		classDocument = model.createClass(model.getNsPrefixURI(INamespace.FOAF) + "Document");

		// self defined classes
		classWahlkreis = model.createClass(model.getNsPrefixURI(INamespace.BTD) + "Wahlkreis");
		classWahlkreis.addProperty(RDF.type, classPlace);
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
		propProvenance = model.createDatatypeProperty(model.getNsPrefixURI(INamespace.DC) + "provenance");
		propCreator = model.createDatatypeProperty(model.getNsPrefixURI(INamespace.DC) + "creator");
		propLicence = model.createDatatypeProperty(model.getNsPrefixURI(INamespace.DC) + "licence");
		// self defined properties
		propNebeneinkuenfteAnzahl = model.createDatatypeProperty(model.getNsPrefixURI(INamespace.BTD)
				+ "anzahlNebeneinkuenfte");
		propNebeneinkuenfteMinimum = model.createDatatypeProperty(model.getNsPrefixURI(INamespace.BTD)
				+ "minNebeneinkuenfte");
		propNebeneinkuenfteMaximum = model.createDatatypeProperty(model.getNsPrefixURI(INamespace.BTD)
				+ "maxNebeneinkuenfte");
		propNebeneinkuenftStufe = model.createDatatypeProperty(model.getNsPrefixURI(INamespace.BTD)
				+ "stufeNebeneinkunft");
		propNebeneinkuenfteInterval = model.createDatatypeProperty(model.getNsPrefixURI(INamespace.BTD)
				+ "intervallNebeneinkunft");
		propNebeneinkuenftJahr = model.createDatatypeProperty(model.getNsPrefixURI(INamespace.BTD)
				+ "jahrNebeneinkunft");
		propNebeneinkuenftTyp = model.createDatatypeProperty(model.getNsPrefixURI(INamespace.BTD) + "typNebeneinkunft");
		propNebeneinkuenftOrt = model.createDatatypeProperty(model.getNsPrefixURI(INamespace.BTD) + "ortNebeneinkunft");
		propNebeneinkuenftFrom = model.createDatatypeProperty(model.getNsPrefixURI(INamespace.BTD) + "startsAt");
		propNebeneinkuenftTo = model.createDatatypeProperty(model.getNsPrefixURI(INamespace.BTD) + "endsAt");
		propNebeneinkuenftSourceString = model.createDatatypeProperty(model.getNsPrefixURI(INamespace.BTD)
				+ "sourceString");
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
		
		propBundestagPage = model.createObjectProperty(model.getNsPrefixURI(INamespace.BTD) + "bundestagWebsite");
		propBundestagPage.setDomain(classAbgeordneter);

	}

}
