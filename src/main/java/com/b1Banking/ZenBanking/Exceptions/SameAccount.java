package com.b1Banking.ZenBanking.Exceptions;

public class SameAccount extends RuntimeException {
	private static final long serialVersionUID = 1L;
public SameAccount(String sa) {
	super(sa);
}
}
