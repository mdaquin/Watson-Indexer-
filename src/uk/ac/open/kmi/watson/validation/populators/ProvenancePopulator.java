package uk.ac.open.kmi.watson.validation.populators;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;

import uk.ac.open.kmi.watson.validation.DataManager;
import uk.ac.open.kmi.watson.validation.Populator;
import uk.ac.open.kmi.watson.validation.PopulatorManager;
import uk.ac.open.kmi.watson.validation.extractors.EmptyExtractor;
import uk.ac.open.kmi.watson.validation.utils.V_CONSTANTS;

/**
 * Watson validation populator: extract the provenance of the ontology. 
 * Not from the model, but from the validation file...
 * Also put the Ontology Space name and cache location if they are set.
 * @author mda99
 * @see EmptyExtractor
 */
public class ProvenancePopulator extends Populator{
	
	/**
	 * set the code (2^32), the required status (nothing) and the extractor
	 */
	public ProvenancePopulator(){
		// set the code:
		this.code = V_CONSTANTS.PROVENANCE_POPULATOR;
		// set the required status = nothing 
		this.requiredStatus = 0; 
		// set the extractor 
		this.extractor = new EmptyExtractor();
	}
	
	/** 
	 * Test the pre-condition: nothing, returns true
	 */
	public boolean testPrecondition(){
		return true;
	}
	
	/**
	 * write to the database in the corresponding of SemanticContent.
	 */
	protected void writeToDB(Object o){
		Document doc_doc = DataManager.getInstance().getDocumentIndex().getDocument(documentID, documentID);	
		try {
			String[] prov = PopulatorManager.getInstance().getDocumentStatus(documentID).getProvenance();
			String osname = PopulatorManager.getInstance().getDocumentStatus(documentID).getOntologySpaceName();
			String cache = PopulatorManager.getInstance().getDocumentStatus(documentID).getCacheLocation();
			String group = PopulatorManager.getInstance().getDocumentStatus(documentID).getPartOf();		
			String provs = ""; int i = 0;
			for (String pr : prov){
				doc_doc.add(new Field("prov", pr, Field.Store.YES, Field.Index.UN_TOKENIZED));
				if ((i++) != 0) provs += " ";
				provs += pr;
			}
	        doc_doc.add(new Field("provs", provs, Field.Store.YES, Field.Index.UN_TOKENIZED));	
	        doc_doc.add(new Field("id", documentID, Field.Store.YES, Field.Index.UN_TOKENIZED));
	        if (osname != null)
				doc_doc.add(new Field("osname", osname, Field.Store.YES, Field.Index.UN_TOKENIZED));
	        if (cache!=null)
				doc_doc.add(new Field("cache", cache, Field.Store.YES, Field.Index.UN_TOKENIZED));
	        if (group!=null)
				doc_doc.add(new Field("group", group, Field.Store.YES, Field.Index.UN_TOKENIZED));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
}
