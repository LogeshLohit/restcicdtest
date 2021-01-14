package com.logesh.archival;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ArchivalController {

	@Autowired
	private ArchivalServiceZip4j archivalSvc;

	//http://localhost:8080/APP/archive?batch=ALL&dir=.output,.check
	
	@GetMapping("archive")
	public String archiveFiles(@RequestParam(value = "batch", required = true) String batchName,
			@RequestParam(value = "dir", required = false) String dir) {
		archivalSvc.archiveFiles(batchName, dir);
		return "Files will be archived in the background.";
	}
}
