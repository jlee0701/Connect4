package core;

import java.io.*;
import java.net.*;

import javax.swing.JFrame;

import javafx.application.Application;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Path;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.text.Font; 
import javafx.scene.text.FontPosture; 
import javafx.scene.text.FontWeight; 
import javafx.scene.text.Text; 
//game packages
import core.Connect4;
import core.Connect4ComputerPlayer;
import core.BackupClientTwo;




public class BackupClientTwo extends Application{
	
	private static String ip = "localhost";
	private int port = 613;
	private Socket socket;
	private DataOutputStream toServer;
	private DataInputStream buffReadfromServer;
	//private DataInputStream fromServer;
	
	public String opponentName;
	public char cresult;
	private String serverMessage;
	public int[][] updateCoin = null;
	
	
	class Client implements Runnable {
		public void run() {
			try {
				socket = new Socket(ip, port);
				System.out.println("listening on port: " + socket.getLocalPort());
				
				try {
					HandleUser();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}catch(IOException e) {
				System.out.println(e);
			}
		}
		
		public void HandleUser() throws Exception{
			final char oName = 'O';
			final char sysMessage = 'S';
			final char moveResult = 'R'; 
			
			try {
				buffReadfromServer = new DataInputStream(socket.getInputStream());
				toServer = new DataOutputStream(socket.getOutputStream());
				boolean runThread = true;
				
				while(runThread) {
					String protocol = buffReadfromServer.readLine();
					if(protocol.charAt(0) == sysMessage && protocol instanceof String) {
						//String serverIn = (String) buffReadfromServer.readObject().
						serverMessage = protocol.substring(1);
						
					}else if(protocol.charAt(0) == moveResult && protocol instanceof String) {
						result = protocol.charAt(1);
					}else if(!(protocol instanceof String) && protocol != null) {
						//ObjectInputStream ois = new ObjectInputStream(buffReadfromServer);
						//updateCoin = (int[][])ois.readObject();
					}else if(protocol.charAt(0) == oName && protocol instanceof String) {
						opponentName = protocol.substring(1);
					}
				}
			}catch (IOException e) {
				e.printStackTrace();
			}catch (Exception e) {
				e.printStackTrace();
			}
			/*
			finally { 
	            try { 
	            	buffReadfromServer.close(); 
	            	toServer.close();
	            	socket.close();
	                System.out.println("A client stopped"); 
	             } catch(IOException ioe) { 
	                ioe.printStackTrace(); 
	             } 
	        } 
	        */
		}
		
		public void sendColumn(int column) {
			try {
				toServer.writeChars("c" + Integer.toString(column));
				toServer.flush();
			}catch(IOException e) {
				e.printStackTrace();
				System.out.println("Error - IOException in 'sendColumn'!");
			}
			
		}
		
		public String getMessage() {
			return serverMessage;
		}
		
		public char getResult() {
			return result;
		}
		
		public int[][] updateBoard() throws ClassNotFoundException {
			return updateCoin;
		}
		
		public String getOpponentName() {
			return opponentName;
		}
		
		
		public void sendName(String playerName) {
			try {
				//System.out.print(playerName);
				toServer.writeChars("n" + playerName);
				//toServer.flush();
			}catch(IOException e) {
				//e.printStackTrace();
				System.out.println("Error: sendName IOException");
			}
			
		}
	}
	Thread thread = new Thread() {
	public void run() {
		private String p1Name = new String();
		private String p2Name = new String();
		private Label sysMessage = new Label();
		private final Button butCol1 = new Button("1");
		private final Button butCol2 = new Button("2");
		private final Button butCol3 = new Button("3");
		private final Button butCol4 = new Button("4");
		private final Button butCol5 = new Button("5");
		private final Button butCol6 = new Button("6");
		private final Button butCol7 = new Button("7");
		TextField player1Name = new TextField();
		TextField player2Name = new TextField();
		
		private final int ROWS = 6;
		private final int COLUMNS = 7;
		private final int MAX_TURNS = 43;

		//constants and variables for check logic
		private final char player1 = 'X';
		private final char player2 = 'O';
		private final char computer = 'O';
		private static int playerNumber;
		private final String comp = new String("Computer");
		private final char success = 's';	
		private final char win = 'w'; 
		private final char reselect = 'r';
		//private final char unavailable = 'u';
		private final char full = 'f';
		private final char boardFull = 'e';
		private static int selectedColumn;
		private static int turn;
		private static char result;
		private static int coinPosition[][];
		private static int convertPositionRow;
		private static int convertPositionCol;
		

		/**
		 * Main GUI for when the game starts,
		 * will need to choose a game mode (PvP or PvC) in order to start the game
		 * 
		 */
		public void start(Stage primaryStage) throws Exception {
			
			BorderPane mainPane = new BorderPane();
			//StackPane stack = new StackPane();
			//Group coins = new Group();
			GridPane topGrid = new GridPane();
			GridPane rightGrid = new GridPane();
			GridPane centerGrid = new GridPane();
			GridPane centerGridBut = new GridPane();
			GridPane bottomGrid = new GridPane();
			StackPane buttomSP = new StackPane();
			
			//For players to enter their names
			final Button butOK = new Button("OK");
			butOK.setPrefWidth(60);
			//For selection of different modes


			
			//GUI labels and buttons
			final Label p1NameLabel = new Label("Your Name:");
			final Label p2NameLabel = new Label("Opponent Name:");
			final Button butStart = new Button("Start");
			final Button butReset = new Button("Reset");
			
			//visual display for player coins in player marks
			Circle circle1 = new Circle(0, 0, 8);
			Circle circle2 = new Circle(0, 0, 8);


			//top grid config
			topGrid.setHgap(4);
			topGrid.setVgap(10);
			topGrid.setPrefHeight(120);
			topGrid.setAlignment(Pos.TOP_LEFT);
			primaryStage.setTitle("Connect4");

			mainPane.setTop(topGrid);
			topGrid.add(new Label("               "), 0, 0); //space padding
			topGrid.add(p1NameLabel, 1, 2);
			topGrid.add(player1Name, 4, 2);
			player1Name.setMaxWidth(200);
			topGrid.add(p2NameLabel, 1, 3);
			topGrid.add(player2Name, 4, 3);
			player2Name.setMaxWidth(200);
			topGrid.add(butOK, 4, 4);
			Label displayName = new Label(" ");
			topGrid.add(displayName,4,5,5,5);
			player1Name.setEditable(false);
			player2Name.setEditable(false);
			player2Name.setVisible(false);
			p2NameLabel.setVisible(false);
			
			topGrid.add(new Label("        "), 6, 2);
			topGrid.add(p2NameLabel, 7, 2);
			topGrid.add(player2Name, 8, 2);


			mainPane.setLeft(new Label("                              ")); //space padding
			Label nameMark1 = new Label(" ");
			Label nameMark2 = new Label(" ");


			//right grid config: player mark display
			Text pmText = new Text("Player marks:");
			pmText.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 12)); 
			rightGrid.setHgap(1);
			rightGrid.setVgap(5);
			mainPane.setRight(rightGrid);
			rightGrid.setPrefWidth(300);
			rightGrid.setAlignment(Pos.TOP_LEFT);
			rightGrid.add(new Label("                 "), 0, 0); //space padding
			rightGrid.add(pmText, 2, 15);
			rightGrid.add(circle1, 1, 20);
			circle1.setFill(Color.GREEN);    
			rightGrid.add(nameMark1, 2, 20,3,3);
			nameMark1.setWrapText(true);
			rightGrid.add(circle2, 1, 25);
			circle2.setFill(Color.RED);
			rightGrid.add(nameMark2, 2, 25,3,3);
			nameMark2.setWrapText(true);
			butStart.setPrefWidth(80);
			rightGrid.add(butStart, 2, 35);
			butReset.setPrefWidth(80);
			rightGrid.add(butReset, 2, 38);
			
			butOK.setOnAction(e -> {
					p1Name = player1Name.getText();
					p2Name = player2Name.getText();
					displayName.setText("Welcome " + p1Name + " and " + p2Name + " !");
					nameMark1.setText("   " + p1Name);
					nameMark2.setText("   " + p2Name);

					//start a client to connect to the server
					//p1Name = player1Name.getText();
					//nameMark1.setText("   " + p1Name);
				
			});

			/* 
			 * "Start" button will be enabled upon choosing a game mode,
			 * then it will be disabled to prevent from changing settings mid-game
			 */
			butStart.setOnAction(e -> {
				butStart.setDisable(true);
				butOK.setDisable(true);
				player1Name.setEditable(false);
				player2Name.setEditable(false);

				turn = 1;
				playPvP(centerGrid);
				
				butCol1.setDisable(false);
				butCol2.setDisable(false);
				butCol3.setDisable(false);
				butCol4.setDisable(false);
				butCol5.setDisable(false);
				butCol6.setDisable(false);
				butCol7.setDisable(false);

			});
			
			/*
			 * Clears the game board display, disables column buttons
			 * and enables selection for game mode and player names
			 * until the player triggers start button to start a new game
			 */
			butReset.setOnAction(e -> {
				butOK.setDisable(false);
				centerGrid.getChildren().clear(); 
				createBoard(centerGrid);

				butStart.setDisable(false);
				player1Name.setEditable(true);
				player2Name.setEditable(true);

				butCol1.setDisable(true);
				butCol2.setDisable(true);
				butCol3.setDisable(true);
				butCol4.setDisable(true);
				butCol5.setDisable(true);
				butCol6.setDisable(true);
				butCol7.setDisable(true);
				sysMessage.setText("Board cleared, you can also choose a different setting to restart.\n"
						+ "Please click 'Start' to start a new game!");

			});	


			//center grid config
			//splitpane to separating buttons and game board later for clearing the board
			SplitPane sp = new SplitPane();
			centerGrid.setAlignment(Pos.TOP_LEFT);
			//button size: 1.5em
			butCol1.setStyle("-fx-font-size: 1.5em; ");
			butCol1.setPrefWidth(50);
			butCol2.setStyle("-fx-font-size: 1.5em; ");
			butCol2.setPrefWidth(50);
			butCol3.setStyle("-fx-font-size: 1.5em; ");
			butCol3.setPrefWidth(50);
			butCol4.setStyle("-fx-font-size: 1.5em; ");
			butCol4.setPrefWidth(50);
			butCol5.setStyle("-fx-font-size: 1.5em; ");
			butCol5.setPrefWidth(50);
			butCol6.setStyle("-fx-font-size: 1.5em; ");
			butCol6.setPrefWidth(50);
			butCol7.setStyle("-fx-font-size: 1.5em; ");
			butCol7.setPrefWidth(50);

			centerGrid.setHgap(2);
			centerGrid.setVgap(2);
			centerGridBut.setHgap(4);
			mainPane.setCenter(sp);
			//sp.setPrefHeight(450);
			sp.setOrientation(Orientation.VERTICAL);
			sp.setStyle("-fx-box-border: transparent;");
			buttomSP.maxHeight(100);
			buttomSP.getChildren().add(centerGrid);
			sp.getItems().addAll(buttomSP,centerGridBut);	  
			centerGridBut.add(butCol1, 1, 0);
			centerGridBut.add(butCol2, 2, 0);
			centerGridBut.add(butCol3, 3, 0);
			centerGridBut.add(butCol4, 4, 0);
			centerGridBut.add(butCol5, 5, 0);
			centerGridBut.add(butCol6, 6, 0);
			centerGridBut.add(butCol7, 7, 0);
			createBoard(centerGrid);

			//space padding
			StackPane left = new StackPane();
			left.setPrefWidth(100);
			mainPane.setLeft(left);
			//StackPane bottom = new StackPane();
			//bottom.setPrefWidth(100);
			//mainPane.setBottom(bottom);

			//bottom pane config
			mainPane.setBottom(bottomGrid);
			bottomGrid.setAlignment(Pos.BASELINE_LEFT);
			bottomGrid.setPrefHeight(140);
			bottomGrid.setHgap(2);
			bottomGrid.setVgap(2);
			bottomGrid.add(new Label("                   "), 1, 0);
			bottomGrid.add(sysMessage, 3, 0,2,6);
			//System message display area
			sysMessage.setText("Please select Play Mode and Game Mode,\n"
					+ "Select 'Online' to connect to the server with another player,\n"
					+ "Select 'Offline' to play on your computer.\n"
					+ "Press 'Start' to play.\n"
					+ "Press 'Reset' to stop and reset the game.");
			sysMessage.setWrapText(true);


			Scene scene = new Scene(mainPane,760, 680);
			primaryStage.setScene(scene); // Place the scene in the stage
			primaryStage.show(); // Display the stage
			primaryStage.setResizable(false);
		}
		
