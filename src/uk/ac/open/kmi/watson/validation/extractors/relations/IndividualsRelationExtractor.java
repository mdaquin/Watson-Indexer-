/*
 * IndividualsRelationExtractor.java
 *
 * Created on 12 April 2007, 17:34
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package uk.ac.open.kmi.watson.validation.extractors.relations;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.vocabulary.DAML_OIL;
import com.hp.hpl.jena.vocabulary.OWL;
import java.util.Vector;
//import uk.ac.open.kmi.watson.validation.EntityExtractor;
import uk.ac.open.kmi.watson.validation.utils.V_CONSTANTS;

/**
 * @author cb7224, mda99
 */
public class IndividualsRelationExtractor extends EntityRelationExtractor{
    
    public Object extract(Model model, String documentID){
        Vector<EntityRelationDataStructure> result = new Vector<EntityRelationDataStructure>();
        if (model == null) {
            errorMessage = "cannot deal with null models";
            return null;
        }
        result.addAll( findOWLDifferentFrom(model) );
        result.addAll( findDAMLDifferentFrom(model) );
        result.addAll( findOWLSameAs(model) );
        result.addAll( findDAMLSameAs(model) );
        errorMessage = null;
        return result;
    }
    
    private Vector<EntityRelationDataStructure> findOWLDifferentFrom(Model model){
        StmtIterator stmtIt;
        Vector<EntityRelationDataStructure> tempResult = new Vector<EntityRelationDataStructure>();
        int relationType;
        stmtIt = model.listStatements(new PredicateSelector(OWL.differentFrom));
        relationType = V_CONSTANTS.DIFFERENT_FROM;
        tempResult = getTripleUris(stmtIt, relationType);
        stmtIt.close();
        return tempResult;
    }
    
    private Vector<EntityRelationDataStructure> findDAMLDifferentFrom(Model model){
        StmtIterator stmtIt;
        Vector<EntityRelationDataStructure> tempResult = new Vector<EntityRelationDataStructure>();
        int relationType;
        stmtIt = model.listStatements(new PredicateSelector(DAML_OIL.differentIndividualFrom));
        relationType = V_CONSTANTS.DIFFERENT_FROM;
        tempResult = getTripleUris(stmtIt, relationType);
        stmtIt.close();
        return tempResult;
    }
    
    private Vector<EntityRelationDataStructure> findOWLSameAs(Model model){
        StmtIterator stmtIt;
        Vector<EntityRelationDataStructure> tempResult = new Vector<EntityRelationDataStructure>();
        int relationType;
        stmtIt = model.listStatements(new PredicateSelector(OWL.sameAs));
        relationType = V_CONSTANTS.SAME_AS;
        tempResult = getTripleUris(stmtIt, relationType);
        stmtIt.close();
        return tempResult;
    }
    
    private Vector<EntityRelationDataStructure> findDAMLSameAs(Model model){
        StmtIterator stmtIt;
        Vector<EntityRelationDataStructure> tempResult = new Vector<EntityRelationDataStructure>();
        int relationType;
        stmtIt = model.listStatements(new PredicateSelector(DAML_OIL.sameIndividualAs));
        relationType = V_CONSTANTS.SAME_AS;
        tempResult = getTripleUris(stmtIt, relationType);
        stmtIt.close();
        return tempResult;
    }
    
    
    
}
