import java.io.*;
import java.util.ArrayList;

import javax.swing.JOptionPane;

//this class is used for generating and saving the transaction files
public class Transaction
{
   ArrayList gList; //parent Game
   int transactionCount = 1;
   int roundCount = 1;
   PrintWriter pw;
   String name[] = new String[2];
   int initB[] = new int[2], init1[] = new int[2], init5[] = new int[2];
   int init25[] = new int[2], init100[] = new int[2], initCash[] = new int[2];

   //default constructor
   public Transaction()
   {
	  try
	  {
		pw = new PrintWriter("transactionReport.txt");
	  }
	  catch (FileNotFoundException e)
	  {
		JOptionPane.showMessageDialog(null, "There was a problem generating the transaction report.");
	  }
	}
   
   //constructor with Game specified
   public Transaction(ArrayList l)
   {
      try
      {
    	  File file = new File("transactionReport.txt");
    	  
    	  if (!file.exists())
    	  {
    		  file.createNewFile();
    	  }
    	  pw = new PrintWriter(file);
          gList = l;
          
          //populates initial arrays          
          for (int i = 0; i < gList.size(); i++)
          {
				Game g = (Game) gList.get(i);
				  
				initB[i] = g.getBalance();
				init1[i] = g.get1s();
				init5[i] = g.get5s();
				init25[i] = g.get25s();
				init100[i] = g.get100s();
				initCash[i] = g.getCash();
				name[i] = g.getName();
          }
        		  
          transactionCount = 1;
          roundCount = 1;
      }
      catch (IOException e)
      {
    	  JOptionPane.showMessageDialog(null, "There was a problem generating the transaction report.");
      }
   }
   
	//this records one full gameplay session for one game
	public void transactionWriter()
	{
		for (int i =0; i < gList.size(); i++)
		{
			startSummaryPrinter(i);
	     
			endSummaryPrinter((Game)gList.get(i)); 
		}
		endPrinter();
	}
   
   //closes printwriter
   public void endPrinter()
   {
      //two line carriages and close printer
      pw.println();
      pw.println();
      pw.close();
   }
   
   //prints summary for beginning game state
   public void startSummaryPrinter(int i)
   {
      pw.println("Game: " + name[i]);
      pw.println("Initial Balance: " + initB[i]);
      pw.println("\tCash: \t" + initCash[i]);
      pw.println("\t$100 chips: \t" + init100[i]);
      pw.println("\t$25 chips: \t" + init25[i]);
      pw.println("\t$5 chips: \t" + init5[i]);
      pw.println("\t$1 chips: \t" + init1[i]);
      pw.println();
      pw.flush();
   }
   
   //prints summary for end game state
   public void endSummaryPrinter(Game parent)
   {  
      pw.println();
      pw.println("Ending Balance: " + parent.getBalance());
      pw.println("\tCash: \t" + parent.getCash());
      pw.println("\t$100 chips: \t" + parent.get100s());
      pw.println("\t$25 chips: \t" + parent.get25s());
      pw.println("\t$5 chips: \t" + parent.get5s());
      pw.println("\t$1 chips: \t" + parent.get1s());
      pw.println();
      pw.println();
      pw.flush();
   }
}