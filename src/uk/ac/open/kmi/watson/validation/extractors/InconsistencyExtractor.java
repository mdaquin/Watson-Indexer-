package uk.ac.open.kmi.watson.validation.extractors;

import org.mindswap.pellet.jena.OWLReasoner;
import org.mindswap.pellet.jena.PelletInfGraph;
import org.mindswap.pellet.jena.PelletReasonerFactory;

import uk.ac.open.kmi.watson.validation.Extractor;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;

/**
 * Watson validation extractor: indicate (boolean) if an ontology is consistent or not, using the Pellet reasoner.
 * @author mda99
 */
public class InconsistencyExtractor extends Extractor{
	
	/** return Boolean(true) if the model is inconsistent, Boolean(false) if it is consistent. **/
	public Object extract(Model m, String documentID){
		if (m==null) {errorMessage = "Cannot work with a null model"; return null;}
		try{
	    	OntModelSpec mySpec = PelletReasonerFactory.THE_SPEC;
			OntModel result = ModelFactory.createOntologyModel(mySpec,m);
			if (result==null) {this.errorMessage="cannot get the OntModel"; return null;}
			result.prepare();
			OWLReasoner reasoner = ((PelletInfGraph) result.getGraph()).getOWLReasoner();
			if (reasoner.isConsistent()) {
				errorMessage=null;
				return new Boolean(true);
			}
			else {
				errorMessage=null;
				return new Boolean(false);
			}
	    	}
			catch(Exception e){
				errorMessage="cannot get the ontModel: "+e;
				return null;
			}
	}
	

	/** for test and use as stand-alone application **/
	public static void main(String[] args){
		if (args.length != 1) {
			usage(); 
		}
		InconsistencyExtractor app = new InconsistencyExtractor();
		Model m = ModelFactory.createDefaultModel();
		m.read(args[0]);
		String incon = (String) app.extract(m, null);
		System.out.println(incon);
	}

	private static void usage(){
		System.err.println("Usage: java InconsistencyExtractor <http://url.of.the.ontology>");
		System.exit(-1);
	}
	
	
}