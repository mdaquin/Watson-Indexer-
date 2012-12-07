package uk.ac.open.kmi.watson.validation.extractors;


import java.util.Iterator;

import org.mindswap.pellet.jena.OWLReasoner;
import org.mindswap.pellet.jena.PelletInfGraph;
import org.mindswap.pellet.jena.PelletReasonerFactory;

import uk.ac.open.kmi.watson.validation.Extractor;
import aterm.ATermAppl;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;

/**
 * Watson validation extractor: count the unsatisfiable concepts in an ontology, using the Pellet reasoner.
 * @author mda99
 */
public class UnsatisfiableConceptsExtractor extends Extractor{
	
	/** returns an Integer corresponding to the number of unsatisfiable concepts in the model 
	 * 
	 * **/
	public Object extract(Model m, String documentID){
		if (m==null) {errorMessage = "Cannot work with a null model"; return null;}
		try{
	    	OntModelSpec mySpec = PelletReasonerFactory.THE_SPEC;
			OntModel result = ModelFactory.createOntologyModel(mySpec,m);
			if (result==null) {this.errorMessage="cannot get the OntModel"; return null;}
			result.prepare();
			OWLReasoner reasoner = ((PelletInfGraph) result.getGraph()).getOWLReasoner();
			Iterator i = reasoner.getKB().getClasses().iterator();
			int count = 0;
			while (i.hasNext()) {
				ATermAppl c = (ATermAppl) i.next();
				if(!reasoner.getKB().isSatisfiable(c)) count++;
			}
			errorMessage=null;
			return new Integer(count);
		}
		catch(Exception e){
			errorMessage="cannot get the ontModel: "+e;
			return null;
		}
	}
}