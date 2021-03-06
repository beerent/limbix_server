package com.remindme.database;

import java.util.ArrayList;

public class QueryResult {            
	private ArrayList<String> column_names;
	private ArrayList<ArrayList<String>> data;

	public QueryResult(ArrayList<String> column_names, ArrayList<ArrayList<String>> data){
		this.column_names = column_names;
		this.data = data;
	}
	
	public int getColumnIndex(String column_name){
		if(this.column_names.contains(column_name))
			return this.column_names.indexOf(column_name);
		else 
			return -1;
	}

	public ArrayList<String> getRow(int i){
		return this.data.get(i);
	}

	public String getElement(int i, String s){
		int j = getColumnIndex(s);
		return getElement(i,j);
	}
	
	public String getElement(int i, int j){
		return this.data.get(i).get(j);
	}

	public int numRows(){
		return this.data.size();
	}

	public int numCols(){
		if(numRows() == 0)
			return 0;

		return this.data.get(0).size();
	}

	public boolean containsData(){
		return numRows() > 0 && numCols() > 0;
	}
	
	public String toString(){
		String ret_str = "";
		for(int i = 0; i < numRows(); i++){
			if(numCols() > 0){
				ret_str += " | " + getElement(i, 0);
				for(int j = 1; j < numCols(); j++){
					ret_str += " | " + getElement(i, j);
				}	
				ret_str += " |\n";
			}
		}
		return ret_str;
	}
}