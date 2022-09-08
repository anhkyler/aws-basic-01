package com.example.hs;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

import com.amazonaws.SdkClientException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.DeleteObjectsRequest;
import com.amazonaws.services.s3.model.ListObjectsV2Request;
import com.amazonaws.services.s3.model.ListObjectsV2Result;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import hs.example.entities.S3Entity;

public class StandaloneS3ServiceTesting {

	public static void main(String[] args) {
//		AWSCredentials credentials = new BasicAWSCredentials("AKIA22HNJIWQKAQAOM7O",
//				"5DlJDJvhBPSp0Ekfn/L/ox8I0I+UDEaWPaFFCUbJ"
//				);
//		AmazonS3 s3Client =AmazonS3ClientBuilder
//				  .standard()
//				  .withCredentials(new AWSStaticCredentialsProvider(credentials))
//				  .withRegion(Regions.US_EAST_1)
//				  .build();
//		
//		String bucketname = "east-hs-test";
//		
//		ListObjectsV2Request  req = new ListObjectsV2Request().withBucketName(bucketname)
//											.withPrefix("home/TMSS/").withDelimiter("/");
//		ListObjectsV2Result  listing= null;
//		List<S3ObjectSummary > summaries = new ArrayList<>();
//		do {
//			listing = s3Client.listObjectsV2(req);
//			System.out.println(listing.toString());
//			System.out.println(listing.getCommonPrefixes().toString());
//			summaries.addAll(listing.getObjectSummaries());
//		}while(listing.isTruncated());
//		summaries.forEach(s3Obj -> {
//			System.out.println(s3Obj.toString());
//		});
		
//		AWSCredentials credentials = new BasicAWSCredentials("AKIA22HNJIWQKAQAOM7O",
//				"5DlJDJvhBPSp0Ekfn/L/ox8I0I+UDEaWPaFFCUbJ"
//				);
//		AmazonS3 s3Client =AmazonS3ClientBuilder
//				  .standard()
//				  .withCredentials(new AWSStaticCredentialsProvider(credentials))
//				  .withRegion(Regions.US_EAST_1)
//				  .build();
//		
//		String bucketname = "east-hs-test";
//		
//		ListObjectsV2Request  req = new ListObjectsV2Request().withBucketName(bucketname)
//											.withPrefix("home/TMSS/SVLM/").withDelimiter("/");
//		ListObjectsV2Result listing= s3Client.listObjectsV2(req);
//		List<S3ObjectSummary > summaries = new ArrayList<>();
//		List<S3Entity> listS3Entity = new ArrayList<>();
//		String tempPrefix = listing.getPrefix();
//		summaries.addAll(listing.getObjectSummaries());
//		parsingRawFolderData(listS3Entity, listing);
//		parsingRawFolderData(listS3Entity, summaries, tempPrefix);
//		while (listing.isTruncated()) {
//			listing = s3Client.listObjectsV2(req);
//			summaries.addAll(listing.getObjectSummaries());
//			parsingRawFolderData(listS3Entity, listing);
//			parsingRawFolderData(listS3Entity, summaries, tempPrefix);
//		};
//		summaries.forEach(s3Obj -> {
//			System.out.println(s3Obj.toString());
//		});
//		
//		listS3Entity.forEach(a -> {
//			System.out.println(a.toString());
//		});
		
		//home/TMSS/SVLM/
//		gettingPrefixFromuser("home/TMSS/SVLM/favic");
//		gettingPrefixWithFileType("home/TMSS/AAIM");
//		testListingObjects();
		
		createFolder("home/TMSS/SVLM2");
	}
//	public static void uploadDocToS3(){
//		String bucketname = "east-hs-test";
//		String fileName ="Name of File";
//		String filePath
//	}
	//tao folder trong S3
	
