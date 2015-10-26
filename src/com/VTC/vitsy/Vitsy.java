package com.VTC.vitsy;
import java.util.*;

public class Vitsy {
	private static boolean direction = true;
	private static int position = 0;
	@SuppressWarnings("all")
	private static ArrayList<Double> stack = new ArrayList(0);
	@SuppressWarnings("all")
	private static ArrayList<String> input = new ArrayList(0);
	private static String[] instruct = null;
	private static Double tempvar = null;
	private static Double globalvar = null;
	public static void main(String[] args) throws InterruptedException {
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
				String[] arrinput = (!value) ? arrin.split(""): arrin.split(" ");
				for (int i = 0; i < arrinput.length; i++) {
					if (!arrinput[i].equals("") && !value) input.add(arrinput[i]);
					else if (!arrinput[i].equals("")) stack.add(Double.parseDouble(arrinput[i]));
				}
			}
		}
		instruct = FileHandler.getFileInstruct(args);
		while (OperativeHandler.operating()) {
			position %= instruct.length;
			if (!instruct[position].equals(""))
				opHandle();
			position = (direction) ? (position + 1)%instruct.length: (position - 1 >= 0) ? position - 1: instruct.length-1;
		}
	}
	public static void forLoopHandler(int reps) throws InterruptedException {
		position = (direction) ? (position + 1)%instruct.length: (position - 1 >= 0) ? position - 1: instruct.length-1;
		if (!instruct[position].equals("[")) {
			for (int x = 0; x < reps; x++) {
				opHandle();
			}
		} else {
			int startPos = position;
			for (int x = 0; x < reps; x++) {	
				position = (direction) ? (startPos + 1)%instruct.length: startPos - 1;
				while(!instruct[position].equals("]")) {
					opHandle();
					position = (direction) ? (position + 1)%instruct.length: (position - 1 >= 0) ? position - 1: instruct.length-1;
				}
			}
		}
	}
	public static void loopHandler() throws InterruptedException {
		int startPos = position;
		while (true) {
			position = (direction) ? (position + 1)%instruct.length: (position - 1 >= 0) ? position - 1: instruct.length-1;
			if (instruct[position].equals("]")) {
				if (stack.get(stack.size()-1).intValue() == 0) { 
					stack.remove(stack.size()-1);
					break;
				}
				position = startPos+1;
			}
			opHandle();
		}
	}
	public static void opHandle() throws InterruptedException {
		switch (OperativeHandler.doOperation(instruct[position])) {
		case "1":
			stack.add(new Double(1));
			break;
		case "2":
			stack.add(new Double(2));
			break;
		case "3":
			stack.add(new Double(3));
			break;
		case "4":
			stack.add(new Double(4));
			break;
		case "5":
			stack.add(new Double(5));
			break;
		case "6":
			stack.add(new Double(6));
			break;
		case "7":
			stack.add(new Double(7));
			break;
		case "8":
			stack.add(new Double(8));
			break;
		case "9":
			stack.add(new Double(9));
			break;
		case "a":
			stack.add(new Double(10));
			break;
		case "b":
			stack.add(new Double(11));
			break;
		case "c":
			stack.add(new Double(12));
			break;
		case "d":
			stack.add(new Double(13));
			break;
		case "e":
			stack.add(new Double(14));
			break;
		case "f":
			stack.add(new Double(15));
			break;
		case "0":
			stack.add(new Double(0));
			break;
		case "input":
			if (input.size() == 0) stack.add((double) -1);
			else {
				stack.add((double) ((int) input.get(input.size()-1).toCharArray()[0]));
				input.remove(input.size()-1);
			}
			break;
		case "inlength":
			stack.add((double)input.size());
			break;
		case "wait":
			Thread.sleep((long) (stack.get(stack.size()-1)*1000));
			stack.remove(stack.size()-1);
			break;
		case "ifnot":
			if (stack.get(stack.size()-1).intValue() == 0 && instruct[(direction) ? (position + 1)%instruct.length: (position - 1 >= 0) ? position - 1: instruct.length-1].equals("[")) {
				stack.remove(stack.size()-1);
				position = (direction) ? (position + 1)%instruct.length: (position - 1 >= 0) ? position - 1: instruct.length-1;
				position = (direction) ? (position + 1)%instruct.length: (position - 1 >= 0) ? position - 1: instruct.length-1;
				while (!instruct[position].equals("]")){
					opHandle();
					position = (direction) ? (position + 1)%instruct.length: (position - 1 >= 0) ? position - 1: instruct.length-1;
				}
			} else if (stack.get(stack.size()-1).intValue() == 0) {
				stack.remove(stack.size()-1);
				position = (direction) ? (position + 1)%instruct.length: (position - 1 >= 0) ? position - 1: instruct.length-1;
			}
			else if (instruct[(direction) ? (position + 1)%instruct.length: (position - 1 >= 0) ? position - 1: instruct.length-1].equals("[")) {
				stack.remove(stack.size()-1);
				while (!instruct[position].equals("]")) {
					position = (direction) ? (position + 1)%instruct.length: (position - 1 >= 0) ? position - 1: instruct.length-1;
				}
			} else stack.remove(stack.size()-1);
			break;
		case "if":
			if (!(stack.get(stack.size()-1).intValue() == 0) && instruct[(direction) ? (position + 1)%instruct.length: (position - 1 >= 0) ? position - 1: instruct.length-1].equals("[")) {
				stack.remove(stack.size()-1);
				position = (direction) ? (position + 1)%instruct.length: (position - 1 >= 0) ? position - 1: instruct.length-1;
				position = (direction) ? (position + 1)%instruct.length: (position - 1 >= 0) ? position - 1: instruct.length-1;
				while (!instruct[position].equals("]")){
					opHandle();
					position = (direction) ? (position + 1)%instruct.length: (position - 1 >= 0) ? position - 1: instruct.length-1;
				}
			} else if (!(stack.get(stack.size()-1).intValue() == 0)) {
				stack.remove(stack.size()-1);
				position = (direction) ? (position + 1)%instruct.length: (position - 1 >= 0) ? position - 1: instruct.length-1;
			}
			else if (instruct[(direction) ? (position + 1)%instruct.length: (position - 1 >= 0) ? position - 1: instruct.length-1].equals("[")) {
				stack.remove(stack.size()-1);
				while (!instruct[position].equals("]")) {
					position = (direction) ? (position + 1)%instruct.length: (position - 1 >= 0) ? position - 1: instruct.length-1;
				}
			} else stack.remove(stack.size()-1);
			break;
		case "skip":
			position = (direction) ? (position + 1)%instruct.length: (position - 1 >= 0) ? position - 1: instruct.length-1;
			break;
		case "loop":
			loopHandler();
			break;
		case "sine":
			stack.set(stack.size()-1,(Math.sin(stack.get(stack.size()-1))));
			break;
		case "asine":
			stack.set(stack.size()-1,(Math.asin(stack.get(stack.size()-1))));
			break;
		case "cosine":
			stack.set(stack.size()-1,(Math.cos(stack.get(stack.size()-1))));
			break;
		case "acosine":
			stack.set(stack.size()-1,(Math.acos(stack.get(stack.size()-1))));
			break;
		case "tangent":
			stack.set(stack.size()-1,(Math.tan(stack.get(stack.size()-1))));
			break;
		case "atangent":
			stack.set(stack.size()-1,(Math.atan(stack.get(stack.size()-1))));
			break;
		case "repnextchar":
			int repeats = stack.get(stack.size()-1).intValue();
			stack.remove(stack.size()-1);
			forLoopHandler(repeats);
			break;
		case "length":
			stack.add((double)stack.size());
			break;
		case "pi":
			stack.add((Math.PI));
			break;
		case "E":
			stack.add((Math.E));
			break;
		case "log":
			stack.set(stack.size()-2, (Math.log(stack.get(stack.size()-2))/Math.log(stack.get(stack.size()-1))));
			stack.remove(stack.size()-1);
			break;
		case "outnum":
			Double x = stack.get(stack.size()-1);
			if (x > 10000 && x.intValue() == x)
				System.out.printf("%f", x.intValue());
			else if (x > 10000) 
				System.out.printf("%f", x);
			else if (x.intValue() == x) 
				System.out.print(x.intValue());
			else
				System.out.print(x);
			stack.remove(stack.size()-1);
			break;
		case "rand":
			stack.set(stack.size()-1, (Math.random()*stack.get(stack.size()-1)));
			break;
		case "outchar":
			System.out.print((char) stack.get(stack.size()-1).intValue());
			stack.remove(stack.size()-1);
			break;
		case "end":
			System.out.println();
			System.exit(0);
		case "teleport":
			position = stack.get(stack.size()-1).intValue()-2;
			stack.remove(stack.size()-1);
			break;
		case "reverse":
			Collections.reverse(stack);
			break;
		case "rotateright":
			@SuppressWarnings("all")
			ArrayList<Double> temp1 = new ArrayList(0);
			for (int k = 0; k < stack.size(); k++) {
				temp1.add(stack.get((k+1)%stack.size()));
			}
			stack = temp1;
			break;
		case "rotateleft":
			@SuppressWarnings("all")
			ArrayList<Double> temp2 = new ArrayList(0);
			for (int k = 0; k < stack.size(); k++) {
				temp2.add(stack.get((k-1 < 0) ? stack.size()-1: k-1));
			}
			stack = temp2;
			break;
		case "duplicate":
			stack.add(stack.get(stack.size()-1));
			break;
		case "tempvar":
			if (tempvar == null) {
				tempvar = stack.get(stack.size()-1);
				stack.remove(stack.size()-1);
			}
			else {
				stack.add(tempvar);
				tempvar = null;
			}
			break;
		case "globalvar":
			if (globalvar == null) {
				globalvar = stack.get(stack.size()-1);
				stack.remove(stack.size()-1);
			}
			else stack.add(globalvar);
			break;
		case "part":
			stack.set(stack.size()-1, stack.get(stack.get(stack.size()-1).intValue()));
			break;
		case "add":
			stack.set(stack.size()-2,stack.get(stack.size()-1)+(stack.get(stack.size()-2)));
			stack.remove(stack.size()-1);
			break;
		case "subtract":
			stack.set(stack.size()-2,stack.get(stack.size()-2)-(stack.get(stack.size()-1)));
			stack.remove(stack.size()-1);
			break;
		case "multiply":
			stack.set(stack.size()-2,stack.get(stack.size()-1)*(stack.get(stack.size()-2)));
			stack.remove(stack.size()-1);
			break;
		case "divide":
			stack.set(stack.size()-2,stack.get(stack.size()-2)/(stack.get(stack.size()-1)));
			stack.remove(stack.size()-1);
			break;
		case "equal":
			stack.set(stack.size()-2, (double) ((stack.get(stack.size()-2)==stack.get(stack.size()-1))? 0: 1));
			stack.remove(stack.size()-1);
			break;
		case "modulo":
			stack.set(stack.size()-2,stack.get(stack.size()-2)%stack.get(stack.size()-1));
			stack.remove(stack.size()-1);
			break;
		case "power":
			stack.set(stack.size()-2,Math.pow(stack.get(stack.size()-2),stack.get(stack.size()-1)));
			stack.remove(stack.size()-1);
			break;
		case "factorial":
			// Approximating the Gamma function if x is NOT an int.
			double output = 1;
			Double y = stack.get(stack.size()-1);
			if (y.intValue() == y) {
				for (int i=1; i<=y; i++) {
					output*=i;
				}
			}
			else {
				output = Math.exp(((y - 0.5) * Math.log(y + 4.5) - (y + 4.5))+(1.0 + 76.18009173/(y + 0) - 86.50532033/(y + 1) + 24.01409822/(y + 2) - 1.231739516/(y + 3) + 0.00120858003/(y + 4) - 0.00000536382/(y + 5)));
			}
			stack.set(stack.size()-1, output);
			break;
		case "quote":
			while (true) {
				position = (direction) ? (position + 1)%instruct.length: (position - 1 >= 0) ? position - 1: instruct.length-1;
				if (instruct[position].equals("\"") || instruct[position].equals("\'")) break;
				stack.add(((double)instruct[position].toCharArray()[0]));
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
		case "err":
			System.err.println("Unknown character: "+instruct[position]);
			System.out.println();
			System.exit(2);
		}
	}
}