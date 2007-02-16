package org.cc;

import org.cc.xmp.XMLExtractor;
import org.cc.xmp.XMLParser;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

public class Controller {
    private static Database database = new Database();
    /*
    public Controller() {
        this.database = new Database();
    }
    */
    public static void refreshDatabaseView() {
        database.exposeAll();
    }
    
    public static boolean parseFile(String filename) throws Exception {
        return parseFile(filename, filename);
    }

    /**
     * Parse a file and put its metadata into the database.
     * 
     * @param filename The file which physically holds the metadata on disk.
     * @param indexname The name by which to index the metadata (if stored permanently on local disk, this will probably be the same as filename, but for URL references it may be the URL address).
     * @return True if the indexing succeeds, and false if it fails (for any reason).
     * @throws Exception When something terrible and unexpected happens.
     */
    
    public static boolean parseFile(String filename, String indexname) throws Exception {
        String extension = getExtension(indexname);
        XMLParser parser = null;
        HashMap tags = null;
        if (extension.equalsIgnoreCase(".pdf"))
            parser = new XMLParser(new PDFExtractor(filename));
        else if (extension.equalsIgnoreCase(".xml"))
            parser = new XMLParser(new XMLExtractor(filename));
        else  // we don't know how to parse it
            return false;
        if (parser != null) {
            tags = parser.getMetadata();
            Iterator iterator = tags.keySet().iterator();
            // loop through all the tag/data pairs, adding each to the database
            while (iterator.hasNext()) {
                String key = (String)iterator.next();
                database.insert(indexname, key, (String)tags.get(key));
            }
            // make the changes permanent
            database.commit();
            database.exposeAll();
        }
        return true;
    }
    
    /**
     * Download a remote file, and index its metadata into the database.
     * 
     * @param url The address of the remote file.
     * @param local The name of a local copy to save persistently, or null to reference the file by URL in the index.
     * @return True if the parsing succeeds, or false if it fails (for any reason).
     * @throws Exception When something goes terribly and unexpectedly wrong.
     */
    
    public static boolean parseURL(String url, String local) throws Exception {
        	FileDownloader fd = new FileDownloader(url);
            File dest = null;
            if (local == null) {
                // make a temporary file in which to store the data
                try { dest = File.createTempFile("ccdam", "tmp"); }
                // if we can't get a temp file for some reason, there's nothing we can do but fail
                catch (IOException e) {
                    System.err.println("unable to create temporary file");
                    return false;
                }
            }
            else {
                // make the local copy file
                dest = new File(local);
            }

            // try to parse its information into the database 
            if (fd.download(dest) == 0)
                return (local == null) ? parseFile(dest.getAbsolutePath(), url) : parseFile(local);
            return false;
    }
    
    public static void doQuery(HashMap hm) {
        database.doQuery(hm);
    }
    
    /**
     * Cleanly shut down the database.  This should only be done when
     * you're finished, as you can't get it back once you've done it.
     */

    public static void shutdown() {
        if (database != null) database.close();
        database = null;
    }
    
    /**
     * Strips the extension out of a filename, or returns a blank string when none exists.
     * @param filename The name of the file for which to get the extension.
     * @return
     */

    private static String getExtension(String filename) {
        int lastindex = filename.lastIndexOf(".");
        return (lastindex == -1) ? "" : filename.substring(lastindex);
    }
    
    /**
     * Gets the database for the Controller.  For registering observers ONLY.  Don't abuse this!!
     * @return A reference to the database we're using.
     */
    
    public static Database getDatabase() {
        return database;
    }
    
    public static HashMap getMetadataForFile(String filename) {
        return database.getMetadataForFile(filename, null);
    }
    
    public static void setMetadataForFile(String filename, HashMap metadata) {
    	System.err.println("!");
    	database.setMetadataForFile(filename, metadata);
    }
    /*
    public void finalize() throws Throwable {
        if (this.database != null) this.database.close();
        super.finalize();
    }
    */
}