package com.b1Banking.ZenBanking.Exceptions;

public class AccountNotFound extends RuntimeException{

	private static final long serialVersionUID = 1L;
public AccountNotFound(String an) {
	super(an);
}
}
