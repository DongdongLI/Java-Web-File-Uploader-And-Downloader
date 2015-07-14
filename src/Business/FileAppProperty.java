package Business;

import java.util.HashMap;
import java.util.Map;


public class FileAppProperty {
	private Map<String,String> map=new HashMap<String,String>();
	
	private FileAppProperty(){}
	
	private static  FileAppProperty instance=new FileAppProperty();
	
	public static FileAppProperty getInstance(){
		return instance;
	}
	public void addProperty(String propertyName,String propertyValue){
		map.put(propertyName, propertyValue);
	}
	
	public String getProperty(String propertyName){
		return map.get(propertyName);
	}
}
