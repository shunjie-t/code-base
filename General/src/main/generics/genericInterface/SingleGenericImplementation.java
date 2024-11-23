package main.generics.genericInterface;

public class SingleGenericImplementation<T> implements SingleGenericInterface<T> {
	private T property;
	
	public SingleGenericImplementation(T input) {
		this.property = input;
	}

	@Override
	public T getProperty() {
		return property;
	}

	@Override
	public void setProperty(T input) {
		this.property = input;
	}

}
