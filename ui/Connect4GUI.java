package ui;

/**
 * GUI for connect 4.
 * Simulates a connect 4 game board and player coins
 * 
 * @author jessica.lee
 * @version 1.2 - update for server connection
 * 
 */

import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
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

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

//game packages
import core.Connect4;
import core.Connect4ComputerPlayer;
import core.Connect4Client;

public class Connect4GUI extends Application{
	//private static Connect4Client client = new Connect4Client();
	//buttons and labels for use between different methods
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
	private static String serverMessage;
	
	private Connect4Client client;
	private String cmpString;
	private String newMsg;
	

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
		final RadioButton checkPvP = new RadioButton("Player vs Player");
		final RadioButton checkPvC = new RadioButton("Player vs Computer");
		final ToggleGroup playerModeToggleGroup = new ToggleGroup();
		//toggleGroup makes sure only 1 button can be checked at a time
		checkPvP.setToggleGroup(playerModeToggleGroup);
		checkPvC.setToggleGroup(playerModeToggleGroup);
		final RadioButton checkOnline = new RadioButton("Online");
		final RadioButton checkOffline = new RadioButton("Offline");
		final ToggleGroup gameModeToggleGroup = new ToggleGroup();
		checkOnline.setToggleGroup(gameModeToggleGroup);
		checkOffline.setToggleGroup(gameModeToggleGroup);
		
		//GUI labels and buttons
		final Label p1NameLabel = new Label("P1 Name:");
		final Label p2NameLabel = new Label("P2 Name:");
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
		topGrid.add(new Label("Play Mode: "), 7, 2);
		topGrid.add(checkPvP, 8, 2);
		topGrid.add(checkPvC, 9, 2);
		topGrid.add(new Label("Game Mode:   "), 7, 3);
		topGrid.add(checkOnline, 8, 3);
		topGrid.add(checkOffline, 9, 3);

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
		
		checkOnline.setOnAction(e -> {
			player2Name.clear();
			player2Name.setEditable(false);
			p1NameLabel.setText("Your Name:");
			p2NameLabel.setText("Opponent:");
			
		});
		
		checkOffline.setOnAction(e -> {
			player2Name.clear();
			player2Name.setEditable(true);
			p1NameLabel.setText("P1 Name:");
			p2NameLabel.setText("P2 Name:");
		});
		
		
		
		butOK.setOnAction(e -> {
			if(playerModeToggleGroup.getSelectedToggle() == checkPvP && gameModeToggleGroup.getSelectedToggle() == checkOffline) {
				p1Name = player1Name.getText();
				p2Name = player2Name.getText();
				displayName.setText("Welcome " + p1Name + " and " + p2Name + " !");
				nameMark1.setText("   " + p1Name);
				nameMark2.setText("   " + p2Name);
			}else if(playerModeToggleGroup.getSelectedToggle() == checkPvP && gameModeToggleGroup.getSelectedToggle() == checkOnline) {
				//start a client to connect to the server
				p1Name = player1Name.getText();
				nameMark1.setText("   " + p1Name);
			}else if(playerModeToggleGroup.getSelectedToggle() == checkPvC){
				p1Name = player1Name.getText();
				displayName.setText("Welcome " + p1Name + "!");
				nameMark1.setText("   " + p1Name);
			}
			
			
		});

