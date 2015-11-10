// Class Wheel for CSCI 145 Project 2 Spring 14
// Modified by: Jialiang Zhou

//************************************************************************
//   Class Wheel represents a roulette wheel and its operations.  Its
//   data and methods are static because there is only one wheel.
//************************************************************************
import java.util.Random;
class Wheel
{
   public final static int GREEN     =  0;		// 00 OR 0
   public final static int RED       =  1;		// odd numbers
   public final static int BLACK     =  2;		// even numbers
   public final static int NUMBER    =  3;		// number bet
   public final static int MIN_NUM   =  -1;		// smallest number to bet
   public final static int MAX_NUM   = 36;		// largest number to bet

   private final static int MAX_POSITIONS = 38;	// number of positions on wheel
   private final static int NUMBER_PAYOFF = 35;	// payoff for number bet
   private final static int COLOR_PAYOFF  = 2;	// payoff for color bet

   private static int ballPosition;				// 00, 0, 1 .. 36
   private static int color;					// GREEN, RED, OR BLACK
   private static String position;				// String which has the color of the area chosen.
   private static Random rand = new Random();
   
   //=====================================================================
   //  Presents betting options
   //=====================================================================
   public static int spin()
   {
	   ballPosition = rand.nextInt(38)-1;
	   
	   if(ballPosition < 0)
	   {
		   //Ball Position: 00;
		   color = GREEN;
		   position = "Green";
	   }
	   else if( ballPosition > 0)
	   {
		   if(ballPosition % 2 == 0)
		   {
			   color = RED;
			   position = "Red";
		   }
		   else{
			   color = BLACK;
			   position = "Black";
		   }
	   }
	   else //(ballPosition == 0)
	   {
		   color = GREEN;
		   position = "Green";
	   }
	   return color;   
   }
   
   public static int getposition()
   {
	   return ballPosition;
   }
   
   public static String getColor()
   {
	   return position;
   }
   
   //make sure to change a bet on 0 and 00 to -1 and 0
   public static int payoff(int betType, int betAmount, int numBet)
   {
	   int earn;
       if(betType == 3) //bet on specific position
       {
			if(numBet == ballPosition)
				earn = NUMBER_PAYOFF * betAmount;
			else
				earn = 0;
       }
       else
       {
    	   if(betType == color) // 0 is bet on green, 1 is bet on red, 2 is bet on black
				earn = COLOR_PAYOFF * betAmount;
    	   else
				earn = 0;
       }
	   return earn;
   }
}
