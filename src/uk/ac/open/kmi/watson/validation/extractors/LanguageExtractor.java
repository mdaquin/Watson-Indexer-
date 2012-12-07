package uk.ac.open.kmi.watson.validation.extractors;

import java.util.Vector;

import uk.ac.open.kmi.watson.validation.Extractor;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.NsIterator;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.vocabulary.DAML_OIL;
import com.hp.hpl.jena.vocabulary.OWL;
import com.hp.hpl.jena.vocabulary.RDF;
import com.hp.hpl.jena.vocabulary.RDFS;

/**
 * Extract the languages used in an ontology (RDF, RDFS, OWL, DAML+OIL).
 * @author mda99, cb??
 */
public class LanguageExtractor extends Extractor{
    
    private static final Resource[] OWLDeclarativeResource =
    {OWL.Class, OWL.DatatypeProperty, OWL.FunctionalProperty,
     OWL.InverseFunctionalProperty, OWL.ObjectProperty,
     OWL.SymmetricProperty, OWL.TransitiveProperty,
     OWL.Restriction, OWL.Ontology, };
    private static final Resource[] DAML_OILDeclarativeResource =
    {DAML_OIL.Class, DAML_OIL.Property, DAML_OIL.DatatypeProperty,
     DAML_OIL.ObjectProperty, DAML_OIL.TransitiveProperty, DAML_OIL.UnambiguousProperty,
     DAML_OIL.UniqueProperty, DAML_OIL.Ontology, DAML_OIL.Restriction};
    private static final Resource[] RDFSDeclarativeResource = {RDFS.Class};
    
    /**
     * Return a Vector of String with the name of the languages in it.
     */
    public Object extract(Model m, String documentID){
        if (m==null) {errorMessage="cannot deal with null models"; return null;}
        Vector<String> results = new Vector<String>();
        // look at the namespace for RDF...
        NsIterator nsit = m.listNameSpaces();
        while (nsit.hasNext()) {
            String ns = nsit.nextNs();
            if (ns.equals(RDF.getURI())) {results.add("RDF");}
        }
        nsit.close();
        if (modelContains(m , OWLDeclarativeResource)){results.add("OWL");}
        if (modelContains(m , DAML_OILDeclarativeResource)){results.add("DAML+OIL");}
        if (modelContains(m , RDFSDeclarativeResource)){results.add("RDF-S");}
        
        errorMessage = null;
        return results;
    }
    
    
    private boolean modelContains(Model m , Resource[] c2) {
        for(Resource res : c2) {
            if (m.contains(null , RDF.type , res))
                return true;
        }
        return false;
    }
    
    /** for test and use as stand-alone application **/
	public static void main(String[] args){
		if (args.length != 1) {
			usage(); 
		}
		LanguageExtractor app = new LanguageExtractor();
		Model m = ModelFactory.createDefaultModel();
		m.read(args[0]);
		Vector<String> lang = (Vector<String>) app.extract(m, null);
		for(String s : lang) System.out.println(s);
	}

	private static void usage(){
		System.err.println("Usage: java ClassExtractor <-l|-n|-c> <http://url.of.the.ontology>\n-l means list the classes, -n list the local names, and -c count them.");
		System.exit(-1);
	}
    
}