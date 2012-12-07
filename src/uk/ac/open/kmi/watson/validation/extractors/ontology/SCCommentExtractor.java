package uk.ac.open.kmi.watson.validation.extractors.ontology;

import com.hp.hpl.jena.rdf.model.Literal;
import java.util.Vector;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.vocabulary.RDFS;

import uk.ac.open.kmi.watson.validation.Extractor;
import uk.ac.open.kmi.watson.validation.extractors.literals.LiteralDataStructure;
import uk.ac.open.kmi.watson.validation.utils.ModelUtils;

/**
 * extract the comments in the ontology header of a model if they are some.
 * @author mda99, cb??
 *
 */
public class SCCommentExtractor extends Extractor {
    
    /**
     * Returns a Vector<LiteralDataStructure> for the comments of the ontology *
     */
    public Object extract(Model m, String documentID){
        if ( m == null ){
            errorMessage="cannot deal with null models";
            return null;
        }
        Resource o = ModelUtils.getOntology(m);
        if ( o == null ){
            return new Vector();
        }
        Vector<LiteralDataStructure> results = new Vector<LiteralDataStructure>();
        StmtIterator it  = o.listProperties(RDFS.comment);
        while (it.hasNext()) {
            try {
        	Statement st = it.nextStatement();
            Literal comment = (Literal)st.getObject();
            LiteralDataStructure l = new LiteralDataStructure();
            l.sbjUri = st.getSubject().getURI();
            l.pptUri = RDFS.comment.getURI();
            l.value = comment.getLexicalForm();
            l.language = comment.getLanguage();
            results.add(l);
            } catch (Exception e){
            	errorMessage = e.getMessage();
            	// e.printStackTrace();
            }
        }
        it.close();
        errorMessage = null;
        return results;
    }
    
    /** for test and use as stand-alone application **/
    @SuppressWarnings("unchecked")
	public static void main(String[] args){
        if (args.length != 1) {
            usage();
        }
        SCCommentExtractor app = new SCCommentExtractor();
        Model m = ModelFactory.createDefaultModel();
        m.read(args[0]);
        Vector<LiteralDataStructure> comments = (Vector<LiteralDataStructure>) app.extract(m, null);
        if (comments == null) {
            System.out.println("Error= "+app.errorMessage);
            System.exit(-1);
        }
        for (LiteralDataStructure l : comments){
            System.out.println(l.value+" ("+l.language+")");
        }
    }
    
    private static void usage(){
        System.err.println("Usage: java SCCommentExtractor <http://url.of.the.ontology>");
        System.exit(-1);
    }
}