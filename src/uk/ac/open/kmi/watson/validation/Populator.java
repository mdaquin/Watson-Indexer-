package uk.ac.open.kmi.watson.validation;

import java.util.Observable;

import com.hp.hpl.jena.rdf.model.Model;

/**
 * A Populator populate the DB with the value given by an extractor. It
 * implements the conditions for trigerring it manage the status, errors, etc.
 * logged in the DB.
 *
 * @author mda99
 */
public abstract class Populator  extends Observable {

    /** The thread **/
   //  private Thread T = new Thread();
    
    /** Have to be specified coherently for each populator * */
    protected long code;
    
    /** Have to be specified coherently for each populator * */
    protected long requiredStatus;
    
    /** ther considered Jena model * */
    protected Model model;
    
    /** the ID of the considered document * */
    protected String documentID;
    
    /** the manager of the populator * */
    private PopulatorManager manager = PopulatorManager.getInstance();
    
    /** the associated extractor * */
    protected Extractor extractor;
    
    /** create an instance **/
    public Populator() {
        this.addObserver(PopulatorManager.getInstance());
    }
    
    public long getCode(){
        return code;
    }
    
    public void setModel(Model model) {
        this.model = model;
    }
    
    public Model getModel() {
        return model;
    }
    
    public void setDocumentID(String DID) {
        this.documentID = DID;
    }
    
    public String getDocumentID() {
        return documentID;
    }
    
    public Thread getTheThread(){
    	return runningThread;
    }
    
    /** test in the validation DB if the status match **/
    public boolean testStatus() {
        // assert (documentID!=null);
        DocumentStatus ds = PopulatorManager.getInstance().getDocumentStatus(documentID);
        long currentStatus = ds.getStatus();
        return ((currentStatus & requiredStatus) == requiredStatus && (currentStatus & code) != code);
    }
    
    /** update the status and errors in the validation DB **/
    private void updateStatusAndError() {
        assert (documentID!=null);
        if (extractor.getErrorMessage()==null){
            DocumentStatus ds = PopulatorManager.getInstance().getDocumentStatus(documentID);
        	ds.setStatus(ds.getStatus()+code);
        } else {
            String errorMessage = extractor.getErrorMessage();
            System.out.println("ERROR:: "+errorMessage);
        }
    }
    
 
    /** start the thread of the populator **/
    public void start(){
        runningThread = new theThread();
        runningThread.start();
        // new counterThread().start();
    }
    
    private theThread runningThread;
    private boolean completed = false;
    private long timeout = 1000000000;
    private long step = 1000000;
    
    private class counterThread extends Thread {
	
	public void run(){
	    
	    for (long counter = 0; counter < timeout; counter+=step){
		try {
		    this.sleep(step);
		} catch (InterruptedException e) {
		    e.printStackTrace();
		}
		if (completed) break;
	    }
	    if (!completed) {
		System.err.println("P"+getCode()+" on "+getDocumentID()+":: TIME OUT EXCEEDED!!");
		// runningThread.interrupt();
	    }
	}
    }
    
    private class theThread extends Thread {
        
        /** Run the thread.
         * Extract the information using the extractor, write to the DB and notify the manager
         */
        public void run(){
            message("Starting...");
            assert(testPrecondition() && testStatus());
            assert(getModel()!=null);
            assert(getDocumentID()!=null);
            message("Create extractor...");
            Object o = extractor.extract(getModel(), getDocumentID());
	    //if (o == null && extractor.getErrorMessage() == null) o = extractor.extract(getDocumentID(), null);
            if (o!=null) {
                message("Writing to DB...");
                writeToDB(o);
            }
            message("Writing potential errors...");
            updateStatusAndError();
            // notify the stop to the manager
            // manager.stopping(this);
            message("Ending...");
            setChanged();
            completed = true;
            notifyObservers();
            
        }
    }
    
    public void run(){
    	message("Starting...");
    	assert(testPrecondition() && testStatus());
    	assert(getModel()!=null);
    	assert(getDocumentID()!=null);
    	// message("Create extractor...");
    	Object o = extractor.extract(getModel(), getDocumentID());
    	if (o!=null) {
    		// message("Writing to DB...");
    		writeToDB(o);
    	}
    	// message("Writing potential errors...");
    	updateStatusAndError();
    	message("Ending...");
    }
    
    
    private void message(String mess){
        System.out.println("P"+getCode()+" on "+getDocumentID()+":: "+mess);
    }
    
    /**
     * Test the precondition of the Populator.
     * Have to be implemented for all specialized popiulators.
     * This test conditions at a different level than the status (e.g. the language of the onto...).
     * @return true if the current extracted information match the requirements of this populator.
     */
    abstract public boolean testPrecondition();
    
    /**
     * Writes the extracted information into the DB
     * @param o the object representing the extracted information.
     */
    abstract protected void writeToDB(Object o);
    
}