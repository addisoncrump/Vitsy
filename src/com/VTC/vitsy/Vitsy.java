package com.VTC.vitsy;

import java.io.*;
import java.util.*;
import javax.script.*;

/**
 * Main Vitsy class.
 * 
 * @author VTCAKAVSMoACE
 *
 */
@SuppressWarnings("all")
public class Vitsy {
	private boolean direction = true;

	private int position = 0;

	private ArrayList<ArrayList<Double>> stac = new ArrayList(0);

	private ArrayList<Integer[]> oldpos = new ArrayList(0);

	private int currstac = 0;

	private ArrayList<String> input = new ArrayList(0);

	private ArrayList<ArrayList<String[]>> instruct = new ArrayList(0);

	private ArrayList<Double[]> objects = new ArrayList(0);

	private ArrayList<String> objectrefs = new ArrayList(0);

	private ArrayList<String> objectrefsold = new ArrayList(0);

	private ArrayList<Integer[]> oldposext = new ArrayList(0);

	private ArrayList<String> extender = new ArrayList(0);

	private ArrayList<String[]> users = new ArrayList(0);

	private ArrayList<String> currclassname = new ArrayList(0);

	private ArrayList<Boolean> olddir = new ArrayList(0);

	private Scanner in = new Scanner(System.in);

	private boolean ending = false;

	private int currin = 0;

	private Double tempvar = null;

	private Double globalvar = null;

	private ArrayList<Boolean> looping = new ArrayList(0);

	private ScriptEngine jsengine = new ScriptEngineManager().getEngineByName("js");

	private Operable operationType = new GolfHandler();

	private FileHandler fileType = new GolfFileHandler();

	private boolean codeOnly = false;

	public static void main(String[] arg)
			throws InterruptedException, IOException, ScriptException, UnrecognizedInstructionException {
		ArrayList<String> args = new ArrayList(Arrays.asList(arg));
		Vitsy mainVitsy = new Vitsy(args.remove("--verbose"));
		mainVitsy.codeOnly = args.remove("--code");
		if (args.size() == 0) {
			errorExit();
		}
		mainVitsy.run(args);
	}

	public Vitsy(boolean verbose) {
		stac.add(new ArrayList(0));
		looping.add(false);
		if (verbose) {
			fileType = new VerboseFileHandler();
			operationType = new VerboseHandler();
		}
	}

	public void run(ArrayList<String> args)
			throws InterruptedException, IOException, ScriptException, UnrecognizedInstructionException {
		if (args.size() > 1) {
			boolean cansplit = true;
			if (args.size() > 1) {
				String arrin = args.get(1);
				for (int i = 2; i < args.size(); i++) {
					arrin += " " + args.get(i);
				}
				try {
					for (int i = 1; i < args.size(); i++)
						Double.parseDouble(args.get(i));
				} catch (Exception e) {
					cansplit = false;
				}
				String[] arrinput = (cansplit) ? arrin.split(" ") : arrin.split("");
				for (int i = 0; i < arrinput.length; i++) {
					if (cansplit)
						push(Double.parseDouble(arrinput[i]));
					else
						input.add(arrinput[i]);
				}
			}
		}
		if (!codeOnly) {
			try {
				extender.add(fileType.getFileInstruct(args, false, new boolean[] { false, true }).get(0)[0]);
				users.add(fileType.getFileInstruct(args, false, new boolean[] { true, false }).get(0));
			} catch (Exception e) {
			}
			currclassname.add(args.get(0));
		}
		instruct.add((ArrayList<String[]>) fileType.getFileInstruct(args, codeOnly, new boolean[] { false, false }));
		while (!ending) {
			if (!looping.get(looping.size() - 1) && position > currin().length - 1 && oldpos.size() == 0)
				System.exit(0);
			else if (position < currin().length && !currin()[position].equals(""))
				opHandle();
			move();
		}
		in.close();
	}

