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
					while ((line = br.readLine()) != null && !line.startsWith(";e ") && !line.startsWith(";u "))
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
		} else if (checkUses[0]) {
			String line = "";
			String totfiles = "";
			try (BufferedReader br = new BufferedReader(new FileReader(filename.get(0)))) {
				while ((line = br.readLine()) != null) {
					if (line.startsWith(";u ") && new File(line.substring(3)).exists())
						totfiles += line.substring(3) + " ";
				}
			} catch (Exception e) {
			}
			file.add(totfiles.split(" "));
		} else {
			String line = "";
			String totfiles = "";
			try (BufferedReader br = new BufferedReader(new FileReader(filename.get(0)))) {
				while ((line = br.readLine()) != null) {
					if (line.startsWith(";e ") && new File(line.substring(3)).exists())
						totfiles = line.substring(3);
				}
			} catch (Exception e) {
				System.out.println(filename.get(0));
				e.printStackTrace();
				System.exit(1);
			}
			file.add(new String[] { totfiles });
		}
		return file;
	}
}