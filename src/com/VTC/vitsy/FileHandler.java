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
				while ((line=br.readLine())!=null && !line.startsWith(";ext")) file.add(line.split(""));
			} catch (Exception e) {
				System.err.println("File "+filename[0]+" not found, exiting...");
				System.exit(1);
			}
		} else if (filename.length > 1) {
			file.add(filename[1].split(""));
		}
		if (file.size() == 0) {
			for (int i = 99; i > 2;) {
				System.out.print(i+" bottles of beer on the wall, "+i+" bottles of beer.\nTake one down and pass it around, ");
				i--;
				System.out.println(i+" bottles of beer on the wall.\n");
			}
			System.out.println("2 bottles of beer on the wall, 2 bottles of beer.\nTake one down and pass it around, 1 bottle of beer on the wall.\n");
			System.out.println("1 bottle of beer on the wall, 1 bottle of beer.\nGo to the store and buy some more, 99 bottles of beer on the wall.");
			System.exit(0);
		}
		return file;
	}
	/**
	 * Soon to be implemented. c:
	@SuppressWarnings("all")
	public static String getExtends(String[] filename) {
		String extender = "";
		if (!filename[0].equals("--code")) {
			String line = "";
			try (BufferedReader br = new BufferedReader(new FileReader(filename[0]))) {
				while ((line=br.readLine())!=null && !line.startsWith(";ext"));
				extender = line.substring(5);
			} catch (IOException e) {
				System.err.println("File "+filename[0]+" not found, exiting...");
				System.exit(1);
			} catch (Exception e) {
				System.err.println("Extend declaration not found, exiting...");
				System.exit(1);
			}
		}
		return extender;
	}
	@SuppressWarnings("all")
	public static ArrayList<String[]> getUses(ArrayList<Double> filename) {
		ArrayList<String[]> file = new ArrayList(0);
		String filenamereal = "";
		for (int i = 0; i < filename.size(); i++) {
			filenamereal += (char) (filename.get(i).intValue());
		}
		String line = "";
		try (BufferedReader br = new BufferedReader(new FileReader(filenamereal))) {
			while ((line=br.readLine())!=null && !line.contains("extends") && !line.contains("uses")) file.add(line.split(""));
		} catch (Exception e) {
			System.err.println("File "+filenamereal+" not found, exiting...");
			System.exit(1);
		}
		return file;
	}
	*/
}