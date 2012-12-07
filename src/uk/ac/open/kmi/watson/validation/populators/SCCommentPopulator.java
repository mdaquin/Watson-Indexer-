package uk.ac.open.kmi.watson.validation.populators;

import uk.ac.open.kmi.watson.validation.Populator;
import uk.ac.open.kmi.watson.validation.extractors.ontology.SCCommentExtractor;
import uk.ac.open.kmi.watson.validation.utils.V_CONSTANTS;

/**
 * Watson validation populator: extract and store the comments of an ontology.
 * Require the language to be extracted.
 * @author mda99
 * @see SCCommentExtractor
 */
public class SCCommentPopulator extends Populator{
    
    /**
     * set the code (2^2), the required status (language done) and the extractor
     */
    public SCCommentPopulator(){
        // set the code:
        this.code = V_CONSTANTS.SC_COMMENT_POPULAOTR;
        // set the required status
        this.requiredStatus = new LanguagePopulator().getCode();
        // set the extractor
        this.extractor = new SCCommentExtractor();
    }
    
    /**
     * Test the pre-condition: the language should be OWL or DAML.
     */
    public boolean testPrecondition(){
 
    	return true; 
    }
    
    /**
     * write to the database in the corresponding relation.
     * for the moment, write on the console...
     */
    protected void writeToDB(Object o){
       
    }
    
}