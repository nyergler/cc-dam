//////////////////////////////////////////////////////////////////////
///                                                                ///
///  PDF Parser                                                    ///
///                                                                ///
//////////////////////////////////////////////////////////////////////

/**
 * A class for extracting XML metadata from a PDF document.  It's a
 * wrapper around the iText libraries, which actually do the work,
 * but this class conforms to an important interface to be realized at
 * a later time.  This will eventually be broken into two classes, one
 * for the PDF parsing, and another for generic XML parsing.  They're
 * together now so I can experiment and get things working side-by-side.
 */

package org.cc;
import org.cc.xmp.Extractor;
import com.lowagie.text.*;
import com.lowagie.text.pdf.*;
import java.io.*;

public class PDFExtractor implements Extractor {
    PdfReader reader = null;
    public PDFExtractor(String filename) throws Exception {
        this.reader = new PdfReader(new FileInputStream(filename));
    }
    public String getXML() {
        try {
            System.err.println(new String(reader.getMetadata()));
            return new String(reader.getMetadata());
        }
        catch (IOException exception) {
            return "";
        }
    }
}