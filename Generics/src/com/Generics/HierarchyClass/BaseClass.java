package com.Generics.HierarchyClass;

import com.Generics.HierarchyInterface.BaseClassInterface;
import com.Generics.HierarchyInterface.BaseClassInterface2;

public class BaseClass extends SuperClass implements BaseClassInterface {
	private String type;

	public BaseClass() {
		this.type = "base class";
	}

	public String getType() {
		return type;
	}
}
