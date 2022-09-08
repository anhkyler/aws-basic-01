package hs.example.controllers;

import java.io.IOException;
import java.nio.file.Path;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.model.S3ObjectSummary;

import hs.example.entities.S3Entity;
import hs.example.services.S3ParsingObject;
import hs.example.services.S3Service;



/**
 * @author JamesMcKay
 *
 */
@CrossOrigin
@RestController
public class FileController{
	private static Logger logger = LoggerFactory.getLogger(FileController.class);
	private final String baseURI = "/file";

	@Autowired
	S3Service s3Service;

	@CrossOrigin(exposedHeaders="Access-Control-Allow-Origin")
	@RequestMapping(value = baseURI + "/gets3objects", method = RequestMethod.GET)
	public ResponseEntity<?> searchAllObject() {
		try {
			List<S3ParsingObject> listS3Entity = s3Service.getObjects();
			return (new ResponseEntity<List<S3ParsingObject>>(listS3Entity, HttpStatus.OK));
		}catch(Exception e) {
			return new  ResponseEntity<>("ERROR! not able to search objects",HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@CrossOrigin(exposedHeaders="Access-Control-Allow-Origin")
	@RequestMapping(value = baseURI + "/delete", method = RequestMethod.POST)
	public ResponseEntity<?> deleteObjects(@RequestBody ArrayList<String> keys) {
		try {
			if(keys.size()<=0) {
				return ResponseEntity.badRequest().body("WARNING! there is no key for deletion");
			}else {
				boolean deleted = s3Service.deletingObjects(keys);
				return (new ResponseEntity<Boolean>(deleted, HttpStatus.OK));
			}
		}catch(Exception e) {
			return new  ResponseEntity<>("ERROR! not able to delete objects by keys",HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@CrossOrigin(exposedHeaders="Access-Control-Allow-Origin")
	@RequestMapping(value = baseURI + "/prefix", method = RequestMethod.GET)
	  public ResponseEntity<?> searchByPrefix(@RequestParam("prefix") String prefix){
			try {
				logger.info("prefix is" + prefix);
				List<S3ParsingObject> listS3Entity = s3Service.gettingObjectsWithPrefix(prefix);
				return (new ResponseEntity<List<S3ParsingObject>>(listS3Entity, HttpStatus.OK));
			}catch(Exception e){
				return new  ResponseEntity<>("ERROR! not able to get objects by prefix",HttpStatus.INTERNAL_SERVER_ERROR);
			}
	  }
	
	@CrossOrigin(exposedHeaders="Access-Control-Allow-Origin")
	@RequestMapping(value = baseURI + "/download", method = RequestMethod.GET)
	  public ResponseEntity<?> downloadObject(@RequestParam("keyPath") String keyPath){
			try {
				logger.info("prefix is" + keyPath);
				ResponseEntity<?> rs = s3Service.downloadFiles(keyPath);
				return rs;
			}catch(Exception e){
				return new  ResponseEntity<>("ERROR! not able to get objects by key path",HttpStatus.INTERNAL_SERVER_ERROR);
			}
	  }
	
	@CrossOrigin(exposedHeaders="Access-Control-Allow-Origin")
	@RequestMapping(value = baseURI + "/createemptyobject", method = RequestMethod.POST)
	public ResponseEntity<?> createEmptyObject(@RequestBody String key) {
		try {
			logger.info("prefix is" + key);
			if(s3Service.createEmptyObject(key)) {
				return (new ResponseEntity<String>("Empty Folder is created", HttpStatus.OK));
			}else {
				return ResponseEntity.badRequest().body("ERROR! key is not valid or Folder exists");
			}
		}catch(Exception e) {
			return ResponseEntity.badRequest().body("ERROR! not able to create an Empty Folder by key");
		}
	}
	
	@CrossOrigin(exposedHeaders="Access-Control-Allow-Origin")
	@RequestMapping(value = baseURI + "/uploadobject", method = RequestMethod.POST)
	public ResponseEntity<?> createEmptyObject(@RequestParam("file") MultipartFile file, @RequestParam("keyPath") String keyPath, @RequestParam("fileName") String fileName) {
		try {
//			logger.info("prefix is" + key);
//			if(s3Service.createEmptyObject(key)) {
			s3Service.uploadFiles(file, keyPath, fileName);
				return (new ResponseEntity<String>("Empty Folder is created", HttpStatus.OK));
//			}else {
//				return ResponseEntity.badRequest().body("ERROR! key is not valid or Folder exists");
//			}
		}catch(Exception e) {
			return ResponseEntity.badRequest().body("ERROR! not able to create an Empty Folder by key");
		}
	}
	
////	@RequestMapping(value = baseURI + "/loadings3", method = RequestMethod.GET)
////	public ResponseEntity<?> searchAllObject() {
////		try {
////			List<S3Entity> listS3Entity = s3Service.gettingMappingObjects();
////			return (new ResponseEntity<List<S3Entity>>(listS3Entity, HttpStatus.OK));
////		}catch(Exception e) {
////			return new  ResponseEntity<>("ERROR! not able to search objects",HttpStatus.INTERNAL_SERVER_ERROR);
////		}
////	}
	
//	private static final String hhgProcess = "hhg";
//	private static final String frtProcess = "frt";
//	private static final String shipmentProcess = "shipment";
//	private static final String surveyProcess = "survey";
//	private static final String iffProcess = "iff";
//	private static final String documentProcess = "Documents";
//	
//	@RequestMapping(value = baseURI + "/testprint", method = RequestMethod.GET)
//	public ResponseEntity<?> testprint() {
//		try {
//			return (new ResponseEntity<String>("abc", HttpStatus.OK));
//		}catch(Exception e) {
//			return new  ResponseEntity<>("ERROR! not able to search objects",HttpStatus.INTERNAL_SERVER_ERROR);
//		}
//	}
//	
//	@RequestMapping(value = baseURI + "/loadings3", method = RequestMethod.GET)
//	public ResponseEntity<?> searchAllObject() {
//		try {
//			List<S3Entity> listS3Entity = s3Service.gettingMappingObjects();
//			return (new ResponseEntity<List<S3Entity>>(listS3Entity, HttpStatus.OK));
//		}catch(Exception e) {
//			return new  ResponseEntity<>("ERROR! not able to search objects",HttpStatus.INTERNAL_SERVER_ERROR);
//		}
//	}
//	
//	@RequestMapping(value = baseURI + "/delete", method = RequestMethod.POST)
//	public ResponseEntity<?> deleteObjects(@RequestBody ArrayList<String> keys) {
//		System.out.println("in cai nay ra");
//		try {
//			if(keys.size()<=0) {
//				return ResponseEntity.badRequest().body("WARNING! there is no key for deletion");
//			}else {
//				boolean deleted = s3Service.deletingObjects(keys);
//				return (new ResponseEntity<Boolean>(deleted, HttpStatus.OK));
//			}
//		}catch(Exception e) {
//			return new  ResponseEntity<>("ERROR! not able to delete objects by keys",HttpStatus.INTERNAL_SERVER_ERROR);
//		}
//	}
//	
//	@RequestMapping(value = baseURI + "/prefix", method = RequestMethod.GET)
//    public ResponseEntity<?> searchByPrefix(@RequestParam("prefix") String prefix){
//		try {
//			logger.info("prefix is" + prefix);
//			List<S3Entity> listS3Entity = s3Service.gettingObjectsWithPrefix(prefix);
//			return (new ResponseEntity<List<S3Entity>>(listS3Entity, HttpStatus.OK));
//			return new  ResponseEntity<>("ERROR! not able to get objects by prefix",HttpStatus.INTERNAL_SERVER_ERROR);
//		}
//    }
//	
//	@RequestMapping(value = baseURI + "/objectselection", method = RequestMethod.GET)
//	public ResponseEntity<?> getObjects(@RequestParam("prefix") String prefix) {
//		try {
//			logger.info("prefix is" + prefix);
//			if(null == prefix || prefix.equalsIgnoreCase("")) {
//				return ResponseEntity.badRequest().body("ERROR! Key is not a valid argument");
//			}else {
//				List<S3Entity> listS3Entity = s3Service.getObjects(prefix);
//				return (new ResponseEntity<List<S3Entity>>(listS3Entity, HttpStatus.OK));	
//			}
//		}catch(Exception e) {
//			return ResponseEntity.badRequest().body("ERROR! not able to get object by key");
//		}
//	}
//	
//	@RequestMapping(value = baseURI + "/createemptyobject", method = RequestMethod.POST)
//	public ResponseEntity<?> createEmptyObject(@RequestParam("key") String key) {
//		try {
//			logger.info("prefix is" + key);
//			if(s3Service.createEmptyObject(key)) {
//				return (new ResponseEntity<String>("Empty Folder is created", HttpStatus.OK));
//			}else {
//				return ResponseEntity.badRequest().body("ERROR! key is not valid or Folder exists");
//			}
//		}catch(Exception e) {
//			return ResponseEntity.badRequest().body("ERROR! not able to create an Empty Folder by key");
//		}
//	}
}
