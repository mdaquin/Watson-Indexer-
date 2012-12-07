package uk.ac.open.kmi.watson.validation.utils;

import java.security.NoSuchAlgorithmException;

import jonelo.jacksum.JacksumAPI;
import jonelo.jacksum.algorithm.AbstractChecksum;

public class URN {
	
	public static final char DELIMITATOR = '\u306e';
	
	private String urn;
	
	public URN(byte[] bytes) {
		AbstractChecksum checksum = null;
		try {
			checksum = JacksumAPI.getChecksumInstance("sha1");
			checksum.reset();
			checksum.setEncoding(AbstractChecksum.HEX);
			checksum.update(bytes);
			urn = checksum.format("urn:sha1:#CHECKSUM");
		} catch (NoSuchAlgorithmException ignored) {
			ignored.printStackTrace();
		}
	}
	
	public URN(String string){
		this(string.getBytes());
	}
	
	public URN(String string1, String string2){
		this(string1 + DELIMITATOR + string2);
	}

	public URN(String string1, String string2, String string3){
		this(string1 + DELIMITATOR + string2 + DELIMITATOR + string3);
	}
	
	public String toString() {
		return urn;
	}
}
