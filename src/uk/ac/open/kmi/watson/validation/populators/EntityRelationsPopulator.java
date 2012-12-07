/*
 * IndividualsRelationPopulator.java
 *
 * Created on 16 April 2007, 11:51
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package uk.ac.open.kmi.watson.validation.populators;

import java.util.Vector;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;

import uk.ac.open.kmi.watson.validation.DataManager;
import uk.ac.open.kmi.watson.validation.Populator;
import uk.ac.open.kmi.watson.validation.extractors.relations.EntityRelationDataStructure;
import uk.ac.open.kmi.watson.validation.extractors.relations.EntityRelationExtractor2;
import uk.ac.open.kmi.watson.validation.utils.LabelSplitter;
import uk.ac.open.kmi.watson.validation.utils.URN;
import uk.ac.open.kmi.watson.validation.utils.UriUtil;
import uk.ac.open.kmi.watson.validation.utils.V_CONSTANTS;

/**
 *
 * @author md99
 */
public class EntityRelationsPopulator extends Populator{
	
	/** Creates a new instance of IndividualsRelationPopulator */
    public EntityRelationsPopulator() {
         // set the code:
        this.code = V_CONSTANTS.ENTITY_RELATION_POPULATOR;
        // set the required status
        this.requiredStatus = V_CONSTANTS.CLASS_POPULATOR + V_CONSTANTS.PROPERTIES_POPULAOTR;
        // set the extractor
        this.extractor = new EntityRelationExtractor2();
    }
    
    public boolean testPrecondition() {
        return true;
    }
    
