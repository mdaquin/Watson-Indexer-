package uk.ac.open.kmi.watson.validation;

import java.io.IOException;
import java.lang.instrument.Instrumentation;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.StopAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexWriter;

import uk.ac.open.kmi.watson.validation.utils.WatsonAnalyzer;

public class LuceneInterface {
	private Analyzer  analyzer = null;
	private IndexWriter writer = null;
	
	private Hashtable<KeyDoc, Document> documentBase = new Hashtable<KeyDoc, Document>();
	
	private String index;
	
	public LuceneInterface(String index) {
		this.index = index;
		System.out.println("Creating "+index);
		try {
			analyzer = new WatsonAnalyzer(); 
			// see http://wiki.apache.org/lucene-java/LuceneFAQ
			writer   = new IndexWriter(index, analyzer, false);
		}
		catch (IOException e) {
			e.printStackTrace();
			System.out.println("Don't exist "+index);
		}
		if (writer == null) {
			try {
				writer = new IndexWriter(index, analyzer, true);
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	private void showKeys(){
		synchronized(this){
		Set<KeyDoc> enu = documentBase.keySet();
		for (KeyDoc kd : enu){
			System.out.println("   XXX in base:"+kd.SCID+" "+kd.documentID);
		}
		}
	}
	
	// BUG: doesn't work properly!
	public Document getDocument(String SCID, String documentId) {
		// showKeys();
		if (documentBase.containsKey(new KeyDoc(SCID, documentId))){
			return documentBase.get(new KeyDoc(SCID, documentId));
		}
		else {
			Document d = new Document();
			storeDocument(new KeyDoc(SCID, documentId), d);
			return d;
		}
	}
	
	private class KeyDoc {
		KeyDoc(String SCID, String documentID){this.SCID=SCID; this.documentID=documentID;}
		String SCID, documentID;
		public boolean equals (Object o){
			KeyDoc kd = (KeyDoc)o;
			return kd.SCID.equals(SCID) && kd.documentID.equals(documentID);
		}
		public int hashCode (){
			return SCID.hashCode()+documentID.hashCode();
		}
		public String toString(){
			return SCID+" "+documentID;
		}
	}
	
	private void storeDocument(KeyDoc kd, Document d) {
		synchronized(this){ documentBase.put(kd, d);}
	}

	public void flushAll(String SCID){
		 Set<KeyDoc> e = null;
		 Vector<KeyDoc> toFlush = new Vector<KeyDoc>();
		synchronized(this){
		  e = documentBase.keySet();
		for (KeyDoc k : e){
			if (k.SCID.equals(SCID)) toFlush.add(k);
		}
		}
		for (KeyDoc kd : toFlush) flushDocument(kd);
		System.out.println("Flushed "+index+" "+documentBase.size());
	}

	public void flushDocument(String SCID, String documentID) {
		flushDocument(new KeyDoc(SCID, documentID));
	}
	
	private void flushDocument(KeyDoc kd) {
		try {
			if (documentBase.containsKey(kd)) {
				Document d = documentBase.get(kd);
				try {
					writer.addDocument(d);
					removeDocument(kd);
				} catch(Exception e) {
					System.err.println("Something wrong... "+kd.SCID);
					e.printStackTrace();
					System.exit(-1);
				}
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}			
	}
	
	private void removeDocument(KeyDoc kd) {
		synchronized(this){
		  documentBase.remove(kd);
		}
	}

	public void optimizeIndex() {
		try {
			writer.optimize();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void close() {
		try {
			writer.flush();
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}
	
	public void realFlush(){
		try {
			writer.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
