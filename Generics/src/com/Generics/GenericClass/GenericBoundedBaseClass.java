package com.Generics.GenericClass;

import com.Generics.HierarchyClass.BaseClass;

public class GenericBoundedBaseClass<T extends BaseClass> {
	private T property;
	
	public GenericBoundedBaseClass( T property) {
		this.property = property;
	}

	public T getProperty() {
		return property;
	}

	public void setProperty(T property) {
		this.property = property;
	}
}
