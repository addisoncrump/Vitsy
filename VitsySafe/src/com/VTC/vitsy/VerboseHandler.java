package com.VTC.vitsy;

public class VerboseHandler implements Operable {
	GolfHandler handler = new GolfHandler();

	public String doOperation(int currin, int position, String operator) throws UnrecognizedInstructionException {
		return handler.doOperation(currin, position, Converter.toGolfed(currin, position, operator));
	}
}
