package com.example.hs;

import org.apache.commons.lang3.StringUtils;

public class StringExecution {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String value = "home/TMSS/AAIM/Documents/";
		String value3 = value;
		String value2 = "e/M/D/";
//		System.out.println(value2.lastIndexOf("/"));
//		
//		String newVal2 = value.substring(0, value.length()-1);
//		System.out.println(newVal2);
//		System.out.println(newVal2.lastIndexOf("/"));
//		System.out.println(newVal2.substring(newVal2.lastIndexOf("/")+1, newVal2.length()));
		
//		String newStr = StringUtils.substring(value, 0, value.length()-1);
//		System.out.println(newStr);
//		System.out.println(StringUtils.substring(newStr, StringUtils.lastIndexOf(newStr, "/")+1,newStr.length()));
		
//		System.out.println(StringUtils.substring(value,value.length()-1));
		System.out.println(value3.lastIndexOf("preprod"));
		System.out.println(StringUtils.substring(value3, ("home/TMSS/preprod/").length() , value3.length()));
		if(value3.trim().contains("home/TMSS/preprod/")) {
			System.out.println(StringUtils.substring(value3, ("home/TMSS/preprod/").length(), value3.length()));
		}else {
			System.out.println(StringUtils.substring(value3, ("home/TMSS/").length(), value3.length()));
		}
		
	}

}
