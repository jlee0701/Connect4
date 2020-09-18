package core;
/**
 * 
 * Connect 4 logic for player against computer
 * 
 * @author jessica.lee
 * @version 1.2 (UPDATE for isFull())
 */

public class Connect4ComputerPlayer{
	
	private static final int ROWS = 6;
	private static final int COLUMNS = 7;
	private static final char SPACE = ' ';	
	private static final char computer = 'O';
	private static final char player = 'X';
	public static final char SUCCESSFUL = 's';
	public static final char UNAVAILABLE ='u';
	public static final char WIN ='w';
	public static final char FULL ='f';
	public static final char RESELECT ='r';
	
	public static int[][] pPosition = new int[1][2];

	public char[][] board = new char[ROWS][COLUMNS];
	
	//took from dropCoin()
	private int fullColsCount = 0;
	private int[] fullCols = new int[7];
		
	
	/**
	 * 
	 * Initialize a blank board
	 * 
	 * @return char[][]
	 */
	public char[][] initBoard() {
		
		for(int r = 0; r < ROWS; r++) {
			for(int c = 0; c < COLUMNS; c++) {
				board[r][c] = SPACE;
			}				
		}
		return board;
	}

	/**
	 * Take in the column number that the player wishes to drop the coin
	 * and checks if it's available.
	 * 
	 * Returns a character to indicate if the coin is dropped successfully dropped
	 * or if the player wins the game
	 * 
	 * Notations:
	 * s = drop successful
	 * r = drop failed, re-select a column
	 * f = board is full, restart the game
	 * w = player wins
	 * 
	 * @param player, column
	 * @return char
	 * 
	 */
	public char dropCoin(char player, int column){
		//for indexing
		int col = column - 1;
		int row = ROWS - 1;

		char result;
		
		do{
			if(board[row][col] == SPACE) {
				board[row][col] = player;
				pPosition[0][0] = row;
				pPosition[0][1] = col;
				if(checkForWinner(board)) {
					result = WIN;
					return result; 
				//}else if(isFull()) {
				//	result = FULL;
				//	return result;
				}else {
					break;
				}
			}else if(row == 0 && board[row][col] != SPACE){
	
				if(contains(fullCols,col)) {		
					result = RESELECT;
					return result;
				}else {
					fullCols[fullColsCount] = col;
					fullColsCount++;
					result = RESELECT;
					return result;
				}	
			}else
				row--;
		}while(row > -1); 
		result = SUCCESSFUL;
		return result;
	}
	
	/**
	 * Checks if the column is full
	 * 
	 * @param arr
	 * @param col
	 * @return boolean
	 */
	private boolean contains(int[] arr,int col) {
		for(int i = 0; i < arr.length; i++) {
			if(arr[i] == col)
				return true;
		}
		return false;
	}
	
	
	/**
	 * 
	 * Check for 4 connected coins in the board
	 * by checking through the entire board
	 * 
	 * @param board
	 * @return boolean
	 */
	public static boolean checkForWinner(char[][] board) {
	    char play;
	    boolean isWin = false;
	    
	    Horizontal:
	    for(int a = 0; a < ROWS; a++) {
	        for(int b = 0; b < COLUMNS - 3; b++) {
	            play = board[a][b];
	            if(play == SPACE)
	                continue;
	            if(board[a][b + 1] == play && board[a][b + 2] == play && board[a][b + 3] == play) {
	                isWin = true;
	                break Horizontal;
	            }
	                
	        }
	    }

	    Vertical:
	    for(int x = 0; x < ROWS - 3; x++) {
	        for(int y = 0; y < COLUMNS; y++) {
	            play = board[x][y];
	            if(play == SPACE)
	                continue;
	            if(board[x + 1][y] == play && board[x + 2][y] == play && board[x + 3][y] == play) {
	                isWin = true;
	                break Vertical;
	            }
	        }
	    }

	    LeftDiagonal:
	    for(int p = 0; p < ROWS - 3; p++) {
	        for(int o = 0; o < COLUMNS - 3; o++) {
	            play = board[p][o];
	            if(play == SPACE)
	                continue;
	            if(board[p + 1][o + 1] == play && board[p + 2][o + 2] == play && board[p + 3][o + 3] == play) {
	                isWin = true;
	                break LeftDiagonal;
	            }
	        }
	    }

	    RightDiagonal:
	    for(int u = 0; u < ROWS - 3; u++) {
	        for(int i = 3; i < COLUMNS; i++) {
	            play = board[u][i];
	            if(play == SPACE)
	                continue;
	            if(board[u + 1][i - 1] == play && board[u + 2][i - 2] == play && board[u + 3][i - 3] == play) {
	                isWin = true;
	                break RightDiagonal;
	            }
	        }
	    }

	    return isWin;
	}

