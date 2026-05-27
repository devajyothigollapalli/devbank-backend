package com.b1Banking.ZenBanking.Exceptions;

public class InvalidOTP extends RuntimeException {

	private static final long serialVersionUID = 1L;
	public InvalidOTP(String io) {
		super (io);
	}

}
