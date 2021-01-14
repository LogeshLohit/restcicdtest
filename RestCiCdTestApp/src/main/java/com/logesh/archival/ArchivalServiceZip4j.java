package com.logesh.archival;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import net.lingala.zip4j.ZipFile;

@Service
public class ArchivalServiceZip4j {

	private final static String BATCH_DIR = "D:\\My works\\zipTest\\batch";

	private final static String destZipPath = "D:\\\\My works\\zipTest\\\\batch\\BackUp\\";

	static List<String> batchNames = Arrays.asList("DIR1", "DIR1", "DIR1");

//	public static void main(String[] args) throws IOException {
//
//		List<String> listOfDirsToBeZipped = new ArrayList<String>();
//		listOfDirsToBeZipped.add(".done");
//		listOfDirsToBeZipped.add(".output");
//
//		// for all batch
////		String batchToBeZipped = batchNames.get(0);
//
//		batchNames.stream().forEach(eachBatch -> {
//			new ArchivalServiceZip4j().zipDir(StringUtils.join(BATCH_DIR, "/", eachBatch), listOfDirsToBeZipped,
//					destZipPath);
//		});
//	}

	/**
	 * @param srcBatchPath - should be 'opt/BT/<BATCH>'
	 * @param dirsToZip    - should be ['done','output']
	 * @param destZipPath  - should be 'opt/batch/BackUp'
	 */
	public void zipDir(String srcBatchPath, List<String> dirsToZip, String destZipPath) {
		// Check whether srcBatchPath exists, create if not exists
		// Check whether destZipPath exists, create if not exists

		createDirIfNotExists(srcBatchPath);
		createDirIfNotExists(destZipPath);

		Path batchSrcPath = Paths.get(srcBatchPath);

		if (isFileExistsByPath(batchSrcPath)) {
			dirsToZip.stream().forEach(dir -> {
				Path dirToZip = Paths.get(srcBatchPath, dir);
				if (isFileExistsByPath(dirToZip) && isDirCheck(dirToZip)) {
					try {
						if (Files.list(dirToZip).count() > 0) {
							String destZipFileName = StringUtils.join(destZipPath, "/", batchSrcPath.getFileName(), dir,
									"_", getCurrentTimeStamp(), ".zip");
							new ZipFile(destZipFileName).addFolder(dirToZip.toFile());
							System.out.println(
									"Successfully zipped :" + destZipFileName + ", to :" + dirToZip.getFileName());
							cleanDir(dirToZip);
						} else {
							System.out
									.println("No files found in Dir :" + dir + ", under: " + srcBatchPath + " to zip.");
						}
					} catch (IOException e) {
						System.out.println(
								"Expception occured while trying to zip:" + dir + ", exception :" + e.getMessage());
						e.printStackTrace();
					}
				} else {
					System.out.println("No Direcotry found to zip in :" + dirToZip.toString());
				}
			});
		}
	}

	private void cleanDir(Path dirPath) {
		try {
			FileUtils.cleanDirectory(dirPath.toFile());
		} catch (IOException e) {
			System.out.println("Unable to delete all files under:" + dirPath.toString());
			e.printStackTrace();
		}
	}

	private void createDirIfNotExists(String path) {

		if (!isFileExistsCheck(path)) {
			System.out.println("Creating new directory at :" + path);
			try {
				Files.createDirectory(Paths.get(path));
			} catch (IOException e) {
				System.out.println("Unable to create a directory at :" + path + ", exception : " + e.getMessage());
				e.printStackTrace();
			}
		} else {
			System.out.println("Dir already exists at:" + path);
		}
	}

	private boolean isFileExistsCheck(String path) {
		return Paths.get(path).toFile().exists();
	}

	private boolean isFileExistsByPath(Path path) {
		return path.toFile().exists();
	}

	private boolean isDirCheck(Path path) {
		return Files.isDirectory(path);
	}

	private String getCurrentTimeStamp() {
		Date date = new Date();
		SimpleDateFormat currentDate = new SimpleDateFormat("dd-MM-yyyy-hh-mm-ss-SSS");
		return currentDate.format(date);
	}

	public void archiveFiles(String batchName, String dir) {
		List<String> listOfDirsToBeZipped = new ArrayList<String>();
		listOfDirsToBeZipped.add(".done");

		if (StringUtils.isNotBlank(dir))
			listOfDirsToBeZipped.addAll(Arrays.asList(dir.split(",")));

		if (StringUtils.isNotBlank(batchName)) {
			if (StringUtils.equalsIgnoreCase(batchName, "ALL"))
				batchNames.stream().forEach(eachBatch -> {
					zipDir(StringUtils.join(BATCH_DIR, "/", eachBatch), listOfDirsToBeZipped, destZipPath);
				});
			else {
				zipDir(StringUtils.join(BATCH_DIR, "/", batchName), listOfDirsToBeZipped, destZipPath);
			}
		}
	}
}