package core;

/**
 * 
 * Connect 4 logic for player against player  
 * 
 * @author jessica.lee
 * @version 1.2 (UPDATE for isFull())
 */

public class Connect4{
	
	private static final int ROWS = 6;
	private static final int COLUMNS = 7;
	private static final char space = ' ';	
	public static final char SUCCESSFUL = 's';
	public static final char WIN ='w';
	public static final char FULL ='f';
	public static final char RESELECT ='r';

	public char[][] board = new char[ROWS][COLUMNS];
	public int[][] pPosition = new int[1][2];
		
	
	/**
	 * 
	 * Initialize a blank board
	 * 
	 * @return char[][], board
	 */
	public char[][] initBoard() {
		
		for(int r = 0; r < ROWS; r++) {
			for(int c = 0; c < COLUMNS; c++) {
				board[r][c] = space;
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
	 * SUCCESSFUL(s) = drop successful
	 * RESELECT(r) = drop failed, re-select a column
	 * FULL(f) = board is full, restart the game
	 * WIN(w) = player wins
	 * 
	 * @param player, column
	 * @return char
	 * 
	 */
	public char dropCoin(char player, int column){
		//for indexing
		int col = column - 1;
		int row = ROWS - 1;
		int fullColsCount = 0;
		int[] fullCols = new int[7];
		char result;
		
		do{
			if(board[row][col] == space) {
				board[row][col] = player;
				pPosition[0][0] = row;
				pPosition[0][1] = col;
				if(checkForWinner(board)) {
					result = WIN;
					return result; 
				}else {
					break;
				}
			}else if(row == 0 && board[row][col] != space){
	
				if(contains(fullCols,col)) {
					System.out.println("Column full, please select another column!");
					result = RESELECT;
					return result;
				}else {
					fullCols[fullColsCount] = col;
					fullColsCount++;
					System.out.println("Column full, please select another column!");
					result = RESELECT;
					return result;
				}	
			}else
				row--;
		}while(row > -1); 
		result = SUCCESSFUL;
		System.out.println("Your coin is successfully dropped!");
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
	            if(play == space)
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
	            if(play == space)
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
	            if(play == space)
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
	            if(play == space)
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
			if(board[0][i] == space) {
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
		
		for(int r = 0; r < ROWS; r++) {	
			for(int c = 0; c < COLUMNS; c++) {
				if(c != 6) {
					System.out.print("|" + board[r][c]);
				}else {
					System.out.println("|" + board[r][c] + "|");
				}
			}	
		}
		
		
	}
	

}