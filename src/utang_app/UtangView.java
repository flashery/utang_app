package utang_app;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.Border;

public class UtangView extends JPanel implements ActionListener, MouseListener {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4435504533184374641L;
	
	// initialize the components
	JRadioButton update_radio = new JRadioButton();
	JRadioButton pay_radio = new JRadioButton();
	JRadioButton add_radio = new JRadioButton();

	JLabel app_lbl = new JLabel();
	JLabel name_label = new JLabel("Name:");
	JLabel amount_label = new JLabel("Amount:");
	JLabel id_label = new JLabel("ID:");
	
	JTextField name_field = new JTextField();
	JTextField amount_field = new JTextField();
	
	JButton btn_add = new JButton();
	JButton btn_update = new JButton();
	JButton btn_pay = new JButton();
	JButton btn_view = new JButton();
	
	JComboBox id_cmb; 
	
	// Components Container
	JPanel panel_top = new JPanel();
	JPanel panel_left = new JPanel();
	JPanel panel_center = new JPanel();
	JPanel panel_right = new JPanel();
	JPanel panel_bottom = new JPanel();
	
	// initialize other objects or classes
	Utang utang = new Utang("Utang App");
	UtangModel um = null;
	ViewTable view_table = new ViewTable();
	
	// initialize variables
	int current_amount, new_amount;
	String table_name = "palautang";
	
	public UtangView()
	{
		update_radio.setText("Update");
		update_radio.addActionListener(this);
		update_radio.setBackground(new Color(0xEADBC4));
		
		pay_radio.setText("Pay");
		pay_radio.setSelected(true);
		pay_radio.addActionListener(this);
		pay_radio.setBackground(new Color(0xEADBC4));
		
		add_radio.setText("Add");
		add_radio.addActionListener(this);
		add_radio.setBackground(new Color(0xEADBC4));
		
		// Group the radio buttons.
        ButtonGroup group = new ButtonGroup();
        group.add(update_radio); 	group.add(pay_radio); 	group.add(add_radio);
        
		app_lbl.setText(utang.appname + " " + utang.version);
		app_lbl.setForeground(new Color(0xFF0098));
		app_lbl.setFont(app_lbl.getFont().deriveFont(35.0f));
		
		/*
		 * setPreferredSize is use because the container is having a layout and not null
		 */
		name_label.setPreferredSize(new Dimension(50,30));
		name_field.setPreferredSize(new Dimension(150, 20)); 
		
		name_label.setPreferredSize(new Dimension(50,30));
		amount_field.setPreferredSize(new Dimension(150, 20));
		
		this.createIdCmb();

		/*
		 * This will set the the JTable object from ViewTable class
		 * to have its own MouseListener
		 */
		view_table.jtable.addMouseListener(this);
		
		
		//+++++++++++++++++++++++ BUTTONS ++++++++++++++++++++++++//
		
		// Add Button
		this.customizeBtn(btn_add, "Add", new Dimension(60, 30));
		
		// Update Button
		this.customizeBtn(btn_update, "Update", new Dimension(60, 30));
		
		// Pay Button
		this.customizeBtn(btn_pay, "Pay", new Dimension(60, 30));
		
		// Add Button
		this.customizeBtn(btn_view, "View", new Dimension(60, 30));

		
		
		//+++++++++++++++++++++++ JPanels ++++++++++++++++++++++++//
		
		// JPanel panel_left: Left Area of the Frame
		this.customizePanel(panel_top, new Color(0xDEC7A4), new Dimension(20, 60));
		panel_top.add(app_lbl);
		
		// JPanel panel_left: Left Area of the Frame
		this.customizePanel(panel_left, new Color(0xEADBC4), new Dimension(220, 200));
			
			// Add radio buttons
			panel_left.add(update_radio); 	panel_left.add(pay_radio); 		panel_left.add(add_radio);
			// Add id label and id field
			panel_left.add(id_label);		panel_left.add(id_cmb);
			// Add name label and name text field
			panel_left.add(name_label);   	panel_left.add(name_field);
			// Add amount label and amount text field
			panel_left.add(amount_label); 	panel_left.add(amount_field);
		
			panel_left.add(btn_add);	panel_left.add(btn_update);		panel_left.add(btn_pay);		panel_left.add(btn_view);
		
		// JPanel panel_center: Center Area of the Frame
		this.customizePanel(panel_center, new Color(0xDEC7A4), null);
			panel_center.setBorder(BorderFactory.createLoweredBevelBorder());
			panel_center.setLayout(new GridLayout());
			panel_center.add(view_table);
			
		// JPanel panel_right: Right Area of the Frame
		this.customizePanel(panel_right, new Color(0xDEC7A4), new Dimension(10, 10));
		
		// JPanel panel_bottom: Bottom Area of the Frame
		this.customizePanel(panel_bottom, new Color(0xDEC7A4), new Dimension(10, 10));
		
		
		/*
		 * This method will initialize the appearance of our
		 * pane_left components  
		 */
		this.leftPanelComponent("Pay");
		
		/*
		 * This method will add all the JPanels 
		 * 				panel_top
		 * panel_left	panel_center	panel_right
		 * 				panel_bottom
		 * into our main panel (this).
		 */
		this.setLayout(new BorderLayout());
		this.setBackground(new Color(0x194166));
		
															this.add(panel_top, BorderLayout.PAGE_START);
															
		this.add(panel_left, BorderLayout.LINE_START);		this.add(panel_center, BorderLayout.CENTER);	this.add(panel_right, BorderLayout.LINE_END);
		
															this.add(panel_bottom,  BorderLayout.PAGE_END);													
	}
	
