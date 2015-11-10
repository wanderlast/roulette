/*  Program: Roulette
    Author: Lianne Louie
    Class: CSCI145
    Date: 5-9-14
    Description: DRIVER CLASS FOR PROJECT 4 - ROULETTE

    I certify that the code below is my own work.
	Exception(s): N/A
*/

import java.util.*;
import javax.swing.*;
import java.io.*; //for reading files

public class Roulette
{	
	public static void main(String[] args)
	{
		//variables -- rename these later depending on other classes
		String ModelName; //right now we only have one model type, but might as well keep this
		int lineCount = 0;
		int numGames = 0;
		int minBet = 0, maxBet = 0, count1 = 0, count5 = 0, count25 = 0, count100 = 0;
		
		String playerName;
		int id, cash = 0;
		
		ArrayList gameList = new ArrayList();
		ArrayList playerList = new ArrayList();
		
		//load games file
		File saveFile = new File("games.txt");
		
		try
		{
			Scanner scan = new Scanner(saveFile);
						
			while (scan.hasNext())
			{
				//reads in one String
				String input = scan.nextLine();
				
				Scanner parse = new Scanner(input);
				parse.useDelimiter(" ");
				
				if(lineCount == 0)
				{
					//since we only have one game type right now, we don't have to worry too much about this
					ModelName = parse.next();
					numGames = parse.nextInt();
					lineCount++;
				}
				else
				{
					minBet = parse.nextInt();
					maxBet = parse.nextInt();
					count1 = parse.nextInt();
					count5  = parse.nextInt();
					count25 = parse.nextInt();
					count100 = parse.nextInt();
					
					//create object of Game type
					Game loadGame = new Game(lineCount, minBet, maxBet, count1, count5, count25, count100);
					
					//send that object to the ArrayList
					gameList.add(loadGame);
					
					lineCount++;
				}
			}
			scan.close();
				
		} catch (FileNotFoundException e)
		{
			//throw error message here "Save files could not be found!"
			JOptionPane.showMessageDialog(null, "Game save files could not be found!");
		}
		
		//informs user of how many games were loaded
		JOptionPane.showMessageDialog(null, "Loaded " + gameList.size() + 
				" games out of " + numGames + " games.");
		
		//now load the players
		File playerFile = new File("players.txt");
		
		try
		{
			Scanner scan = new Scanner(playerFile);
						
			while (scan.hasNext())
			{
				//reads in one String
				String input = scan.nextLine();
				
				Scanner parse = new Scanner(input);
				parse.useDelimiter(" ");
				
				Player loadPlayer;
				
				String playerType = parse.next();
				if(playerType.equals("Y"))
				{
					//if VIP
					cash = parse.nextInt();
					id = parse.nextInt();
					playerName = parse.next();
					loadPlayer = new Vip(cash, id, playerName);
				}
				else
				{
					//if not VIP
					cash = parse.nextInt();
					loadPlayer = new Player(cash);
				}
				
				playerList.add(loadPlayer);
			}
			
			scan.close();
		} catch (FileNotFoundException e)
		{
			//throw error message here "Save files could not be found!"
			JOptionPane.showMessageDialog(null, "Player file could not be found!");
		}
		
		//build GUI
		RouletteGUI menu = new RouletteGUI(gameList, playerList);
	}	
}