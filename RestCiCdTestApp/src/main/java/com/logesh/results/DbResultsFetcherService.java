package com.logesh.results;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
public class DbResultsFetcherService {

	@Value("${cassSchema}")
	private String cassSchema;

	@Value("${db2Schema}")
	private String db2Schema;

	@Value("${dbResultsFilePath}")
	private String dbResultsFilePath;

	public List<DbResultsModel> readInputFile(boolean isDb2File, boolean isCassFile, MultipartFile file) {
//		String cassSchema = "CASS";
//		String db2Schema = "DB2";
		System.out.println(db2Schema + " " + cassSchema);
		List<DbResultsModel> allQueryResults = new ArrayList<DbResultsModel>();
		try {
			String inputFileContent = new String(file.getBytes());
			System.out.println("File Content:" + inputFileContent);
//					new String(Files
//					.readAllBytes(Paths.get("D:\\My works\\Verizon Projects\\FETCH-QUERY-RESULTS\\CASS-query.txt")));
			for (String eachQuery : inputFileContent.trim().split(";")) {
//				//System.out.println("Query:" + eachQuery);

				if (StringUtils.startsWithIgnoreCase(eachQuery.trim(), "select")) {
//					//System.out.println("Starts with Select:" + eachQuery.trim());

					String[] queryStartsFrom = eachQuery.trim().split("(?i)FROM");

					eachQuery = !checkHasWhereClause(queryStartsFrom)
							? appendLimitCondition(isDb2File, isCassFile, eachQuery)
							: eachQuery;
					String tableName = queryStartsFrom[1].split("(?i)WHERE")[0];
					System.out.println("Table Name;" + tableName);

					eachQuery = tableName.contains(".")
							? getQueryByDbSchema(isDb2File, isCassFile, eachQuery, tableName,
									StringUtils.split(tableName, ".")[0].trim())
							: getQueryByDbSchema(isDb2File, isCassFile, eachQuery, tableName, tableName.trim());
				}
				System.out.println("Final Query:" + eachQuery.trim());

				DbResultsModel queryModel = new DbResultsModel(eachQuery);
				DbResultsModel queryRsesults = null;
				queryRsesults = invokeDB(queryModel);
				allQueryResults.add(queryRsesults);
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return allQueryResults;
	}

	private String appendLimitCondition(boolean isDb2File, boolean isCassFile, String eachQuery) {
		if (isDb2File)
			eachQuery = eachQuery.concat(" fetch first 10 rows");
		else if (isCassFile)
			eachQuery = eachQuery.concat(" limit 10");
		return eachQuery;
	}

	private boolean checkHasWhereClause(String[] queryStartsFrom) {
//		System.out.println("***Has Where check:" +
		// Arrays.toString(queryStartsFrom[1].split("(?i)WHERE")));
		return queryStartsFrom[1].split("(?i)WHERE").length > 1;
	}

	private DbResultsModel invokeDB(DbResultsModel queryModel) {
		// PERFROM DB CALL HERE
		// RESULT WILL LIST<HASHMAP>

		List<Map<String, Object>> sampleDbResult = getInput();
		String opFileContent = prepareOutputFileContent(sampleDbResult);
		queryModel.setResults(opFileContent);
		return queryModel;
	}

	private String getQueryByDbSchema(boolean isDb2File, boolean isCassFile, String eachQuery, String tableName,
			String tableNameWithSchema) {
		if (isDb2File) {
			return eachQuery.replace(tableName.trim(), db2Schema + "." + tableNameWithSchema);
		} else if (isCassFile) {
			return eachQuery.replace(tableName.trim(), cassSchema + "." + tableNameWithSchema);
		}
		return null;
	}

	//NEW MTD
	public String getResults(String db2File, String cassFile) {
		long start = System.currentTimeMillis();

		List<DbResultsModel> db2Content = null;
		List<DbResultsModel> cassContent = null;

		CompletableFuture<List<DbResultsModel>> db2 = null;
		CompletableFuture<List<DbResultsModel>> cass = null;

		if (!ObjectUtils.isEmpty(db2File)) {
			db2Content = readInputFile(true, false, db2File);
//			db2 = CompletableFuture.supplyAsync(() -> {
//				System.out.println("Thread in which the db2 executes is :" + Thread.currentThread().getName());
//				return readInputFile(true, false, db2File);
//			});
		}
		if (!ObjectUtils.isEmpty(cassFile)) {
			cassContent = readInputFile(false, true, cassFile);
//
//			cass = CompletableFuture.supplyAsync(() -> {
//				System.out.println("Thread in which the cass executes is :" + Thread.currentThread().getName());
//				return readInputFile(false, true, cassFile);
//			});
		}

//		db2.whenComplete((results, e) -> {
//			System.out.println("Total Time:" + ((System.currentTimeMillis()) - start));
//		});

//		return null;
		return writeContentToFile(db2Content, cassContent);
	}

	private List<DbResultsModel> readInputFile(boolean isDb2File, boolean isCassFile, String file) {
		// String cassSchema = "CASS";
//		String db2Schema = "DB2";
		System.out.println(db2Schema + " " + cassSchema);
		List<DbResultsModel> allQueryResults = new ArrayList<DbResultsModel>();
		try {
			String inputFileContent = new String(Files.readAllBytes(Paths.get(file)));
			System.out.println("File Content:" + inputFileContent);
//					new String(Files
//					.readAllBytes(Paths.get("D:\\My works\\Verizon Projects\\FETCH-QUERY-RESULTS\\CASS-query.txt")));
			for (String eachQuery : inputFileContent.trim().split(";")) {
//				//System.out.println("Query:" + eachQuery);

				if (StringUtils.startsWithIgnoreCase(eachQuery.trim(), "select")) {
//					//System.out.println("Starts with Select:" + eachQuery.trim());

					String[] queryStartsFrom = eachQuery.trim().split("(?i)FROM");

					eachQuery = !checkHasWhereClause(queryStartsFrom)
							? appendLimitCondition(isDb2File, isCassFile, eachQuery)
							: eachQuery;
					String tableName = queryStartsFrom[1].split("(?i)WHERE")[0];
					System.out.println("Table Name;" + tableName);

					eachQuery = tableName.contains(".")
							? getQueryByDbSchema(isDb2File, isCassFile, eachQuery, tableName,
									StringUtils.split(tableName, ".")[0].trim())
							: getQueryByDbSchema(isDb2File, isCassFile, eachQuery, tableName, tableName.trim());
				}
				System.out.println("Final Query:" + eachQuery.trim());

				DbResultsModel queryModel = new DbResultsModel(eachQuery);
				DbResultsModel queryRsesults = null;
				queryRsesults = invokeDB(queryModel);
				allQueryResults.add(queryRsesults);
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return allQueryResults;

	}

	public String getResults(MultipartFile db2File, MultipartFile cassFile) {
		long start = System.currentTimeMillis();

		List<DbResultsModel> db2Content = null;
		List<DbResultsModel> cassContent = null;

		CompletableFuture<List<DbResultsModel>> db2 = null;
		CompletableFuture<List<DbResultsModel>> cass = null;

		if (!ObjectUtils.isEmpty(db2File) && StringUtils.isNotBlank(db2File.getOriginalFilename())) {
			db2Content = readInputFile(true, false, db2File);
//			db2 = CompletableFuture.supplyAsync(() -> {
//				System.out.println("Thread in which the db2 executes is :" + Thread.currentThread().getName());
//				return readInputFile(true, false, db2File);
//			});
		}
//		if (!ObjectUtils.isEmpty(cassFile) && StringUtils.isNotBlank(cassFile.getOriginalFilename())) {
		cassContent = readInputFile(false, true, cassFile);
//
//			cass = CompletableFuture.supplyAsync(() -> {
//				System.out.println("Thread in which the cass executes is :" + Thread.currentThread().getName());
//				return readInputFile(false, true, cassFile);
//			});
//		}
//		db2.whenComplete((results, e) -> {
//			System.out.println("Total Time:" + ((System.currentTimeMillis()) - start));
//		});

//		return null;
		return writeContentToFile(db2Content, cassContent);
	}

	private String writeContentToFile(List<DbResultsModel> db2Content, List<DbResultsModel> cassContent) {
		// Get the file reference
		String fileLocation = dbResultsFilePath + "DbResults_" + getCurrentTimeStamp() + ".txt";

		try {
			new java.io.File(fileLocation);

			BufferedWriter writer = new BufferedWriter(new FileWriter(fileLocation));

			if (!ObjectUtils.isEmpty(db2Content) && !db2Content.isEmpty()) {
				writer.write("*** DB2 RESULTS ***\n");

				for (DbResultsModel model : db2Content) {
					writer.write(model.getQuery());
					writer.write("\n");
					writer.write(model.getResults());
				}
				writer.write("\n*** DB2 RESULTS END ***\n\n");
			}

			if (!ObjectUtils.isEmpty(cassContent) && !cassContent.isEmpty()) {
				writer.write("*** CASSANDRA RESULTS ***\n");
				for (DbResultsModel model : cassContent) {
					writer.write(model.getQuery());
					writer.write("\n");
					writer.write(model.getResults());
				}
				writer.write("\n*** CASSANDRA RESULTS END ***");
			}
			writer.close();
			System.out.println("file created by new mtd");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return fileLocation;
	}

	public List<Map<String, Object>> getInput() {

		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		List<Map<String, Object>> resultSet = new ArrayList<>();

		Map<String, Object> inputQuery = new HashMap<String, Object>();
		inputQuery.put("npanxx", "123456");
		inputQuery.put("tln", "6676");
		inputQuery.put("id", 1);
		inputQuery.put("iccid", null);
		resultSet.add(inputQuery);

		Map<String, Object> inputQuery2 = new HashMap<String, Object>();
		inputQuery2.put("npanxx", "878932");
		inputQuery2.put("tln", "9999");
		inputQuery2.put("iccid", "928374983798488");
		inputQuery2.put("id", 1);

		resultSet.add(inputQuery2);

		return resultSet;
	}

	private synchronized String prepareOutputFileContent(List<Map<String, Object>> result) {

		StringBuilder fileContents = new StringBuilder();

		if (!result.isEmpty()) {
//			fileContents.append(result.))
//
			List<String> columnKeys = result.get(0).entrySet().stream().map(i -> i.getKey())
					.collect(Collectors.toList());

//			fileContents.append("------------------------------------------------");
//			result.stream().limit(1)
//			.forEach(i -> i.forEach((keyColumn, resultValue) -> fileContents.append(keyColumn)));
//			
//			
			System.out.println(result);
			System.out.println("Keys:" + columnKeys);
			columnKeys.forEach(key -> fileContents.append(StringUtils.rightPad(key, 30)));
			fileContents.append("\n");
			IntStream.range(0, columnKeys.size()).boxed()
					.forEach(i -> fileContents.append("----------------------------- "));
			fileContents.append("\n");
			for (Map<String, Object> eachResults : result) {
//				for (Map.Entry<String, Object> queryResults : eachResults.entrySet()) {
				for (String column : columnKeys) {
					if (eachResults.get(column) == null) {
//						fileContents.append("----------------------------- ");
						fileContents.append(StringUtils.rightPad(StringUtils.EMPTY, 30));
					} else {
//						fileContents.append("---------s-------------------- ");
						fileContents.append(StringUtils.rightPad(eachResults.get(column).toString(), 30));
					}
				}
//				}
				fileContents.append("\n");
			}
			System.out.println(fileContents.toString());
//			writeToFile(fileContents.toString());
		}

		return fileContents.toString();
	}

//	private static void writeToFile(String content) {
//		// Get the file reference
//		String fileLocation = "src/main/resources/op-files/fileToCreate.txt";
//
//		// Use try-with-resource to get auto-closeable writer instance
//		try {
//			Files.write(Paths.get(fileLocation), content.getBytes());
//			//System.out.println("file created");
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}

	public String getCurrentTimeStamp() {
		Date objDate = new Date();
		String strDateFormat = "dd-MMM-yyyy-hh-mm-ss-SSS";
		SimpleDateFormat objSDF = new SimpleDateFormat(strDateFormat);
		return objSDF.format(objDate);

	}
}
