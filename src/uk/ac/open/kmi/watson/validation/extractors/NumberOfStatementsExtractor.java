/*
 * NumberOfStatementsExtractor.java
 *
 * Created on 29 May 2007, 11:46
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package uk.ac.open.kmi.watson.validation.extractors;

import uk.ac.open.kmi.watson.validation.Extractor;
import com.hp.hpl.jena.rdf.model.Model;

/**
 * 
 * @author cb7224, mda99
 */
public class NumberOfStatementsExtractor extends Extractor {
    
    public Object extract(Model model, String documentID) {
    	if (model == null) {
    		errorMessage = "Cannot deal with a null model";
    		return null;
    	}
        return ((long)model.listStatements().toList().size());
    }
    
}