	/**
	 * 
	 * checks potential winning routes and block
	 * 
	 * @return char
	 */
	public char isWinning() throws IndexOutOfBoundsException{  
	    try {
		    //Horizontal:
		    for(int a = 0; a < ROWS; a++) {
		        for(int b = 0; b < COLUMNS - 3; b++) {
		        	//System.out.println("Horizontal");
		            if(board[a][b] == SPACE)
		                continue;
		            if(board[a][b] == player && board[a][b + 1] == player && board[a][b + 2] == player) {
		            	if(board[a][b + 3] == SPACE && board[a + 1][b + 3] != SPACE) {
		            		board[a][b + 3] = computer;
		            		pPosition[0][0] = a;
		    				pPosition[0][1] = b + 3;
		            		
		            		if(checkForWinner(board)) {
			            		return WIN;
			            	}else
			            		return SUCCESSFUL;
		            		
		            	}else if(board[a][b - 1] == SPACE && board[a + 1][b - 1] != SPACE) {
		            		board[a][b - 1] = computer;
		            		pPosition[0][0] = a;
		    				pPosition[0][1] = b-1;
		            		
		            		if(checkForWinner(board)) {
			            		return WIN;
			            	}else
			            		return SUCCESSFUL;
		            		
		            	}
		            }
		                
		        }
		    }
		    
		    //Vertical:
		    
		    for(int x = 0; x < ROWS - 2; x++) {
		        for(int y = 0; y < COLUMNS; y++) {
		        	//System.out.println("Vertical");
		            if(board[x][y] == SPACE)
		                continue;
		            if(board[x][y] == player && board[x + 1][y] == player && board[x + 2][y] == player && board[x - 1][y] == SPACE) {
		            	//&& board[x + 2][y] == play
		            	board[x - 1][y] = computer;
		            	pPosition[0][0] = x - 1;
	    				pPosition[0][1] = y;
		            	
		            	if(checkForWinner(board)) {
		            		return WIN;
		            	}else
		            		return SUCCESSFUL;
		            }
		        }
		    }
		    
		    
		    //LeftDiagonal -> \
		    for(int p = 0; p < ROWS - 3; p++) {
		        for(int o = 0; o < COLUMNS - 3; o++) {
		            if(board[p][o] == SPACE)
		                continue;
		            if(board[p][o] == player && board[p + 1][o + 1] == player && board[p + 2][o + 2] == player ) {
		            	if(board[p + 3][o + 3] == SPACE && board[p + 4][o + 3] != SPACE) {
		            		board[p + 3][o + 3] = computer;
		            		pPosition[0][0] = p + 3;
		    				pPosition[0][1] = o + 3;
		            		
		            		if(checkForWinner(board)) {
			            		return WIN;
			            	}else
			            		return SUCCESSFUL;           		
		            	}else if(board[p - 1][o - 1] == SPACE && board[p][o - 1] != SPACE) {
		            		board[p - 1][o - 1] = computer;
		            		pPosition[0][0] = p - 1;
		    				pPosition[0][1] = o - 1;
		            		
		            		if(checkForWinner(board)) {
			            		return WIN;
			            	}else
			            		return SUCCESSFUL;
		            	}
		            }
		        }
		    }
		    
		    //RightDiagonal ->  /
		    for(int u = 0; u < ROWS - 2; u++) {
		        for(int i = 2; i < COLUMNS; i++) {
		            if(board[u][i] == SPACE)
		                continue;
		            if(board[u][i] == player && board[u + 1][i - 1] == player && board[u + 2][i - 2] == player) {
		            	if((u+2) != ROWS-1 && board[u + 3][i - 3] == SPACE && board[u + 4][i - 3] != SPACE){
		            		board[u + 3][i - 3] = computer;
		            		pPosition[0][0] = u + 3;
		    				pPosition[0][1] = i - 3;
		    				
		            		if(checkForWinner(board)) {
			            		return WIN;
			            	}else 
			            		return SUCCESSFUL;
		            	}else if(board[u - 1][i + 1] == SPACE && board[u][i + 1] != SPACE){
		            		board[u - 1][i + 1] = computer;
		            		pPosition[0][0] = u - 1;
		    				pPosition[0][1] = i + 1;
		            		
		            		if(checkForWinner(board)) {
			            		return WIN;
			            	}else
			            		return SUCCESSFUL;
		            	}
		                
		            }
		        }
		    }
	    }catch(IndexOutOfBoundsException e) {
	    	
	    	char drop;
	    	for(int c = 1; c <= COLUMNS; c++) {
	    		drop = dropCoin(computer,c);
	    		if(drop == 'f' || drop == 'r') {	    			
	    			continue;
	    		}else {
	    			return drop;
	    		}
	    	}
	    }
    
	    return UNAVAILABLE;
	}
	
