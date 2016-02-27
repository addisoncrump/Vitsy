package com.VTC.vitsy;

import java.io.*;
import java.math.BigDecimal;
import java.math.BigInteger;
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

	private ArrayList<ArrayList<BigDecimal>> stac = new ArrayList(0);

	private ArrayList<Integer[]> oldpos = new ArrayList(0);

	private int currstac = 0;

	private ArrayList<String> input = new ArrayList(0);

	private ArrayList<ArrayList<String[]>> instruct = new ArrayList(0);

	private ArrayList<BigDecimal[]> objects = new ArrayList(0);

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

	private BigDecimal tempvar = null;

	private BigDecimal globalvar = null;

	private ArrayList<Boolean> looping = new ArrayList(0);

	private ScriptEngine jsengine = new ScriptEngineManager().getEngineByName("js");

	private Operable operationType = new GolfHandler();

	private FileHandler fileType = new GolfFileHandler();

	public final boolean CODE_ONLY;
	
	public final boolean VERBOSE;

	public static void main(String[] arg)
			throws InterruptedException, IOException, ScriptException, UnrecognizedInstructionException {
		ArrayList<String> args = new ArrayList(Arrays.asList(arg));
		Vitsy mainVitsy = new Vitsy(args.remove("--verbose"), args.remove("--code"));
		if (args.remove("--convert")) {
			if (mainVitsy.VERBOSE)
				FullConversion.fullConvertGolfed(args.get(0));
			else
				FullConversion.fullConvertVerbose(args.get(0));
			return;
		}
		if (args.size() == 0) {
			errorExit();
		}
		try {
			mainVitsy.run(args);
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + " thrown in "+mainVitsy.currclassname.get(mainVitsy.currclassname.size()-1)+" at command #" + mainVitsy.currin + "," + mainVitsy.position);
		}
	}

	public Vitsy(boolean verbose, boolean codeOnly) {
		stac.add(new ArrayList(0));
		looping.add(false);
		if (verbose) {
			fileType = new VerboseFileHandler();
			operationType = new VerboseHandler();
		}
		CODE_ONLY = codeOnly;
		VERBOSE = verbose;
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
						new BigDecimal(args.get(i));
				} catch (Exception e) {
					cansplit = false;
				}
				String[] arrinput = (cansplit) ? arrin.split(" ") : arrin.split("");
				for (int i = 0; i < arrinput.length; i++) {
					if (cansplit)
						push(new BigDecimal(arrinput[i]));
					else
						input.add(arrinput[i]);
				}
			}
		}
		if (!CODE_ONLY) {
			try {
				extender.add(fileType.getFileInstruct(args, false, new boolean[] { false, true }).get(0)[0]);
				users.add(fileType.getFileInstruct(args, false, new boolean[] { true, false }).get(0));
			} catch (Exception e) {
			}
			currclassname.add(args.get(0));
		} else {
			currclassname.add("[command line]");
		}
		instruct.add((ArrayList<String[]>) fileType.getFileInstruct(args, CODE_ONLY, new boolean[] { false, false }));
		while (!ending) {
			if (!looping.get(looping.size() - 1) && position > currin().length - 1 && oldpos.size() == 0)
				System.exit(0);
			else if (position < currin().length)
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
			} else
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
		instruct.add(fileType.getFileInstruct(
				new ArrayList(Arrays.asList((new String[] { extender.get(extender.size() - 1) }))), false,
				new boolean[] { false, false }));
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
		instruct.add(fileType.getFileInstruct(
				new ArrayList(Arrays.asList((new String[] { users.get(users.size() - 1)[source] }))), false,
				new boolean[] { false, false }));
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

		if (!currin()[position].trim().equals((operationType instanceof GolfHandler) ? "[" : "begin recursive area")) {
			for (int x = 0; x < reps; x++) {
				opHandle();
			}
		} else {
			int startPos = position;
			outerloop: for (int x = 0; x < reps && !ending; x++) {
				position = (direction) ? (startPos + 1) % currin().length
						: (!(startPos - 1 < 0)) ? startPos - 1 : currin().length - 1;
				while (!currin()[position].trim()
						.equals((operationType instanceof GolfHandler) ? "]" : "end recursive area")) {
					if (operationType.doOperation(currin, position, (String) currin()[position].trim()) == "end") {
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
			if (currin()[position].trim().equals((operationType instanceof GolfHandler) ? "]" : "end recursive area")) {
				position = (direction) ? (startPos + 1) % currin().length
						: (startPos - 1 >= 0) ? startPos - 1 : currin().length - 1;
			}
			opHandle();
		}
		while (!currin()[position].trim().equals((operationType instanceof GolfHandler) ? "]" : "end recursive area"))
			loopmove();
		looping.remove(looping.size() - 1);
		ending = false;
	}

	public void opHandle() throws InterruptedException, IOException, ScriptException, UnrecognizedInstructionException {
		boolean makingObject = false;
		switchout:
		switch (operationType.doOperation(currin, position, (String) currin()[position].trim())) {
		case "1":
			push(new BigDecimal(1));
			break;

		case "2":
			push(new BigDecimal(2));
			break;

		case "3":
			push(new BigDecimal(3));
			break;

		case "4":
			push(new BigDecimal(4));
			break;

		case "5":
			push(new BigDecimal(5));
			break;

		case "6":
			push(new BigDecimal(6));
			break;

		case "7":
			push(new BigDecimal(7));
			break;

		case "8":
			push(new BigDecimal(8));
			break;

		case "9":
			push(new BigDecimal(9));
			break;

		case "a":
			push(new BigDecimal(10));
			break;

		case "b":
			push(new BigDecimal(11));
			break;

		case "c":
			push(new BigDecimal(12));
			break;

		case "d":
			push(new BigDecimal(13));
			break;

		case "e":
			push(new BigDecimal(14));
			break;

		case "f":
			push(new BigDecimal(15));
			break;

		case "0":
			push(new BigDecimal(0));
			break;

		case "input":
			if (input.size() == 0)
				push(new BigDecimal(-1.0));
			else {
				push(new BigDecimal((int) input.get(input.size() - 1).toCharArray()[0]));
				input.remove(input.size() - 1);
			}
			break;

		case "inlength":
			push(new BigDecimal((double) input.size()));
			break;

		case "wait":
			Thread.sleep((long) (top().doubleValue() * 1000));
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
					push(new BigDecimal((double) stuffs[i]));
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
					push(new BigDecimal(Double.parseDouble(fullout)));
				} catch (Exception e) {
					for (int i = fullout.length(); i > 0;) {
						push(new BigDecimal((int) ((char) fullout.substring(i - 1, i--).toCharArray()[0])));
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
			try {
				push(new BigDecimal(Double.parseDouble(evaluated.toString())));
			} catch (Exception e) {
				try {
					char[] output = evaluated.toString().toCharArray();
					for (int i = output.length - 1; i >= 0; i--)
						push(new BigDecimal((int) output[i]));
				} catch (Exception i) {}
			}
			break;

		case "ifnot":
			if (top().toBigInteger().compareTo(new BigDecimal(0).toBigInteger()) == 0 && currin()[(direction)
					? (position + 1) % currin().length : (position - 1 >= 0) ? position - 1 : currin().length - 1]
							.equals((operationType instanceof GolfHandler) ? "[" : "begin recursive area")) {
				rmtop();
				loopmove();
				loopmove();
				while (!currin()[position].trim()
						.equals((operationType instanceof GolfHandler) ? "]" : "end recursive area")) {
					opHandle();
					loopmove();
				}
			} else if (top().toBigInteger().compareTo(new BigDecimal(0).toBigInteger()) == 0)
				rmtop();
			else if (currin()[(direction) ? (position + 1) % currin().length
					: (position - 1 >= 0) ? position - 1 : currin().length - 1]
							.equals((operationType instanceof GolfHandler) ? "[" : "begin recursive area")) {
				rmtop();
				while (!currin()[position].trim()
						.equals((operationType instanceof GolfHandler) ? "]" : "end recursive area"))
					loopmove();
			} else {
				rmtop();
				loopmove();
			}
			break;

		case "if":
			if (top().toBigInteger().compareTo(new BigDecimal(0).toBigInteger()) != 0 && currin()[(direction)
					? (position + 1) % currin().length : (position - 1 >= 0) ? position - 1 : currin().length - 1]
							.equals((operationType instanceof GolfHandler) ? "[" : "begin recursive area")) {
				rmtop();
				loopmove();
				loopmove();
				while (!currin()[position].trim()
						.equals((operationType instanceof GolfHandler) ? "]" : "end recursive area")) {
					opHandle();
					loopmove();
				}
			} else if (top().toBigInteger().compareTo(new BigDecimal(0).toBigInteger()) != 0)
				rmtop();
			else if (currin()[(direction) ? (position + 1) % currin().length
					: (position - 1 >= 0) ? position - 1 : currin().length - 1]
							.equals((operationType instanceof GolfHandler) ? "[" : "begin recursive area")) {
				rmtop();
				while (!currin()[position].trim()
						.equals((operationType instanceof GolfHandler) ? "]" : "end recursive area"))
					loopmove();
			} else {
				rmtop();
				loopmove();
			}
			break;

		case "skip":
			loopmove();
			break;

		case "loop":
			loopHandler();
			break;

		case "sine":
			settop(new BigDecimal(Math.sin(top().doubleValue())));
			break;

		case "asine":
			settop(new BigDecimal(Math.asin(top().doubleValue())));
			break;

		case "cosine":
			settop(new BigDecimal(Math.cos(top().doubleValue())));
			break;

		case "acosine":
			settop(new BigDecimal(Math.acos(top().doubleValue())));
			break;

		case "tangent":
			settop(new BigDecimal(Math.tan(top().doubleValue())));
			break;

		case "atangent":
			settop(new BigDecimal(Math.atan(top().doubleValue())));
			break;

		case "repnextchar":
			int repeats = top().intValue();
			rmtop();
			forLoopHandler(repeats);
			break;

		case "length":
			push(new BigDecimal(stac.get(currstac).size()));
			break;

		case "pi":
			push(new BigDecimal(Math.PI));
			break;

		case "E":
			push(new BigDecimal(Math.E));
			break;

		case "log":
			setind(2, new BigDecimal(Math.log(index(2).doubleValue()) / Math.log(top().doubleValue())));
			rmtop();
			break;

		case "outnum":
			System.out.print(top());
			rmtop();
			break;

		case "rand":
			settop(top().multiply(new BigDecimal(Math.random())));
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
				push(new BigDecimal((int) input.get(input.size() - 1).toCharArray()[0]));
				input.remove(input.size() - 1);
			}
			break;

		case "prompt":
			String prompt = "";
			prompt = in.nextLine();
			try {
				push(new BigDecimal(Double.parseDouble(prompt)));
			} catch (Exception e) {
				for (int i = prompt.length(); i > 0;) {
					push(new BigDecimal((int) ((char) prompt.substring(i - 1, i--).toCharArray()[0])));
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
			ArrayList<BigDecimal> temp1 = new ArrayList(0);
			for (int k = 0; k < stac.get(currstac).size(); k++) {
				temp1.add(stac.get(currstac).get((k + 1) % stac.get(currstac).size()));
			}
			stac.remove(currstac);
			stac.add(temp1);
			break;

		case "rotateleft":
			ArrayList<BigDecimal> temp2 = new ArrayList(0);
			for (int k = 0; k < stac.get(currstac).size(); k++) {
				temp2.add(stac.get(currstac).get((k - 1 < 0) ? stac.get(currstac).size() - 1 : k - 1));
			}
			stac.remove(currstac);
			stac.add(temp2);
			break;

		case "singswitch":
			multiswitch(2);
			break;

		case "multiswitch":
			int temp4 = top().toBigInteger().mod(new BigDecimal(stac.get(currstac).size()).toBigInteger()).intValue();
			rmtop();
			multiswitch(temp4);
			break;

		case "duplicate":
			push(top());
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
			push(new BigDecimal(users.get(users.size() - 1).length));
			break;

		case "classname":
			int usereference = top().intValue();
			rmtop();
			char[] classname = ((usereference >= 0) ? users.get(users.size() - 1)[usereference]
					: (usereference == -1) ? currclassname.get(currclassname.size() - 1)
							: extender.get(extender.size() - 1)).toCharArray();
			for (int i = classname.length - 1; i != -1; i--) {
				push(new BigDecimal((int) classname[i]));
			}
			break;

		case "objects":
			objects.add((BigDecimal[]) stac.get(currstac).toArray());
			move();
			int k = objectrefsold.indexOf(currin()[position].trim());
			if (k != -1) {
				objectrefs.remove(k);
			}
			objectrefs.add(currin()[position].trim());
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
			push(new BigDecimal(stac.size()));
			break;

		case "add":
			setind(2, top().add(index(2)));
			rmtop();
			break;

		case "subtract":
			setind(2, index(2).subtract(top()));
			rmtop();
			break;

		case "multiply":
			setind(2, top().multiply(index(2)));
			rmtop();
			break;

		case "divide":
			try {
				setind(2, index(2).divide(top()));
			} catch (ArithmeticException e) {
				setind(2, index(2).divide(top(), 20, BigDecimal.ROUND_HALF_DOWN));
			}
			rmtop();
			break;

		case "equal":
			setind(2, new BigDecimal((index(2).compareTo(top()) == 0) ? 1 : 0));
			rmtop();
			break;

		case "modulo":
			setind(2, index(2).remainder(top()));
			rmtop();
			break;
			
		case "int":
			settop(new BigDecimal(top().toBigInteger()));
			break;

		case "power":
			setind(2, new BigDecimal(Math.pow(index(2).doubleValue(), top().doubleValue())));
			rmtop();
			break;

		case "factorial":
			BigDecimal output = new BigDecimal(1);
			BigDecimal y = top();
			for (BigDecimal i = new BigDecimal(1); i.compareTo(y) != 1; i = i.add(new BigDecimal(1))) {
				output = i.multiply(output);
			}
			settop(output);
			break;

		case "quote":
			String quotetype = currin()[position].trim();
			while (true) {
				loopmove();
				if (currin()[position].trim().equals(quotetype) || currin()[position].trim().equals(quotetype))
					break;
				push(new BigDecimal((double) ((String) ((operationType instanceof VerboseHandler)
						? Converter.toGolfed(currin, position, currin()[position]) : currin()[position]))
								.toCharArray()[0]));
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
				push(new BigDecimal(g));
			push(new BigDecimal(g));
			break;

		case "factorize":
			int f = top().intValue();
			rmtop();
			if (f < 0)
				push(new BigDecimal(-1));
			f = Math.abs(f);
			for (int i = 2; i <= f; i++) {
				while (f % i == 0) {
					push(new BigDecimal(i));
					f /= i;
				}
			}
			break;

		case "prime":
			Double n = top().doubleValue();
			rmtop();
			if (n < 2) {
				push(new BigDecimal(0));
				break;
			} else if (n == 2 || n == 3) {
				push(new BigDecimal(1));
				break;
			} else if (n % 2 == 0 || n % 3 == 0) {
				push(new BigDecimal(0));
				break;
			}
			long sqrtN = (long) Math.sqrt(n) + 1;
			for (long i = 6L; i <= sqrtN; i += 6) {
				if (n % (i - 1) == 0 || n % (i + 1) == 0) {
					push(new BigDecimal(0));
					break switchout;
				}
			}
			push(new BigDecimal(1));
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
				stac.add((ArrayList<BigDecimal>) (Arrays.asList(objects.get(i))));
				changeStack(true);
				objects.remove(i);
				objectrefsold.add(objectrefs.get(i));
				objectrefs.remove(i);
			} else if ((i = objectrefsold.indexOf(currin()[position])) != -1) {
				objects.add((BigDecimal[]) stac.get(currstac).toArray());
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

	public int getCurrin() {
		return currstac;
	}

	private BigDecimal top() {
		return index(1);
	}

	private void changeStack(boolean chngDir) {
		if (stac.size() > 0)
			currstac = (chngDir) ? (currstac + 1) % stac.size() : (currstac - 1 >= 0) ? currstac - 1 : stac.size() - 1;
	}

	private BigDecimal index(int i) {
		return stac.get(currstac).get(stac.get(currstac).size() - i);
	}

	private void settop(BigDecimal x) {
		setind(1, x);
	}

	private void setind(int i, BigDecimal x) {
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

	private void push(BigDecimal x) {
		stac.get(currstac).add(x);
	}

	private void multiswitch(int amount) {
		BigDecimal temp = index(amount);
		for (int i = amount; i > 1;) {
			setind(i, index(--i));
		}
		settop(temp);
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
		for (ArrayList<BigDecimal> i : stac) {
			info += "\n";
			for (BigDecimal k : i) {
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