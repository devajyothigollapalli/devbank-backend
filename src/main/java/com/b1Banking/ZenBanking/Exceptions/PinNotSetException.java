package com.b1Banking.ZenBanking.Exceptions;

public class PinNotSetException extends RuntimeException{

	private static final long serialVersionUID = 1L;
	
	public PinNotSetException (String pnse) {
		super(pnse);
	}

}
