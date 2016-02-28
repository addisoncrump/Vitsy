package com.VTC.vitsy;

import java.io.*;
import java.util.*;

public class GolfFileHandler implements FileHandler {
	@SuppressWarnings("all")
	public ArrayList<String[]> getFileInstruct(ArrayList<String> filename, boolean codeOnly, boolean[] checkUses) {
		ArrayList<String[]> file = new ArrayList(0);
		if (!checkUses[0] && !checkUses[1]) {
			if (!codeOnly) {
				String line = "";
				try (BufferedReader br = new BufferedReader(new FileReader(filename.get(0)))) {
					while ((line = br.readLine()) != null)
						file.add(line.split(""));
				} catch (Exception e) {
					System.err.println("File " + filename.get(0) + " not found, exiting...");
					e.printStackTrace();
					System.exit(1);
				}
			} else {
				file.add(filename.get(0).split(""));
			}
			if (file.size() == 0) {
				for (int i = 99; i > 2;) {
					System.out.print(i + " bottles of beer on the wall, " + i
							+ " bottles of beer.\nTake one down and pass it around, ");
					i--;
					System.out.println(i + " bottles of beer on the wall.\n");
				}
				System.out.println(
						"2 bottles of beer on the wall, 2 bottles of beer.\nTake one down and pass it around, 1 bottle of beer on the wall.\n");
				System.out.println(
						"1 bottle of beer on the wall, 1 bottle of beer.\nGo to the store and buy some more, 99 bottles of beer on the wall.");
				System.exit(0);
			}
		}
		return file;
	}
}