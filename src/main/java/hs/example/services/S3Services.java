package hs.example.services;


import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.DeleteObjectsRequest;
import com.amazonaws.services.s3.model.ListObjectsRequest;
import com.amazonaws.services.s3.model.ListObjectsV2Request;
import com.amazonaws.services.s3.model.ListObjectsV2Result;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.google.common.collect.Lists;

import hs.example.entities.S3Entity;
import hs.example.entities.S3ObjectEntity;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;


@Component
public class S3Services {
	ExecutorService  executorService= Executors.newCachedThreadPool();
	AWSCredentials credentials = new BasicAWSCredentials();
	AmazonS3 amazonS3; 
	
	String bucketname = "east-hs-test";
	
	@PostConstruct
	public void s3init() {
		amazonS3 =AmazonS3ClientBuilder
				  .standard()
				  .withCredentials(new AWSStaticCredentialsProvider(credentials))
				  .withRegion(Regions.US_EAST_1)
				  .build();
	}
	
	public List<Bucket> listBucket(){
		List<Bucket> buckets = amazonS3.listBuckets();
		if(null == buckets || buckets.isEmpty())
			return null;
		for (Bucket b : buckets) {
		    System.out.println("* " + b.getName());
		}
		return buckets;
	}
	
    public static class ListFolderDto{
        public String folderName, continuationToken, previousToken, bucketName;
        public int maxKeys=3;

    }
    public static class S3File{
        public String  name,key, dateStr, fileSize, folder;
        public long size;
        public Date createDate;
    }
    public static class  FolderData{
        public String continuationToken, folderName,parentFolderName;
        public List<String> folders= Lists.newArrayList();
        public List<S3File>  files=Lists.newArrayList();
        public int keyCount;
    }
    
    
    //list all folder
	public void listFolder() {
		ObjectListing listing = amazonS3.listObjects( "controller-2", "");
		List<S3ObjectSummary> summaries = listing.getObjectSummaries();
		HashSet<String> uniqueFolder = new HashSet<>();
		summaries.forEach(a -> {
			System.out.println(a.toString());
			if(a.getSize() == 0 && a.getKey().contains("/"))
				uniqueFolder.add(a.getKey());
		});
		
		while (listing.isTruncated()) {
				summaries.clear();
			   listing = amazonS3.listNextBatchOfObjects (listing);
			   System.out.println("in ra cai nay");

			   summaries.addAll (listing.getObjectSummaries());
			   summaries.forEach(a -> {
					System.out.println(a.toString());
					if(a.getSize() == 0)
						uniqueFolder.add(a.getKey());
				});
			}
		ArrayList<String> flower_array = new ArrayList<>(uniqueFolder);
		System.out.println(flower_array.toString());
		
	}
	