	/**
	 * 
	 * checks for available space in the outer circle of the coin last dropped 
	 * by the player, then drop a coin around it
	 * 
	 * @return char
	 */
	public char outerCircle() throws IndexOutOfBoundsException{

		int row = pPosition[0][0];
		int column = pPosition[0][1];
		
		int rowDiff = (ROWS-1) - row;
		if(rowDiff > 1)
			rowDiff = 1;
		
		int rowUpperDiff = 1;
		if(row < 2)
			rowUpperDiff = row;
		
		int colDiff = (COLUMNS-1) - column;
		if(colDiff > 1)
			colDiff = 1;
		
		int colLeftDiff = column;
		if(column > 1)
			colLeftDiff = 1;
	
		//first check the row below	
		try {
			for(int r = row+rowDiff; r > row-rowUpperDiff-1; r--) {
				for(int c = column-colLeftDiff; c < column+colDiff+1; c++) {
					if(row > ROWS-3) {	
						if(board[r][c] == SPACE) {
							board[r][c] = computer;
							pPosition[0][0] = r;
		    				pPosition[0][1] = c;
		    				
							if(checkForWinner(board)) {
								return WIN;
							}else {
								return SUCCESSFUL;
							}
						}else
							continue;
					}else if(row < ROWS-2) {					
						if(board[r][c] == SPACE && board[r+1][c] != SPACE) {
							board[r][c] = computer;
							pPosition[0][0] = r;
		    				pPosition[0][1] = c;
		    				
							if(checkForWinner(board)) {
								return WIN;
							}else {
								return SUCCESSFUL;
							}
						}else
							continue;
					}
				}
			}
		}catch(IndexOutOfBoundsException e) {
			System.out.println("throws exception 1");
			
		}catch(Exception e) {
			
			System.out.println("throws general exception");
		}
		
		char drop;
    	
		for(int c = 1; c <= COLUMNS; c++) {
    		drop = dropCoin(computer,c);
    		if(drop == 'f' || drop == 'r') {	    			
    			continue;
    		}else {
    			return drop;
    		}
    	}
    	
		return UNAVAILABLE;
	}
			
	
	/**
	 * 
	 * Check if the first row of the board is full,
	 * if the first row is full means the entire board is full
	 * based on the game logic 
	 * 
	 * UPDATE: Eliminated from use for the new version because of 
	 * double checking
	 * 
	 * @return boolean
	 * 
	 */
	private boolean isFull() {

		for(int i = 0; i < COLUMNS; i++) {
			if(board[0][i] == SPACE) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * Get the last dropped coin's position
	 * 
	 * @return int[][],pPositin
	 */
	public int[][] getPosition() {
		return pPosition;
	}

	
	/**
	 * 
	 * Display the game board in text form
	 * 
	 */
	public void display() {
		
		System.out.println("");
		for(int r = 0; r < ROWS; r++) {	
			for(int c = 0; c < COLUMNS; c++) {
				if(c != 6) {
					
					System.out.print("|" + board[r][c]);
				}else {
					System.out.println("|" + board[r][c] + "|");
				}
			}	
		}
		System.out.println("");
	}
	

}