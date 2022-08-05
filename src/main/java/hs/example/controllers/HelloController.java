package hs.example.controllers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.amazonaws.services.s3.transfer.internal.UploadPartRequestFactory;

import hs.example.services.S3Services;



@RestController
public class HelloController {
	@Autowired
	S3Services s3Service;
	@RequestMapping("/persons")
    public String persons(){
        return "Helo Hieu";
    }
	
//	@RequestMapping("/")
//    public String firstMethod(){
////		List<Bucket> list = s3Service.listBucket();
////		s3Service.listObjectsUnderCertainPath();
////		s3Service.listFolder();
////		List<String> list = Arrays.asList("bkt1-1/ronin pharse.txt","bkt1-1/phantom.txt");
////		s3Service.deleteObjects(list,"controller-2");
////		s3Service.checkValidPath("HHG3080");
////		s3Service.exploreObjects("SVLM");
//		s3Service.exploreObjects("");
//        return "Helo Hieu";
//    }
//	
//	@RequestMapping("/svlm")
//    public String firstMethodSVLM(){
////		List<Bucket> list = s3Service.listBucket();
////		s3Service.listObjectsUnderCertainPath();
////		s3Service.listFolder();
////		List<String> list = Arrays.asList("bkt1-1/ronin pharse.txt","bkt1-1/phantom.txt");
////		s3Service.deleteObjects(list,"controller-2");
////		s3Service.checkValidPath("HHG3080");
////		s3Service.exploreObjects("SVLM");
//		s3Service.exploreObjects("SVLM");
//        return "Helo Hieu";
//    }
//	
//	@GetMapping("/dynamic/{id}")
//    public String firstMethodHHG3080(@PathVariable String id){
////		List<Bucket> list = s3Service.listBucket();
////		s3Service.listObjectsUnderCertainPath();
////		s3Service.listFolder();
////		List<String> list = Arrays.asList("bkt1-1/ronin pharse.txt","bkt1-1/phantom.txt");
////		s3Service.deleteObjects(list,"controller-2");
////		s3Service.checkValidPath("HHG3080");
////		s3Service.exploreObjects("SVLM");
//		if(id.contains("7&7")) {
//			String convertRequest = id.replaceAll("7&7", "/");
//			s3Service.exploreObjects(convertRequest);
//		}else
//			s3Service.exploreObjects(id);
//		
//        return "Helo Hieu";
//    }
//	
//	@RequestMapping("/validpath")
//    public String validpath(){
////		List<Bucket> list = s3Service.listBucket();
////		s3Service.listObjectsUnderCertainPath();
////		s3Service.listFolder();
////		List<String> list = Arrays.asList("bkt1-1/ronin pharse.txt","bkt1-1/phantom.txt");
////		s3Service.deleteObjects(list,"controller-2");
////		s3Service.checkValidPath("1.txt");
////		s3Service.checkValidPath("1");
//		s3Service.checkValidPath("S3 Test.txt");
////		s3Service.exploreObjects("SVLM");
////		s3Service.exploreObjects("Shipment");
//        return "Helo Hieu";
//    }
//	
//
//	
//	@GetMapping("/file/{file}")
//	public String searhFile(@PathVariable String file) {
//		s3Service.isAValidObject(file);
//        return "Helo Hieu";
//	}
//	
//	
//	
//	must have / at the end of search string. From UI must convert to 7&7 for /
	@GetMapping("/prefix/{prefix}")
    public ResponseEntity<?> searchWPrefix(@PathVariable String prefix){
		try {
			String convertRequest = prefix.replaceAll("7&7", "/");
			List<S3ObjectSummary> list = s3Service.listObjectsWPrefix(convertRequest);
			return (new ResponseEntity<List<S3ObjectSummary>>(list, HttpStatus.OK));
		}catch(Exception e) {
			return ResponseEntity.badRequest().body("ERROR! not able to search object by key");
		}
    }
	
	@GetMapping("/getobject/{key}")
	public ResponseEntity<?> getObjects(@PathVariable String key) {
		try {
			String convertRequest = key.replaceAll("7&7", "/");
			List<S3ObjectSummary> list = s3Service.getObjectsFromCertainPath(convertRequest);
			return (new ResponseEntity<List<S3ObjectSummary>>(list, HttpStatus.OK));
		}catch(Exception e) {
			return ResponseEntity.badRequest().body("ERROR! not able to get object by key");
		}
        
	}
	
//	key must be a full path to a file. For example "home/TMSS/SVLM/Rates/1.txt"
	@GetMapping("/delete")
	public ResponseEntity<?> deleteObjects() {
		try {
			List<String> deleteO = new ArrayList<>();
			deleteO.add("home/TMSS/SVLM/Rates/1.txt");
			boolean deleted = s3Service.deleteObjects(deleteO,"controller-2");
			return (new ResponseEntity<Boolean>(deleted, HttpStatus.OK));
		}catch(Exception e) {
			return ResponseEntity.badRequest().body("ERROR! not able to delete objects by keys");
		}
		
	}
}
