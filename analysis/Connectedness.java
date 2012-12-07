package uk.ac.open.kmi.watson.validation.analysis;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import uk.ac.open.kmi.watson.db.Entity;
import uk.ac.open.kmi.watson.db.EntityRelation;
import uk.ac.open.kmi.watson.db.SemanticContent;
import uk.ac.open.kmi.watson.db.impl.Factory;
import uk.ac.open.kmi.watson.validation.model.DocumentStatus;
import uk.ac.open.kmi.watson.validation.model.DocumentStatusHome;

public class Connectedness {

	public static void main(String[] args){
		generateConnectednessAnalysisReport();
		generateDereferencableAnalysisReport();
	}

	static Hashtable<String,Integer> ht = new Hashtable<String, Integer>();
	
	private static void generateConnectednessAnalysisReport() {
		List<DocumentStatus> dss = new DocumentStatusHome().listDocumentStatus();
		Factory f = new Factory();
		// int i = 0;
		for (DocumentStatus ds: dss){
			//i++;
			// if (i == 100) break;
			f.beginTransaction();
			SemanticContent sc = f.getDocument(ds.getDocumentId()).semanticContent();
			Iterator it = sc.listEntities();
		    Vector<String> v = new Vector<String>();	
			while (it.hasNext()){
				Entity e = (Entity) it.next();
				if (!v.contains(e.namespace())){
					v.add(e.namespace());
				}
			}
			it = sc.listEntityRelations();
			while (it.hasNext()){
				EntityRelation er = (EntityRelation)it.next();
				String sub = er.subject();
				String obj = er.object();
				if (obj.indexOf("#")!= -1) 
					obj = obj.substring(0,obj.lastIndexOf("#"));
				if (sub.indexOf("#")!= -1) 
					sub = sub.substring(0,sub.lastIndexOf("#"));
				if (!v.contains(obj)){
					v.add(obj);
				}
				if (!v.contains(sub)){
					v.add(sub);
				}
			}
			System.out.println(ds.getDocumentId()+" = "+v.size());
			for (String ns: v){
				if (!ht.containsKey(ns)){
					ht.put(ns, 1);
				}
				else ht.put(ns,ht.get(ns).intValue()+1);
			}
			f.commitTransaction();
		}
		System.out.println("***** NS pop ***** ");
		Enumeration<String> k = ht.keys();
		while(k.hasMoreElements()){
			String ns = k.nextElement();
			System.out.println(ns+" = "+ht.get(ns));
		}
	}

	private static void generateDereferencableAnalysisReport() {
		// TODO Auto-generated method stub
		
	}
	
	
}
