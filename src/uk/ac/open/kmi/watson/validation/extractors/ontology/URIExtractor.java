package uk.ac.open.kmi.watson.validation.extractors.ontology;


import uk.ac.open.kmi.watson.validation.Extractor;
import uk.ac.open.kmi.watson.validation.utils.ModelUtils;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Resource;

/**
 * Extract the URI of an ontology if it is declared (in the ontology header or the xml:base)
 * @author mda99, cb??
 */
public class URIExtractor extends Extractor {
	
	/** returns a String with the URI or null 
	 *  work with base as well??? **/
	public Object extract(Model m, String documentID){
		if (m==null) {errorMessage="cannot deal with null models"; return null;}
		Resource o = null;
		try{
		 o = ModelUtils.getOntology(m);
		} catch (Exception e){ e.printStackTrace(); return null; }
		if (o==null){return null;}
		if (o.getURI()!=null) {errorMessage=null; return o.getURI();}
		return null;
	}
	
	 /** for test and use as stand-alone application **/
	public static void main(String[] args){
		if (args.length != 1) {
			usage(); 
		}
		URIExtractor app = new URIExtractor();
		Model m = ModelFactory.createDefaultModel();
		m.read(args[0]);
		String uri = (String) app.extract(m, null);
		if (uri == null) {
			System.out.println("Error= "+app.errorMessage);
			System.exit(-1);
		}
		System.out.println(uri);
	}
	
	private static void usage(){
		System.err.println("Usage: java URIExtractor <http://url.of.the.ontology>");
		System.exit(-1);
	}
	
}
