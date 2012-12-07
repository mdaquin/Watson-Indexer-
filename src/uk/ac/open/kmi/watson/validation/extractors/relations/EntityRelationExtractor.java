package uk.ac.open.kmi.watson.validation.extractors.relations;

import java.util.Vector;

import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.SimpleSelector;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;

import uk.ac.open.kmi.watson.validation.Extractor;

/**
 * Common useful method for extracting entity relations...
 * 
 */
public abstract class EntityRelationExtractor extends Extractor {

	 //selects only the statements that have the specified property as predicate
    protected static class PredicateSelector extends SimpleSelector{
        Property property;
        public PredicateSelector(Property p ){
            property = p;
        }
        public boolean selects(Statement s) {
            return s.getPredicate().equals(property);
        }
    }
    
    protected Vector<EntityRelationDataStructure> getTripleUris(StmtIterator si, int relationType){
        Statement st;
        Resource sbj;
        RDFNode obj;
        Vector<EntityRelationDataStructure> temp = new Vector<EntityRelationDataStructure>();
        while (si.hasNext()) {
            st = si.nextStatement();
            sbj = st.getSubject();
            obj = st.getObject();
            if (!sbj.isAnon() & !obj.isAnon()){
                EntityRelationDataStructure er = new EntityRelationDataStructure();
                er.sbjUri = sbj.getURI();
                er.objUri = ((Resource)obj).getURI();
                er.pptUri = st.getPredicate().getURI();
                er.type = relationType;
                temp.add(er);
            }
        }return temp;
    }
	
	
	
}
