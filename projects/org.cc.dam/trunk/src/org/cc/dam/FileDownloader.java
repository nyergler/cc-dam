package org.cc.dam;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import java.io.FileNotFoundException;
import java.io.IOException;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class FileDownloader {
    private String source;
	
	public FileDownloader(String source) {
         this.source = source;
	}
	
    public int download(File dest) {
        byte[] buff = new byte[1024];        // byte buffer for faster(?) transfer
        int    numRead = 0;                  // number of bytes read from input stream
        long   numWritten = 0;               // number of bytes written to output stream

        BufferedOutputStream output = null;  // file stream to write out to
        InputStream input = null;            // input stream from the connection

        URL url = null;                      // resource location to connect to
        URLConnection connection = null;     // connection to the url


        try { url = new URL(this.source); }
        catch (MalformedURLException e) {
            System.err.println("FATAL ERROR: malformed resource name \"" + this.source + "\"");
            return 2;
        }

        try { output = new BufferedOutputStream(new FileOutputStream(dest)); }
        catch (Exception e) {
            System.err.println("FATAL ERROR: unable to open file \"" + dest.getAbsolutePath() + "\" for writing");
            return 3;
        }
        
        try { connection = url.openConnection(); }
        catch (IOException e) {
            System.err.println("FATAL ERROR: unable to open connection to resource \"" + url.toString() + "\"");
            return 4;
        }
        
        try { input = connection.getInputStream(); }
        catch (IOException e) {
            System.err.println("FATAL ERROR: unable to get input stream associated with connection");
            System.err.println("POSSIBLE REASON: remote file doesn't exist or isn't accessible");
            return 5;
        }
        
        try {
            while ((numRead = input.read(buff)) != -1) {
                try { output.write(buff, 0, numRead); }
                catch (IOException e) {
                    System.err.println("FATAL ERROR: unable to write to output stream");
                    return 6;
                }
                numWritten += numRead;
            }
        }
        
        catch (IOException e) {
            System.err.println("FATAL ERROR: unable to read connection input stream");
            return 7;
        }
        
        try { input.close(); }
        catch (IOException e) {
            System.err.println("ERROR: unable to close connection input stream");
            return 8;
        }
        
        try { output.close(); }
        catch (IOException e) {
            System.err.println("ERROR: unable to close file write stream");
            return 9;
        }
        System.out.println("downloaded successfully to \"" + dest.getAbsolutePath() + "\"");
        return 0;
    }
}
