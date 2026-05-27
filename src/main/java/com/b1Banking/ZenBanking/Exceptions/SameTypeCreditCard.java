package com.b1Banking.ZenBanking.Exceptions;

public class SameTypeCreditCard extends RuntimeException{

	private static final long serialVersionUID = 1L;

public SameTypeCreditCard(String st) {
	super(st);
}
}
