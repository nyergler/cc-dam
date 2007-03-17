package org.cc.dam;

public class ConstraintWrapper {

	private String key;
	private String value;
	
	public ConstraintWrapper(String key, String value){
		this.key = key;
		this.value = value;
		
	}
	public String getKey(){
		return key;
	}
	public String getValue(){
		return value;
	}
	
}
