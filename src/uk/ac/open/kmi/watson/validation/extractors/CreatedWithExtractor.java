package uk.ac.open.kmi.watson.validation.extractors;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import uk.ac.open.kmi.watson.validation.Extractor;

import com.hp.hpl.jena.rdf.model.Model;

public class CreatedWithExtractor extends Extractor {

	public Object extract(Model model, String documentID) {
		String url = getCacheLocation(documentID);
		try {
			String editor = extract(url);
			return editor;
		} catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}

	private String extract(String furl) throws IOException {
		String resultEditor = null;
		URL url = new URL(furl);
		BufferedReader in = new BufferedReader(
					new InputStreamReader(
					url.openStream()));
		String inputLine;
		boolean ntkclue1 = false;
		while ((inputLine = in.readLine()) != null){
			if (inputLine.contains("<!-- Created with Protege")){
				resultEditor = "Protege"; int ind =-1; int ind2 = -1;
				if ((ind=inputLine.indexOf("Plugin"))!=-1 && (ind2=inputLine.indexOf(")"))!=-1){
					String ver = inputLine.substring(ind+7, ind2);
					resultEditor += " with OWL Plugin "+ver;
				}
				break;
			}
			if (inputLine.contains("<!ENTITY kaon2 'http://kaon2.semanticweb.org/internal#'>")){
				ntkclue1 = true;
			}
			if (inputLine.contains("xmlns:kaon2=\"http://kaon2.semanticweb.org/internal#\"")
					&& ntkclue1){
				resultEditor = "NeOn Toolkit";
				break;
			}		
		}
		in.close();
		return resultEditor;
	}

	private String getCacheLocation(String documentID) {
		// TODO: implement and use the extractor...
		return null;
	}

	public static void main(String[] args){
		CreatedWithExtractor cwe = new CreatedWithExtractor();
		for (String s : args){
			try {
				System.out.println(cwe.extract(s));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	
}
