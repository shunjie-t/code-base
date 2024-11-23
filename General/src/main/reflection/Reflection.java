package main.reflection;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class Reflection {
	private static void creatingClassObject() throws ClassNotFoundException {
		/*Obtain class object by its fully qualified name*/
		Class<?> classFullQualifiedName = Class.forName("java.lang.String");
		
		/*Obtain class object of a class or interface by implicit class literal referenceS.*/
		Class<String> classLiteral = String.class;
		
		/*Obtain class object by getClass() method from an object instance*/
		String str = "a string";
		Class<?> classGetClass = str.getClass();
	}
	
	private static void classObjectCommonUsage() {
		Class<String> clazz = String.class;
		System.out.println("==classObjectCommonUsage==");
		System.out.println("getCanonicalName: " + clazz.getCanonicalName());
		System.out.println("getName: " + clazz.getName());
		System.out.println("getPackageName: " + clazz.getPackageName());
		System.out.println("getSimpleName: " + clazz.getSimpleName());
		System.out.println("getTypeName: " + clazz.getTypeName());
		System.out.println();
		
		System.out.println("-Fields: ");
		Field[] fields = clazz.getDeclaredFields();
		for (Field field : fields) {
		    System.out.println(field.getName());
		}
		System.out.println();
		
		System.out.println("-Methods: ");
		Method[] methods = clazz.getDeclaredMethods();
		for (Method method : methods) {
		    System.out.println(method.getName());
		}
		System.out.println();
	}
	
	private static void classObjectDynamicMethods() throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, NoSuchFieldException {
		/*dynamic instantiation of StringBuilder using class object*/
		Class<StringBuilder> clazz = StringBuilder.class;
		StringBuilder stringBuilder1 = clazz.getDeclaredConstructor().newInstance();
		
		/*normal instantiation of StringBuilder*/
		StringBuilder stringBuilder2 = new StringBuilder();
		
		Class<SampleClass> clazzSampleClass = SampleClass.class;
		Field field = clazzSampleClass.getDeclaredField("field1");
		field.setAccessible(true);
		SampleClass sampleClass = new SampleClass();
		field.set(sampleClass, "John Doe");
		System.out.println("==classObjectDynamicMethods==");
		System.out.println(field.getName() + ": " + field.get(sampleClass));
		System.out.println();
		
		if (clazzSampleClass.isInstance(sampleClass)) {
		    System.out.println("sampleClass is an instance of clazzSampleClass");
		}
		System.out.println();
		
		if (clazzSampleClass.isAssignableFrom(clazzSampleClass)) {
		    System.out.println("SampleClass is assignable to SampleSuperClass");
		}
		System.out.println();
		
		if (clazzSampleClass.isAnnotationPresent(SampleAnnotation.class)) {
		    System.out.println("SampleAnnotation is present on SampleClass");
		}
		System.out.println();
	}
}
