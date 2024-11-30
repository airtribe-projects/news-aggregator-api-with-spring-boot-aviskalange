package org.airtribe.newsApi.util;

import java.util.List;

public class Result {
	private String status;

	private Integer totalResults;

	private List<Article> articles;

	public Result() {
	}

	public Result(String status, Integer totalResults, List<Article> articles) {
		this.status = status;
		this.totalResults = totalResults;
		this.articles = articles;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Integer getTotalResults() {
		return totalResults;
	}

	public void setTotalResults(Integer totalResults) {
		this.totalResults = totalResults;
	}

	public List<Article> getArticles() {
		return articles;
	}

	public void setArticles(List<Article> articles) {
		this.articles = articles;
	}
}
