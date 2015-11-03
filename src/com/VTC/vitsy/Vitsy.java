package com.VTC.vitsy;
import java.util.*;

public class Vitsy {
	private static boolean direction = true;
	private static int position = 0;
	@SuppressWarnings("all")
	private static ArrayList<ArrayList<Double>> stac = new ArrayList(0);
	private static int currstac = 0;
	@SuppressWarnings("all")
	private static ArrayList<String> input = new ArrayList(0);
	private static String[] instruct = null;
	private static Double tempvar = null;
	private static Double globalvar = null;
	private static boolean looping = false;
	@SuppressWarnings("all")
	public static void main(String[] args) throws InterruptedException {
		stac.add(new ArrayList(0));
		if (args.length == 0) {
			System.err.println("Need a file pointer.");
			return;
		}
		if (args.length > 1) {
			boolean value = false;
			int offset = 0;
			if (args[0].equals("--code")) offset += 1;
			if (args.length > 2 && args[2].equals("-v")) {
				offset += 1;
				value = true;
			}
			if (args.length > offset+1) {
				String arrin = args[1+offset];
				for (int i = 2 + offset; i < args.length; i++) {
					arrin += " "+args[i];
				}
				try {
					Integer.parseInt(args[offset]);
				} catch (Exception e) {
					value = true;
				}
				String[] arrinput = (value) ? arrin.split(""): arrin.split(" ");
				for (int i = 0; i < arrinput.length; i++) {
					if (value) input.add(arrinput[i]);
					else if (!arrinput[i].equals("")) stac.get(currstac).add(Double.parseDouble(arrinput[i]));
				}
			}
		}
		instruct = FileHandler.getFileInstruct(args);
		while (OperativeHandler.operating()) {
			if (!looping && position > instruct.length - 1) System.exit(0);
			if (!instruct[position].equals(""))
				opHandle();
			position = (direction) ? (position + 1): (position - 1 >= 0) ? position - 1: instruct.length-1;
		}
	}
	public static void forLoopHandler(int reps) throws InterruptedException {
		looping = true;
		position = (direction) ? (position + 1)%instruct.length: (position - 1 >= 0) ? position - 1: instruct.length-1;
		if (!instruct[position].equals("[")) {
			for (int x = 0; x < reps; x++) {
				opHandle();
			}
		} else {
			int startPos = position;
			for (int x = 0; x < reps; x++) {	
				position = (direction) ? (startPos + 1)%instruct.length: (!(startPos - 1 < 0)) ? startPos - 1: instruct.length-1;
				while(!instruct[position].equals("]")) {
					opHandle();
					position = (direction) ? (position + 1)%instruct.length: (position - 1 >= 0) ? position - 1: instruct.length-1;
				}
			}
		}
		looping = false;
	}
	public static void loopHandler() throws InterruptedException {
		looping = true;
		int startPos = position;
		while (true) {
			position = (direction) ? (position + 1)%instruct.length: (position - 1 >= 0) ? position - 1: instruct.length-1;
			if (instruct[position].equals("]")) {
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
	@SuppressWarnings("all")
	public static void opHandle() throws InterruptedException {
		switch (OperativeHandler.doOperation(instruct[position])) {
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
		case "ifnot":
			if (stac.get(currstac).get(stac.get(currstac).size()-1).intValue() == 0 && instruct[(direction) ? (position + 1)%instruct.length: (position - 1 >= 0) ? position - 1: instruct.length-1].equals("[")) {
				stac.get(currstac).remove(stac.get(currstac).size()-1);
				position = (direction) ? (position + 1)%instruct.length: (position - 1 >= 0) ? position - 1: instruct.length-1;
				position = (direction) ? (position + 1)%instruct.length: (position - 1 >= 0) ? position - 1: instruct.length-1;
				while (!instruct[position].equals("]")){
					opHandle();
					position = (direction) ? (position + 1)%instruct.length: (position - 1 >= 0) ? position - 1: instruct.length-1;
				}
			} else if (stac.get(currstac).get(stac.get(currstac).size()-1).intValue() == 0) {
				stac.get(currstac).remove(stac.get(currstac).size()-1);
				position = (direction) ? (position + 1)%instruct.length: (position - 1 >= 0) ? position - 1: instruct.length-1;
			}
			else if (instruct[(direction) ? (position + 1)%instruct.length: (position - 1 >= 0) ? position - 1: instruct.length-1].equals("[")) {
				stac.get(currstac).remove(stac.get(currstac).size()-1);
				while (!instruct[position].equals("]")) {
					position = (direction) ? (position + 1)%instruct.length: (position - 1 >= 0) ? position - 1: instruct.length-1;
				}
			} else stac.get(currstac).remove(stac.get(currstac).size()-1);
			break;
		case "if":
			if (!(stac.get(currstac).get(stac.get(currstac).size()-1).intValue() == 0) && instruct[(direction) ? (position + 1)%instruct.length: (position - 1 >= 0) ? position - 1: instruct.length-1].equals("[")) {
				stac.get(currstac).remove(stac.get(currstac).size()-1);
				position = (direction) ? (position + 1)%instruct.length: (position - 1 >= 0) ? position - 1: instruct.length-1;
				position = (direction) ? (position + 1)%instruct.length: (position - 1 >= 0) ? position - 1: instruct.length-1;
				while (!instruct[position].equals("]")){
					opHandle();
					position = (direction) ? (position + 1)%instruct.length: (position - 1 >= 0) ? position - 1: instruct.length-1;
				}
			} else if (!(stac.get(currstac).get(stac.get(currstac).size()-1).intValue() == 0)) {
				stac.get(currstac).remove(stac.get(currstac).size()-1);
				position = (direction) ? (position + 1)%instruct.length: (position - 1 >= 0) ? position - 1: instruct.length-1;
			}
			else if (instruct[(direction) ? (position + 1)%instruct.length: (position - 1 >= 0) ? position - 1: instruct.length-1].equals("[")) {
				stac.get(currstac).remove(stac.get(currstac).size()-1);
				while (!instruct[position].equals("]")) {
					position = (direction) ? (position + 1)%instruct.length: (position - 1 >= 0) ? position - 1: instruct.length-1;
				}
			} else stac.get(currstac).remove(stac.get(currstac).size()-1);
			break;
		case "skip":
			position = (direction) ? (position + 1)%instruct.length: (position - 1 >= 0) ? position - 1: instruct.length-1;
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
			@SuppressWarnings("all")
			ArrayList<Double> temp1 = new ArrayList(0);
			for (int k = 0; k < stac.get(currstac).size(); k++) {
				temp1.add(stac.get(currstac).get((k+1)%stac.get(currstac).size()));
			}
			stac.remove(currstac);
			stac.add(temp1);
			break;
		case "rotateleft":
			@SuppressWarnings("all")
			ArrayList<Double> temp2 = new ArrayList(0);
			for (int k = 0; k < stac.get(currstac).size(); k++) {
				temp2.add(stac.get(currstac).get((k-1 < 0) ? stac.get(currstac).size()-1: k-1));
			}
			stac.remove(currstac);
			stac.add(temp2);
			break;
		case "duplicate":
			stac.get(currstac).add(stac.get(currstac).get(stac.get(currstac).size()-1));
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
		case "changestack":
			currstac = (currstac + 1) % stac.size();
			break;
		case "clnstack": 
			stac.add(stac.get(currstac));
			currstac = (currstac + 1) % stac.size();
			break;
		case "newstack": 
			stac.add(new ArrayList(0));
			currstac = (currstac + 1) % stac.size();
			break;
		case "rmstack":
			stac.remove(currstac);
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
				position = (direction) ? (position + 1)%instruct.length: (position - 1 >= 0) ? position - 1: instruct.length-1;
				if (instruct[position].equals("\"") || instruct[position].equals("\'")) break;
				stac.get(currstac).add(((double)instruct[position].toCharArray()[0]));
			}
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
			break;
		}
	}
}