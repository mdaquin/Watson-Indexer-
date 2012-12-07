package uk.ac.open.kmi.watson.validation.extractors.ontology;

import com.hp.hpl.jena.util.iterator.ExtendedIterator;
import java.util.Vector;

import uk.ac.open.kmi.watson.validation.Extractor;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.vocabulary.DAML_OIL;
import com.hp.hpl.jena.vocabulary.OWL;

/**
 * Extract the URI of an ontology if it is declared (in the ontology header or the xml:base)
 * @author mda99, cb??
 */
public class ImportsExtractor extends Extractor {
    
    /** returns a String with the URI or null **/
    public Object extract(Model m, String documentID){
        Vector<String> result = new Vector<String>();
        if (m==null) {errorMessage="cannot deal with null models"; return null;}
        ExtendedIterator it = m.listObjectsOfProperty(OWL.imports).andThen(m.listObjectsOfProperty(DAML_OIL.imports));
        while(it.hasNext()){
            RDFNode n = (RDFNode)it.next();
            try {
            	result.add( ((Resource)n).getURI());
            }
            catch(Exception e){
            	errorMessage = e.getMessage();
            	errorMessage = null;
            }
        }
        return result;
    }
    
    /** for test and use as stand-alone application **/
    @SuppressWarnings("unchecked")
	public static void main(String[] args){
        if (args.length != 1) {
            usage();
        }
        ImportsExtractor app = new ImportsExtractor();
        Model m = ModelFactory.createDefaultModel();
        m.read(args[0]);
        Vector<String> res = (Vector<String>) app.extract(m, null);
        if (res == null || res.size()==0) {
            System.out.println("Error= "+app.errorMessage);
            System.exit(-1);
        } else for(String s : res) System.out.println(s);
    }
    
    private static void usage(){
        System.err.println("Usage: java ImportsExtractor <http://url.of.the.ontology>");
        System.exit(-1);
    }

}