	//list objects under certain paths. can take more than 1000 items by using istruncated from s3 aws
	//we can see folders and file.
	public void listObjectsUnderCertainPath() {
		ObjectListing listing = amazonS3.listObjects( "controller-2", "");
		List<S3ObjectSummary> summaries = listing.getObjectSummaries();
		List<S3ObjectEntity> finVal = new LinkedList<>();
		while (listing.isTruncated()) {
			   listing = amazonS3.listNextBatchOfObjects (listing);
			   System.out.println("in ra cai nay");
			   summaries.addAll (listing.getObjectSummaries());
		}
		summaries.forEach(s3Object -> {
			try {
				if(s3Object.getSize() < 1 || s3Object.getKey().endsWith("/")) {
					
				} else {
					S3ObjectEntity anObject = new S3ObjectEntity();
					anObject.setKey(s3Object.getKey());
					String fileName = s3Object.getKey();
					if(s3Object.getKey().contains("/"))
						anObject.setFileName(fileName.substring(s3Object.getKey().lastIndexOf("/")+1));
					else
						anObject.setFileName(fileName);
					anObject.setSize(s3Object.getSize());
					String lastMoDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(s3Object.getLastModified());
					anObject.setModifiedDate(lastMoDate);
					if(s3Object.getKey().contains("/")) {
						String filePath = s3Object.getKey();
						anObject.setPath(filePath.substring(0, filePath.lastIndexOf("/")));
					} else {
						anObject.setPath("");
					}
					
					finVal.add(anObject);
				}
			}catch(Exception e) {
				System.out.print(e.toString());
			}
			
		});
		summaries.forEach(a -> {
			System.out.println(a);
		});
		finVal.forEach(a -> {
			System.out.println(a.toString());
		});
	}
	

	
	public void checkValidPath(String searchString) {
//		 loop thru the entire bucket to search for files
		ObjectListing listing = amazonS3.listObjects( "controller-2", "home/TMSS/"+searchString);
		List<S3ObjectSummary> summaries = listing.getObjectSummaries();
		List<S3ObjectSummary> finVal = new LinkedList<>();
		while (listing.isTruncated()) {
			   listing = amazonS3.listNextBatchOfObjects (listing);
			   System.out.println("in ra cai nay");
			   summaries.addAll (listing.getObjectSummaries());
		}
		
		summaries.forEach(s3Obj -> {
			if(s3Obj.getKey().toUpperCase().contains(searchString.toUpperCase())) {
				finVal.add(s3Obj);
			}
		});
		
		finVal.forEach(oB -> {
			System.out.println(oB);
		});
	}
	
	public void exploreObjects(String searchString) {
		ObjectListing listing = amazonS3.listObjects( "controller-2", "home/TMSS/");
		List<S3ObjectSummary> summaries = listing.getObjectSummaries();
		HashSet<S3ObjectSummary> firstLevelFolder = new HashSet<>();
		HashSet<String> folders = new HashSet<>();
		while (listing.isTruncated()) {
		   listing = amazonS3.listNextBatchOfObjects (listing);
		   System.out.println("in ra cai nay");
		   summaries.addAll (listing.getObjectSummaries());
		}
		if(searchString == null) {
			for(int i=1 ; i<summaries.size() ; i++) {
				if(comparingNames(firstLevelFolder, summaries.get(i))) {
					firstLevelFolder.add(summaries.get(i));
				}
			}
		}else {
			if(searchString.equalsIgnoreCase("HHG3080") || searchString.equalsIgnoreCase("Rates") || searchString.equalsIgnoreCase("shipment")) {
				summaries.forEach(s3Obj -> {
					if(s3Obj.getKey().contains(searchString)) {
						if(folders.size() == 0 ) {
							folders.add(s3Obj.getKey());
						}else {
							if(comparingString(folders,s3Obj.getKey())) { folders.add(s3Obj.getKey()); }
						}
					}
				});
				for(String e: folders) {System.out.println(e);}
			}else {
				//searching files
				this.checkValidPath(searchString);
			}
			
		}
		
		firstLevelFolder.forEach(e -> {
			System.out.println(e.toString());
		});
		
	}
	
	public boolean comparingString(Set<String> path, String key) {
		for(String e: path) {
			if(key.contains(e)) {
				return false;
			}
		}
		return true;
	}
	
