
package uk.ac.open.kmi.watson.validation;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStream;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;
import java.util.Vector;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.util.FileManager;

/**
 * Manager for registering populator, triggering them, chaining them and handle
 * the corresponding theads.
 */
public class PopulatorManager implements Observer {

	/** the maximum number of threads **/
	private static final int NB_THREAD_TOTAL = 1;

    /** to avoid rescanning when nb thread = 1 **/
    private int current_document_indice = 0;

	/** the number of thread currently running * */
	private int nbThreadsRunning = 0;

	/** the unique instance of this class * */
	private static PopulatorManager singleton = new PopulatorManager();

	/** the known populators classes * */
	private Vector<Class> registry = new Vector<Class>();

	/** the DataManager **/
	public DataManager dataManager = DataManager.getInstance();
	
	/** the list of document to be treated... */
	private HashMap<String, DocumentStatus> documents = new HashMap<String, DocumentStatus>();
	private Vector<DocumentStatus> documents_v = new Vector<DocumentStatus>();

	
	/** indicate that there is nothing running at the moment **/
	private boolean idle = false;
	
	/** private constructors, because of the singleton * */
	private PopulatorManager() {
	}

	/**
	 * Add a populator to the registry of known populators. Check if the code of
	 * this populator is not already used.
	 * 
	 * @param populatorClass
	 *            the new class of popularor
	 */
	public void registerPopulator(Class populatorClass) {
		Populator p1 = null;
		try {
			p1 = (Populator) populatorClass.newInstance();
		} catch (Exception e1) {
			e1.printStackTrace();
			return;
		}
		// check if the new populator have a code that already exist....
		for (Class c : registry) {
			try {
				Populator p2 = (Populator) c.newInstance();
				if (p1.getCode() == p2.getCode())
					throw new RuntimeException(
							"Already existing Populator code" + p1.getCode());
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		message("Registering populator "+p1.getCode());
		registry.add(populatorClass);
	}

	/**
	 * find the next populator to be ran after p. Return false if no populator
	 * can be ran *
	 */
	private boolean runNextPopulator(Populator p) {
		Populator np = scan(p.getDocumentID());
		if (np == null)
			return false;
		else {
			np.setDocumentID(p.getDocumentID());
			np.setModel(p.getModel());
			np.start();
			return true;
		}
	}

	
	public void indexDocs() {
		for (int i = 0; i < documents_v.size(); i ++){
				DocumentStatus ds = documents_v.elementAt(i);
				Populator p = null;
				do {
				p = scan(ds.getDocumentId());
				if (p != null) {
					p.setDocumentID(ds.getDocumentId());
					Model m = getModel(ds.getDocumentId());
					if (m== null) {
						System.err.println("Null model... should be removed...");
					}
					else {
						p.setModel(m);
						p.run();
					}
				}
			} while (p!=null);
				System.out.println("FLUSHING INDEXES "+ds.getDocumentId());
				dataManager.flushIndexes(ds.getDocumentId());	
		}
	}
	
	
	/** to avoid concurent execution of scan * */
	private boolean scanRunning = false;

	/**
	 * Run new populators until the maximum number of threads is reached.
	 */
	public void scan() {
		// to avoid several execution of scan
		if (scanRunning){
			message("Already scanning, stop...");
			return;
		}
		scanRunning = true;
		message("Scanning...");
		while (nbThreadsRunning < NB_THREAD_TOTAL) {
			boolean iHaveEvidence = false;
			boolean workOnSomething = false;
			if (nbThreadsRunning == 0)
				iHaveEvidence = true;
			int current = 0;
			if (NB_THREAD_TOTAL == 1 && current_document_indice > 3) current = current_document_indice - 3;
			for (int i = current; i < documents_v.size(); i ++){
				DocumentStatus ds = documents_v.elementAt(i);
			    if (!ds.isTaken()) {
					// first test if it should stop... and if yes stop.
					if (shouldStop()) break;
					Populator p = scan(ds.getDocumentId());
					if (p != null) {
						workOnSomething = true;
						ds.setTaken(true);
						p.setDocumentID(ds.getDocumentId());
						Model m = getModel(ds.getDocumentId());
						if (m== null) {
						    System.err.println("Null model... should be removed...");
						}
						else {
						   p.setModel(m);
						  nbThreadsRunning++;
						  current_document_indice = i;
						  p.start();
						}
						if (nbThreadsRunning >= NB_THREAD_TOTAL) break;
					}
				}
				else 
					iHaveEvidence = false;
			}
			if (!workOnSomething && nbThreadsRunning == 0 && iHaveEvidence) {
				System.out.println("Nothing running right now... should stop");
				save();
				idle = true;
				break;
			}
			if (shouldStop()) break;
		}
		scanRunning = false;
	}

     boolean shouldStop() {
		File f = new File("STOP.NOW");
		if (f.canRead()) {
			System.out.println("SHOULD STOP NOW!");
			save();
			return true;
		}
		return false;
	}
     
     boolean isIdle(){
    	 return idle;
     }

	private void save() {
		return; 
		/* try{
			 FileWriter fstream = new FileWriter("saved-state.txt");
			 BufferedWriter out = new BufferedWriter(fstream);
			 for (DocumentStatus ds : documents_v){
				 String line = ds.getDocumentId()+","+ds.getPath()+","+ds.getProvenance()[0]+","+ds.getSize()+","+ds.getStatus()+","+(ds.isTaken()?"1":"0");
				 out.write(line+"\n");
			 }		
			 out.close();
		 }catch (Exception e){//Catch exception if any
			 System.err.println("Error: " + e.getMessage());
		 } */
		
	}

	/** find the next populator for DID, return null if it does not exist * */
	private Populator scan(String DID) {
		// message("Scanning for "+DID+"...");
		long time1 = System.currentTimeMillis();
		Vector<Populator> listPP = new Vector<Populator>();
		DocumentStatus ds = getDocumentStatus(DID);
		long time2 = System.currentTimeMillis();
		// System.out.println("time 1 = "+(time2-time1));
		// create the list of possible populators for DID
		for (Class c : this.registry) {
			try {
				long time3 = System.currentTimeMillis();
				Populator p = (Populator) c.newInstance();
				long time4 = System.currentTimeMillis();
				p.setDocumentID(DID);
				if (p.testStatus() && p.testPrecondition()) {
					// message("found populator "+p.getCode()+" for "+DID);
					// System.out.println("Total time= "+(System.currentTimeMillis()-time1));
					return p;
					// listPP.add(p);
				}
				long time5 = System.currentTimeMillis();
				// System.out.println("time 2-1 = "+(time4-time3));				
				// System.out.println("time 2-2 = "+(time5-time4));				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		long time6 = System.currentTimeMillis();
		System.out.println("time 2 = "+(time6-time1));
		if (listPP.size() == 0){
			return null;
		}
		listPP = filterAndRankPopulators(ds, listPP);
		long time7 = System.currentTimeMillis();
		System.out.println("time 3 = "+(time6-time1));
		if (listPP.size() == 0){
			
			return null;
		}
		message("found populator "+listPP.elementAt(0).getCode()+" for "+DID);
		return listPP.elementAt(0);
	}

	private Vector<Populator> filterAndRankPopulators(DocumentStatus ds,
			Vector<Populator> listPP) {
		return listPP;
	}

	/** get the unique instance of this singletong class * */
	static public PopulatorManager getInstance() {
		return singleton;
	}
	
	private int saveinc = 0;

	/**
	 * Method called when a populator notify that it will stop...
	 * 
	 * @param o
	 *            the populator
	 * @param arg
	 *            the parameter... null
	 */
	public void update(Observable o, Object arg) {
		Populator p = (Populator) o;
		message("Ending "+p.getCode()+" on "+p.getDocumentID());
		if (!runNextPopulator(p)) {
			// Process finished for this doc, flush indexes
			dataManager.flushIndexes(p.getDocumentID());
			setTaken(p.getDocumentID(), false);
			if(saveinc % 10 == 1) save();
			saveinc++;
			this.nbThreadsRunning--;
			p.getModel().close();
			DataManager.getInstance().realFlush();
			scan();
		}
	}

	/**
	 * Change the status of the Document... 
	 * @param documentID
	 * @param b
	 */
	private void setTaken(String documentID, boolean b) {
		DocumentStatus st = getDocumentStatus(documentID);
		st.setTaken(b);
	}
	
	/** used to get the model if only the documentID is available **/
	  private Model getModel(String documentID){
	  	  long t1 = System.currentTimeMillis();
	  	  Model model = ModelFactory.createDefaultModel();
	  	  try {
	  		String inputFileName = getDocumentStatus(documentID).getPath();
	  		InputStream in = FileManager.get().open( inputFileName );
	  		   if (in == null) {
	  		         throw new IllegalArgumentException(
	  		              "File: " + inputFileName + " not found");
	  		  }
	  		   model.read(in, "");
	  		  // model.read("file://"+getDocumentStatus(documentID).getPath());
	        System.out.println("Time to create"+documentID+" model ("+inputFileName+"):: "+(System.currentTimeMillis()-t1)+" model:: "+model.size());
	        // if model == null, release the thread, it won't go anywhere
	        // may not work well, becuase it may kill all, but at least we save the index
	        if (model == null) this.nbThreadsRunning--; 
	        return model;
	  	   } catch(Exception e){
	  		   System.out.println("maybe an n-triple file???");
	  		   try{
	  			   // TODO: this needs to change... put how to put the provenance if if is a part file... need to get the group...
	  			   model.read("file://"+getDocumentStatus(documentID).getPath(), "N-TRIPLE");	 
	  			  System.out.println("Time to create model:: "+(t1-System.currentTimeMillis()));	  		   
	  	        // if model == null, release the thread, it won't go anywhere
	  	        // may not work well, becuase it may kill all, but at least we save the index
	  	        if (model == null) this.nbThreadsRunning--; 
	  			  return model;
	  		   } catch (Exception e2){
		  		   e2.printStackTrace();
		  	   }
	  	   }
	  	   return null;
	  }
	  
	  private void message(String mess){
	      System.out.println("Manager"+"("+nbThreadsRunning+"):: "+mess);
	  }
	  
	public uk.ac.open.kmi.watson.validation.DocumentStatus getDocumentStatus(String documentID) {
		long time1 = System.currentTimeMillis();
		DocumentStatus res = documents.get(documentID);
		// System.out.println("GET DOC TIME = "+(System.currentTimeMillis()-time1));
		return res;
		/* for (DocumentStatus ds : documents)
			if (ds.getDocumentId().equals(documentID)) {
				System.out.println("GET DOC TIME = "+(System.currentTimeMillis()-time1));
				return ds;
			}
		return null; */
	}

	public void setDocuments(Vector<uk.ac.open.kmi.watson.validation.DocumentStatus> result) {
		idle = false;
		scanRunning = false;
		documents_v = result;
		Collections.sort(documents_v);
		for (DocumentStatus ds : result){
			documents.put(ds.getDocumentId(), ds);
		}
	}

	public Vector<DocumentStatus> getDocuments() {
		if (documents_v == null) documents_v = new Vector<DocumentStatus>();
		return documents_v;
	}

	
	
	public void closeIndexes() {
		dataManager.closeIndexes();
	}
	
}