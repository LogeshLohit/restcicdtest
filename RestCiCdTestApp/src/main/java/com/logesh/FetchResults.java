//package com.logesh;
//
//import java.io.BufferedReader;
//import java.io.FileInputStream;
//import java.io.IOException;
//import java.io.InputStreamReader;
//import java.nio.charset.StandardCharsets;
//import java.nio.file.Files;
//import java.nio.file.Paths;
//import java.util.stream.Collectors;
//
//import org.springframework.core.io.InputStreamResource;
//import org.springframework.core.io.Resource;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.MediaType;
//import org.springframework.http.ResponseEntity;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.multipart.MultipartFile;
//
//@Controller
//public class FetchResults {
//
//	@PostMapping("upload")
//	public ResponseEntity<Resource>  readInputFile(MultipartFile file) {
//		System.out.println("Uploaded file at :" + file.getOriginalFilename());
////		Files.readAllBytes(Paths.get(file.getInputStream());
//		
//	    try {
//			String text = new BufferedReader(
//				      new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))
//				        .lines()
//				        .collect(Collectors.joining("\n"));
//			
//			System.out.println("Value:\n"+text);
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	    
//	    InputStreamResource resource = null;
//		try {
//			resource = new InputStreamResource(new FileInputStream("src/main/resources/op-files/fileToCreate.txt"));
////			resource = new File("src/main/resources/op-files/fileToCreate.txt");
//			
//			System.out.println(resource.getFilename());
//			System.out.println(Files.readAllBytes(Paths.get("src/main/resources/op-files/fileToCreate.txt")));
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//
//        HttpHeaders header = new HttpHeaders();
//        header.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=fileToCreate.txt");
//        header.add("Cache-Control", "no-cache, no-store, must-revalidate");
//        header.add("Pragma", "no-cache");
//        header.add("Expires", "0");
//        
//        
//	    return ResponseEntity.ok()
//	            .headers(header)
////	            .contentLength(resource.getInputStream().length())
//	            .contentType(MediaType.APPLICATION_OCTET_STREAM)
//	            .body(resource);
////		return "Thank you!";
//	}
//}
