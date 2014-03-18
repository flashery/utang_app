package utang_app;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class ViewTable extends JPanel implements MouseListener {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 767816770264403473L;
	
	// Components for our table
	JTable jtable;
	JScrollPane jsp;
	DefaultTableModel dtm = null;
	UtangModel um = null;
	ResultSet rs = null;
	Utang utang = new Utang("");
	public ViewTable()
	{
		//this.setSize(utang.frame_width, utang.frame_height);
		this.setLayout(new GridLayout());
		this.setBackground(Color.BLACK);
		displayTable();
		
		jtable.setBackground(new Color(51,102,153));
		jtable.setForeground(Color.WHITE);
		jtable.addMouseListener(this);
		jtable.setPreferredScrollableViewportSize(new Dimension(550, 70));
		jtable.setFillsViewportHeight(true);

		jsp = new JScrollPane(jtable);
		this.add(jsp);
	}
	
	public void displayTable()
	{
		
		// Getting data from the Database
		try {
			um = new UtangModel();
			rs = um.getAllUtang("palautang");
			
			// Lets create our TableModel
			ResultSetMetaData rs_meta = rs.getMetaData();
	
			String [] column_names = new String[rs_meta.getColumnCount()];
			for(int i = 0; i < column_names.length; i++)
			{
				column_names[i] = rs_meta.getColumnLabel(i+1);
			}
			
			dtm = new DefaultTableModel(null, column_names);

			while(rs.next())
			{
				
				dtm.addRow(new Object []{
						rs.getString("id"),
						rs.getString("name"),
						rs.getString("utang_date"),
						rs.getString("utang_amount")
				});

			}
			
			rs.close();
			
		} 
		catch (ClassNotFoundException | InstantiationException | IllegalAccessException | SQLException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		// Setting up and adding the Components for the table
		jtable = new JTable(dtm);
	
		
	}
	
	public void setSelectedData()
	{
		
		int row_index = jtable.getSelectedRow();

			
		for(int i = 0; i < jtable.getColumnCount(); i++)
		{
				
			if(jtable.getColumnName(i).equals("id"))
			{
				utang.setId(Integer.parseInt(jtable.getModel().getValueAt(row_index, i).toString()));
			}
				
			if(jtable.getColumnName(i).equals("name"))
			{
				utang.setName(jtable.getModel().getValueAt(row_index, i).toString());
			}
				
			if(jtable.getColumnName(i).equals("utang_amount"))
			{
				utang.setCurrentAmount(Integer.parseInt(jtable.getModel().getValueAt(row_index, i).toString()));
			}
				
		}	
		
	}
	
	
	
	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub
		this.setSelectedData();
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

}
