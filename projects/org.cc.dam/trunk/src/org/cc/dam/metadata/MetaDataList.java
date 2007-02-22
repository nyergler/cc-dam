package org.cc.dam.metadata;

import java.util.Vector;

public abstract class MetaDataList {
	private Vector tags;
	
	public String[] getTags()
	{
		return (String[]) tags.toArray();
	}
	
	public abstract boolean isValid(String tag, String value);
}
