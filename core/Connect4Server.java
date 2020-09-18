package core;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import core.Connect4;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Connect4Server extends JFrame{
	//protected LinkedList<gameNode> gameQueue; 
	private static int PORT = 613;
	//variables for client service multithreading
	protected static final int MAX_THREADS = 20;
	protected static Queue<OpenNewGame.HandleClient> pairingQueue = new PriorityQueue<OpenNewGame.HandleClient>();
	//protected static ArrayList<HandleClient> readyQueue = new ArrayList<HandleClient>(2);
	private static ExecutorService pool = Executors.newFixedThreadPool(MAX_THREADS);
	ServerSocket serverSocket;
	
	private static final int MAX_TURNS = 43;
	private static final char player1 = 'X';
	private static final char player2 = 'O';
	protected final int p1 = 1;
	protected final int p2 = 2;
	OpenNewGame.HandleClient playerNum1;
	OpenNewGame.HandleClient playerNum2;
	//win result messages starts with "S"(Sys message)
	protected final String p1Win = "Sp1w"; 
	protected final String p2Win = "Sp2w";
	//wait for player messages starts with "S"(Sys message)
	protected final String waitForP1 = "Swfp1";
	protected final String waitForP2 = "Swfp2";
	//move result messages starts with "R"
	protected final String successMsg = "Rs";	
	protected final String winMsg = "Rw";	
	protected final String reselectMsg = "Rr";
	protected final String fullMsg = "Rf";
	protected final String boardFullMsg = "Re";
	
	
	
	public static void main(String[] args) throws Exception {
		
		//implemented swing to display server status
		JFrame frame = new JFrame();
		frame.add(new JLabel("System Message"), BorderLayout.CENTER);
		JTextArea textArea = new JTextArea();
		frame.add(new JScrollPane(textArea));
		frame.pack();
		frame.setVisible( true );
		frame.setSize(500,300);
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		
		textArea.append("Server is now running...\n");
		
		ServerSocket serverSocket = new ServerSocket(PORT);
		//boolean acceptConnections = true;
		
		//while(acceptConnections){
			
		//EstablishConnection:
		while(true) { 
			Socket playerSocket1 = serverSocket.accept();
			OpenNewGame game = new OpenNewGame();
			OpenNewGame.HandleClient client1 = game.new HandleClient(playerSocket1, player1);
			//game.HandleClient client1 = new game.HandleClient(playerSocket1,player1);
			textArea.append("Connection with player1 Port:" + playerSocket1.getPort() + " established\n");
			
			Socket playerSocket2 = serverSocket.accept();
			OpenNewGame.HandleClient client2 = game.new HandleClient(playerSocket2, player2);
			//HandleClient client2 = new HandleClient(playerSocket2,player2);
			textArea.append("Connection with player2 Port:" + playerSocket2.getPort() + " established\n");
		 
			//pairingQueue.add(client1);
			
			/*
			if(pairingQueue.size()%2 == 0 && pairingQueue.size() > 0) {
				for(int i = 0; i < readyQueue.size(); i++) {
					readyQueue.add(pairingQueue.poll());
					break EstablishConnection;
				}
			}
			*/
			
			game.setClients(client1,client2);
			client1.setOpponent(client2);
			client2.setOpponent(client1);
			game.currentPlayer = client1;
			client1.start();
			client2.start();
		}
		//}
		
			/*
	        try {
	            while (true) {
	            	//System.out.print
	            	OpenNewGame game = new OpenNewGame();
	            	HandleClient player = game.new HandleClient(playerSocket.accept(), player1);
	            	HandleClient playerO = game.new HandleClient(playerSocket.accept(), player2);
	                
	            }
	        } finally {
	        	playerSocket.close();
	        }
        
	        playerX.setOpponent(playerO);
	        playerO.setOpponent(playerX);
	        
	        game.currentPlayer = playerX;
	        playerX.start();
	        playerO.start();
	        
	        System.out.println(playerO.getPlayerName());
	        playerX.sendMessage("O" + playerO.getPlayerName());
    	}
	}	
			 */

		/*
		
		*/
	}
		
	static class OpenNewGame {
		//private HandleClient client1, client2;
		public String p1Name,p2Name;
		public HandleClient client1, client2;
		HandleClient currentPlayer;
		final char playerX = 'X';
		final char playerO = 'O'; 
		final char success = 's';
		final char win = 'w';
		final char reselect = 'r';
		final char full = 'f';
		final char boardFull = 'b';
		Connect4 board;
		int turn = 1;
		
		OpenNewGame(){
			board = new Connect4();
			board.initBoard();
		}
		
		public void setClients(HandleClient c1, HandleClient c2) {
			client1 = c1;
			client2 = c2;
		}
		
		
		public void setPlayerName(HandleClient client) {
			if(client == client1) {
				p1Name = client1.getPlayerName();
			}else if(client == client2) {
				p2Name = client2.getPlayerName();
			}
			
		}
		
		public void getOpponentName(HandleClient client) {
			p1Name = client1.getPlayerName();
		}
		
		// thread when player tries a move
	    public synchronized char legalMove(int selectedColumn, HandleClient player) {
	    	char result;
	    	int[][] position;
	    	if(turn < 43) {
		        if (player == currentPlayer && board.dropCoin(player.mark, selectedColumn) == success) {
		        	result = success;
		        	turn++;
		        	position = board.getPosition();
		           int row = position[0][0] + 1;
		           int column = position[0][1] + 1;
		        	player.currentRow = String.valueOf(row);
		        	player.currentColumn = String.valueOf(column);
		            currentPlayer = currentPlayer.opponent;
		            currentPlayer.otherPlayerMoved();
		            return result;
		        }else if(player == currentPlayer && board.dropCoin(player.mark, selectedColumn) == win) {
		        	result = win;
		        	turn = 44;
		        	position = board.getPosition();
		           int row = position[0][0] + 1;
		           int column = position[0][1] + 1;
		        	player.currentRow = String.valueOf(row);
		        	player.currentColumn = String.valueOf(column);
		            return result;
		        }else if(player == currentPlayer && board.dropCoin(player.mark, selectedColumn) == reselect) {
		        	result = reselect;
		            return result;
		        }else if(player == currentPlayer && board.dropCoin(player.mark, selectedColumn) == full) {
		        	result = full;
		            return result;
		        }
	    	}
	    	result = boardFull;
	    	return result;
	    }

		
		class HandleClient extends Thread {
			
			Socket pSocket;
			BufferedWriter serverOutput;
			BufferedReader bufferedIn;
			String pNameInput;
			String opponentName;
			String pStatus;
			int pColumnInput;
			char column = 'c';
			char playerName = 'n';
			char terminate = 't';
			char mark;
			String currentRow;
			String currentColumn;
			HandleClient opponent; 
			final char player1 = 'X';
			final char player2 = 'O';
			final char success = 's';
			final char win = 'w';
			final char reselect = 'r';
			final char full = 'f';
			final char boardFull = 'b';
			String sysMessage;
			public HandleClient(Socket socket,char mark) throws Exception {
				pSocket = socket;
				this.mark = mark;
				serverOutput = new BufferedWriter(new OutputStreamWriter(pSocket.getOutputStream()));
				bufferedIn = new BufferedReader(new InputStreamReader(pSocket.getInputStream()));
				try {
					//listen on any free port
					//ServerSocket serverSocket = new ServerSocket(PORT);
					
					System.out.println("listening on port: " + pSocket.getLocalPort());
					
					/*
					String in = bufferedIn.readLine();
					if(in.charAt(0) == playerName) {
						pNameInput = in.substring(1);
						System.out.println("Player Name Received: "+ pNameInput);
						Thread.sleep(300);
					}
					*/
					
					//BufferedWriter serverOutput = new BufferedWriter(new OutputStreamWriter(pSocket.getOutputStream()));
					//BufferedReader bufferedIn = new BufferedReader(new InputStreamReader(pSocket.getInputStream()));
					serverOutput.write("SWelcome! Waiting for opponent to connect...\r");
					serverOutput.flush();
					System.out.println("server output sent");
					
					//InetAddress inetAddress = socket.getInetAddress();
					//System.out.println("Server connected with " + inetAddress);
					
				}catch(IOException e) {
					System.out.println(e);
				//	return;
				//}finally{
					//serverOutput.close();
					//bufferedIn.close();
				}
			}
			
			public char getMark() {
	            return mark;
	        }
			
			public void otherPlayerMoved() {
				try {
					serverOutput.write("SOpponent dropped a coin, it's your turn!\r");
					serverOutput.flush();
				} catch (IOException e) {
					e.printStackTrace();
				}
	        }
			
			public void setOpponent(HandleClient opponent) {
	            this.opponent = opponent;
	        }
			/*
			public void otherPlayerMoved(int location) {
				sendMessage("SOPPONENT_MOVED ");
	            //output.println(
	             //   hasWinner() ? "DEFEAT" : boardFilledUp() ? "TIE" : "");
	        }
	        */
		
			public String getPlayerName() {
				return pNameInput;
			}
			
			public int getPlayerMove() {
				return pColumnInput;
			}
			
			/*
			public void sendMessage(String msg) {
				try {
					serverOutput.write(msg);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			*/
			
			public void sendMoveResult(int[][] coinPosition) {
				/*
				try {	
					serverOutput.write(coinPosition);
				}catch(IOException e) {
					e.printStackTrace();
					System.out.println("Error - \"setMessageOutput\"");
				}
				*/
			}
			
			public void run() {
				
				try {
					
					System.out.println("run() started");
					//BufferedWriter serverOutput = new BufferedWriter(new OutputStreamWriter(pSocket.getOutputStream()));
					//BufferedReader bufferedIn = new BufferedReader(new InputStreamReader(pSocket.getInputStream()));
					serverOutput.write("SYou are now connected with another player!\r");
					serverOutput.flush();
					//System.out.println("serverOutput sent");
					/*
					String pName = bufferedIn.readLine();
					if(pName.charAt(0) == playerName) {
						pNameInput = pName.substring(1);
						System.out.println("Player Name Received: "+ pNameInput);
						//Thread.sleep(300);
					}
					*/
					
	                // Tell the first player that it is his/her turn.]
	                if (mark == player1) {
	                	serverOutput.write("SPlayer 1's turn!\r");
	                	serverOutput.flush();
	                }else if(mark == player2) {
	                	serverOutput.write("SPlease wait for player 1's turn!\r");
	                	serverOutput.flush();
	                }
	                
					boolean runThread = true;
					
					while(runThread) {
						String pCommand = bufferedIn.readLine();
						//read data and store to the proper parameter according to the protocol
						if(pCommand.charAt(0) == column) {
							pColumnInput = Integer.parseInt(pCommand.substring(1));
							System.out.println("pColumnInput = "+ pColumnInput);
							if(legalMove(pColumnInput,this) == success) {
								serverOutput.write("SCoin successfully dropped!");
								serverOutput.flush();
								serverOutput.write("P" + this.currentRow + this.currentColumn);
								serverOutput.flush();
							}
							//runThread = false;
						}else if(pCommand.charAt(0) == playerName) {
							pNameInput = pCommand.substring(1);
							
							System.out.println("Player Name Received: "+ pNameInput);
							//runThread = false;
						}else if(pCommand.charAt(0) == terminate) {
							runThread = false;
						}
					/*
					while (true) {
	                    String command = input.readLine();
	                    if (command.startsWith("MOVE")) {
	                        int location = Integer.parseInt(command.substring(5));
	                        if (legalMove(location, this)) {
	                            output.println("VALID_MOVE");
	                            output.println(hasWinner() ? "VICTORY"
	                                         : boardFilledUp() ? "TIE"
	                                         : "");
	                        } else {
	                            output.println("MESSAGE ?");
	                        }
	                    } else if (command.startsWith("QUIT")) {
	                        return;
	                    }
	                }
					*/
						 
					}
				}catch(IOException e) {
					e.printStackTrace();
				}finally { 
		            //try { 
		            	//playerInput.close(); 
		            	//serverOutput.close();
		            	//bufferedIn.close();
		            	//pOOS.close(); 
		            	//pSocket.close(); 
						sysMessage = "A client stopped";
		             //} catch(IOException ioe) { 
		             //   ioe.printStackTrace(); 
		             //} 
		        } 
		
			}
		}
		/*
		public static void main(String[] args) throws InterruptedException {
			
			client1.wait();
			p2Name = client2.getPlayerName();
			client1.sendMessage("O" + p2Name);
			client2.sendMessage("O" + p1Name);
			
			
		}
		*/
		/*
		final char success = 's';
		final char win = 'w';
		final char unavailable = 'u';
		final char reselect = 'r';
		final char full = 'f';
		int turn = 1;
		char result;
		int coinPosition[][];
		char[][] board;

		HandleClient currentPlayer;
		Connect4 game = new Connect4();
		
		private char[][] init() {
			return game.initBoard();
		}
		
		private char checkResult() {
			return game.dropCoin(currentPlayer.getMark(), currentPlayer.getPlayerMove());
		}
		
		private int[][] getPosition() {
			return game.getPosition();
		}
		
		*/
		
		//p1.waitForInputs();
		//p2.waitForInputs();
		/*
		String p1Name = "O" + p1.getName();
		String p2Name = "O" + p2.getName();
		System.out.println("Name: " + p1Name);
		p1.sendMessage(p2Name + "\n");
		p2.sendMessage(p1Name + "\n");
		*/
		
		/*
		public synchronized boolean (int column, HandleClient player) {
			board = init();
		    if (checkResult() == success) {
		    	coinPosition = getPosition();
		        currentPlayer = currentPlayer.opponent;
		        currentPlayer.otherPlayerMoved(column);
		        return true;
		    }
		    return false;
		}
		*/

	}
	
	

	
}
