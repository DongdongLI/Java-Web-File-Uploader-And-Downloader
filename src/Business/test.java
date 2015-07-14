package Business;

public class test {
	public static void main(String[] args){
		inside a = new test().new inside();
		String s = a.id;
	}
	
	private class inside{
		public String id ="";
	}
}
