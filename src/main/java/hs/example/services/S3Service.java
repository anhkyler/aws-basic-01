package hs.example.services;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.InstanceProfileCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.DeleteObjectsRequest;
import com.amazonaws.services.s3.model.DeleteObjectsRequest.KeyVersion;
import com.amazonaws.services.s3.model.DeleteObjectsResult;
import com.amazonaws.services.s3.model.ListObjectsRequest;
import com.amazonaws.services.s3.model.ListObjectsV2Request;
import com.amazonaws.services.s3.model.ListObjectsV2Result;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.amazonaws.util.IOUtils;

import hs.example.entities.S3Entity;


/**
 * @author PramodBNair
 *
 */
@Service
public class S3Service {
	
	@Autowired
	private HttpServletRequest request;
	
	static String serverName = null;
	static Regions clientRegion = Regions.US_EAST_1;
	static String bucketName = "";
	static AmazonS3 s3Client = null;
	private static Logger logger = LoggerFactory.getLogger(S3Service.class);
	static {
		try {
			AWSCredentials credentials = new BasicAWSCredentials();		
			s3Client = AmazonS3ClientBuilder
					  .standard()
					  .withCredentials(new AWSStaticCredentialsProvider(credentials))
					  .withRegion(Regions.US_EAST_1)
					  .build();
			logger.info("After Creating S3 Client " + s3Client.toString());  
			
		}
		catch (Exception e) {
			logger.error("Error Creating S3 Client 11 " + s3Client + " Error : " + e.getMessage());
			e.printStackTrace(); 
		}
	} 
	
	private void init() {
		logger.info("Init S3 Client 1 " + bucketName);
		bucketName = "east-hs-test";
	}
	//loading page should call this method to load all first level folder and files which belonging to first level.
	public List<S3Entity> gettingMappingObjects(){
		List<S3Entity> listS3Entity = new ArrayList<S3Entity>();
		try {
			init();
			ListObjectsV2Request listObjectsV2Request = null;
			listObjectsV2Request = new ListObjectsV2Request().withBucketName(bucketName)
					.withPrefix("home/TMSS/").withDelimiter("/");
			logger.info("Listing objects by prefix home/TMSS/" + bucketName);
//			if (serverName.equalsIgnoreCase(Constants.CONFIG_KEY_FCS_PRE_PROD)) {
//				listObjectsV2Request = new ListObjectsV2Request().withBucketName(bucketName)
//						.withPrefix("home/TMSS/preprod/").withDelimiter("/");
//				logger.info("Listing objects BY PREFIX home/TMSS/preprod/" + bucketName);
//			} else {
//				listObjectsV2Request = new ListObjectsV2Request().withBucketName(bucketName)
//						.withPrefix("home/TMSS/").withDelimiter("/");
//				logger.info("Listing objects by prefix home/TMSS/" + bucketName);
//			}
			ListObjectsV2Result listing = s3Client.listObjectsV2(listObjectsV2Request);
			List<S3ObjectSummary> listS3ObjectSummary = new ArrayList<>();
			String tempPrefix = listing.getPrefix();
			listS3ObjectSummary.addAll(listing.getObjectSummaries());
			
			while(listing.isTruncated()) {
				listing = s3Client.listObjectsV2(listObjectsV2Request);
				listS3ObjectSummary.addAll(listing.getObjectSummaries());
			};
			parsingRawObjectstoFolder(listS3Entity,listing);
			parsingRawFilesData(listS3Entity, listS3ObjectSummary, tempPrefix);
		} catch (AmazonServiceException e) {
			logger.error(e.toString());
		} catch (SdkClientException e) {
			logger.error(e.toString());
		} catch (Exception e) {
			logger.error(e.toString());
		}
		return listS3Entity;
	}
	
	private void parsingRawObjectstoFolder(List<S3Entity> listS3Entity, ListObjectsV2Result listing) {
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
				System.out.println(s3Entity.toString());
				listS3Entity.add(s3Entity);
			});
		}
	}
	
	private void parsingRawFilesData(List<S3Entity> listS3Entity, List<S3ObjectSummary> summaries, String tempPrefix) {
		if(null != summaries) {
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
					s3Entity.setType("File");
					System.out.println(s3Entity.toString());
					listS3Entity.add(s3Entity);
				}
			}
		}
	}
	//once use click on a link from table
