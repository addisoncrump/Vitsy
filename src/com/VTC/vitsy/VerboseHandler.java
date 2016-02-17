package com.VTC.vitsy;

public class VerboseHandler implements Operable {
	GolfHandler handler = new GolfHandler();

	public String doOperation(String operator) throws UnrecognizedInstructionException {
		return handler.doOperation(Converter.toGolfed(operator));
	}
}
