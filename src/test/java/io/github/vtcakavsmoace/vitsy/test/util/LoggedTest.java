package io.github.vtcakavsmoace.vitsy.test.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;

import com.google.gson.Gson;

public abstract class LoggedTest {

	@BeforeClass
	public static void resetLog() {
		log = new TestGroupResults();
	}

	private static TestGroupResults log;

	private static File testLogDir = new File(System.getProperty("maventestdir"));

	private static Gson gson = new Gson();

	@Rule
	public TestWatcher watchman = new TestWatcher() {
		@Override
		protected void failed(Throwable e, Description description) {
			if (log.getTestUnit() == null) {
				log.setTestUnit(getTestUnitName());
			}
			TestResult tr = new TestResult(description, false);
			log.add(tr);
		}

		@Override
		protected void succeeded(Description description) {
			if (log.getTestUnit() == null) {
				log.setTestUnit(getTestUnitName());
			}
			TestResult tr = new TestResult(description, true);
			log.add(tr);
		}
	};
	
	public abstract String getTestUnitName();

	@AfterClass
	public static void writeToLog() throws FileNotFoundException {
		if (!testLogDir.exists()) {
			testLogDir.mkdirs();
		}
		PrintWriter output = new PrintWriter(testLogDir + File.separator + log.getTestUnit() + ".json");
		output.println(gson.toJson(log));
		output.close();
	}

}
