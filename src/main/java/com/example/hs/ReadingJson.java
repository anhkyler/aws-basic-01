package com.example.hs;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.collections4.MultiValuedMap;
import org.apache.commons.collections4.multimap.ArrayListValuedHashMap;

import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;

import java.lang.reflect.Array;
import java.lang.reflect.Type;

public class ReadingJson {
	 public static void main(String[] args) throws Exception {
		 Gson gson = new Gson();
		 JsonReader reader = new JsonReader(new FileReader("testObjs.json"));
		 Type collectionType = new TypeToken<Collection<MappingObject>>(){}.getType();
		 Collection<MappingObject> enums = gson.fromJson(reader, collectionType);
		 List<S3ObjectSummary> aList = new ArrayList<>();
		 enums.forEach(a -> {
			 S3ObjectSummary anObj = new S3ObjectSummary();
			 anObj.setBucketName("hs-XXXX");
			 anObj.setKey(a.getObjectkey());
			 anObj.setSize(Long.parseLong(a.getContentlength()));
			 anObj.setLastModified(new Date(a.getLastmodified()));
			 anObj.setStorageClass(a.getStorageclass());
			 aList.add(anObj);
		 });
		 
		 List<S3ParsingObject> parsingObj = new ArrayList<>();
//		 aList.forEach(a -> System.out.println(a.toString()));
		 HashMap<String, S3ParsingObject> mapKey = new HashMap<>();
		 int counter = 1;
		 for(S3ObjectSummary k : aList) {
			
			 double keySize = k.getSize();
			 Date modifiedDate = k.getLastModified();
			 
//			 sObj.setSize(k.getSize());
//			 sObj.setDateModified(k.getLastModified());
//			 sObj.setId(counter++);
			 String []splStr = k.getKey().split("/");
//			 String [] tempSplit = new String[splStr.length];
			 List<String> tempSplit = new ArrayList<>();
			 for(int i=0;i<splStr.length ; i++) {
				 S3ParsingObject sObj = new S3ParsingObject();
				 tempSplit.add(splStr[i]);
				 String tempKey = String.join("/", tempSplit);
				 sObj.setSize(keySize);
				 sObj.setDateModified(modifiedDate);
				 sObj.setId(counter);
				 sObj.setFilePath(tempKey);
				 if(mapKey == null ) {
					 mapKey.put(String.join("/", tempSplit), sObj);
				 }else {
					 if(mapKey.get(tempKey) == null) {
						 mapKey.put(String.join("/", tempSplit), sObj);
						 counter++;
					 }
						  
				 }
//				 sObj.setFilePath(null);
			 }
			 tempSplit.clear();
//			 sObj.setFilePath(new ArrayList<String>(Arrays.asList(splStr)));
//			 sObj.setSize(k.getSize());
//			 sObj.setDateModified(k.getLastModified());
//			 sObj.setId(counter++);
//			 parsingObj.add(sObj);
		 }
		 List<S3ParsingObject> values =  new ArrayList<>(mapKey.values());
		 values.forEach(a -> {
			 String []splStr = a.getFilePath().split("/");
			 a.setParsePath(new ArrayList<String>(Arrays.asList(splStr)));
//			 a.set (new ArrayList<String>(Arrays.asList(splStr)));
			 System.out.println(a.toString());
		 });
		 
//		 List<S3ParsingObject> finalSort = Collections.sort(values, Comparator.comparing(S3ParsingObject::getId));
//		 parsingObj.forEach(a -> System.out.println(a.toString()));
		 
//		 List<String> prefix = new ArrayList<String>(Arrays.asList("0190000102","4224",
//				 "790","976","983","998","AAIM","ABBC","ADIF","ADOL","AEKA","AERM","AHPJ","ATVL",
//				 "AVLH","BTFA","CHMP","CPAO","GFCT","notifyUsers"));
////		 prefix.add(null)
//		 MultiValuedMap<String, S3ObjectSummary> mapS3BasedOnSCAC = new ArrayListValuedHashMap<>();
//		 Gson gson = new GsonBuilder().setPrettyPrinting().create();
//		 prefix.forEach( k -> {
//			 List<S3ObjectSummary> listForAPrefix = new ArrayList<>();
//			 aList.forEach(obj -> {
//				 if(obj.getKey().contains("/" + k + "/")) {
//					 listForAPrefix.add(obj);
//				 }
//				 
//			 });
//		 });
//		 
//		 prefix.forEach(key -> {
//			 for(int i=0 ; i < aList.size() ; i++) {
//				 if(aList.get(i).getKey().contains("home/TMSS/"+key+"/")) {
//					 mapS3BasedOnSCAC.put("home/TMSS/"+key+"/", aList.get(i));
//				 }
//			 }
//		 });
//		 Iterator<String> it = mapS3BasedOnSCAC.keySet().iterator();
//
//
//         while(it.hasNext()){
//           String theKey = (String)it.next();
//           System.out.println(theKey);
//           System.out.println(mapS3BasedOnSCAC.get(theKey));
//       }
//         
//         MultiValuedMap<String, MultiValuedMap<String, S3ObjectSummary>> mapS3BasedOnSubFolder = new ArrayListValuedHashMap<>();
//         
	 }
	 
	 
}
