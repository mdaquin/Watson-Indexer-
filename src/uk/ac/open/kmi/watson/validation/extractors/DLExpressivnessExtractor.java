/*
 * DLexpressivityExtractor.java
 *
 * Created on 20 April 2007, 14:19
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package uk.ac.open.kmi.watson.validation.extractors;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.util.iterator.ExtendedIterator;
import com.hp.hpl.jena.vocabulary.DAML_OIL;
import com.hp.hpl.jena.vocabulary.OWL;
import com.hp.hpl.jena.vocabulary.RDF;
import com.hp.hpl.jena.vocabulary.RDFS;

import uk.ac.open.kmi.watson.validation.Extractor;

/**
 *
 * @author cb7224, mda99
 */
public class DLExpressivnessExtractor extends Extractor{
    
    public Object extract(Model model, String documentID) {
    		boolean hasIntersection = true; // we assume all ontologies habe intersection
    		boolean hasUnion = false; // if UnionOf
    		boolean hasValueRestriction = false; // if allValuesFrom, or range and domain
    		boolean hasExistentialQuantifier = false; // if someValuesFrom
    		boolean hasRoleInclusion = false; // if subProperty (equivalentProperty?)
    		boolean hasNegation = false; // if ComplementOf or disjointWith
    		boolean hasNominal = false; // if hasValue or oneOf
    		boolean hasTransitiveRole = false; // if transitiveProperty
    		boolean hasDatatype = false; // if literals, datatypes or datatypeProperties
    		boolean hasInverseRole = false; // if inverse properties
    		boolean hasQualifiedNumberRestriction = false; // cannot happen with OWL but can with DAML
    		boolean hasUnqualifiedNumberRestriction = false; // if someValuesFrom
    		boolean hasFuntionalNumberRestriction = false; // if cardinality = 1
            // Union
    		ExtendedIterator sts = 
            	model.listSubjectsWithProperty(OWL.unionOf)
                .andThen(model.listSubjectsWithProperty(DAML_OIL.unionOf))
                .andThen(model.listSubjectsWithProperty(DAML_OIL.disjointUnionOf));
    		if (sts.hasNext()) hasUnion = true;sts.close();
    		// domain and range???
    		// ValueRestriction
    		sts = 
            	model.listSubjectsWithProperty(OWL.allValuesFrom)
                .andThen(model.listSubjectsWithProperty(DAML_OIL.toClass))
                .andThen(model.listSubjectsWithProperty(RDFS.domain)) // because it is translated
                .andThen(model.listSubjectsWithProperty(RDFS.range)); // into a forall
            if (sts.hasNext()) hasValueRestriction = true;sts.close();
            // Existantial Quantifier
    		sts = 
            	model.listSubjectsWithProperty(OWL.someValuesFrom)
                .andThen(model.listSubjectsWithProperty(DAML_OIL.hasClass));
            if (sts.hasNext()) hasExistentialQuantifier = true;sts.close();
            // Role inclusion
    		sts = 
            	model.listSubjectsWithProperty(RDFS.subPropertyOf)
                .andThen(model.listSubjectsWithProperty(DAML_OIL.subPropertyOf));
            if (sts.hasNext()) hasRoleInclusion = true;sts.close();
//         negation
    		sts = 
            	model.listSubjectsWithProperty(OWL.complementOf)
                .andThen(model.listSubjectsWithProperty(DAML_OIL.complementOf))
                .andThen(model.listSubjectsWithProperty(DAML_OIL.disjointWith))
                .andThen(model.listSubjectsWithProperty(DAML_OIL.disjointUnionOf))
                .andThen(model.listSubjectsWithProperty(OWL.disjointWith));
            if (sts.hasNext()) hasNegation = true;sts.close();
//          nominals
    		sts = 
            	model.listSubjectsWithProperty(OWL.hasValue)
                .andThen(model.listSubjectsWithProperty(DAML_OIL.hasValue))
                .andThen(model.listSubjectsWithProperty(DAML_OIL.oneOf))
                .andThen(model.listSubjectsWithProperty(OWL.oneOf));
            if (sts.hasNext()) hasNominal = true; sts.close();
//          transitive roles
    		sts = 
            	model.listSubjectsWithProperty(RDF.type, OWL.TransitiveProperty)
                .andThen(model.listSubjectsWithProperty(RDF.type, DAML_OIL.TransitiveProperty));
            if (sts.hasNext()) hasTransitiveRole = true; sts.close();
//          inverse roles
    		sts = 
            	model.listSubjectsWithProperty(OWL.inverseOf)
                .andThen(model.listSubjectsWithProperty(DAML_OIL.inverseOf));
            if (sts.hasNext()) hasInverseRole = true; sts.close();
            // Unqualifed number restriction
            sts = 
            	model.listSubjectsWithProperty(OWL.maxCardinality)
                .andThen(model.listSubjectsWithProperty(OWL.minCardinality))
            	.andThen(model.listSubjectsWithProperty(OWL.cardinality))
            	.andThen(model.listSubjectsWithProperty(DAML_OIL.maxCardinality))
                .andThen(model.listSubjectsWithProperty(DAML_OIL.minCardinality))
                .andThen(model.listSubjectsWithProperty(DAML_OIL.cardinality));
            if (sts.hasNext()) hasUnqualifiedNumberRestriction = true; sts.close();
//          qualifed number restriction (only daml+oil)
            sts = 
            	model.listSubjectsWithProperty(DAML_OIL.maxCardinalityQ)
                .andThen(model.listSubjectsWithProperty(DAML_OIL.minCardinalityQ))
                .andThen(model.listSubjectsWithProperty(DAML_OIL.cardinalityQ));
            if (sts.hasNext()) hasQualifiedNumberRestriction = true; sts.close();
            // TODO: add functional number restriction
            if (hasValueRestriction && hasNegation) hasExistentialQuantifier = true;
            if (hasExistentialQuantifier && hasNegation) hasValueRestriction = true;
            if (hasIntersection && hasNegation) hasUnion = true;
            if (hasUnion && hasNegation) hasIntersection = true;
            
            String result = "";
            if (hasIntersection && hasValueRestriction) result +="AL";
            else if (hasIntersection && hasExistentialQuantifier) result+="EL";
            if (hasNegation) result+="C";
            if (hasTransitiveRole) result+="R+";
            if (result.equals("ALCR+")) result = "S";
            // TODO: why omiting U??
            if (hasRoleInclusion) result+="H";
            if (hasNominal) result+="O";
            if (hasInverseRole) result+="I";
            if (hasQualifiedNumberRestriction) result+="Q";
            else if (hasUnqualifiedNumberRestriction) result+="N";
            else if (hasFuntionalNumberRestriction) result+="F"; // not implemented
            if (hasDatatype) result+="(D)"; // not implemented
                return result;
        }

    /** for test and use as stand-alone application **/
	public static void main(String[] args){
		if (args.length != 1) {
			usage(); 
		}
		DLExpressivnessExtractor app = new DLExpressivnessExtractor();
		Model m = ModelFactory.createDefaultModel();
		m.read(args[0]);
		String expr = (String) app.extract(m, null);
		System.out.println(expr);
	}


	private static void usage(){
		System.err.println("Usage: java DLExpressivnessExtractor <http://url.of.the.ontology>");
		System.exit(-1);
	}
	


}
