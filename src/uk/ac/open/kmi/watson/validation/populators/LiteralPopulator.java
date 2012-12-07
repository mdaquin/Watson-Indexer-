/*
 * LiteralPopulator.java
 *
 * Created on 16 April 2007, 12:01
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
import uk.ac.open.kmi.watson.validation.extractors.literals.LiteralDataStructure;
import uk.ac.open.kmi.watson.validation.extractors.literals.LiteralExtractor;
import uk.ac.open.kmi.watson.validation.utils.LabelSplitter;
import uk.ac.open.kmi.watson.validation.utils.URN;
import uk.ac.open.kmi.watson.validation.utils.UriUtil;
import uk.ac.open.kmi.watson.validation.utils.V_CONSTANTS;

/**
 *
 * @author cb7224
 */
public class LiteralPopulator extends Populator{
    	
    /** Creates a new instance of LiteralPopulator */
    public LiteralPopulator() {
        // set the code:
        this.code = V_CONSTANTS.LITERAL_POPULATOR;
        // set the required status
        this.requiredStatus = V_CONSTANTS.CLASS_POPULATOR +  V_CONSTANTS.PROPERTIES_POPULAOTR;
        // set the extractor
        this.extractor = new LiteralExtractor();
    }
    
    public boolean testPrecondition() {
        return true;
    }
    