	public void methodHandler(int source)
			throws InterruptedException, IOException, ScriptException, UnrecognizedInstructionException {
		olddir.add(direction);
		direction = true;
		if (source == -1) {
			selfMethod();
			return;
		}
		if (source == -2) {
			superMethod();
		} else {
			useMethod(source);
		}
		while (position < currin().length && !ending) {
			if (!looping.get(looping.size() - 1) && position > currin().length - 1) {
				break;
			} else if (!currin()[position].equals(""))
				opHandle();
			move();
		}
		instruct.remove(instruct.size() - 1);
		currin = oldposext.get(oldposext.size() - 1)[0].intValue();
		position = oldposext.get(oldposext.size() - 1)[1].intValue();
		oldposext.remove(oldposext.size() - 1);
		currclassname.remove(currclassname.size() - 1);
		direction = olddir.get(olddir.size() - 1);
		olddir.remove(olddir.size() - 1);
		ending = false;
	}

	private void selfMethod()
			throws InterruptedException, IOException, ScriptException, UnrecognizedInstructionException {
		oldpos.add(new Integer[] { currin, position });
		currin = top().intValue();
		position = 0;
		rmtop();
		while (position < currin().length && !ending) {
			if (!looping.get(looping.size() - 1) && position > currin().length - 1) {
				break;
			} else
				opHandle();
			move();
		}
		currin = oldpos.get(oldpos.size() - 1)[0].intValue();
		position = oldpos.get(oldpos.size() - 1)[1].intValue();
		oldpos.remove(oldpos.size() - 1);
		direction = olddir.get(olddir.size() - 1);
		olddir.remove(olddir.size() - 1);
		ending = false;
	}

	private void superMethod() {
		instruct.add((ArrayList<String[]>) fileType.getFileInstruct(
				new ArrayList(Arrays
						.asList(new ArrayList(Arrays.asList(new String[] { extender.get(extender.size() - 1) })))),
				false, new boolean[] { false, false }));
		users.add(fileType
				.getFileInstruct(new ArrayList(Arrays.asList(new String[] { extender.get(extender.size() - 1) })),
						false, new boolean[] { true, false })
				.get(0));
		extender.add(fileType
				.getFileInstruct(new ArrayList(Arrays.asList(new String[] { extender.get(extender.size() - 1) })),
						false, new boolean[] { false, true })
				.get(0)[0]);
		oldposext.add(new Integer[] { currin, position });
		currclassname.add(extender.get(extender.size() - 2));
		position = 0;
	}

	private void useMethod(int source) {
		instruct.add((ArrayList<String[]>) fileType.getFileInstruct(
				new ArrayList(Arrays
						.asList(new ArrayList(Arrays.asList(new String[] { users.get(users.size() - 1)[source] })))),
				false, new boolean[] { false, false }));
		users.add(fileType
				.getFileInstruct(new ArrayList(Arrays.asList(new String[] { users.get(users.size() - 1)[source] })),
						false, new boolean[] { true, false })
				.get(0));
		extender.add(fileType
				.getFileInstruct(new ArrayList(Arrays.asList(new String[] { users.get(users.size() - 2)[source] })),
						false, new boolean[] { false, true })
				.get(0)[0]);
		oldposext.add(new Integer[] { currin, position });
		currin = top().intValue();
		currclassname.add(users.get(users.size() - 2)[source]);
		position = 0;
		rmtop();
	}

	public void forLoopHandler(int reps)
			throws InterruptedException, IOException, ScriptException, UnrecognizedInstructionException {
		looping.add(true);
		loopmove();

		if (!currin()[position].equals((operationType instanceof GolfHandler) ? "[" : "begin recursive area")) {
			for (int x = 0; x < reps; x++) {
				opHandle();
			}
		} else {
			int startPos = position;
			outerloop: for (int x = 0; x < reps && !ending; x++) {
				position = (direction) ? (startPos + 1) % currin().length
						: (!(startPos - 1 < 0)) ? startPos - 1 : currin().length - 1;
				while (!currin()[position]
						.equals((operationType instanceof GolfHandler) ? "]" : "end recursive area")) {
					if (operationType.doOperation((String) currin()[position]) == ";") {
						break outerloop;
					}
					opHandle();
					loopmove();
				}
			}
		}
		looping.remove(looping.size() - 1);
		ending = false;
	}

