package io.github.vtcakavsmoace.vitsy.test.util;

import org.junit.runner.Description;

public class TestResult {

	public String method;
	public boolean success;
	
	public TestResult(Description description, boolean success) {
		this.method = description.getMethodName();
		this.success = success;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}
	
}
