package com.b1Banking.ZenBanking.Exceptions;

public class InactiveAccount extends RuntimeException {

	private static final long serialVersionUID = 1L;

public InactiveAccount(String ia) {
	super(ia);
}
}
