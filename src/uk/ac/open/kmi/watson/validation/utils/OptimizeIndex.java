package uk.ac.open.kmi.watson.validation.utils;

import java.io.IOException;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.StopAnalyzer;
import org.apache.lucene.index.IndexWriter;

public class OptimizeIndex {

	static void optimize(String arg){
	Analyzer    rAnalyzer = new StopAnalyzer();
	try {
		IndexWriter rWriter   = new IndexWriter(arg, rAnalyzer, false);
		rWriter.optimize();
		rWriter.close();
	} catch (IOException e) {
		e.printStackTrace();
	}	
	}
	
	
	public static void main (String[] args){
		if (args.length == 1) {
			optimize(args[0]);
		}
		else if (args.length == 0){
			ConfFileReader cfr = new ConfFileReader("watson.conf");
			String[] docis = cfr.getStringArrayParameter("document-indexes");
			for (String doc : docis){
				System.out.println("Optimizing "+doc);
				optimize(doc);
			}
			String[] entis = cfr.getStringArrayParameter("entity-indexes");
			for (String doc : entis){
				System.out.println("Optimizing "+doc);
				optimize(doc);
			}
			String[] relis = cfr.getStringArrayParameter("relation-indexes");
			for (String doc : relis){
				System.out.println("Optimizing "+doc);
				optimize(doc);
			}
			String[] litis = cfr.getStringArrayParameter("literal-indexes");
			for (String doc : relis){
				System.out.println("Optimizing "+doc);
				optimize(doc);
			}
		}
	}

	private static void usage() {
		System.out.println("java OptimizeIndex <index_directory>");
		System.exit(-1);
	}
	
	
	
	
}
