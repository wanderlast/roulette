/*  Program: RouletteGUI
    Author: Lianne Louie
    Class: CSCI145
    Date: 5-9-14
    Description: BUILDS GUI FOR PROJECT 4 - ROULETTE

    I certify that the code below is my own work.
	Exception(s): N/A
*/

import javax.swing.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;

public class RouletteGUI extends JPanel implements ActionListener, ItemListener
{

	//other stuff
	ArrayList gameList = new ArrayList();
	ArrayList playerList = new ArrayList();
	boolean firstTime = true;
	
	//components
	private JFrame mainFrame = new JFrame("Roulette!");
	private JPanel menuPanel = new JPanel();
	private JPanel buttonPanel = new JPanel();
	private JPanel titlePanel = new JPanel();
	
	private JLabel titleLabel = new JLabel("Roulette");
	private JLabel authorLabel = new JLabel("Author: Lianne Louie, Eric Zhou, Steven Luu");

	private JButton loadGameButton = new JButton ("Select a Game");
	private JButton quitButton = new JButton ("Quit Game");
	
	private JPanel optionsPanel = new JPanel();
	private JButton addPlayers = new JButton("Add Players");
	private JButton playRound = new JButton("Play a Round");
	private JButton switchGames = new JButton("Switch Games");
	
	private JComboBox gameNumberComboBox;
	private JTextArea descriptionTextArea = new JTextArea(2, 35);
	private JButton playGameButton = new JButton("Play this Game");
	private JButton cancelButton = new JButton("Cancel");
	
	private JComboBox playerComboBox;
	private JButton addPlayerButton = new JButton ("Add the Selected Player");
	private JButton cancelOptionsButton = new JButton("Cancel");
	
	private JTextArea transactionTextArea = new JTextArea();
	private JLabel currentActionLabel = new JLabel("The game has started!");
	
	private Font instructionFont = new Font(Font.SANS_SERIF, Font.PLAIN, 14);
	private int inProgressIndex; //index of game in progress in the arraylist
	
	private Transaction report;
	
	
	//default constructor
	public RouletteGUI()
	{
		generateMenuGUI();
		addListeners();
	}
	
	//constructor that loads ArrayList in	
	public RouletteGUI(ArrayList list, ArrayList pList)
	{
		generateMenuGUI();
		addListeners();
		gameList = list;
		playerList = pList;
		report = new Transaction(gameList);
	}
	
	//GUI for main menu, first and initial screen
	private void generateMenuGUI()
	{	
		mainFrame.getContentPane().removeAll(); //we specify content pane so that the other panes in the frame aren't destroyed
		//formatting objects
		Font titleFont = new Font(Font.SERIF, Font.BOLD, 48);
		Font labelFont = new Font(Font.SANS_SERIF, Font.PLAIN, 12);
		
		//skeleton structure
		mainFrame.setSize (500,300);
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //so it'll stop whining that I haven't terminated
		menuPanel.setLayout(new BorderLayout(50, 25));
		
		//menu components
		titleLabel.setFont(titleFont);
		authorLabel.setFont(labelFont);
		
		titlePanel.add(titleLabel);
		menuPanel.add(titlePanel,BorderLayout.PAGE_START);
		menuPanel.add(authorLabel,BorderLayout.PAGE_END);
		
		buttonPanel.setLayout(new GridLayout (3,1));
		buttonPanel.add(loadGameButton);
		buttonPanel.add(quitButton);
		menuPanel.add(buttonPanel,BorderLayout.CENTER);
		
		mainFrame.add(menuPanel);
		mainFrame.setVisible(true);
		
		//mainFrame.validate();
		mainFrame.repaint();
	}

	//GUI for load game screen, lists potential games to load and allows user to pick based off a combobox
	private void generateGameChoiceGUI()
	{
		mainFrame.getContentPane().removeAll();
		//build GUI for choosing games
		JPanel selectPanel = new JPanel();
		JLabel instructionsLabel = new JLabel("Choose the game you wish to launch from the combo box: ");
		instructionsLabel.setFont(instructionFont);
		descriptionTextArea.setText("");
		
		Object[] arr = gameList.toArray();
		
		gameNumberComboBox = new JComboBox(arr);
		gameNumberComboBox.addItemListener(this);
		gameNumberComboBox.setSelectedIndex(-1);
		
		selectPanel.add(instructionsLabel);
		selectPanel.add(gameNumberComboBox);
		selectPanel.add(descriptionTextArea);
		selectPanel.add(playGameButton);
		selectPanel.add(cancelButton);
		mainFrame.add(selectPanel);
		
		mainFrame.validate();
		mainFrame.repaint();
	}
	
