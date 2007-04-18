package org.cc.dam;

//Needed Packages
import java.util.Vector;
import java.util.LinkedList;
import org.cc.dam.metadata.*;

public class MetaDataLoader
{
	Vector<MetaDataList> mdList = new Vector<MetaDataList>();
	String allTags[];
	LinkedList allTagMetaDatas[];
	
	/**
	 * Constructor.
	 * Load all the MetaData Classes in.
	 */
	public MetaDataLoader()
	{
		mdList.add(new MetaDataListCC());
		mdList.add(new MetaDataListDC());
		mdList.add(new MetaDataListxapRights());
	}
	
	/**
	 * Get all the tags in all name spaces
	 * @return All tags.
	 */
	public String[] getAllTags()
	{
		int num = 0;
		//Find the number of tags
		for(int i = 0; i < mdList.size(); i++)
			num += mdList.get(i).numTags();
		
		String[] tags = new String[num];
		int counter = 0;
		//Populate the list of tags
		for(int i = 0; i < mdList.size(); i++)
		{
			String tag[] = mdList.get(i).getTags();
			for(int j = 0; j < tag.length; j++)
			{
				tags[counter] = tag[j];
				counter++;
			}
		}
		
		return tags;
	}
}
