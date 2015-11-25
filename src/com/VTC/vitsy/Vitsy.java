package com.VTC.vitsy;
import java.io.*;
import java.util.*;

@SuppressWarnings("all")
public class Vitsy {
	private static boolean direction = true;

	private static int position = 0;

	private static ArrayList<ArrayList<Double>> stac = new ArrayList(0);

	private static ArrayList<Integer[]> oldpos = new ArrayList(0);

	private static int currstac = 0;

	private static ArrayList<String> input = new ArrayList(0);

	private static ArrayList<ArrayList<String[]>> instruct = new ArrayList(0);

	private static ArrayList<ArrayList<Double>> objects = new ArrayList(0);

	private static ArrayList<String> objectrefs = new ArrayList(0);

	private static ArrayList<String> objectrefsold = new ArrayList(0);

	private static ArrayList<Integer[]> oldposext = new ArrayList(0);

	private static ArrayList<String> extender = new ArrayList(0);

	private static ArrayList<String[]> users = new ArrayList(0);
	
	private static ArrayList<String> currclassname = new ArrayList(0);
	
	private static ArrayList<Boolean> olddir = new ArrayList(0);

	private static int currinstruct = 0;

	private static Double tempvar = null;

	private static Double globalvar = null;

	private static boolean looping = false;


	public static void main(String[] args) throws InterruptedException, IOException {
		stac.add(new ArrayList(0));
		if (args.length == 0) {
			System.err.println("Need a file pointer.");
			return;
		}
		if (args.length > 1) {
			boolean cansplit = true;
			int offset = 0;
			if (args[0].equals("--code")) offset += 1;
			if (args.length > offset+1) {
				String arrin = args[1+offset];
				for (int i = 2 + offset; i < args.length; i++) {
					arrin += " "+args[i];
				}
				try {
					for (int i = 1+offset; i < args.length; i++)
						Double.parseDouble(args[i]);
				} catch (Exception e) {
					cansplit = false;
				}
				String[] arrinput = (cansplit) ? arrin.split(" "): arrin.split("");
				for (int i = 0; i < arrinput.length; i++) {
					if (cansplit) stac.get(currstac).add(Double.parseDouble(arrinput[i]));
					else input.add(arrinput[i]);
				}
			}
		}
		if (!args[0].equals("--code")) {
			try {
				extender.add(FileHandler.getFileInstruct(args, new boolean[]{false, true}).get(0)[0]);
				users.add(FileHandler.getFileInstruct(args, new boolean[]{true, false}).get(0));
			} catch (Exception e) {}
			currclassname.add(args[0]);
		}
		instruct.add(FileHandler.getFileInstruct(args, new boolean[]{false, false}));
		while (OperativeHandler.operating()) {
			if (!looping && position > instruct.get(instruct.size()-1).get(currinstruct).length - 1 && oldpos.size()==0) System.exit(0);
			else if (position < instruct.get(instruct.size()-1).get(currinstruct).length && !instruct.get(instruct.size()-1).get(currinstruct)[position].equals(""))
				opHandle();
			position = (direction) ? (position + 1): (position - 1 >= 0) ? position - 1: instruct.get(instruct.size()-1).get(currinstruct).length-1;
		}
	}

