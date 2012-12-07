package uk.ac.open.kmi.watson.validation.populators;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;

import uk.ac.open.kmi.watson.validation.DataManager;
import uk.ac.open.kmi.watson.validation.Populator;
import uk.ac.open.kmi.watson.validation.extractors.ontology.URIExtractor;
import uk.ac.open.kmi.watson.validation.utils.V_CONSTANTS;

/**
 * Watson validation populator: extract an store the declared URI of the model if it exists.
 * @author mda99
 * @see URIExtractor
 */
public class URIPopulator extends Populator{
	
	/**
	 * set the code (2^1), the required status (nothing) and the extractor
	 */
	public URIPopulator(){
		// set the code:
		this.code = V_CONSTANTS.URI_POPULATOR;
		// set the required status (in fact, not dependent of the language)
		this.requiredStatus = 0;
		// set the extractor 
		this.extractor = new URIExtractor();
	}
	
	/** 
	 * No Precondition. returns true.
	 */
	public boolean testPrecondition(){
		return true;
	}
	
	/**
	 * write to the database in the corresponding attribute of Measures.
	 * for the moment, write on the console...
	 */
	protected void writeToDB(Object o){
	  	Document doc_doc = DataManager.getInstance().getDocumentIndex().getDocument(documentID, documentID);
        String URI = (String)o;
        if (!(URI.startsWith("http://kmi-web05") || URI.startsWith("http://paoli")))
        	doc_doc.add(new Field("URI", URI, Field.Store.YES, Field.Index.UN_TOKENIZED));
	}
	
}