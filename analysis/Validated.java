package uk.ac.open.kmi.watson.validation.analysis;

import uk.ac.open.kmi.watson.validation.utils.V_CONSTANTS;
public class Validated {

	
	public static void main (String[] args){
		System.out.println("Number to be Validated: "+Utils.nbValidatedWithStatus(0));
		System.out.println("Classes Extracted: "+Utils.nbValidatedWithStatus(V_CONSTANTS.CLASS_POPULATOR));
		System.out.println("Class Relations Extracted: "+Utils.nbValidatedWithStatus(V_CONSTANTS.CLASS_RELATION_POPULATOR));
		System.out.println("DL Exp Extracted: "+Utils.nbValidatedWithStatus(V_CONSTANTS.DL_EXPRESSIVITY_POPULATOR));
		System.out.println("Inconsistency Extracted: "+Utils.nbValidatedWithStatus(V_CONSTANTS.INCONSISTENCY_POPULATOR));
		System.out.println("Individuals Extracted: "+Utils.nbValidatedWithStatus(V_CONSTANTS.INDIVIDUAL_POPULATOR));
		System.out.println("Languages Extracted: "+Utils.nbValidatedWithStatus(V_CONSTANTS.LANGUAGE_POPULATOR));
		System.out.println("OWL Species Extracted: "+Utils.nbValidatedWithStatus(V_CONSTANTS.OWL_SPECIE_POPULAOTR));
		System.out.println("Literals Extracted: "+Utils.nbValidatedWithStatus(V_CONSTANTS.LITERAL_POPULATOR));
		System.out.println("Properties Extracted: "+Utils.nbValidatedWithStatus(V_CONSTANTS.PROPERTIES_POPULAOTR));
		System.out.println("Property relations Extracted: "+Utils.nbValidatedWithStatus(V_CONSTANTS.PROPERTIES_RELATION_POPULATOR));
		System.out.println("URI Extracted: "+Utils.nbValidatedWithStatus(V_CONSTANTS.URI_POPULATOR));
		// ...
		System.out.println("----------");
		System.out.println("All Entitites: "+Utils.nbValidatedWithStatus(V_CONSTANTS.CLASS_POPULATOR+V_CONSTANTS.PROPERTIES_POPULAOTR+V_CONSTANTS.INDIVIDUAL_POPULATOR));
		System.out.println("All Entitites and Literals: "+Utils.nbValidatedWithStatus(V_CONSTANTS.CLASS_POPULATOR+V_CONSTANTS.PROPERTIES_POPULAOTR+V_CONSTANTS.INDIVIDUAL_POPULATOR+V_CONSTANTS.LITERAL_POPULATOR));
		System.out.println("All Relations: "+Utils.nbValidatedWithStatus(V_CONSTANTS.PROPERTIES_RELATION_POPULATOR+V_CONSTANTS.CLASS_RELATION_POPULATOR+V_CONSTANTS.INDIVIDUAL_RELATION_POPULATOR));
		System.out.println("All Entitites and Relations: "+Utils.nbValidatedWithStatus(V_CONSTANTS.CLASS_POPULATOR+V_CONSTANTS.PROPERTIES_POPULAOTR+V_CONSTANTS.INDIVIDUAL_POPULATOR+V_CONSTANTS.PROPERTIES_RELATION_POPULATOR+V_CONSTANTS.CLASS_RELATION_POPULATOR+V_CONSTANTS.INDIVIDUAL_RELATION_POPULATOR));
		System.out.println("All Entitites and Relations and Literals: "+Utils.nbValidatedWithStatus(V_CONSTANTS.CLASS_POPULATOR+V_CONSTANTS.PROPERTIES_POPULAOTR+V_CONSTANTS.INDIVIDUAL_POPULATOR+V_CONSTANTS.PROPERTIES_RELATION_POPULATOR+V_CONSTANTS.CLASS_RELATION_POPULATOR+V_CONSTANTS.INDIVIDUAL_RELATION_POPULATOR+V_CONSTANTS.LITERAL_POPULATOR));			
	}
	
}
