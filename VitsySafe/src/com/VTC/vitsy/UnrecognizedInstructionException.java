package com.VTC.vitsy;

public class UnrecognizedInstructionException extends Exception {
	private static final long serialVersionUID = -3899359504638816111L;

	public UnrecognizedInstructionException() {
		super();
	}

	public UnrecognizedInstructionException(String message) {
		super(message);
	}
}
