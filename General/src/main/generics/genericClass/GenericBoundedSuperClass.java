package main.generics.genericClass;

import main.generics.hierarchyClass.SuperClass;

public class GenericBoundedSuperClass<T extends SuperClass> {
	private T property;
	
	public GenericBoundedSuperClass( T property) {
		this.property = property;
	}

	public T getProperty() {
		return property;
	}

	public void setProperty(T property) {
		this.property = property;
	}

}
