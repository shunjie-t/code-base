package com.Generics;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.Generics.GenericClass.DoubleGenericClass;
import com.Generics.GenericClass.GenericBoundedBaseClass;
import com.Generics.GenericClass.GenericBoundedSubClass;
import com.Generics.GenericClass.GenericBoundedSuperClass;
import com.Generics.GenericClass.SingleGenericClass;
import com.Generics.GenericInterface.DoubleGenericImplementation;
import com.Generics.GenericInterface.DoubleGenericInterface;
import com.Generics.GenericInterface.SingleGenericImplementation;
import com.Generics.GenericInterface.SingleGenericInterface;
import com.Generics.HierarchyClass.BaseClass;
import com.Generics.HierarchyClass.MultiInterfaceBaseClass;
import com.Generics.HierarchyClass.MultiInterfaceSubClass;
import com.Generics.HierarchyClass.MultiInterfaceSuperClass;
import com.Generics.HierarchyClass.SubClass;
import com.Generics.HierarchyClass.SuperClass;

public class Generics {

	public static void main(String[] args) {
		typeSafetyExample();
		genericTypeMethodExamples();
		arrayToListExample();
		genericBoundedTypeMethodExamples();
		multiBoundedTypeMethodExample();
		genericClassExample();
		genericBoundedClassExample();
	}
	
	private static void typeSafetyExample() {
		/*generics are represented with the angle bracket <> with the specified type within*/
		/*it is used to ensures type safety*/
		List<Integer> IntegerArrayList = new ArrayList<>();
		List<Character> CharacterArrayList = new ArrayList<>();
		List<String> StringArrayList = new ArrayList<>();
		List<Byte> ByteArrayList = new ArrayList<>();
		List<Short> ShortArrayList = new ArrayList<>();
		List<Long> LongArrayList = new ArrayList<>();
		List<Float> FloatArrayList = new ArrayList<>();
		List<Double> DoubleArrayList = new ArrayList<>();
		List<Boolean> BooleanArrayList = new ArrayList<>();
		
		/*without type safety, collections are able to store different data types*/
		List arrayList = new ArrayList<>();
		arrayList.add("data1");
		arrayList.add(false);
		arrayList.add(5.5);
		arrayList.add(new Object());
		/*however, accessing the items in collection in the following commented lines will result in exception due to data type mismatch*/
//		String[] store = new String[arrayList.size()];
//		for(int i = 0; i < arrayList.size(); i++) {
//			store[i] = (String) arrayList.get(i);
//		}
		
		/*generics work only with reference data types, primitive data types are not allowed*/
		/*following commented lines will produce compile-time error as it has primitive data type*/
//		List<int> intList;
//		List<char> charList;
//		List<byte> byteList;
//		List<short> shortList;
//		List<long> longList;
//		List<float> floatList;
//		List<double> doubleList;
//		List<boolean> booleanList;
		
		/*primitive type arrays can be passed to the type parameter because arrays are reference types.*/
		/*following code are valid use of generic as they are arrays of primitive data type*/
		List<int[]> intArrayList;
		List<char[]> charArrayList;
		List<byte[]> byteArrayList;
		List<short[]> shortArrayList;
		List<long[]> longArrayList;
		List<float[]> floatArrayList;
		List<double[]> doubleArrayList;
		List<boolean[]> booleanArrayList;
		
		/*the unbounded wild card genric is represented by angle brackets with a question mark within, <?>*/
		List<?> wildCardList;
		
		/*the upper bounded generic wildcard is represented in this manner <? extends specifiedClassType>.*/
		/*it allows the wild card generic type(?) to be either the specified class type or its sub class type.*/
		List<? extends BaseClass> upperBoundWildCardList;
		/*In the 3 examples below, the specified class type is BaseClass, sub class type is SubClass and super class type is SuperClass.*/
		List<BaseClass> baseClass = Arrays.asList(new BaseClass(), new BaseClass());
		List<SubClass> subClass = Arrays.asList(new SubClass(), new SubClass());
		List<SuperClass> superClass = Arrays.asList(new SuperClass(), new SuperClass());
		/*the 2 lines below are valid as arrayLists of specified class type and sub class type are assigned to the upper bounded list.*/
		upperBoundWildCardList = baseClass;
		upperBoundWildCardList = subClass;
		/*the commented line below is invalid as it is a super class type of the specified class type being assigned.*/
//		upperBoundWildCard = superClass;
		
		/*the lower bounded generic wildcard is represented in this manner <? super specifiedClassType>.*/
		/*it allows the wild card generic type(?) to be either the specified class type or a super class type.*/
		List<? super BaseClass> lowerBoundWildCardList;
		/*following examples are valid as the lower bounded list accepts */
		lowerBoundWildCardList = baseClass;
		lowerBoundWildCardList = superClass;
		/*commented line below is invalid as it is a sub class type of specified class type.*/
//		lowerBoundWildCard = subClass;	
	}
	