    protected void writeToDB(Object o) {
		long time_gd;
		long sum_gd = 0;
		StringBuffer d_localN = new StringBuffer("");
       	StringBuffer d_IndLocN = new StringBuffer("");
       	StringBuffer d_HIndLocN = new StringBuffer("");
       	StringBuffer d_HCIndLocN = new StringBuffer("");
		time_gd = System.currentTimeMillis();
    	Vector<EntityRelationDataStructure> relations = (Vector<EntityRelationDataStructure>)o;
        Document rel_doc = null;
        Document doc_doc = DataManager.getInstance().getDocumentIndex().getDocument(documentID, documentID);
		sum_gd += (System.currentTimeMillis()-time_gd);
		try {
			System.out.println("Found "+relations.size()+" relations");
			long time1 = System.currentTimeMillis();
			long sum_add = 0;
			long sum_urn = 0;
			long sum_get = 0;
			long sum_if = 0;
			long time_add;
			long time_if;
			long time_urn;
			long time_get;
			for(EntityRelationDataStructure rel : relations){
			time_gd = System.currentTimeMillis();
			String relID = "rel"+Math.random();
			rel_doc = DataManager.getInstance().getRelationIndex().getDocument(documentID, relID);
			sum_gd += (System.currentTimeMillis()-time_gd);
			time_add = System.currentTimeMillis();
			rel_doc.add(new Field("scid", documentID, Field.Store.YES, Field.Index.UN_TOKENIZED));
			rel_doc.add(new Field("subj", rel.sbjUri, Field.Store.YES, Field.Index.UN_TOKENIZED));
			rel_doc.add(new Field("pred", rel.pptUri, Field.Store.YES, Field.Index.UN_TOKENIZED));
			rel_doc.add(new Field("obj",  rel.objUri, Field.Store.YES, Field.Index.UN_TOKENIZED));
			sum_add += (System.currentTimeMillis()-time_add);
			// DataManager.getInstance().getRelationIndex().flushDocument(documentID, relID);
			// Populating entity index, 
			time_gd = System.currentTimeMillis();
			String entityID = documentID+rel.sbjUri;
			Document ent_doc = DataManager.getInstance().getEntityIndex().getDocument(documentID, entityID);			
			sum_gd += (System.currentTimeMillis()-time_gd);
			// individuals are the elements not yet typed, but being subjects of relations or literals.
			time_get = System.currentTimeMillis();
			String locN = ent_doc.get("locN");
			String ns = ent_doc.get("ns");
			sum_get += (System.currentTimeMillis()-time_get);
			if (locN == null && ns == null){
				time_urn = System.currentTimeMillis();
				locN = UriUtil.splitNamespace(rel.sbjUri)[1];
				String HlocN = new URN(locN).toString();
				String HClocN = new URN(new LabelSplitter().splitLabel(locN).toLowerCase()).toString();
				ns   = UriUtil.splitNamespace(rel.sbjUri)[0];
				sum_urn += (System.currentTimeMillis()-time_urn);
				time_add = System.currentTimeMillis();
				ent_doc.add(new Field("sc", documentID, Field.Store.YES, Field.Index.UN_TOKENIZED));
				ent_doc.add(new Field("locN", locN, Field.Store.YES, Field.Index.TOKENIZED));
	        	ent_doc.add(new Field("HlocN", HlocN, Field.Store.NO, Field.Index.UN_TOKENIZED));
			   	ent_doc.add(new Field("HClocN", HClocN, Field.Store.NO, Field.Index.UN_TOKENIZED));
				ent_doc.add(new Field("ns", ns, Field.Store.YES, Field.Index.UN_TOKENIZED));
				ent_doc.add(new Field("type", Integer.toString(V_CONSTANTS.INDIVIDUAL_ENTITY), Field.Store.YES, Field.Index.UN_TOKENIZED));
				sum_add += (System.currentTimeMillis()-time_add);
				// add to the document index
				d_localN.append(" "+locN);
	           	d_IndLocN.append(" "+locN);
	           	d_HIndLocN.append(" "+HlocN);
	           	d_HCIndLocN.append(" "+HClocN);
				/* String localN = doc_doc.get("localN");
	           	String IndLocN = doc_doc.get("IndLocN");
	           	String HIndLocN = doc_doc.get("HIndLocN");
	           	String HCIndLocN = doc_doc.get("HCIndLocN");
				sum_get += (System.currentTimeMillis()-time_get);
				time_if = System.currentTimeMillis();
				if (localN == null) localN = "";
	           	else doc_doc.removeField("localN");
	           	if (IndLocN == null) IndLocN = "";
	           	else doc_doc.removeField("IndLocN");
	        	if (HIndLocN == null) HIndLocN = "";
	           	else doc_doc.removeField("HIndLocN");
	        	if (HCIndLocN == null) HCIndLocN = "";
	           	else doc_doc.removeField("HCIndLocN");
	           	localN += " "+locN;
	           	IndLocN += " "+locN;
	           	HIndLocN += " "+HlocN;
	           	HCIndLocN += " "+HClocN;   	
				sum_if += (System.currentTimeMillis()-time_if);
	           	time_add = System.currentTimeMillis();
	           	doc_doc.add(new Field("localN", localN, Field.Store.NO, Field.Index.TOKENIZED));
	           	doc_doc.add(new Field("IndLocN", IndLocN, Field.Store.NO, Field.Index.TOKENIZED));
	           	doc_doc.add(new Field("HIndLocN", HIndLocN, Field.Store.NO, Field.Index.TOKENIZED));
	           	doc_doc.add(new Field("HCIndLocN", HCIndLocN, Field.Store.NO, Field.Index.TOKENIZED));
				sum_add += (System.currentTimeMillis()-time_add); */
			}
		}
			time_get = System.currentTimeMillis();
			String localN = doc_doc.get("localN");
           	String IndLocN = doc_doc.get("IndLocN");
           	String HIndLocN = doc_doc.get("HIndLocN");
           	String HCIndLocN = doc_doc.get("HCIndLocN");
			sum_get += (System.currentTimeMillis()-time_get);
			time_if = System.currentTimeMillis();
			if (localN == null) localN = "";
           	else doc_doc.removeField("localN");
           	if (IndLocN == null) IndLocN = "";
           	else doc_doc.removeField("IndLocN");
        	if (HIndLocN == null) HIndLocN = "";
           	else doc_doc.removeField("HIndLocN");
        	if (HCIndLocN == null) HCIndLocN = "";
           	else doc_doc.removeField("HCIndLocN");
           	localN += " "+d_localN;
           	IndLocN += " "+d_IndLocN;
           	HIndLocN += " "+d_HIndLocN;
           	HCIndLocN += " "+d_HCIndLocN;   	
			sum_if += (System.currentTimeMillis()-time_if);
           	time_add = System.currentTimeMillis();
           	doc_doc.add(new Field("localN", localN.trim(), Field.Store.NO, Field.Index.TOKENIZED));
           	doc_doc.add(new Field("IndLocN", IndLocN.trim(), Field.Store.NO, Field.Index.TOKENIZED));
           	doc_doc.add(new Field("HIndLocN", HIndLocN.trim(), Field.Store.NO, Field.Index.TOKENIZED));
           	doc_doc.add(new Field("HCIndLocN", HCIndLocN.trim(), Field.Store.NO, Field.Index.TOKENIZED));
			sum_add += (System.currentTimeMillis()-time_add); 
		System.out.println("Time to create: "+(System.currentTimeMillis()-time1));
		System.out.println("Total time to get docs: "+sum_gd);
		System.out.println("Total time to get fields: "+sum_get);
		System.out.println("Total time to test and remove: "+sum_if);
		System.out.println("Total time to create urns: "+sum_urn);
		System.out.println("Total time to add fields: "+sum_add);
		time1 = System.currentTimeMillis();
		DataManager.getInstance().getRelationIndex().flushAll(documentID);
		System.out.println("Time to store: "+(System.currentTimeMillis()-time1));
		// rWriter.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
