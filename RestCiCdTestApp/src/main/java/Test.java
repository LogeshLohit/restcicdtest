import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;

public class Test {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
//		TreeMap<String, Integer> map = new TreeMap<String, Integer>();
//		map.putIfAbsent("logesh", 1);
//		map.putIfAbsent("log", 3);
//		map.putIfAbsent("logesh", 4);
////		map.computeIfAbsent("3", a -> a.get(a) );
////		map.pu
//		System.out.println(map);

		// Input

//		List<String> inputs = Arrays.asList("Verizon", "Nomura", "IBM", "BNP Paribas", "JP Morgan", "Morgan Stanley");
//
//		inputs.parallelStream().map(query -> excuteQueryAndReturnRs(query))
//				.forEach(result -> writeToOutputFile(result));

		writeToOutputFile(new Test().getInput());

	}

	private synchronized static Object writeToOutputFile(List<Map<String, Object>> result) {

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
			IntStream.range(0, columnKeys.size()).boxed().forEach(i -> fileContents.append("----------------------------- "));
			fileContents.append("\n");
			boolean needToPrintKey = true;
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
			writeToFile(fileContents.toString());
		}

		return null;
	}

	private static void writeToFile(String content) {
		// Get the file reference
		String fileLocation = "src/main/resources/op-files/fileToCreate.txt";

		// Use try-with-resource to get auto-closeable writer instance
		try {
			Files.write(Paths.get(fileLocation), content.getBytes());
			System.out.println("file created");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static void printColumnKeyHeader(String column) {

	}

	private static Object getKeys(Map<String, Object> i) {
		// TODO Auto-generated method stub
//		i.forEach(action);
		return null;
	}

	private static List<Map<String, Object>> excuteQueryAndReturnRs(String query) {

		List<Map<String, Object>> resultSet = null;
		// Have query return rs

		Map<String, Object> inputQuery = new HashMap<String, Object>();
		inputQuery.put("INPUT-QUERY", query);
		resultSet.add(inputQuery);
		return resultSet;
	}

	public List<Map<String, Object>> getInput() {
		List<Map<String, Object>> resultSet = new ArrayList<>();

		Map<String, Object> inputQuery = new HashMap<String, Object>();
		inputQuery.put("npanxx", "123456");
		inputQuery.put("tln", "6676");
		inputQuery.put("id", 1);
		inputQuery.put("iccid", null);
		resultSet.add(inputQuery);

		Map<String, Object> inputQuery2 = new HashMap<String, Object>();
		inputQuery2.put("npanxx", "8789321");
		inputQuery2.put("tln", "9999");
		inputQuery2.put("iccid", "9283749837984888888888888888888888888888888");
		inputQuery2.put("id", 1);
		
		resultSet.add(inputQuery2);

		return resultSet;
	}

}