	public boolean comparingNames(Set<S3ObjectSummary> firstLevelFolder, S3ObjectSummary anObj) {
		if(firstLevelFolder.size() == 0) {
			return true;
		}else {
			for(S3ObjectSummary obj : firstLevelFolder) {
				if(anObj.getKey().contains(obj.getKey())) {
					return false;
				}
			}
		}
		
		return true;
	}
	public void isAValidObject(String object) {
		try {
			boolean doesItExists = amazonS3.doesObjectExist("controller-2", "home/TMSS/SVLM/");
			 System.out.println(doesItExists);
			 System.out.println(amazonS3.doesObjectExist("controller-2", "home/TMSS/SVLM/HHG3080/"));
		} catch (AmazonServiceException ase) {
			System.out.print(ase.toString());
		}
	}
	
//	delete multiple selection objects on S3
//	get list of key. process 1000 items each time
	public Boolean deleteObjects(List<String> listObjects) {
		try {
			System.out.println(listObjects.toString());
			DeleteObjectsRequest deleteObjectRequest = new DeleteObjectsRequest(bucketname).withQuiet(true);
			deleteObjectRequest.setKeys(listObjects.stream().map(k -> new DeleteObjectsRequest.KeyVersion(k)).collect(Collectors.toList()));
			amazonS3.deleteObjects(deleteObjectRequest);
			return true;
		}catch(AmazonS3Exception e) {
			System.out.println(e.toString());
			return false;
		}
		
	}
	
//	. it only search by prefix. nothing else
	public List<S3ObjectSummary> listObjectsWPrefix(String prefix) {
		if(null == prefix || prefix.equalsIgnoreCase("null")) prefix = "";
		ListObjectsRequest listObjectsRequest = new ListObjectsRequest().withBucketName(bucketname)
				.withPrefix("home/TMSS/" + prefix);
		ObjectListing listing = amazonS3.listObjects(listObjectsRequest);
		List<S3ObjectSummary> finalList = new ArrayList<>();
		List<S3ObjectSummary> summaries = listing.getObjectSummaries();
		while (listing.isTruncated()) {
			listing = amazonS3.listNextBatchOfObjects(listing);
			summaries.addAll(listing.getObjectSummaries());
		}
		
		if (prefix != "") {
			if (prefix.toUpperCase().contains("HHG3080") || prefix.toUpperCase().contains("Rates")) {
				for (int i = 1; i < summaries.size(); i++) {
					if (summaries.get(i).getSize() > 0)
						finalList.add(summaries.get(i));
				}
			} else {
				for (int i = 1; i < summaries.size(); i++) {
					if (summaries.get(i).getKey().endsWith("/"))
						finalList.add(summaries.get(i));
				}
			}
		} else {
			if(summaries.size() > 1) {
				S3ObjectSummary tempObj = summaries.get(1);	
				finalList.add(tempObj);
				for(int i = 1 ; i < summaries.size() ; i++) {
					if(!summaries.get(i).getKey().contains(tempObj.getKey()) && summaries.get(i).getKey().endsWith("/")) {
						tempObj = summaries.get(i);
						finalList.add(tempObj);
					}
				}
			}
		}
		

		finalList.forEach(e -> {
			System.out.println(e.toString());
		});

		return finalList;
	}
	
	public List<S3ObjectSummary> getObjectsFromCertainPath(String prefix) {
		List<S3ObjectSummary> finalList = new ArrayList<>();
		try {
			ListObjectsRequest listObjectsRequest = new ListObjectsRequest().withBucketName(bucketname)
					.withPrefix( prefix);
			ObjectListing listing = amazonS3.listObjects(listObjectsRequest);
			
			List<S3ObjectSummary> summaries = listing.getObjectSummaries();
			while (listing.isTruncated()) {
				listing = amazonS3.listNextBatchOfObjects(listing);
				summaries.addAll(listing.getObjectSummaries());
			}
			if(prefix.toUpperCase().contains("RATES") || prefix.toUpperCase().contains("HHG3080") || 
					prefix.toUpperCase().contains("RATEDOWNLOAD") || prefix.toUpperCase().contains("RATEFILETEMPLATE") ||
					prefix.toUpperCase().contains("SHIPMENT") || prefix.toUpperCase().contains("SYNCADAXMLS")) {
				for(int i=0;i<summaries.size();i++) {
					if(!summaries.get(i).getKey().endsWith("/"))
						finalList.add(summaries.get(i));
				}
			}else {
				for(int i=1;i<summaries.size();i++) {
					if(summaries.get(i).getKey().endsWith("/"))
						finalList.add(summaries.get(i));
				}
			}
			
			finalList.forEach(e -> {
				System.out.println(e.toString());
			});	
		}catch (AmazonServiceException ase) {
			System.out.print(ase.toString());
		}
			
		return finalList;
	}
	
	
	public void testFindObject() {
		ListObjectsV2Request req = new ListObjectsV2Request().withBucketName(bucketname).withPrefix("home/TMSS/");
		ListObjectsV2Result listing = amazonS3.listObjectsV2(req);
		for (String commonPrefix : listing.getCommonPrefixes()) {
		        System.out.println(commonPrefix);
		}
		 System.out.println("asdasd");
		for (S3ObjectSummary summary: listing.getObjectSummaries()) {
		    System.out.println(summary.getKey());
		}
	}
	
