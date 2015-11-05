package com.VTC.vitsy;
import java.io.*;
import java.util.*;

public class FileHandler {
	@SuppressWarnings("all")
	public static ArrayList<String[]> getFileInstruct(String[] filename) {
		ArrayList<String[]> file = new ArrayList(0);
		if (!filename[0].equals("--code")) {
			String line = "";
			try (BufferedReader br = new BufferedReader(new FileReader(filename[0]))) {
				while ((line=br.readLine())!=null) file.add(line.split(""));
			} catch (Exception e) {
				System.err.println("File not found, exiting...");
				System.exit(1);
			}
		} else if (filename.length > 1) {
			file.add(filename[1].split(""));
		} else {
			file.add(";".split(""));
		}
		return file;
	}
}