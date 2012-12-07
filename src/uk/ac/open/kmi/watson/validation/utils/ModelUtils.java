/*
 @prefix    :   <http://nwalsh.com/rdf/cvs#> .
 @prefix  dc:   <http://purl.org/dc/elements/1.1/> .
 @prefix rdf:   <http://www.w3.org/1999/02/22-rdf-syntax-ns#> .
 
    <>  dc:title    "ModelUtils.java";
        :Revision   "$Revision: 1.1 $";
        :Author     "$Author: mathieu $";
        :date       "$Date: 2008/06/09 08:53:16 $";
        :Log        """
                    $Log: ModelUtils.java,v $
                    Revision 1.1  2008/06/09 08:53:16  mathieu
                    *** empty log message ***

                    Revision 1.1  2007/10/20 06:46:43  mathieu
                    *** empty log message ***

                    Revision 1.2  2007/08/14 12:37:54  mat
                    First code "rearrangement"

                    Revision 1.1  2007/06/14 14:26:12  mat
                    First commit

                    Revision 1.2  2007/04/20 18:37:26  Mathieu
                    *** empty log message ***

                    Revision 1.1  2007/04/17 16:54:10  Claudio
                    Class that implements methods that deal with resources

                    Revision 1.1  2006/12/20 16:39:09  cb7224
                    Change of Package of this class

                    Revision 1.2  2006/11/30 14:31:49  cb7224
                    no message

                    Revision 1.1  2006/11/27 16:21:37  lg3388
                    model utils

                    """;
        :Id         "$Id: ModelUtils.java,v 1.1 2008/06/09 08:53:16 mathieu Exp $" .
 */

package uk.ac.open.kmi.watson.validation.utils;

import com.hp.hpl.jena.graph.Node;
import com.hp.hpl.jena.graph.Triple;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.NodeIterator;
import com.hp.hpl.jena.rdf.model.ResIterator;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.vocabulary.DAML_OIL;
import com.hp.hpl.jena.vocabulary.OWL;
import com.hp.hpl.jena.vocabulary.RDF;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.Vector;

/**
 *
 * @author  $Author: mathieu $
 * @version $Revision: 1.1 $
 */
public class ModelUtils {

	 public static Resource getOntology(Model m) {
		Resource owlOntology = getOwlOntology(m);
		return owlOntology != null ? owlOntology : getDamlOntology(m);
	}

	private static Resource getOwlOntology(Model m) {
		NodeIterator pvIt = m.listObjectsOfProperty(OWL.priorVersion);

		Vector<String> pvURI = new Vector<String>();
		while (pvIt.hasNext()) {
			Resource pvOntology = (Resource) pvIt.next();
			pvURI.add(pvOntology.getURI());
		}

		ResIterator it = m.listSubjectsWithProperty(RDF.type, OWL.Ontology);
		while (it.hasNext()) {
			Resource ontology = (Resource) it.next();
			if (!pvURI.contains(ontology.getURI())) {
				return ontology;
			}
		}
		return null;
	}

	private static Resource getDamlOntology(Model m) {
		ResIterator it = m
				.listSubjectsWithProperty(RDF.type, DAML_OIL.Ontology);
		while (it.hasNext()) {
			return (Resource) it.next();
		}
		return null;
	}
	
	
    public static Iterator<String> listNameSpaces(Model m)  {
        Set<String> nameSpaces = new HashSet();
        updateNamespace( nameSpaces, listPredicates(m) );
        updateNamespace( nameSpaces, listTypes(m) );
        return nameSpaces.iterator();
    }
    
    private static void updateNamespace( Set set, Iterator it ) {
        while (it.hasNext())
            {
            Node node = (Node) it.next();
            if (node.isURI())
                {
                String uri = node.getURI();
                String ns = getNamespace(uri);
                set.add( ns );
                }
            }
   }
    
    public static String getNamespace(String uri) {
        return splitNamespace(uri)[0];
    }
    
    public static String getLocalName(String uri) {
        return splitNamespace(uri)[1];
    }
    
    public static String[] splitNamespace(String uri) {
    	String ns, localName;
        ns = uri;
        // seek #
        int i = uri.lastIndexOf("#");
        // no #, then seek /
        if ( i == -1 ) i = uri.lastIndexOf("/");
        if ( i != -1 ) {
            ns = uri.substring(0, i + 1);
        } else {
            // no # or /, then use ModelUtils default
            ns = uri.substring( 0, com.hp.hpl.jena.rdf.model.impl.Util.splitNamespace( uri ) );
        }
        localName = uri.substring(ns.length());
        return new String[] {ns, localName};
    }
    
    
   public static Iterator listPredicates(Model m){
       return m.getGraph().queryHandler().predicatesFor( Node.ANY, Node.ANY ); 
   }
     
   public static Iterator listTypes(Model m) {
        Set types = new HashSet();
        com.hp.hpl.jena.util.iterator.ClosableIterator it = m.getGraph().find( null, RDF.type.asNode(), null );
        while (it.hasNext()) types.add( ((Triple) it.next()).getObject() );
        return types.iterator();
   }

   public static Iterator listOwlImports(Model m) {
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
