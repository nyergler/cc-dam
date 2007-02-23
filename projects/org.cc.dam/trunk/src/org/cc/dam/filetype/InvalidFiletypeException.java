package org.cc.dam.filetype;

public class InvalidFiletypeException extends Exception {
	public InvalidFiletypeException() {
		super("Invalid filetype.");
	}
	
	public InvalidFiletypeException(String msg) {
		super(msg);
	}
}