	public static void methodHandler(int source) throws InterruptedException, IOException {
		olddir.add(direction);
		direction = true;
		ArrayList<String[]> methodFromOtherSource = new ArrayList(0);
		if (source == -1) {
			oldpos.add(new Integer[]{currinstruct,position});
			currinstruct = stac.get(currstac).get(stac.get(currstac).size()-1).intValue();
			position = 0;
			stac.get(currstac).remove(stac.get(currstac).size()-1);
			while (position < instruct.get(instruct.size()-1).get(currinstruct).length) {
				if (!looping && position > instruct.get(instruct.size()-1).get(currinstruct).length - 1) {
					break;
				}
				else if (!instruct.get(instruct.size()-1).get(currinstruct)[position].equals(""))
					opHandle();
				position = (direction) ? (position + 1): (position - 1 >= 0) ? position - 1: instruct.get(instruct.size()-1).get(currinstruct).length-1;
			}
			currinstruct = oldpos.get(oldpos.size()-1)[0].intValue();
			position = oldpos.get(oldpos.size()-1)[1].intValue();
			oldpos.remove(oldpos.size()-1);
			direction = olddir.get(olddir.size()-1);
			olddir.remove(olddir.size()-1);
			return;
		}
		instruct.add(FileHandler.getFileInstruct(new String[]{(source == -2)?extender.get(extender.size()-1):users.get(users.size()-1)[source]}, new boolean[]{false, false}));
		users.add(FileHandler.getFileInstruct(new String[]{(source == -2)?extender.get(extender.size()-1):users.get(users.size()-1)[source]}, new boolean[]{true, false}).get(0));
		extender.add(FileHandler.getFileInstruct(new String[]{(source == -2)?extender.get(extender.size()-1):users.get(users.size()-2)[source]}, new boolean[]{false, true}).get(0)[0]);
		oldposext.add(new Integer[]{currinstruct, position});
		currinstruct = (source == -2)?currinstruct:stac.get(currstac).get(stac.get(currstac).size()-1).intValue();
		currclassname.add((source == -2)?extender.get(extender.size()-2):users.get(users.size()-2)[source]);
		position = 0;
		if (source != -2) stac.get(currstac).remove(stac.get(currstac).size()-1);
		while (position < instruct.get(instruct.size()-1).get(currinstruct).length) {
			if (!looping && position > instruct.get(instruct.size()-1).get(currinstruct).length - 1) {
				break;
			}
			else if (!instruct.get(instruct.size()-1).get(currinstruct)[position].equals(""))
				opHandle();
			position = (direction) ? (position + 1): (position - 1 >= 0) ? position - 1: instruct.get(instruct.size()-1).get(currinstruct).length-1;
		}
		instruct.remove(instruct.size()-1);
		currinstruct = oldposext.get(oldposext.size()-1)[0].intValue();
		position = oldposext.get(oldposext.size()-1)[1].intValue();
		oldposext.remove(oldposext.size()-1);
		currclassname.remove(currclassname.size()-1);
		direction = olddir.get(olddir.size()-1);
		olddir.remove(olddir.size()-1);
	}

	public static void forLoopHandler(int reps) throws InterruptedException, IOException {
		looping = true;
		position = (direction) ? (position + 1)%instruct.get(instruct.size()-1).get(currinstruct).length: (position - 1 >= 0) ? position - 1: instruct.get(instruct.size()-1).get(currinstruct).length-1;

		if (!instruct.get(instruct.size()-1).get(currinstruct)[position].equals("[")) {
			for (int x = 0; x < reps; x++) {
				opHandle();
			}
		} else {
			int startPos = position;
			for (int x = 0; x < reps; x++) {	
				position = (direction) ? (startPos + 1)%instruct.get(instruct.size()-1).get(currinstruct).length: (!(startPos - 1 < 0)) ? startPos - 1: instruct.get(instruct.size()-1).get(currinstruct).length-1;
				while(!instruct.get(instruct.size()-1).get(currinstruct)[position].equals("]")) {
					opHandle();
					position = (direction) ? (position + 1)%instruct.get(instruct.size()-1).get(currinstruct).length: (position - 1 >= 0) ? position - 1: instruct.get(instruct.size()-1).get(currinstruct).length-1;
				}
			}
		}
		looping = false;
	}

	public static void loopHandler() throws InterruptedException, IOException {
		looping = true;
		int startPos = position;

		while (true) {
			position = (direction) ? (position + 1)%instruct.get(instruct.size()-1).get(currinstruct).length: (position - 1 >= 0) ? position - 1: instruct.get(instruct.size()-1).get(currinstruct).length-1;
			if (instruct.get(instruct.size()-1).get(currinstruct)[position].equals("]")) {
				if (stac.get(currstac).get(stac.get(currstac).size()-1).intValue() == 0) { 
					stac.get(currstac).remove(stac.get(currstac).size()-1);
					break;

				}
				position = startPos+1;
			}
			opHandle();
		}
		looping = false;
	}

