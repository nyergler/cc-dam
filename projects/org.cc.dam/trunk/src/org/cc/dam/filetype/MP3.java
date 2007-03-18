package org.cc.dam.filetype;

import java.util.HashMap;

import java.io.IOException;

import org.cc.dam.metadata.MetaDataList;
import org.cc.dam.metadata.MetaDataListCC;
import org.cc.dam.metadata.MetaDataListDC;
import org.cc.dam.metadata.MetaDataListxapRights;

import org.farng.mp3.MP3File;
import org.farng.mp3.id3.AbstractID3;
import org.farng.mp3.TagException;

public class MP3 extends Generic {
	public static MetaDataList[] SUPPORTED_METADATA = { new MetaDataListCC(),
                                                        new MetaDataListDC(),
                                                        new MetaDataListxapRights() };
	public MP3(String filename) {
		super(filename);
	}

	public HashMap getMetadata() {
		MP3File file = null;
		try {
			file = new MP3File(this.filename);
		}
		catch (TagException e) {
			return null;
		}
		catch (IOException e) {
			return null;
		}
		AbstractID3 tag = file.getID3v2Tag();
		HashMap<String, String> result = new HashMap<String, String>();
		if (tag == null) {
			tag = file.getID3v1Tag();
			if (tag == null) return result;
		}
		try { result.put("dc:title", tag.getSongTitle()); }
		catch (UnsupportedOperationException e) { }
		try { result.put("dc:creator", tag.getLeadArtist()); }
		catch (UnsupportedOperationException e) { }
		try { result.put("dc:date", tag.getYearReleased()); }
		catch (UnsupportedOperationException e) { }
		return result;
	}

}
