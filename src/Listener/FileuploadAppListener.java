package Listener;

import java.io.InputStream;
import java.util.Map;
import java.util.Properties;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import Business.FileAppProperty;

/**
 * Application Lifecycle Listener implementation class FileuploadAppListener
 *
 */
 
@WebListener
public class FileuploadAppListener implements ServletContextListener {

    /**
     * Default constructor. 
     */
    public FileuploadAppListener() {
        // TODO Auto-generated constructor stub
    }

	/**
     * @see ServletContextListener#contextDestroyed(ServletContextEvent)
     */
    public void contextDestroyed(ServletContextEvent arg0)  { 
         // TODO Auto-generated method stub
    }

	/**
     * @see ServletContextListener#contextInitialized(ServletContextEvent)
     */
    public void contextInitialized(ServletContextEvent arg0)  { 
    	InputStream in = getClass().getClassLoader().getResourceAsStream("/upload.properties");
    	Properties properties= new Properties();
    	try{
    		properties.load(in);
    		for(Map.Entry<Object, Object> prop:properties.entrySet()){
    			FileAppProperty.getInstance().addProperty((String)prop.getKey(), (String)prop.getValue());
    		}
    	}
    	catch(Exception e){
    		e.printStackTrace();
    	}
    	
    }
	
}
