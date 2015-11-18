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
		if (file.size() == 0) {
			for (int i = 99; i > 2; i--)
				System.out.println(i+" bottles of beer on the wall, "+i+" bottles of beer.\nTake one down and pass it around, "+i+" bottles of beer.");
			System.out.println("2 bottles of beer on the wall, 2 bottles of beer.\nTake one down and pass it around, 1 bottle of beer.");
			System.out.println("1 bottle of beer on the wall, 1 bottle of beer.\nGo to the store and buy some more, 99 bottles of beer.");
			System.exit(0);
		}
		return file;
	}
}