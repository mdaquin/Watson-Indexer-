package uk.ac.open.kmi.watson.validation;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import uk.ac.open.kmi.watson.validation.utils.Today;

public class DataManager {
	
	private LuceneInterface documentIndex;
	private LuceneInterface relationIndex;
	private LuceneInterface entityIndex;	
	private LuceneInterface literalIndex;	
	
	private static DataManager instance = new DataManager();
	
	private String date;
	private long currentIndexNumber;
	private String ind_dir;
	
	public String getInd_dir() {
		return ind_dir;
	}

	public long getCurrentIndexNumber(){
		return currentIndexNumber;
	}
	
	
	public static DataManager getInstance(){
		return instance;
	}
	
	private  DataManager() {
		loadIndexes();
	}
	
	public void loadIndexes(){
		date = Today.todayString();
		Map map = System.getenv();
		ind_dir = (String) map.get("VAL_INDEX_DIR");
		if (ind_dir == null) {
			System.out.println("Should specify a VAL_INDEX_DIR...");
			return;
		}
		else {ind_dir += "/";
		System.out.println("Using VAL_INDEX_DIR = "+ind_dir);
		}
		currentIndexNumber = System.currentTimeMillis();
		documentIndex = new LuceneInterface(ind_dir+"documents-"+currentIndexNumber);
		relationIndex = new LuceneInterface(ind_dir+"relations-"+currentIndexNumber);
		entityIndex =  new LuceneInterface(ind_dir+"entities-"+currentIndexNumber);
		literalIndex = new LuceneInterface(ind_dir+"literals-"+currentIndexNumber);
	}
	
	public void setIndexName(String name){
		documentIndex = new LuceneInterface("documents-"+name);
		relationIndex = new LuceneInterface("relations-"+name);
		entityIndex =  new LuceneInterface("entities-"+name);
		literalIndex = new LuceneInterface("literals-"+name);
	}
	
	public LuceneInterface getDocumentIndex() {
		return documentIndex;
	}

	public LuceneInterface getEntityIndex() {
		return entityIndex;
	}

	public LuceneInterface getRelationIndex() {
		return relationIndex;
	}

	public LuceneInterface getLiteralIndex() {
		return literalIndex;
	}
	
	public void flushIndexes(String SCID){
		getDocumentIndex().flushAll(SCID);
		getEntityIndex().flushAll(SCID);
		getRelationIndex().flushAll(SCID);
		getLiteralIndex().flushAll(SCID);
		Runtime.getRuntime().gc();
	}

	public void realFlush(){
		getDocumentIndex().realFlush();
		getEntityIndex().realFlush();
		getRelationIndex().realFlush();
		getLiteralIndex().realFlush();
	}
	
	public void closeIndexes() {
		documentIndex.close();
		entityIndex.close();
		relationIndex.close();
		literalIndex.close();
	}
}
