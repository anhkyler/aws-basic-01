package hs.example.controllers;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.amazonaws.services.s3.model.Bucket;

import hs.example.services.S3Services;



@RestController
public class HelloController {
	@Autowired
	S3Services s3Service;
	@RequestMapping("/persons")
    public String persons(){
        return "Helo Hieu";
    }
	
	@RequestMapping("/")
    public String firstMethod(){
//		List<Bucket> list = s3Service.listBucket();
//		s3Service.listObjectsUnderCertainPath();
//		s3Service.listFolder();
//		List<String> list = Arrays.asList("bkt1-1/ronin pharse.txt","bkt1-1/phantom.txt");
//		s3Service.deleteObjects(list,"controller-2");
//		s3Service.checkValidPath("HHG3080");
//		s3Service.exploreObjects("SVLM");
		s3Service.exploreObjects("");
        return "Helo Hieu";
    }
	
	@RequestMapping("/svlm")
    public String firstMethodSVLM(){
//		List<Bucket> list = s3Service.listBucket();
//		s3Service.listObjectsUnderCertainPath();
//		s3Service.listFolder();
//		List<String> list = Arrays.asList("bkt1-1/ronin pharse.txt","bkt1-1/phantom.txt");
//		s3Service.deleteObjects(list,"controller-2");
//		s3Service.checkValidPath("HHG3080");
//		s3Service.exploreObjects("SVLM");
		s3Service.exploreObjects("SVLM");
        return "Helo Hieu";
    }
	
	@GetMapping("/dynamic/{id}")
    public String firstMethodHHG3080(@PathVariable String id){
//		List<Bucket> list = s3Service.listBucket();
//		s3Service.listObjectsUnderCertainPath();
//		s3Service.listFolder();
//		List<String> list = Arrays.asList("bkt1-1/ronin pharse.txt","bkt1-1/phantom.txt");
//		s3Service.deleteObjects(list,"controller-2");
//		s3Service.checkValidPath("HHG3080");
//		s3Service.exploreObjects("SVLM");
		s3Service.exploreObjects(id);
        return "Helo Hieu";
    }
	
	@RequestMapping("/validpath")
    public String validpath(){
//		List<Bucket> list = s3Service.listBucket();
//		s3Service.listObjectsUnderCertainPath();
//		s3Service.listFolder();
//		List<String> list = Arrays.asList("bkt1-1/ronin pharse.txt","bkt1-1/phantom.txt");
//		s3Service.deleteObjects(list,"controller-2");
//		s3Service.checkValidPath("1.txt");
//		s3Service.checkValidPath("1");
		s3Service.checkValidPath("S3 Test.txt");
//		s3Service.exploreObjects("SVLM");
//		s3Service.exploreObjects("Shipment");
        return "Helo Hieu";
    }
}
