//////////////////////////////////////////////////////////////////////
///                                                                ///
///  Derby Test Program                                            ///
///                                                                ///
//////////////////////////////////////////////////////////////////////

/**
 * A class for handling interactions to the database, currently Derby
 * from the Apache project.  This class provides methods for creating,
 * updating, and querying the database.  Be sure to call the close()
 * method when you're done working with a database.
 */

package org.cc.dam;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Observable;
import java.util.Properties;

public class Database extends Observable {
    public static String framework = "embedded";
    public static String driver = "org.apache.derby.jdbc.EmbeddedDriver";
    public static String protocol = "jdbc:derby:";

    private Connection connection;
    private Properties properties;

    private boolean    changed = true;

    public Database() {
        try {
            /*
             * Load the driver class, which starts Derby
             * automatically when working in embedded mode.
             */
            Class.forName(Database.driver).newInstance();

            this.connection = null;
            this.properties = new Properties();

            this.properties.put("user", "user");
            this.properties.put("password", "secret");

            /*
             * Setting create = true in the connection will
             * automatically create the specified database.
             * Simple delete the file given by the DB name
             * to drop it.  This will be created in the FS
             * either in the value of system property
             * derby.system.home or in the current directory.
             */

            // create a new database with the name derbyDB
            this.connection =
                DriverManager.getConnection(Database.protocol + "derbyDB;create=true",
                                            this.properties);
            this.connection.setAutoCommit(false);

            Statement statement = this.connection.createStatement();

            statement.execute(" CREATE TABLE metadata(     " +
                              "   subject   VARCHAR(255),  " +  // assume filename / url <= 255 char
                              "   predicate VARCHAR(255),  " +  // assume url <= 255 char
                              "   object    VARCHAR(1024)  " +
                              " )                          " );

            this.commit();
        }
        catch (Throwable exception) {
            System.err.println("Exception thrown! ");
            if (exception instanceof SQLException) {
                SQLException sqlex = (SQLException)exception;
                System.err.println("SQL Error Code #" + sqlex.getErrorCode());
                //if (sqlex.getErrorCode() != 20000)  // error code for table exists
                    this.printSQLError(sqlex);
            }
            else {
                exception.printStackTrace();
            }
        }
    }

    /**
     * Insert a new triple into the database.  A triple is composed of
     * a subject, predicate, and object.  This does not auto-commit.
     *
     * @param subject   The subject of the metadata, e.g. MyDocument.
     * @param predicate The metadata attribute of the data, e.g. Author.
     * @param object    The fact of the metadata, e.g. Alan Turing.
     */

    public int insert(String subject, String predicate, String object) {
        try {
            /*
             * Now we can make some SQL commands to the database.
             */

            PreparedStatement statement =
                this.connection.prepareStatement("INSERT INTO metadata VALUES(?, ?, ?)");

            statement.clearParameters();
            statement.setString(1, subject);
            statement.setString(2, predicate);
            statement.setString(3, object);
            statement.execute();
            statement.close();
        }
        catch (Throwable exception) {
            System.out.println("Exception thrown!");
            if (exception instanceof SQLException) {
                this.printSQLError((SQLException) exception);
            }
            else {
                exception.printStackTrace();
            }
        }
        this.changed = true;
        return 0;
    }
    
    public void doQuery(HashMap attrib) {
        HashSet files  = this.findFilesWithAttributes(attrib);
        HashSet<String[]> result = new HashSet<String[]>();
        Iterator it = files.iterator();
        while (it.hasNext()) {
            String[] metadata = {"dc:title", "dc:creator"};
            String   filename = (String)it.next();
            HashMap hm = this.getMetadataForFile(filename, metadata);
            String[] tmp = {(String)hm.get("dc:title"), (String)hm.get("dc:creator"), filename};
            result.add(tmp);
        }
        this.setChanged();
        this.notifyObservers(new UpdateMessage(UpdateMessage.CLEAR, null));
        this.setChanged();
        this.notifyObservers(new UpdateMessage(UpdateMessage.QUERY, result));
    }
    
    /**
     * Find all files in the database that have the given attributes.
     * 
     * @param attrib An array of pairs (more correctly, arrays of at least dimension two) of the form [attribute, value] for searching through files.  Example: {{"dc:title", "My Story"}, {"dc:author", "Me"}} 
     * @return A Vector of Strings that contains a listing of filenames with attributes matching those specified.
     */
    
