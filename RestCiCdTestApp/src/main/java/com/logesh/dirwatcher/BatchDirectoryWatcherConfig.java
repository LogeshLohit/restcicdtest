package com.logesh.dirwatcher;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BatchDirectoryWatcherConfig {

	@Autowired
	BatchOneOperations one;

	@Autowired
	BatchTwoOperations two;

	@Bean
	public void batchOneWatcher() {
		WatchService watchService;
		try {
			watchService = FileSystems.getDefault().newWatchService();
			
			Map<String, Path> pathsWithBatch = addPathAndRegisterDir(watchService);


			WatchKey key;
			while ((key = watchService.take()) != null) {
				for (WatchEvent<?> event : key.pollEvents()) {
					System.out.println("Event kind:" + event.kind() + ". File affected: " + event.context() + ".");
					checkFileAndStartBatchOperations(event, pathsWithBatch);
				}
				key.reset();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

//		return new CustomDirectoryWatcher();
	}


	private Map<String, Path> addPathAndRegisterDir(WatchService watchService) throws IOException {
		Path pathOne = Paths.get("D:\\My works\\Verizon Projects\\DirWatcher\\1");
		Path pathTwo = Paths.get("D:\\My works\\Verizon Projects\\DirWatcher\\2");

		pathOne.register(watchService, StandardWatchEventKinds.ENTRY_CREATE, StandardWatchEventKinds.ENTRY_DELETE,
				StandardWatchEventKinds.ENTRY_MODIFY);
		pathTwo.register(watchService, StandardWatchEventKinds.ENTRY_CREATE, StandardWatchEventKinds.ENTRY_DELETE,
				StandardWatchEventKinds.ENTRY_MODIFY);

		Map<String, Path> pathsWithBatch = new HashMap<String, Path>();
		pathsWithBatch.put("BATCH1", pathOne);
		pathsWithBatch.put("BATCH2", pathTwo);
		
		return pathsWithBatch;
	}


	private void checkFileAndStartBatchOperations(WatchEvent<?> event, Map<String, Path> pathsWithBatch) {
		if (isTextFile(event)) {
			for (Map.Entry<String, Path> pathInfo : pathsWithBatch.entrySet()) {
				boolean fileExists = new File(pathInfo.getValue().toFile(), event.context().toString()).exists();
				if (fileExists) {
					switch (pathInfo.getKey()) {
					case "BATCH1":
						one.batchOneStart();
						break;
					case "BATCH2":
						two.batchOneStart();
						break;
					default:
						break;
					}
				}
			}
		}
	}

	private boolean isTextFile(WatchEvent<?> event) {
		return null != event && event.context() != null && event.context().toString().contains(".txt");
	}
}
