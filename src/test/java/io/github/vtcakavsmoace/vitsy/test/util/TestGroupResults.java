package io.github.vtcakavsmoace.vitsy.test.util;

import java.util.ArrayList;
import java.util.List;

public class TestGroupResults {

	public String testUnit;
	public int successCount;
	public int tests;
	public List<TestResult> results;
	
	public TestGroupResults() {
		this.results = new ArrayList<TestResult>();
	}

	public List<TestResult> getResults() {
		return results;
	}

	public void setResults(List<TestResult> results) {
		this.results = new ArrayList<TestResult>(results.size());
		for (TestResult result : results) {
			this.add(result);
		}
	}

	public String getTestUnit() {
		return testUnit;
	}

	public void setTestUnit(String testUnit) {
		this.testUnit = testUnit;
	}
	
	public void add(TestResult result) {
		results.add(result);
		tests++;
		if (result.success)
			successCount++;
	}

	public int getSuccessCount() {
		return successCount;
	}

	public void setSuccessCount(int successCount) {
		this.successCount = successCount;
	}
		
}
