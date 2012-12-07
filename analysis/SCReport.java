package uk.ac.open.kmi.watson.validation.analysis;

import java.util.Iterator;
import java.util.List;

import uk.ac.open.kmi.watson.db.Document;
import uk.ac.open.kmi.watson.db.Entity;
import uk.ac.open.kmi.watson.db.Measures;
import uk.ac.open.kmi.watson.db.SemanticContent;
import uk.ac.open.kmi.watson.db.impl.Factory;
import uk.ac.open.kmi.watson.validation.model.DocumentStatus;
import uk.ac.open.kmi.watson.validation.model.DocumentStatusHome;
import uk.ac.open.kmi.watson.validation.utils.V_CONSTANTS;

public class SCReport {

	
	public static void report(String documentID){
		System.out.println("-----------------------------------------");
		System.out.println("Report for "+documentID);
		System.out.println("-----------------------------------------");
		System.out.println("**** Validation ***");
		generatWhatsValidated(documentID);
		System.out.println("**** Ontology infos ****");
		ontologyInfo(documentID);
	}
	
	public static void generatWhatsValidated(String documentID) {
		if (Utils.isValidated(documentID, V_CONSTANTS.CLASS_POPULATOR)){
			System.out.println("Classes extracted...");
		}
		if (Utils.isValidated(documentID, V_CONSTANTS.CLASS_RELATION_POPULATOR)){
			System.out.println("Class relations extracted...");
		}
		if (Utils.isValidated(documentID, V_CONSTANTS.DL_EXPRESSIVITY_POPULATOR)){
			System.out.println("DL Expressivity extracted...");
		}
		if (Utils.isValidated(documentID, V_CONSTANTS.INCONSISTENCY_POPULATOR)){
			System.out.println("Inconsistency extracted...");
		}
		if (Utils.isValidated(documentID, V_CONSTANTS.INDIVIDUAL_POPULATOR)){
			System.out.println("Individuals extracted...");
		}
		if (Utils.isValidated(documentID, V_CONSTANTS.LANGUAGE_POPULATOR)){
			System.out.println("Language extracted...");
		}
		if (Utils.isValidated(documentID, V_CONSTANTS.OWL_SPECIE_POPULAOTR)){
			System.out.println("OWL Specie extracted...");
		}
		if (Utils.isValidated(documentID, V_CONSTANTS.LITERAL_POPULATOR)){
			System.out.println("Literals extracted...");
		}
		if (Utils.isValidated(documentID, V_CONSTANTS.PROPERTIES_POPULAOTR)){
			System.out.println("Properties extracted...");
		}
		if (Utils.isValidated(documentID, V_CONSTANTS.PROPERTIES_RELATION_POPULATOR)){
			System.out.println("Property relations extracted...");
		}
		if (Utils.isValidated(documentID, V_CONSTANTS.URI_POPULATOR)){
			System.out.println("URI extracted...");
		}
		
	}
	
	
	public static void ontologyInfo(String documentID){
		Factory f = new Factory();
		f.beginTransaction();
		Document d = f.getDocument(documentID);
		SemanticContent sc = d.semanticContent();
		Measures m = d.measures();
		System.out.println("Size= "+d.size()+"B");
		System.out.println("URI= "+sc.URI());
		System.out.print("URLs= ");
		Iterator it = d.provenance();
		while (it.hasNext()){
			System.out.print((String)it.next());
			if (it.hasNext()) System.out.print(", ");
			else System.out.println();
		}
		System.out.print("Languages= ");
		Iterator it2 = sc.languages();
		while (it2.hasNext()){
			System.out.print((String)it2.next());
			if (it2.hasNext()) System.out.print(", ");
		}
		System.out.println();
		if (Utils.isValidated(documentID, V_CONSTANTS.OWL_SPECIE_POPULAOTR)) System.out.println("OWL Specy= " + m.owlPart());
		if (Utils.isValidated(documentID, V_CONSTANTS.DL_EXPRESSIVITY_POPULATOR)) System.out.println("DL Language= " + m.dlExpressivity());
		if (Utils.isValidated(documentID, V_CONSTANTS.INCONSISTENCY_POPULATOR)) System.out.println("Inconsistent= " + m.inconsistent());
		Iterator it3 = sc.listEntities();
		int total = 0;
		int cl = 0;
		int pr = 0;
		int in = 0;
		while(it3.hasNext()){
			total++;
			Entity en = (Entity)it3.next();
			if (en.type()==V_CONSTANTS.CLASS_ENTITY) cl++;
			if (en.type()==V_CONSTANTS.PROPERTY_ENTITY) pr++;
			if (en.type()==V_CONSTANTS.INDIVIDUAL_ENTITY) in++;
		}
		System.out.println("NB Entities= "+total+" (cl= "+cl+") (pr= "+pr+") (in= "+in+")");
	}


	public static String pickRamdomly(){
		DocumentStatusHome dsh = new DocumentStatusHome();
		List<DocumentStatus> dss = dsh.listDocumentStatus();
		int index = (int) ((double)dss.size() * Math.random());
		int i = 0;
		for (DocumentStatus ds : dss){
			if (i++==index) return ds.getDocumentId();
		}
		return null;
	}
	
	public static void main(String[] args){
		report(pickRamdomly());
	}
	
}