	public void loopHandler()
			throws InterruptedException, IOException, ScriptException, UnrecognizedInstructionException {
		looping.add(true);
		int startPos = position;

		while (!ending) {
			loopmove();
			if (currin()[position].equals((operationType instanceof GolfHandler) ? "]" : "end recursive area")) {
				position = (direction) ? (startPos + 1) % currin().length
						: (startPos - 1 >= 0) ? startPos - 1 : currin().length - 1;
			}
			opHandle();
		}
		while (!currin()[position].equals((operationType instanceof GolfHandler) ? "]" : "end recursive area"))
			loopmove();
		looping.remove(looping.size() - 1);
		ending = false;
	}

	public void opHandle() throws InterruptedException, IOException, ScriptException, UnrecognizedInstructionException {
		boolean makingObject = false;
		switch (operationType.doOperation((String) currin()[position])) {
		case "1":
			push(new Double(1));
			break;

		case "2":
			push(new Double(2));
			break;

		case "3":
			push(new Double(3));
			break;

		case "4":
			push(new Double(4));
			break;

		case "5":
			push(new Double(5));
			break;

		case "6":
			push(new Double(6));
			break;

		case "7":
			push(new Double(7));
			break;

		case "8":
			push(new Double(8));
			break;

		case "9":
			push(new Double(9));
			break;

		case "a":
			push(new Double(10));
			break;

		case "b":
			push(new Double(11));
			break;

		case "c":
			push(new Double(12));
			break;

		case "d":
			push(new Double(13));
			break;

		case "e":
			push(new Double(14));
			break;

		case "f":
			push(new Double(15));
			break;

		case "0":
			push(new Double(0));
			break;

		case "input":
			if (input.size() == 0)
				push(-1.0);
			else {
				push((double) ((int) input.get(input.size() - 1).toCharArray()[0]));
				input.remove(input.size() - 1);
			}
			break;

		case "inlength":
			push((double) input.size());
			break;

		case "wait":
			Thread.sleep((long) (top() * 1000));
			rmtop();
			break;

		case "method":
			methodHandler(-1);
			break;

		case "file":
			try {
				String filename = "";
				for (int i = stac.get(currstac).size() - 1; i >= 0; i--) {
					filename += new String(new char[] { (char) stac.get(currstac).get(i).intValue() });
				}
				stac.set(currstac, new ArrayList(0));
				File file = new File(filename);
				FileInputStream fis = new FileInputStream(file);
				byte[] data = new byte[(int) file.length()];
				fis.read(data);
				fis.close();
				char[] stuffs = new String(data, "UTF-8").toCharArray();
				for (int i = stuffs.length - 1; i >= 0; i--) {
					push((double) stuffs[i]);
				}
			} catch (FileNotFoundException e) {
			}
			break;

		case "write":
			if (stac.size() < 2)
				break;
			String filename = "";
			for (int i = stac.get(currstac).size() - 1; i >= 0; i--) {
				filename += new String(new char[] { (char) stac.get(currstac).get(i).intValue() });
			}
			if (new File(filename).exists())
				break;
			stac.remove(currstac);
			try {
				stac.get(currstac);
			} catch (Exception e) {
				if (stac.size() > 0)
					changeStack(true);
			}
			PrintWriter out = new PrintWriter(new File(filename));
			try {
				for (int i = stac.get(currstac).size() - 1; i >= 0; i--)
					out.print((char) stac.get(currstac).get(i).intValue());
			} catch (Exception e) {
			}
			out.close();
			stac.set(currstac, new ArrayList(0));
			break;

		case "shell":
			String fullout = "";
			String command = "";
			for (int i = stac.get(currstac).size() - 1; i >= 0; i--)
				command += (char) stac.get(currstac).get(i).intValue();
			stac.set(currstac, new ArrayList(0));
			try {
				Process proc = Runtime.getRuntime().exec(command);
				BufferedReader br = new BufferedReader(new InputStreamReader(proc.getInputStream()));
				String line;
				while ((line = br.readLine()) != null)
					fullout += line;
				try {
					push(Double.parseDouble(fullout));
				} catch (Exception e) {
					for (int i = fullout.length(); i > 0;) {
						push(new Double((int) ((char) fullout.substring(i - 1, i--).toCharArray()[0])));
					}
				}
			} catch (Exception e) {
			}
			break;

		case "eval":
			String toEvaluate = "";
			for (int i = stac.get(currstac).size() - 1; i >= 0; i--)
				toEvaluate += new String(new char[] { (char) (stac.get(currstac).get(i).intValue()) });
			stac.set(currstac, new ArrayList(0));
			Object evaluated = jsengine.eval(toEvaluate);
			if (evaluated instanceof Double)
				push((Double) evaluated);
			else if (evaluated instanceof Integer)
				push(((Integer) jsengine.eval(toEvaluate)).doubleValue());
			else if (evaluated instanceof String) {
				char[] output = ((String) jsengine.eval(toEvaluate)).toCharArray();
				for (int i = output.length - 1; i >= 0; i--)
					push(new Double((int) output[i]));
			} else {
				push(0 / 0.0);
			}
			break;

		case "ifnot":
			if (top().intValue() == 0 && currin()[(direction) ? (position + 1) % currin().length
					: (position - 1 >= 0) ? position - 1 : currin().length - 1]
							.equals((operationType instanceof GolfHandler) ? "[" : "begin recursive area")) {
				rmtop();
				loopmove();
				loopmove();
				while (!currin()[position]
						.equals((operationType instanceof GolfHandler) ? "]" : "end recursive area")) {
					opHandle();
					loopmove();
				}
			} else if (top().intValue() == 0) {
				rmtop();
				loopmove();
			} else if (currin()[(direction) ? (position + 1) % currin().length
					: (position - 1 >= 0) ? position - 1 : currin().length - 1]
							.equals((operationType instanceof GolfHandler) ? "[" : "begin recursive area")) {
				rmtop();
				while (!currin()[position].equals((operationType instanceof GolfHandler) ? "]" : "end recursive area"))
					loopmove();
			} else
				rmtop();
			break;

		case "if":
			if (!(top().intValue() == 0) && currin()[(direction) ? (position + 1) % currin().length
					: (position - 1 >= 0) ? position - 1 : currin().length - 1]
							.equals((operationType instanceof GolfHandler) ? "[" : "begin recursive area")) {
				rmtop();
				loopmove();
				loopmove();
				while (!currin()[position]
						.equals((operationType instanceof GolfHandler) ? "]" : "end recursive area")) {
					opHandle();
					loopmove();
				}
			} else if (!(top().intValue() == 0)) {
				rmtop();
				loopmove();
			} else if (currin()[(direction) ? (position + 1) % currin().length
					: (position - 1 >= 0) ? position - 1 : currin().length - 1]
							.equals((operationType instanceof GolfHandler) ? "[" : "begin recursive area")) {
				rmtop();
				while (!currin()[position].equals((operationType instanceof GolfHandler) ? "]" : "end recursive area"))
					loopmove();
			} else
				rmtop();
			break;

		case "skip":
			loopmove();
			break;

		case "loop":
			loopHandler();
			break;

		case "sine":
			settop((Math.sin(top())));
			break;

		case "asine":
			settop((Math.asin(top())));
			break;

		case "cosine":
			settop((Math.cos(top())));
			break;

		case "acosine":
			settop((Math.acos(top())));
			break;

		case "tangent":
			settop((Math.tan(top())));
			break;

		case "atangent":
			settop((Math.atan(top())));
			break;

		case "repnextchar":
			int repeats = top().intValue();
			rmtop();
			forLoopHandler(repeats);
			break;

		case "length":
			push((double) stac.get(currstac).size());
			break;

		case "pi":
			push((Math.PI));
			break;

		case "E":
			push((Math.E));
			break;

		case "log":
			setind(2, (Math.log(index(2)) / Math.log(top())));
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
			settop((Math.random() * top()));
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
			for (int i = reps - 1; i >= 0; i--) {
				System.out.print((char) top().intValue());
				rmtop();
			}
			break;

		case "getall":
			int reps2 = input.size();
			for (int i = reps2 - 1; i >= 0; i--) {
				push((double) ((int) input.get(input.size() - 1).toCharArray()[0]));
				input.remove(input.size() - 1);
			}
			break;

		case "prompt":
			String prompt = "";
			prompt = in.nextLine();
			try {
				push(Double.parseDouble(prompt));
			} catch (Exception e) {
				for (int i = prompt.length(); i > 0;) {
					push(new Double((int) ((char) prompt.substring(i - 1, i--).toCharArray()[0])));
				}
			}
			break;

		case "teleport":
			position = top().intValue() - 2;
			rmtop();
			break;

		case "reverse":
			Collections.reverse(stac.get(currstac));
			break;

		case "rotateright":
			ArrayList<Double> temp1 = new ArrayList(0);
			for (int k = 0; k < stac.get(currstac).size(); k++) {
				temp1.add(stac.get(currstac).get((k + 1) % stac.get(currstac).size()));
			}
			stac.remove(currstac);
			stac.add(temp1);
			break;

		case "rotateleft":
			ArrayList<Double> temp2 = new ArrayList(0);
			for (int k = 0; k < stac.get(currstac).size(); k++) {
				temp2.add(stac.get(currstac).get((k - 1 < 0) ? stac.get(currstac).size() - 1 : k - 1));
			}
			stac.remove(currstac);
			stac.add(temp2);
			break;

		case "duplicate":
			push(top().doubleValue());
			break;

		case "tempvar":
			if (tempvar == null) {
				tempvar = top();
				rmtop();
			} else {
				push(tempvar);
				tempvar = null;
			}
			break;

		case "globalvar":
			if (globalvar == null) {
				globalvar = top();
				rmtop();
			} else
				push(globalvar);
			break;

		case "remove":
			rmtop();
			break;

		case "part":
			settop(stac.get(currstac).get(top().intValue()));
			break;

		case "classmethod":
			int methodToUse = index(2).intValue();
			stac.get(currstac).remove(stac.get(currstac).size() - 2);
			methodHandler(methodToUse);
			break;

		case "super":
			methodHandler(-2);
			break;

		case "usecount":
			push(new Double(users.get(users.size() - 1).length));
			break;

		case "classname":
			int usereference = top().intValue();
			rmtop();
			char[] classname = ((usereference >= 0) ? users.get(users.size() - 1)[usereference]
					: (usereference == -1) ? currclassname.get(currclassname.size() - 1)
							: extender.get(extender.size() - 1)).toCharArray();
			for (int i = classname.length - 1; i != -1; i--) {
				push(new Double((int) classname[i]));
			}
			break;

		case "objects":
			objects.add((Double[]) stac.get(currstac).toArray());
			move();
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
					currstac = (currstac + ((makingObject) ? -1 : 1)) % stac.size();
				}
			}
			makingObject = false;
			break;