	public List<S3ObjectSummary> searchAllSubFolder(String searchKey) {
		ListObjectsRequest listObjectsRequest = new ListObjectsRequest().withBucketName(bucketname)
				.withPrefix("home/TMSS/");
		ObjectListing listing = amazonS3.listObjects(listObjectsRequest);
		List<S3ObjectSummary> finalList = new ArrayList<>();
		List<S3ObjectSummary> summaries = listing.getObjectSummaries();
		while (listing.isTruncated()) {
			listing = amazonS3.listNextBatchOfObjects(listing);
			summaries.addAll(listing.getObjectSummaries());
		}
		if(null == searchKey || searchKey.equalsIgnoreCase("")) {
			return summaries;
		}else {
			for(int i = 0; i< summaries.size() ; i++) {
				if(summaries.get(i).getKey().toUpperCase().contains(searchKey.toUpperCase())) {
					finalList.add(summaries.get(i));
				}
			}
			return finalList;
		}
	}
	
	public List<S3Entity> getFirstFolderAndFiles(){
		List<S3Entity> listS3Entity = new ArrayList<>();
		ListObjectsV2Request  req = new ListObjectsV2Request().withBucketName(bucketname)
				.withPrefix("home/TMSS/").withDelimiter("/");
		ListObjectsV2Result  listing= amazonS3.listObjectsV2(req);
		List<S3ObjectSummary > summaries = new ArrayList<>();
		String tempPrefix = listing.getPrefix();
		
		while(listing.isTruncated()) {
			
		};
		
		do {
			listing = amazonS3.listObjectsV2(req);
			System.out.println(listing.toString());
			System.out.println(listing.getCommonPrefixes().toString());
			summaries.addAll(listing.getObjectSummaries());
		}while(listing.isTruncated());
		summaries.forEach(s3Obj -> {
			System.out.println(s3Obj.toString());
		});
		return null;
	}
	
	private void parsingRawFolderData(List<S3Entity> listS3Entity, ListObjectsV2Result listing){
		List<String> commentPrefix = listing.getCommonPrefixes();
		if(null != commentPrefix && !commentPrefix.isEmpty()) {
			commentPrefix.forEach(key -> {
				S3Entity s3Entity = new S3Entity();
				String newStr = StringUtils.substring(key, 0, key.length()-1);
				String name = StringUtils.substring(newStr, StringUtils.lastIndexOf(newStr, "/")+1,newStr.length());
				s3Entity.setName(name);
				s3Entity.setKey(key);
				s3Entity.setType("Folder");
				s3Entity.setSize(0);
			});
		}
	}
	
	private void parsingRawFolderData(List<S3Entity> listS3Entity, List<S3ObjectSummary> summaries, String tempPrefix) {
		if(null != summaries || !summaries.isEmpty()) {
			for(int i=0; i < summaries.size() ; i++) {
				if(i == 0 && summaries.get(0).getKey().equalsIgnoreCase(tempPrefix)) {
					//dont try to solve in here 
					//because it is always the prefix of every request. I place this validation for getting more than 1000 ojbects from S3
				}else {
					S3Entity s3Entity = new S3Entity();
					String key = summaries.get(i).getKey();
					String name = StringUtils.substring(key, StringUtils.lastIndexOf(key, "/")+1,key.length());
					s3Entity.setName(name);
					s3Entity.setKey(key);
					s3Entity.setSize(summaries.get(i).getSize());
					s3Entity.setModifiedDate(summaries.get(i).getLastModified());
				}
			}
		}
	}
}
