package org.cc.dam.filetype;

public class FiletypeNotSupportedException extends Exception {
	public FiletypeNotSupportedException() {
		super("Filetype not yet supported.");
	}
	public FiletypeNotSupportedException(String msg) {
		super(msg);
	}
}
