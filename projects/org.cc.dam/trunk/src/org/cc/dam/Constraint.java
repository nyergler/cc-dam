package org.cc.dam;

import java.io.Serializable;

public class Constraint implements Serializable {
	private String key;
	private String data;
	
	public Constraint(String key, String data){
		this.key = key;
		this.data = data;
	}
	public String getKey(){
		return key;
	}
	public String getData(){
		return data;
	}
}