	public static void createFolder(String key) {
		AWSCredentials credentials = new BasicAWSCredentials("AKIA22HNJIWQKAQAOM7O",
				"5DlJDJvhBPSp0Ekfn/L/ox8I0I+UDEaWPaFFCUbJ"
				);
		AmazonS3 s3Client =AmazonS3ClientBuilder
				.standard()
				.withCredentials(new AWSStaticCredentialsProvider(credentials))
				.withRegion(Regions.US_EAST_1)
				.build();

		String bucketname = "east-hs-test";
		boolean isObjectExists = s3Client.doesObjectExist(bucketname, key+"/");
		if(!isObjectExists) {
			ObjectMetadata metadata = new ObjectMetadata();
		    metadata.setContentLength(0);

		    // create empty content
		    InputStream emptyContent = new ByteArrayInputStream(new byte[0]);
		    PutObjectRequest putObjectRequest = new PutObjectRequest(bucketname,
		    		key+"/", emptyContent, metadata);
		    s3Client.putObject(putObjectRequest);	
		    System.out.println(putObjectRequest.toString());
		}else {
			System.out.println("object is existing");
		}
		
	}
	//xoa toan bo mot folder -- chi can lay key la duoc/ bao gom ca delete a file or multiple files
	public static void testListingObjects() {
		AWSCredentials credentials = new BasicAWSCredentials("AKIA22HNJIWQKAQAOM7O",
				"5DlJDJvhBPSp0Ekfn/L/ox8I0I+UDEaWPaFFCUbJ"
				);
		AmazonS3 s3Client =AmazonS3ClientBuilder
				.standard()
				.withCredentials(new AWSStaticCredentialsProvider(credentials))
				.withRegion(Regions.US_EAST_1)
				.build();

		String bucketname = "east-hs-test";
		List<S3ObjectSummary > summaries = new ArrayList<>();
		ListObjectsV2Request  req = new ListObjectsV2Request().withBucketName(bucketname)
				.withPrefix("home/TMSS/test - Copy.txt");
		ListObjectsV2Result listing= s3Client.listObjectsV2(req);
		
		summaries.addAll(listing.getObjectSummaries());
		List<S3Entity> listS3Entity = new ArrayList<>();
		while (listing.isTruncated()) {
			listing = s3Client.listObjectsV2(req);
			summaries.addAll(listing.getObjectSummaries());
		};
		
		summaries.forEach(e -> {
			System.out.println(e.toString());
			//can phai co che do de tach lay file va thong tin
		});
		
		 Set<String> filesList = new HashSet<>();
		 for (S3ObjectSummary summary: summaries) {
             filesList.add(summary.getKey());
         }
		 System.out.println(filesList.toString());
		if(filesList.size() > 0) {
			DeleteObjectsRequest deleteObjectsRequest = new DeleteObjectsRequest(bucketname)
	                .withKeys(filesList.toArray(new String[0]));
			
			try {
				s3Client.deleteObjects(deleteObjectsRequest);
	        }  catch (SdkClientException e) {
	            System.out.println(e.getMessage());
	            throw e;
	        }
		}
	}
	
	
	public static void gettingPrefixWithFileType(String prefix) {
		String newPrefix = StringUtils.substring(prefix,StringUtils.lastIndexOf(prefix, ".")+1, prefix.length()) ;	
		System.out.println(prefix);
		getDataFromPrefixWithFileExtension(prefix);
//		if(prefix.contains(".")) {
////			String newPrefix = StringUtils.substring(prefix,StringUtils.lastIndexOf(prefix, ".")+1, prefix.length()) ;	
////			System.out.println(newPrefix);
////			getDataFromPrefixWithFileExtension(prefix);
//		}else {
//			System.out.println("khong co gi het");
//			getDataFromPrefixWithoutFileExtension1(prefix);
//		}
	}
	
	public static void getDataFromPrefixWithFileExtension(String prefix) {
		AWSCredentials credentials = new BasicAWSCredentials("AKIA22HNJIWQKAQAOM7O",
				"5DlJDJvhBPSp0Ekfn/L/ox8I0I+UDEaWPaFFCUbJ"
				);
		AmazonS3 s3Client =AmazonS3ClientBuilder
				.standard()
				.withCredentials(new AWSStaticCredentialsProvider(credentials))
				.withRegion(Regions.US_EAST_1)
				.build();

		String bucketname = "east-hs-test";
		ListObjectsV2Request  req = new ListObjectsV2Request().withBucketName(bucketname)
				.withPrefix(prefix).withDelimiter("/");
		ListObjectsV2Result listing= s3Client.listObjectsV2(req);
		List<S3ObjectSummary > summaries = new ArrayList<>();
		summaries.addAll(listing.getObjectSummaries());
		List<S3Entity> listS3Entity = new ArrayList<>();
		String tempPrefix = listing.getPrefix();
		while (listing.isTruncated()) {
			listing = s3Client.listObjectsV2(req);
			summaries.addAll(listing.getObjectSummaries());
		};
		parsingRawFolderData(listS3Entity, listing);
		parsingRawFolderData(listS3Entity, summaries, tempPrefix);
		summaries.forEach(e -> {
			System.out.println(e.toString());
			//can phai co che do de tach lay file va thong tin
		});
		listS3Entity.forEach(k ->{
			System.out.println(k.toString());
		});
	}
	
