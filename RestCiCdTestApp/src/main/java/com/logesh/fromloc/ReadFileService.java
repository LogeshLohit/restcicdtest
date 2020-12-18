package com.logesh.fromloc;

import java.io.File;
import java.io.IOException;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.logesh.results.DbResultsFetcherService;

@Service
public class ReadFileService {

	@Value("${inputFilePath}")
	private String inputFilesFolder;

	@Value("${inputFilePathArchieve}")
	private String inputFilesArchive;

	@Value("${dbResultsFilePath}")
	private String outputFilesFolder;

	@Autowired
	private DbResultsFetcherService svc;

	public String getResults() {

		return readAllFilesFromInputDir();
	}

	private String readAllFilesFromInputDir() {

//		String inputFilesFolder = "D:/My works/Verizon Projects/FETCH-QUERY-RESULTS/fileQuery";
		try {
			// createDirIfNotExist for inputFilesFolder
			// createDirIfNotExist for archievefolder
			// createDirIfNotExist for outputFilesFolder

			String[] allFiles = new File(inputFilesFolder).list();
			FileUtils.cleanDirectory(new File(outputFilesFolder));

			AtomicInteger counter = new AtomicInteger();
			String resultResponse = StringUtils.EMPTY;

			List<String> db2Files = new ArrayList<String>();
			List<String> cassFiles = new ArrayList<String>();

			for (String eachFile : allFiles) {
				if (StringUtils.containsIgnoreCase(eachFile, "DB2"))
					db2Files.add(inputFilesFolder + "/" + eachFile);
				else if (StringUtils.containsIgnoreCase(eachFile, "CASS"))
					cassFiles.add(inputFilesFolder + "/" + eachFile);
			}

			for (String db2FileName : db2Files) {
				String currentOutputFilName = svc.getResults(db2FileName, null);// SERVICE IMPL CALL
				resultResponse += moveFileToOutputDir(currentOutputFilName, db2FileName, counter);
				moveInputFilesToArchive(db2FileName,
						inputFilesArchive + db2FileName.substring(db2FileName.lastIndexOf("/") + 1));
			}

			for (String cassFileName : cassFiles) {
				String currentOutputFilName = svc.getResults(null, cassFileName);
				;// SERVICE IMPL CALL

				resultResponse += moveFileToOutputDir(currentOutputFilName, cassFileName, counter);
				moveInputFilesToArchive(cassFileName,
						inputFilesArchive + cassFileName.substring(cassFileName.lastIndexOf("/") + 1));
			}

//			allFiles.forEach(i -> System.out.println(i.getFileName()));
//			System.out.println("Final :" + resultResponse);
			return preapareStringResponse(counter, resultResponse);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	private void moveInputFilesToArchive(String inputFile, String arhiveLoc) {
		try {
			Files.move(Paths.get(inputFile), Paths.get(arhiveLoc), StandardCopyOption.REPLACE_EXISTING);
			System.out.println("*****" + inputFile + "*" + arhiveLoc);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("Unable to move file:" + e);

			e.printStackTrace();
		}
	}

	private String preapareStringResponse(AtomicInteger counter, String resultResponse) {
		return "Total Number of files processed: " + counter.get() + "\n " + resultResponse;

	}

	private String moveFileToOutputDir(String currentOutputFilName, String destFileName, AtomicInteger counter) {
//		String outputFileDirectory = "";
		try {
			FileUtils.moveFile(FileUtils.getFile(currentOutputFilName), FileUtils.getFile(
					outputFilesFolder + "/DBResults_" + destFileName.substring(destFileName.lastIndexOf("/") + 1)));
			counter.incrementAndGet();
			return outputFilesFolder + "/DBResults_" + destFileName.substring(destFileName.lastIndexOf("/") + 1) + "\n";
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return StringUtils.EMPTY;
	}

	public static void main(String[] args) {
		new ReadFileService().getResults();
	}

}
