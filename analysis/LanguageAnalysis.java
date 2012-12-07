package uk.ac.open.kmi.watson.validation.analysis;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;
import java.util.Vector;

import uk.ac.open.kmi.watson.db.Measures;
import uk.ac.open.kmi.watson.db.SemanticContent;
import uk.ac.open.kmi.watson.db.impl.Factory;
import uk.ac.open.kmi.watson.validation.utils.V_CONSTANTS;

public class LanguageAnalysis {

	
	
	public static void main (String[] args){
		generateLanguageReport();
		// generateOWLSpecyReport();
		generateDLExpReport();
		
	}

	private static Hashtable<String,Integer> exp_total = new Hashtable<String,Integer>();
	private static Hashtable<String,Integer> exp_owl = new Hashtable<String,Integer>();
	private static Hashtable<String,Integer> exp_daml = new Hashtable<String,Integer>();
	private static Hashtable<String,Integer> exp_full = new Hashtable<String,Integer>();
	
	private static void generateDLExpReport() {
		// Basic list + list of 1 (popularity of particular constructs) + list of 2 (constructs together)
		// + cross with species
		// + cross with languages
		// + cross with size ??
		Vector<String> lds = Utils.getValidatedWithStatus(V_CONSTANTS.DL_EXPRESSIVITY_POPULATOR);
		Factory f = new Factory();
		f.beginTransaction();
		for (String s : lds){
			SemanticContent sc = f.getDocument(s).semanticContent();
			Measures m = f.getDocument(s).measures();
			addToExpTable(m.dlExpressivity(), exp_total);
			if (Utils.isValidated(s, V_CONSTANTS.OWL_SPECIE_POPULAOTR) &&
					m.owlPart() == V_CONSTANTS.OWL_FULL)
				addToExpTable(m.dlExpressivity(), exp_full);
			Iterator it  = sc.languages();
			while(it.hasNext()){
				Object o = it.next();
				if ("OWL".equals(o)){
					addToExpTable(m.dlExpressivity(), exp_owl);
				}
				if ("DAML+OIL".equals(o)){
					addToExpTable(m.dlExpressivity(), exp_daml);
				}
			}
		}
		f.commitTransaction();
		System.out.println("**** EXP TOTAL ****");
		printExpTable(exp_total);
		System.out.println("**** EXP OWL ****");
		printExpTable(exp_owl);
		System.out.println("**** EXP DAML+OIL ****");
		printExpTable(exp_daml);
		System.out.println("**** EXP OWL FULL ****");
		printExpTable(exp_full);	
	}
	
	private static void printExpTable(Hashtable<String, Integer> table) {
		Set<String> keys = table.keySet();
		for (String s : keys){
			System.out.println(s+" = "+table.get(s).toString());
		}
	}




	private static void addToExpTable(String s, Hashtable<String, Integer> table) {
		if (table.containsKey(s)) {
			table.put(s, new Integer(table.get(s).intValue()+1));
		}
		else table.put(s, new Integer(1));
	}

	private static Hashtable<String,Integer> languages = new Hashtable<String,Integer>();
	
	private static void generateLanguageReport() {
		Vector<String> lds = Utils.getValidatedWithStatus(V_CONSTANTS.LANGUAGE_POPULATOR);
		Factory f = new Factory();
		f.beginTransaction();
		for (String s : lds){
			SemanticContent sc = f.getDocument(s).semanticContent();
			Iterator it  = sc.languages();
			String ls = "";
			while(it.hasNext()){
				ls += " "+(String)it.next();
			}
			if (ls.equals("")) ls = "NO";
			if (languages.containsKey(ls)) {
				languages.put(ls, new Integer(languages.get(ls).intValue()+1));
			}
			else languages.put(ls, new Integer(1));
			System.err.println(s+" = "+ls);
		}
		Set<String> keys = languages.keySet();
		for (String s : keys){
			System.out.println(s+" = "+languages.get(s).toString());
		}
		f.commitTransaction();
	}
	
}

