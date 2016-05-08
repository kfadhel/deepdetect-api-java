package com.deepdetect.api.enums;

public enum MLType {
	SUPERVISED, UNSUPERVISED;
	
	public String getValue(){
		return this.name().toLowerCase();
	}
}
