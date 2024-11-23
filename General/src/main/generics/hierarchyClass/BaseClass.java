package main.generics.hierarchyClass;

import main.generics.hierarchyInterface.BaseClassInterface;

public class BaseClass extends SuperClass implements BaseClassInterface {
	private String type;

	public BaseClass() {
		this.type = "base class";
	}

	public String getType() {
		return type;
	}
}