		case "clnstack":
			stac.add(new ArrayList(stac.get(currstac)));
			changeStack(true);
			break;

		case "rightstack":
			changeStack(true);
			break;

		case "leftstack":
			changeStack(false);
			break;

		case "newstack":
			stac.add(new ArrayList(0));
			if (stac.size() > 0)
				changeStack(true);
			break;

		case "numstack":
			push((double) stac.size());
			break;

		case "add":
			setind(2, top() + (index(2)));
			rmtop();
			break;

		case "subtract":
			setind(2, index(2) - (top()));
			rmtop();
			break;

		case "multiply":
			setind(2, top() * (index(2)));
			rmtop();
			break;

		case "divide":
			setind(2, index(2) / (top()));
			rmtop();
			break;

		case "equal":
			setind(2, (double) ((index(2).doubleValue() == top().doubleValue()) ? 1 : 0));
			rmtop();
			break;

		case "modulo":
			setind(2, index(2) % top());
			rmtop();
			break;

		case "power":
			setind(2, Math.pow(index(2), top()));
			rmtop();
			break;

		case "factorial":
			double output = 1;
			Double y = top();
			for (int i = 1; i <= y; i++) {
				output *= i;
			}
			settop(output);
			break;

		case "quote":
			String quotetype = currin()[position];
			while (true) {
				loopmove();
				if (currin()[position].equals(quotetype) || currin()[position].equals(quotetype))
					break;
				push(((double) ((String) currin()[position]).toCharArray()[0]));
			}
			break;

