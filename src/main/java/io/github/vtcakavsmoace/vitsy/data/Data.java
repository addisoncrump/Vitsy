package io.github.vtcakavsmoace.vitsy.data;

import java.math.BigInteger;

public class Data {

	private final Stack<Stack<BigInteger>> stackstack = new Stack<Stack<BigInteger>>();
	private final Variable finVar = new Variable(true);
	private final Variable tempVar = new Variable(false);
	
	public Stack<Stack<BigInteger>> getStackstack() {
		return stackstack;
	}
	public Variable getFinVar() {
		return finVar;
	}
	public Variable getTempVar() {
		return tempVar;
	}
	
}
