/*
 * OWLSpecieExtractor.java
 *
 * Created on 20 April 2007, 14:18
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package uk.ac.open.kmi.watson.validation.extractors;

import com.hp.hpl.jena.graph.Graph;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;

import org.mindswap.pellet.jena.PelletInfGraph;
import org.mindswap.pellet.jena.PelletReasoner;
import org.mindswap.pellet.jena.PelletReasonerFactory;
import uk.ac.open.kmi.watson.validation.Extractor;
import uk.ac.open.kmi.watson.validation.utils.V_CONSTANTS;

/**
 *
 * @author cb7224, mda99
 */
public class OWLSpeciesExtractor extends Extractor{
    
    /** Creates a new instance of OWLSpecieExtractor */
    public OWLSpeciesExtractor() {
    }
    
    public Object extract(Model model, String documentID) {
        int result = getOWLSubLanguage(model);
        if (result==-1) return null;
    	return result;
    }
    
    private int getOWLSubLanguage(Model model) {
    	int specie = V_CONSTANTS.NOT_OWL_LANGUAGE;
    	if (model == null) {
			errorMessage = "cannot deal with null models";
			return -1;
		}
        Graph gr = model.getGraph();
        PelletReasoner pr = new PelletReasoner(gr , PelletReasonerFactory.theInstance());
        PelletInfGraph pg = new PelletInfGraph(gr , pr);
        
        pg.prepare();
        String level = pg.getOWLReasoner().getSpecies().toString();
        
        if (level.toLowerCase().contains("lite"))
            specie = V_CONSTANTS.OWL_LITE;
        if (level.toLowerCase().contains("dl"))
            specie = V_CONSTANTS.OWL_DL;
        if (level.toLowerCase().contains("full"))
            specie = V_CONSTANTS.OWL_FULL;
        
        return specie;
    }
    

    /** for test and use as stand-alone application **/
	public static void main(String[] args){
		if (args.length != 1) {
			usage(); 
		}
		OWLSpeciesExtractor app = new OWLSpeciesExtractor();
		Model m = ModelFactory.createDefaultModel();
		m.read(args[0]);
		Integer expr = (Integer) app.extract(m, null);
		System.out.println(expr);
	}


	private static void usage(){
		System.err.println("Usage: java OWLSpeciesExtractor <http://url.of.the.ontology>");
		System.exit(-1);
	}
	
    
}
