package org.cc.dam.metadata;

import java.util.Vector;
//trying to change name
public abstract class MetaDataList {
	protected Vector tags;
	
	public MetaDataList()
	{
		tags = new Vector();
	}
	
	public String[] getTags()
	{
		return (String[]) tags.toArray();
	}
	
	public abstract boolean isValid(String tag, String value);
	
	public abstract boolean containsTag(String tag);
}
