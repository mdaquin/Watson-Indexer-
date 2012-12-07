/*
 * ClassFilteredOutNs.java
 *
 * Created on 17 April 2007, 13:19
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package uk.ac.open.kmi.watson.validation.utils;

import com.hp.hpl.jena.vocabulary.DCTerms;
import com.hp.hpl.jena.vocabulary.DCTypes;
import com.hp.hpl.jena.vocabulary.DC_10;
import com.hp.hpl.jena.vocabulary.DC_11;
import java.util.ArrayList;

/**
 *
 * @author cb7224
 */
public class ClassFilteredOutNs extends FilteredOutNs{
    
    static { 
        nss = new ArrayList();
        //Uncomment to NOT consider Dublin Core resource when discovering Semantic Content resources
        nss.add(DCTerms.NS);
        nss.add(DCTypes.NS);
        nss.add(DC_10.getURI());
        nss.add(DC_11.getURI());
        //Uncomment to NOT consider FOAF resource when discovering Semantic Content resources
        nss.add(NameSpaces.NS_FOAF);
        //Uncomment to NOT consider RSS resource when discovering Semantic Content resources
        nss.add(NameSpaces.NS_RSS);
    }
    

    
}
