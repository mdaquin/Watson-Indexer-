/*
 * IndividualsExtractor.java
 *
 * Created on 16 April 2007, 15:05
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 *//*
 * IndividualsExtractor.java
 *
 * Created on 16 April 2007, 15:05
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package uk.ac.open.kmi.watson.validation.extractors.entities;

import java.util.Vector;

import org.mindswap.pellet.utils.URIUtils;

import uk.ac.open.kmi.watson.validation.Extractor;
import uk.ac.open.kmi.watson.validation.extractors.relations.EntityRelationDataStructure;
import uk.ac.open.kmi.watson.validation.utils.ModelUtils;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.vocabulary.RDF;

/**
 * TODO: needs to be redone... and tested.
 * TODO: Note working properly: lots of individuals are missed (look at the first docs...)
 * Basically, extract any named resource type of something, that is not a class or a property
 * and for which the namespace is not a known one and the thing it is type of is not a known one.
 * @author cb7224, mda99
 */
public class IndividualsExtractor extends Extractor {

	public Object extract(Model model, String documentID) {
		if (model == null) {
			errorMessage = "cannot deal with null models";
			return null;
		} 
		errorMessage = null;
		return getIndividuals(model, documentID);
	}

	private Vector<EntityRelationDataStructure> getIndividuals(Model model,
			String documentID) {
		Vector<EntityRelationDataStructure> individuals = new Vector<EntityRelationDataStructure>();
		StmtIterator it = model.listStatements();
		while (it.hasNext()){
			Statement st = it.nextStatement();
			if (st.asTriple().getPredicate().isURI())
			if (st.asTriple().getPredicate().getURI().equals(RDF.type.getURI())){
				if (st.asTriple().getSubject().isURI()){
					String entURI = st.asTriple().getSubject().getURI();
					if (!inLanguageNamespace(entURI)){
						String objURI = null;
						if (st.asTriple().getObject().isURI())
						     objURI = st.asTriple().getObject().getURI();
					if (!inLanguageNamespace(objURI)){
					   EntityRelationDataStructure erds = new EntityRelationDataStructure();
					   erds.sbjUri = entURI;
					   erds.pptUri = RDF.type.getURI();
					   erds.objUri = objURI;
					   individuals.add(erds);
					   // System.out.println(erds);
					}
					}
				}
			}
		}
		it.close();
		return individuals;
	}

	private String[] languageNamespaces = {
			 "http://www.daml.org/2000/12/daml+oil",
		     "http://www.daml.org/2001/03/daml+oil",
		     "http://www.daml.org/2000/10/daml-ont",
		     "http://www.w3.org/TR/1999/PR-rdf-schema-19990303",
		     "http://www.w3.org/2000/01/rdf-schema",
		     "http://www.w3.org/1999/02/22-rdf-syntax-ns",
		     "http://www.w3.org/2002/07/owl"
	};
	
	private boolean inLanguageNamespace(String entURI) {
		if (entURI == null) return false;
		String namespace = URIUtils.getNameSpace(entURI);
		for (String lns : languageNamespaces){
			if (lns.equals(namespace) || (lns+"#").equals(namespace)) return true;
		}
		return false;
	}

	/** for test and use as stand-alone application **/
	@SuppressWarnings("unchecked")
	public static void main(String[] args) {
		if (args.length != 1) {
			usage();
		}
		IndividualsExtractor app = new IndividualsExtractor();
		Model m = ModelFactory.createDefaultModel();
		m.read(args[0]);
		Vector<EntityRelationDataStructure> indis = (Vector<EntityRelationDataStructure>) app
				.extract(m, null);
		for (EntityRelationDataStructure s : indis) {
			System.out.println(ModelUtils.getLocalName(s.sbjUri) + " -"
					+ ModelUtils.getLocalName(s.pptUri) + "- "
					+ ModelUtils.getLocalName(s.objUri));
		}

	}

	private static void usage() {
		System.err
				.println("Usage: java IndividualsExtractor <http://url.of.the.ontology>");
		System.exit(-1);
	}

}
