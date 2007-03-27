package org.cc.dam;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;


public class ConstraintList {
	public static final String DELIM = "@";
	private ArrayList<ConstraintWrapper> list;
	/***
	 * Default Constructor
	 * Creates a ConstraintWrapper list.
	 */
	public ConstraintList(){
		list = new ArrayList<ConstraintWrapper>();
	}
	/***
	 * Binds a key-value pair as an object and adds it to the list.
	 * @param key The tag namespace
	 * @param value The constraint for the key
	 */
	public void addConstraint(String key, String value){
		list.add(new ConstraintWrapper(key,value));
		
	}
	/***
	 * Binds a key-value pair as an object and add it to the list.
	 * This method is to be used with the getData() method from the
	 * queryTable.
	 * @param the Data array 
	 */
	public void addConstraint(String[] data){
		if(data.length == 2){
			list.add(new ConstraintWrapper(data[0],data[1]));
		}
		else
			System.err.println("bad data.");
	}
	/***
	 * Converts the list into a HashMap, which is the "query form"
	 * @return "Queryable" HashMap of all the key-value pairs
	 */
	public HashMap toHashMap(){
		HashMap <String,String>hm = new HashMap<String,String>();
		for(int i= 0;i < list.size(); i++){
			String key = list.get(i).getKey();
			String value = list.get(i).getValue();
			hm.put(key, value);
		}
		return hm;
	}
	/***
	 * Saves a query to a file.
	 * @param path filepath
	 */
	public void saveToFile(String path){
		try {

			PrintWriter outFile = new PrintWriter(new FileWriter(path));

			for(int i= 0;i < list.size(); i++){
				String key = list.get(i).getKey();
				String value = list.get(i).getValue();
				outFile.println(key + DELIM + value);
			}

			outFile.flush();
			outFile.close();

		} catch (IOException ex) {
			System.err.println("Error saving query.");
		}
	}
	/***
	 * Returns an iterator of the key-value list.
	 * @return iterator
	 */
	public Iterator toIterator(){
		return list.iterator();
	}
	/***
	 * Returns the size of the list
	 * @return size
	 */
	public int size(){
		return list.size();
	}
	
}
