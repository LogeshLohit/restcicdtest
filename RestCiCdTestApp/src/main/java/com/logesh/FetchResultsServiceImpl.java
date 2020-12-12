package com.logesh;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.text.TabExpander;

import org.springframework.util.StringUtils;

public class FetchResultsServiceImpl {
	public static void main(String[] args) {
		new FetchResultsServiceImpl().readInputFile(false, true);
	}

	public void readInputFile(boolean isDb2File, boolean isCassFile) {
		String cassSchema = "CASS";
		String db2Schema = "DB2";
		try {
			String inputFileContent = new String(Files
					.readAllBytes(Paths.get("D:\\My works\\Verizon Projects\\FETCH-QUERY-RESULTS\\CASS-query.txt")));
			for (String eachQuery : inputFileContent.trim().split(";")) {
//				System.out.println("Query:" + eachQuery);

				if (StringUtils.startsWithIgnoreCase(eachQuery.trim(), "select")) {
//					System.out.println("Starts with Select:" + eachQuery.trim());

					String[] queryStartsFrom = eachQuery.trim().split("(?i)FROM");

					String tableName = queryStartsFrom[1].split("(?i)WHERE")[0];
					System.out.println("Table Name;" + tableName);

					eachQuery = tableName.contains(".")
							? getQueryByDbSchema(isDb2File, isCassFile, eachQuery, tableName,
									StringUtils.split(tableName, ".")[0].trim(), db2Schema, cassSchema)
							: getQueryByDbSchema(isDb2File, isCassFile, eachQuery, tableName, tableName.trim(),
									db2Schema, cassSchema);
				}
				System.out.println("Final Query:" + eachQuery.trim());

				invokeDB(eachQuery);
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private String getQueryByDbSchema(boolean isDb2File, boolean isCassFile, String eachQuery, String tableName,
			String tableNameWithSchema, String db2Schema, String cassSchema) {
		if (isDb2File) {
			return eachQuery.replace(tableName.trim(), db2Schema + "." + tableNameWithSchema);
		} else if (isCassFile) {
			return eachQuery.replace(tableName.trim(), cassSchema + "." + tableNameWithSchema);
		}
		return null;
	}

	private void invokeDB(String eachQuery) {
//		JdbcTempl
	}
}
