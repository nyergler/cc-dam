package org.cc.dam.filetype;

import java.util.HashMap;

import org.cc.xmp.PDFExtractor;
import org.cc.xmp.XMLParser;

public class PDF extends Generic {
	public PDF(String filename) {
		super(filename);
	}
	public HashMap getMetadata() {
		try {
			XMLParser xmlp = new XMLParser(new PDFExtractor(this.filename));
			return xmlp.getMetadata();
		}
		catch (Exception e) {
			System.err.println(e.toString());
			return new HashMap();
		}
	}
}