package uk.ac.open.kmi.watson.validation.utils;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Hashtable;
import java.util.StringTokenizer;

public class ConfFileReader {

	private String filename;

	Hashtable<String, String> attributeValue;

	public ConfFileReader(String filename) {
		this.filename = filename;
		attributeValue = new Hashtable<String, String>();
		load();
	}

	private void load() {
		File file = new File(this.filename);
		FileInputStream fis = null;
		BufferedInputStream bis = null;
		DataInputStream dis = null;

		try {
			fis = new FileInputStream(file);
			bis = new BufferedInputStream(fis);
			dis = new DataInputStream(bis);
			while (dis.available() != 0) {
				String line = dis.readLine();
				analyseLine(line);
			}
			fis.close();
			bis.close();
			dis.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void analyseLine(String line) {
		String attribute = line.substring(0, line.indexOf("=")).trim();
		String value = line.substring(line.indexOf("=")+1, line.length()).trim();
		attributeValue.put(attribute, value);
	}

	public String getStringParameter(String att) {
		String value = attributeValue.get(att);
		return value;
	}

	public String[] getStringArrayParameter(String att) {
		String value = attributeValue.get(att);	
		StringTokenizer st = new StringTokenizer(value, ",");
		String[] result = new String[st.countTokens()];
		int i = 0;
		while (st.hasMoreTokens()){
			String v = st.nextToken();
			result[i++] = v.trim();
		}
		return result;
	}

	public int getIntParameter(String att) {
		String value = attributeValue.get(att);	
		return Integer.parseInt(value);
	}

	public static void main(String[] args){
		ConfFileReader cfr = new ConfFileReader("test.conf");
		System.out.println("document-indexes (s) :: "+cfr.getStringParameter("document-indexes"));
		System.out.print("document-indexes (a) :: ");
		String[] a = cfr.getStringArrayParameter("document-indexes");
		for (String s : a) System.out.print(s+"--");
		System.out.println();
		System.out.println("entity-indexes (s) :: "+cfr.getStringParameter("entity-indexes"));
		System.out.print("entity-indexes (a) :: ");
		a = cfr.getStringArrayParameter("entity-indexes");
		for (String s : a) System.out.print(s+"--");
		System.out.println();
		System.out.println("intparam (i) :: "+cfr.getIntParameter("intparam"));

	}
	
	
}
