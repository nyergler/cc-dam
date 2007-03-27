package org.cc.dam.metadata;

public class MetaDataListDC extends MetaDataList
{	
	/**
	 * Constructor.
	 *
	 */
	public MetaDataListDC()
	{
		tags.add("dc:title");
		tags.add("dc:creator");
		tags.add("dc:subject");
		tags.add("dc:description");
		tags.add("dc:publisher");
		tags.add("dc:contributor");
		tags.add("dc:date");
		tags.add("dc:type");
		tags.add("dc:format");
		tags.add("dc:identifier");
		tags.add("dc:source");
		tags.add("dc:language");
		tags.add("dc:relation");
		tags.add("dc:coverage");
		tags.add("dc:rights");
	}
	
	/**
	 * See if a tag is contained in this metadata list.
	 */
	public boolean containsTag(String tag) {
		for(int i = 0; i < tags.size(); i++)
			if(tags.get(i).equals(tag)) return true;
		return false;
	}

	/**
	 * Validate a tag to a given format.
	 */
	public boolean isValid(String tag, String value) {
		if(containsTag(tag)) return true;
		else return false;
	}
}
