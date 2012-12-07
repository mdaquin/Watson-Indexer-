/*
 * DLexpressivityPopulator.java
 *
 * Created on 20 April 2007, 14:20
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package uk.ac.open.kmi.watson.validation.populators;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;

import uk.ac.open.kmi.watson.validation.DataManager;
import uk.ac.open.kmi.watson.validation.Populator;
import uk.ac.open.kmi.watson.validation.extractors.DLExpressivnessExtractor;
import uk.ac.open.kmi.watson.validation.utils.V_CONSTANTS;

/**
 *
 * @author mda99, cb7224
 */
public class DLexpressivityPopulator extends Populator{
    
    /** Creates a new instance of DLexpressivityPopulator */
    public DLexpressivityPopulator() {
        // set the code:
        this.code = V_CONSTANTS.DL_EXPRESSIVITY_POPULATOR;
        // set the required status
        this.requiredStatus = 0 ; //new LanguagePopulator().getCode();
        // set the extractor
        this.extractor = new DLExpressivnessExtractor();
    }
    
    public boolean testPrecondition() {
        return true;
    }
    
    protected void writeToDB(Object o) {
    	 Document doc_doc = DataManager.getInstance().getDocumentIndex().getDocument(documentID, documentID);
         String expr = (String)o;
         doc_doc.add(new Field("DL", expr, Field.Store.YES, Field.Index.UN_TOKENIZED));
    }
    
    /*	Factory factory = new Factory();
        factory.beginTransaction();
        Controller c = new Controller();
        uk.ac.open.kmi.watson.db.Measures sc = factory.getDocument(documentID).measures();
        String expressivity = (String)o;
        sc.setDlExpressivity(expressivity);
        factory.commitTransaction();
    }*/
    
}
