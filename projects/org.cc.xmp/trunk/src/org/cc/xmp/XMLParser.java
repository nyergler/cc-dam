package org.cc.xmp;

import java.io.ByteArrayInputStream;
import java.util.HashMap;
import java.util.Stack;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class XMLParser {
    HashMap metadata = null;
    Extractor extractor = null;
    String[] supportedTags;
    public XMLParser(Extractor extractor, String[] supportedTags) {
        metadata = new HashMap();
        this.extractor = extractor;
        this.supportedTags = supportedTags;
    }
    public HashMap getMetadata() throws Exception {
        this.parse();
        return this.metadata;
    }
    private void parse() throws Exception {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        SAXParser saxParser = null;
        saxParser = factory.newSAXParser();
        DefaultHandler handler = new MySAXApp(this.metadata, this.supportedTags);

        // String -> byte[] -> ByteArrayInputStream -> InputStream
        // and then we wonder why Java is inefficient

        saxParser.parse(new ByteArrayInputStream(extractor.getXML().getBytes()), handler);
    }
}



class MySAXApp extends DefaultHandler {
    private boolean i_care_about_this_tag = false;
    private StringBuffer buffer;
    private HashMap<String, String> metadata;
    private Stack<String> tagStack = null;
    private String[] supportedTags;

    public MySAXApp(HashMap<String, String> where, String[] supportedTags) {
        super();
        this.metadata = where;
        this.tagStack = new Stack<String>();
        this.supportedTags = supportedTags;
        System.err.println(supportedTags);
    }
    public void startDocument() {
        System.err.println("doc starts!");
    }
    public void endDocument() {
        System.err.println("doc ends!");
        System.err.println(this.metadata);
    }

    public void startElement(String namespaceURI,
                             String sName, // simple name
                             String qName, // qualified name
                             Attributes attrs)
        throws SAXException {
        String element = sName.equals("") ? qName : sName;
        System.err.println(">> " + element);
        if (i_care_about_this_tag == false) buffer = null;
        for (int i = 0; i < this.supportedTags.length; i += 1) {
        	if (element.equals(this.supportedTags[i])) {
        		i_care_about_this_tag = true;
        		i = this.supportedTags.length;
        	}
        }
        if (i_care_about_this_tag || this.tagStack.size() > 0) {
            this.tagStack.push(element);
            System.out.println(this.tagStack);
        }

        /*
          String eName = sName; // element name
        if ("".equals(eName)) eName = qName; // not namespace-aware
        emit("<"+eName);
        if (attrs != null) {
            for (int i = 0; i < attrs.getLength(); i++) {
                String aName = attrs.getLocalName(i); // Attr name
                if ("".equals(aName)) aName = attrs.getQName(i);
                emit(" ");
                emit(aName+"=\""+attrs.getValue(i)+"\"");
            }
        }
        emit(">");
        */
    }

    public void endElement(String namespaceURI,
                           String sName, // simple name
                           String qName) // qualified name
        throws SAXException {
        // if we're at the bottom of the stack of tags
        // that we care about, then (and only then) we
        // can take this buffer and put it to use
        if (this.tagStack.size() == 1) {
            System.err.println(buffer);
            this.metadata.put(qName, buffer.toString().trim());
            this.tagStack.pop();
            buffer = null;
        }
        else if (this.tagStack.size() > 0) {
            this.tagStack.pop();
        }
        System.err.println("<< " + qName);
        /*
        String eName = sName; // element name
        if ("".equals(eName)) eName = qName; // not namespace-aware
        emit("</"+eName+">");
        */
    }

    public void characters(char buf[], int offset, int len)
        throws SAXException
    {
        String s = new String(buf, offset, len);
        if (buffer == null) {
            buffer = new StringBuffer(s);
        } else {
            buffer.append(s);
        }
    }
}
