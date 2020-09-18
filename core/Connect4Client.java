package core;

import java.io.*;
import java.net.*;


//has to extend thread for GUI
public class Connect4Client{
	
	private static final String ip = "localhost";
	private static final int port = 613;
	private Socket ClientSocket;
	private BufferedWriter toServer;
	private BufferedReader fromServer;
	//private DataInputStream fromServer;
	
	public String playerName = "nPLAYER NAME NOT RECEIVED";
	public String opponentName;
	public char result;
	public String serverMessage;
	public int[][] updateCoin = null;
	public String selectedColumn;
	
	private final char oName = 'O';
	private final char sysMessage = 'S';
	private final char moveResult = 'R'; 
	private final char playerPosition = 'R';
	
	public void setColumn(int column) {
		selectedColumn = "c" + String.valueOf(column) + "\r";
		try {
			toServer.write(selectedColumn);
			System.out.println(selectedColumn);
			toServer.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	public void sendPlayerName(String name) {
		try {
			toServer.write("n" + name);
			toServer.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public String getMessage() {
		try {
			boolean receive = true;
			String protocol = fromServer.readLine();
			while(receive) {
				if(protocol.charAt(0) == sysMessage && protocol instanceof String) {
					serverMessage = protocol.substring(1);
					System.out.println(serverMessage);
					//Thread.currentThread();
					//Thread.sleep(300);
					receive = false;
				}else if(protocol.charAt(0) == moveResult && protocol instanceof String) {
					serverMessage = protocol.substring(1);
					receive = false;
					//Thread.sleep(300);
				}else if(protocol.charAt(0) == playerPosition) {
					serverMessage = protocol;
					receive = false;
				}
			}
		}catch(IOException e) {
			e.printStackTrace();
		}
		return serverMessage; 
	}
	
	public void sendName() {
		try {
			toServer.write("n" + playerName);
			toServer.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
	//public void sendName() {
		
		//final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
		
		/*
		while(runThread) {
			String protocol = fromServer.readLine();
			System.out.println(protocol);
			if(protocol.charAt(0) == sysMessage && protocol instanceof String) {
				serverMessage = protocol.substring(1);
				System.out.println(serverMessage);
				runThread = false;
			}else if(protocol.charAt(0) == moveResult && protocol instanceof String) {
				result = protocol.charAt(1);
				runThread = false;
			}else if(!(protocol instanceof String) && protocol != null) {
				updateCoin = (int[][])ois.readObject();
			}
		}
		
		ScheduledFuture<?> countdown = scheduler.schedule(new Runnable() {
            @Override
            public void run() {
                // do the thing
                System.out.println("Out of time!");
        }}, 1, TimeUnit.SECONDS);
        
        */
			
	
	//}
	
	
	public Connect4Client() {
		boolean runThread = true;
		
		try {
			ClientSocket = new Socket(ip, port);
			System.out.println("listening on port: " + ClientSocket.getLocalPort());
			toServer = new BufferedWriter(new OutputStreamWriter(ClientSocket.getOutputStream()));
			System.out.println("BufferedWriter created");
			fromServer = new BufferedReader(new InputStreamReader(ClientSocket.getInputStream()));
			System.out.println("BufferedReader created");
			
			/*
			String protocol;
			protocol = fromServer.readLine();
			if(protocol.charAt(0) == sysMessage && protocol instanceof String) {
				serverMessage = protocol.substring(1);
				System.out.println(serverMessage);
				Thread.sleep(300);
			}
			
			 
			while(runThread) {
				protocol = fromServer.readLine();
				if(protocol.charAt(0) == sysMessage && protocol instanceof String) {
					serverMessage = protocol.substring(1);
					System.out.println(serverMessage);
					//Thread.currentThread();
					Thread.sleep(300);
					//runThread = false;
				}else if(protocol.charAt(0) == moveResult && protocol instanceof String) {
					result = protocol.charAt(1);
					//runThread = false;
					Thread.sleep(300);
				}else if(!(protocol instanceof String) && protocol != null) {
					//updateCoin = (int[][])ois.readObject();
				}
			}
			
			*/
		}catch(IOException e) {
			System.out.println(e);	
		}
		
		
	}
	
	//public static void main(String[] args){
		
		//Connect4Client player = new Connect4Client();
		

		
		
		/*
		try {
			buffReadfromServer = new BufferedReader(new InputStreamReader(ClientSocket.getInputStream()));
			toServer = new BufferedWriter(new OutputStreamWriter(ClientSocket.getOutputStream()));
			boolean runThread = false;
			String response = buffReadfromServer.readLine();
			System.out.println("response is: " + response + "\n");
			
			if(response.charAt(0) == oName && response instanceof String) {
				opponentName = response.substring(1);
				System.out.println("no name received");
				runThread = true;
			}
			
			
			while(runThread) {
				String protocol = buffReadfromServer.readLine();
				System.out.println(protocol);
				if(protocol.charAt(0) == sysMessage && protocol instanceof String) {
					String serverIn = (String) buffReadfromServer.readObject().
					serverMessage = protocol.substring(1);
					System.out.println(serverMessage);
					runThread = false;
				}else if(protocol.charAt(0) == moveResult && protocol instanceof String) {
					result = protocol.charAt(1);
					runThread = false;
				}else if(!(protocol instanceof String) && protocol != null) {
					ObjectInputStream ois = new ObjectInputStream(buffReadfromServer);
					updateCoin = (int[][])ois.readObject();
				}
			}
		}catch (IOException e) {
			e.printStackTrace();
		}catch (Exception e) {
			e.printStackTrace();
		}
		finally { 
            try { 
            	buffReadfromServer.close(); 
            	toServer.close();
            	ClientSocket.close();
                System.out.println("client finally cause"); 
             } catch(IOException ioe) { 
                ioe.printStackTrace(); 
             } 
        } 
        */
	        
	//}
}
		
		
	


