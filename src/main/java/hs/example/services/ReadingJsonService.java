package hs.example.services;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.MultiValuedMap;
import org.apache.commons.collections4.multimap.ArrayListValuedHashMap;
import org.springframework.stereotype.Component;

import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;

import java.lang.reflect.Array;
import java.lang.reflect.Type;

@Component
public class ReadingJsonService {
	 public List<S3ParsingObject> printS3Objects() throws Exception {
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
		 HashMap<String, S3ParsingObject> mapKey = new HashMap<>();
		 int counter = 1;
		 for(S3ObjectSummary k : aList) {
			
			 double keySize = k.getSize();
			 Date modifiedDate = k.getLastModified();
			 String []splStr = k.getKey().split("/");
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
			 }
			 tempSplit.clear();
		 }
		 List<S3ParsingObject> values =  new ArrayList<>(mapKey.values());
		 values.forEach(a -> {
			 String []splStr = a.getFilePath().split("/");
			 a.setParsePath(new ArrayList<String>(Arrays.asList(splStr)));
			 System.out.println(a.toString());
		 });
		return values;
	 }
	 
	 
}
