package uk.ac.open.kmi.watson.validation.utils;

import java.io.IOException;

import org.apache.lucene.search.IndexSearcher;

public class CountDocs {

	static long sum;
	
	public static void main (String[] args){
		if (args.length == 1) {
			count(args[0]);
			System.out.println(sum);
		}
		else if (args.length == 0){
			ConfFileReader cfr = new ConfFileReader("watson.conf");
			String[] docis = cfr.getStringArrayParameter("document-indexes");
			sum = 0;
			for (String doc : docis){
				// System.out.println("Counting "+doc);
				count(doc);
			}
			System.out.println("Total docs = "+sum);
			sum = 0;
			String[] entis = cfr.getStringArrayParameter("entity-indexes");
			for (String doc : entis){
				// System.out.println("Counting "+doc);
				count(doc);
			}
			System.out.println("Total ents = "+sum);
			sum = 0;
			String[] relis = cfr.getStringArrayParameter("relation-indexes");
			for (String doc : relis){
				// System.out.println("Counting "+doc);
				count(doc);
			}
			System.out.println("Total rels = "+sum);
			sum = 0;
			String[] litis = cfr.getStringArrayParameter("literal-indexes");
			for (String doc : litis){
				// System.out.println("Counting "+doc);
				count(doc);
			}
			System.out.println("Total lits = "+sum);
		}
		

	}
	
	
	private static void count(String string) {
		try {
			IndexSearcher indexSearcher= new IndexSearcher(string);
			// System.out.print(".");
			// System.out.println(indexSearcher.maxDoc());
			sum += indexSearcher.maxDoc();
			// System.out.print(",");
			indexSearcher.close();
			// System.out.print("!");
		} catch (IOException e) {
			e.printStackTrace();
		}			
	}


	private static void usage() {
		System.out.println("java CountDocs <index_directory>");
		System.exit(-1);
	}
	
}