		/**
		 * 
		 * Initial creation for the initial display of 
		 * the empty board
		 * 
		 * @param GridPane, gridpane
		 */
		private void createBoard(final GridPane gridpane){
			for(int r = 1;r <= ROWS; r++){
				for(int c = 1; c <= COLUMNS; c++){
					
					/* In order to create the visual effect of the actual
					 * game board, a smaller circle is subtracted
					 * from a rectangle to create the hollow effect,
					 * then place each individual piece in columns and rows 
					 * to mimic a 6x7 board
					 */
					Rectangle rect = new Rectangle(50,50);
					Circle circ = new Circle(21);
					circ.centerXProperty().set(25);
					circ.centerYProperty().set(25);
					Shape cell = Path.subtract(rect, circ);
					cell.setFill(Color.BLUE);
					cell.setStroke(Color.BLUE);
					cell.setOpacity(.8);

					StackPane stack = new StackPane();
					stack.getChildren().addAll(cell);
					gridpane.add(stack, c, r); 

				}
			}
		}
		
		/**
		 * starts a player vs player game with
		 * the logic from Connect4.java
		 * 
		 * @param GridPane, gridpane
		 */
		private void playPvP(final GridPane gridpane){

			Connect4 game = new Connect4();
			
			//initialize an empty logical board
			game.initBoard();
			sysMessage.setText("Players please click on a column number to drop a coin!\n"
					+ p1Name + "'s turn...");
			
			//action when the column button is clicked by the player 
			//that tried to drop a coin in that column
			butCol1.setOnAction(e -> {
				selectedColumn = 1;
				//odd turns will always be P1
				if(turn < MAX_TURNS && turn%2 != 0) { 
					result = game.dropCoin(player1, selectedColumn);
					//calls checkPvPResult to check on the game result
					//to determine if a coin can be placed
					checkPvPResult(gridpane,result,game,player1);
				//even turns will always be P1
				}else if(turn < MAX_TURNS && turn%2 == 0) {
					result = game.dropCoin(player2, selectedColumn);
					checkPvPResult(gridpane,result,game,player2);
					
				//if the game took 42 turns without a win indicator it 
				//means the space in the board is full and no one won
				}else if(turn == MAX_TURNS) {
					sysMessage.setText("Board is full, no one won :( ");
				}	
			});
			
			//same algorithm for all the column buttons
			butCol2.setOnAction(e -> {
				selectedColumn = 2;
				if(turn < MAX_TURNS && turn%2 != 0) { 
					result = game.dropCoin(player1, selectedColumn);
					checkPvPResult(gridpane,result,game,player1);
				}else if(turn < MAX_TURNS && turn%2 == 0) {
					result = game.dropCoin(player2, selectedColumn);
					checkPvPResult(gridpane,result,game,player2);
				}else if(turn == MAX_TURNS) {
					sysMessage.setText("Board is full, no one won :( ");
				}	
			});

			butCol3.setOnAction(e -> {
				selectedColumn = 3;
				if(turn < MAX_TURNS && turn%2 != 0) { 
					result = game.dropCoin(player1, selectedColumn);
					checkPvPResult(gridpane,result,game,player1);
				}else if(turn < MAX_TURNS && turn%2 == 0) {
					result = game.dropCoin(player2, selectedColumn);
					checkPvPResult(gridpane,result,game,player2);
				}else if(turn == MAX_TURNS) {
					sysMessage.setText("Board is full, no one won :( ");
				}	
			});

			butCol4.setOnAction(e -> {
				selectedColumn = 4;
				if(turn < MAX_TURNS && turn%2 != 0) { 
					result = game.dropCoin(player1, selectedColumn);
					checkPvPResult(gridpane,result,game,player1);
				}else if(turn < MAX_TURNS && turn%2 == 0) {
					result = game.dropCoin(player2, selectedColumn);
					checkPvPResult(gridpane,result,game,player2);
				}else if(turn == MAX_TURNS) {
					sysMessage.setText("Board is full, no one won :( ");
				}	
			});

			butCol5.setOnAction(e -> {
				selectedColumn = 5;
				if(turn < MAX_TURNS && turn%2 != 0) { 
					result = game.dropCoin(player1, selectedColumn);
					checkPvPResult(gridpane,result,game,player1);
				}else if(turn < MAX_TURNS && turn%2 == 0) {
					result = game.dropCoin(player2, selectedColumn);
					checkPvPResult(gridpane,result,game,player2);
				}else if(turn == MAX_TURNS) {
					sysMessage.setText("Board is full, no one won :( ");
				}	
			});

			butCol6.setOnAction(e -> {
				selectedColumn = 6;
				if(turn < MAX_TURNS && turn%2 != 0) { 
					result = game.dropCoin(player1, selectedColumn);
					checkPvPResult(gridpane,result,game,player1);
				}else if(turn < MAX_TURNS && turn%2 == 0) {
					result = game.dropCoin(player2, selectedColumn);
					checkPvPResult(gridpane,result,game,player2);
				}else if(turn == MAX_TURNS) {
					sysMessage.setText("Board is full, no one won :( ");
				}	
			});

			butCol7.setOnAction(e -> {
				selectedColumn = 7;
				if(turn < MAX_TURNS && turn%2 != 0) { 
					result = game.dropCoin(player1, selectedColumn);
					checkPvPResult(gridpane,result,game,player1);
				}else if(turn < MAX_TURNS && turn%2 == 0) {
					result = game.dropCoin(player2, selectedColumn);
					checkPvPResult(gridpane,result,game,player2);
				}else if(turn == MAX_TURNS) {
					sysMessage.setText("Board is full, no one won :( ");
				}	
			});
		}




