package uk.ac.open.kmi.watson.validation.extractors.entities;

// TODO: should store the types of the entity

// import java.util.Hashtable;
import java.util.Vector;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.util.iterator.ExtendedIterator;
import com.hp.hpl.jena.vocabulary.DAML_OIL;
import com.hp.hpl.jena.vocabulary.OWL;
import com.hp.hpl.jena.vocabulary.RDF;
//import uk.ac.open.kmi.watson.validation.EntityExtractor;
import uk.ac.open.kmi.watson.validation.Extractor;
import uk.ac.open.kmi.watson.validation.utils.ModelUtils;

public class PropertiesExtractor extends Extractor {
    
	// private Hashtable<String, String> propertiesCollection = new Hashtable();
    
    /**
     * Returns a Vector<String> with the URIs of the properties contained in the ontology
     */
    public Object extract(Model model, String documentID) {
        if (model == null) {
            errorMessage = "cannot deal with null models";
            return null;
        }
        Vector<String> result = new Vector<String>();
        ExtendedIterator properties =
                model.listSubjectsWithProperty(RDF.type, RDF.Property)
                .andThen(model.listSubjectsWithProperty(RDF.type, OWL.DatatypeProperty))
                .andThen(model.listSubjectsWithProperty(RDF.type, OWL.ObjectProperty))
                .andThen(model.listSubjectsWithProperty(RDF.type, OWL.TransitiveProperty))
                .andThen(model.listSubjectsWithProperty(RDF.type, OWL.FunctionalProperty))
                .andThen(model.listSubjectsWithProperty(RDF.type, OWL.InverseFunctionalProperty))
                .andThen(model.listSubjectsWithProperty(RDF.type, OWL.SymmetricProperty))
                .andThen(model.listSubjectsWithProperty(RDF.type, DAML_OIL.DatatypeProperty))
                .andThen(model.listSubjectsWithProperty(RDF.type, DAML_OIL.ObjectProperty))
                .andThen(model.listSubjectsWithProperty(RDF.type, DAML_OIL.Property))
                .andThen(model.listSubjectsWithProperty(RDF.type, DAML_OIL.TransitiveProperty))
                .andThen(model.listSubjectsWithProperty(RDF.type, DAML_OIL.UnambiguousProperty))
                .andThen(model.listSubjectsWithProperty(RDF.type, DAML_OIL.UniqueProperty));
        while (properties.hasNext()) {
            Resource propRes = (Resource)properties.next();
            if (!propRes.isAnon() && !propRes.isLiteral()) {
                String propertyUri = propRes.getURI();
                //set the ns used to declare ontological entity belonging to this schema
                // baseNS = ModelUtils.getNamespace(propertyUri);
                // if (!propertiesCollection.contains(propertyUri)) {
                    result.add(propertyUri);
                  //   registerDiscoveredProperty(propertyUri);
                //}
            }
        }
        properties.close();
        errorMessage = null;
        return result;
    }
    
  /*  private void registerDiscoveredProperty(String uri) {
        propertiesCollection.put(uri, uri);
    } */
    
    /** for test and use as stand-alone application **/
    @SuppressWarnings("unchecked")
	public static void main(String[] args){
        if (args.length != 2) {
            usage();
        }
        PropertiesExtractor app = new PropertiesExtractor();
        Model m = ModelFactory.createDefaultModel();
        m.read(args[1]);
        Vector<String> properties = (Vector<String>) app.extract(m, null);
        if (args[0].equals("-l")){
            for(String s : properties) System.out.println(s);
        } else if (args[0].equals("-c")){
            System.out.println(properties.size());
        } else usage();
    }
    
    private static void usage(){
        System.err.println("Usage: java PropertiesExtractor <-l|-c> <http://url.of.the.ontology>\n-l means list the classes and -c count them.");
        System.exit(-1);
    }
}
