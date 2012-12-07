package uk.ac.open.kmi.watson.validation.populators;

import java.util.Vector;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;

import uk.ac.open.kmi.watson.validation.DataManager;
import uk.ac.open.kmi.watson.validation.Populator;
import uk.ac.open.kmi.watson.validation.extractors.entities.PropertiesExtractor;
import uk.ac.open.kmi.watson.validation.utils.LabelSplitter;
import uk.ac.open.kmi.watson.validation.utils.URN;
import uk.ac.open.kmi.watson.validation.utils.UriUtil;
import uk.ac.open.kmi.watson.validation.utils.V_CONSTANTS;

/**
 * Watson validation populator: extract an store the properties in an ontology.
 * @author mda99
 * @see PropertiesExtractor
 */
public class PropertiesPopulator extends Populator{
	
	/**
	 * set the code (2^5), the required status (nothing) and the extractor
	 */
	public PropertiesPopulator(){
		// set the code:
		this.code = V_CONSTANTS.PROPERTIES_POPULAOTR;
		// set the required status
		this.requiredStatus = 0;
		// set the extractor 
		this.extractor = new PropertiesExtractor();
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
        Vector<String> props = (Vector<String>)o;
        Document doc_doc = DataManager.getInstance().getDocumentIndex().getDocument(documentID, documentID);
        System.out.println("Found "+props.size()+" properties");
    	StringBuffer d_localN = new StringBuffer("");
       	StringBuffer d_PrpLocN = new StringBuffer("");
       	StringBuffer d_HPrpLocN = new StringBuffer("");
       	StringBuffer d_HCPrpLocN = new StringBuffer("");
        for(String uri : props){
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
           	ent_doc.add(new Field("type", Integer.toString(V_CONSTANTS.PROPERTY_ENTITY), Field.Store.YES, Field.Index.UN_TOKENIZED));       	
           	d_localN.append(" "+locN);
           	d_PrpLocN.append(" "+locN);
           	d_HPrpLocN.append(" "+HlocN);
           	d_HCPrpLocN.append(" "+HClocN);
           	/* String localN = doc_doc.get("localN");           	
           	String PrpLocN = doc_doc.get("PrpLocN");
           	String HPrpLocN = doc_doc.get("HPrpLocN");
           	String HCPrpLocN = doc_doc.get("HCPrpLocN");
           	if (localN == null) localN = "";
           	else doc_doc.removeField("localN");
           	if (PrpLocN == null) PrpLocN = "";
           	else doc_doc.removeField("PrpLocN");
        	if (HPrpLocN == null) HPrpLocN = "";
           	else doc_doc.removeField("HPrpLocN");
        	if (HCPrpLocN == null) HCPrpLocN = "";
           	else doc_doc.removeField("HCPrpLocN");
           	localN += " "+locN;
           	PrpLocN += " "+locN;
           	HPrpLocN += " "+HlocN;
           	HCPrpLocN += " "+HClocN;
        	doc_doc.add(new Field("localN", localN, Field.Store.NO, Field.Index.TOKENIZED));
           	doc_doc.add(new Field("PrpLocN", PrpLocN, Field.Store.NO, Field.Index.TOKENIZED)); 
          	doc_doc.add(new Field("HPrpLocN", HPrpLocN, Field.Store.NO, Field.Index.TOKENIZED)); 
          	doc_doc.add(new Field("HCPrpLocN", HCPrpLocN, Field.Store.NO, Field.Index.TOKENIZED)); */
        }
        String localN = doc_doc.get("localN");           	
       	String PrpLocN = doc_doc.get("PrpLocN");
       	String HPrpLocN = doc_doc.get("HPrpLocN");
       	String HCPrpLocN = doc_doc.get("HCPrpLocN");
       	if (localN == null) localN = "";
       	else doc_doc.removeField("localN");
       	if (PrpLocN == null) PrpLocN = "";
       	else doc_doc.removeField("PrpLocN");
    	if (HPrpLocN == null) HPrpLocN = "";
       	else doc_doc.removeField("HPrpLocN");
    	if (HCPrpLocN == null) HCPrpLocN = "";
       	else doc_doc.removeField("HCPrpLocN");
       	localN += " "+d_localN;
       	PrpLocN += " "+d_PrpLocN;
       	HPrpLocN += " "+d_HPrpLocN;
       	HCPrpLocN += " "+d_HCPrpLocN;
    	doc_doc.add(new Field("localN", localN.trim(), Field.Store.NO, Field.Index.TOKENIZED));
       	doc_doc.add(new Field("PrpLocN", PrpLocN.trim(), Field.Store.NO, Field.Index.TOKENIZED)); 
      	doc_doc.add(new Field("HPrpLocN", HPrpLocN.trim(), Field.Store.NO, Field.Index.TOKENIZED)); 
      	doc_doc.add(new Field("HCPrpLocN", HCPrpLocN.trim(), Field.Store.NO, Field.Index.TOKENIZED));
  }

}
