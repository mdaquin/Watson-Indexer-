package uk.ac.open.kmi.watson.validation.extractors.entities;

import java.util.Vector;

import uk.ac.open.kmi.watson.validation.Extractor;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.util.iterator.ExtendedIterator;
import com.hp.hpl.jena.vocabulary.DAML_OIL;
import com.hp.hpl.jena.vocabulary.OWL;
import com.hp.hpl.jena.vocabulary.RDF;
import com.hp.hpl.jena.vocabulary.RDFS;

/** TODO: Change, remove everything that is not for extracting declared classes!
 * 
 * @author mda99, cb??
 */
public class ClassesExtractor extends Extractor {
    /**
     * Returns a Vector<String> with the URIs of the classes contained in the ontology
     */
    public Object extract(Model model, String documentID) {
        if (model == null) {
            errorMessage = "cannot deal with null models";
            return null;
        }
        Vector<String> result = new Vector<String>();
        ExtendedIterator classes = 
                model.listSubjectsWithProperty(RDF.type, RDFS.Class)
                .andThen(model.listSubjectsWithProperty(RDF.type, DAML_OIL.Class))
                .andThen(model.listSubjectsWithProperty(RDF.type, OWL.Class))
                .andThen(model.listSubjectsWithProperty(RDF.type, OWL.Restriction))
                .andThen(model.listSubjectsWithProperty(RDF.type, DAML_OIL.Restriction));
        while (classes.hasNext()) {
            Resource classRes = (Resource)classes.next();
            // set the ns used to declare ontological entity belonging to this schema
            // baseNS = ModelUtils.getNamespace(classRes.getURI());
            if (!classRes.isAnon() && !classRes.isLiteral()) {
                if (!result.contains(classRes.getURI())) result.add(classRes.getURI());
            }
        }
        classes.close();
        errorMessage=null;
        return result;
    }
    
  /*
    private static Vector<String> getDeclaredClasses(String documentID){
//         to test without DB, and for reusability
//    	if (documentID == null) return (Vector<String>) new ClassesExtractor().extract(model, null);
        Vector<String> temp = new Vector<String>();
        Factory factory = new Factory();
        factory.beginTransaction();
        SemanticContent sc = factory.getDocument(documentID).semanticContent();
        Iterator entities = sc.listEntities();
        while (entities.hasNext()) {
            Entity e = (Entity) entities.next();
            if(e.type() == V_CONSTANTS.CLASS_ENTITY){
                temp.add(e.URI());
                registerDiscoveredClass(e.URI());
            }
        }
        factory.commitTransaction();
        return temp;
    }
    
    private static Vector<String> getReferredClasses(Model model, Property property , String base) {
        Resource clazz;
        Statement s;
        Vector<String> temp = new Vector<String>();
        ArrayList<String> NScollection = getNScollection(model , base);
        StmtIterator referredClasses= model.listStatements(new SelectStatementOnNameSpace(NScollection, property));
        while (referredClasses.hasNext()) {
            s = referredClasses.nextStatement();
            clazz = (Resource)s.getObject();
            try {
                if(!clazz.isAnon()) {
                    temp.add(clazz.getURI() );
                    registerDiscoveredClass(clazz.getURI());
                }
            } catch (Exception e) {e.printStackTrace();}//catch the exception of a problem experiencied in writing an entity on the DB to prevent to skip the whole document
        }
        referredClasses.close();
        NScollection.clear();
        return temp;
    }
    
    public static Vector<String> getReferredClassesForIndividuals(Model model, String base){
        resourceCollection.clear();
        return getReferredClasses(model, RDF.type, base);
    }
    
    public static Vector<String> getReferredClassesForRange(Model model, String base){
        resourceCollection.clear();
        return getReferredClasses(model, RDFS.range, base);
    }
    
    public static Vector<String> getReferredClassesForDomain(Model model, String base){
        resourceCollection.clear();
        return getReferredClasses(model, RDFS.domain, base);
    }
    public static Vector<String> getReferredClassesForSubClass(Model model, String base){
        resourceCollection.clear();
        return getReferredClasses(model, RDFS.subClassOf, base);
    }
    
    public static Vector getAllClasses(Model model, String documentID, int relationConstantValue){
        Property p;
        switch(relationConstantValue){
            case V_CONSTANTS.TYPE_OF : p = RDF.type; break;
            case V_CONSTANTS.SUB_CLASS_OF : p = RDFS.subClassOf; break;
            case V_CONSTANTS.RANGE : p = RDFS.range; break;
            case V_CONSTANTS.DOMAIN : p = RDFS.domain; break;
            default : p = null;
        }
        Vector classes = new Vector();
        classes.addAll(getDeclaredClasses(documentID));
        classes.addAll(getReferredClasses(model, p, baseNS));
        return classes;
    }

    private static void registerDiscoveredClass(String uri){
        resourceCollection.put(uri, uri);
    }
    */
    
    /** for test and use as stand-alone application **/
    public static void main(String[] args){
        if (args.length != 2) {
            usage();
        }
        ClassesExtractor app = new ClassesExtractor();
        Model m = ModelFactory.createDefaultModel();
        m.read(args[1]);
        Vector<String> classes = (Vector<String>) app.extract(m, null);
        if (args[0].equals("-l")){
            for(String s : classes) System.out.println(s);
        } else if (args[0].equals("-n")){
            classes=restrictToLocalName(classes);
            for(String s : classes) System.out.println(s);
        } else if (args[0].equals("-c")){
            System.out.println(classes.size());
        } else usage();
    }
    
    private static Vector<String> restrictToLocalName(Vector<String> classes) {
        Vector<String> results = new Vector<String>(classes.size());
        for(String c : classes){
            results.add(c.substring(c.indexOf("#")+1, c.length()));
        }
        return results;
    }
    
    private static void usage(){
        System.err.println("Usage: java ClassExtractor <-l|-n|-c> <http://url.of.the.ontology>\n-l means list the classes, -n list the local names, and -c count them.");
        System.exit(-1);
    }
    
}
