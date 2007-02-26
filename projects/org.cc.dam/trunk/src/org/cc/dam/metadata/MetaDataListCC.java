package org.cc.dam.metadata;

public class MetaDataListCC extends MetaDataList
{
	public MetaDataListCC()
	{
		tags.add("cc:license");
	}

	public boolean containsTag(String tag) {
		for(int i = 0; i < tags.size(); i++)
			if(tags.get(i).equals(tag)) return true;
		return false;
	}

	public boolean isValid(String tag, String value) {
		if(containsTag(tag)) return true;
		else return false;
	}
}
