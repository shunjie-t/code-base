package main.generics.genericClass;

import main.generics.hierarchyClass.BaseClass;

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
