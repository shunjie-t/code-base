package main.generics.genericClass;

import main.generics.hierarchyClass.SubClass;

public class GenericBoundedSubClass<T extends SubClass> {
	private T property;
	
	public GenericBoundedSubClass( T property) {
		this.property = property;
	}

	public T getProperty() {
		return property;
	}

	public void setProperty(T property) {
		this.property = property;
	}
}
