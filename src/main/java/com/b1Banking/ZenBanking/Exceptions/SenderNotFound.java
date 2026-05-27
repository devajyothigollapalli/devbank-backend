package com.b1Banking.ZenBanking.Exceptions;

public class SenderNotFound extends RuntimeException{

	private static final long serialVersionUID = 1L;
public SenderNotFound(String snf) {
	super(snf);
}
}
