package uk.ac.open.kmi.watson.validation.analysis;

import java.util.List;
import java.util.Vector;

import uk.ac.open.kmi.watson.validation.model.DocumentStatus;
import uk.ac.open.kmi.watson.validation.model.DocumentStatusHome;

public class Utils {

	public static int nbValidatedWithStatus(long status){
		return getValidatedWithStatus(status).size();
	}

	public static Vector<String> getValidatedWithStatus(long status) {
		Vector<String> results = new Vector<String>();
		DocumentStatusHome dsh = new DocumentStatusHome();
		List<DocumentStatus> lds = dsh.listDocumentStatus();
		for (DocumentStatus ds : lds){
			if (isValidated(ds.getDocumentId(), status)) results.add(ds.getDocumentId());
		}
		return results;
	}
	
	public static boolean isValidated(String documentID, long status) {
		DocumentStatusHome dsh = new DocumentStatusHome();
		DocumentStatus ds = dsh.findById(documentID);
		return ((ds.getStatus() & status) == status);
	}
	
	
}
