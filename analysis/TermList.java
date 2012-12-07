package uk.ac.open.kmi.watson.validation.analysis;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.Iterator;
import java.util.Vector;

import uk.ac.open.kmi.watson.db.Entity;
import uk.ac.open.kmi.watson.db.LiteralStatement;
import uk.ac.open.kmi.watson.db.SemanticContent;
import uk.ac.open.kmi.watson.db.impl.EntityContent;
import uk.ac.open.kmi.watson.db.impl.Factory;
import uk.ac.open.kmi.watson.validation.utils.V_CONSTANTS;

public class TermList {

	private  Vector<String> getTermList(String documentID){
		Vector<String> results = new Vector<String>();
		Factory factory = new Factory();
		factory.beginTransaction();
		SemanticContent sc = factory.getDocument(documentID).semanticContent();
		Iterator it= sc.listEntities();
		while(it.hasNext()){
			EntityContent e = (EntityContent)it.next();
			String ln = e.localName();
			results.add(ln);
			Iterator it2 = e.literalStatements();
			while (it2.hasNext()){
				try{
				LiteralStatement l = (LiteralStatement)it2.next();
				if (l.predicate().endsWith("label")) 
					results.add(l.literal());
				} catch(Exception ee){System.err.println(ee); ee.printStackTrace(); break;}
			}
		}
		factory.commitTransaction();
		return results;
	}
	
	private class pair{
		String docID; Vector<String> terms;
	}
	
	public  Vector<pair> getTermList(){
		Vector<pair> results = new Vector<pair>();
		Vector<String> lds = Utils.getValidatedWithStatus(V_CONSTANTS.CLASS_POPULATOR+V_CONSTANTS.PROPERTIES_POPULAOTR+V_CONSTANTS.INDIVIDUAL_POPULATOR+V_CONSTANTS.LITERAL_POPULATOR);
		for (String did : lds){
			System.out.println(did);
			Vector<String> tl = getTermList(did);
			pair p = new pair();
			p.docID = did; p.terms=tl;
			writeToFiles(p);
		}
		return results;
	}
	
	
	public static void main(String[] args) {
		TermList app = new TermList();
	    app.getTermList();
	}


	private static void writeToFiles(pair p) {
		try{        
		BufferedWriter out = new BufferedWriter(new FileWriter("analysis/termLists/"+p.docID));
		        for (String s : p.terms){
					out.write(s+"\n");
				}
		        out.close();
		    } catch (Exception e) {
		    	System.err.println(e);
		    }
		
	}

}
