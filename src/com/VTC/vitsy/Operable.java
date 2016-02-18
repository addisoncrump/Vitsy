package com.VTC.vitsy;

public interface Operable {
	String doOperation(int currin, int position, String operator) throws UnrecognizedInstructionException;
}
