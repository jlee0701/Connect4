package core;

import java.io.*;
import java.net.*;
import java.util.*;
import core.Connect4;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class BackupServer {
	//protected LinkedList<gameNode> gameQueue; 
	protected Queue<HandlePvPClient> clientQueue = new PriorityQueue<HandlePvPClient>();
	protected final int MAX_TURNS = 43;
	protected final char player1 = 'X';
	protected final char player2 = 'O';
	protected final int p1 = 1;
	protected final int p2 = 2;
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
	
	private static int PORT = 613;
	
	 public static void main(String args[]) 
		        throws Exception 
		    { 
		  
		        // Create server Socket 
		        ServerSocket serverSocket = new ServerSocket(PORT); 
		  
		        // connect it to client socket 
		        Socket s = ss.accept(); 
		        System.out.println("Connection established"); 
		  
		        // to send data to the client 
		        PrintStream ps 
		            = new PrintStream(s.getOutputStream()); 
		  
		        // to read data coming from the client 
		        BufferedReader br 
		            = new BufferedReader( 
		                new InputStreamReader( 
		                    s.getInputStream())); 
		  
		        // to read data from the keyboard 
		        BufferedReader kb 
		            = new BufferedReader( 
		                new InputStreamReader(System.in)); 
		  
		        // server executes continuously 
		        while (true) { 
		  
		            String str, str1; 
		  
		            // repeat as long as the client 
		            // does not send a null string 
		  
		            // read from client 
		            while ((str = br.readLine()) != null) { 
		                System.out.println(str); 
		                str1 = kb.readLine(); 
		  
		                // send to client 
		                ps.println(str1); 
		            } 
		  
		            // close connection 
		            ps.close(); 
		            br.close(); 
		            kb.close(); 
		            ss.close(); 
		            s.close(); 
		  
		            // terminate application 
		            System.exit(0); 
		  
		        } // end of while 
		    } 
		} 