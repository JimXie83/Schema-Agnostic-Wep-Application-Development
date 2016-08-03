package com.jamesx.util;

public enum ObjectState {
	Unchanged(0), Added(1), Modified(2),Deleted(3);

	    final int value;

	    private ObjectState(int value) {
	        this.value = value;
	    }
	    private ObjectState() {
	        this.value = 0;
	    }
	    public int getValue() {
	        return value;
	    }
	//public String toString(){return getValue()+"";}
}

//public class ObjectState {
//	public static int Unchanged = 0;
//	public static int Added = 1;
//	public static int Modified = 2;
//	public static int Deleted = 4;
//}