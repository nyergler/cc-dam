package org.cc;

public class StringExtractor implements Extractor {
    String xml = null;
    public StringExtractor(String data) {
        this.xml = data;
    }
    public String getXML() {
        return this.xml;
    }
}