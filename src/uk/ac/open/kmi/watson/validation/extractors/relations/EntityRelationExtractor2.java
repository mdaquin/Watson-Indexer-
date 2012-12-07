package uk.ac.open.kmi.watson.validation.extractors.relations;

import java.util.Vector;

import uk.ac.open.kmi.watson.validation.Extractor;
import uk.ac.open.kmi.watson.validation.utils.UriUtil;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;

/**
 * Entity relation extractor, whatever is the relation... replace all the other relation extractors 
 * @author mda99
 */
public class EntityRelationExtractor2 extends Extractor {

	private String[] languageNamespaces = {
			 "http://www.daml.org/2000/12/daml+oil",
		     "http://www.daml.org/2001/03/daml+oil",
		     "http://www.daml.org/2000/10/daml-ont",
		     "http://www.w3.org/TR/1999/PR-rdf-schema-19990303",
		     "http://www.w3.org/2000/01/rdf-schema",
		     "http://www.w3.org/1999/02/22-rdf-syntax-ns",
		     "http://www.w3.org/2002/07/owl"
	};
	
	  public Object extract(Model model, String documentID){
	      Vector<EntityRelationDataStructure> result = new Vector<EntityRelationDataStructure>();
	      if (model == null) {
	            errorMessage = "cannot deal with null models";
	            return null;
	        }
	      StmtIterator it = model.listStatements();
	      while (it.hasNext()){
	    	  Statement st = it.nextStatement();
	    	  if (!st.getSubject().isAnon() && !st.getPredicate().isAnon() 
	    			  && st.getObject().isResource() && !st.getObject().isAnon()){
	    		  if (!languageResource(((Resource)st.getObject().as(Resource.class)).getURI())){
	    		  EntityRelationDataStructure erds = new EntityRelationDataStructure();
	    		  	erds.sbjUri = st.getSubject().getURI();
	    		  	erds.pptUri = st.getPredicate().getURI();
	    		  	erds.objUri = ((Resource)st.getObject().as(Resource.class)).getURI();
	    		  	result.add(erds);
	    		  }
	    	  }
	      }
	      errorMessage = null;
	      return result;
	  }
	  
	  private boolean languageResource(String uri) {
		  String namespace = UriUtil.splitNamespace(uri)[0];
		  for (String ns : languageNamespaces)
			  if (ns.equals(namespace) || namespace.equals(ns+"#")) return true;
		 return false;
	}

	// for test purposes
	  public static void main(String[] args){
		  if (args.length != 1) {
	            usage();
	        }
	        EntityRelationExtractor2 app = new EntityRelationExtractor2();
	        Model m = ModelFactory.createDefaultModel();
	        m.read(args[0]);
	        Vector<EntityRelationDataStructure> erds = (Vector<EntityRelationDataStructure>) app.extract(m, null);
	        for (EntityRelationDataStructure ds : erds)
	        	System.out.println(ds);
	        System.out.println(erds.size());
	    }
	    
	    private static void usage(){
	        System.err.println("Usage: java EntityRelationExtractor2 <http://url.of.the.ontology>");
	        System.exit(-1);
	    }
	    
}
	

