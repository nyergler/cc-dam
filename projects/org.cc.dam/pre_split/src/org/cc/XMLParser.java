package org.cc;

import java.io.File;
import java.io.ByteArrayInputStream;
import java.util.HashMap;
import java.util.Stack;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class XMLParser {
    HashMap metadata = null;
    Extractor extractor = null;
    public XMLParser(Extractor extractor) {
        metadata = new HashMap();
        this.extractor = extractor;
    }
    public HashMap getMetadata() throws Exception {
        this.parse();
        return this.metadata;
    }
    private void parse() throws Exception {
        String xml = this.extractor.getXML();
        SAXParserFactory factory = SAXParserFactory.newInstance();
        SAXParser saxParser = null;
        saxParser = factory.newSAXParser();
        DefaultHandler handler = new MySAXApp(this.metadata);

        // String -> byte[] -> ByteArrayInputStream -> InputStream
        // and then we wonder why Java is inefficient

        saxParser.parse(new ByteArrayInputStream(extractor.getXML().getBytes()), handler);
    }
}



class MySAXApp extends DefaultHandler {
    private boolean i_care_about_this_tag = false;
    private StringBuffer buffer;
    private HashMap metadata;
    private Stack tagStack = null;

    public MySAXApp(HashMap where) {
        super();
        this.metadata = where;
        this.tagStack = new Stack();
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
        String namespace = element.substring(0, element.lastIndexOf(":"));
        String tag       = element.substring(element.lastIndexOf(":") + 1);
        if (i_care_about_this_tag == false) buffer = null;
        i_care_about_this_tag = true;
        if (namespace.equals("xapRights")) {
            if (tag.equals("Copyright")) {
                System.err.print("Found rights: ");
            }
            else if (tag.equals("Marked")) {
                System.err.print("Found marked: ");
            }
            else if (tag.equals("WebStatement")) {
                System.err.print("Found web statement: ");
            }
        }
        else if (namespace.equals("cc")) {
            if (tag.equals("cc:license")) {
                System.err.print("Found CC License: ");
            }
        }
        else if (namespace.equals("dc")) {
            try {
                DublinCore.typeByString(tag);
            }
            catch (Exception e) { i_care_about_this_tag = false; }
        }
        else {
            i_care_about_this_tag = false;
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
