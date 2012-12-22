package de.ulei.nebeneinkuenfte.model;

import com.hp.hpl.jena.ontology.DatatypeProperty;
import com.hp.hpl.jena.ontology.ObjectProperty;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.vocabulary.RDF;

public class RDFModel {

	// prefix
	private final String PREFIX = "";

	// model
	private OntModel model;

	// classes
	private OntClass classAbgeordneter;
	private OntClass classPartei;
	private OntClass classNebeneinkunft;
	private OntClass classAuftraggeber;

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

	// ObjectProperties
	private ObjectProperty propHomepage;
	private ObjectProperty propWahlkreis;
	private ObjectProperty propMbox;
	private ObjectProperty propMember;
	private ObjectProperty propHatNebeneinkunft;
	private ObjectProperty propHatAuftraggeber;
	private ObjectProperty propBezahlt;
	private ObjectProperty propIsPartOf;

	public RDFModel() {

		// create model and set namespaceMap
		this.model = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM);
		this.model.setNsPrefixes(INamespace.NAMSESPACE_MAP);

		// create classes and properties
		createClasses();
		createDatatypeProperties();
		createObjectProperties();

	}

	private void createClasses() {

		// existing classes
		classPerson = model.createClass(model.getNsPrefixURI(INamespace.FOAF)
				+ "Person");
		classOrganization = model.createClass(model
				.getNsPrefixURI(INamespace.FOAF) + "Organization");

		// self defined classes
		classPartei = model.createClass(model.getNsPrefixURI(INamespace.BTD)
				+ "Partei");
		classPartei.addProperty(RDF.type, classOrganization);
		classNebeneinkunft = model.createClass(model
				.getNsPrefixURI(INamespace.BTD) + "Nebeneinkunft");
		classAuftraggeber = model.createClass(model
				.getNsPrefixURI(INamespace.BTD) + "Auftraggeber");
		classAbgeordneter = model.createClass(model
				.getNsPrefixURI(INamespace.BTD) + "Abgeordneter");
		classAbgeordneter.addProperty(RDF.type, classPerson);

	}

	private void createDatatypeProperties() {

		// existing properties
		propFirstName = model.createDatatypeProperty(model
				.getNsPrefixURI(INamespace.FOAF) + "firstName");
		propGivenName = model.createDatatypeProperty(model
				.getNsPrefixURI(INamespace.FOAF) + "givenName");
		propTitle = model.createDatatypeProperty(model
				.getNsPrefixURI(INamespace.FOAF) + "title");

		// self defined properties
		propNebeneinkuenfteAnzahl = model.createDatatypeProperty(model
				.getNsPrefixURI(INamespace.BTD) + "anzahlNebeneinkuenfte");
		propNebeneinkuenfteMinimum = model.createDatatypeProperty(model
				.getNsPrefixURI(INamespace.BTD) + "minNebeneinkuenfte");
		propNebeneinkuenfteMaximum = model.createDatatypeProperty(model
				.getNsPrefixURI(INamespace.BTD) + "maxNebeneinkuenfte");
		propNebeneinkuenftStufe = model.createDatatypeProperty(model
				.getNsPrefixURI(INamespace.BTD) + "stufeNebeneinkunft");

	}

	private void createObjectProperties() {

		// existing properties
		propHomepage = model.createObjectProperty(model
				.getNsPrefixURI(INamespace.FOAF) + "homepage");
		propMbox = model.createObjectProperty(model
				.getNsPrefixURI(INamespace.FOAF) + "mbox");
		propWahlkreis = model.createObjectProperty(model
				.getNsPrefixURI(INamespace.BTD) + "wahlkreis");
		propMember = model.createObjectProperty(model
				.getNsPrefixURI(INamespace.FOAF) + "member");
		propIsPartOf = model.createObjectProperty(model
				.getNsPrefixURI(INamespace.DC) + "isPartOf");

		// self defined properties
		propHatNebeneinkunft = model.createObjectProperty(model
				.getNsPrefixURI(INamespace.BTD) + "hatNebeneinkunft");
		propHatNebeneinkunft.setDomain(classAbgeordneter);
		propHatNebeneinkunft.setRange(classNebeneinkunft);

		propHatAuftraggeber = model.createObjectProperty(model
				.getNsPrefixURI(INamespace.BTD) + "hatAuftraggeber");
		propHatAuftraggeber.setDomain(classNebeneinkunft);
		propHatAuftraggeber.setRange(classAuftraggeber);

		propBezahlt = model.createObjectProperty(model
				.getNsPrefixURI(INamespace.BTD) + "bezahlt");
		propBezahlt.setDomain(classAuftraggeber);
		propBezahlt.setRange(classAbgeordneter);

	}

}