	private static void genericTypeMethodExamples() {
		GenericMethods genericMethods = new GenericMethods();
		
		/*explicit way of declaring the parameter type*/
		//String variable1 = genericMethods.<Integer>identifyDataType(2147483647);
		//String variable2 = genericMethods.<Character>identifyDataType('%');
		//String variable3 = genericMethods.<String>identifyDataType("enroll");
		//String variable4 = genericMethods.<Boolean>identifyDataType(true);
		//String variable5 = genericMethods.<Byte>identifyDataType(Byte.valueOf("127"));
		//String variable6 = genericMethods.<Short>identifyDataType(Short.valueOf("32767"));
		//String variable7 = genericMethods.<Long>identifyDataType(Long.valueOf(976903820L));
		//String variable8 = genericMethods.<Float>identifyDataType(Float.valueOf(4.591498F));
		//String variable9 = genericMethods.<Double>identifyDataType(Double.valueOf(581.66666666));
		//String variable10 = genericMethods.<Object>identifyDataType(new GenericMethods());
		
		/*implicit way of declaring the parameter type*/
		String variable1 = genericMethods.identifyDataType(2147483647);
		String variable2 = genericMethods.identifyDataType('%');
		String variable3 = genericMethods.identifyDataType("enroll");
		String variable4 = genericMethods.identifyDataType(true);
		String variable5 = genericMethods.identifyDataType(Byte.valueOf("127"));
		String variable6 = genericMethods.identifyDataType(Short.valueOf("32767"));
		String variable7 = genericMethods.identifyDataType(Long.valueOf(976903820L));
		String variable8 = genericMethods.identifyDataType(Float.valueOf(4.591498F));
		String variable9 = genericMethods.identifyDataType(Double.valueOf(581.66666666));
		String variable10 = genericMethods.identifyDataType(new GenericMethods());
		String variable11 = genericMethods.identifyDataType(null);
		System.out.println("===identifyDataTypeExample===");
		System.out.println();
		System.out.println(variable1);
		System.out.println(variable2);
		System.out.println(variable3);
		System.out.println(variable4);
		System.out.println(variable5);
		System.out.println(variable6);
		System.out.println(variable7);
		System.out.println(variable8);
		System.out.println(variable9);
		System.out.println(variable10);
		System.out.println(variable11);
		System.out.println();
	}
	
	private static void arrayToListExample() {
		GenericMethods genericMethods = new GenericMethods();
		Integer[] integerArray = {1,2,3};
		String[] stringArray = {"a","b","c"};
		
		List<Class> s1 = genericMethods.arrayToList(integerArray, Object::getClass);
		List<Class> s2 = genericMethods.arrayToList(stringArray, Object::getClass);
		List<Class> s3 = genericMethods.arrayToList(stringArray, String::getClass);
		List<char[]> s4 = genericMethods.arrayToList(stringArray, String::toCharArray);
		List<String> s5 = genericMethods.arrayToList(stringArray, String::toLowerCase);
		List<String> s6 = genericMethods.arrayToList(stringArray, String::toString);
		List<String> s7 = genericMethods.arrayToList(stringArray, String::toUpperCase);
		List<String> s8 = genericMethods.arrayToList(integerArray, Integer::toBinaryString);
		List<String> s9 = genericMethods.arrayToList(integerArray, Integer::toHexString);
		List<String> s10 = genericMethods.arrayToList(integerArray, Integer::toOctalString);
		List<Class> s11 = genericMethods.arrayToList(integerArray, Integer::getClass);
		List<String> s12 = genericMethods.arrayToList(integerArray, Object::toString);
	}
	
