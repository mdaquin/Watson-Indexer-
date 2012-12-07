package uk.ac.open.kmi.watson.validation.utils;

import java.io.File;
import java.util.Vector;

import jonelo.jacksum.JacksumAPI;
import jonelo.jacksum.algorithm.AbstractChecksum;
import uk.ac.open.kmi.watson.validation.DocumentStatus;
import uk.ac.open.kmi.watson.validation.PopulatorManager;
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

public class IndexFiles {

	private PopulatorManager pm = PopulatorManager.getInstance();

	private void registerPopulators() {
		pm.registerPopulator(URIPopulator.class); // 16384
		pm.registerPopulator(NumberOfStatementsPopulator.class); // 33554432
		pm.registerPopulator(LanguagePopulator.class); // 128
		pm.registerPopulator(ImportsPopulator.class); // 1048576
		pm.registerPopulator(ClassesPopulator.class); // 2
		pm.registerPopulator(PropertiesPopulator.class); // 1024
		pm.registerPopulator(ProvenancePopulator.class); // 4294967296
		pm.registerPopulator(SizePopulator.class); // 4294967296

		pm.registerPopulator(LiteralPopulator.class); // 512
		pm.registerPopulator(EntityRelationsPopulator.class); // 4294967296

		// maybe taking to much resources....
		// pm.registerPopulator(OWLSpeciePopulator.class); // 256
		pm.registerPopulator(DLexpressivityPopulator.class); // 8

		// pm.registerPopulator(SCCommentPopulator.class); // 4096
		// pm.registerPopulator(SCLabelPopulator.class); // 8192

		// pm.registerPopulator(InconsistencyPopulator.class);
		// pm.registerPopulator(UnsatisfiableConceptsPopulator.class);
	}

	public static void main(String[] args) {
		IndexFiles app = new IndexFiles();
		app.registerPopulators();
		if (args.length < 2) {
		    System.out.println("ERROR: not enough parameters");
		    System.out.println("give the name of the index and the path to the file(s) to add");
		    System.exit(1);
		}
		String index = args[0];
		String prefix = args[1];
		Vector<DocumentStatus> docs = new Vector<DocumentStatus>();
		for (int i = 2 ; i < args.length; i++){
			String path = args[i];
			String[] prov = {"file://"+path};
			File f = new File(args[i]);
			String documentID = null;
			try {
				documentID = app.getFingerPrint(f);
			} catch (Exception e) {
				e.printStackTrace();
			}
			long size = f.length();
			long status = 0;
			boolean taken = false;
			DocumentStatus ds = new DocumentStatus(documentID, path, prov, size, status, taken);
			ds.setCacheLocation(path);
			ds.setOntologySpaceName(index);
			docs.add(ds);
		}
		app.pm.setDocuments(docs);
		app.pm.dataManager.setIndexName(index);
		app.pm.indexDocs();
		app.pm.closeIndexes();
	}
	
	  public String getFingerPrint(File file) throws Exception {
	    	AbstractChecksum checksum = null;
	    	checksum = JacksumAPI.getChecksumInstance("sha1");
	        checksum.reset();
	        checksum.setEncoding(AbstractChecksum.HEX);
	        checksum.readFile(file.getAbsolutePath());
	        return checksum.format("urn:sha1:#CHECKSUM");
	    }

}
