package utang_app;

public class Utang {
	
	public static String name;
	private static int current_amount;
	private static int id;
	
	// Settings
	public String appname;
	public final String version = "1.0";
	public final int frame_width = 900;
	public final int frame_height = 700;
	public final String table_name = "palautang";
	
	public Utang(String appname)
	{
		this.appname = appname;
	}
	
	public void setName(String name)
	{
		this.name = name;
	}
	
	public String getName()
	{
		return this.name;
	}
	
	public void setCurrentAmount(int current_amount) 
	{
		this.current_amount = current_amount;
	}
	
	public int getCurrentAmount() 
	{
		return current_amount;
	}
	
	public void setId(int id) 
	{
		this.id = id;
	}
	
	public int getId() 
	{
		return id;
	}

	
}
