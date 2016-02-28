package com.VTC.vitsy;

public class Converter {
	public static String toVerbose(int currin, int position, String operator) throws UnrecognizedInstructionException {
		switch (operator) {
		case "1":
			return "push 1";
		case "2":
			return "push 2";
		case "3":
			return "push 3";
		case "4":
			return "push 4";
		case "5":
			return "push 5";
		case "6":
			return "push 6";
		case "7":
			return "push 7";
		case "8":
			return "push 8";
		case "9":
			return "push 9";
		case "a":
			return "push 10";
		case "b":
			return "push 11";
		case "c":
			return "push 12";
		case "d":
			return "push 13";
		case "e":
			return "push 14";
		case "f":
			return "push 15";
		case "0":
			return "push 0";
		case "i":
			return "push input item";
		case "I":
			return "push input length";
		case "w":
			return "wait top seconds";
		case ")":
			return "if (int) top is not 0";
		case "(":
			return "if (int) top is 0";
		case "[":
			return "begin recursive area";
		case "S":
			return "push sine of top";
		case "s":
			return "push inverse sine of top";
		case "C":
			return "push cosine of top";
		case "A":
			return "push inverse cosine of top";
		case "T":
			return "push tangent of top";
		case "t":
			return "push inverse tangent of top";
		case "\\":
			return "repeat next instruction set top times";
		case "l":
			return "push length of stack";
		case "P":
			return "push pi";
		case "E":
			return "push e";
		case "L":
			return "push log_top(second to top)";
		case "N":
			return "output top as number";
		case "O":
			return "output top as character";
		case "R":
			return "push random decimal in top to second to top";
		case ";":
			return "generic exit";
		case "Z":
			return "output stack as chars";
		case "z":
			return "push all input";
		case "W":
			return "STDIN";
		case "#":
			return "teleport to top instruction";
		case "r":
			return "reverse stack";
		case "$":
			return "switch the top two items";
		case "%":
			return "pop n, rotate the top n items";
		case "}":
			return "rotate stack right";
		case "{":
			return "rotate stack left";
		case "D":
			return "duplicate top item";
		case "@":
			return "get top specified item";
		case "?":
			return "rotate right a stack";
		case "|":
			return "rotate left a stack";
		case ":":
			return "clone current stack";
		case "&":
			return "make new stack";
		case "Y":
			return "remove current stack";
		case "y":
			return "push number of stacks";
		case "u":
			return "flatten top two stacks";
		case "v":
			return "save top as temporary variable";
		case "V":
			return "save top as permanent variable";
		case "X":
			return "remove top";
		case "+":
			return "add top two";
		case "-":
			return "subtract top two";
		case "*":
			return "multiply top two";
		case "/":
			return "divide top two";
		case "=":
			return "push whether top two are equal";
		case "M":
			return "modulo top two";
		case "_":
			return "replace top with int(top)";
		case "h":
			return "factorize top item";
		case "H":
			return "push all ints between second to top and top";
		case "^":
			return "push second to top to the power of top";
		case "F":
			return "push top!";
		case "p":
			return "push whether (int) top item is prime";
		case "m":
			return "goto top method";
		case "o":
			return "capture stack as object with next";
		/**
		 * case "k": return
		 * "goto top instruction set of second to top index class"; case "K":
		 * return "execute top instruction set of superclass"; case "g": return
		 * "push number of use declarations"; case "G": return
		 * "push top index of classname"; case "`": return
		 * "read file with name (stack)"; case ".": return
		 * "write to file with name (stack)"; case ",": return
		 * "execute stack as shell";
		 */
		case "n":
			return "eval(stack)";
		case "x":
			return "exit now with top status";
		case "<":
			return "go backward";
		case ">":
			return "go forward";
		case "]":
			return "end recursive area";
		case "\'":
			return "toggle single quote";
		case "\"":
			return "toggle double quote";
		}
		if (operator.length() > 1)
			throw new UnrecognizedInstructionException(
					"Could not recognize instruction: " + operator + " (command #" + currin + "," + position + ")");
		return operator;
	}

