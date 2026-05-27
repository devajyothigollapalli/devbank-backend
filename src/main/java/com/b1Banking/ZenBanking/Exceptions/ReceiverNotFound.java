package com.b1Banking.ZenBanking.Exceptions;

public class ReceiverNotFound extends RuntimeException {

	
	private static final long serialVersionUID = 1L;
public ReceiverNotFound(String rnf) {
	super(rnf);
}
}
