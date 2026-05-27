package com.b1Banking.ZenBanking.Exceptions;

public class InvalidDOB extends RuntimeException{

	
	private static final long serialVersionUID = 1L;
	
public InvalidDOB(String ild) {
	super(ild);
}
}