		case "flatten":
			currstac = (currstac - 1 + stac.size()) % stac.size();
			stac.get(currstac).addAll(stac.get((currstac + 1) % stac.size()));
			stac.remove(stac.get((currstac + 1) % stac.size()));
			break;

		case "range":
			int ind2 = index(2).intValue();
			int ind1 = top().intValue();
			rmtop();
			rmtop();
			int g = 0;
			for (g = ind2; g != ind1; g += (ind2 > ind1) ? -1 : 1)
				push(new Double(g));
			push(new Double(g));
			break;

		case "factorize":
			int f = top().intValue();
			rmtop();
			if (f < 0)
				push(new Double(-1));
			f = Math.abs(f);
			for (int i = 2; i <= f; i++) {
				while (f % i == 0) {
					push(new Double(i));
					f /= i;
				}
			}
			break;

		case "prime":
			Double n = top();
			rmtop();
			if (n < 2) {
				push(new Double(0));
				break;
			} else if (n == 2 || n == 3) {
				push(new Double(1));
				break;
			} else if (n % 2 == 0 || n % 3 == 0) {
				push(new Double(0));
				break;
			}
			long sqrtN = (long) Math.sqrt(n) + 1;
			for (long i = 6L; i <= sqrtN; i += 6) {
				if (n % (i - 1) == 0 || n % (i + 1) == 0) {
					push(new Double(1));
					break;
				}
			}
			push(new Double(1));
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
			direction = (Math.random() < .5);
			break;

