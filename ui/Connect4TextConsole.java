package ui;

/**
 * Main text console for connect 4
 * also will prompt for the choice between text console ad 
 * GUI
 * 
 * @author jessica.lee
 * @version 1.0
 */

import java.util.*;
import java.lang.NumberFormatException;
import core.Connect4;
import core.Connect4ComputerPlayer;
import ui.Connect4GUI;

public class Connect4TextConsole{

	/**
	 * Start a new game - player vs player
	 * 
	 */
	private static void playerVplayerStart() throws NumberFormatException{			
			
		final char playerX = 'X';
		final char playerO = 'O'; 
		char result;
		final char success = 's';
		final char win = 'w';
		final char reselect = 'r';
		final char full = 'f';
		
		int pSelect = 0;
		Scanner s = new Scanner(System.in);
		
		System.out.println("Player marks:");
		System.out.println("Player X = X");
		System.out.println("Player O = O");
	
		Connect4 game = new Connect4();
		
		game.initBoard();
		game.display();
	
		System.out.println("Players please select a column number from 1-7 to drop a coin!");
		
		int turn = 1;
		
		Game:
		do{
			char player;
			if(turn%2 != 0) {
				player = playerX;
				System.out.println("Player X's turn: ");
				boolean valid = false;
				
				while(!valid){
					try {
						pSelect = Integer.parseInt(s.nextLine());
						valid = true;
					}catch(NumberFormatException e) {
						System.out.println("Please enter numbers only!");
						//pSelect = Integer.parseInt(s.nextLine());
					}
				}
				

				if(pSelect < 1 || pSelect >7) {
					System.out.println("Please enter a number between 1-7!");
					
				}else {
					result = game.dropCoin(player,pSelect);
					game.display();
					if(result == success) {
						System.out.println("Your coin is successfully dropped!");
						turn++;
					}else if(result == win){
						System.out.println("Player " + player + " wins!");
						break Game;
					}else if(result == reselect || result == full) {
						System.out.println("Column full, please select another column!");
					}
				}
			}else if(turn%2 == 0) {
				player = playerO;
				System.out.println("Player O's turn: ");
				boolean valid = false;
				
				while(!valid){
					try {
						pSelect = Integer.parseInt(s.nextLine());
						valid = true;
					}catch(NumberFormatException e) {
						System.out.println("Please enter numbers only!");
						//pSelect = Integer.parseInt(s.nextLine());
					}
				}	
				
				if(pSelect < 1 || pSelect >7) {
					System.out.println("Please enter a number between 1-7!");
					
				}else {
					result = game.dropCoin(player,pSelect);
					game.display();
					if(result == success) {
						System.out.println("Your coin is successfully dropped!");
						turn++;
					}else if(result == win){
						System.out.println("Player " + player + " wins!");
						break Game;
					}else if(result == reselect || result == full) {
						System.out.println("Column full, please select another column!");
					}
				}	
			}
		}while(turn < 43);
		if (turn == 42)
			System.out.println("Board is full, no one won :(");		
	}
	
	
	/**
	 * Start a new game - player vs computer
	 * 
	 */
	private static void playerVcomputerStart() throws NumberFormatException{		
			
		final char playerX = 'X';
		char result;
		final char success = 's';
		final char win = 'w';
		final char unavailable = 'u';
		final char reselect = 'r';
		final char full = 'f';
		
		int pSelect = 0;
		Scanner s = new Scanner(System.in);
		
		System.out.println("Player marks:");
		System.out.println("Player X = X");
		System.out.println("Computer = O");
	
		Connect4ComputerPlayer game = new Connect4ComputerPlayer();
		
		game.initBoard();
		game.display();
	
		System.out.println("Players please select a column number from 1-7 to drop a coin!");
		
		int turn = 1;
		
		Game:
		do{
			char player;
			if(turn%2 != 0) {
				player = playerX;
				System.out.println("Player X's turn: ");
				boolean valid = false;
				
				while(!valid){
					try {
						pSelect = Integer.parseInt(s.nextLine());
						valid = true;
					}catch(NumberFormatException e) {
						System.out.println("Please enter numbers only!");
					}
				}
				

				if(pSelect < 1 || pSelect >7) {
					System.out.println("Please enter a number between 1-7!");
					
				}else {
					result = game.dropCoin(player,pSelect);
					//System.out.println("Test");
					game.display();
					if(result == success) {
						System.out.println("Your coin is successfully dropped!");
						turn++;
					}else if(result == win){
						System.out.println("Player " + player + " wins!");
						break Game;
					}else if(result == reselect || result == full) {
						System.out.println("Column full, please select another column!");
					}
				}
			}else if(turn%2 == 0) {
				System.out.println("Computers's turn: ");
				result = game.isWinning();
				//System.out.println(result);
				if(result == success) {
					game.display();
					turn++;
				}else if(result == win) {
					game.display();
					System.out.println("Computer wins!");
					break Game;
				}else {
					result = game.outerCircle();
					game.display();
					if(result == success) {
						turn++;
					}else if(result == win){		
						System.out.println("Computer wins!");
						break Game;
					}else if(result == unavailable) {
						System.out.println("System Error");
						break Game;
					}
				}				
			}			
		}while(turn < 43);	
		if (turn == 42)
			System.out.println("Board is full, no one won :(\n" + turn);
	}	
	
	
	/**
	 * Menu for text console
	 * 
	 */
	public static void consoleMenu() {
		int result = 1;
		char choice = 'n';
		
		while(result != 0) {
			System.out.println("Enter 'P' to start a new game against another player, 'C' to start a new game against computer"
					+ ", 'E' to exit:");					
			
			Scanner in = new Scanner(System.in);
			boolean valid = false;		
			while(!valid) {		
				try {
					choice = in.next().charAt(0);
					
					if(choice == 'p') {
						choice = 'P';
					}else if(choice == 'c') {
						choice = 'C';
					}else if(choice == 'e') {
						choice = 'E';
					}
					
					if(choice == 'P' || choice == 'C' || choice == 'E') {
						valid = true;
					}else {
						System.out.println("Please enter either 'P', 'C' or 'E'!");
					}						
				}catch(Exception e) {
					System.out.println("Please enter either 'P', 'C' or 'E'");
				}
			}
					
			switch (choice) {
				case 'P':
					System.out.println ( "Initializing a new game against another player..." );
					playerVplayerStart();		
					break;
	    		
				case 'C':

					System.out.println ( "Initializing a new game against the computer..." );
					playerVcomputerStart();		
					break;
	    	
				case 'E':
					System.out.println("Exiting the game...\n"
							+ "See you next time :)");
					System.exit(0);
			}	
		}					
	}
	
	
	/**
	 * Main menu
	 * 
	 * @param args
	 */
	public static void main(String[] args){
		int result = 1;
		System.out.println("Welcome to Connect4!");
		int choice = 0;
			
		while(result != 3) {
			System.out.println("Enter '1' to play in text console, '2' to play in GUI"
					+ ", '3' to exit:");
			boolean valid = false;
			Scanner in = new Scanner(System.in);
			
			while(!valid) {
				try {
					choice = Integer.parseInt(in.nextLine());
					valid = true;
				}catch(NumberFormatException e) {
					System.out.println("Please enter either '1', '2' or '3'!");
				}
			}
			
			switch (choice) {
				case 1:
					System.out.println ( "Initializing text console..." );
					consoleMenu();		
					break;
	    		
				case 2:

					System.out.println ( "Initializing GUI..." );
					Connect4GUI.main(null);		
					break;
	    	
				case 3:
					System.out.println("Exiting the game...\n"
							+ "See you next time :)");
					System.exit(0);
			}	
		}
		
		//System.exit(0);
	}
}
	