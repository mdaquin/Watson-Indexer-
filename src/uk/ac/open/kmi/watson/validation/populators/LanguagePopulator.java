package uk.ac.open.kmi.watson.validation.populators;

import java.util.Vector;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;

import uk.ac.open.kmi.watson.validation.DataManager;
import uk.ac.open.kmi.watson.validation.Populator;
import uk.ac.open.kmi.watson.validation.extractors.LanguageExtractor;
import uk.ac.open.kmi.watson.validation.utils.V_CONSTANTS;

/**
 * Watson validation populator: extract the languages enployed in the ontology. 
 * @author mda99
 * @see LanguageExtractor
 */
public class LanguagePopulator extends Populator{
	
	/**
	 * set the code (2^10), the required status (nothing) and the extractor
	 */
	public LanguagePopulator(){
		// set the code:
		this.code = V_CONSTANTS.LANGUAGE_POPULATOR;
		// set the required status = nothing 
		this.requiredStatus = 0; 
		// set the extractor 
		this.extractor = new LanguageExtractor();
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
        Vector<String> langs = (Vector<String>)o;
        String stLangs = "";
        for(String lang : langs){
          stLangs += " "+lang;
        }
        doc_doc.add(new Field("lang", stLangs, Field.Store.YES, Field.Index.TOKENIZED));
	}
}
