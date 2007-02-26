package org.cc.dam.filetype;

import java.util.HashMap;

import org.cc.dam.metadata.MetaDataList;
import org.cc.dam.metadata.MetaDataListCC;
import org.cc.dam.metadata.MetaDataListDC;
import org.cc.dam.metadata.MetaDataListxapRights;

import org.cc.xmp.PDFExtractor;
import org.cc.xmp.XMLParser;

public class PDF extends Generic {
	public static MetaDataList[] SUPPORTED_METADATA = { new MetaDataListCC(),
		                                                new MetaDataListDC(),
		                                                new MetaDataListxapRights() };
	public PDF(String filename) {
		super(filename);
		System.err.println("#");
	}
	public HashMap getMetadata() {
		try {
			String[] supportedTags;
			int numberOfTags = 0;
			System.err.println("!");
			for (int i = 0; i < PDF.SUPPORTED_METADATA.length; i += 1) {
				numberOfTags += PDF.SUPPORTED_METADATA[i].getTags().length;
			}
			System.err.println(numberOfTags);
			supportedTags = new String[numberOfTags];
			int index = 0;
			for (int i = 0; i < PDF.SUPPORTED_METADATA.length; i += 1) {
				String[] tags = PDF.SUPPORTED_METADATA[i].getTags();
				for (int j = 0; j < tags.length; j += 1) {
					supportedTags[index++] = tags[j]; 
				}
			}
			XMLParser xmlp = new XMLParser(new PDFExtractor(this.filename), supportedTags);
			return xmlp.getMetadata();
		}
		catch (Exception e) {
			System.err.println("Got an exception!");
			System.err.println(e.toString());
			return new HashMap();
		}
	}
}