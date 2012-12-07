package uk.ac.open.kmi.watson.validation.populators;

import java.util.Vector;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;

import uk.ac.open.kmi.watson.validation.DataManager;
import uk.ac.open.kmi.watson.validation.Populator;
import uk.ac.open.kmi.watson.validation.extractors.InconsistencyExtractor;
import uk.ac.open.kmi.watson.validation.extractors.ontology.ImportsExtractor;
import uk.ac.open.kmi.watson.validation.utils.V_CONSTANTS;

/**
 * Watson validation populator: extract an store the information about the consistency of an ontology.
 * Require the language to be extracted.
 * @author mda99
 * @see InconsistencyExtractor
 */
public class ImportsPopulator extends Populator{
    
    /**
     * set the code (2^20), the required status (langauge) and the extractor
     */
    public ImportsPopulator(){
        // set the code:
        this.code = V_CONSTANTS.IMPORTS_POPULATOR;
        // set the required status
        this.requiredStatus = V_CONSTANTS.LANGUAGE_POPULATOR;
        // set the extractor
        this.extractor = new ImportsExtractor();
    }
    
    /**
     * Test the pre-condition: the language should be OWL or DAML.
     */
    public boolean testPrecondition(){
    	// TODO: have to be OWL or DAML
    	return true;
    }
    
    /**
     * write to the database in the corresponding attribute of Measures.
     */
    protected void writeToDB(Object o){
    	try {
    	Document doc_doc = DataManager.getInstance().getDocumentIndex().getDocument(documentID, documentID);
        Vector<String> imps = (Vector<String>)o;
        String stImps = "";
        for(String imp : imps){
          doc_doc.add(new Field("import", imp, Field.Store.YES, Field.Index.UN_TOKENIZED));
          stImps += " "+imp;
        }
        doc_doc.add(new Field("imports", stImps, Field.Store.YES, Field.Index.UN_TOKENIZED));
    	} catch(Exception e){
        	e.printStackTrace();
        }
    }
}