	//gives options between each round of play to play a new round, add a player or switch games
	private void optionsGUI()
	{
		mainFrame.getContentPane().removeAll();
		optionsPanel.add(addPlayers);
		optionsPanel.add(playRound);
		optionsPanel.add(switchGames);
		mainFrame.add(optionsPanel);
		if (((Game)gameList.get(inProgressIndex)).getNumPlayers() < 1)
		{
			playRound.setEnabled(false);
		}
		
		mainFrame.validate();
		mainFrame.repaint();
	}
	
	//GUI for selecting a player to add to the game
	private void playerSelectGUI()
	{
		mainFrame.getContentPane().removeAll();
		JPanel playerPanel = new JPanel();
		
		if (firstTime)
		{
			Object[] arr = playerList.toArray();
			playerComboBox = new JComboBox(arr);
			playerComboBox.setPreferredSize(new Dimension(400,30));
			firstTime = false;
		}
		
		playerPanel.add(playerComboBox);
		playerPanel.add(addPlayerButton);
		playerPanel.add(cancelOptionsButton);
		mainFrame.add(playerPanel);
		
		mainFrame.validate();
		mainFrame.repaint();
		
	}
	
	private void gameGUI()
	{
		mainFrame.getContentPane().removeAll();
		mainFrame.setSize(750,500);
		JPanel gamePanel = new JPanel(new BorderLayout(5,5));
		JPanel playerAreaPanel = new JPanel(new GridLayout(5,1)); //this is going to hold player info
		JLabel playerName = new JLabel("Seat 1: No player in this seat.");
		JLabel player2Name = new JLabel("Seat 2: No player in this seat.");
		JLabel player3Name = new JLabel("Seat 3: No player in this seat.");
		JLabel player4Name = new JLabel("Seat 4: No player in this seat.");
		JLabel player5Name = new JLabel("Seat 5: No player in this seat.");
		Game inProgressGame = (Game) gameList.get(inProgressIndex);
		
		try
		{
			playerName.setText("Seat 1: " + (inProgressGame.getPlayer(0)).getName());
			player2Name.setText("Seat 2: " + (inProgressGame.getPlayer(1)).getName());
			player3Name.setText("Seat 3: " + (inProgressGame.getPlayer(2)).getName());
			player4Name.setText("Seat 4: " + (inProgressGame.getPlayer(3)).getName());	
			player5Name.setText("Seat 5: " + (inProgressGame.getPlayer(4)).getName());
			
		}
		catch(IndexOutOfBoundsException e)
		{
			//it's fine, just catch it and move on
		}
		finally
		{
			playerAreaPanel.add(playerName);
			playerAreaPanel.add(player2Name);
			playerAreaPanel.add(player3Name);
			playerAreaPanel.add(player4Name);
			playerAreaPanel.add(player5Name);
		}
		
		
		JPanel roundInfoAreaPanel = new JPanel(); //this will hold ongoing game information, including bets
		transactionTextArea.setPreferredSize(new Dimension (500, 400));
		transactionTextArea.append("\n Welcome to Roulette! \n Round " + inProgressGame.getRoundCount());
		transactionTextArea.append("\n ----------------------");
		transactionTextArea.setEditable(false);
		JScrollPane myScrollPane = new JScrollPane(transactionTextArea); 
		roundInfoAreaPanel.add(myScrollPane);
		
		JPanel currentActionPanel = new JPanel(); //this will hold information on what's going on now
		currentActionPanel.add(currentActionLabel);
		
		gamePanel.add(roundInfoAreaPanel, BorderLayout.CENTER);
		gamePanel.add(playerAreaPanel, BorderLayout.WEST);
		gamePanel.add(currentActionPanel, BorderLayout.SOUTH);
		mainFrame.add(gamePanel);
		
		mainFrame.validate();
		mainFrame.repaint();
		
	}
	
