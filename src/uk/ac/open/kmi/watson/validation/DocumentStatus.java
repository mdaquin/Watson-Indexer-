package uk.ac.open.kmi.watson.validation;

public class DocumentStatus implements Comparable {

	private long status = 0;
	private String documentID;
	private String path;
	private String[] prov;
	private long size;
	private boolean taken = false;
	private String osname = null;
	private String cacheLoc = null;
	private String partOf = null;
	
	public DocumentStatus(String documentID, String path, String[] prov, long size, long status, boolean taken){
		this.documentID = documentID;
		this.path = path;
		this.prov = prov;
		this.size = size;
		this.status = status;
		this.taken = taken;
	}
	
	public DocumentStatus(String groupid, String docID, String path, String[] prov, long size, long stat, boolean taken) {
		this(docID, path, prov, size, stat, taken);
		this.partOf=groupid;
	}

	public long getStatus() {
		return status;
	}
	
	public void setStatus(long l) {
		status = l;
	}

	public String getDocumentId() {
		return documentID;
	}

	public String[] getProvenance() {
		return prov;
	}

	public long getSize() {
		return size;
	}

	public boolean isTaken() {
		return taken;
	}
	
	public void setTaken(boolean t){
		taken = t;
	}

	public String getPath() {
		return path;
	}

	public int compareTo(Object arg0) {
		return (int) (size-((DocumentStatus)arg0).size);
	}

	public String getCacheLocation() {
		return cacheLoc;
	}

	public void setCacheLocation(String cacheLoc) {
		this.cacheLoc = cacheLoc;
	}

	public String getOntologySpaceName() {
		return osname;
	}

	public void setOntologySpaceName(String osname) {
		this.osname = osname;
	}

	public String getPartOf() {
		return partOf;
	}

	public void setPartOf(String partOf) {
		this.partOf = partOf;
	}

}
