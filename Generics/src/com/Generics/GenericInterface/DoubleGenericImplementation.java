package com.Generics.GenericInterface;

public class DoubleGenericImplementation<T1, T2> implements DoubleGenericInterface<T1, T2> {
	private T1 property1;
	private T2 property2;
	
	public DoubleGenericImplementation(T1 property1, T2 property2) {
		this.property1 = property1;
		this.property2 = property2;
	}
	
	@Override
	public T1 getPropertyT1() {
		return property1;
	}

	@Override
	public void setPropertyT1(T1 property) {
		this.property1 = property;
	}

	@Override
	public T2 getPropertyT2() {
		return property2;
	}

	@Override
	public void setPropertyT2(T2 property) {
		this.property2 = property;
	}

}
