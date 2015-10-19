package com.VTC.vitsy;
import java.util.*;

public class Vitsy {
	private static boolean direction = true;
	private static int position = 0;
	@SuppressWarnings("all")
	private static ArrayList<Double> stack = new ArrayList(0);
	private static String[] instruct = null;
	public static void main(String[] args) throws InterruptedException {
		if (args.length == 0) {
			System.err.println("Need a file pointer.");
			return;
		}
		instruct = FileHandler.getFileInstruct(args[0]);
		while (OperativeHandler.operating()) {
			position %= instruct.length;
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
			case "wait":
				Thread.sleep((long) (stack.get(stack.size()-1)*1000));
				stack.remove(stack.size()-1);
				break;
			case "ifnot":
				if ((stack.get(stack.size()-1)).equals(0)) position = (direction) ? (position + 1)%instruct.length: (position - 1 >= 0) ? position - 1: instruct.length-1;
				stack.remove(stack.size()-1);
				break;
			case "if":
				if (!(stack.get(stack.size()-1)).equals(0)) position = (direction) ? (position + 1)%instruct.length: (position - 1 >= 0) ? position - 1: instruct.length-1;
				stack.remove(stack.size()-1);
				break;
			case "skip":
				position = (direction) ? (position + 1)%instruct.length: (position - 1 >= 0) ? position - 1: instruct.length-1;
				break;
			case "loop":
				position = (direction) ? (position + 1)%instruct.length: (position - 1 >= 0) ? position - 1: instruct.length-1;
				loopHandler();
				break;
			case "sine":
				stack.set(stack.size()-1,(Math.sin(stack.get(stack.size()-1))));
				break;
			case "cosine":
				stack.set(stack.size()-1,(Math.cos(stack.get(stack.size()-1))));
				break;
			case "tangent":
				stack.set(stack.size()-1,(Math.tan(stack.get(stack.size()-1))));
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
				System.out.print(stack.get(stack.size()-1));
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
				int looptimesr = stack.get(stack.size()-1).intValue();
				stack.remove(stack.size()-1);
				for (int k = 0; k < looptimesr; k++) {
					stack.add(stack.get(stack.size()-1));
					for (int i = stack.size()-2; i >= 0; i++) stack.set(i, stack.get((i!=0) ? i-1: stack.size()-1));
					stack.remove(stack.size()-1);
				}
				break;
			case "rotateleft":
				int looptimesl = stack.get(stack.size()-1).intValue();
				stack.remove(stack.size()-1);
				for (int k = 0; k < looptimesl; k++) {
					stack.add(stack.get(0));
					for (int i = 0; i < stack.size()-1; i++) stack.set(i, stack.get(i+1));
					stack.remove(stack.size()-1);
				}
				break;
			case "duplicate":
				stack.add(stack.get(stack.size()-1));
				break;
			case "add":
				stack.set(stack.size()-2,stack.get(stack.size()-1)+(stack.get(stack.size()-2)));
				stack.remove(stack.size()-1);
				break;
			case "subtract":
				stack.set(stack.size()-2,stack.get(stack.size()-1)-(stack.get(stack.size()-2)));
				stack.remove(stack.size()-1);
				break;
			case "multiply":
				stack.set(stack.size()-2,stack.get(stack.size()-1)*(stack.get(stack.size()-2)));
				stack.remove(stack.size()-1);
				break;
			case "divide":
				stack.set(stack.size()-2,stack.get(stack.size()-1)/(stack.get(stack.size()-2)));
				stack.remove(stack.size()-1);
				break;
			case "modulo":
				stack.set(stack.size()-2,stack.get(stack.size()-1)%stack.get(stack.size()-2));
				stack.remove(stack.size()-1);
				break;
			case "power":
				stack.set(stack.size()-2,Math.pow(stack.get(stack.size()-2),stack.get(stack.size()-1)));
				stack.remove(stack.size()-1);
				break;
			case "quote":
				while (true) {
					position = (direction) ? (position + 1)%instruct.length: (position - 1 >= 0) ? position - 1: instruct.length-1;
					if (instruct[position].equals("\"")) break;
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
			position = (direction) ? (position + 1)%instruct.length: (position - 1 >= 0) ? position - 1: instruct.length-1;
		}
	}
	public static void loopHandler() throws InterruptedException {
		int startPos = position;
		while (true) {
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
			case "wait":
				Thread.sleep((long) (stack.get(stack.size()-1)*1000));
				stack.remove(stack.size()-1);
				break;
			case "ifnot":
				if ((stack.get(stack.size()-1)).equals(0)) position = (direction) ? (position + 1)%instruct.length: (position - 1 >= 0) ? position - 1: instruct.length-1;
				stack.remove(stack.size()-1);
				break;
			case "if":
				if (!(stack.get(stack.size()-1)).equals(0)) position = (direction) ? (position + 1)%instruct.length: (position - 1 >= 0) ? position - 1: instruct.length-1;
				stack.remove(stack.size()-1);
				break;
			case "skip":
				position = (direction) ? (position + 1)%instruct.length: (position - 1 >= 0) ? position - 1: instruct.length-1;
				break;
			case "loop":
				position = (direction) ? (position + 1)%instruct.length: (position - 1 >= 0) ? position - 1: instruct.length-1;
				loopHandler();
				break;
			case "sine":
				stack.set(stack.size()-1,(Math.sin(stack.get(stack.size()-1))));
				break;
			case "cosine":
				stack.set(stack.size()-1,(Math.cos(stack.get(stack.size()-1))));
				break;
			case "tangent":
				stack.set(stack.size()-1,(Math.tan(stack.get(stack.size()-1))));
				break;
			case "pi":
				stack.add(Math.PI);
				break;
			case "E":
				stack.add(Math.E);
				break;
			case "log":
				stack.set(stack.size()-2, (Math.log(stack.get(stack.size()-2))/Math.log(stack.get(stack.size()-1))));
				stack.remove(stack.size()-1);
				break;
			case "outnum":
				System.out.print(stack.get(stack.size()-1));
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
				break;
			case "teleport":
				position = stack.get(stack.size()-1).intValue();
				stack.remove(stack.size()-1);
				break;
			case "reverse":
				Collections.reverse(stack);
				break;
			case "rotateright":
				int looptimesr = stack.get(stack.size()-1).intValue();
				stack.remove(stack.size()-1);
				for (int k = 0; k < looptimesr; k++) {
					stack.add(stack.get(stack.size()-1));
					for (int i = stack.size()-2; i >= 0; i++) stack.set(i, stack.get((i!=0) ? i-1: stack.size()-1));
					stack.remove(stack.size()-1);
				}
				break;
			case "rotateleft":
				int looptimesl = stack.get(stack.size()-1).intValue();
				stack.remove(stack.size()-1);
				for (int k = 0; k < looptimesl; k++) {
					stack.add(stack.get(0));
					for (int i = 0; i < stack.size()-1; i++) stack.set(i, stack.get(i+1));
					stack.remove(stack.size()-1);
				}
				break;
			case "duplicate":
				stack.add(stack.get(stack.size()-1));
				break;
			case "add":
				stack.set(stack.size()-2,stack.get(stack.size()-1)+(stack.get(stack.size()-2)));
				stack.remove(stack.size()-1);
				break;
			case "subtract":
				stack.set(stack.size()-2,stack.get(stack.size()-1)-(stack.get(stack.size()-2)));
				stack.remove(stack.size()-1);
				break;
			case "multiply":
				stack.set(stack.size()-2,stack.get(stack.size()-1)*(stack.get(stack.size()-2)));
				stack.remove(stack.size()-1);
				break;
			case "divide":
				stack.set(stack.size()-2,stack.get(stack.size()-1)/(stack.get(stack.size()-2)));
				stack.remove(stack.size()-1);
				break;
			case "modulo":
				stack.set(stack.size()-2,stack.get(stack.size()-1)%stack.get(stack.size()-2));
				stack.remove(stack.size()-1);
				break;
			case "power":
				stack.set(stack.size()-2,Math.pow(stack.get(stack.size()-2),stack.get(stack.size()-1)));
				stack.remove(stack.size()-1);
				break;
			case "quote":
				while (true) {
					position = (direction) ? (position + 1)%instruct.length: (position - 1 >= 0) ? position - 1: instruct.length-1;
					if (instruct[position].equals("\"")) break;
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
			position = (direction) ? (position + 1)%instruct.length: (position - 1 >= 0) ? position - 1: instruct.length-1;
			if (instruct[position].equals("]")) {
				if (stack.get(stack.size()-1).intValue() == 0) { 
					stack.remove(stack.size()-1);
					break;
				}
				position = startPos+1;
			}
		}
	}
}