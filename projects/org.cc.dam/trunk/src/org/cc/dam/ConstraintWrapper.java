package org.cc.dam;

public class ConstraintWrapper {

	private String key;
	private String value;
	/***
	 * Default Constructor
	 * @param key metadata tag
	 * @param value constraint for the key
	 */
	public ConstraintWrapper(String key, String value){
		this.key = key;
		this.value = value;
		
	}
	/***
	 * Get the key value
	 * @return key
	 */
	public String getKey(){
		return key;
	}
	/***
	 * Get the constaint value
	 * @return value
	 */
	public String getValue(){
		return value;
	}
	
}
