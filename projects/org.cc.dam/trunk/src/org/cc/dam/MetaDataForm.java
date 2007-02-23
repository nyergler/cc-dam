package org.cc.dam;

import org.cc.dam.metadata.*;
import java.util.*;

public class MetaDataForm
{
	private Hashtable table;
	private Hashtable mdLookup;
	private Vector tags;
	
	public MetaDataForm(MetaDataList dataLists[])
	{
		table    = new Hashtable();
		mdLookup = new Hashtable();
		tags     = new Vector();
		//Add all the keys to the hash table
		//and store all tags in the hashtable
		for(int list = 0; list < dataLists.length; list++)
		{
			String tagList[] = dataLists[list].getTags();
			for(int pos = 0; pos < tagList.length; pos++)
			{
				table.put(tagList[pos], "");
				tags.add(tagList[pos]);
				mdLookup.put(tagList[pos], dataLists[list]);
			}
		}
	}
	
	public String[] getTags()
	{
		return (String[]) tags.toArray();
	}
	
	public String getValue(String tag)
	{
		//Check to see if the key is valid
		if(!table.containsKey(tag))
		{
			throw new IllegalArgumentException("Tag: " + tag + 
					" is not recognized in this MetaDataForm.");
		}
		//Return value
		return (String) table.get(tag);
	}
	
	public void setValue(String tag, String value)
	{
		//Check to see if the key is valid
		if(tag == null || !table.containsKey(tag))
		{
			throw new IllegalArgumentException("Tag: " + tag + 
					" is not recognized in this MetaDataForm.");
		}
		//If the value isn't valid
		else if(value == null || 
				!((MetaDataList) mdLookup.get(tag)).isValid(tag, value))
		{
			throw new IllegalArgumentException("Value: " + value +
					" is not a valid value for Tag: " + tag + ".");
		}
		//Put the value in the table
		table.put(tag, value);
	}
}
