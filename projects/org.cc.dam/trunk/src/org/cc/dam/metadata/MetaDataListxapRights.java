package org.cc.dam.metadata;

public class MetaDataListxapRights extends MetaDataList
{
	public MetaDataListxapRights()
	{
		tags.add("xapRights:Copyright");
		tags.add("xapRights:Marked");
		tags.add("xapRights:WebStatement");
	}

	public boolean containsTag(String tag) {
		for(int i = 0; i < tags.size(); i++)
			if(((String) tags.get(i)).equals(tag)) return true;
		return false;
	}

	public boolean isValid(String tag, String value) {
		if(containsTag(tag)) return true;
		else return false;
	}
}
