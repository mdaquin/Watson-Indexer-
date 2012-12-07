/*
 * PropertiesRelationExtractor.java
 *
 * Created on 12 April 2007, 14:01
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package uk.ac.open.kmi.watson.validation.extractors.relations;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.vocabulary.RDFS;
import java.util.Vector;
//import uk.ac.open.kmi.watson.validation.EntityExtractor;
import uk.ac.open.kmi.watson.validation.utils.V_CONSTANTS;

/**
 * @author cb7224, mda99
 */
public class PropertiesRelationExtractor extends EntityRelationExtractor {
    
    public Object extract(Model model, String documentID) {
        Vector<EntityRelationDataStructure> result = new Vector<EntityRelationDataStructure>();
        if (model == null) {
            errorMessage = "cannot deal with null models";
            return null;
        }
        result.addAll( findDomain(model) );
        result.addAll( findRange(model) );
        result.addAll( findSubProperyOf(model) );
        errorMessage = null;
        return result;
    }
    
    private Vector<EntityRelationDataStructure> findDomain(Model model){
    	StmtIterator stmtIt;
    	Vector<EntityRelationDataStructure> tempResult = new Vector<EntityRelationDataStructure>();
    	int relationType; 	
    	stmtIt = model.listStatements(new PredicateSelector(RDFS.domain));
        relationType = V_CONSTANTS.DOMAIN;
        tempResult = getTripleUris(stmtIt, relationType);
        stmtIt.close();
        return tempResult;
    }
    
    private Vector<EntityRelationDataStructure> findRange(Model model){
    	StmtIterator stmtIt;
    	Vector<EntityRelationDataStructure> tempResult = new Vector<EntityRelationDataStructure>();
    	int relationType; 	
    	stmtIt = model.listStatements(new PredicateSelector(RDFS.range));
        relationType = V_CONSTANTS.RANGE;
        tempResult = getTripleUris(stmtIt, relationType);
        stmtIt.close();
        return tempResult;
    }
    
    
    private Vector<EntityRelationDataStructure> findSubProperyOf(Model model){
    	StmtIterator stmtIt;
    	Vector<EntityRelationDataStructure> tempResult = new Vector<EntityRelationDataStructure>();
    	int relationType; 	
    	stmtIt = model.listStatements(new PredicateSelector(RDFS.subPropertyOf));
        relationType = V_CONSTANTS.SUBPROPERTY_OF;
        tempResult = getTripleUris(stmtIt, relationType);
        stmtIt.close();
        return tempResult;
    }
}