	private void createIdCmb() {
		ResultSet rs = null;
		ResultSet rs1 = null;
		int count_rows = 0;
		int count = 0;
		String [] id_array = null;
		try {
			um = new UtangModel();
			rs = um.getAllUtang("palautang");
			while(rs.next())
			{
				count_rows++;
				
			}
			
			id_array = new String[count_rows];
			
			rs1 = um.getAllUtang("palautang");
			while(rs1.next())
			{
				id_array[count] = rs1.getString("id");
				count++;
				
			}
			
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | SQLException e1) {
			e1.printStackTrace();
		}
		
		id_cmb = new JComboBox(id_array);
		id_cmb.setSelectedIndex(0);
		id_label.setPreferredSize(new Dimension(50, 20)); 
		id_cmb.setPreferredSize(new Dimension(150, 20)); 
	}

	/*
	 * This method will customize the appearance of our buttons
	 * and also add's some event listeners
	 */
	public void customizeBtn(JButton jbutton, String btnText)
	{
		jbutton.addActionListener(this);
		jbutton.setText(btnText);
		jbutton.setBackground(new Color(0x91826C));
		jbutton.setForeground(Color.WHITE);
		
	}
	
	/*
	 * This method is the same as the method above but has a 
	 * different parameters. Also called an overloading method.
	 */
	public void customizeBtn(JButton jbutton, String btnText, Dimension d)
	{
		jbutton.addActionListener(this);
		jbutton.setText(btnText);
		jbutton.setPreferredSize(d);
		jbutton.setBackground(new Color(0x91826C));
		jbutton.setForeground(Color.WHITE);
		Border one = BorderFactory.createEtchedBorder();
	    Border two = BorderFactory.createMatteBorder(4, 4, 4, 4, new Color(0x91826C));
		jbutton.setBorder(BorderFactory.createCompoundBorder(one, two));
	}
	