	public static void getDataFromPrefixWithoutFileExtension1(String prefix) {
		AWSCredentials credentials = new BasicAWSCredentials("AKIA22HNJIWQKAQAOM7O",
				"5DlJDJvhBPSp0Ekfn/L/ox8I0I+UDEaWPaFFCUbJ"
				);
		AmazonS3 s3Client =AmazonS3ClientBuilder
				.standard()
				.withCredentials(new AWSStaticCredentialsProvider(credentials))
				.withRegion(Regions.US_EAST_1)
				.build();

		String bucketname = "east-hs-test";
		ListObjectsV2Request  req = new ListObjectsV2Request().withBucketName(bucketname)
				.withPrefix(prefix).withDelimiter("/");
		ListObjectsV2Result listing= s3Client.listObjectsV2(req);
		List<S3ObjectSummary > summaries = new ArrayList<>();
		List<S3Entity> listS3Entity = new ArrayList<>();
		String tempPrefix = listing.getPrefix();
		summaries.addAll(listing.getObjectSummaries());
		parsingRawFolderData(listS3Entity, listing);
		parsingRawFolderData(listS3Entity, summaries, tempPrefix);
		while (listing.isTruncated()) {
			listing = s3Client.listObjectsV2(req);
			summaries.addAll(listing.getObjectSummaries());
			parsingRawFolderData(listS3Entity, listing);
			parsingRawFolderData(listS3Entity, summaries, tempPrefix);
		};
		summaries.forEach(s3Obj -> {
			System.out.println(s3Obj.toString());
		});

		listS3Entity.forEach(a -> {
			System.out.println(a.toString());
		});
	}
	
	public static void getDataFromPrefixWithoutFileExtension(String prefix) {
		AWSCredentials credentials = new BasicAWSCredentials("AKIA22HNJIWQKAQAOM7O",
				"5DlJDJvhBPSp0Ekfn/L/ox8I0I+UDEaWPaFFCUbJ"
				);
		AmazonS3 s3Client =AmazonS3ClientBuilder
				.standard()
				.withCredentials(new AWSStaticCredentialsProvider(credentials))
				.withRegion(Regions.US_EAST_1)
				.build();

		String bucketname = "east-hs-test";
		ListObjectsV2Request  req = new ListObjectsV2Request().withBucketName(bucketname)
				.withPrefix(prefix).withDelimiter("/");
		ListObjectsV2Result listing= s3Client.listObjectsV2(req);
		List<S3ObjectSummary > summaries = new ArrayList<>();
		List<S3Entity> listS3Entity = new ArrayList<>();
		String tempPrefix = listing.getPrefix();
		summaries.addAll(listing.getObjectSummaries());
		parsingRawFolderData(listS3Entity, listing);
		parsingRawFolderData(listS3Entity, summaries, tempPrefix);
		while (listing.isTruncated()) {
			listing = s3Client.listObjectsV2(req);
			summaries.addAll(listing.getObjectSummaries());
			parsingRawFolderData(listS3Entity, listing);
			parsingRawFolderData(listS3Entity, summaries, tempPrefix);
		};
		summaries.forEach(s3Obj -> {
			System.out.println(s3Obj.toString());
		});

		listS3Entity.forEach(a -> {
			System.out.println(a.toString());
		});
	}
	
	private static void parsingRawFolderData(List<S3Entity> listS3Entity, ListObjectsV2Result listing){
		List<String> commentPrefix = listing.getCommonPrefixes();
		if(null != commentPrefix && !commentPrefix.isEmpty()) {
			commentPrefix.forEach(key -> {
				S3Entity s3Entity = new S3Entity();
				String newStr = StringUtils.substring(key, 0, key.length()-1);
				s3Entity.setName(StringUtils.substring(newStr, StringUtils.lastIndexOf(newStr, "/")+1,newStr.length()));
				s3Entity.setKey(key);
				s3Entity.setType("Folder");
				s3Entity.setSize(0);
				listS3Entity.add(s3Entity);
			});
		}
	}
	
	private static void parsingRawFolderData(List<S3Entity> listS3Entity, List<S3ObjectSummary> summaries, String tempPrefix) {
		if(null != summaries) {
			for(int i=0; i < summaries.size() ; i++) {
				if(i == 0 && summaries.get(0).getKey().equalsIgnoreCase(tempPrefix)) {
					//dont try to solve in here 
					//because it is always the prefix of every request. I place this validation for getting more than 1000 ojbects from S3
				}else {
					S3Entity s3Entity = new S3Entity();
					String key = summaries.get(i).getKey();
					s3Entity.setName(StringUtils.substring(key, StringUtils.lastIndexOf(key, "/")+1,key.length()));
					s3Entity.setKey(key);
					s3Entity.setSize(summaries.get(i).getSize());
					s3Entity.setModifiedDate(summaries.get(i).getLastModified());
					s3Entity.setType("File");
					listS3Entity.add(s3Entity);
				}
			}
		}
	}

}
