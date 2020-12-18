package com.logesh.results;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class DbResultsFetcherController {

	@Autowired
	private DbResultsFetcherService dbService;

	@PostMapping("/upload")
	public ResponseEntity<Resource> readInputFileAndGetResults(
			@RequestParam(name = "db2File", required = false) MultipartFile db2File,
			@RequestParam(name = "cassFile", required = false) MultipartFile cassFile) {
//		System.out.println("Uploaded file at :" + file.getOriginalFilename());
//		Files.readAllBytes(Paths.get(file.getInputStream());

		if (!ObjectUtils.isEmpty(db2File)
				&& StringUtils.isNotBlank(db2File.getOriginalFilename()))
			System.out.println("DB2 file Uploaded, " + db2File.getOriginalFilename());
		if (!ObjectUtils.isEmpty(cassFile) 
				&& StringUtils.isNotBlank(cassFile.getOriginalFilename()))
			System.out.println("Cass file Uploaded, " + cassFile.getOriginalFilename());

		String fileLocation = 
				//"src/main/resources/op-files/fileToCreate.txt";
				dbService.getResults(db2File, cassFile);
//		try {
//			String text = new BufferedReader(new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))
//					.lines().collect(Collectors.joining("\n"));
//
//			System.out.println("Value:\n" + text);
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//
		InputStreamResource resource = null;
		try {
			resource = new InputStreamResource(new FileInputStream(fileLocation));
//				resource = new InputStreamResource(new FileInputStream("src/main/resources/op-files/fileToCreate.txt"));

			System.out.println(resource.getFilename());
			System.out.println(Files.readAllBytes(Paths.get(fileLocation)));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		HttpHeaders header = new HttpHeaders();
		String fileName = fileLocation.substring(fileLocation.lastIndexOf("/") + 1);
		System.out.println("OutPut filename:" + fileName);
		header.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName);
		header.add("Cache-Control", "no-cache, no-store, must-revalidate");
		header.add("Pragma", "no-cache");
		header.add("Expires", "0");

		return ResponseEntity.ok().headers(header)
//	            .contentLength(resource.getInputStream().length())
				.contentType(MediaType.APPLICATION_OCTET_STREAM).body(resource);
//		return "Thank you!";
	}
}
