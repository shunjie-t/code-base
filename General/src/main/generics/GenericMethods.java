package main.generics;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import main.generics.hierarchyClass.BaseClass;
import main.generics.hierarchyClass.MultiInterfaceBaseClass;
import main.generics.hierarchyClass.MultiInterfaceSubClass;
import main.generics.hierarchyClass.MultiInterfaceSuperClass;
import main.generics.hierarchyClass.SubClass;
import main.generics.hierarchyClass.SuperClass;
import main.generics.hierarchyInterface.BaseClassInterface;
import main.generics.hierarchyInterface.BaseClassInterface2;
import main.generics.hierarchyInterface.BaseClassSuperInterface;
import main.generics.hierarchyInterface.SubClassInterface;
import main.generics.hierarchyInterface.SubClassInterface2;
import main.generics.hierarchyInterface.SubClassSuperInterface;
import main.generics.hierarchyInterface.SuperClassInterface;
import main.generics.hierarchyInterface.SuperClassInterface2;
import main.generics.hierarchyInterface.SuperClassSuperInterface;

public class GenericMethods {
	public <T> String identifyDataType(T input) {
		if(input instanceof Integer) {
			return "variable is Integer data type";
		}
		else if(input instanceof Character) {
			return "variable is Character data type";
		}
		else if(input instanceof String) {
			return "variable is String data type";
		}
		else if(input instanceof Boolean) {
			return "variable is Boolean data type";
		}
		else if(input instanceof Byte) {
			return "variable is Byte data type";
		}
		else if(input instanceof Short) {
			return "variable is Short data type";
		}
		else if(input instanceof Long) {
			return "variable is Long data type";
		}
		else if(input instanceof Float) {
			return "variable is Float data type";
		}
		else if(input instanceof Double) {
			return "variable is Double data type";
		}
		else if(input instanceof Object) {
			return "variable is Object data type";
		}
		return "variable is null";
	}
	
	public <T, G> List<G> arrayToList(T[] input, Function<T, G> mapperFunction) {
		return Arrays.stream(input).map(mapperFunction).collect(Collectors.toList());
	}
	
	public <T extends BaseClass> void boundedGeneric(T input) {
		System.out.println("boundedGeneric method, argument type: " + input.getType());
	}
	
	public <T1 extends SuperClass, T2 extends BaseClass, T3 extends SubClass> void boundedGeneric(T1 input1, T2 input2, T3 input3) {
		System.out.println(String.format(
				"boundedGeneric method, arg 1 type: %s, arg 2 type: %s, arg 3 type: %s", 
				input1.getType(), input2.getType(), input3.getType()
				));
	}
	
	public <T extends SubClass & SubClassInterface> void subClassMultiBoundedGeneric1(T input) {}
	public <T extends SubClass & SubClassInterface & BaseClassInterface> void subClassMultiBoundedGeneric2(T input) {}
	public <T extends SubClass & SubClassInterface & BaseClassInterface & SuperClassInterface> void subClassMultiBoundedGeneric3(T input) {}
	
	public <T extends SubClass & BaseClassInterface> void subClassBaseClassMultiBoundedGeneric1(T input) {}
	public <T extends BaseClass & BaseClassInterface> void subClassBaseClassMultiBoundedGeneric1(T input) {}
	
	public <T extends SubClass & BaseClassInterface & SuperClassInterface> void subClassBaseClassMultiBoundedGeneric2(T input) {}
	public <T extends BaseClass & BaseClassInterface & SuperClassInterface> void subClassBaseClassMultiBoundedGeneric2(T input) {}
	
	public <T extends SubClass & SuperClassInterface> void subClassBaseClassSuperClassMultiBoundedGeneric(T input) {}
	public <T extends BaseClass & SuperClassInterface> void subClassBaseClassSuperClassMultiBoundedGeneric(T input) {}
	public <T extends SuperClass & SuperClassInterface> void subClassBaseClassSuperClassMultiBoundedGeneric(T input) {}
	
	public <T extends MultiInterfaceSubClass & SubClassInterface & SubClassInterface2 & SubClassSuperInterface & BaseClassInterface & BaseClassInterface2 & BaseClassSuperInterface & SuperClassInterface & SuperClassInterface2 & SuperClassSuperInterface> void subClassAllBounded(T input) {}
	public <T extends MultiInterfaceBaseClass & BaseClassInterface & BaseClassSuperInterface & BaseClassInterface2 & SuperClassInterface & SuperClassSuperInterface & SuperClassInterface2> void baseClassAllBounded(T input) {}
	public <T extends MultiInterfaceSuperClass & SuperClassInterface & SuperClassSuperInterface & SuperClassInterface2> void superClassAllBounded(T input) {}
}
