package org.cc.dam.metadata;

import java.util.Vector;
//trying to change name
public abstract class MetaDataList {
	protected Vector<String> tags;
	
	/**
	 * Constructor.
	 *
	 */
	public MetaDataList()
	{
		tags = new Vector<String>();
	}
	
	/**
	 * Number of tags in the MetaData List.
	 * @return num tags.
	 */
	public int numTags()
	{
		return tags.size();
	}
	
	/**
	 * Get the tags in the metadata list.
	 */
	public String[] getTags()
	{
		return (String[]) tags.toArray(new String[tags.size()]);
	}
	
	/**
	 * Validate a tag to a given format.
	 */
	public abstract boolean isValid(String tag, String value);
	
	/**
	 * See if a tag is contained in this metadata list.
	 */	
	public abstract boolean containsTag(String tag);
}
