package org.cc.dam.metadata;

public class MetaDataListxapRights extends MetaDataList
{
	/**
	 * Constructor.
	 *
	 */
	public MetaDataListxapRights()
	{
		namespace = "xapRights:";
		hrNamespace = "XAP Rights";
		tags.add("Copyright");
		tags.add("Marked");
		tags.add("WebStatement");
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