		/* 
		 * "Start" button will be enabled upon choosing a game mode,
		 * then it will be disabled to prevent from changing settings mid-game
		 */
		butStart.setOnAction(e -> {
			butStart.setDisable(true);
			butOK.setDisable(true);
			if(playerModeToggleGroup.getSelectedToggle() == checkPvP && gameModeToggleGroup.getSelectedToggle() == checkOffline) {
				player1Name.setEditable(false);
				player2Name.setEditable(false);

				turn = 1;
				playPvPOffline(centerGrid);
			}else if(playerModeToggleGroup.getSelectedToggle() == checkPvP && gameModeToggleGroup.getSelectedToggle() == checkOnline) {
				//start a client to connect to the server in the background thread
				try {
					startTask(centerGrid);
				} catch (Exception e1) {
					System.out.println("stuck at start button");
					e1.printStackTrace();
				}
				
			}else if(playerModeToggleGroup.getSelectedToggle() == checkPvC){
				player2Name.setEditable(false);
				player1Name.setEditable(false);
				turn = 1;
				playPvC(centerGrid);
			}
			butCol1.setDisable(false);
			butCol2.setDisable(false);
			butCol3.setDisable(false);
			butCol4.setDisable(false);
			butCol5.setDisable(false);
			butCol6.setDisable(false);
			butCol7.setDisable(false);
			checkPvP.setDisable(true);
			checkPvC.setDisable(true);

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

			checkPvP.setDisable(false);
			checkPvC.setDisable(false);

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

		//actions upon selecting PvP
		checkPvP.setOnAction(e -> {
			checkOnline.setDisable(false);
			checkOffline.setDisable(false);
			nameMark1.setText("   ");
			nameMark2.setText("   ");
			p2NameLabel.setVisible(true);
			player1Name.setEditable(true);
			player2Name.setEditable(true);
			player2Name.setVisible(true);

		});

		//actions upon selecting PvC
		checkPvC.setOnAction(e -> {
			p1NameLabel.setText("P1 Name:");
			gameModeToggleGroup.selectToggle(checkOffline); 
			checkOnline.setDisable(true);
			checkOffline.setDisable(true);
			nameMark1.setText("   ");
			nameMark2.setText("   Computer");
			p2NameLabel.setVisible(false);
			player1Name.setEditable(true);
			player2Name.setEditable(false);
			player2Name.setVisible(false);
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
		
		primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
		    @Override
		    public void handle(WindowEvent t) {
		        Platform.exit();
		        System.exit(0);
		    }
		});

	}
	
	public void startTask(final GridPane gridpane) 
    {
        // Create a Runnable
        Runnable task = new Runnable()
        {
            public void run()
            {
            	try {
					playPvPOnline(gridpane);
				} catch (Exception e) {
					e.printStackTrace();
				}
            }
        };
 
        // Run the task in a background thread
        Thread backgroundThread = new Thread(task);
        // Terminate the running thread if the application exits
        backgroundThread.setDaemon(true);
        // Start the thread
        backgroundThread.start();
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
	 * @throws Exception 
	 */
	private void playPvPOnline(final GridPane gridpane) throws Exception{
		//initialize an empty logical board
		//game.initBoard();
		Connect4Client client = new Connect4Client();
		//get welcome and wait message
		newMsg = client.getMessage();
		displayMsg(newMsg);
		
		//get connected message
		newMsg = client.getMessage();
		//client.sendPlayerName(p1Name);
		displayMsg(newMsg);
		
		//get play message
		newMsg = client.getMessage();
		displayMsg(newMsg);
				
		butCol1.setOnAction(e -> {
			selectedColumn = 1;
			
			newMsg = client.getMessage();
			displayMsg(newMsg);
			ExecutorService butExecutor = Executors.newSingleThreadExecutor();
			try {
				butExecutor.submit(new Runnable() {
					@Override
					public void run() {
							try {
								Thread.sleep(300);
							} catch (InterruptedException e) {
							}
							Platform.runLater(new Runnable() {
								@Override
								public void run() {
									client.setColumn(selectedColumn);
									//sysMessage.setText(serverMessage);
								}
							});
					}
				});
				butExecutor.shutdown();
			}catch(Exception ex) {
				ex.printStackTrace();
			}
			newMsg = client.getMessage();
			displayMsg(newMsg);
		});
		
		/*
		client = new Connect4Client();
		client.setPlayerName(p1Name);
		client.start();
		client.sleep(500);
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				try {
					serverMessage = client.getMessage();
					sysMessage.setText(serverMessage);
					cmpString = serverMessage;
				}catch(Exception e) {
					e.printStackTrace();
				}
			}
		});
				
		
		ExecutorService statusExecutor = Executors.newSingleThreadExecutor();
		try {
			statusExecutor.submit(new Runnable() {
				@Override
				public void run() {
					while(noUpdate(serverMessage)) {
						try {
							Thread.sleep(300);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						Platform.runLater(new Runnable() {
							@Override
							public void run() {
								serverMessage = client.getMessage();
								sysMessage.setText(serverMessage);
							}
						});
					}
				}
			});
			statusExecutor.shutdown();
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		butCol1.setOnAction(e -> {
			client.sleep(500);
			selectedColumn = 1;
			

			ExecutorService butExecutor = Executors.newSingleThreadExecutor();
			try {
				butExecutor.submit(new Runnable() {
					@Override
					public void run() {
							Thread.sleep(300);
							Platform.runLater(new Runnable() {
								@Override
								public void run() {
									client.setColumn(selectedColumn);
									sysMessage.setText(serverMessage);
								}
							});
						}
					}
				});
				statusExecutor.shutdown();
			}catch(Exception e) {
				e.printStackTrace();
			}
			*/
			
			//odd turns will always be P1
			//if(turn < MAX_TURNS && turn%2 != 0) { 
			//	result = game.dropCoin(player1, selectedColumn);
				//calls checkPvPResult to check on the game result
				//to determine if a coin can be placed
			//	checkPvPResult(gridpane,result,game,player1);
			//even turns will always be P1
			//}else if(turn < MAX_TURNS && turn%2 == 0) {
			//	result = game.dropCoin(player2, selectedColumn);
			//	checkPvPResult(gridpane,result,game,player2);
				
			//if the game took 42 turns without a win indicator it 
			//means the space in the board is full and no one won
		//	}else if(turn == MAX_TURNS) {
			//	sysMessage.setText("Board is full, no one won :( ");
		//	}	
		//});
	
		
		
		
		/*
		Task<Void> guiSleeper = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                try {
                	puts GUI to sleep to process client thread
                    Thread.sleep(500);  
                } catch (InterruptedException e) {
                }
                return null;
            }
        }; 
        */
        
        
		//guiSleeper.setOnSucceeded(new EventHandler<WorkerStateEvent>() {
		//	@Override
		//	public void handle(WorkerStateEvent event) {
				//client.start();
				//System.out.println("client started");
				//sysMessage.setText(client.getMessage());
		//	}
		//});
		//new Thread(guiSleeper).start();
		//Thread.currentThread().interrupt();
		
		//sysMessage.setText("Players please click on a column number to drop a coin!");
		//sysMessage.setText(serverMessage);
		
		//action when the column button is clicked by the player 
		//that tried to drop a coin in that column
		
		
		
		/*
		if(playerNumber) {
			
		}
		
		butCol1.setOnAction(e -> {
			selectedColumn = 1;
			client.sendColumn(selectedColumn);
			
			
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
		*/
	}
	
	
	private boolean noUpdate(String msg) {
		if(msg.equals(cmpString)) 
			return true;
		return false;
	}
	
	private void displayMsg(String msg) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				try {
					sysMessage.setText(msg);
				}catch(Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	private void sendCommand(String msg) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				try {
					sysMessage.setText(msg);
				}catch(Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * starts a player vs player game with
	 * the logic from Connect4.java
	 * 
	 * @param GridPane, gridpane
	 */
	private void playPvPOffline(final GridPane gridpane){

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
	 * 
	 * Play player vs computer game and decide the computer's moves
	 * based on Connect4ComputerPlayer.java algorithm
	 * logic is similar to playPvP with slight modification
	 * to computer's move
	 * 
	 * @param GridPane, gridpane
	 */
	private void playPvC(final GridPane gridpane){

		Connect4ComputerPlayer game = new Connect4ComputerPlayer();

		game.initBoard();
		sysMessage.setText("Player please click on a column number to drop a coin!\n"
				+ p1Name + "'s turn...");

		butCol1.setOnAction(e -> {
			selectedColumn = 1;
			result = game.dropCoin(player1, selectedColumn);
			checkPvCResult(gridpane,result,game);
			if(result == success && turn%2 == 0 && turn < MAX_TURNS) {
				/*if P1 successfully dropped a coin, system calls
				 * isWinning() from Connect4ComputerPlayer.java first
				 * to check if the player has a winning route to block
				 */
				result = game.isWinning();
				checkPvCResult(gridpane,result,game);
				if(result != success && turn%2 == 0 && turn < MAX_TURNS) {
					/*
					 * Checks the result of isWnning() and if the player
					 * does not have a winning route the system proceed to
					 * call outerCircle() from Connect4ComputerPlayer.java
					 * to check if there's any space available around player's
					 * last dropped coin to place a computer coin
					 */
					result = game.outerCircle();
					checkPvCResult(gridpane,result,game);
				}else if(turn == MAX_TURNS) {
					sysMessage.setText("Board is full, no one won :( ");
				}	
			}else if(turn == MAX_TURNS) {
				sysMessage.setText("Board is full, no one won :( ");
			}	
		});
		
		//same algorithm for all the column buttons
		butCol2.setOnAction(e -> {
			selectedColumn = 2;
			result = game.dropCoin(player1, selectedColumn);
			checkPvCResult(gridpane,result,game);
			if(result == success && turn%2 == 0 && turn < MAX_TURNS) {
				result = game.isWinning();
				checkPvCResult(gridpane,result,game);
				if(result != success && turn%2 == 0 && turn < MAX_TURNS) {
					result = game.outerCircle();
					checkPvCResult(gridpane,result,game);
				}else if(turn == MAX_TURNS) {
					sysMessage.setText("Board is full, no one won :( ");
				}	
			}else if(turn == MAX_TURNS) {
				sysMessage.setText("Board is full, no one won :( ");
			}		
		});

		butCol3.setOnAction(e -> {
			selectedColumn = 3;
			result = game.dropCoin(player1, selectedColumn);
			checkPvCResult(gridpane,result,game);
			if(result == success && turn%2 == 0 && turn < MAX_TURNS) {
				result = game.isWinning();
				checkPvCResult(gridpane,result,game);
				if(result != success && turn%2 == 0 && turn < MAX_TURNS) {
					result = game.outerCircle();
					checkPvCResult(gridpane,result,game);
				}else if(turn == MAX_TURNS) {
					sysMessage.setText("Board is full, no one won :( ");
				}	
			}else if(turn == MAX_TURNS) {
				sysMessage.setText("Board is full, no one won :( ");
			}
		});

		butCol4.setOnAction(e -> {
			selectedColumn = 4;
			result = game.dropCoin(player1, selectedColumn);
			checkPvCResult(gridpane,result,game);
			if(result == success && turn%2 == 0 && turn < MAX_TURNS) {
				result = game.isWinning();
				checkPvCResult(gridpane,result,game);
				if(result != success && turn%2 == 0 && turn < MAX_TURNS) {
					result = game.outerCircle();
					checkPvCResult(gridpane,result,game);
				}else if(turn == MAX_TURNS) {
					sysMessage.setText("Board is full, no one won :( ");
				}	
			}else if(turn == MAX_TURNS) {
				sysMessage.setText("Board is full, no one won :( ");
			}
		});

		butCol5.setOnAction(e -> {
			selectedColumn = 5;
			result = game.dropCoin(player1, selectedColumn);
			checkPvCResult(gridpane,result,game);
			if(result == success && turn%2 == 0 && turn < MAX_TURNS) {
				result = game.isWinning();
				checkPvCResult(gridpane,result,game);
				if(result != success && turn%2 == 0 && turn < MAX_TURNS) {
					result = game.outerCircle();
					checkPvCResult(gridpane,result,game);
				}else if(turn == MAX_TURNS) {
					sysMessage.setText("Board is full, no one won :( ");
				}	
			}else if(turn == MAX_TURNS) {
				sysMessage.setText("Board is full, no one won :( ");
			}	
		});

		butCol6.setOnAction(e -> {
			selectedColumn = 6;
			result = game.dropCoin(player1, selectedColumn);
			checkPvCResult(gridpane,result,game);
			if(result == success && turn%2 == 0 && turn < MAX_TURNS) {
				result = game.isWinning();
				checkPvCResult(gridpane,result,game);
				if(result != success && turn%2 == 0 && turn < MAX_TURNS) {
					result = game.outerCircle();
					checkPvCResult(gridpane,result,game);
				}else if(turn == MAX_TURNS) {
					sysMessage.setText("Board is full, no one won :( ");
				}	
			}else if(turn == MAX_TURNS) {
				sysMessage.setText("Board is full, no one won :( ");
			}	
		});

		butCol7.setOnAction(e -> {
			selectedColumn = 7;
			result = game.dropCoin(player1, selectedColumn);
			checkPvCResult(gridpane,result,game);
			if(result == success && turn%2 == 0 && turn < MAX_TURNS) {
				result = game.isWinning();
				checkPvCResult(gridpane,result,game);
				if(result != success && turn%2 == 0 && turn < MAX_TURNS) {
					result = game.outerCircle();
					checkPvCResult(gridpane,result,game);
				}else if(turn == MAX_TURNS) {
					sysMessage.setText("Board is full, no one won :( ");
				}	
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
	 * Checks the result of player's move then decide 
	 * computer's move based on it
	 * 
	 * @param GridPane, gridpane
	 * @param char, checkResult
	 * @param Connect4ComputerPlayer, game
	 */
	private void checkPvCResult(final GridPane gridpane, char checkResult, Connect4ComputerPlayer game) {
		Circle coin = new Circle();
		String name = new String();
		String opponent = new String();
		char player;
		
		/*
		 * Basic checking logic is the same as PvP check with
		 * slight modification to P2 to remove prompt for reselect
		 * since computer will automatically checks and choose
		 * an available space
		 */
		if(turn < MAX_TURNS && turn%2 != 0) {
			player = player1;
			coin = p1Coin();
			name = p1Name;
			opponent = comp;

			if(checkResult == success) {
				coinPosition = game.getPosition();
				convertPositionRow = coinPosition[0][1] + 1;
				convertPositionCol = coinPosition[0][0] + 1;

				StackPane child = new StackPane();
				child.getChildren().add(coin);
				gridpane.add(child,convertPositionRow,convertPositionCol);

				sysMessage.setText("Success!" + " " + opponent + "'s turn..");

				turn++;

			}else if(checkResult == reselect) {
				sysMessage.setText("Column full, please select another column!");
			}else if(checkResult == win){
				coinPosition = game.getPosition();
				convertPositionRow = coinPosition[0][1] + 1;
				convertPositionCol = coinPosition[0][0] + 1;

				StackPane child = new StackPane();
				child.getChildren().add(coin);
				gridpane.add(child,convertPositionRow,convertPositionCol);

				sysMessage.setText("Player " + name + " wins!");
				turn = MAX_TURNS + 1;
			}else if(turn == MAX_TURNS) {
				sysMessage.setText("Board is full, no one won :( ");
			}	

		}else if(turn < MAX_TURNS && turn%2 == 0) {
			player = computer;
			coin = p2Coin();
			name = comp;
			opponent = p1Name;

			if(checkResult == success) {
				coinPosition = game.getPosition();
				convertPositionRow = coinPosition[0][1] + 1;
				convertPositionCol = coinPosition[0][0] + 1;

				StackPane child = new StackPane();
				child.getChildren().add(coin);
				gridpane.add(child,convertPositionRow,convertPositionCol);

				sysMessage.setText("Success!" + " " + opponent + "'s turn..");

				turn++;
			}else if(checkResult == win){
				coinPosition = game.getPosition();
				convertPositionRow = coinPosition[0][1] + 1;
				convertPositionCol = coinPosition[0][0] + 1;

				StackPane child = new StackPane();
				child.getChildren().add(coin);
				gridpane.add(child,convertPositionRow,convertPositionCol);

				sysMessage.setText("Computer wins!");
				turn = MAX_TURNS + 1;
			}else if(turn == MAX_TURNS) {
				sysMessage.setText("Board is full, no one won :( ");
			}
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
	

	/**
	 * The main method
	 */
	public static void main(String[] args) {
		launch(args);
	}
}