	public static void opHandle() throws InterruptedException, IOException {
		switch (OperativeHandler.doOperation((String) instruct.get(instruct.size()-1).get(currinstruct)[position])) {
		case "1":
			stac.get(currstac).add(new Double(1));
			break;

		case "2":
			stac.get(currstac).add(new Double(2));
			break;

		case "3":
			stac.get(currstac).add(new Double(3));
			break;

		case "4":
			stac.get(currstac).add(new Double(4));
			break;

		case "5":
			stac.get(currstac).add(new Double(5));
			break;

		case "6":
			stac.get(currstac).add(new Double(6));
			break;

		case "7":
			stac.get(currstac).add(new Double(7));
			break;

		case "8":
			stac.get(currstac).add(new Double(8));
			break;

		case "9":
			stac.get(currstac).add(new Double(9));
			break;

		case "a":
			stac.get(currstac).add(new Double(10));
			break;

		case "b":
			stac.get(currstac).add(new Double(11));
			break;

		case "c":
			stac.get(currstac).add(new Double(12));
			break;

		case "d":
			stac.get(currstac).add(new Double(13));
			break;

		case "e":
			stac.get(currstac).add(new Double(14));
			break;

		case "f":
			stac.get(currstac).add(new Double(15));
			break;

		case "0":
			stac.get(currstac).add(new Double(0));
			break;

		case "input":
			if (input.size() == 0) stac.get(currstac).add((double) -1);
			else {
				stac.get(currstac).add((double) ((int) input.get(input.size()-1).toCharArray()[0]));
				input.remove(input.size()-1);
			}
			break;

		case "inlength":
			stac.get(currstac).add((double)input.size());
			break;

		case "wait":
			Thread.sleep((long) (stac.get(currstac).get(stac.get(currstac).size()-1)*1000));
			stac.get(currstac).remove(stac.get(currstac).size()-1);
			break;

		case "method":
			methodHandler(-1);
			break;

		case "file":
			try {
				String filename = "";
				for (int i = stac.get(currstac).size()-1; i >= 0; i--) {
					filename+=new String(new char[]{(char)stac.get(currstac).get(i).intValue()});
				}
				stac.set(currstac, new ArrayList(0));
				File file = new File(filename);
				FileInputStream fis = new FileInputStream(file);
				byte[] data = new byte[(int) file.length()];
				fis.read(data);
				fis.close();
				char[] stuffs = new String(data, "UTF-8").toCharArray();
				for (int i = stuffs.length-1; i >= 0; i--) {
					stac.get(currstac).add((double) stuffs[i]);
				}
			} catch (FileNotFoundException e) {}
			break;

		case "ifnot":
			if (stac.get(currstac).get(stac.get(currstac).size()-1).intValue() == 0 && instruct.get(instruct.size()-1).get(currinstruct)[(direction) ? (position + 1)%instruct.get(instruct.size()-1).get(currinstruct).length: (position - 1 >= 0) ? position - 1: instruct.get(instruct.size()-1).get(currinstruct).length-1].equals("[")) {
				stac.get(currstac).remove(stac.get(currstac).size()-1);
				position = (direction) ? (position + 1)%instruct.get(instruct.size()-1).get(currinstruct).length: (position - 1 >= 0) ? position - 1: instruct.get(instruct.size()-1).get(currinstruct).length-1;
				position = (direction) ? (position + 1)%instruct.get(instruct.size()-1).get(currinstruct).length: (position - 1 >= 0) ? position - 1: instruct.get(instruct.size()-1).get(currinstruct).length-1;
				while (!instruct.get(instruct.size()-1).get(currinstruct)[position].equals("]")){
					opHandle();
					position = (direction) ? (position + 1)%instruct.get(instruct.size()-1).get(currinstruct).length: (position - 1 >= 0) ? position - 1: instruct.get(instruct.size()-1).get(currinstruct).length-1;
				}
			} else if (stac.get(currstac).get(stac.get(currstac).size()-1).intValue() == 0) {
				stac.get(currstac).remove(stac.get(currstac).size()-1);
				position = (direction) ? (position + 1)%instruct.get(instruct.size()-1).get(currinstruct).length: (position - 1 >= 0) ? position - 1: instruct.get(instruct.size()-1).get(currinstruct).length-1;
			}
			else if (instruct.get(instruct.size()-1).get(currinstruct)[(direction) ? (position + 1)%instruct.get(instruct.size()-1).get(currinstruct).length: (position - 1 >= 0) ? position - 1: instruct.get(instruct.size()-1).get(currinstruct).length-1].equals("[")) {
				stac.get(currstac).remove(stac.get(currstac).size()-1);
				while (!instruct.get(instruct.size()-1).get(currinstruct)[position].equals("]")) {
					position = (direction) ? (position + 1)%instruct.get(instruct.size()-1).get(currinstruct).length: (position - 1 >= 0) ? position - 1: instruct.get(instruct.size()-1).get(currinstruct).length-1;
				}
			} else stac.get(currstac).remove(stac.get(currstac).size()-1);
			break;

		case "if":
			if (!(stac.get(currstac).get(stac.get(currstac).size()-1).intValue() == 0) && instruct.get(instruct.size()-1).get(currinstruct)[(direction) ? (position + 1)%instruct.get(instruct.size()-1).get(currinstruct).length: (position - 1 >= 0) ? position - 1: instruct.get(instruct.size()-1).get(currinstruct).length-1].equals("[")) {
				stac.get(currstac).remove(stac.get(currstac).size()-1);
				position = (direction) ? (position + 1)%instruct.get(instruct.size()-1).get(currinstruct).length: (position - 1 >= 0) ? position - 1: instruct.get(instruct.size()-1).get(currinstruct).length-1;
				position = (direction) ? (position + 1)%instruct.get(instruct.size()-1).get(currinstruct).length: (position - 1 >= 0) ? position - 1: instruct.get(instruct.size()-1).get(currinstruct).length-1;
				while (!instruct.get(instruct.size()-1).get(currinstruct)[position].equals("]")){
					opHandle();
					position = (direction) ? (position + 1)%instruct.get(instruct.size()-1).get(currinstruct).length: (position - 1 >= 0) ? position - 1: instruct.get(instruct.size()-1).get(currinstruct).length-1;
				}
			} else if (!(stac.get(currstac).get(stac.get(currstac).size()-1).intValue() == 0)) {
				stac.get(currstac).remove(stac.get(currstac).size()-1);
				position = (direction) ? (position + 1)%instruct.get(instruct.size()-1).get(currinstruct).length: (position - 1 >= 0) ? position - 1: instruct.get(instruct.size()-1).get(currinstruct).length-1;
			}
			else if (instruct.get(instruct.size()-1).get(currinstruct)[(direction) ? (position + 1)%instruct.get(instruct.size()-1).get(currinstruct).length: (position - 1 >= 0) ? position - 1: instruct.get(instruct.size()-1).get(currinstruct).length-1].equals("[")) {
				stac.get(currstac).remove(stac.get(currstac).size()-1);
				while (!instruct.get(instruct.size()-1).get(currinstruct)[position].equals("]")) {
					position = (direction) ? (position + 1)%instruct.get(instruct.size()-1).get(currinstruct).length: (position - 1 >= 0) ? position - 1: instruct.get(instruct.size()-1).get(currinstruct).length-1;
				}
			} else stac.get(currstac).remove(stac.get(currstac).size()-1);
			break;

		case "skip":
			position = (direction) ? (position + 1)%instruct.get(instruct.size()-1).get(currinstruct).length: (position - 1 >= 0) ? position - 1: instruct.get(instruct.size()-1).get(currinstruct).length-1;
			break;

		case "loop":
			loopHandler();
			break;

		case "sine":
			stac.get(currstac).set(stac.get(currstac).size()-1,(Math.sin(stac.get(currstac).get(stac.get(currstac).size()-1))));
			break;

		case "asine":
			stac.get(currstac).set(stac.get(currstac).size()-1,(Math.asin(stac.get(currstac).get(stac.get(currstac).size()-1))));
			break;

		case "cosine":
			stac.get(currstac).set(stac.get(currstac).size()-1,(Math.cos(stac.get(currstac).get(stac.get(currstac).size()-1))));
			break;

		case "acosine":
			stac.get(currstac).set(stac.get(currstac).size()-1,(Math.acos(stac.get(currstac).get(stac.get(currstac).size()-1))));
			break;

		case "tangent":
			stac.get(currstac).set(stac.get(currstac).size()-1,(Math.tan(stac.get(currstac).get(stac.get(currstac).size()-1))));
			break;

		case "atangent":
			stac.get(currstac).set(stac.get(currstac).size()-1,(Math.atan(stac.get(currstac).get(stac.get(currstac).size()-1))));
			break;

		case "repnextchar":
			int repeats = stac.get(currstac).get(stac.get(currstac).size()-1).intValue();
			stac.get(currstac).remove(stac.get(currstac).size()-1);
			forLoopHandler(repeats);
			break;

		case "length":
			stac.get(currstac).add((double)stac.get(currstac).size());
			break;

		case "pi":
			stac.get(currstac).add((Math.PI));
			break;

		case "E":
			stac.get(currstac).add((Math.E));
			break;

		case "log":
			stac.get(currstac).set(stac.get(currstac).size()-2, (Math.log(stac.get(currstac).get(stac.get(currstac).size()-2))/Math.log(stac.get(currstac).get(stac.get(currstac).size()-1))));
			stac.get(currstac).remove(stac.get(currstac).size()-1);
			break;

		case "outnum":
			Double x = stac.get(currstac).get(stac.get(currstac).size()-1);
			if (x > 10000 && x.intValue() == x)
				System.out.print(x.intValue());
			else if (x > 10000) 
				System.out.printf("%f", x);
			else if (x.intValue() == x) 
				System.out.print(x.intValue());
			else
				System.out.print(x);
			stac.get(currstac).remove(stac.get(currstac).size()-1);
			break;

		case "rand":
			stac.get(currstac).set(stac.get(currstac).size()-1, (Math.random()*stac.get(currstac).get(stac.get(currstac).size()-1)));
			break;

		case "outchar":
			System.out.print((char) stac.get(currstac).get(stac.get(currstac).size()-1).intValue());
			stac.get(currstac).remove(stac.get(currstac).size()-1);
			break;

		case "end":
			System.exit(0);
		case "printall":
			int reps = stac.get(currstac).size();
			for(int i = reps - 1; i >= 0; i--) {
				System.out.print((char) stac.get(currstac).get(stac.get(currstac).size()-1).intValue());
				stac.get(currstac).remove(stac.get(currstac).size()-1);
			}
			break;

		case "getall":
			int reps2 = input.size();
			for(int i = reps2 - 1; i >= 0; i--) {
				stac.get(currstac).add((double) ((int) input.get(input.size()-1).toCharArray()[0]));
				input.remove(input.size()-1);
			}
			break;

		case "teleport":
			position = stac.get(currstac).get(stac.get(currstac).size()-1).intValue()-2;
			stac.get(currstac).remove(stac.get(currstac).size()-1);
			break;

		case "reverse":
			Collections.reverse(stac.get(currstac));
			break;

		case "rotateright":

			ArrayList<Double> temp1 = new ArrayList(0);
			for (int k = 0; k < stac.get(currstac).size(); k++) {
				temp1.add(stac.get(currstac).get((k+1)%stac.get(currstac).size()));
			}
			stac.remove(currstac);
			stac.add(temp1);
			break;

		case "rotateleft":

			ArrayList<Double> temp2 = new ArrayList(0);
			for (int k = 0; k < stac.get(currstac).size(); k++) {
				temp2.add(stac.get(currstac).get((k-1 < 0) ? stac.get(currstac).size()-1: k-1));
			}
			stac.remove(currstac);
			stac.add(temp2);
			break;

		case "duplicate":
			stac.get(currstac).add(stac.get(currstac).get(stac.get(currstac).size()-1).doubleValue());
			break;

		case "tempvar":
			if (tempvar == null) {
				tempvar = stac.get(currstac).get(stac.get(currstac).size()-1);
				stac.get(currstac).remove(stac.get(currstac).size()-1);
			}
			else {
				stac.get(currstac).add(tempvar);
				tempvar = null;
			}
			break;

		case "globalvar":
			if (globalvar == null) {
				globalvar = stac.get(currstac).get(stac.get(currstac).size()-1);
				stac.get(currstac).remove(stac.get(currstac).size()-1);
			}
			else stac.get(currstac).add(globalvar);
			break;

		case "remove":
			stac.get(currstac).remove(stac.get(currstac).size()-1);
			break;

		case "part":
			stac.get(currstac).set(stac.get(currstac).size()-1, stac.get(currstac).get(stac.get(currstac).get(stac.get(currstac).size()-1).intValue()));
			break;
			
		case "classmethod":
			int methodToUse = stac.get(currstac).get(stac.get(currstac).size()-2).intValue();
			stac.get(currstac).remove(stac.get(currstac).size()-2);
			methodHandler(methodToUse);
			break;

		case "super":
			methodHandler(-2);
			break;
			
		case "usecount":
			stac.get(currstac).add(new Double(users.get(users.size()-1).length));
			break;
			
		case "classname":
			int usereference = stac.get(currstac).get(stac.get(currstac).size()-1).intValue();
			stac.get(currstac).remove(stac.get(currstac).size()-1);
			char[] classname = ((usereference >= 0)?users.get(users.size()-1)[usereference]:(usereference == -1)?currclassname.get(currclassname.size()-1):extender.get(extender.size()-1)).toCharArray();
			for (int i = classname.length-1; i != -1; i--) {
				stac.get(currstac).add(new Double((int) classname[i]));
			}
			break;
			
		case "objects":
			objects.add(stac.get(currstac));
			position = (direction) ? (position + 1): (position - 1 >= 0) ? position - 1: instruct.get(instruct.size()-1).get(currinstruct).length-1;
			int k = objectrefsold.indexOf(instruct.get(instruct.size()-1).get(currinstruct)[position]);
			if (k != -1) {
				String placehold = objectrefs.get(k);
				objectrefs.remove(k);
			}
			objectrefs.add(instruct.get(instruct.size()-1).get(currinstruct)[position]);
			
		case "rmstack":
			stac.remove(currstac);
			if (stac.size() != 0) {
				try {
					stac.get(currstac);
				} catch (Exception e) {
					currstac = (currstac + 1) % stac.size();
				}
			}
			break;

		case "clnstack": 
			stac.add(new ArrayList(stac.get(currstac)));
		case "changestack":
			currstac = (currstac + 1) % stac.size();
			break;

		case "newstack": 
			stac.add(new ArrayList(0));
			currstac = (currstac + 1) % stac.size();
			break;

		case "numstack":
			stac.get(currstac).add((double) stac.size());
			break;

		case "add":
			stac.get(currstac).set(stac.get(currstac).size()-2,stac.get(currstac).get(stac.get(currstac).size()-1)+(stac.get(currstac).get(stac.get(currstac).size()-2)));
			stac.get(currstac).remove(stac.get(currstac).size()-1);
			break;

		case "subtract":
			stac.get(currstac).set(stac.get(currstac).size()-2,stac.get(currstac).get(stac.get(currstac).size()-2)-(stac.get(currstac).get(stac.get(currstac).size()-1)));
			stac.get(currstac).remove(stac.get(currstac).size()-1);
			break;

		case "multiply":
			stac.get(currstac).set(stac.get(currstac).size()-2,stac.get(currstac).get(stac.get(currstac).size()-1)*(stac.get(currstac).get(stac.get(currstac).size()-2)));
			stac.get(currstac).remove(stac.get(currstac).size()-1);
			break;

		case "divide":
			stac.get(currstac).set(stac.get(currstac).size()-2,stac.get(currstac).get(stac.get(currstac).size()-2)/(stac.get(currstac).get(stac.get(currstac).size()-1)));
			stac.get(currstac).remove(stac.get(currstac).size()-1);
			break;

		case "equal":
			stac.get(currstac).set(stac.get(currstac).size()-2, (double) ((stac.get(currstac).get(stac.get(currstac).size()-2).equals(stac.get(currstac).get(stac.get(currstac).size()-1)))? 0: 1));
			stac.get(currstac).remove(stac.get(currstac).size()-1);
			break;

		case "modulo":
			stac.get(currstac).set(stac.get(currstac).size()-2,stac.get(currstac).get(stac.get(currstac).size()-2)%stac.get(currstac).get(stac.get(currstac).size()-1));
			stac.get(currstac).remove(stac.get(currstac).size()-1);
			break;

		case "power":
			stac.get(currstac).set(stac.get(currstac).size()-2,Math.pow(stac.get(currstac).get(stac.get(currstac).size()-2),stac.get(currstac).get(stac.get(currstac).size()-1)));
			stac.get(currstac).remove(stac.get(currstac).size()-1);
			break;

		case "factorial":
			double output = 1;
			Double y = stac.get(currstac).get(stac.get(currstac).size()-1);
			for (int i=1; i<=y; i++) {
				output*=i;
			}
			stac.get(currstac).set(stac.get(currstac).size()-1, output);
			break;

		case "quote":
			while (true) {
				position = (direction) ? (position + 1)%instruct.get(instruct.size()-1).get(currinstruct).length: (position - 1 >= 0) ? position - 1: instruct.get(instruct.size()-1).get(currinstruct).length-1;
				if (instruct.get(instruct.size()-1).get(currinstruct)[position].equals("\"") || instruct.get(instruct.size()-1).get(currinstruct)[position].equals("\'")) break;

				stac.get(currstac).add(((double)((String)instruct.get(instruct.size()-1).get(currinstruct)[position]).toCharArray()[0]));
			}
			break;

		case "prime":
			Double n = stac.get(currstac).get(stac.get(currstac).size()-1);
			stac.get(currstac).remove(stac.get(currstac).size()-1);
			if(n < 2) {
				stac.get(currstac).add(new Double(0));
				break;
			}
			else if(n == 2 || n == 3) {
				stac.get(currstac).add(new Double(1));
				break;
			}
			else if(n%2 == 0 || n%3 == 0) {
				stac.get(currstac).add(new Double(0));
				break;
			}
			long sqrtN = (long)Math.sqrt(n)+1;
			for(long i = 6L; i <= sqrtN; i += 6) {
				if(n%(i-1) == 0 || n%(i+1) == 0) {
					stac.get(currstac).add(new Double(1));
					break;
				}
			}
			stac.get(currstac).add(new Double(1));
			break;

		case "changedir":
			direction = !direction;
			break;

		case "goleft":
			direction = false;
			break;

		case "goright":
			direction = true;
			break;

		case "randdir":
			direction = (Math.random()<.5);
			break;

		case "nothing":
			int i = objectrefs.indexOf(instruct.get(instruct.size()-1).get(currinstruct)[position]);
			if (i != -1) {
				stac.add(objects.get(i));
				currstac = (currstac + 1) % stac.size();
				objects.remove(i);
				objectrefsold.add(objectrefs.get(i));
				objectrefs.remove(i);
			} else if ((i = objectrefsold.indexOf(instruct.get(instruct.size()-1).get(currinstruct)[position])) != -1) {
				objects.add(stac.get(currstac));
				objectrefs.add(objectrefsold.get(i));
				objectrefsold.remove(i);
				stac.remove(currstac);
				try {
					stac.get(currstac);
				} catch (Exception e) {
					currstac = (currstac + 1) % stac.size();
				}
			}
		}
	}
}