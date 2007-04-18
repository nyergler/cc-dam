package org.cc.dam.metadata;

public class MetaDataListDC extends MetaDataList
{	
	/**
	 * Constructor.
	 *
	 */
	public MetaDataListDC()
	{
		namespace = "dc:";
		hrNamespace = "Dublin Core";
		tags.add("title");
		tags.add("creator");
		tags.add("subject");
		tags.add("description");
		tags.add("publisher");
		tags.add("contributor");
		tags.add("date");
		tags.add("type");
		tags.add("format");
		tags.add("identifier");
		tags.add("source");
		tags.add("language");
		tags.add("relation");
		tags.add("coverage");
		tags.add("rights");
	}
	
	/**
	 * See if a tag is contained in this metadata list.
	 */
	public boolean containsTag(String tag) {
		for(int i = 0; i < tags.size(); i++)
			if(tags.get(i).equals(tag) ||
	   		   (namespace + tags.get(i)).equals(tag)) return true;
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
