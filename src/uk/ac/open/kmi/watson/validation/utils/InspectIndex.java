package uk.ac.open.kmi.watson.validation.utils;

import java.io.IOException;
import java.util.Enumeration;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.search.IndexSearcher;

public class InspectIndex {

	public static void main (String[] args){
		if (args.length != 1) usage();
		try {
			IndexSearcher indexSearcher= new IndexSearcher(args[0]);
			for (int i = 0; i < 10000; i++) {
				Document d = indexSearcher.doc(i);
				Enumeration fields = d.fields();
				while(fields.hasMoreElements()){
					Field f = (Field)fields.nextElement();
					System.out.println(f.name()+" :: "+d.get(f.name()));
				}
			System.out.println();
			}
			indexSearcher.close();
		} catch (IOException e) {
			e.printStackTrace();
		}			

	}
	
	private static void usage() {
		System.out.println("java InspectIndex <index_directory>");
		System.exit(-1);
	}
	
}