	/*
	 * This method will customize our JPanel's appearance
	 */
	public void customizePanel(JPanel jpanel, Color c, Dimension d)
	{
		jpanel.setPreferredSize(d);
		jpanel.setBackground(c);
	}
	
	
	/*
	 * This method is an Overriding Methods of our ActionListener implementation
	 */
	@Override
	public void actionPerformed(ActionEvent e) 
	{
		/*
		 *  e.getSource() will filter a component from
		 *  where the event occur during an ActionEvent
		 */
		if(e.getSource() == this.btn_add)
		{
			// When clicked the button add the following statements will be done
			
			if(this.validateField(this.name_field, this.amount_field))	// Check if txtfields is empty
			{
		
				try 
				{
	
					um = new UtangModel();
					um.insert("palautang", this.name_field.getText(),  Integer.parseInt(this.amount_field.getText()));
					
					
				} 
				catch (ClassNotFoundException | InstantiationException| IllegalAccessException | SQLException e1) 
				{
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				finally
				{
					// Clear
					this.name_field.setText("");
					this.amount_field.setText("");
				}
				
			}
		}
		else if(e.getSource() == this.btn_update)
		{
			// When clicked the button update
			if(this.validateField(this.name_field, this.amount_field))
			{
				try 
				{
	
					um = new UtangModel();
					um.update("palautang", this.name_field.getText(),  Integer.parseInt(this.amount_field.getText()));
					
					
				} 
				catch (ClassNotFoundException | InstantiationException| IllegalAccessException | SQLException e1) 
				{
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				finally
				{
					// Clear
					this.name_field.setText("");
					this.amount_field.setText("");
				}
			}
		}
		else if(e.getSource() == this.btn_pay)
		{
			// When clicked the button pay the following statements will be done
			if(this.validateField(this.name_field, this.amount_field))
			{
				try 
				{
	
					um = new UtangModel();
					um.pay("palautang", this.name_field.getText(),  Integer.parseInt(this.amount_field.getText()));
					
					
				} 
				catch (ClassNotFoundException | InstantiationException| IllegalAccessException | SQLException e1) 
				{
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				finally
				{
					// Clear
					this.name_field.setText("");
					this.amount_field.setText("");
				}
			}
		}
		else if(e.getSource() == this.btn_view)
		{
			// When clicked the button view the following statements will be done
			panel_center.removeAll();				  // Remove all the component in the JPanel center
			view_table = null;						  // Make this object null or doesn't exist so that we can create new one
			view_table = new ViewTable();			  // Create new instance of this object
			view_table.dtm.fireTableDataChanged();	  // Notifies the table for any data changes
			view_table.jtable.repaint();			  // Repaint the the table
			view_table.jtable.addMouseListener(this); // Create new listener because we create a new object of ViewTable class
			panel_center.add(view_table);
			panel_center.repaint();
			panel_center.revalidate();
			this.repaint();
			this.revalidate();
		}
		
		/*
		 * 
		 */
		if (e.getSource() == this.update_radio)
		{
			this.leftPanelComponent("Update");
		}
		else if(e.getSource() == this.pay_radio)
		{
			this.leftPanelComponent("Pay");
		}
		else if(e.getSource() == this.add_radio)
		{
			this.leftPanelComponent("Add");
		}
	}
	
	/*
	 * This method will validate the field for security purposes
	 */
	public boolean validateField(JTextField txt_field1, JTextField txt_field2)
	{
		boolean result = true;
		
		if(txt_field1.getText().trim().isEmpty())
		{
			JOptionPane.showMessageDialog(null, "Please input data in Name field.", "Error", JOptionPane.ERROR_MESSAGE);
			result = false;
		}
		else if(txt_field2.getText().trim().isEmpty())
		{
			JOptionPane.showMessageDialog(null, "Please input data in Amount field.", "Error", JOptionPane.ERROR_MESSAGE);
			result = false;
		}
		
		try
		{
			Integer.parseInt(txt_field2.getText());
		}
		catch(NumberFormatException ne)
		{
			JOptionPane.showMessageDialog(null, "Please only put a number on the Amount field.", "Error", JOptionPane.ERROR_MESSAGE);
			result = false;
		}
		
		return result;
	}
	
	public void leftPanelComponent(String selection)
	{
		if(selection == "Update")
		{
			btn_add.setVisible(false);
			btn_pay.setVisible(false);
			btn_update.setVisible(true);
			
			panel_left.repaint();
			panel_left.revalidate();
		}
		else if(selection == "Pay")
		{
			btn_add.setVisible(false);
			btn_update.setVisible(false);
			btn_pay.setVisible(true);
			
			panel_left.repaint();
			panel_left.revalidate();
		}
		else if(selection == "Add")
		{
			btn_pay.setVisible(false);
			btn_update.setVisible(false);
			btn_add.setVisible(true);
			
			panel_left.repaint();
			panel_left.revalidate();
		}
	}

	
	public void setFieldValue()
	{
		this.amount_field.setText(Integer.toString(utang.getCurrentAmount()));
		this.name_field.setText(utang.getName()); 
		this.id_cmb.setSelectedItem(Integer.toString(utang.getId()));
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		if(e.getSource() == view_table)
		{
			
		}
		this.setFieldValue();
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

}
