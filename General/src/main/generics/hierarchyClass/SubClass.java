package main.generics.hierarchyClass;

import main.generics.hierarchyInterface.SubClassInterface;

public class SubClass extends BaseClass implements SubClassInterface {
	private String type;

	public SubClass() {
		this.type = "sub class";
	}

	public String getType() {
		return type;
	}
}
