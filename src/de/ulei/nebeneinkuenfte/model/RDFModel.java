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
	private OntClass classPartei;
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

	// ObjectProperties
	private ObjectProperty propHomepage;
	private ObjectProperty propWahlkreis;
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

	}

	private void createClasses() {

		// existing classes
		classPerson = model.createClass(model.getNsPrefixURI(INamespace.FOAF) + "Person");
		classOrganization = model.createClass(model.getNsPrefixURI(INamespace.FOAF) + "Organization");

		// self defined classes
		classWahlkreis = model.createClass(model.getNsPrefixURI(INamespace.BTD) + "Wahlkreis");
		classPartei = model.createClass(model.getNsPrefixURI(INamespace.BTD) + "Partei");
		classPartei.addProperty(RDF.type, classOrganization);
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

	}

	private void createObjectProperties() {

		// existing properties
		propHomepage = model.createObjectProperty(model.getNsPrefixURI(INamespace.FOAF) + "homepage");
		propMbox = model.createObjectProperty(model.getNsPrefixURI(INamespace.FOAF) + "mbox");
		propWahlkreis = model.createObjectProperty(model.getNsPrefixURI(INamespace.BTD) + "wahlkreis");
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

	protected void createPersonResources(List<Abgeordneter> personList) {

		createParteiResources();

		Resource abgeordneter;

		for (Abgeordneter mdb : personList) {

			abgeordneter = model.createResource(mdb.getURI(), classAbgeordneter);
			abgeordneter.addProperty(propFirstName, mdb.getForename());
			abgeordneter.addProperty(propGivenName, mdb.getLastname());
			abgeordneter.addProperty(propMbox, mdb.getEmail() != null ? mdb.getEmail() : "");
			abgeordneter.addProperty(propHomepage, mdb.getHomepage() != null ? mdb.getHomepage() : "");
			abgeordneter.addProperty(propNebeneinkuenfteAnzahl,
					model.createTypedLiteral(mdb.getAnzahlNebeneinkuenfte()));
			abgeordneter.addProperty(propNebeneinkuenfteMaximum, model.createTypedLiteral(mdb.getMaxZusatzeinkommen()));
			abgeordneter.addProperty(propNebeneinkuenfteMinimum, model.createTypedLiteral(mdb.getMinZusatzeinkommen()));
			abgeordneter.addProperty(propHatWahlkreis, getWahlkreisResource(mdb));
			abgeordneter.addProperty(propIsPartOf, getParteiRessource(mdb));

		}

	}

	private Resource getWahlkreisResource(Abgeordneter abgeordneter) {

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
						: "Wahlkreis unbekannt"));
		return wahlkreis;

	}

	private void createParteiResources() {

		Resource partei = null;

		partei = model.createResource(SPD_FRAKTION, classPartei);
		partei.addProperty(RDFS.label, SPD_LABEL);

		partei = model.createResource(CDU_CSU_FRAKTION, classPartei);
		partei.addProperty(RDFS.label, CDU_CSU_LABEL);

		partei = model.createResource(DIE_LINKE_FRAKTION, classPartei);
		partei.addProperty(RDFS.label, DIE_LINKE_LABEL);

		partei = model.createResource(FDP_FRAKTION, classPartei);
		partei.addProperty(RDFS.label, FDP_LABEL);

		partei = model.createResource(GRUENE_FRAKTION, classPartei);
		partei.addProperty(RDFS.label, GRUENE_LABEL);

	}

	/**
	 * TODO: fix getResoure part
	 */
	private Resource getParteiRessource(Abgeordneter abgeordneter) {

		Resource partei = null;
		String fraktion = abgeordneter.getFraktion().trim();

		if (fraktion.equals(SPD_LABEL))
			return model.getResource(SPD_FRAKTION);
		else if (fraktion.equals(CDU_CSU_LABEL))
			return model.getResource(CDU_CSU_FRAKTION);
		else if (fraktion.equals(DIE_LINKE_LABEL))
			return model.getResource(DIE_LINKE_FRAKTION);
		else if (fraktion.equals(FDP_LABEL))
			return model.getResource(FDP_FRAKTION);
		else if (fraktion.equals(GRUENE_LABEL))
			return model.getResource(GRUENE_FRAKTION);

		// emergency case
		partei = model.getResource(abgeordneter.getFraktion());

		if (partei != null)
			return partei;

		partei = model.createResource(abgeordneter.getWahlkreisUri(), classPartei);
		partei.addProperty(RDFS.label, model.createTypedLiteral(abgeordneter.getWahlkreisName()));
		return partei;

	}

	/**
	 * File export of the generated model.
	 * 
	 * 
	 * @param path
	 *            Path to the folder that should conatain the file
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
		model.createPersonResources(conv.getAbgeordnete());

		String path = System.getProperty("user.home") + "/Desktop/nebeneinkunft." + IRDFExport.TYPES.get(IRDFExport.N3);
		model.fileExport(path, IRDFExport.N3);
		System.out.println("done");

	}

}
