/*
 * LiteralExtractor.java
 *
 * Created on 13 April 2007, 18:33
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package uk.ac.open.kmi.watson.validation.extractors.literals;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.SimpleSelector;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import java.util.Vector;
import uk.ac.open.kmi.watson.validation.Extractor;
import uk.ac.open.kmi.watson.validation.utils.LiteralFilteredOutNs;
import uk.ac.open.kmi.watson.validation.utils.ModelUtils;

/**
 * TODO: need checking and refinment 
 * @author cb7224
 */
public class LiteralExtractor extends Extractor{
    
    public Object extract(Model model, String documentID) {
        Vector<LiteralDataStructure> result = new Vector<LiteralDataStructure>();
        if (model == null) {
            errorMessage = "cannot deal with null models";
            return null;
        }
        result.addAll( findLiteralRelation(model) );
        errorMessage = null;
        return result;
    }

    private Vector<LiteralDataStructure> findLiteralRelation(Model model) {
    	Vector<LiteralDataStructure> tempLiteralResult = new Vector<LiteralDataStructure>();
    	StmtIterator stmtIt;
    	stmtIt = model.listStatements(new LiteralValueSelector());
        tempLiteralResult = getLiteralRelation(stmtIt);
        stmtIt.close();
        return tempLiteralResult;
    }
    
    protected Vector<LiteralDataStructure> getLiteralRelation(StmtIterator si){
        Statement st;
        Resource sbj;
        RDFNode obj;
        com.hp.hpl.jena.rdf.model.Literal literal;
        Vector<LiteralDataStructure> temp = new Vector<LiteralDataStructure>();
        while (si.hasNext()) {
            LiteralDataStructure l = new LiteralDataStructure();
            st = si.nextStatement();
            sbj = st.getSubject();
            obj = st.getObject();
            if (!sbj.isAnon()){
                literal = (com.hp.hpl.jena.rdf.model.Literal)obj;
                l.sbjUri = sbj.getURI();
                l.pptUri = st.getPredicate().getURI();
                l.value = literal.getLexicalForm();
                l.language = literal.getLanguage();
                temp.add(l);
            }
        }return temp;
    }
    
    private class LiteralValueSelector extends SimpleSelector{
        public LiteralValueSelector(){}
        public boolean selects(Statement s) {
            return s.getObject().isLiteral() && !s.getSubject().isAnon() && namespaceFilter(s.getSubject());
        }
        
        //Filter out literal statements over resources' namespace declared unwanted
        private boolean namespaceFilter(Resource r){
            // if (r.getURI()==null) return true;
            String rNS = ModelUtils.getNamespace( r.getURI() );
            return !LiteralFilteredOutNs.contains(rNS);
        }
    }
   
    
    /** for test and use as stand-alone application **/
	public static void main(String[] args){
		if (args.length != 1) {
			usage(); 
		}
		LiteralExtractor app = new LiteralExtractor();
		Model m = ModelFactory.createDefaultModel();
		m.read(args[0]);
		Vector<LiteralDataStructure> lits = (Vector<LiteralDataStructure>) app.extract(m, null);
		for(LiteralDataStructure l : lits) System.out.println(l);
	}

	private static void usage(){
		System.err.println("Usage: java LiteralExtractor  <http://url.of.the.ontology>");
		System.exit(-1);
	}
    
}
