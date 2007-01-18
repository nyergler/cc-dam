package org.cc.test;

import org.cc.PDFExtractor;
import org.cc.StringExtractor;
import org.cc.XMLExtractor;
import org.cc.XMLParser;

import java.util.HashMap;
import java.util.Iterator;

import junit.framework.TestCase;

public class XMLParserTest extends TestCase {
    public void testStringExtractor() {
        StringExtractor se = new StringExtractor("foo");
        assertTrue(se.getXML().equals("foo"));
    }
    public void testHashMap() {
        try {
            PDFExtractor pe = new PDFExtractor("/usr/home/taylor/tmp/cctools/foo.pdf");
            XMLParser xp = new XMLParser(pe);
            HashMap metadata = xp.getMetadata();
            Iterator i = metadata.keySet().iterator();
            while (i.hasNext()) {
                String s = i.next().toString();
                System.err.println(s + " => " + (String)metadata.get(s));
            }
            System.err.println(metadata.size());
            assertTrue(metadata.size() == 4);
        }
        catch (Exception e) { System.err.println(e.toString()); }
    }
}
