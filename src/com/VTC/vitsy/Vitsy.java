package com.VTC.vitsy;
import java.io.*;
import java.util.*;
import javax.script.*;

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

	private static Scanner in = new Scanner(System.in);

	private static boolean ending = false;

	private static int currin = 0;

	private static Double tempvar = null;

	private static Double globalvar = null;

	private static ArrayList<Boolean> looping = new ArrayList(0);
	
	private static ScriptEngine jsengine = new ScriptEngineManager().getEngineByName("js");

	public static void main(String[] args) throws InterruptedException, IOException, ScriptException {
		stac.add(new ArrayList(0));
		looping.add(false);
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
		while (!ending) {
			if (!looping.get(looping.size()-1) && position > currin().length - 1 && oldpos.size()==0) System.exit(0);
			else if (position < currin().length && !currin()[position].equals(""))
				opHandle();
			position = (direction) ? (position + 1): (position - 1 >= 0) ? position - 1: currin().length-1;
		}
		in.close();
	}

	public static void methodHandler(int source) throws InterruptedException, IOException, ScriptException {
		olddir.add(direction);
		direction = true;
		if (source == -1) {
			oldpos.add(new Integer[]{currin,position});
			currin = top().intValue();
			position = 0;
			rmtop();
			while (position < currin().length && !ending) {
				if (!looping.get(looping.size()-1) && position > currin().length - 1) {
					break;
				}
				else opHandle();
				position = (direction) ? (position + 1): (position - 1 >= 0) ? position - 1: currin().length-1;
			}
			currin = oldpos.get(oldpos.size()-1)[0].intValue();
			position = oldpos.get(oldpos.size()-1)[1].intValue();
			oldpos.remove(oldpos.size()-1);
			direction = olddir.get(olddir.size()-1);
			olddir.remove(olddir.size()-1);
			ending = false;
			return;
		}
		instruct.add(FileHandler.getFileInstruct(new String[]{(source == -2)?extender.get(extender.size()-1):users.get(users.size()-1)[source]}, new boolean[]{false, false}));
		users.add(FileHandler.getFileInstruct(new String[]{(source == -2)?extender.get(extender.size()-1):users.get(users.size()-1)[source]}, new boolean[]{true, false}).get(0));
		extender.add(FileHandler.getFileInstruct(new String[]{(source == -2)?extender.get(extender.size()-1):users.get(users.size()-2)[source]}, new boolean[]{false, true}).get(0)[0]);
		oldposext.add(new Integer[]{currin, position});
		currin = (source == -2)?currin:top().intValue();
		currclassname.add((source == -2)?extender.get(extender.size()-2):users.get(users.size()-2)[source]);
		position = 0;
		if (source != -2) rmtop();
		while (position < currin().length && !ending) {
			if (!looping.get(looping.size()-1) && position > currin().length - 1) {
				break;
			}
			else if (!currin()[position].equals(""))
				opHandle();
			position = (direction) ? (position + 1): (position - 1 >= 0) ? position - 1: currin().length-1;
		}
		instruct.remove(instruct.size()-1);
		currin = oldposext.get(oldposext.size()-1)[0].intValue();
		position = oldposext.get(oldposext.size()-1)[1].intValue();
		oldposext.remove(oldposext.size()-1);
		currclassname.remove(currclassname.size()-1);
		direction = olddir.get(olddir.size()-1);
		olddir.remove(olddir.size()-1);
		ending = false;
	}

	public static void forLoopHandler(int reps) throws InterruptedException, IOException, ScriptException {
		looping.add(true);
		move();

		if (!currin()[position].equals("[")) {
			for (int x = 0; x < reps; x++) {
				opHandle();
			}
		} else {
			int startPos = position;
			outerloop:
				for (int x = 0; x < reps && !ending; x++) {	
					position = (direction) ? (startPos + 1)%currin().length: (!(startPos - 1 < 0)) ? startPos - 1: currin().length-1;
					while(!currin()[position].equals("]")) {
						if (OperativeHandler.doOperation((String) currin()[position]) == ";") {
							break outerloop;
						}
						opHandle();
						move();
					}
				}
		}
		looping.remove(looping.size()-1);
		ending = false;
	}

	public static void loopHandler() throws InterruptedException, IOException, ScriptException {
		looping.add(true);
		int startPos = position;

		while (!ending) {
			move();
			if (currin()[position].equals("]")) {
				position = (direction) ? (startPos + 1)%currin().length: (startPos - 1 >= 0) ? startPos - 1: currin().length-1;
			}
			opHandle();
		}
		while (!currin()[position].equals("]"))
			move();
			looping.remove(looping.size()-1);
			ending = false;
	}

	public static void opHandle() throws InterruptedException, IOException, ScriptException {
		boolean makingObject = false;
		switch (OperativeHandler.doOperation((String) currin()[position])) {
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
			if (input.size() == 0) stac.get(currstac).add(-1.0);
			else {
				stac.get(currstac).add((double) ((int) input.get(input.size()-1).toCharArray()[0]));
				input.remove(input.size()-1);
			}
			break;

		case "inlength":
			stac.get(currstac).add((double)input.size());
			break;

		case "wait":
			Thread.sleep((long) (top()*1000));
			rmtop();
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

		case "write":
			if (stac.size() < 2)
				break;
			String filename = "";
			for (int i = stac.get(currstac).size()-1; i >= 0; i--) {
				filename+=new String(new char[]{(char)stac.get(currstac).get(i).intValue()});
			}
			if (new File(filename).exists()) break;
			stac.remove(currstac);
			try {
				stac.get(currstac);
			} catch (Exception e) {
				if (stac.size() > 0) currstac = (currstac + 1) % stac.size();
			}
			try (PrintWriter out = new PrintWriter(new File(filename))) {
				for (int i = stac.get(currstac).size()-1; i >=0; i--)
					out.print((char) stac.get(currstac).get(i).intValue());
			} catch (Exception e) {}
			stac.set(currstac, new ArrayList(0));
			break;

		case "shell":
			String fullout = "";
			String command = "";
			for (int i = stac.get(currstac).size()-1; i >= 0; i--)
				command += (char) stac.get(currstac).get(i).intValue();
			stac.set(currstac, new ArrayList(0));
			try {
				Process proc = Runtime.getRuntime().exec(command);
				BufferedReader br = new BufferedReader(new InputStreamReader(proc.getInputStream()));
				String line;
				while ((line = br.readLine()) != null)
					fullout += line;
				try {
					stac.get(currstac).add(Double.parseDouble(fullout));
				} catch (Exception e) {
					for (int i = fullout.length(); i > 0;) {
						stac.get(currstac).add(new Double((int) ((char) fullout.substring(i-1, i--).toCharArray()[0])));
					}
				}
			} catch (Exception e) {}
			break; 

		case "eval":
			String toEvaluate = "";
			for (int i = stac.get(currstac).size() - 1; i >= 0; i--) toEvaluate += new String(new char[]{(char) (stac.get(currstac).get(i).intValue())});
			stac.set(currstac, new ArrayList(0));
			try {
				stac.get(currstac).add((Double) jsengine.eval(toEvaluate));
			} catch (ClassCastException e) {
				try {
					stac.get(currstac).add(((Integer) jsengine.eval(toEvaluate)).doubleValue());
				} catch (ClassCastException ex) {
					char[] output = ((String) jsengine.eval(toEvaluate)).toCharArray();
					for (int i = output.length - 1; i >= 0; i--) {
						stac.get(currstac).add(new Double((int) output[i]));
					}
				}
			} catch (Exception e) {
				stac.get(currstac).add(0/0.0);
			}
			break;
			
		case "ifnot":
			if (top().intValue() == 0 && currin()[(direction) ? (position + 1)%currin().length: (position - 1 >= 0) ? position - 1: currin().length-1].equals("[")) {
				rmtop();
				move();
				move();
				while (!currin()[position].equals("]")){
					opHandle();
					move();
				}
			} else if (top().intValue() == 0) {
				rmtop();
				move();
			}
			else if (currin()[(direction) ? (position + 1)%currin().length: (position - 1 >= 0) ? position - 1: currin().length-1].equals("[")) {
				rmtop();
				while (!currin()[position].equals("]")) {
					move();
				}
			} else rmtop();
			break;

		case "if":
			if (!(top().intValue() == 0) && currin()[(direction) ? (position + 1)%currin().length: (position - 1 >= 0) ? position - 1: currin().length-1].equals("[")) {
				rmtop();
				move();
				move();
				while (!currin()[position].equals("]")){
					opHandle();
					move();
				}
			} else if (!(top().intValue() == 0)) {
				rmtop();
				move();
			}
			else if (currin()[(direction) ? (position + 1)%currin().length: (position - 1 >= 0) ? position - 1: currin().length-1].equals("[")) {
				rmtop();
				while (!currin()[position].equals("]")) {
					move();
				}
			} else rmtop();
			break;

		case "skip":
			move();
			break;

		case "loop":
			loopHandler();
			break;

		case "sine":
			stac.get(currstac).set(stac.get(currstac).size()-1,(Math.sin(top())));
			break;

		case "asine":
			stac.get(currstac).set(stac.get(currstac).size()-1,(Math.asin(top())));
			break;

		case "cosine":
			stac.get(currstac).set(stac.get(currstac).size()-1,(Math.cos(top())));
			break;

		case "acosine":
			stac.get(currstac).set(stac.get(currstac).size()-1,(Math.acos(top())));
			break;

		case "tangent":
			stac.get(currstac).set(stac.get(currstac).size()-1,(Math.tan(top())));
			break;

		case "atangent":
			stac.get(currstac).set(stac.get(currstac).size()-1,(Math.atan(top())));
			break;

		case "repnextchar":
			int repeats = top().intValue();
			rmtop();
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
			stac.get(currstac).set(stac.get(currstac).size()-2, (Math.log(index(2))/Math.log(top())));
			rmtop();
			break;

		case "outnum":
			Double x = top();
			if (x > 10000 && x.intValue() == x)
				System.out.print(x.intValue());
			else if (x > 10000) 
				System.out.printf("%f", x);
			else if (x.intValue() == x) 
				System.out.print(x.intValue());
			else
				System.out.print(x);
			rmtop();
			break;

		case "rand":
			stac.get(currstac).set(stac.get(currstac).size()-1, (Math.random()*top()));
			break;

		case "outchar":
			System.out.print((char) top().intValue());
			rmtop();
			break;

		case "end":
			ending = true;
			break;

		case "printall":
			int reps = stac.get(currstac).size();
			for(int i = reps - 1; i >= 0; i--) {
				System.out.print((char) top().intValue());
				rmtop();
			}
			break;

		case "getall":
			int reps2 = input.size();
			for(int i = reps2 - 1; i >= 0; i--) {
				stac.get(currstac).add((double) ((int) input.get(input.size()-1).toCharArray()[0]));
				input.remove(input.size()-1);
			}
			break;

		case "prompt":
			String prompt = "";
			prompt = in.nextLine();
			try {
				stac.get(currstac).add(Double.parseDouble(prompt));
			} catch (Exception e) {
				for (int i = prompt.length(); i > 0;) {
					stac.get(currstac).add(new Double((int) ((char) prompt.substring(i-1, i--).toCharArray()[0])));
				}
			}
			break;

		case "teleport":
			position = top().intValue()-2;
			rmtop();
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
			stac.get(currstac).add(top().doubleValue());
			break;

		case "tempvar":
			if (tempvar == null) {
				tempvar = top();
				rmtop();
			}
			else {
				stac.get(currstac).add(tempvar);
				tempvar = null;
			}
			break;

		case "globalvar":
			if (globalvar == null) {
				globalvar = top();
				rmtop();
			}
			else stac.get(currstac).add(globalvar);
			break;

		case "remove":
			rmtop();
			break;

		case "part":
			stac.get(currstac).set(stac.get(currstac).size()-1, stac.get(currstac).get(top().intValue()));
			break;

		case "classmethod":
			int methodToUse = index(2).intValue();
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
			int usereference = top().intValue();
			rmtop();
			char[] classname = ((usereference >= 0)?users.get(users.size()-1)[usereference]:(usereference == -1)?currclassname.get(currclassname.size()-1):extender.get(extender.size()-1)).toCharArray();
			for (int i = classname.length-1; i != -1; i--) {
				stac.get(currstac).add(new Double((int) classname[i]));
			}
			break;

		case "objects":
			objects.add(stac.get(currstac));
			position = (direction) ? (position + 1): (position - 1 >= 0) ? position - 1: currin().length-1;
			int k = objectrefsold.indexOf(currin()[position]);
			if (k != -1) {
				objectrefs.remove(k);
			}
			objectrefs.add(currin()[position]);
			makingObject = true;

		case "rmstack":
			stac.remove(currstac);
			if (stac.size() != 0) {
				try {
					stac.get(currstac);
				} catch (Exception e) {
					currstac = (currstac + ((makingObject)?-1:1)) % stac.size();
				}
			}
			makingObject = false;
			break;

		case "clnstack": 
			stac.add(new ArrayList(stac.get(currstac)));
		case "changestack":
			if (stac.size() > 0) currstac = (currstac + 1) % stac.size();
			break;

		case "newstack": 
			stac.add(new ArrayList(0));
			if (stac.size() > 0) currstac = (currstac + 1) % stac.size();
			break;

		case "numstack":
			stac.get(currstac).add((double) stac.size());
			break;

		case "add":
			stac.get(currstac).set(stac.get(currstac).size()-2,top()+(index(2)));
			rmtop();
			break;

		case "subtract":
			stac.get(currstac).set(stac.get(currstac).size()-2,index(2)-(top()));
			rmtop();
			break;

		case "multiply":
			stac.get(currstac).set(stac.get(currstac).size()-2,top()*(index(2)));
			rmtop();
			break;

		case "divide":
			stac.get(currstac).set(stac.get(currstac).size()-2,index(2)/(top()));
			rmtop();
			break;

		case "equal":
			stac.get(currstac).set(stac.get(currstac).size()-2, (double) ((index(2).doubleValue() == top().doubleValue())? 1: 0));
			rmtop();
			break;

		case "modulo":
			stac.get(currstac).set(stac.get(currstac).size()-2,index(2)%top());
			rmtop();
			break;

		case "power":
			stac.get(currstac).set(stac.get(currstac).size()-2,Math.pow(index(2),top()));
			rmtop();
			break;

		case "factorial":
			double output = 1;
			Double y = top();
			for (int i=1; i<=y; i++) {
				output*=i;
			}
			stac.get(currstac).set(stac.get(currstac).size()-1, output);
			break;

		case "quote":
			String quotetype = currin()[position];
			while (true) {
				move();
				if (currin()[position].equals(quotetype) || currin()[position].equals(quotetype)) break;
				stac.get(currstac).add(((double)((String)currin()[position]).toCharArray()[0]));
			}
			break;

		case "prime":
			Double n = top();
			rmtop();
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
			int i = objectrefs.indexOf(currin()[position]);
			if (i != -1) {
				stac.add(objects.get(i));
				currstac = (currstac + 1) % stac.size();
				objects.remove(i);
				objectrefsold.add(objectrefs.get(i));
				objectrefs.remove(i);
			} else if ((i = objectrefsold.indexOf(currin()[position])) != -1) {
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
	private static Double top() {
		return index(1);
	}
	private static Double index(int i) {
		return stac.get(currstac).get(stac.get(currstac).size()-i);
	}
	private static String[] currin() {
		return instruct.get(instruct.size()-1).get(currin);
	}
	private static void move() {
		position = (direction) ? (position + 1)%currin().length: (position - 1 >= 0) ? position - 1: currin().length-1;
	}
	private static void rmtop() {
		stac.get(currstac).remove(stac.get(currstac).size()-1);
	}
}