package com.VTC.vitsy;
import java.io.*;

public class FileHandler {
	public static String[] getFileInstruct(String filename) {
		String[] file = null;
		try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
		    file = br.readLine().split("");
		} catch (Exception e) {
			System.err.println("File not found, exiting...");
			System.exit(1);
		}
		return file;
	}
}