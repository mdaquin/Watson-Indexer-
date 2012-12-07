package uk.ac.open.kmi.watson.validation.utils;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.StringTokenizer;
import java.util.Vector;

import uk.ac.open.kmi.watson.validation.DocumentStatus;

public class DocListReader {

	private static DataInputStream dis = null;

	public static void setFilename(String filename){
		if (dis ==null){
		File file = new File(filename);
		FileInputStream fis = null;
		BufferedInputStream bis = null;

		try {
			fis = new FileInputStream(file);
			bis = new BufferedInputStream(fis);
			dis = new DataInputStream(bis);
		}
		catch(Exception e){
			e.printStackTrace();
		}
		}
		
	}
	
	public static Vector<DocumentStatus> createList(int limit) {
		Vector<DocumentStatus> result = new Vector<DocumentStatus>();
		int nb = 0;
		try{
			while (dis.available() != 0) {
				if (nb >= limit) break;
				nb ++;
				String line = dis.readLine();
				if (line.trim().startsWith("PartOf::"))
					try {
//						 System.out.println(line);
						StringTokenizer st = new StringTokenizer(line, ",");
						String idg = st.nextToken().substring(8);
						System.out.print(idg+ " is the group of");
						String id = st.nextToken();
						System.out.print(id+" ");
						String path = st.nextToken();
						System.out.print(path+" ");
						String prov = st.nextToken();
						System.out.print(prov+" ");
						long size = Long.parseLong(st.nextToken());
						System.out.print(size+" ");				
						long status = Long.parseLong(st.nextToken());
						System.out.print(status+" ");				
						boolean taken = st.nextToken().equals("1");
						System.out.println(taken+" ");				
						String[] provs = {prov};
						DocumentStatus ds = new DocumentStatus(id, path, provs, size, status, taken);
						ds.setCacheLocation(cachePrefix+prov.substring(provPrefix.length(), prov.length()));
						ds.setPartOf(idg);
						result.add(ds);
						/* StringTokenizer st = new StringTokenizer(line, ",");
						String id = st.nextToken();		
						System.out.println(id);		
						String path = st.nextToken();
						String prov = st.nextToken();
						long size = Long.parseLong(st.nextToken());
						long status = Long.parseLong(st.nextToken());
						boolean taken = st.nextToken().equals("1");
						//DocumentStatus ds = new DocumentStatus(id, path, prov,
						//		size, status, taken);
						//ds.setPartOf(idg);
						//result.add(ds); */
					} catch (Exception e) {
						System.out.println("WARNING:: Cannot process line: "
								+ line);
						e.printStackTrace();
					}

				else
					try {
						// System.out.println(line);
						StringTokenizer st = new StringTokenizer(line, ",");
						String id = st.nextToken();
						// System.out.print(id+" ");
						String path = st.nextToken();
						// System.out.print(path+" ");
						String prov = st.nextToken();
						// System.out.print(prov+" ");
						long size = Long.parseLong(st.nextToken());
						// System.out.print(size+" ");				
						long status = Long.parseLong(st.nextToken());
						// System.out.print(status+" ");				
						boolean taken = st.nextToken().equals("1");
						// System.out.println(taken+" ");				
						String[] provs = {prov};
						DocumentStatus ds = new DocumentStatus(id, path, provs, size, status, taken);
						ds.setCacheLocation(cachePrefix+prov.substring(provPrefix.length(), prov.length()));
						result.add(ds);
						System.out.println("read "+nb);
					} catch (Exception e) {
						System.out.println("WARNING:: Cannot process line: "
								+ line);
						e.printStackTrace();
					}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		// cleaning
		for (DocumentStatus ds : result) {
			if (ds.isTaken()) {
				ds.setStatus(0);
				ds.setTaken(false);
			}
		}
		return result;
	}
	
	private static String provPrefix = null;	
	public static void setProvPrefix(String provpre) {
		provPrefix = provpre;
	}
	
	private static String cachePrefix = null;	
	public static void setCachePrefix(String cachepre) {
		cachePrefix = cachepre;
	}

}
