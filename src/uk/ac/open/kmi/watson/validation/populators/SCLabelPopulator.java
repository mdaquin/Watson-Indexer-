package uk.ac.open.kmi.watson.validation.populators;

import uk.ac.open.kmi.watson.validation.Populator;
import uk.ac.open.kmi.watson.validation.extractors.ontology.SCLabelExtractor;
import uk.ac.open.kmi.watson.validation.utils.V_CONSTANTS;

/**
 * Watson validation populator: extract and store the labels of an ontology. 
 * Require the language to be extracted.
 * @author mda99
 * @see SCLabelExtractor
 */
public class SCLabelPopulator extends Populator{
	
	/**
	 * set the code (2^3), the required status (language done) and the extractor
	 */
	public SCLabelPopulator(){
		// set the code:
		this.code = V_CONSTANTS.SC_LABEL_POPULATOR;
		// set the required status
		this.requiredStatus = new LanguagePopulator().getCode();
		// set the extractor 
		this.extractor = new SCLabelExtractor();
	}
	
	/** 
	 * Test the pre-condition: the language should be OWL or DAML.
	 */
	public boolean testPrecondition(){

		return true; 
	}
	
	/**
	 * write to the database in the corresponding relation.
	 */
	protected void writeToDB(Object o){
	  	
	}

}