package com.remindme.server;

import java.io.File;
import java.util.HashMap;
import java.util.Scanner;

public class PropertiesManager {
	private static PropertiesManager properties_manager = null;
	private HashMap<String, String> properties;
	
	private PropertiesManager(){
		properties = new HashMap<String, String>();
		loadProperties();
	}
	
	private void loadProperties(){
		try{
			File f = new File(System.getProperty("user.dir") + "/Remind Me/conf/properties");
			Scanner sc = new Scanner(f);
			String next, first, second;
			int equal;
			while(sc.hasNextLine()){
				next = sc.nextLine();
				if(next.length() == 0 || next.charAt(0) == '#')
					continue;
				equal = next.indexOf("=");
				first = next.substring(0, equal);
				second = next.substring(equal+1);
				addProperty(first, second);
			}
			sc.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public String getProperty(String first){
		return this.properties.get(first);
	}
	
	public Integer getPropertyInteger(String first){
		return Integer.parseInt(this.properties.get(first));
	}
	
	private void addProperty(String first, String last){
		properties.put(first, last);
	}
	
	public static PropertiesManager getInstance(){
		if(PropertiesManager.properties_manager == null)
			PropertiesManager.properties_manager = new PropertiesManager();
		return PropertiesManager.properties_manager;
	}
	
	public static void main(String[] args) {
		PropertiesManager pm = PropertiesManager.getInstance();
		System.out.println(pm.getProperty("db_user"));
	}
}
