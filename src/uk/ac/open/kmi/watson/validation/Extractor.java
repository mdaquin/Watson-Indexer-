package uk.ac.open.kmi.watson.validation;
import uk.ac.open.kmi.watson.validation.utils.JenaUtils;

import com.hp.hpl.jena.db.DBConnection;
import com.hp.hpl.jena.db.IDBConnection;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.ModelMaker;

/**
 *  Extract an information from a jena model or the document in the DB.
 *  It is the actual reusable part component in the validation process.
 *  Can eventually be exported as a web service... but the object return of extract is a poblem...
 *  @author mda99
 * */
public abstract class Extractor {
    
    protected String errorMessage;
    
    /**
     * The method that have to be extended by specilized extractor. Extract an information from the model.
     * Should implement the extraction, return the object or null if error or problem,
     * and set the error message if required (null if not error).
     * Return null in case the documentID is used.
     * @param model the jena model to consider.
     * @return An object representing the required information.
     */
    abstract public Object extract(Model model, String documentID);
    
    /**
     * The method that have to be extended by specilized extractor. Extract an information from the document in the DB.
     * Should implement the extraction, return the object or null if error or problem,
     * and set the error message if required (null if not error).
     * Return null also in case gthe model is used.
     * @param documentID the ID of the document.
     * @return An object representing the required information.
     */
////  abstract public Object extract(String documentID);
//
//    public Object extract(String documentID) {
//        Model m;
//        if ((m = getModel(documentID)) == null)
//            return null;
//        return extract(m);
//    }
    
    /**
     * returns the error message from the last extraction.
     * @returns the error message from the last extraction.
     */
    public String getErrorMessage() {
        return errorMessage;
    }
    
    public void checkModel(Model model){
        if (model == null) {
            errorMessage = "cannot deal with null models";
            return;
        }
    }
    
    /** used to get the model if only the documentID is available **/
    protected Model getModel(String documentID){
        return JenaUtils.getModel(documentID);
    }
    
}