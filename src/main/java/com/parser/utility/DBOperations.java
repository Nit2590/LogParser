package com.parser.utility;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import com.parser.event.EventLog;




public class DBOperations {

	Logging log=new Logging("DBOperations");

	// Initializing DB Variables
	private static final String DB_NAME = PropertyReader.getFieldValue("DB_NAME");
	private static final String DB_TABLENAME = PropertyReader.getFieldValue("DB_TABLENAME");;
	private static final String DB_SQL = String.valueOf(PropertyReader.getFieldValue("DB_SQL")).replace("{{TABLE_NAME}}",DB_TABLENAME);
	
	private Connection connection;

	//Opening DB connections
	 public DBOperations()
	{
		//Creating connection
		connection=openConnection();
	}
	/**
	 * Closes the existing connection and stops the HSQLDB server
	 */
	public void stopHSQLDB() {
		
		if (connection != null) {
			try {
				connection.close();
				log.info("Connection to HSQL Server closed");
			} catch (SQLException e) {
				log.info("Failed to close HSQL Server");
				e.printStackTrace();
			}
		}

	}
	
	/**
	 *  method to create a connection
	 *
	 */
	private Connection openConnection() {	
		try {
			Class.forName("org.hsqldb.jdbc.JDBCDriver");
			return DriverManager.getConnection("jdbc:hsqldb:file:DBLogsEvent"+"/"+DB_NAME, "SA", "");

		} catch (SQLException | ClassNotFoundException e) {
			log.fatal( e.getMessage());
		}
		return null;
	}

	/**
	 *  method to drop the existing HSQL Table
	 */
	public void dropHSQLDBLTable() {
		Statement stmt = null;

		try {
			if (connection == null) {
				connection = openConnection();
			}
			stmt = connection.createStatement();
			stmt.executeUpdate("DROP TABLE "+DB_TABLENAME);
			log.info("Successfully dropped any existing table with name "+ DB_TABLENAME);
		}  catch (SQLException e) {
			log.fatal("No Table to Drop");
		} 
	}
	/**
	 * Method to delete the existing HSQL Table
	 */
	public void deleteHSQLDBLTable() {
		Statement stmt = null;

		try {
			if (connection == null) {
				connection = openConnection();
			}
			stmt = connection.createStatement();
			stmt.executeUpdate("Delete from "+DB_TABLENAME);
			connection.commit();

		}  catch (SQLException e) {
			log.fatal("No Table to Delete");
		}
	}


	/**
	 * Creates the alerts table if not already existing
	 */
	public void createHSQLDBTable() {
		Statement stmt = null;

		try {
			if (connection == null) {
				connection = openConnection();
			}

			//Check first if table exists
			DatabaseMetaData dbm = connection.getMetaData();
			ResultSet tables = dbm.getTables(null, null, DB_TABLENAME, new String[] {"TABLE"});
			
			if (!tables.next()) {
				stmt = connection.createStatement();
				stmt.executeUpdate(DB_SQL);
				log.info("Successfully created new db table with name :"+DB_TABLENAME);
			}

		}  catch (SQLException  e) {
			log.fatal( e.getMessage());
		} 
	}
	
	/**
	 * Inserts a list of events to the database
	 * @param eventList
	 */
	public void insertEvents(List<EventLog> eventList) {
		Statement stmt = null; 
		int insertedRows = 0;

		try {
			if (connection == null) {
				connection = openConnection();
			}
			stmt = connection.createStatement(); 
			
			for (EventLog e : eventList) {

				//Prevent insertion if ID already exists:
				ResultSet existingRow = stmt.executeQuery("SELECT * FROM "+DB_TABLENAME.toUpperCase()+" WHERE LOG_ID='"+e.getId()+"'");

				if (!existingRow.next()) {

					String updateStatement = "INSERT INTO "+DB_TABLENAME+" VALUES('"+e.getId()+"',"+e.getDuration()+",'"+e.getHost()+"','"+e.getType()+"',"+e.getAlert()+")";
					insertedRows += stmt.executeUpdate(updateStatement);

				} else {
					log.info("Row not inserted row as ID "+e.getId()+ " already exists");
				}

			}
			connection.commit(); 
		} catch (SQLException e) {
			log.fatal( e.getMessage());
		} 
		log.info(insertedRows+" rows inserted into table "+ DB_TABLENAME.toUpperCase());
	}
	
	/**
	 * Returns a display-friendly string showing the content of the alerts table
	 * @return elements of the alerts db to be displayed
	 */
	public String readEvents() {
		StringBuilder sb  = new StringBuilder();
		Statement stmt = null; 
		ResultSet result;

		if (connection == null) {
			connection = openConnection();
		}

		try {
			stmt = connection.createStatement();
			result = stmt.executeQuery("SELECT * FROM "+DB_TABLENAME.toUpperCase());
			ResultSetMetaData rsmd = result.getMetaData();
			

			while (result.next()) {
			    for (int i = 1; i <= 5; i++) {
			        if (i > 1) {
			        	sb.append(",  ");
			        }
			        String columnValue = result.getString(i);
			        sb.append(rsmd.getColumnName(i)+":"+columnValue);
			    }
			    sb.append("\n");
			}
		} catch (SQLException e) {
			log.fatal( e.getMessage());
		}
		return sb.toString();

	}

	/**
	 * Returns a display-friendly string showing the content of the alerts table
	 * @return elements of the alerts db to be displayed
	 */
	public List<String> readParticularEvent(String eventId) {
		StringBuilder sb  = new StringBuilder();
		Statement stmt = null;
		ResultSet result;
		List<String> event=new ArrayList<>();

		if (connection == null) {
			connection = openConnection();
		}

		try {
			stmt = connection.createStatement();
			ResultSet existingRow = stmt.executeQuery("SELECT * FROM "+DB_TABLENAME.toUpperCase()+" WHERE LOG_ID='"+eventId+"'");
			ResultSetMetaData rsmd = existingRow.getMetaData();


			while (existingRow.next()) {
				for (int i = 1; i <= 5; i++) {
					event.add(existingRow.getString(i).toString());
				}

			}
		} catch (SQLException e) {
			log.fatal( e.getMessage());
		}
		return event;

	}
}