	//adds listeners to everything
	private void addListeners()
	{
		quitButton.addActionListener(this);
		loadGameButton.addActionListener(this);
		
		addPlayers.addActionListener(this);
		playRound.addActionListener(this);
		switchGames.addActionListener(this);
		
		playGameButton.addActionListener(this);
		cancelButton.addActionListener(this);
		
		addPlayerButton.addActionListener(this);
		cancelOptionsButton.addActionListener(this);
		
	}
	
	
	//action performed 
	public void actionPerformed (ActionEvent evt)
	{
		Object source = evt.getSource();
			
		//select a game
		if (source == loadGameButton)
		{
			generateGameChoiceGUI();
		}
		
		//saves index of the game and opens up game options
		if (source == playGameButton)
		{
			inProgressIndex = gameNumberComboBox.getSelectedIndex();
			optionsGUI();
		}
		
		//menu option to add a player to a game
		if (source == addPlayers)
		{
			playerSelectGUI();
		}
		
		//quits out of program entirely, generating the transaction report for each game
		if (source == quitButton)
		{
			int ans = JOptionPane.showConfirmDialog(null, "Do you wish to quit the application?");		
			
			if(ans == JOptionPane.YES_OPTION)
			{
				report.transactionWriter();
				//close window
				System.exit(0);
			}
		}
		
		//closes current game instance and switches to new one
		if (source == switchGames)
		{	
			int answer = JOptionPane.showConfirmDialog(null, "Do you wish to end this game?");		
			
			if(answer == JOptionPane.YES_OPTION)
			{				
				//sets firstTime variable to "true", clears stuff
				firstTime = true;
				playRound.setEnabled(false);
				transactionTextArea.setText("");
				
				//goes to the main menu
				generateMenuGUI();
			}
		}
		
		//goes to main menu
		if(source == cancelButton)
		{
			//goes to the main menu
			generateMenuGUI();
		}
		
		//goes to pre-game options menu
		if (source == cancelOptionsButton)
		{
			optionsGUI();
		}
		
		//adds player
		if (source == addPlayerButton)
		{
			Player selectPlayer = (Player) playerComboBox.getSelectedItem();
			
			//add the player to the game
			boolean result = ((Game) gameList.get(inProgressIndex)).addPlayer(selectPlayer);
			
			if (result == false)
				JOptionPane.showMessageDialog(null, "You can only have 5 players active in a game!");
			else
			{
				JOptionPane.showMessageDialog(null, "Player added!");
				playerComboBox.removeItemAt(playerComboBox.getSelectedIndex());
				optionsGUI();
			}
			
			playRound.setEnabled(true);
		}
		
		//play a single round
		if (source == playRound)
		{
			//actual game code goes here!
			
			//creates menu
			gameGUI();
			
			//call for bets from each player	
			currentActionLabel.setText("Betting round begins!");
			((Game)gameList.get(inProgressIndex)).betRound();
			
			//spin wheel and determines results
			currentActionLabel.setText("Time to spin the wheel!");
			int color = Wheel.spin();
			transactionTextArea.append("\nThe wheel has spun and...\n");
			transactionTextArea.append("\n\tThe color is: " + Wheel.getColor());
			int ballPosition = Wheel.getposition();
			if(ballPosition == -1)
				transactionTextArea.append("\n\tThe number is: 00");
			else
				transactionTextArea.append("\n\tThe number is: " + ballPosition+"\n");
			
			//figure out results and pay out
			currentActionLabel.setText("Paying out!");
			String append = ((Game)gameList.get(inProgressIndex)).payRound();
			transactionTextArea.append(append);
			
			//ask people if they want to spin again -- remove those who say no
			currentActionLabel.setText("Another round?");
			((Game)gameList.get(inProgressIndex)).spinAgain();
			
			JOptionPane.showMessageDialog(null, "Close this window when done!");
			optionsGUI();
		}
	}

	//for the itemlistener on ComboBox
	public void itemStateChanged(ItemEvent evt)
	{
		Object source = evt.getSource();
		
		if(source == gameNumberComboBox)
		{
			if (evt.getStateChange() == ItemEvent.SELECTED)
			{
				//if one of the games is selected, we display the stats of that game
				Game currentGame = (Game) gameList.get(gameNumberComboBox.getSelectedIndex());
				descriptionTextArea.setText("Min Bet: " + currentGame.getMin());
				descriptionTextArea.append("\nMax Bet: " + currentGame.getMax());
				
			}
		}
		
	}
	
}
