/*
 * JenaUtils.java
 *
 * Created on April 19, 2007, 1:12 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package uk.ac.open.kmi.watson.validation.utils;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.hp.hpl.jena.graph.Node;
import com.hp.hpl.jena.graph.Triple;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.vocabulary.RDF;

/**
 *
 * @author lg3388
 */
public class JenaUtils {
   
    public static final String CACHE = "http://paoli.open.ac.uk/watson-cache/";
    public static final String CACHE2 = "http://kmi-web05.open.ac.uk:81/cache/";
    
    
    public static Model getModel(String name) {
  	  long t1 = System.currentTimeMillis();
  	   try {
  	  	Model model = ModelFactory.createDefaultModel();
  	    model.read(CACHE + getPath(name));
        System.out.println("Time to create model:: "+(t1-System.currentTimeMillis()));
        return model;
  	   } catch(Exception e){
  		   try {
  		   System.out.println("Not found in Crawler cache... try production cache...");
  		   Model model = ModelFactory.createDefaultModel();
  		   model.read(CACHE2 + getPath(name));
  	       System.out.println("Time to create model:: "+(t1-System.currentTimeMillis()));
  	       return model;
  		   } catch (Exception e2){
  			   e.printStackTrace();
  		   }
  	   }
  	   return null;
    }

    
    public static String getPath(String contentURI) {
        //urn:sha1:fc75445068463f261e4da336a8b1c99d75e84ff0
        //01234567890123456789012345678901234567890123456789
        //---------^-^--^---^----^---------^
        //9,11,14,18,23,33
        return contentURI.substring(9, 10)
        + "/"
                + contentURI.substring(10, 13)
                + "/"
                + contentURI.substring(13, 17)
                + "/"
                + contentURI.substring(17, 22)
                + "/"
                + contentURI.substring(22, 32)
                + "/"
                + contentURI.substring(32);
    }
    
    public static Iterator<String> listNameSpaces(Model m)  {
        Set<String> nameSpaces = new HashSet();
        updateNamespace( nameSpaces, listPredicates(m) );
        updateNamespace( nameSpaces, listTypes(m) );
        return nameSpaces.iterator();
    }
    
    private static void updateNamespace( Set set, Iterator it ) {
        while (it.hasNext()) {
            Node node = (Node) it.next();
            if (node.isURI()) {
                String uri = node.getURI();
                // String ns = uri.substring( 0, Util.splitNamespace( uri ) );
                // String ns = IteratorFactory.asResource( node, this ).getNameSpace();
                String ns = uri;
                int i = uri.lastIndexOf("#");
                if ( i == -1 ) i = uri.lastIndexOf("/");
                if ( i != -1 ) {
                    ns = uri.substring(0, i);
                } else {
                    ns = uri.substring( 0, com.hp.hpl.jena.rdf.model.impl.Util.splitNamespace( uri ) );
                }
                set.add( ns );
            }
        }
    }
    
    
    private static Iterator listPredicates(Model m){
        return m.getGraph().queryHandler().predicatesFor( Node.ANY, Node.ANY );
    }
    
    private static Iterator listTypes(Model m) {
        Set types = new HashSet();
        com.hp.hpl.jena.util.iterator.ClosableIterator it = m.getGraph().find( null, RDF.type.asNode(), null );
        while (it.hasNext()) types.add( ((Triple) it.next()).getObject() );
        return types.iterator();
    }
    
    private static Iterator listOwlImports(Model m) {
        Set types = new HashSet();
        com.hp.hpl.jena.util.iterator.ClosableIterator it = m.getGraph().find( null, com.hp.hpl.jena.vocabulary.OWL.imports.asNode(), null );
        while (it.hasNext()) types.add( ((Triple) it.next()).getObject() );
        return types.iterator();
    }
    
    private static Iterator listSeeAlso(Model m) {
        Set types = new HashSet();
        com.hp.hpl.jena.util.iterator.ClosableIterator it = m.getGraph().find( null, com.hp.hpl.jena.vocabulary.RDFS.seeAlso.asNode(), null );
        while (it.hasNext()) types.add( ((Triple) it.next()).getObject() );
        return types.iterator();
    }
    
    
}