    protected void writeToDB(Object o) {
        Vector<LiteralDataStructure> literals = (Vector<LiteralDataStructure>)o;
        Document lit_doc = null;
        Document ent_doc = null;
        // note: the getDocument from Lucene interface doesn't seem to be working properly!
        Document doc_doc = DataManager.getInstance().getDocumentIndex().getDocument(documentID, documentID);;
        Document lastdd = null;
        
       	StringBuffer d_localN = new StringBuffer("");
       	StringBuffer d_IndLocN = new StringBuffer("");
       	StringBuffer d_HIndLocN = new StringBuffer("");
       	StringBuffer d_HCIndLocN = new StringBuffer("");
  
        StringBuffer d_IndLit = new StringBuffer("");
        StringBuffer d_IndL = new StringBuffer("");
        StringBuffer d_IndC = new StringBuffer("");  
        StringBuffer d_HCIndLit = new StringBuffer("");
        StringBuffer d_HCIndL = new StringBuffer("");
        StringBuffer d_HCIndC = new StringBuffer("");  

        StringBuffer d_PrpLit = new StringBuffer("");
        StringBuffer d_PrpL = new StringBuffer("");
        StringBuffer d_PrpC = new StringBuffer("");  
        StringBuffer d_HCPrpLit = new StringBuffer("");
        StringBuffer d_HCPrpL = new StringBuffer("");
        StringBuffer d_HCPrpC = new StringBuffer("");  

        StringBuffer d_ClsLit = new StringBuffer("");
        StringBuffer d_ClsL = new StringBuffer("");
        StringBuffer d_ClsC = new StringBuffer("");  
        StringBuffer d_HCClsLit = new StringBuffer("");
        StringBuffer d_HCClsL = new StringBuffer("");
        StringBuffer d_HCClsC = new StringBuffer("");  

        
        
        try {
        	System.out.println("Found "+literals.size()+" literals");
		for(LiteralDataStructure lit : literals){
			// Populating literal index
			String litID = "lit"+Math.random();
			lit_doc = DataManager.getInstance().getLiteralIndex().getDocument(documentID, litID);
			lit_doc.add(new Field("scid", documentID, Field.Store.YES, Field.Index.UN_TOKENIZED));
			lit_doc.add(new Field("subj", lit.sbjUri, Field.Store.YES, Field.Index.UN_TOKENIZED));
			lit_doc.add(new Field("pred", lit.pptUri, Field.Store.YES, Field.Index.UN_TOKENIZED));
			lit_doc.add(new Field("obj",  lit.value, Field.Store.YES, Field.Index.TOKENIZED));
			lit_doc.add(new Field("lang",  lit.language, Field.Store.YES, Field.Index.UN_TOKENIZED));
			// DataManager.getInstance().getLiteralIndex().flushDocument(documentID, litID);
		
			// Populating entity index, 
			String entityID = documentID+lit.sbjUri;
			ent_doc = DataManager.getInstance().getEntityIndex().getDocument(documentID, entityID);			
			// individuals are the elements not yet typed, but being subjects of relations or literals.
			String locN = ent_doc.get("locN");
			String ns = ent_doc.get("ns");
			if (locN == null && ns == null){
				locN = UriUtil.splitNamespace(lit.sbjUri)[1];
				String HlocN = new URN(locN).toString();
	        	String HClocN = new URN(new LabelSplitter().splitLabel(locN).toLowerCase()).toString();
				ns   = UriUtil.splitNamespace(lit.sbjUri)[0];
				ent_doc.add(new Field("sc", documentID, Field.Store.YES, Field.Index.UN_TOKENIZED));
				ent_doc.add(new Field("locN", locN, Field.Store.YES, Field.Index.TOKENIZED));
				ent_doc.add(new Field("HlocN", HlocN, Field.Store.NO, Field.Index.UN_TOKENIZED));
			   	ent_doc.add(new Field("HClocN", HClocN, Field.Store.NO, Field.Index.UN_TOKENIZED));
				ent_doc.add(new Field("ns", ns, Field.Store.YES, Field.Index.UN_TOKENIZED));
				ent_doc.add(new Field("type", Integer.toString(V_CONSTANTS.INDIVIDUAL_ENTITY), Field.Store.YES, Field.Index.UN_TOKENIZED));
				// add to the document index
				d_localN.append(" "+locN);
				d_IndLocN.append(" "+locN);
				d_HCIndLocN.append(" "+HClocN);
				d_HIndLocN.append(" "+HlocN);
			}
			String HClit = new URN(new LabelSplitter().splitLabel(lit.value).toLowerCase()).toString();

			if (lit.pptUri.equals("http://www.w3.org/2000/01/rdf-schema#label") || lit.pptUri.equals("http://www.w3.org/2000/01/rdf-schema#comment")){
				ent_doc.add(new Field(lit.pptUri, lit.value, Field.Store.NO, Field.Index.TOKENIZED));
				ent_doc.add(new Field("HC"+lit.pptUri, HClit, Field.Store.NO, Field.Index.TOKENIZED));
			}
			// ....
			String exist_lit = ent_doc.get("lit");
			String exist_HClit = ent_doc.get("HClit");
			if (exist_lit != null) 
				ent_doc.removeField("lit");
			else exist_lit = "";
			if (exist_HClit != null) 
				ent_doc.removeField("HClit");
			else exist_HClit = "";
			exist_lit += " "+lit.value;
			ent_doc.add(new Field("lit", exist_lit, Field.Store.NO, Field.Index.TOKENIZED));
			exist_HClit += " "+HClit;
			ent_doc.add(new Field("HClit", exist_HClit, Field.Store.NO, Field.Index.TOKENIZED));

			// Populating document index, 
			doc_doc = DataManager.getInstance().getDocumentIndex().getDocument(documentID, documentID);
			int type = Integer.parseInt(ent_doc.get("type"));
			switch(type){
			case V_CONSTANTS.CLASS_ENTITY:
				d_ClsLit.append(" "+lit.value);
				d_HCClsLit.append(" "+HClit);
				if (lit.pptUri.equals("http://www.w3.org/2000/01/rdf-schema#label")){
					d_ClsL.append(" "+lit.value);
					d_HCClsL.append(" "+HClit);
				}
				if (lit.pptUri.equals("http://www.w3.org/2000/01/rdf-schema#comment")){
					d_ClsC.append(" "+lit.value);
					d_HCClsC.append(" "+HClit);
				}
				break;
			case V_CONSTANTS.PROPERTY_ENTITY:
				d_PrpLit.append(" "+lit.value);
				d_HCPrpLit.append(" "+HClit);
				if (lit.pptUri.equals("http://www.w3.org/2000/01/rdf-schema#label")){
					d_PrpL.append(" "+lit.value);
					d_HCPrpL.append(" "+HClit);
				}
				if (lit.pptUri.equals("http://www.w3.org/2000/01/rdf-schema#comment")){
					d_PrpC.append(" "+lit.value);
					d_HCPrpC.append(" "+HClit);
				}
				break;
			case V_CONSTANTS.INDIVIDUAL_ENTITY:
				d_IndLit.append(" "+lit.value);
				d_HCIndLit.append(" "+HClit);
				if (lit.pptUri.equals("http://www.w3.org/2000/01/rdf-schema#label")){
					d_IndL.append(" "+lit.value);
					d_HCIndL.append(" "+HClit);	
				}
				if (lit.pptUri.equals("http://www.w3.org/2000/01/rdf-schema#comment")){
					d_IndC.append(" "+lit.value);
					d_HCIndC.append(" "+HClit);
				}
				break;
			}
		}
		System.out.println("Saving...");
		DataManager.getInstance().getLiteralIndex().flushAll(documentID);
		System.out.println("Completing document...");	
		String localN = doc_doc.get("localN");
       	String IndLocN = doc_doc.get("IndLocN");
    	String HIndLocN = doc_doc.get("HIndLocN");
    	String HCIndLocN = doc_doc.get("HCIndLocN");
       	if (localN == null) localN = "";
       	else doc_doc.removeField("localN");
       	if (IndLocN == null) IndLocN = "";
       	else doc_doc.removeField("IndLocN");
       	if (HIndLocN == null) HIndLocN = "";
       	else doc_doc.removeField("HIndLocN");
       	if (HCIndLocN == null) HCIndLocN = "";
       	else doc_doc.removeField("HCIndLocN");
       	localN += " "+d_localN;
       	IndLocN += " "+d_HCIndLocN;
     	HIndLocN += " "+d_HIndLocN;
     	HCIndLocN += " "+d_HCIndLocN;
    	doc_doc.add(new Field("localN", localN.trim(), Field.Store.NO, Field.Index.TOKENIZED));
       	doc_doc.add(new Field("IndLocN", IndLocN.trim(), Field.Store.NO, Field.Index.TOKENIZED));
       	doc_doc.add(new Field("HIndLocN", HIndLocN.trim(), Field.Store.NO, Field.Index.TOKENIZED));
       	doc_doc.add(new Field("HCIndLocN", HCIndLocN.trim(), Field.Store.NO, Field.Index.TOKENIZED));
		
		String IndLit = doc_doc.get("IndLit");
		if (IndLit!=null) doc_doc.removeField("IndLit");
		else IndLit = "";
		IndLit += " "+d_IndLit;
		doc_doc.add(new Field("IndLit", IndLit.trim(), Field.Store.NO, Field.Index.TOKENIZED));
		String HCIndLit = doc_doc.get("HCIndLit");
		if (HCIndLit!=null) doc_doc.removeField("HCIndLit");
		else HCIndLit = "";
		HCIndLit += " "+d_HCIndLit;
		doc_doc.add(new Field("HCIndLit", HCIndLit.trim(), Field.Store.NO, Field.Index.TOKENIZED));
		String IndL = doc_doc.get("IndL");
		if (IndL!=null) doc_doc.removeField("IndL");
		else IndL = "";
		IndL += " "+d_IndL;
		doc_doc.add(new Field("IndL", IndL.trim(), Field.Store.NO, Field.Index.TOKENIZED));	
		String HCIndL = doc_doc.get("HCIndL");
		if (HCIndL!=null) doc_doc.removeField("HCIndL");
		else HCIndL = "";
		HCIndL += " "+d_HCIndL;
		doc_doc.add(new Field("HCIndL", HCIndL.trim(), Field.Store.NO, Field.Index.TOKENIZED));
		String IndC = doc_doc.get("IndC");
		if (IndC!=null) doc_doc.removeField("IndC");
		else IndC = "";
		IndC += " "+d_IndC;
		doc_doc.add(new Field("IndC", IndC.trim(), Field.Store.NO, Field.Index.TOKENIZED));	
		String HCIndC = doc_doc.get("HCIndC");
		if (HCIndC!=null) doc_doc.removeField("HCIndC");
		else HCIndC = "";
		HCIndC += " "+d_HCIndC;
		doc_doc.add(new Field("HCIndC", HCIndC.trim(), Field.Store.NO, Field.Index.TOKENIZED));	
		
		String PrpLit = doc_doc.get("PrpLit");
		if (PrpLit!=null) doc_doc.removeField("PrpLit");
		else PrpLit = "";
		PrpLit += " "+d_PrpLit;
		doc_doc.add(new Field("PrpLit", PrpLit.trim(), Field.Store.NO, Field.Index.TOKENIZED));
		String HCPrpLit = doc_doc.get("HCPrpLit");
		if (HCPrpLit!=null) doc_doc.removeField("HCPrpLit");
		else HCPrpLit = "";
		HCPrpLit += " "+d_HCPrpLit;
		doc_doc.add(new Field("HCPrpLit", HCPrpLit.trim(), Field.Store.NO, Field.Index.TOKENIZED));
		String PrpL = doc_doc.get("PrpL");
		if (PrpL!=null) doc_doc.removeField("PrpL");
		else PrpL = "";
		PrpL += " "+d_PrpL;
		doc_doc.add(new Field("PrpL", PrpL.trim(), Field.Store.NO, Field.Index.TOKENIZED));
		String HCPrpL = doc_doc.get("HCPrpL");
		if (HCPrpL!=null) doc_doc.removeField("HCPrpL");
		else HCPrpL = "";
		HCPrpL += " "+d_HCPrpL;
		doc_doc.add(new Field("HCPrpL", HCPrpL.trim(), Field.Store.NO, Field.Index.TOKENIZED));
		String PrpC = doc_doc.get("PrpC");
		if (PrpC!=null) doc_doc.removeField("PrpC");
		else PrpC = "";
		PrpC += " "+d_PrpC;
		doc_doc.add(new Field("PrpC", PrpC.trim(), Field.Store.NO, Field.Index.TOKENIZED));
		String HCPrpC = doc_doc.get("HCPrpC");
		if (HCPrpC!=null) doc_doc.removeField("HCPrpC");
		else HCPrpC = "";
		HCPrpC += " "+d_HCPrpC;
		doc_doc.add(new Field("HCPrpC", HCPrpC.trim(), Field.Store.NO, Field.Index.TOKENIZED));
		
		
		String ClsLit = doc_doc.get("ClsLit");
		if (ClsLit!=null) doc_doc.removeField("ClsLit");
		else ClsLit = "";
		ClsLit += " "+d_ClsLit;
		doc_doc.add(new Field("ClsLit", ClsLit.trim(), Field.Store.NO, Field.Index.TOKENIZED));
		String HCClsLit = doc_doc.get("HCClsLit");
		if (HCClsLit!=null) doc_doc.removeField("HCClsLit");
		else HCClsLit = "";
		HCClsLit += " "+d_HCClsLit;
		doc_doc.add(new Field("HCClsLit", HCClsLit.trim(), Field.Store.NO, Field.Index.TOKENIZED));
		String ClsL = doc_doc.get("ClsL");
		if (ClsL!=null) doc_doc.removeField("ClsL");
		else ClsL = "";
		ClsL += " "+d_ClsL;
		doc_doc.add(new Field("ClsL", ClsL.trim(), Field.Store.NO, Field.Index.TOKENIZED));
		String HCClsL = doc_doc.get("HCClsL");
		if (HCClsL!=null) doc_doc.removeField("HCClsL");
		else HCClsL = "";
		HCClsL += " "+d_HCClsL;
		doc_doc.add(new Field("HCClsL", HCClsL.trim(), Field.Store.NO, Field.Index.TOKENIZED));
		String ClsC = doc_doc.get("ClsC");
		if (ClsC!=null) doc_doc.removeField("ClsC");
		else ClsC = "";
		ClsC += " "+d_ClsC;
		doc_doc.add(new Field("ClsC", ClsC.trim(), Field.Store.NO, Field.Index.TOKENIZED));
		String HCClsC = doc_doc.get("HCClsC");
		if (HCClsC!=null) doc_doc.removeField("HCClsC");
		else HCClsC = "";
		HCClsC += " "+d_HCClsC;
		doc_doc.add(new Field("HCClsC", HCClsC.trim(), Field.Store.NO, Field.Index.TOKENIZED));
        }catch(Exception e){
			e.printStackTrace();
		}
    }
}
    
//1)The literal values are extracted as a bunch without looking at the resource subject of the statement;   
//therefore to populate only the literals about the classes, properties and individuals in the document a 
//check on the subject resource needs to be done