package uk.ac.open.kmi.watson.validation.utils;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Vector;

import jonelo.jacksum.JacksumAPI;
import jonelo.jacksum.algorithm.AbstractChecksum;

/**
 * Simple util class that create a validator file from a directory containing the 
 * RDF files to be indexed. The resulting file will often have to be edited to 
 * get the absolute path to the file and the correct provenance...
 * @author mda99
 */
public class CreateDescFileFromDirectory {

	static  FileWriter fstream; // = new FileWriter("saved-state.txt");
	static BufferedWriter out;// = new BufferedWriter(fstream);
	
	public static class FileTraversal {
		public final void traverse( final File f, String path, String provpre) throws IOException {
			if (f.isDirectory()) {
				onDirectory(f);
				final File[] childs = f.listFiles();
				for( File child : childs ) {
					traverse(child, path, provpre);
				}
				return;
			}
			onFile(f, path, provpre);
		}

		public void onDirectory( final File d ) {
			System.out.println("Considering directory " + d.getName());
		}
		
	    public String getFingerPrint(File file) throws Exception {
	    	AbstractChecksum checksum = null;
	    	checksum = JacksumAPI.getChecksumInstance("sha1");
	        checksum.reset();
	        checksum.setEncoding(AbstractChecksum.HEX);
	        checksum.readFile(file.getAbsolutePath());
	        return checksum.format("urn:sha1:#CHECKSUM");
	    }

		public void onFile( final File f, String path, String provpre) {
//			boolean addThis = false;
//			if (f.getName().toLowerCase().endsWith(".owl")) {
//				System.out.println("Considering OWL file: " + f.getName());
//				addThis = true;
//			}
//			if (f.getName().toLowerCase().endsWith(".rdf") || f.getName().toLowerCase().endsWith(".rdfs")) {
//				System.out.println("Considering RDF file: " + f.getName());
//				addThis = true;
//			}
//			if (f.getName().toLowerCase().endsWith(".xml")) {
//				System.out.println("Considering XML file: " + f.getName());
//				addThis = true;
//			}
			try {
				// String contentFP = getFingerPrint(f);
				ProcessFile(f, path, provpre);
				}
				catch (Exception e) {
					e.printStackTrace();
				}
			}
			
		}		
	
	public static void main (String [] args){
		try {
			fstream = new FileWriter("original-state.txt");
			out = new BufferedWriter(fstream);
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		
		try {
			if (args.length == 3)
				new FileTraversal().traverse(new File("."), args[1], args[2]);
			else {System.out.println("Please specify directory, path and prov base"); System.exit(-1);}
			out.close();
		}
		catch (Exception e) {
			e.printStackTrace();
		}	
	
	}

	private static void ProcessFile(File f, String path, String provpre) {
		Vector<String> exist = new Vector<String>();
		long size = f.length();
		String hash = "";
		try {
			if (!f.isDirectory()) {
				System.out.println("Reading " + f.getName());
				String fp = readFileAsString(f);
				if (fp != null)
					hash = new URN(fp).toString();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (exist.contains(hash)) { 
				System.out.println("Duplicated file" + f.getName());
		}
		else {
			try {
				out.write(hash+","+path+f.getPath().substring(1) +","+provpre+f.getPath().substring(1) +","+size+",0,0\n");
			} catch (IOException e) {
				e.printStackTrace();
			}
			exist.add(hash);
		}
	}
	
	  private static String readFileAsString(File f)
	    throws java.io.IOException{
	        BufferedReader reader = new BufferedReader(
	                new FileReader(f));
	        char[] buf = new char[4096];
	        int numRead=0;
	        while((numRead=reader.read(buf)) != -1){
	            String readData = String.valueOf(buf, 0, numRead);
	            return readData;
	        }
	        reader.close();
	        return null;
	    }
	
}
