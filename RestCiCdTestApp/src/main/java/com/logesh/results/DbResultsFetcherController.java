package com.logesh.results;

import java.io.FileInputStream;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class DbResultsFetcherController {

	@Autowired
	private DbResultsFetcherService dbService;

	@PostMapping("/upload")
	public String readInputFileAndGetResults(@RequestParam(name = "db2File", required = false) MultipartFile db2File,
			@RequestParam(name = "cassFile", required = false) MultipartFile cassFile) {
//		System.out.println("Uploaded file at :" + file.getOriginalFilename());
//		Files.readAllBytes(Paths.get(file.getInputStream());
		long startTime = System.currentTimeMillis();
		if (!ObjectUtils.isEmpty(db2File) && StringUtils.isNotBlank(db2File.getOriginalFilename()))
			System.out.println("DB2 file Uploaded, " + db2File.getOriginalFilename());
		if (!ObjectUtils.isEmpty(cassFile) && StringUtils.isNotBlank(cassFile.getOriginalFilename()))
			System.out.println("Cass file Uploaded, " + cassFile.getOriginalFilename());

		String opFileName = dbService.generateOutputFileName();
//		String fileLocation =
		// "src/main/resources/op-files/fileToCreate.txt";
		dbService.moveFilesToInputDir(db2File, cassFile);
		dbService.getResults(
				!ObjectUtils.isEmpty(db2File) && StringUtils.isNotBlank(db2File.getOriginalFilename())
						? db2File.getOriginalFilename()
						: StringUtils.EMPTY,
				!ObjectUtils.isEmpty(cassFile) && StringUtils.isNotBlank(cassFile.getOriginalFilename())
						? cassFile.getOriginalFilename()
						: StringUtils.EMPTY,
				opFileName);
//		try {
//			String text = new BufferedReader(new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))
//					.lines().collect(Collectors.joining("\n"));
//
//			System.out.println("Value:\n" + text);
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
////
//		InputStreamResource resource = null;
//		try {
//			resource = new InputStreamResource(new FileInputStream(fileLocation));
////				resource = new InputStreamResource(new FileInputStream("src/main/resources/op-files/fileToCreate.txt"));
//
////			System.out.println(resource.getFilename());
////			System.out.println(Files.readAllBytes(Paths.get(fileLocation)));
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		System.out.println("***Total time taken: " + (System.currentTimeMillis() - startTime));
//		HttpHeaders header = new HttpHeaders();
//		String fileName = fileLocation.substring(fileLocation.lastIndexOf("/") + 1);
//		System.out.println("OutPut filename:" + fileName);
//		header.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName);
//		header.add("Cache-Control", "no-cache, no-store, must-revalidate");
//		header.add("Pragma", "no-cache");
//		header.add("Expires", "0");
//
//		return ResponseEntity.ok().headers(header)
////	            .contentLength(resource.getInputStream().length())
//				.contentType(MediaType.APPLICATION_OCTET_STREAM).body(resource);
		return "DbResults are being fetched in background. Please find results in , '" + opFileName
				+ ".txt' after few seconds.";
	}

//	public String getResults(MultipartFile db2File, MultipartFile cassFile) {
//		long start = System.currentTimeMillis();
//
//		List<DbResultsModel> db2Content = null;
//		List<DbResultsModel> cassContent = null;
//
//		CompletableFuture<List<DbResultsModel>> db2 = null;
//		CompletableFuture<List<DbResultsModel>> cass = null;
//
//		if (!ObjectUtils.isEmpty(db2File) && StringUtils.isNotBlank(db2File.getOriginalFilename())) {
//			db2Content = dbService.readInputFile(true, false, db2File);
////			db2 = CompletableFuture.supplyAsync(() -> {
////				System.out.println("Thread in which the db2 executes is :" + Thread.currentThread().getName());
////				return readInputFile(true, false, db2File);
////			});
//		}
//		if (!ObjectUtils.isEmpty(cassFile) && StringUtils.isNotBlank(cassFile.getOriginalFilename())) {
//			cassContent = dbService.readInputFile(false, true, cassFile);
////
////			cass = CompletableFuture.supplyAsync(() -> {
////				System.out.println("Thread in which the cass executes is :" + Thread.currentThread().getName());
////				return readInputFile(false, true, cassFile);
////			});
//		}
////		db2.whenComplete((results, e) -> {
////			System.out.println("Total Time:" + ((System.currentTimeMillis()) - start));
////		});
//
////		return null;
//		return dbService.writeContentToFile(db2Content, cassContent);
//	}
}
