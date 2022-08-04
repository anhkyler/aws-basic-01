package hs.example.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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
		s3Service.listObjectsUnderCertainPath();
//		s3Service.listFolder();
        return "Helo Hieu";
    }
}
