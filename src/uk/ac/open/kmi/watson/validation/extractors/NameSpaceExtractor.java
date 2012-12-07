/*
 * NameSpaceExtractor.java
 *
 * Created on 09 May 2007, 12:50
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package uk.ac.open.kmi.watson.validation.extractors;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.vocabulary.DAML_OIL;
import com.hp.hpl.jena.vocabulary.OWL;
import com.hp.hpl.jena.vocabulary.RDF;
import com.hp.hpl.jena.vocabulary.RDFS;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;
//import uk.ac.open.kmi.watson.validation.EntityExtractor;
import uk.ac.open.kmi.watson.validation.Extractor;
import uk.ac.open.kmi.watson.validation.utils.ClassFilteredOutNs;
import uk.ac.open.kmi.watson.validation.utils.ModelUtils;

/**
 * TODO: rewrite to be less complicated and more independent.
 * @author cb7224
 */
public class NameSpaceExtractor extends Extractor{
    
    /** Creates a new instance of NameSpaceExtractor */
    public NameSpaceExtractor() {
    }

    public Object extract(Model model, String documentID) {
        if (model == null) {
            errorMessage = "cannot deal with null models";
            return null;
        }
        Vector<String> result = new Vector();
         result = getNScollection(model);
        
        errorMessage = null;
        return result;
    }
    
    private Vector<String> getNScollection(Model model) {
        String lang = "";
        Vector<String> namespaces = new Vector();
        boolean OWLlang = false;
        boolean DAMLlang = false;
        boolean RDFSlang = false;
        boolean RDFlang = false;
        boolean recognized = false;
        
        /**check the namespaces and collect the ones which are not listed in the filteredOut.*/
        Iterator<String> it = listNameSpaces(model);
        while (it.hasNext()) {
            String ns = it.next();
            if (ns.equals(RDF.getURI())){RDFlang = true; recognized = true;}
            if (ns.equals(OWL.NS)){OWLlang = true; recognized = true;}
            if (ns.equals(DAML_OIL.NAMESPACE_DAML_2000_12_URI)){DAMLlang = true; recognized = true;}
            if (ns.equals(DAML_OIL.NAMESPACE_DAML_2001_03_URI)){DAMLlang = true; recognized = true;}
            if (ns.equals((RDFS.getURI()))) {RDFSlang = true; recognized = true;}
            
            /**if the actual NS is mapped with no one of the above check if is a refused NS and if not the case add it to the list
             *of NS used in the document.
             */
            if ( !recognized && !namespaces.contains(ns) && !ClassFilteredOutNs.contains(ns) )
                namespaces.add(ns);
            recognized = false;
        }
        return namespaces;
    }
    
    private static Iterator<String> listNameSpaces(Model model) {
        Vector<String> temp = new Vector();
        
        temp = addAllUnique(temp, model.listNameSpaces().toList());
        temp = addAllUnique(temp, listResourceNS(model.listObjects()));
        temp = addAllUnique(temp, listResourceNS(model.listSubjects()));
        return temp.iterator();
    }
    
    private static Vector<String> listResourceNS(Iterator it) {
        Vector<String> temp = new Vector();
        String ns = "";
        while (it.hasNext()) {
            Object o = (Object) it.next();
            if(o instanceof RDFNode){
                RDFNode node = (RDFNode)o;
                if(!node.isAnon() && node.isResource() ){
                    Resource r = (Resource)node;
                    ns = ModelUtils.getNamespace(r.getURI());
                }
            }else if(o instanceof Resource){
                Resource r = (Resource)o;
                if(!r.isAnon() && r.isResource() ){
                    ns = ModelUtils.getNamespace(r.getURI());
                }
            }
            if(!temp.contains(ns))
                temp.add(ns);
        }
        return temp;
    }
    
    private static Vector<String> addAllUnique(Vector<String> base, Collection<String> newCollection){
        for(String ns : newCollection){
            if(!base.contains(ns))
                base.add(ns);
            
        }
        return base;
    }
}
