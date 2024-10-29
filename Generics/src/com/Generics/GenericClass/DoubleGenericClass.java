package com.Generics.GenericClass;

public class DoubleGenericClass<T1, T2> {
	private T1 objectT1;
	private T2 objectT2;
	
	public DoubleGenericClass(T1 objectT1, T2 objectT2) {
		this.objectT1 = objectT1;
		this.objectT2 = objectT2;
	}
	
	public T1 getObjectT1() {
		return this.objectT1;
	}
	
	public void setObjectT1(T1 object) {
		this.objectT1 = object;
	}
	
	public T2 getObjectT2() {
		return this.objectT2;
	}
	
	public void setObjectT2(T2 object) {
		this.objectT2 = object;
	}
}
