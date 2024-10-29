package com.Generics.GenericClass;

public class SingleGenericClass<T> {
	private T object;
	
	public SingleGenericClass(T object) {
		this.object = object;
	}
	
	public T getObject() {
		return this.object;
	}
	
	public void setObject(T object) {
		this.object = object;
	}
}
