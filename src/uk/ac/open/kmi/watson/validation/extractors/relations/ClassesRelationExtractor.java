/*
 * ClassesRelationExtractor.java
 *
 * Created on 12 April 2007, 16:41
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package uk.ac.open.kmi.watson.validation.extractors.relations;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.vocabulary.OWL;
import com.hp.hpl.jena.vocabulary.RDFS;
import java.util.Vector;

import uk.ac.open.kmi.watson.validation.utils.V_CONSTANTS;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.vocabulary.DAML_OIL;

/**
 * @author cb7224, mda99
 */
public class ClassesRelationExtractor extends EntityRelationExtractor{
  
    public Object extract(Model model, String documentID){
        Vector<EntityRelationDataStructure> result = new Vector<EntityRelationDataStructure>();
        if (model == null) {
            errorMessage = "cannot deal with null models";
            return null;
        }
        result.addAll( findSubClassOf(model) );
        result.addAll( findOWLDisjointWith(model) );
        result.addAll( findDAMLDisjointWith(model) );  
        result.addAll( findOWLEquivalentClass(model) );
        result.addAll( findDAMLEquivalentClass(model) );
        errorMessage = null;
        return result;
    }
    
    
    private Vector<EntityRelationDataStructure> findSubClassOf(Model model){
    	StmtIterator stmtIt;
    	Vector<EntityRelationDataStructure> tempResult = new Vector<EntityRelationDataStructure>();
    	int relationType; 	  
    	stmtIt = model.listStatements(new PredicateSelector(RDFS.subClassOf));
        relationType = V_CONSTANTS.SUB_CLASS_OF;
        tempResult = getTripleUris(stmtIt, relationType);
        stmtIt.close();
        return tempResult;
    }
    
    private Vector<EntityRelationDataStructure> findOWLDisjointWith(Model model){
    	StmtIterator stmtIt;
    	Vector<EntityRelationDataStructure> tempResult = new Vector<EntityRelationDataStructure>();
    	int relationType;
    	stmtIt = model.listStatements(new PredicateSelector(OWL.disjointWith));
        relationType = V_CONSTANTS.DISJOINT_WITH;
        tempResult = getTripleUris(stmtIt, relationType);
        stmtIt.close();
        return tempResult;
    }
   
    private Vector<EntityRelationDataStructure> findDAMLDisjointWith(Model model){
    	StmtIterator stmtIt;
    	Vector<EntityRelationDataStructure> tempResult = new Vector<EntityRelationDataStructure>();
    	stmtIt = model.listStatements(new PredicateSelector(DAML_OIL.disjointWith));
        int relationType = V_CONSTANTS.DISJOINT_WITH;
        tempResult = getTripleUris(stmtIt, relationType);
        stmtIt.close();
        return tempResult;
    }
     
    private Vector<EntityRelationDataStructure> findOWLEquivalentClass(Model model){
    	StmtIterator stmtIt;
    	Vector<EntityRelationDataStructure> tempResult = new Vector<EntityRelationDataStructure>();
    	stmtIt = model.listStatements(new PredicateSelector(OWL.equivalentClass));
        int relationType = V_CONSTANTS.EQUIVALENT;
        tempResult = getTripleUris(stmtIt, relationType);
        stmtIt.close();
        return tempResult;
    }
    
    private Vector<EntityRelationDataStructure> findDAMLEquivalentClass(Model model){
    	StmtIterator stmtIt;
    	Vector<EntityRelationDataStructure> tempResult = new Vector<EntityRelationDataStructure>();
    	stmtIt = model.listStatements(new PredicateSelector(DAML_OIL.equivalentTo));
        int relationType = V_CONSTANTS.EQUIVALENT;
        tempResult = getTripleUris(stmtIt, relationType);
        stmtIt.close();
        return tempResult;
    }

}
