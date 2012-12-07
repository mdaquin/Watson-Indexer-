/*
 * NumberOfStatementsPopulator.java
 *
 * Created on 29 May 2007, 11:51
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package uk.ac.open.kmi.watson.validation.populators;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;

import uk.ac.open.kmi.watson.validation.DataManager;
import uk.ac.open.kmi.watson.validation.Populator;
import uk.ac.open.kmi.watson.validation.extractors.NumberOfStatementsExtractor;
import uk.ac.open.kmi.watson.validation.utils.V_CONSTANTS;

/**
 *
 * @author cb7224
 */
public class NumberOfStatementsPopulator extends Populator{
    
    /** Creates a new instance of NumberOfStatementsPopulator */
    public NumberOfStatementsPopulator() {
        // set the code:
        this.code = V_CONSTANTS.NUM_OF_STATEMENTS_POPULATOR;
        // set the required status
        this.requiredStatus = 0;
        // set the extractor
        this.extractor = new NumberOfStatementsExtractor();
    }
    
    public boolean testPrecondition() {
        return true;
    }
    
    protected void writeToDB(Object o) {
    	Document doc_doc = DataManager.getInstance().getDocumentIndex().getDocument(documentID, documentID);
        String nb = ((Long)o).toString();
        doc_doc.add(new Field("nbStat", nb, Field.Store.YES, Field.Index.UN_TOKENIZED));
    }
    
    
}
