package uk.ac.open.kmi.watson.validation.populators;

import uk.ac.open.kmi.watson.validation.Populator;
import uk.ac.open.kmi.watson.validation.extractors.InconsistencyExtractor;
import uk.ac.open.kmi.watson.validation.utils.V_CONSTANTS;

/**
 * Watson validation populator: extract an store the information about the consistency of an ontology. 
 * Require the language to be extracted.
 * @author mda99
 * @see InconsistencyExtractor
 */
public class InconsistencyPopulator extends Populator{
	
	/**
	 * set the code (2^11), the required status (language done) and the extractor
	 */
	public InconsistencyPopulator(){
		// set the code:
		this.code = V_CONSTANTS.INCONSISTENCY_POPULATOR;
		// set the required status
		this.requiredStatus = new LanguagePopulator().getCode();
		// set the extractor 
		this.extractor = new InconsistencyExtractor();
	}
	
	/** 
	 * Test the pre-condition: the language should be OWL or DAML.
	 */
	public boolean testPrecondition(){
		return false;
	}
	
	/**
	 * write to the database in the corresponding attribute of Measures.
	 */
	protected void writeToDB(Object o){
		
	}
}