/*
 * FilteredOutNs.java
 *
 * Created on 10 November 2006, 21:35
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
import uk.ac.open.kmi.watson.validation.utils.NameSpaces;

/**
 *
 * @author cb7224
 */
public class FilteredOutNs {
    
    protected static ArrayList<String> nss;
    
    static { 
        nss = new ArrayList();
        nss.add(DCTerms.NS);
        nss.add(DCTypes.NS);
        nss.add(DC_10.getURI());
        nss.add(DC_11.getURI());
        nss.add(NameSpaces.NS_FOAF);
        nss.add(NameSpaces.NS_RSS);
    }
    
    
    public static boolean contains(String ns)
    {
        for (String forbiddenNS : nss){
             if (forbiddenNS.contains(ns))//the match is necessary cos the RSS vocabulary has many modules which otherwise
                                          //should be listed, this way it only matches the first part of the NS which remains the same
                return true;
        }
        return false;
    }
}
