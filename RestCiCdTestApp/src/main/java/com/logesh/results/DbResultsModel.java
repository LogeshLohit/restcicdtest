package com.logesh.results;

public class DbResultsModel {

	private String query;
	private String results;

	public DbResultsModel() {
		// TODO Auto-generated constructor stub
	}

	public DbResultsModel(String query) {
		super();
		this.query = query;
	}

	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}

	public String getResults() {
		return results;
	}

	public void setResults(String results) {
		this.results = results;
	}

}