    public HashSet findFilesWithAttributes(HashMap attrib) {
        // case attrib of
        //      null -> getAllFileNames
        //     (H:T) -> [ file | file <- QUERY, getMetadataForFile(file, T) = T ]
        if (attrib == null) {
            return this.getAllFileNames();
        }
        else {
            try {
            Iterator it    = attrib.keySet().iterator();
            String key     = (String)it.next(); 
            String value   = (String)attrib.get(key);
            //Statement st   = this.connection.createStatement();
            PreparedStatement ps = this.connection.prepareStatement("SELECT subject FROM metadata WHERE predicate=? AND object=?");
            ps.setString(1, key);
            ps.setString(2, value);
            ResultSet rs   = ps.executeQuery();
            HashSet<String> files  = new HashSet<String>();
            HashSet<String> result = new HashSet<String>();
            while (rs.next())
                files.add(rs.getString("subject"));
            
            attrib.remove(key);  // WARNING: attrib is no longer the same as it was when it came in!
            
            Object[] tmp        = attrib.keySet().toArray();
            String[] attributes = new String[tmp.length];
            for (int i = 0; i < tmp.length; i += 1)
                attributes[i] = (String)tmp[i];
            
            it = files.iterator();
            while (it.hasNext()) {
                String file = (String)it.next();
                if (this.getMetadataForFile(file, attributes).equals(attrib))
                    result.add(file);
            }
            return result;
            }
            catch  (Throwable exception) {
                System.out.println("Exception thrown!");
                if (exception instanceof SQLException) {
                    this.printSQLError((SQLException) exception);
                }
                else {
                    exception.printStackTrace();
                }
            }
            return null;
        }
    }

    /**
     * Get a finite map of requested metadata attribute -> value pairs.
     * @param filename The filename to examine in the database.
     * @param attrib An array of attributes to search and return values for.
     * @return A mapping of the form attribute -> value for each pair found.
     */
    
    public HashMap getMetadataForFile(String filename, String[] attrib) {
        HashMap<String, String> result = new HashMap<String, String>();
        try {
            if (attrib != null) {
                PreparedStatement statement =
                    this.connection.prepareStatement("SELECT object FROM metadata WHERE subject=\'" + filename + "\' AND predicate=?");
                
                for (int i = 0; i < attrib.length; i += 1) {
                    statement.setString(1, attrib[i]);
                    ResultSet rs = statement.executeQuery();
                    while (rs.next()) {
                        result.put(attrib[i], rs.getString("object"));
                    }
                    this.changed = true;
                }
            }
            else {
                Statement statement = this.connection.createStatement();
                ResultSet rs = statement.executeQuery("SELECT predicate, object FROM metadata WHERE subject=\'" + filename + "\'");
                while (rs.next()) {
                    result.put(rs.getString("predicate"), rs.getString("object"));
                }
                this.changed = true;
                statement.close();
            }
        }
        catch  (Throwable exception) {
            System.out.println("Exception thrown!");
            if (exception instanceof SQLException) {
                this.printSQLError((SQLException) exception);
            }
            else {
                exception.printStackTrace();
            }
        }
        return result;
    }
    
    public int setMetadataForFile(String filename, HashMap metadata) {
    	try {
    		PreparedStatement statement =
    			this.connection.prepareStatement("UPDATE metadata SET object = ? WHERE subject = \'" + filename + "\' AND predicate = ?");
    		Iterator it = metadata.keySet().iterator();
    		while (it.hasNext()) {
    			String key = (String)it.next();
    			String value = (String)metadata.get(key);
    			statement.setString(1, value);
    			statement.setString(2, key);
    			if (statement.executeUpdate() == 0) { // zero rows affected
    				this.insert(filename, key, value);
    				System.err.println("new");
    			}
    			else
    				System.err.println(key + " := " + value);
    		}
    		this.commit();
    	}
        catch  (Throwable exception) {
            System.out.println("Exception thrown!");
            if (exception instanceof SQLException) {
                this.printSQLError((SQLException) exception);
            }
            else {
                exception.printStackTrace();
            }
        }
        return 0;
    }
    
    /**
     * Deletes the entries (rows) for any specific filename and with the given metadata attributes set.
     * If the attribute set is set to null, all entries for that file will be deleted (i.e. that file is completely removed from the index).
     * 
     * @param filename The name of the file to search for.
     * @param attrib Metadata attributes which will be deleted.
     * @return
     */
    
