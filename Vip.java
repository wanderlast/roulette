public class Vip extends Player{
	private String name;
	private int digitID;
	private double cashBack;

	public Vip()
	{
		super();
		digitID = 1000;
		name = "N/A";
	}
	
	public Vip(int c, int id, String n)
	{
		super(c);
		digitID = id;
		name = n;
	}
	
	public void setID(int num)
	{
		digitID = num;
	}
	
	public void setName (String n)
	{
		name = n;
	}
	
	public int getID()
	{
		return digitID;
	}
	
	public String getName()
	{
		return name;
	}
	
	public boolean spinAgain(Game g)
	 {
		boolean cont = super.spinAgain(g);
		if(!cont)
		{
			cashBack = totalBet * 0.05;
			cash += cashBack;
		}
		return cont;
	  
	 }  // method spinAgain

	public String toString()
	{
		return name + " - " + digitID;
	}
}