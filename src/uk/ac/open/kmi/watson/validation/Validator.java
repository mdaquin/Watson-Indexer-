package uk.ac.open.kmi.watson.validation;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.StringTokenizer;
import java.util.Vector;

import uk.ac.open.kmi.watson.validation.populators.ClassesPopulator;
import uk.ac.open.kmi.watson.validation.populators.DLexpressivityPopulator;
import uk.ac.open.kmi.watson.validation.populators.EntityRelationsPopulator;
import uk.ac.open.kmi.watson.validation.populators.ImportsPopulator;
import uk.ac.open.kmi.watson.validation.populators.LanguagePopulator;
import uk.ac.open.kmi.watson.validation.populators.LiteralPopulator;
import uk.ac.open.kmi.watson.validation.populators.NumberOfStatementsPopulator;
import uk.ac.open.kmi.watson.validation.populators.PropertiesPopulator;
import uk.ac.open.kmi.watson.validation.populators.ProvenancePopulator;
import uk.ac.open.kmi.watson.validation.populators.SizePopulator;
import uk.ac.open.kmi.watson.validation.populators.URIPopulator;
import uk.ac.open.kmi.watson.validation.utils.DocListReader;


/** 
 * Main class of the Watson indexer.
 * Take a file containing the information about the documents to index, 
 * register to the populators and run the validation/indexing process.
 * Create 4 lucene indexes (directory) named documents-xxx, entities-xxx, 
 * relations-xxx and literals-xxx, where xxx is a timestamp corresponding
 * to the creation of the index.
 * @author mda99
 *
 */
public class Validator {
        
    private PopulatorManager pm = PopulatorManager.getInstance();
      
    private void registerPopulators(){
        pm.registerPopulator(URIPopulator.class); // 16384
        pm.registerPopulator(NumberOfStatementsPopulator.class); // 33554432
        pm.registerPopulator(LanguagePopulator.class); // 128
          pm.registerPopulator(ImportsPopulator.class); // 1048576
          pm.registerPopulator(ClassesPopulator.class); // 2
          pm.registerPopulator(PropertiesPopulator.class); // 1024
          pm.registerPopulator(ProvenancePopulator.class); //4294967296
          pm.registerPopulator(SizePopulator.class); //4294967296
               
    pm.registerPopulator(LiteralPopulator.class); // 512
    pm.registerPopulator(EntityRelationsPopulator.class); // 4294967296

    // maybe taking to much resources....
    // pm.registerPopulator(OWLSpeciePopulator.class); // 256
    pm.registerPopulator(DLexpressivityPopulator.class); // 8

      //  	pm.registerPopulator(SCCommentPopulator.class); // 4096
      //    pm.registerPopulator(SCLabelPopulator.class); // 8192

     
         // pm.registerPopulator(InconsistencyPopulator.class); 
        // pm.registerPopulator(UnsatisfiableConceptsPopulator.class);    
    }
    
    private boolean createDocumentList(String filename, String provpre, String cachepre, int limit){
       	DocListReader.setProvPrefix(provpre);
       	DocListReader.setCachePrefix(cachepre);
       	DocListReader.setFilename(filename);       	
    	pm.setDocuments(DocListReader.createList(limit));
    	return pm.getDocuments().size()!=0;
    }
    
    public static void main(String[] args) {
		Validator app = new Validator();
		app.registerPopulators();
		if (args.length < 3) {
			System.out
					.println("Need the conf file, provenance prefix and cache prefix");
			System.exit(-1);
		}
		if (args.length == 3) {
			while (app.createDocumentList(args[0], args[1], args[2], 10000)) {
				while (!app.pm.shouldStop() && !app.pm.isIdle()) {
					app.pm.scan();
					if (!app.pm.shouldStop() && !app.pm.isIdle()) {
						System.out.println("Max number of thread reached");
						try {
							Thread.sleep(120000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
			}
		}
		System.out.println("Closing indexes...");
		app.pm.closeIndexes();
		System.out.println("Done.");
	}
}
