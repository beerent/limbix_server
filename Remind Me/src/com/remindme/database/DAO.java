package com.remindme.database;

import java.sql.*;
import java.util.ArrayList;

import com.remindme.server.PropertiesManager;

public class DAO{
	private static final String DB_URL = PropertiesManager.getInstance().getProperty("db_string");
	private static final String PASSWORD = PropertiesManager.getInstance().getProperty("db_user");
	private static final String USERNAME = PropertiesManager.getInstance().getProperty("db_password");


	public DAO(){
	}

	/*
	 * connects to the database; if successful, returns a Connection obj connected to database, else returns null
	 */
	public Connection getConnection(){
		try{
			System.out.println(DB_URL);
			System.out.println(USERNAME);
			System.out.println(PASSWORD);
			Class.forName("com.mysql.jdbc.Driver");
			return DriverManager.getConnection(DB_URL, DAO.USERNAME, DAO.PASSWORD);
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}

	/*
	 * Accepts: a sql query
	 *
	 * Logic: creates a 2D ArrayList of the results found from the query.
	 * Examples (each <> is an ArrayList, anything inside it separated by commas are elements):
	 *                            "select release_name from releases limit 1" would return:
	 *                                            <<"Release name 1">>
	 *
	 *                            "select release_name, date from releases limit 1" would return:
	 *                                            <<"Release name 1", "date">>
	 *
	 *                            "select release_name from releases limit 2" would return:
	 *                                            <<"Release name 1">, <"Release Name 2">>
	 *
	 *                            "select release_name, date from releases limit 2" would return:
	 *                                            <<"Release name 1", "date">, <"Release Name 2", "date">>
	 */
	public QueryResult executeQuery(Connection connection, PreparedStatement statement){
		ArrayList<ArrayList<String>> container = new ArrayList<ArrayList<String>>();
		ArrayList<String> column_names = new ArrayList<String>();

		try{
			ResultSet resultSet = statement.executeQuery();

			ResultSetMetaData meta = resultSet.getMetaData();
			final int columnCount = meta.getColumnCount();
			
			for(int i = 1; i <= columnCount; i++){
				column_names.add(meta.getColumnName(i));
			}

			while (resultSet.next()){
				ArrayList<String> columnList = new ArrayList<String>();
				container.add(columnList);

				for (int column = 1; column <= columnCount; column++) {
					Object value = resultSet.getObject(column);
					if (value != null) {
						columnList.add(value.toString());
					} else {
						columnList.add("null"); // you need this to keep your columns in sync.
					}
				}
			}
			resultSet.close();
			connection.close();
		}catch(Exception e){
			e.printStackTrace();
		}
		return new QueryResult(column_names, container);
	}
	
	public boolean executeUpdate(Connection connection, PreparedStatement statement){
		try {
			return statement.executeUpdate() > 0;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}

	// accepts an insert statement and commits it to the database
	public int executeInsert(Connection connection, PreparedStatement statement){                    
		try{
			statement.execute();
			ResultSet rs = statement.getGeneratedKeys();
			if(rs.next()){
				int result = rs.getInt(1);
				rs.close();
				connection.close();
				return result;
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return -1;
	}
}