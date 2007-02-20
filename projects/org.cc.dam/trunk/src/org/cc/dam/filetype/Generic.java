package org.cc.dam.filetype;

import java.util.HashMap;

public abstract class Generic {
	protected String filename;
	public Generic(String filename) {
		this.filename = filename;
	}
	public abstract HashMap getMetadata();
}