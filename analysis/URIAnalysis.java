package uk.ac.open.kmi.watson.validation.analysis;

import java.util.List;
import java.util.Vector;

import uk.ac.open.kmi.watson.db.SemanticContent;
import uk.ac.open.kmi.watson.db.impl.Factory;
import uk.ac.open.kmi.watson.validation.utils.V_CONSTANTS;

public class URIAnalysis {

	public static Vector<String> getSCWithURI(String URI){
		Vector<String> results = new Vector<String>();
		List<String> lds = Utils.getValidatedWithStatus(V_CONSTANTS.URI_POPULATOR);
		Factory factory = new Factory();
		factory.beginTransaction();
		for (String id : lds){
			// System.err.print(".");
			SemanticContent sc = factory.getDocument(id).semanticContent();
			if(URI.equals(sc.URI())) results.add(id);
		}
		factory.commitTransaction();
		return results;
	}
	
	
	public static void main(String[] args){
		Vector<String> scs = getSCWithURI(args[0]);
		for (String s : scs){
			SCReport.ontologyInfo(s);
		}
	}
	
	
}
