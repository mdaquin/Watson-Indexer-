/*
 * OWLSpeciePopulator.java
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
import uk.ac.open.kmi.watson.validation.extractors.OWLSpeciesExtractor;
import uk.ac.open.kmi.watson.validation.utils.V_CONSTANTS;

/**
 *
 * @author cb7224
 */
public class OWLSpeciePopulator extends Populator{
    
    /** Creates a new instance of OWLSpeciePopulator */
    public OWLSpeciePopulator() {
        // set the code:
        this.code = V_CONSTANTS.OWL_SPECIE_POPULAOTR;
        // set the required status
        this.requiredStatus = new LanguagePopulator().getCode();
        // set the extractor
        this.extractor = new OWLSpeciesExtractor();
    }
    
    public boolean testPrecondition() {
    	Document doc_doc = DataManager.getInstance().getDocumentIndex().getDocument(documentID, documentID);
    	String lang = doc_doc.get("lang");
    	if (lang == null) return false;
    	return lang.contains("OWL");
    }
    
    protected void writeToDB(Object o) {
    	Document doc_doc = DataManager.getInstance().getDocumentIndex().getDocument(documentID, documentID);
        Integer OWL = (Integer)o;
        doc_doc.add(new Field("Owl", OWL.toString(), Field.Store.YES, Field.Index.UN_TOKENIZED));
    }
    
}
