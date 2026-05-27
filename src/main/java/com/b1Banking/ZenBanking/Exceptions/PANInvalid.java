package com.b1Banking.ZenBanking.Exceptions;

public class PANInvalid extends RuntimeException{

	private static final long serialVersionUID = 1L;

public PANInvalid (String pan) {
	super(pan);
}
}