//	public List<S3Entity> getObjects(String prefix){
//		List<S3Entity> listS3Entity = new ArrayList<S3Entity>();
//		if(prefix.trim().contains("home/TMSS/preprod/")) {
//			listS3Entity = gettingObjectsWithPrefix(StringUtils.substring(prefix, ("home/TMSS/preprod/").length(), prefix.length()));
//		}else {
//			listS3Entity = gettingObjectsWithPrefix(StringUtils.substring(prefix, ("home/TMSS/").length(), prefix.length()));
//		}
//		return listS3Entity;
//	}
	
//	when user searching for specific prefix
	public List<S3ParsingObject> gettingObjectsWithPrefix(String prefix) {
		List<S3ParsingObject> listS3Entity = new ArrayList<S3ParsingObject>();
		try {
			init();
			ListObjectsV2Request listObjectsV2Request = null;
			listObjectsV2Request = new ListObjectsV2Request().withBucketName(bucketName)
					.withPrefix(prefix.trim());
//			if (serverName.equalsIgnoreCase(Constants.CONFIG_KEY_FCS_PRE_PROD)) {
//				listObjectsV2Request = new ListObjectsV2Request().withBucketName(bucketName)
//						.withPrefix("home/TMSS/preprod/" + prefix.trim()).withDelimiter("/");
//				logger.info("Listing objects BY PREFIX home/TMSS/preprod/" + bucketName);
//			} else {
//				listObjectsV2Request = new ListObjectsV2Request().withBucketName(bucketName)
//						.withPrefix("home/TMSS/" + prefix.trim()).withDelimiter("/");
//				logger.info("Listing objects by prefix home/TMSS/" + bucketName);
//			}
			
			ListObjectsV2Result listing= s3Client.listObjectsV2(listObjectsV2Request);
			List<S3ObjectSummary > listS3ObjectSummary = new ArrayList<>();
			String tempPrefix = listing.getPrefix();
			listS3ObjectSummary.addAll(listing.getObjectSummaries());
			while (listing.isTruncated()) {
				listing = s3Client.listObjectsV2(listObjectsV2Request);
				listS3ObjectSummary.addAll(listing.getObjectSummaries());
			};
			listS3Entity = parsingObjectToAgGridFormat(listS3ObjectSummary);
		} catch (AmazonServiceException e) {
			logger.error(e.toString());
		} catch (SdkClientException e) {
			logger.error(e.toString());
		} catch (Exception e) {
			logger.error(e.toString());
		}
		return listS3Entity;
	}
	
	//delete one or many obj
	public boolean deletingObjects(List<String> listKeys) {
		boolean isFinished = false;
		if(null != listKeys) {
			try {
				init();
				List<S3ObjectSummary > summaries = new ArrayList<>();
				listKeys.forEach( k -> {
					ListObjectsV2Request  req = new ListObjectsV2Request().withBucketName(bucketName)
							.withPrefix(k);
					ListObjectsV2Result listing= s3Client.listObjectsV2(req);
					
					summaries.addAll(listing.getObjectSummaries());
					while (listing.isTruncated()) {
						listing = s3Client.listObjectsV2(req);
						summaries.addAll(listing.getObjectSummaries());
					};	
				});
				if(summaries.size() == 0) {
					return true;
				}
				HashMap<String, S3ObjectSummary> keyMapping = new HashMap<>();
				for(S3ObjectSummary summary: summaries) {
					keyMapping.put(summary.getKey(), summary);
				}
				ArrayList<KeyVersion> keys = new ArrayList<>();
				List<S3ObjectSummary> tempSummaries =  new ArrayList<>(keyMapping.values());
				for(S3ObjectSummary summary: tempSummaries) {
					System.out.println(summary.toString());
					keys.add(new KeyVersion(summary.getKey()));
				}
				if(keys.size() > 0) {
					DeleteObjectsRequest deleteObjectRequest = new DeleteObjectsRequest(bucketName).withKeys(keys);
					DeleteObjectsResult  delObjRes= s3Client.deleteObjects(deleteObjectRequest);
					int  successfulDeletes = delObjRes.getDeletedObjects().size();
					System.out.println(successfulDeletes);
					isFinished = true;
				}
			} catch (AmazonServiceException e) {
				logger.error(e.toString());
	        } catch (SdkClientException e) {
	        	logger.error(e.toString());
	        } catch (Exception e) {
	        	logger.error(e.toString());
	        }
		}else {isFinished = true;}
		return isFinished;
	}
	
	//create empty object under certain key (aka path)
	public boolean createEmptyObject(String key) {
		boolean isCreated = false;
		if (null != key) {
			try {
				String tempKey = key + "/";
				init();
				boolean isObjExisting = s3Client.doesObjectExist(bucketName, tempKey);
				if (!isObjExisting) {
					ObjectMetadata metadata = new ObjectMetadata();
					metadata.setContentLength(0);
					InputStream emptyContent = new ByteArrayInputStream(new byte[0]);
					PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, tempKey, emptyContent,
							metadata);
					s3Client.putObject(putObjectRequest);
					logger.info("Create an empty object" + tempKey);
					isCreated = true;
				}
			} catch (AmazonServiceException e) {
				logger.error(e.toString());
			} catch (SdkClientException e) {
				logger.error(e.toString());
			} catch (Exception e) {
				logger.error(e.toString());
			}
		}
		return isCreated;
	}
	
	public List<S3ParsingObject> getObjects(){
		List<S3ParsingObject> listS3Entity = new ArrayList<S3ParsingObject>();
		try {
			init();
			ListObjectsV2Request listObjectsV2Request = null;
			listObjectsV2Request = new ListObjectsV2Request().withBucketName(bucketName)
					.withPrefix("home/TMSS/");
			logger.info("Listing objects by prefix home/TMSS/" + bucketName);
//			if (serverName.equalsIgnoreCase(Constants.CONFIG_KEY_FCS_PRE_PROD)) {
//				listObjectsV2Request = new ListObjectsV2Request().withBucketName(bucketName)
//						.withPrefix("home/TMSS/preprod/").withDelimiter("/");
//				logger.info("Listing objects BY PREFIX home/TMSS/preprod/" + bucketName);
//			} else {
//				listObjectsV2Request = new ListObjectsV2Request().withBucketName(bucketName)
//						.withPrefix("home/TMSS/").withDelimiter("/");
//				logger.info("Listing objects by prefix home/TMSS/" + bucketName);
//			}
			ListObjectsV2Result listing = s3Client.listObjectsV2(listObjectsV2Request);
			List<S3ObjectSummary> listS3ObjectSummary = new ArrayList<>();
			listS3ObjectSummary.addAll(listing.getObjectSummaries());
			while(listing.isTruncated()) {
				listing = s3Client.listObjectsV2(listObjectsV2Request);
				listS3ObjectSummary.addAll(listing.getObjectSummaries());
			};
			listS3Entity = parsingObjectToAgGridFormat(listS3ObjectSummary);
			
		} catch (AmazonServiceException e) {
			logger.error(e.toString());
		} catch (SdkClientException e) {
			logger.error(e.toString());
		} catch (Exception e) {
			logger.error(e.toString());
		}
		return listS3Entity;
	}
	private List<S3ParsingObject> parsingObjectToAgGridFormat(List<S3ObjectSummary> listS3ObjectSummary) {
		 HashMap<String, S3ParsingObject> mapKey = new HashMap<>();
		 int counter = 1;
		 for(S3ObjectSummary parsingObj : listS3ObjectSummary) {
			 double keySize = parsingObj.getSize();
			 Date modifiedDate = parsingObj.getLastModified();
			 String []splStr = parsingObj.getKey().split("/");
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
		 List<S3ParsingObject> listS3Entity = new ArrayList<>(mapKey.values());
		 listS3Entity.forEach(a -> {
			 String []splStr = a.getFilePath().split("/");
			 a.setParsePath(new ArrayList<String>(Arrays.asList(splStr)));
			 System.out.println(a.toString());
		 });
		 return listS3Entity;
	}
	
	public ResponseEntity<?> downloadFiles(String key) {
		// TODO Business Type is not used. TO be removed later
		init();
		OutputStream outStream = null;
		try {
			// Download a file as a new object with ContentType and title specified.
			S3Object s3object = s3Client.getObject(bucketName, key); // fileObjKeyName="FRT/SVLM/test1.xlsx"
			S3ObjectInputStream inputStream = s3object.getObjectContent();
			byte[] bytes = IOUtils.toByteArray(inputStream);
			String fileNameOut = URLEncoder.encode(key, "UTF-8").replaceAll("\\+", "%20");
			HttpHeaders httpHeaders = new HttpHeaders();

			String contentType = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
			httpHeaders.setContentType(MediaType.parseMediaType(contentType));
			// httpHeaders.setContentType(MediaType.APPLICATION_OCTET_STREAM);
			httpHeaders.setContentLength(bytes.length);
			httpHeaders.setAccessControlAllowHeaders(Arrays.asList("Content-Disposition"));
			ContentDisposition contentDisposition = ContentDisposition.builder("attachment").filename(fileNameOut)
					.build();
			httpHeaders.setContentDisposition(contentDisposition);
			return new ResponseEntity<>(bytes, httpHeaders, HttpStatus.OK);
		} catch (AmazonServiceException e) {
			// The call was transmitted successfully, but Amazon S3 couldn't process
			// it, so it returned an error response.
			e.printStackTrace();
			return new ResponseEntity<>("AmazonServiceException : " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (SdkClientException e) {
			// Amazon S3 couldn't be contacted for a response, or the client
			// couldn't parse the response from Amazon S3.
			e.printStackTrace();
			return new ResponseEntity<>("SdkClientException : " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (IOException e) {
			// Amazon S3 couldn't be contacted for a response, or the client
			// couldn't parse the response from Amazon S3.
			e.printStackTrace();
			return new ResponseEntity<>("IOException : " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		} catch (Exception e) {
			// Amazon S3 couldn't be contacted for a response, or the client
			// couldn't parse the response from Amazon S3.
			e.printStackTrace();
			return new ResponseEntity<>("Exception : " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		} finally {
			try {
				/*
				 * if (outStream != null) outStream.close();
				 */
			} catch (Exception e2) {
				// TODO: handle exception
			}

		}
	}
	
	public void uploadFiles(MultipartFile file, String keypath, String fileName) {
		init();
		String fileObjKeyName = null;
		try {
			File fileConvert = convertMultiPartToFile(file);
			
			// Upload a file as a new object with ContentType and title specified.
			logger.info("Before PutObjectRequestfileObjKeyName : " + keypath);
			PutObjectRequest request = new PutObjectRequest(bucketName, keypath+"/"+fileName, fileConvert);
//			ObjectMetadata metadata = new ObjectMetadata();
//			metadata.setContentType("plain/text");
//			metadata.addUserMetadata("x-amz-meta-title", "someTitle");
//			request.setMetadata(metadata);
			logger.info("Before PutObjectResult : " + request.getBucketName() + " " + request.getKey());
			PutObjectResult result = s3Client.putObject(request);
			logger.info("After PutObjectResult : " + request.getBucketName() + " " + request.getKey());
			System.out.println(result.toString());
		} catch (AmazonServiceException e) {
			logger.info("uploadFiles   AmazonServiceException");
			e.printStackTrace();
		} catch (SdkClientException e) {
			logger.info("uploadFiles   SdkClientException");
			e.printStackTrace();
		}	catch(Exception e) {
			logger.info("uploadFiles converting File");
			e.printStackTrace();
		}
	}
	
	private File convertMultiPartToFile(MultipartFile file) throws IOException {
	    File convFile = new File(file.getOriginalFilename());
	    FileOutputStream fos = new FileOutputStream(convFile);
	    fos.write(file.getBytes());
	    fos.close();
	    return convFile;
	}
}
