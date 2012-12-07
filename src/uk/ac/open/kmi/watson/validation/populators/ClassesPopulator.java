package uk.ac.open.kmi.watson.validation.populators;

import java.util.Vector;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;

import uk.ac.open.kmi.watson.validation.DataManager;
import uk.ac.open.kmi.watson.validation.Populator;
import uk.ac.open.kmi.watson.validation.extractors.entities.ClassesExtractor;
import uk.ac.open.kmi.watson.validation.utils.LabelSplitter;
import uk.ac.open.kmi.watson.validation.utils.URN;
import uk.ac.open.kmi.watson.validation.utils.UriUtil;
import uk.ac.open.kmi.watson.validation.utils.V_CONSTANTS;

/**
 * Watson validation populator: extract an store the classes in an ontology.
 * @author mda99
 * @see ClassesExtractor
 */
public class ClassesPopulator extends Populator{
    
    /**
     * set the code (2^4), the required status (nothing) and the extractor
     */
    public ClassesPopulator(){
        // set the code:
        this.code = V_CONSTANTS.CLASS_POPULATOR;
        // set the required status
        this.requiredStatus = 0;
        // set the extractor
        this.extractor = new ClassesExtractor();
    }
    
    /**
     * Test the pre-condition: nothing, return true;
     */
    public boolean testPrecondition(){
        return true;
    }
    
    /**
     * write to the database in the corresponding attribute of Measures.
     */
    protected void writeToDB(Object o){
        Document ent_doc = null;
        Document doc_doc = DataManager.getInstance().getDocumentIndex().getDocument(documentID, documentID);
        Vector<String> classes = (Vector<String>)o;
        for(String uri : classes){
        	String locN = UriUtil.splitNamespace(uri)[1];
        	String HlocN = new URN(locN).toString();
        	String HClocN = new URN(new LabelSplitter().splitLabel(locN).toLowerCase()).toString();
        	String ns = UriUtil.splitNamespace(uri)[0];
        	String entityID = documentID+uri;
        	ent_doc = DataManager.getInstance().getEntityIndex().getDocument(documentID, entityID);
        	ent_doc.add(new Field("sc", documentID, Field.Store.YES, Field.Index.UN_TOKENIZED));
        	ent_doc.add(new Field("locN", locN, Field.Store.YES, Field.Index.TOKENIZED));
        	ent_doc.add(new Field("HlocN", HlocN, Field.Store.NO, Field.Index.UN_TOKENIZED));
           	ent_doc.add(new Field("HClocN", HClocN, Field.Store.NO, Field.Index.UN_TOKENIZED));
        	ent_doc.add(new Field("ns", ns, Field.Store.YES, Field.Index.UN_TOKENIZED));
           	ent_doc.add(new Field("type", Integer.toString(V_CONSTANTS.CLASS_ENTITY), Field.Store.YES, Field.Index.UN_TOKENIZED));       	
           	// Populate document index...
           	String localN = doc_doc.get("localN");
           	String ClslocN = doc_doc.get("ClslocN");
           	String HClslocN = doc_doc.get("HClslocN");
           	String HCClslocN = doc_doc.get("HCClslocN");
           	if (localN == null) localN = "";
           	else doc_doc.removeField("localN");
           	if (ClslocN == null) ClslocN = "";
           	else doc_doc.removeField("ClslocN");
           	if (HClslocN == null) HClslocN = "";
           	else doc_doc.removeField("HClslocN");
           	if (HCClslocN == null) HCClslocN = "";
           	else doc_doc.removeField("HCClslocN");
           	localN += " "+locN;
           	ClslocN += " "+locN;
           	HClslocN += " "+HlocN;
           	HCClslocN += " "+HClocN;
        	doc_doc.add(new Field("localN", localN, Field.Store.NO, Field.Index.TOKENIZED));
           	doc_doc.add(new Field("ClslocN", ClslocN, Field.Store.NO, Field.Index.TOKENIZED));       	
          	doc_doc.add(new Field("HClslocN", HClslocN, Field.Store.NO, Field.Index.TOKENIZED));       	
          	doc_doc.add(new Field("HCClslocN", HCClslocN, Field.Store.NO, Field.Index.TOKENIZED));       	
        }
        
 /*   	Factory factory = new Factory();
        factory.beginTransaction();
        //Controller c = new Controller();
        uk.ac.open.kmi.watson.db.SemanticContent sc = factory.getDocument(documentID).semanticContent();
        //System.out.println(">>>>>> " + sc.document().ID());
        // (SemanticContent) c.findById(documentID, SemanticContent.class);
        Vector<String> classes = (Vector<String>)o;
        for(String uri : classes){
            sc.addEntity(uri);
            uk.ac.open.kmi.watson.db.Entity e = sc.getEntity(uri);
            //System.out.println("E >>>>> " + e.ID());
            e.setType(V_CONSTANTS.CLASS_ENTITY);
            // System.out.println("ADDED class"+uri);
        }
        
        factory.commitTransaction();
        */
        //check
                /*
                factory = new Factory();
                factory.beginTransaction();
                sc = factory.getDocument(documentID).semanticContent();
                 
                classes = (Vector<String>)o;
                for(String uri : classes){
                 
                        uk.ac.open.kmi.watson.db.Entity e = sc.getEntity(uri);
                        System.out.println("E ????? " + e.ID());
                 
                }
                 
                factory.commitTransaction();
                 */
    }
    
    public static void main (String[] args){
    	String locN = "VOYAGE";
    	String splitted = new LabelSplitter().splitLabel(locN).toLowerCase();
    	String HlocN = new URN(locN).toString();
    	String HClocN = new URN(new LabelSplitter().splitLabel(locN).toLowerCase()).toString();
    	System.out.println(locN);
    	System.out.println(splitted);
    	System.out.println(HlocN);
    	System.out.println(HClocN);
    	
    }
    
}
