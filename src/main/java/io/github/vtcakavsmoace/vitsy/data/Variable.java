package io.github.vtcakavsmoace.vitsy.data;

import java.math.BigInteger;

public class Variable {

	private final boolean isFinal;
	private BigInteger value;
	
	public Variable(boolean isFinal) {
		this.isFinal = isFinal;
	}
	
	public BigInteger interact(BigInteger in) {
		if (value == null) {
			value = in;
			return null;
		} else {
			if (isFinal) {
				return value;
			} else {
				BigInteger prev = value;
				value = null;
				return prev;
			}
		}
	}
	
}
