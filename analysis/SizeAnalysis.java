package uk.ac.open.kmi.watson.validation.analysis;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;
import java.util.List;

import uk.ac.open.kmi.watson.db.Entity;
import uk.ac.open.kmi.watson.db.impl.EntityContent;
import uk.ac.open.kmi.watson.db.impl.Factory;
import uk.ac.open.kmi.watson.validation.model.DocumentStatus;
import uk.ac.open.kmi.watson.validation.model.DocumentStatusHome;

import uk.ac.open.kmi.watson.validation.utils.V_CONSTANTS;

public class SizeAnalysis {
	
	private class pair {
		String docID; long size;
	}

	public  Vector<pair> getFileSizeList(){
		Vector<pair> results = new Vector<pair>();
		DocumentStatusHome dsh = new DocumentStatusHome();
		List<DocumentStatus> lds = dsh.listDocumentStatus();
		for (DocumentStatus ds : lds){
			Factory factory = new Factory();
			factory.beginTransaction();
			long size = factory.getDocument(ds.getDocumentId()).size();
			pair p = new pair();
			p.docID=ds.getDocumentId(); p.size=size;
			results.add(p);
			System.out.println(ds.getDocumentId()+"="+size);
			factory.commitTransaction();
		}
		return results;
	}
	
	public Vector<pair> getEntitySizeList(){
		Vector<pair> results = new Vector<pair>();
		Vector<String> lds = Utils.getValidatedWithStatus(V_CONSTANTS.CLASS_POPULATOR+V_CONSTANTS.PROPERTIES_POPULAOTR+V_CONSTANTS.INDIVIDUAL_POPULATOR);
		for (String did : lds){
			Factory factory = new Factory();
			factory.beginTransaction();
			Iterator it = factory.getDocument(did).semanticContent().listEntities();
			long size = 0;
			while(it.hasNext()) {it.next(); size++;}
			pair p = new pair();
			p.docID =did; p.size=size;
			results.add(p);
			System.out.println(did+"="+size);
			factory.commitTransaction();
		}
		return results;
	}
	
	public Vector<pair> getClassSizeList(){
		Vector<pair> results = new Vector<pair>();
		Vector<String> lds = Utils.getValidatedWithStatus(V_CONSTANTS.CLASS_POPULATOR);
		for (String did : lds){
			Factory factory = new Factory();
			factory.beginTransaction();
			Iterator it = factory.getDocument(did).semanticContent().listEntities();
			long size = 0;
			while(it.hasNext()) {
				Entity e = (Entity)it.next(); 
				if (e.type()==V_CONSTANTS.CLASS_ENTITY) size++;
			}
			pair p = new pair();
			p.docID =did; p.size=size;
			results.add(p);
			System.out.println(did+"="+size);
			factory.commitTransaction();
		}
		return results;
	}
	
	public Vector<pair> getIndiSizeList(){
		Vector<pair> results = new Vector<pair>();
		Vector<String> lds = Utils.getValidatedWithStatus(V_CONSTANTS.INDIVIDUAL_POPULATOR);
		for (String did : lds){
			Factory factory = new Factory();
			factory.beginTransaction();
			Iterator it = factory.getDocument(did).semanticContent().listEntities();
			long size = 0;
			while(it.hasNext()) {
				Entity e = (Entity)it.next(); 
				if (e.type()==V_CONSTANTS.INDIVIDUAL_ENTITY) size++;
			}
			pair p = new pair();
			p.docID =did; p.size=size;
			results.add(p);
			System.out.println(did+"="+size);
			factory.commitTransaction();
		}
		return results;
	}
	
	public static void main(String[] args) {
		SizeAnalysis app = new SizeAnalysis();
		writeToFile(app.getFileSizeList(), "FileSizes.txt");
		writeToFile(app.getEntitySizeList(), "EntityNumbers.txt");
		writeToFile(app.getClassSizeList(), "ClassNumbers.txt");
		writeToFile(app.getIndiSizeList(), "IndiNumbers.txt");
	}

	private static void writeToFile(Vector<pair> v, String filename) {
		 try {
		        BufferedWriter out = new BufferedWriter(new FileWriter("analysis/"+filename));
		        for (pair l : v){
					// out.write(l.docID+","+l.size+"\n");
		        	out.write(l.size+"\n");
				}
		        out.close();
		    } catch (Exception e) {
		    	System.err.println(e);
		    }
		
	}

}
