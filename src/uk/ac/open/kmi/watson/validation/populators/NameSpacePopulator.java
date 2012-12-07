/*
 * NameSpacePopulator.java
 *
 * Created on 09 May 2007, 12:52
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package uk.ac.open.kmi.watson.validation.populators;

import java.util.Vector;

import uk.ac.open.kmi.watson.validation.Populator;
import uk.ac.open.kmi.watson.validation.extractors.NameSpaceExtractor;
import uk.ac.open.kmi.watson.validation.utils.V_CONSTANTS;

import com.hp.hpl.jena.graph.Factory;

/**
 *
 * @author cb7224
 */
public class NameSpacePopulator extends Populator{
    
    /** Creates a new instance of NameSpacePopulator */
    public NameSpacePopulator() {
        // set the code:
        this.code = V_CONSTANTS.NAME_SPACE_POPULATOR;
        // set the required status
        this.requiredStatus = 0;
        // set the extractor
        this.extractor = new NameSpaceExtractor();
    }
    
    public boolean testPrecondition() {
        return true;
    }
    
    protected void writeToDB(Object o) {
        
    }
    
}
