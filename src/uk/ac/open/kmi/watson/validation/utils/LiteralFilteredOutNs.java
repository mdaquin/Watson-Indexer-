/*
 * LiteralFilteredOutNs.java
 *
 * Created on 17 April 2007, 13:37
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package uk.ac.open.kmi.watson.validation.utils;

import java.util.ArrayList;

/**
 *
 * @author cb7224
 */
public class LiteralFilteredOutNs extends FilteredOutNs{
    
    static {
        nss = new ArrayList();
        //Uncomment to NOT consider literal value on FOAF resources
//        nss.add(NameSpaces.NS_FOAF);
        
        //Uncomment to NOT consider literal value on RSS resources
        nss.add(NameSpaces.NS_RSS);
    }
}
