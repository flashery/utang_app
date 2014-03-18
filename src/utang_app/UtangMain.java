package utang_app;

import javax.swing.JFrame;

public class UtangMain extends JFrame{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6906536970379487831L;
	UtangView uv = new UtangView();
	
	public UtangMain()
	{
		this.setVisible(true);
		this.setContentPane(uv);
		this.pack();
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
	
	public static void main(String [] args)
	{
		new UtangMain();
	}
}