package core;

import java.io.*;
import java.net.*;




public class backupClient{
	
	private static String ip = "localhost";
	private int port = 613;
	private Socket socket;
	private DataOutputStream toServer;
	private DataInputStream buffReadfromServer;
	//private DataInputStream fromServer;
	
	public String opponentName;
	public char result;
	private String serverMessage;
	public int[][] updateCoin = null;
	
	public backupClient() throws Exception {
		
		try {
			socket = new Socket(ip, port);
			System.out.println("listening on port: " + socket.getLocalPort());
			//HandleUser();
			
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
	
	/*
	public void waitForInputs() throws InterruptedException {
	    synchronized (this) {
	        // Make the current thread waits until a notify or an interrupt
	        wait();
	    }
	}
	*/
	
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
	
	/*
	public void sendColumn(int column) throws InterruptedException {
		//waitForInputs();
		sendColumn(column);
	}
	*/
	
	/*
	public void sendName(String name) throws InterruptedException {
		user.waitForInputs();
		user.sendName(name);
	}
	*/
	
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
	
	/*
	public static void main(String[] args) throws Exception {
		Connect4Client client = new Connect4Client(ip);
		client.HandleUser();
	}
	*/
}

