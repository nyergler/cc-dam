package org.cc.xmp;

import java.io.*;

public class XMLExtractor implements Extractor {
    BufferedInputStream stream = null;
    public XMLExtractor(String filename) {
        try {
            this.stream = new BufferedInputStream(new FileInputStream(filename));
        }
        catch (Exception e) { }
    }
    public String getXML() {
        String fileAsString = "";
        int input = 0;
        try {
            while ((input = this.stream.read()) != -1) {
                fileAsString += (char)input;
            }
        }
        catch (Exception e) { }
        System.err.println(fileAsString);
        return fileAsString;
    }
}