package com.VTC.vitsy;
import java.io.*;

public class FileHandler {
	public static String[] getFileInstruct(String[] filename) {
		String[] file = null;
		if (!filename[0].equals("--code")) {
			try (BufferedReader br = new BufferedReader(new FileReader(filename[0]))) {
				file = br.readLine().split("");
			} catch (Exception e) {
				System.err.println("File not found, exiting...");
				System.exit(1);
			}
		} else if (filename.length > 1) {
			file = filename[1].split("");
		} else {
			file = ";".split("");
		}
		return file;
	}
}