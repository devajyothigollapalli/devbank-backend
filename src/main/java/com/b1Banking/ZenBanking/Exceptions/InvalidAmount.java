package com.b1Banking.ZenBanking.Exceptions;

public class InvalidAmount extends RuntimeException {

	private static final long serialVersionUID = 1L;
	public InvalidAmount(String amount) {
		super(amount);
	}

}
