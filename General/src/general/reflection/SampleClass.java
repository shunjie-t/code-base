package general.reflection;

@SampleAnnotation(annoField1="str", annoField2=10, annoField3=true)
public class SampleClass extends SampleSuperClass {
	private String field1;
	private int field2;
	private char field3;
	
	public String getField1() {
		return field1;
	}
	
	public void setField1(String field1) {
		this.field1 = field1;
	}
	
	public int getField2() {
		return field2;
	}
	
	public void setField2(int field2) {
		this.field2 = field2;
	}
	
	public char getField3() {
		return field3;
	}
	
	public void setField3(char field3) {
		this.field3 = field3;
	}
}
