import java.util.Scanner;

import javax.swing.JOptionPane;
public class Player {
	protected int cash;
	protected int money, totalBet = 0;
	protected int bet[] = {0, 0, 0};
	protected int betCount = 0;
	protected int betType[] = new int[3];
	protected int selectNumber[] = {-2, -2, -2};
	protected Scanner scan;
	protected int chip1, chip5, chip25, chip100, userBalance; // I guess userbalance is combined value of chips?
	
	public Player()
	{
		//default cash balance of 100
		cash = 100;
		userBalance = 0;
	}
	
	public Player(int c)
	{
		cash = c;
		userBalance = 0;
	}
	
	public int getChip1(){ return chip1; }
	public int getChip5(){ return chip5; }
	public int getChip25(){ return chip25; }
	public int getChip100(){ return chip100; }
	public int getUserBalance(){ return userBalance; }
	
	public int getBetCount()
	{
		return betCount;
	}
	
	public String getName()
	{
		return "Player - cash: " + cash;  
	}
	
	public int getCash()
	{
		return cash;
	}
	
   public void setUserBalance()
   {
		userBalance = chip1 + 5*chip5 + 25*chip25 + 100*chip100;
   }
	
   	//exchanges cash for chips
	public void exchange(int trade)
	{
		cash = cash - trade;
		earningsAdjust(trade);
	}
	
	//when you bet, you lose chips
	public void reduce(int trade)
	{
		int c1 = 0, c5 = 0, c25 = 0, c100 = 0;		
		
		while (trade > 99)
		{
			trade -= 100;
			c100++;
		}
		
		while (trade > 24)
		{
			trade -= 25;
			c25++;
		}
		
		while (trade > 4)
		{
			trade -= 5;
			c5++;
		}
		
		while (trade > 0)
		{
			trade -= 1;
			c1++;
		}
		
		chip1 -= c1;
		chip5 -= c5;
		chip25 -= c25;
		chip100 -= c100;
		setUserBalance();
	}
	
	public void earningsAdjust(int trade)
	{
		int c1 = 0, c5 = 0, c25 = 0, c100 = 0;	
		
		while (trade > 99)
		{
			trade -= 100;
			c100++;
		}
		
		while (trade > 24)
		{
			trade -= 25;
			c25++;
		}
		
		while (trade > 4)
		{
			trade -= 5;
			c5++;
		}
		
		while (trade > 0)
		{
			trade -= 1;
			c1++;
		}
		
		chip1 += c1;
		chip5 += c5;
		chip25 += c25;
		chip100 += c100;
		setUserBalance();
	}
	
	public int makeBetType()
	{		
		 //make a bet, loop it by user choice or when betCount reaches 3
		 Object[] options = {"Bet by color!", "Bet by number!"};
		 Object[] colorOptions = {"Green", "Red", "Black"};
		 
		 int n = JOptionPane.showOptionDialog(null, "Pick how you're going to bet.",
				    "Betting Options", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE, null,
				    options, options[1]);
		 
		 if (n == JOptionPane.YES_OPTION) //bet by color
		 {
			 betType[betCount] = JOptionPane.showOptionDialog(null, "What color do you want to bet on?",
					 "Betting Options", JOptionPane.YES_NO_CANCEL_OPTION,
					 JOptionPane.PLAIN_MESSAGE, null, colorOptions, options[0]);
			 //YES = 0, NO = 1, CANCEL = 2
			 //betting on green, red, and black respectively
		 }
		 else //bet by number
		 {
			betType[betCount] = 3;
			
			String result = JOptionPane.showInputDialog(null, "What number do you want to bet on?");
			if (result.equals("00"))
				result = "-1";
			
			try
			{
				selectNumber[betCount] =  Integer.parseInt(result);
				
				while(selectNumber[betCount] < Wheel.MIN_NUM || selectNumber[betCount] > Wheel.MAX_NUM)
				{
					result = JOptionPane.showInputDialog(null, "You can't bet on that number! What number do you want to bet on?");
					if (result.equals("00"))
						result = "-1";
					selectNumber[betCount] =  Integer.parseInt(result);
				}
			}
			catch(NumberFormatException e)
			{
				JOptionPane.showMessageDialog(null, "You can only bet on numbers!");
			}
		 }
		 
		 return betType[betCount];
	}
		 
	public int makeBet(int max, int min)
	{
		int num = -1;
		String amount = JOptionPane.showInputDialog(null, "How much do you want to bet? Enter the numerical amount only.");
		
		while (num < 0)
		{
			try
			{
				num = Integer.parseInt(amount);				
				while (num > max || num < min)
				{
					amount = JOptionPane.showInputDialog(null, "Your bet must be between $" + min + " and $" + max + ". Please enter your bet.");
					num = Integer.parseInt(amount);
				}
				
				while (!validExchange(num))
				{
					amount = JOptionPane.showInputDialog(null, "You have " + userBalance + " in chips to bet with. Please enter your bet.");
					num = Integer.parseInt(amount);
					
					while (num > max || num < min)
					{
						amount = JOptionPane.showInputDialog(null, "Your bet must be between $" + min + " and $" + max + ". Please enter your bet.");
						num = Integer.parseInt(amount);
					}
				}
				
				bet[betCount] = num;
				reduce(num);
				betCount++;
			}
			catch(NumberFormatException e)
			{
				amount = JOptionPane.showInputDialog(null, " Please enter a valid number. How much do you want to bet?");
				num = Integer.parseInt(amount);
			}
		}
		
		return num;
	}
	
	   //checks for validity of most cash/chip exchanges??
	public boolean validExchange(int n)
	{
		
		if(n > userBalance)
		{
			JOptionPane.showMessageDialog(null, "You do not have enough chips to bet this amount.");
			return false;
		}
			
		return true; //if all of these are true, then it's valid
	}
	 
	 public int payment()
	 {
		 int totalEarn = 0;
		 
		 for (int i = 0; i < betCount; i++)
		 {
		   int earn = Wheel.payoff(betType[i], bet[i], selectNumber[i]);
		   totalEarn += earn;
		 }

		 earningsAdjust(totalEarn); //gives chips for winnings
		 return totalEarn;
	 }


	 public boolean spinAgain(Game g)
	 {
	   int answer = JOptionPane.showConfirmDialog(null, "Do you want to stay in this game?");
	   if(answer == JOptionPane.YES_OPTION)
	   {
		   //reset betType, betCount and selectNumber
		   betCount = 0;
		   for (int i = 0; i < 3; i++)
		   {
			   betType[i] = -2;
			   selectNumber[i] = -2;
		   }
		   //check if they have no more chips
		   if(userBalance <= 0)
		   {
			   answer = JOptionPane.showConfirmDialog(null, "You need more chips if you want to keep playing! Add more chips?");
			   if(answer == JOptionPane.YES_OPTION)
			   {
				   while (userBalance <= 0)
				   {
					   g.exchange(this);
				   }
				   return true;
			   }
			   else
				   return false;
		   }
	   		return true;
	   }
	   else
		   	return false;
 	  
	 }  // method spinAgain
	 
	 public String toString()
	 {
		 return getName();
	 }
}