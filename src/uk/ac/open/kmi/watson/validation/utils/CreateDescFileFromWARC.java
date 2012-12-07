package uk.ac.open.kmi.watson.validation.utils;

import it.unimi.dsi.fastutil.io.FastBufferedInputStream;
import it.unimi.dsi.fastutil.io.MeasurableInputStream;
import it.unimi.dsi.law.warc.filters.Filter;
import it.unimi.dsi.law.warc.filters.Filters;
import it.unimi.dsi.law.warc.io.GZWarcRecord;
import it.unimi.dsi.law.warc.io.WarcFilteredIterator;
import it.unimi.dsi.law.warc.io.WarcRecord;
import it.unimi.dsi.law.warc.util.BURL;
import it.unimi.dsi.law.warc.util.WarcHttpResponse;

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


public class CreateDescFileFromWARC {
			
		public static void dumpContent(MeasurableInputStream block, String filename) throws IOException {
			FileWriter fstream; // = new FileWriter("saved-state.txt");
			BufferedWriter out = null;// = new BufferedWriter(fstream);
			try {
				fstream = new FileWriter(filename);
				out = new BufferedWriter(fstream);
			} catch (Exception e1) {
				e1.printStackTrace();
				return;
			}
			int c = 0;
			while ((c = block.read()) != -1) {
				out.write(c);
			}
			out.close();
			fstream.close();
		}
		
		public static class TrueFilter extends Filter<BURL> {
			
			@Override
			public boolean accept( BURL x ) {
				return true;
			}

			@Override
			public String toExternalForm() {
				
				return "true";
			}

		}

		
		public static void main(String[] args) throws FileNotFoundException {
			if (args.length < 2) {
				System.err.println("Usage: WarcReader <file> <dir>");
				System.exit(-1);
			}
			boolean indir = false;
			if (args.length == 3) indir = true;
			try {
				fstream = new FileWriter("original-state.txt");
				out = new BufferedWriter(fstream);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			String dir = args[1];
			final FastBufferedInputStream in = new FastBufferedInputStream(new FileInputStream(new File(args[0])));
			GZWarcRecord record = new GZWarcRecord();
			Filter<WarcRecord> filter = Filters.adaptFilterBURL2WarcRecord(new TrueFilter());
			WarcFilteredIterator it = new WarcFilteredIterator(in, record, filter);
			int urlCount = 0;
			WarcHttpResponse response = new WarcHttpResponse();
			try {
				while (it.hasNext()) {					
					WarcRecord nextRecord = it.next();
					try {
						String dir2 = dir;
						if (indir) dir2 = dir+"/"+Math.round(Math.random()*10000.);
						response.fromWarcRecord(nextRecord);
						System.out.println("Processing: " + nextRecord.header.subjectUri);
						String name = ""+Math.round(Math.random()*1000000000.);
						dumpContent(response.contentAsStream(), dir2+"/"+name);
						System.out.println("Processing "+response.url().toString()+"in "+dir2+"/"+name);
						ProcessFile(new File(dir2+"/"+name), response.url().toString());
						
					} catch (IOException e) {
						e.printStackTrace();
						continue;
					}
				} 
			} catch (RuntimeException re) {}			
			try {
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	
	static  FileWriter fstream; // = new FileWriter("saved-state.txt");
	static BufferedWriter out;// = new BufferedWriter(fstream);
	
	public static String getFingerPrint(File file) throws Exception {
		AbstractChecksum checksum = null;
		checksum = JacksumAPI.getChecksumInstance("sha1");
		checksum.reset();
		checksum.setEncoding(AbstractChecksum.HEX);
		checksum.readFile(file.getAbsolutePath());
		return checksum.format("urn:sha1:#CHECKSUM");
	}

	private static void ProcessFile(File f, String prov) {
		Vector<String> exist = new Vector<String>();
		long size = f.length();
		String hash = "";
		try {
			hash = getFingerPrint(f);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (exist.contains(hash)) { 
				System.out.println("Duplicated file" + f.getName());
		}
		else {
			try {
				out.write(hash+","+f.getAbsolutePath() +","+ prov +","+size+",0,0\n");
			} catch (IOException e) {
				e.printStackTrace();
			}
			exist.add(hash);
		}
	}
	
}