	private static void genericBoundedTypeMethodExamples() {
		GenericMethods genericMethod = new GenericMethods();
		System.out.println("===methodBoundingExamples===");
		System.out.println();
		
		/*In the 3 examples below, the specified class type is BaseClass, sub class type is SubClass and super class type is SuperClass.*/
		BaseClass baseClass = new BaseClass();
		SubClass subClass = new SubClass();
		SuperClass superClass = new SuperClass();
		/*combination1 method parameter is upper bounded to BaseClass, it accepts BaseClass and SubClass type arguments*/
		genericMethod.boundedGeneric(baseClass);
		genericMethod.boundedGeneric(subClass);
		/*commented line below is invalid as SuperClass type are not accepted for upper bound generic methods*/
//		genericMethod.combination1(superClass);
		
		System.out.println();
		
		/*in the following examples, boundedGeneric method take 3 arguments*/
		/*left argument's specified type is bounded to SuperClass, middle is bounded to BaseClass and right is bounded to SubClass*/
		/*the valid combinations are as below*/
		genericMethod.boundedGeneric(superClass, baseClass, subClass);
		genericMethod.boundedGeneric(baseClass, baseClass, subClass);
		genericMethod.boundedGeneric(subClass, baseClass, subClass);
		genericMethod.boundedGeneric(superClass, subClass, subClass);
		genericMethod.boundedGeneric(baseClass, subClass, subClass);
		genericMethod.boundedGeneric(subClass, subClass, subClass);
		
		System.out.println();
	}
	
	private static void multiBoundedTypeMethodExample() {
		GenericMethods genericMethod = new GenericMethods();
		/*In the 3 examples below, the specified class type is BaseClass, sub class type is SubClass and super class type is SuperClass.*/
		BaseClass baseClass = new BaseClass();
		SubClass subClass = new SubClass();
		SuperClass superClass = new SuperClass();
		
		genericMethod.subClassMultiBoundedGeneric1(subClass);
		genericMethod.subClassMultiBoundedGeneric2(subClass);
		genericMethod.subClassMultiBoundedGeneric3(subClass);
		
		genericMethod.subClassBaseClassMultiBoundedGeneric1(baseClass);
		genericMethod.subClassBaseClassMultiBoundedGeneric1(subClass);
		
		genericMethod.subClassBaseClassMultiBoundedGeneric2(baseClass);
		genericMethod.subClassBaseClassMultiBoundedGeneric2(subClass);
		
		genericMethod.subClassBaseClassSuperClassMultiBoundedGeneric(superClass);
		genericMethod.subClassBaseClassSuperClassMultiBoundedGeneric(baseClass);
		genericMethod.subClassBaseClassSuperClassMultiBoundedGeneric(subClass);
		
		MultiInterfaceSubClass mISubClass = new MultiInterfaceSubClass();
		MultiInterfaceBaseClass mIBaseClass = new MultiInterfaceBaseClass();
		MultiInterfaceSuperClass mISuperClass = new MultiInterfaceSuperClass();
		
		genericMethod.subClassAllBounded(mISubClass);
		
		genericMethod.baseClassAllBounded(mIBaseClass);
		genericMethod.baseClassAllBounded(mISubClass);
		
		genericMethod.superClassAllBounded(mISuperClass);
		genericMethod.superClassAllBounded(mIBaseClass);
		genericMethod.superClassAllBounded(mISubClass);
	}
	
	private static void genericClassExample() {
	    SingleGenericClass<?> wildCardObject = new SingleGenericClass<>(100);
	    SingleGenericClass<String> strObject = new SingleGenericClass<>("String Data");
	    
	    DoubleGenericClass<String, Integer> dblObject = new DoubleGenericClass<>("Balance: ", 9800);
	    	    
	    SingleGenericInterface<String> sglIObject = new SingleGenericImplementation<>("String data");
	    
	    DoubleGenericInterface<Integer, String> dblObjectImpl = new DoubleGenericImplementation<>(6000, "dollar");
	}
	
	private static void genericBoundedClassExample() {
		GenericBoundedSubClass<SubClass> genericSubClass = new GenericBoundedSubClass<>(new SubClass());
		
		GenericBoundedBaseClass<BaseClass> genericBaseClass1 = new GenericBoundedBaseClass<>(new SubClass());
		GenericBoundedBaseClass<BaseClass> genericBaseClass2 = new GenericBoundedBaseClass<>(new BaseClass());
		
		GenericBoundedSuperClass<SuperClass> genericSuperClass1 = new GenericBoundedSuperClass<>(new SubClass());
		GenericBoundedSuperClass<SuperClass> genericSuperClass2 = new GenericBoundedSuperClass<>(new BaseClass());
		GenericBoundedSuperClass<SuperClass> genericSuperClass3 = new GenericBoundedSuperClass<>(new SuperClass());
	}
}
