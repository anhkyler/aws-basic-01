package hs.example.services;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Component;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.DeleteObjectsRequest;
import com.amazonaws.services.s3.model.ListObjectsRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.S3ObjectSummary;


import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

@Component
public class S3MainService {
	ExecutorService  executorService= Executors.newCachedThreadPool();
	static AWSCredentials credentials = new BasicAWSCredentials();
	AmazonS3 amazonS3;
	
	@PostConstruct
	public void s3init() {
		amazonS3 = AmazonS3ClientBuilder
				  .standard()
				  .withCredentials(new AWSStaticCredentialsProvider(credentials))
				  .withRegion(Regions.US_EAST_1)
				  .build();
	}
	
	
	
//	delete multiple selection objects on S3
//	get list of key. process 1000 items each time.
	public Boolean deleteObjects(List<String> listObjects, String bucket) {
		try {
			System.out.println(listObjects.toString());
			DeleteObjectsRequest deleteObjectRequest = new DeleteObjectsRequest(bucket).withQuiet(true);
			deleteObjectRequest.setKeys(listObjects.stream().map(k -> new DeleteObjectsRequest.KeyVersion(k)).collect(Collectors.toList()));
			amazonS3.deleteObjects(deleteObjectRequest);
			return true;
		}catch(AmazonS3Exception e) {
			System.out.println(e.toString());
			return false;
		}
		
	}
	

	public List<S3ObjectSummary> listObjectsWPrefix(String prefix) {
		if(null == prefix || prefix.equalsIgnoreCase("null"))
			prefix ="";
	    ListObjectsRequest listObjectsRequest = new ListObjectsRequest()
	            .withBucketName("controller-2").withPrefix("home/TMSS/" + prefix);
		ObjectListing listing = amazonS3.listObjects(listObjectsRequest);
		List<S3ObjectSummary> finalList = new ArrayList<>(); 
		List<S3ObjectSummary> summaries = listing.getObjectSummaries();
		while (listing.isTruncated()) {
		   listing = amazonS3.listNextBatchOfObjects (listing);
		   summaries.addAll (listing.getObjectSummaries());
		}
		if(prefix.toUpperCase().contains("RATES") || prefix.toUpperCase().contains("HHG3080") || 
				prefix.toUpperCase().contains("RATEDOWNLOAD") || prefix.toUpperCase().contains("RATEFILETEMPLATE") ||
				prefix.toUpperCase().contains("SHIPMENT") || prefix.toUpperCase().contains("SYNCADAXMLS")) {
			for(int i=1;i<summaries.size();i++) {
				if(summaries.get(i).getKey().endsWith("/"))
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
		
		return finalList;
	}
	
	public List<S3ObjectSummary> getObjectsFromCertainPath(String prefix) {
		List<S3ObjectSummary> finalList = new ArrayList<>();
		try {
			ListObjectsRequest listObjectsRequest = new ListObjectsRequest().withBucketName("controller-2")
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
}
