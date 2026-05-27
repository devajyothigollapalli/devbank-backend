package com.b1Banking.ZenBanking.Exceptions;

public class UnavailableBalance extends RuntimeException {

	
	private static final long serialVersionUID = 1L;
public UnavailableBalance(String ub) {
	super(ub);
}
}
