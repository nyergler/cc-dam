package org.cc.dam;
import java.util.*;

public class ConstraintList {
	private ArrayList<ConstraintWrapper> list;
	public ConstraintList(){
		list = new ArrayList<ConstraintWrapper>();
	}

	public void addConstraint(String key, String value){
		list.add(new ConstraintWrapper(key,value));
		
	}
	public Iterator getIterator(){
		return list.iterator();
	}
	public int size(){
		return list.size();
	}
	
}
