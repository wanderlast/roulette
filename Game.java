import java.io.IOException;
import java.util.*;

import javax.swing.JOptionPane;

public class Game
{
	private String name;
	private int casinoCash;
	private int minBet;
	private int maxBet;
	private int totalBets = 0;
	private int chip1;
	private int chip5;
	private int chip25;
	private int chip100;
	private int totalRounds = 0; //counts total # of rounds that have been played so far during this game session
	private int balance; //total # of chips owned by casino
	private ArrayList currentPlayers = new ArrayList(5);
	private int numPlayers = 0;
	private int roundCount = 1;
	
	//default constructor--shouldn't ever be used
	public Game()
	{
		minBet = 0;
		maxBet = 0;
		chip1 = 0;
		chip5 = 0;
		chip25 = 0;
		chip100 = 0;
		balance = 0;
		casinoCash = 0;
		name = "100A?";
		totalRounds = 0;
		roundCount = 1;
	}
	
	//normal constructor
	public Game (int index, int min, int max, int c1, int c5, int c25, int c100)
	{
		name = "100A" + index;
		setGame(min, max);
		setGame(c1, c5, c25, c100);
		totalRounds = 0;
		roundCount = 1;
	}
	
	//sets chip count
	public void setGame(int c1, int c5, int c25, int c100)
	{
		chip1 = c1;
		chip5 = c5;
		chip25 = c25;
		chip100 = c100;
		balance = c1+5*c5+25*c25+100*c100;
	}
	
	//overload -- sets min and max bet
	public void setGame(int minB, int maxB)
	{
		minBet = minB;
		maxBet = maxB;
	}

	//adds player passed to the game
	public boolean addPlayer(Player p)
	{		
		if (numPlayers > 5)
		{
			return false;
		}
		else
		{
			currentPlayers.add(p);
			while (p.getUserBalance() < 1)
			{
				exchange(p);
			}

						
			JOptionPane.showMessageDialog(null, "You have " + p.getChip100() + " $100 chips, " + p.getChip25() +
					" $25 chips, " + p.getChip5() + " $5 chips, and " + p.getChip1() + " $1 chips.");
			numPlayers++;
			return true;
		}
	}
	
	public void exchange(Player p)
	{
		int cash = 0;
		String amount = JOptionPane.showInputDialog("Please exchange cash for chips! You have $" + p.getCash());
		try
		{
			cash = Integer.parseInt(amount);
			
			while (cash < 0)
			{
				amount = JOptionPane.showInputDialog("You cannot use negative numbers! You have $" + p.getCash());
				cash = Integer.parseInt(amount);
			}

			while (cash > p.getCash())
			{
				amount = JOptionPane.showInputDialog("You cannot change into chips more money than you have! You have $" + p.getCash());
				cash = Integer.parseInt(amount);
			}
			
			p.exchange(cash); //changes it into actual chip values
			buyIn(cash); //reduces the # of chips in this game
			
		}
		catch(NumberFormatException e)
		{
			JOptionPane.showMessageDialog(null, "You must put in the amount you wish to exchange in numbers.");
		}
	}
	
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
		balance = chip1+5*chip5+25*chip25+100*chip100;
	}
	
	public void buyIn(int trade)
	{
		casinoCash += trade;
		reduce(trade);
	}
	
	public void betIn (int amount)
	{
		int c1 = 0, c5 = 0, c25 = 0, c100 = 0;	
		
		while (amount > 99)
		{
			amount -= 100;
			c100++;
		}
		
		while (amount > 24)
		{
			amount -= 25;
			c25++;
		}
		
		while (amount > 4)
		{
			amount -= 5;
			c5++;
		}
		
		while (amount > 0)
		{
			amount -= 1;
			c1++;
		}
		
		chip1 += c1;
		chip5 += c5;
		chip25 += c25;
		chip100 += c100;
		balance = chip1+5*chip5+25*chip25+100*chip100;
	}
	
	public void betRound()
	{
		 int cont = -1;
		
		//so for each of the players
		for (int i = 0; i < numPlayers; i++)
		{
			JOptionPane.showMessageDialog(null, "Player " + (i+1) + ", it's your turn! Press OK when ready.");
			
			Player p = (Player) currentPlayers.get(i);
			
			//make a bet
			int betType = p.makeBetType();
			int bet = p.makeBet(maxBet, minBet);
			betIn(bet);
			totalBets += bet;
			
			if (p.getUserBalance() > 0)
			{
				cont = JOptionPane.showConfirmDialog(null, "Do you want to make another bet? Maximum of 3 bets.");
			}
			while (p.getBetCount() < 3 && cont == JOptionPane.YES_OPTION)
			{
				betType = p.makeBetType();
				bet = p.makeBet(maxBet, minBet);
				betIn(bet);
				totalBets += bet;
				if (p.getUserBalance() > 0 && p.getBetCount() < 3)
				{
					cont = JOptionPane.showConfirmDialog(null, "Do you want to make another bet? Maximum of 3 bets.");
				}
			}
		}
	}
	
	public String payRound()
	{
		int totalLoss = 0; //loss by the casino
		int playerEarnings;
		String result = "";
		
		//so for each of the players
		for (int i = 0; i < numPlayers; i++)
		{
			Player p = (Player) currentPlayers.get(i);
			
			playerEarnings = p.payment();
			totalLoss += playerEarnings;
			result += "\nPlayer " + (i+1) + " has won " + playerEarnings + "!\n";
		}
		
		reduce(totalLoss);
		totalRounds++;
		roundCount++;
		return result;
	}
	
	public void spinAgain()
	{
		for (int j = 0; j < currentPlayers.size(); j++)
		{
			Player p = (Player) currentPlayers.get(j);
			boolean cont = p.spinAgain(this);
			
			if(!cont)
			{
				currentPlayers.remove(j);
				j--; //since everything gets moved to the left upon removal, we need to repeat the next #
			}			
		}
		
		numPlayers = currentPlayers.size();
	}
	
	//get methods
	public int getMax()
	{
		return maxBet;
	}
	
	public int getMin()
	{
		return minBet;
	}
	
	public String getName()
	{
	   return name;
	}
   
   public int getCash()
   {
	   return casinoCash;
   }
   
   public int getBalance()
   {
	   return balance;
   }
   
   public int get100s()
   {
	   return chip100;
   }
   
   public int get25s()
   {
	   return chip25;
   }
   
   public int get5s()
   {
	   return chip5;
   }
   
   public int get1s()
   {
	   return chip1;
   }
   
   public int getTotalRounds()
   {
	   return totalRounds;
   }
   
   public int getNumPlayers()
   {
	   return numPlayers;
   }
   
   public int getRoundCount()
   {
	   return roundCount;
   }
   
	public Player getPlayer(int index)
	{
		return (Player) currentPlayers.get(index);
	}   
	
	public String toString()
	{
		return name;
	}
	
}
