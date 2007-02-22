package org.cc.dam.tags;

import java.util.Vector;

public abstract class TagList
{
	private Vector tags;
	
	public String[] getTags()
	{
		return (String[]) tags.toArray();
	}
	
	public abstract boolean isValid(String tag, String value);
}
