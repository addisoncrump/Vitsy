package com.VTC.vitsy;

import java.io.*;

public class FullConversion {
	public static void fullConvertVerbose(String file) throws IOException, UnrecognizedInstructionException {
		BufferedReader br = new BufferedReader(new FileReader(file));
		StringBuilder sb = new StringBuilder();
		try {
			String line = br.readLine();
			for (int k = 0; line != null; k++) {
				char[] portions = line.toCharArray();
				for (int i = 0; i < portions.length; i++) {
					sb.append(Converter.toVerbose(k, i, new String(new char[] { portions[i] })) + ";");
					sb.append(System.lineSeparator());
				}
				sb.append(":");
				line = br.readLine();
			}
			System.out.println(sb.toString().substring(0, sb.toString().lastIndexOf(":")));
		} finally {
			br.close();
		}
	}
	
	public static void fullConvertGolfed(String file) throws IOException, UnrecognizedInstructionException {
		BufferedReader br = new BufferedReader(new FileReader(file));
		StringBuilder sb = new StringBuilder();
		FileInputStream fis = null;
		try {
			File fileFile = new File(file);
			fis = new FileInputStream(fileFile);
			byte[] data = new byte[(int) fileFile.length()];
			fis.read(data);
			String[] stuffs = new String(data, "UTF-8").replaceAll("\n", "").replaceAll("\r", "").split(":");
			for (int k = 0; k < stuffs.length; k++) {
				String[] curr = (stuffs[k] = stuffs[k].trim()).substring(0, stuffs[k].lastIndexOf(";")).split(";");
				for (int i = 0; i < curr.length; i++) {
					sb.append(Converter.toGolfed(k, i, curr[i]));
				}
				sb.append("\n");
			}
			System.out.println(sb.toString());
		} finally {
			fis.close();
			br.close();
		}
	}
}
