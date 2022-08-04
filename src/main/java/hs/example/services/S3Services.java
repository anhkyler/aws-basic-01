package hs.example.services;


import javax.annotation.PostConstruct;

import org.springframework.stereotype.Component;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.ListNextBatchOfObjectsRequest;
import com.amazonaws.services.s3.model.ListObjectsV2Request;
import com.amazonaws.services.s3.model.ListObjectsV2Result;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.google.common.collect.Lists;

import hs.example.entities.S3ObjectEntity;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.*;


@Component
public class S3Services {
	ExecutorService  executorService= Executors.newCachedThreadPool();
	static AWSCredentials credentials = new BasicAWSCredentials(
			  "AKIA5GMUBTZ2OWJ7O6I4", 
			  "n7XSy/TK9R+7KCYyWC9FtT7BJJAZyWq3SLasg17W"
			);
	AmazonS3 amazonS3;
	
	@PostConstruct
	public void s3init() {
		amazonS3 = AmazonS3ClientBuilder
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
			if(s3Object.getSize() < 1 || s3Object.getKey().endsWith("/"))
				return;
			else {
				S3ObjectEntity anObject = new S3ObjectEntity();
				String fileName = s3Object.getKey();
				if(s3Object.getKey().contains("/"))
					anObject.setFileName(fileName.substring(s3Object.getKey().lastIndexOf("/")+1));
				anObject.setFileName(fileName);
				anObject.setSize(anObject.getSize());
//				String lastMoDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(anObject.getModifiedDate());
//				anObject.setModifiedDate(lastMoDate);
				String filePath = s3Object.getKey();
//				anObject.setPath(filePath.substring(0, filePath.lastIndexOf("/")));
				finVal.add(anObject);
			}
		});
		finVal.forEach(a -> {
			System.out.println(a.toString());
		});
		
	}
}
