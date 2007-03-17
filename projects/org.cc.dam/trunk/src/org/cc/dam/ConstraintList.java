package org.cc.dam;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

import org.eclipse.swt.widgets.TableItem;

public class ConstraintList {
	public static final String DELIM = "@";
	private ArrayList<ConstraintWrapper> list;
	public ConstraintList(){
		list = new ArrayList<ConstraintWrapper>();
	}

	public void addConstraint(String key, String value){
		list.add(new ConstraintWrapper(key,value));
		
	}
	public void addConstraint(String[] data){
		if(data.length == 2){
			list.add(new ConstraintWrapper(data[0],data[1]));
		}
		else
			System.err.println("bad data.");
	}
	public HashMap toHashMap(){
		HashMap <String,String>hm = new HashMap<String,String>();
		for(int i= 0;i < list.size(); i++){
			String key = list.get(i).getKey();
			String value = list.get(i).getValue();
			hm.put(key, value);
		}
		return hm;
	}
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
	public Iterator toIterator(){
		return list.iterator();
	}
	public int size(){
		return list.size();
	}
	
}