	public static String toGolfed(int currin, int position, String operator) throws UnrecognizedInstructionException {
		switch (operator) {
		case "push 1":
			return "1";
		case "push 2":
			return "2";
		case "push 3":
			return "3";
		case "push 4":
			return "4";
		case "push 5":
			return "5";
		case "push 6":
			return "6";
		case "push 7":
			return "7";
		case "push 8":
			return "8";
		case "push 9":
			return "9";
		case "push 10":
			return "a";
		case "push 11":
			return "b";
		case "push 12":
			return "c";
		case "push 13":
			return "d";
		case "push 14":
			return "e";
		case "push 15":
			return "f";
		case "push 0":
			return "0";
		case "push input item":
			return "i";
		case "push input length":
			return "I";
		case "wait top seconds":
			return "w";
		case "if (int) top is not 0":
			return ")";
		case "if (int) top is 0":
			return "(";
		case "begin recursive area":
			return "[";
		case "push sine of top":
			return "S";
		case "push inverse sine of top":
			return "s";
		case "push cosine of top":
			return "C";
		case "push inverse cosine of top":
			return "A";
		case "push tangent of top":
			return "T";
		case "push inverse tangent of top":
			return "t";
		case "repeat next instruction set top times":
			return "\\";
		case "push length of stack":
			return "l";
		case "push pi":
			return "P";
		case "push e":
			return "E";
		case "push log_top(second to top)":
			return "L";
		case "output top as number":
			return "N";
		case "output top as character":
			return "O";
		case "push random decimal in top to second to top":
			return "R";
		case "generic exit":
			return ";";
		case "output stack as chars":
			return "Z";
		case "push all input":
			return "z";
		case "STDIN":
			return "W";
		case "teleport to top instruction":
			return "#";
		case "reverse stack":
			return "r";
		case "switch the top two items":
			return "$";
		case "pop n, rotate the top n items":
			return "%";
		case "rotate stack right":
			return "}";
		case "rotate stack left":
			return "{";
		case "duplicate top item":
			return "D";
		case "get top specified item":
			return "@";
		case "rotate right a stack":
			return "?";
		case "rotate left a stack":
			return "|";
		case "clone current stack":
			return ":";
		case "make new stack":
			return "&";
		case "remove current stack":
			return "Y";
		case "push number of stacks":
			return "y";
		case "flatten top two stacks":
			return "u";
		case "save top as temporary variable":
			return "v";
		case "save top as permanent variable":
			return "V";
		case "remove top":
			return "X";
		case "add top two":
			return "+";
		case "subtract top two":
			return "-";
		case "multiply top two":
			return "*";
		case "divide top two":
			return "/";
		case "push whether top two are equal":
			return "=";
		case "modulo top two":
			return "M";
		case "replace top with int(top)":
			return "_";
		case "factorize top item":
			return "h";
		case "push all ints between second to top and top":
			return "H";
		case "push second to top to the power of top":
			return "^";
		case "push top!":
			return "F";
		case "push whether (int) top item is prime":
			return "p";
		case "goto top method":
			return "m";
		case "capture stack as object with next":
			return "o";
		/**
		 * case "goto top instruction set of second to top index class": return
		 * "k"; case "execute top instruction set of superclass": return "K";
		 * case "push number of use declarations": return "g"; case
		 * "push top index of classname": return "G"; case
		 * "read file with name (stack)": return "`"; case
		 * "write to file with name (stack)": return "."; case
		 * "execute stack as shell": return ",";
		 */
		case "eval(stack)":
			return "n";
		case "exit now with top status":
			return "x";
		case "go backward":
			return "<";
		case "go forward":
			return ">";
		case "end recursive area":
			return "]";
		case "toggle single quote":
			return "\'";
		case "toggle double quote":
			return "\"";
		}
		if (operator.length() > 1) {
			throw new UnrecognizedInstructionException(
					"Warning: unrecognized operator: " + operator + " (command #" + currin + "," + position + ")");
		}
		return operator;
	}
}