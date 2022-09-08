package hs.example.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import hs.example.services.ReadingJsonService;
import hs.example.services.S3ParsingObject;
//import hs.example.services.S3Services;



@RestController
public class HelloController {
//	@Autowired
//	S3Services s3Service;
	
	@Autowired
	ReadingJsonService readingJsonService;
	
	
	@RequestMapping("/persons")
    public String persons(){
        return "Helo Hieu";
    }
	
	@CrossOrigin(exposedHeaders="Access-Control-Allow-Origin")
	@GetMapping("/mapping")
	public ResponseEntity<?> getFakeObjs() {
		try {
			return (new ResponseEntity<List<S3ParsingObject>>(readingJsonService.printS3Objects(), HttpStatus.OK));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return ResponseEntity.badRequest().body("ERROR! not able to get object by key");
		}
	}
	
	@GetMapping("/")
    public String firstMethod(){
//		List<Bucket> list = s3Service.listBucket();
//		s3Service.listObjectsUnderCertainPath();
//		s3Service.listFolder();
//		List<String> list = Arrays.asList("bkt1-1/ronin pharse.txt","bkt1-1/phantom.txt");
//		s3Service.deleteObjects(list,"controller-2");
//		s3Service.checkValidPath("HHG3080");
//		s3Service.exploreObjects("SVLM");
//		s3Service.exploreObjects("");
        return "Helo Hieu";
    }
////	
////	@RequestMapping("/svlm")
////    public String firstMethodSVLM(){
//////		List<Bucket> list = s3Service.listBucket();
//////		s3Service.listObjectsUnderCertainPath();
//////		s3Service.listFolder();
//////		List<String> list = Arrays.asList("bkt1-1/ronin pharse.txt","bkt1-1/phantom.txt");
//////		s3Service.deleteObjects(list,"controller-2");
//////		s3Service.checkValidPath("HHG3080");
//////		s3Service.exploreObjects("SVLM");
////		s3Service.exploreObjects("SVLM");
////        return "Helo Hieu";
////    }
////	
////	@GetMapping("/dynamic/{id}")
////    public String firstMethodHHG3080(@PathVariable String id){
//////		List<Bucket> list = s3Service.listBucket();
//////		s3Service.listObjectsUnderCertainPath();
//////		s3Service.listFolder();
//////		List<String> list = Arrays.asList("bkt1-1/ronin pharse.txt","bkt1-1/phantom.txt");
//////		s3Service.deleteObjects(list,"controller-2");
//////		s3Service.checkValidPath("HHG3080");
//////		s3Service.exploreObjects("SVLM");
////		if(id.contains("7&7")) {
////			String convertRequest = id.replaceAll("7&7", "/");
////			s3Service.exploreObjects(convertRequest);
////		}else
////			s3Service.exploreObjects(id);
////		
////        return "Helo Hieu";
////    }
////	
////	@RequestMapping("/validpath")
////    public String validpath(){
//////		List<Bucket> list = s3Service.listBucket();
//////		s3Service.listObjectsUnderCertainPath();
//////		s3Service.listFolder();
//////		List<String> list = Arrays.asList("bkt1-1/ronin pharse.txt","bkt1-1/phantom.txt");
//////		s3Service.deleteObjects(list,"controller-2");
//////		s3Service.checkValidPath("1.txt");
//////		s3Service.checkValidPath("1");
////		s3Service.checkValidPath("S3 Test.txt");
//////		s3Service.exploreObjects("SVLM");
//////		s3Service.exploreObjects("Shipment");
////        return "Helo Hieu";
////    }
////	
////
////	
////	@GetMapping("/file/{file}")
////	public String searhFile(@PathVariable String file) {
////		s3Service.isAValidObject(file);
////        return "Helo Hieu";
////	}
////	
////	
////	
////	must have / at the end of search string. From UI must convert to 7&7 for /
//	@GetMapping("/prefix")
//    public ResponseEntity<?> searchWPrefix(@RequestParam(required = false) String prefix){
//		try {
//			List<S3ObjectSummary> list = s3Service.listObjectsWPrefix(prefix);
//			return (new ResponseEntity<List<S3ObjectSummary>>(list, HttpStatus.OK));
//		}catch(Exception e) {
//			return ResponseEntity.badRequest().body("ERROR! not able to search object by key");
//		}
//    }
//	
//	@GetMapping("/getobject")
//	public ResponseEntity<?> getObjects(@RequestParam String key) {
//		try {
//			if(null == key || key.equalsIgnoreCase("")) {
//				return ResponseEntity.badRequest().body("ERROR! Key is not a valid argument");
//			}else {
//				List<S3ObjectSummary> list = s3Service.getObjectsFromCertainPath(key);
//				return (new ResponseEntity<List<S3ObjectSummary>>(list, HttpStatus.OK));	
//			}
//		}catch(Exception e) {
//			return ResponseEntity.badRequest().body("ERROR! not able to get object by key");
//		}
//        
//	}
//	
////	key must be a full path to a file. For example "home/TMSS/SVLM/Rates/1.txt"
//	@PostMapping("/delete")
//	public ResponseEntity<?> deleteObjects(@RequestBody List<String> key) {
//		try {
//			if(key.size()<=0) {
//				return ResponseEntity.badRequest().body("WARNING! there is no key for deletion");
//			}else {
//				boolean deleted = s3Service.deleteObjects(key);
//				return (new ResponseEntity<Boolean>(deleted, HttpStatus.OK));
//			}
//			
//		}catch(Exception e) {
//			return ResponseEntity.badRequest().body("ERROR! not able to delete objects by keys");
//		}
//		
//	}
//	
//	@GetMapping("/searchAllObjects")
//	public ResponseEntity<?> searchAllObject(@RequestParam String searchString) {
//		try {
//			List<S3ObjectSummary> list = s3Service.searchAllSubFolder(searchString);
//			return (new ResponseEntity<List<S3ObjectSummary>>(list, HttpStatus.OK));
//		}catch(Exception e) {
//			return ResponseEntity.badRequest().body("ERROR! not able to delete objects by keys");
//		}
//		
//	}
//	
//	@GetMapping("/testlisting")
//	public ResponseEntity<?> getFiles() {
//		try {
//			List<S3ObjectSummary> list = null;
//			s3Service.testFindObject();
//			return (new ResponseEntity<List<S3ObjectSummary>>(list, HttpStatus.OK));
//		}catch(Exception e) {
//			return ResponseEntity.badRequest().body("ERROR! not able to delete objects by keys");
//		}
//		
//	}
}
