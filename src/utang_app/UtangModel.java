package utang_app;


import java.awt.Color;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JFrame;
import javax.swing.JOptionPane;


public class UtangModel{

	Connection connection = null;
	
	public UtangModel() throws ClassNotFoundException, java.sql.SQLException, InstantiationException, IllegalAccessException 
	{
		String connectionURL = "jdbc:mysql://localhost:3306/utang_database";
		String user = "flashery";
		String password = "ErxHvAGC2wLewE82";

		Class.forName("com.mysql.jdbc.Driver").newInstance();
		connection = DriverManager.getConnection(connectionURL, user, password);
	}
	
	
	/*****************************************************************
	 ============================ SELECT ALL =========================
	 *****************************************************************/
	protected ResultSet getAllUtang(String table_name)
	{
		
		Statement statement = null;
		ResultSet rs = null;
		if(!table_name.equals(""))
		{
			// initializing the query statement
			try {statement = connection.createStatement(); } 
			catch (SQLException e) { e.printStackTrace(); }
			
			// executing query to database
			try {
				
				rs = statement.executeQuery("SELECT id, name, utang_date, utang_amount FROM " + table_name);
				
			} catch (SQLException e) {e.printStackTrace();}
			
		}
		
		return rs;
	}
	
	
	/*****************************************************************
	 ================= Get data by column or by row ==================
	 *****************************************************************/
	protected String getByColumn(boolean all, String table_name, String column_name, String name)
	{
		Statement statement = null;
		ResultSet rs = null;
		String result = "";
		if(!table_name.equals(""))
		{
			// initializing the query statement
			try {statement = connection.createStatement();} 
			catch (SQLException e) {e.printStackTrace();}
			
					
			// executing query to database
			try {
				
				if(all) // Check if we need to get all the data on this column
				{
					rs = statement.executeQuery("SELECT " +  column_name + " FROM " + table_name + "");
					while(rs.next()) result = rs.getString(column_name);

				}
				else {
					rs = statement.executeQuery("SELECT " +  column_name + " FROM " + table_name + " WHERE name = \"" +  name + "\"");
					while(rs.next()) result = rs.getString(column_name); // Putting the data in result variable to be return
				}

			} catch (SQLException e) {e.printStackTrace();}
		}

		return result;
	}
	
	
	/*****************************************************************
	 ================= DELETE DATA OR MODIFY DATA ====================
	 *****************************************************************/
	protected void pay(String table_name, String name, int amount)
	{
		if(validateData(table_name, name))
		{
			Statement statement = null;
			boolean paid = false;
			int current_amount = 0;
			
			// initializing the query statement
			try {statement = connection.createStatement();} 
			catch (SQLException e) {e.printStackTrace();}
			
			// executing query to database
			try {
				
				String column_name = "utang_amount"; // Column in the database we want to access
				
				// Getting the current amount from database
				current_amount = Integer.parseInt(getByColumn(false, table_name, column_name, name));
				
				if(amount < current_amount)
				{
					// If amount is lesser than the current amount from the database subtract it
					int new_amount = current_amount - amount;
					statement.executeUpdate("UPDATE " + table_name + " SET " + column_name + " = " + new_amount + " WHERE name = \"" +  name + "\"");
					connection.close();
					paid = false;
				}
				else
				{
					// If the amount are equal or greater than the current amount from the database delete it
					statement.executeUpdate("DELETE FROM " + table_name + " WHERE name = \"" +  name + "\"");
					connection.close();
					paid = true;
					
				}
	
				connection.close();
				
			} catch (SQLException e) {e.printStackTrace();}
			
			finally
			{
				if(paid)
				{
					JOptionPane.showMessageDialog(null, "You have successfully paid your Utang", "Success", JOptionPane.INFORMATION_MESSAGE);
				}
				else
				{
					JOptionPane.showMessageDialog(null, "You have successfully made difference on your Utang", "Success", JOptionPane.INFORMATION_MESSAGE);
				}
				
			}
		}
		else
		{
			JOptionPane.showMessageDialog(null, name + " is not found in our Palautang list", "Failed", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	
	/*****************************************************************
	 ================= UPDATE DATA ON THE DATABASE ===================
	 *****************************************************************/
	protected void update(String table_name, String name, int amount)
	{
		if(validateData(table_name, name))
		{
			Statement statement = null;
			
			// initializing the query statement
			try {statement = connection.createStatement();} 
			catch (SQLException e) {e.printStackTrace();}
			
			// executing query to database
			try {
				
				statement.executeUpdate("UPDATE " + table_name + " SET name = \"" + name +"\", utang_amount = " + amount + " WHERE name = \"" +  name + "\"");
				connection.close();
				
			} catch (SQLException e) {e.printStackTrace();}
			finally
			{
				JOptionPane.showMessageDialog(null, "Data successfully updated", "Success", JOptionPane.INFORMATION_MESSAGE);
			}
		}
		else
		{
			JOptionPane.showMessageDialog(null, name + " is not found in our Palautang list", "Failed", JOptionPane.ERROR_MESSAGE);
		}
		
	}
	
	/*****************************************************************
	 ================= INSERT DATA ON THE DATABASE ===================
	 *****************************************************************/
	protected void insert(String table_name, String name, int amount)
	{
		if(!validateData(table_name, name))
		{
			Statement statement = null;
			
			// initializing the query statement
			try {
				
				statement = connection.createStatement();
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			// executing query to database
			try {
			
				statement.executeUpdate("INSERT INTO " + table_name + " (name, utang_date, utang_amount) VALUES (\"" + name + "\", now(), " + amount + " )");
				connection.close();
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			finally
			{
					JOptionPane.showMessageDialog(null, "Data successfully added", "Success", JOptionPane.INFORMATION_MESSAGE);
			}
		}
		else
		{
			String message = "Sorry that data is already existed \n"
							+ name + " is already listed in our Palautang list";
			JOptionPane.showMessageDialog(null, message, "Failed", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	private boolean validateData(String table_name, String name)
	{
		ResultSet rs = null;
		boolean result = false;
		
		rs = this.getAllUtang(table_name);
		
		try {
			while(rs.next())
			{
				if(rs.getString("name").equals(name))
				{
					result = true;
					break;
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return result;
	}
	
}