    public int deleteEntryForFileWithMetadata(String filename, String[] attrib) {
        int rowsAffected = 0;
        try {
            if (attrib != null) {
                PreparedStatement statement =
                    this.connection.prepareStatement("DELETE FROM metadata WHERE subject=\'" + filename + "\' AND predicate=?");
                
                for (int i = 0; i < attrib.length; i += 1) {
                    statement.setString(1, attrib[i]);
                    rowsAffected += statement.executeUpdate();
                }
                this.changed = true;
            }
            else {
                Statement statement = this.connection.createStatement();
                rowsAffected = statement.executeUpdate("DELETE FROM metadata WHERE subject=\'" + filename + "\'");
                this.changed = true;
            }
        }
        catch  (Throwable exception) {
            System.out.println("Exception thrown!");
            if (exception instanceof SQLException) {
                this.printSQLError((SQLException) exception);
            }
            else {
                exception.printStackTrace();
            }
        }
        this.commit();
        return 0;
    }
    
    /**
     * Gets all the names of the files from the index.
     * @return A HashSet containing all the file names.
     */
    
    public HashSet getAllFileNames() {
        HashSet<String> result = new HashSet<String>();
        try {
            Statement statement = this.connection.createStatement();
            ResultSet rs = statement.executeQuery("SELECT subject FROM metadata");
            while (rs.next()) {
                result.add(rs.getString("subject"));
            }
            this.changed = true;
            statement.close();
        }
        catch  (Throwable exception) {
            System.out.println("Exception thrown!");
            if (exception instanceof SQLException) {
                this.printSQLError((SQLException) exception);
            }
            else {
                exception.printStackTrace();
            }
        }
        return result;
    }
    
    /**
     * Print out all the information in the metadata table of the database.
     * Mostly for debugging purposes; will probably be removed after we're
     * happy that things work right.
     */

    public void exposeAll() {
        HashSet files  = this.getAllFileNames();
        HashSet<String[]> result = new HashSet<String[]>();
        Iterator it = files.iterator();
        while (it.hasNext()) {
            String[] metadata = {"dc:title", "dc:creator"};
            String   filename = (String)it.next();
            HashMap hm = this.getMetadataForFile(filename, metadata);
            String[] tmp = {(String)hm.get("dc:title"), (String)hm.get("dc:creator"), filename};
            result.add(tmp);
        }
        this.setChanged();
        this.notifyObservers(new UpdateMessage(UpdateMessage.CLEAR, null));
        this.setChanged();
        this.notifyObservers(new UpdateMessage(UpdateMessage.LISTING, result));
    }

    /**
     * Commit changes made to the database.  This must be called before
     * issuing close() in order to keep your changes.
     */

    public void commit() {
        try {
            this.connection.commit();
            this.changed = false;
        }
        catch (Throwable exception) {
            System.out.println("Exception thrown!");
            if (exception instanceof SQLException) {
                this.printSQLError((SQLException) exception);
            }
            else {
                exception.printStackTrace();
            }
        }
    }

    /**
     * Roll back changes made to the database.  Any modifications you
     * have made will be discarded.  This is called automatically when
     * you close the database.
     */

    public void rollback() {
        try {
            this.connection.rollback();
            this.changed = false;
        }
        catch (Throwable exception) {
            System.out.println("Exception thrown!");
            if (exception instanceof SQLException) {
                this.printSQLError((SQLException) exception);
            }
            else {
                exception.printStackTrace();
            }
        }
    }

    /**
     * Close the database connection and shutdown the database.  This
     * takes a pessimistic view of database interaction and automatically
     * rolls back any changes since the last time you called commit().
     * Of course, if you call commit() right before close(), all your
     * changes will be saved correctly.
     */

    public void close() {
        try {
            if (this.changed) this.rollback();
            this.connection.close();
            boolean gotSQLExc = false;
            if (Database.framework.equals("embedded")) {
                try {
                    DriverManager.getConnection("jdbc:derby:;shutdown=true");
                }
                catch (SQLException se) {
                    gotSQLExc = true;
                }
                if (!gotSQLExc) {
                    System.out.println("Database did not shut down normally");
                }
                else {
                    System.out.println("Database shut down OK.");
                }
            }
        }
        catch (Throwable exception) {
            System.out.println("Exception thrown!");
            if (exception instanceof SQLException) {
                this.printSQLError((SQLException) exception);
            }
            else {
                exception.printStackTrace();
            }
        }
    }
    
    /**
     * A nice way to print out SQL errors from Derby.
     * 
     * @param exception The exception thrown as the result of trying
     *                  to interact with the database.
     */

    private void printSQLError(SQLException exception) {
        while (exception != null) {
            System.out.println(exception.toString());
            exception = exception.getNextException();
        }
    }
}
