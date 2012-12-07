package uk.ac.open.kmi.watson.validation.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Today {

	public static String todayString(){
		Calendar today = Calendar.getInstance();
		String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
	// 	String date = today.get(Calendar.YEAR)+"-"+(today.get(Calendar.MONTH)+1)+"-"+today.get(Calendar.DAY_OF_MONTH);
		return date;
	}
	
	public static String yesterdayString(){
		Calendar today = Calendar.getInstance();
		String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date(System.currentTimeMillis()-86400000));
	// 	String date = today.get(Calendar.YEAR)+"-"+(today.get(Calendar.MONTH)+1)+"-"+today.get(Calendar.DAY_OF_MONTH);
		return date;
	}
	
	public static void main (String [] args){
		if (args.length == 1) System.out.println(yesterdayString());
		else System.out.println(todayString());
	}
	
}
