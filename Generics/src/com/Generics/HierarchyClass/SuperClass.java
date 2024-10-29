package com.Generics.HierarchyClass;

import com.Generics.HierarchyInterface.SuperClassInterface;
import com.Generics.HierarchyInterface.SuperClassInterface2;

public class SuperClass implements SuperClassInterface {
	private String type;

	public SuperClass() {
		this.type = "super class";
	}

	public String getType() {
		return type;
	}
}
