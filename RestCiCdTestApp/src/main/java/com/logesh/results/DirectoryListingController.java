package com.logesh.results;

import java.io.File;
import java.io.FileInputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DirectoryListingController {

	private final String outputDirectory = "D:\\My works\\Verizon Projects\\FETCH-QUERY-RESULTS\\output\\";

	@GetMapping("getFile")
	public ResponseEntity<Object> getFileByName(@RequestParam("name") String fileName) {
		if (!fileName.toLowerCase().endsWith(".txt"))
			fileName = fileName.concat(".txt");

		// CREATE DIR IF NOT EXISTS
		String fileLocation = StringUtils.join(outputDirectory, fileName);

		if (!Files.exists(Paths.get(fileLocation))) {
			return ResponseEntity.ok().body("File Not Found!");
		}

		InputStreamResource resource = null;
		try {
			resource = new InputStreamResource(new FileInputStream(fileLocation));
		} catch (Exception e) {
			e.printStackTrace();
		}
		HttpHeaders header = new HttpHeaders();

		header.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName);
		header.add("Cache-Control", "no-cache, no-store, must-revalidate");
		header.add("Pragma", "no-cache");
		header.add("Expires", "0");

		return ResponseEntity.ok().headers(header).contentType(MediaType.APPLICATION_OCTET_STREAM).body(resource);
	}

	@GetMapping("listFiles")
	public String getAllFileNames() {
		// CREATE DIR IF NOT EXISTS
		List<String> allFiles = FileUtils.listFiles(Paths.get(outputDirectory).toFile(), null, false).stream()
				.map(File::getName).collect(Collectors.toList());
		return allFiles.isEmpty() ? StringUtils.join("-- No Files Found -- ,EMPTY DIRECTORY")
				: StringUtils.join(allFiles, ", ");
	}
}
