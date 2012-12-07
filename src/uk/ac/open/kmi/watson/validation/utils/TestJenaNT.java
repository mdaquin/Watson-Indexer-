package uk.ac.open.kmi.watson.validation.utils;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;

public class TestJenaNT {

	
	public static void main(String[] args){
		  long t1 = System.currentTimeMillis();
	  	   try {
	  	  	Model model = ModelFactory.createDefaultModel();
	  	    model.read("file:///Volumes/Shared/LinkedData/dbpedia_persons/persons.nt", "N-TRIPLE");
	        System.out.println("Time to create model:: "+(t1-System.currentTimeMillis()));
	  	   } catch(Exception e){
	  		   e.printStackTrace();
	  	   }
	}
	
}