		/**
		 * Checks the result from the last dropped coin - player vs player
		 * 
		 * @param GridPane, gridpane
		 * @param char, result
		 * @param Connect4, game
		 * @param char, player
		 */
		private void checkPvPResult(final GridPane gridpane, char checkResult, Connect4 game,char player ) {
			Circle coin = new Circle();
			String name = new String();
			String opponent = new String();
			
			/*
			 * Sets player specific parameters
			 */
			if(player == player1) {
				coin = p1Coin();
				name = p1Name;
				opponent = p2Name;

			}else if(player == player2) {
				coin = p2Coin();
				name = p2Name;
				opponent = p1Name;
			}
			
			/* if the drop is successful, create a circle to 
			 * mimic a coin to display in the game board
			 * then increment player turn and prompt the next 
			 * player's move
			 */
			if(checkResult == success) {
				coinPosition = game.getPosition();
				// +1 adjustment for array indexing
				convertPositionRow = coinPosition[0][1] + 1;
				convertPositionCol = coinPosition[0][0] + 1;

				StackPane child = new StackPane();
				child.getChildren().add(coin);
				gridpane.add(child,convertPositionRow,convertPositionCol);

				sysMessage.setText("Success!" + " " + opponent + "'s turn..");

				turn++;
				if(turn == MAX_TURNS) {
					sysMessage.setText("Board is full, no one won :( ");
				}

			}else if(checkResult == reselect || checkResult == full) {
				//prompt reselect is the column is full
				sysMessage.setText("Column full, please select another column!");
			}else if(checkResult == win){
				/* create a circle to mimic a coin to display in the game board
				 * first then display the winning message and sets player turn 
				 * to 44 to stop the game 
				 */
				coinPosition = game.getPosition();
				convertPositionRow = coinPosition[0][1] + 1;
				convertPositionCol = coinPosition[0][0] + 1;

				StackPane child = new StackPane();
				child.getChildren().add(coin);
				gridpane.add(child,convertPositionRow,convertPositionCol);

				sysMessage.setText("Player " + name + " wins!");
				turn = MAX_TURNS + 1;
				return;
			}
		}

			
		/**
		 * 
		 * Create a green coin for player 1
		 * 
		 * @return Circle, coin
		 */
		private Circle p1Coin() {

			Circle coin = new Circle(21);
			coin.centerXProperty().set(25);
			coin.centerYProperty().set(25);
			coin.setFill(Color.GREEN);

			return coin;
		}

		/**
		 * 
		 * Create a red coin for player 2
		 * 
		 * @return Circle, coin
		 */
		private Circle p2Coin() {

			Circle coin = new Circle(21);
			coin.centerXProperty().set(25);
			coin.centerYProperty().set(25);
			coin.setFill(Color.RED);

			return coin;
		}
	}
	
		
	
	
	
	
	
	
	public static void main(String[] args) throws Exception {
		launch(args);
       // Connect4Client client = new Connect4Client();
        
        
    }
	
}

