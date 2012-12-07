package uk.ac.open.kmi.watson.validation.populators;

import uk.ac.open.kmi.watson.validation.Populator;
import uk.ac.open.kmi.watson.validation.extractors.UnsatisfiableConceptsExtractor;
import uk.ac.open.kmi.watson.validation.utils.V_CONSTANTS;

/**
 * Watson validation populator: extract an store the number of unsatisfiable concepts in an ontology. 
 * Require the language and the inconsistency to be extracted.
 * @author mda99
 * @see UnsatisfiableConceptsExtractor
 */
public class UnsatisfiableConceptsPopulator extends Populator{
	
	/**
	 * set the code (2^12), the required status (inconsistency and language done) and the extractor
	 */
	public UnsatisfiableConceptsPopulator(){
		// set the code:
		this.code = V_CONSTANTS.UNSATISFIABILITY_POPUPALTOR;
		// set the required status 
		this.requiredStatus = new InconsistencyPopulator().getCode() + new LanguagePopulator().getCode();
		// set the extractor 
		this.extractor = new UnsatisfiableConceptsExtractor();
	}
	
	/** 
	 * Test the pre-condition: the language should be OWL or DAML and the onto should be consistent.
	 * UNIMPLEMENTED
	 */
	public boolean testPrecondition(){
		return true;
	}
	
	/**
	 * write to the database in the corresponding attribute of Measures.
	 * for the moment, write on the console...
	 * UNIMPLEMENTED
	 */
	protected void writeToDB(Object o){
		System.out.println("NB unsatisfiable concepts for "+documentID+" = "+((Integer)o).intValue());
	}
}