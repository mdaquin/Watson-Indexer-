package uk.ac.open.kmi.watson.validation.populators;

import java.math.BigDecimal;
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
 * Not from the model, but from the validation database...
 * @author mda99
 * @see EmptyExtractor
 */
public class SizePopulator extends Populator{
	
	/**
	 * set the code (2^32), the required status (nothing) and the extractor
	 */
	public SizePopulator(){
		// set the code:
		this.code = V_CONSTANTS.SIZE_POPULATOR;
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
			long size = PopulatorManager.getInstance().getDocumentStatus(documentID).getSize();
			doc_doc.add(new Field("size", ""+size, Field.Store.YES, Field.Index.UN_TOKENIZED));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
}
