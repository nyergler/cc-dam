package org.cc.dam.metadata;

import java.util.Vector;
//trying to change name
public abstract class MetaDataList {
	protected Vector<String> tags;
	
	public MetaDataList()
	{
		tags = new Vector<String>();
	}
	
	public String[] getTags()
	{
		return (String[]) tags.toArray(new String[tags.size()]);
	}
	
	public abstract boolean isValid(String tag, String value);
	
	public abstract boolean containsTag(String tag);
}
