package uk.ac.open.kmi.watson.validation.utils;

import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Vector;

import uk.ac.open.kmi.watson.validation.DocumentStatus;

/** 
 * Simple util class that takes a validator input file, poiting to (supposedly big) NT files, 
 * cut the files into pieces of fixed size and create 2 additional files:
 *      - <original_file>.part which is a validator input file for all the partition files, with pointers to the original group file
 *      - <original_files>.group which contain the description of the original (large) files
 * @author mda99
 */
public class PartitionBigFiles {

	private String infile;
	private String outfile;
	private String groupFile;
	private Vector<DocumentStatus> origlist;
	
	private FileWriter ofstream; // = new FileWriter("saved-state.txt");
	private BufferedWriter oout;// = new BufferedWriter(fstream);
	private FileWriter gfstream; // = new FileWriter("saved-state.txt");
	private BufferedWriter gout;// = new BufferedWriter(fstream);

	
	public PartitionBigFiles(String infile, String provpre) {
		this.infile = infile;
		outfile = infile+".part";
		groupFile = infile+".group";
		DocListReader.setProvPrefix(provpre);
		// origlist = DocListReader.createList(infile);
	}

	public static void main (String [] args){
		PartitionBigFiles pbf = new PartitionBigFiles(args[0], args[1]);
		pbf.createPatition();
	}

	public void createPatition() {
		try {
			ofstream = new FileWriter(outfile);
			oout = new BufferedWriter(ofstream);
			gfstream = new FileWriter(groupFile);
			gout = new BufferedWriter(gfstream);
		for (DocumentStatus ds : origlist){
			if (ds.getSize() > 50000000){
				partition(ds);
			}
			else writeAsIs(ds);
		}
		oout.close();
		gout.close();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}

	private void writeAsIs(DocumentStatus ds) throws IOException {
		System.out.println("Writing "+ds.getDocumentId()+" as is...");
		long size =ds.getSize();
		String hash = ds.getDocumentId();
		oout.write(hash+","+ds.getPath() +","+ ds.getProvenance()[0] +","+size+",0,0\n");
	}

	private void partition(DocumentStatus ds) throws IOException {
		System.out.println("Partitioning "+ds.getDocumentId());
		writeAsGroup(ds);
		Vector<DocumentStatus> parts = createPartFiles(ds);
		for (DocumentStatus dsp : parts){
			writeAsPart(dsp, 	ds.getDocumentId());
		}
	}

	private void writeAsPart(DocumentStatus dsp, String groupID) throws IOException {
		System.out.println("   Writing "+dsp.getDocumentId());
		oout.write("PartOf::"+dsp.getPartOf()+","+dsp.getDocumentId()+","+dsp.getPath() +","+ dsp.getProvenance()[0] +","+dsp.getSize()+",0,0\n");
		oout.flush();
	}
	

	public static final int CHUNK_SIZE = 100000;
	
	private Vector<DocumentStatus> createPartFiles(DocumentStatus ds) throws IOException {
		Vector<DocumentStatus> result = new Vector<DocumentStatus>();
		String path = ds.getPath();
		FileReader fr = new FileReader(path);
		boolean finished = false;
		int partnum=1;
		char[] toWriteLeft = null;
		while (!finished){
			System.out.println("   Creating "+path+"part"+(partnum));
			FileWriter fw = new FileWriter(path+"part"+(partnum));
			BufferedWriter fwout = new BufferedWriter(fw);
			char [] read = new char[CHUNK_SIZE];
			int i = CHUNK_SIZE;
			int sum = 0;
			boolean endChunk = false;
			while (!endChunk && sum < 50000000){
				if (toWriteLeft!=null){	
					System.out.println("Get what's left");
					i = toWriteLeft.length;
					read = toWriteLeft;
					toWriteLeft = null;
				}
				else {
					read = new char[CHUNK_SIZE];
					i = fr.read(read);
					if (i != CHUNK_SIZE) {
						finished = true;
						endChunk = true;
					}
				}
				sum += i;
				char[] toWrite = new char[i];
				boolean write = true;
				for (int j = 0; j < i; j++) {
					if (sum > 49900000 && read[j]=='\n' && !finished){
						toWrite[j]=read[j];
						char[] toWrite2 = new char[j+1];
						for (int k=0; k < toWrite2.length; k++)
							toWrite2[k] = toWrite[k];
						toWrite = toWrite2;
						System.out.println("Soemthing left..."+i+" "+j);
						toWriteLeft = new char[i-(j+1)];
						for (int k=0; k<toWriteLeft.length; k++)
							toWriteLeft[k] = read[(j+1)+k];
							write = false;
						break;				
					}
					else toWrite[j]=read[j];
				}
				fwout.write(toWrite);
			}
			fwout.close();
			DocumentStatus part = new DocumentStatus(ds.getDocumentId()+"part"+partnum, ds.getPath()+"part"+(partnum), new String[] {ds.getProvenance()[0]+"part"+(partnum)} , sum, 0, false);
			part.setPartOf(ds.getDocumentId());
			result.add(part);
			partnum++;
		}
		return result;
	}

	private void writeAsGroup(DocumentStatus ds) throws IOException {
		gout.write(ds.getDocumentId()+","+ds.getPath()+","+ ds.getProvenance()[0]+"\n");
	}
}
