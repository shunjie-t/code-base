package main.generics.hierarchyClass;

import main.generics.hierarchyInterface.SuperClassInterface;

public class SuperClass implements SuperClassInterface {
	private String type;

	public SuperClass() {
		this.type = "super class";
	}

	public String getType() {
		return type;
	}
}