		case "nothing":
			int i = objectrefs.indexOf(currin()[position]);
			if (i != -1) {
				stac.add((ArrayList<Double>) (Arrays.asList(objects.get(i))));
				changeStack(true);
				objects.remove(i);
				objectrefsold.add(objectrefs.get(i));
				objectrefs.remove(i);
			} else if ((i = objectrefsold.indexOf(currin()[position])) != -1) {
				objects.add((Double[]) stac.get(currstac).toArray());
				objectrefs.add(objectrefsold.get(i));
				objectrefsold.remove(i);
				stac.remove(currstac);
				try {
					stac.get(currstac);
				} catch (Exception e) {
					changeStack(true);
				}
			}
		}
	}

	private Double top() {
		return index(1);
	}

	private void changeStack(boolean chngDir) {
		if (stac.size() > 0)
			currstac = (chngDir) ? (currstac + 1) % stac.size() : (currstac - 1 >= 0) ? currstac - 1 : stac.size() - 1;
	}

	private Double index(int i) {
		return stac.get(currstac).get(stac.get(currstac).size() - i);
	}

	private void settop(Double x) {
		setind(1, x);
	}

	private void setind(int i, Double x) {
		stac.get(currstac).set(stac.get(currstac).size() - i, x);
	}

	private String[] currin() {
		return instruct.get(instruct.size() - 1).get(currin);
	}

	private void move() {
		position = (direction) ? (position + 1) : (position - 1 >= 0) ? position - 1 : currin().length - 1;
	}

	private void loopmove() {
		position = (direction) ? (position + 1) % currin().length
				: (position - 1 >= 0) ? position - 1 : currin().length - 1;
	}

	private void rmtop() {
		stac.get(currstac).remove(stac.get(currstac).size() - 1);
	}

	private void push(Double x) {
		stac.get(currstac).add(x);
	}

	public String toString() {
		String info = "";
		info += "Verbose: " + (operationType instanceof VerboseHandler);
		info += "\nInstruction Direction: " + ((direction) ? "right" : "left");
		info += "\nCurrent Line: " + currin;
		info += "\nCurrent Position: " + position;
		info += "\nInstuction Array (all loaded classes):";
		for (ArrayList<String[]> i : instruct) {
			info += "\n";
			for (String[] k : i) {
				for (String z : k) {
					info += z + ((operationType instanceof VerboseHandler) ? ";\n" : "");
				}
				info += "\n";
			}
		}
		info += "\nStack:";
		for (ArrayList<Double> i : stac) {
			info += "\n";
			for (Double k : i) {
				info += k + " ";
			}
		}
		return info;

	}

	private static void errorExit() {
		System.err.println("Syntax: java -jar Vitsy[Safe].jar [--verbose] <file name> [argument 1] [argument 2] [...]");
		System.err.println("OR");
		System.err
				.println("Syntax: java -jar Vitsy[Safe].jar [--verbose] --code <code> [argument 1] [argument 2] [...]");
		System.exit(1);
	}
}