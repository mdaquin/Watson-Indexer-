package uk.ac.open.kmi.watson.validation.utils;

import java.io.Reader;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.CharTokenizer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;

public class WatsonAnalyzer extends Analyzer{
	  
	private static final Analyzer STANDARD = new StandardAnalyzer();

	    public TokenStream tokenStream(String field, final Reader reader){
	        // do not tokenize field called 'element'
	        if (field.startsWith("H")) {
	            return new CharTokenizer(reader) {
	                protected boolean isTokenChar(char c) {
	                    return (c != ' ');
	                }
	            };
	        } else {
	            return STANDARD.tokenStream(field, reader);
	        }
	    }
	    	
}
