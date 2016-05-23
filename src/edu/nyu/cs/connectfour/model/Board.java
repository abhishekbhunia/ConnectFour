package edu.nyu.cs.connectfour.model;

import edu.nyu.cs.connectfour.GameConfig;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author abhishek bhunia
 *
 */

public class Board{  
  

  
  private int[] chipsInColumn;
  private int[][] boardState;
  private List<Point> allMoves;
  private List<Point> traceWinner;
  private int nextPlayer;  
  private int winner;
  private static Board board = new Board();

  //stores current position externally accessible by view
  public Point currentPos;
  
  /**
   * Inner Class that saves position co-ordinates
   * 
   */
  public class Point {
	    private int x;
	    private int y;

	    public Point(int x, int y) {
	      this.x = x;
	      this.y = y;
	    }
	    public void set(int x, int y) {
	    	this.setX(x);
	    	this.setY(y);
	    }
	    public void setX(int x) {
	    	this.x = x;
	    }
	    public int getX() {
	      return x;
	    }
	    public void setY(int y) {
	    	this.y = y;
	    }
	    public int getY() {
	      return y;
	    }
	  }

  
  /**
   * private constructor to prevent normal instantiation
   */
  private Board() {
  }

  /**
   * Singleton class that always returns the same board object
   * 
   * @return board object
   */
  public static Board getBoard(int row, int col) {
    if (board == null) {
      board = new Board();
    }
    board.reset(row, col);
    return board;
  }

  /**
   * rest the board object to default values
   * @param row
   * @param col
   */
  public void reset(int row, int col) {
    allMoves = new ArrayList<Point>();
    traceWinner = new ArrayList<Point>();
    boardState = new int[row][col];
    chipsInColumn = new int[col];
    currentPos = new Point(-1,-1);    
    winner = -1;
    nextPlayer = 1;   
  }

  /**
   * 
   * @param col, the selected column
   * @return number of chips currently held by the column
   */
  public int chipsInColumn(int col) {
    return chipsInColumn[col];
  }

  /**
   * Backtrack from bot player's anticipated moves 
   */
  public void backTrack() {
    if (allMoves.size() > 0) {
    	int x = GameConfig.ROW - board.chipsInColumn(this.currentPos.getY());
        int y = this.currentPos.getY();
     if (this.boardState[x][y] == this.getOpponent()) {
    	 
        this.boardState[x][y] = 0;
        this.chipsInColumn[y]--;
         
        this.nextPlayer = this.getOpponent();        
        
        this.currentPos.setX(allMoves.get(allMoves.size() - 1).getX());
        this.currentPos.setY(allMoves.get(allMoves.size() - 1).getY());
        
        this.winner = 0;
         
        allMoves.remove(allMoves.size() - 1);       
        
      }
    }    
  }

  /**
   * For valid column, increase the chip count of chosen column
   * else throw exception
   * 
   * @param pos refers to the column number selected by player   *          
   * 
   */
  private void validateMove(int col) throws RuntimeException{
	  if ((col < 0) || (col >= GameConfig.COL)) { 		  
	      throw new RuntimeException(col + " Invalid Column");
	    }
	    if ((this.chipsInColumn[col] >= GameConfig.ROW)) {
	      throw new RuntimeException("Column Overflow");	      
	    }	  
  }
  /**
   * For the respective column increments chip-count, add the current move
   * to allMoves and updates the state of the board for this position with
   * the current player(i.e. next player of the previous turn)
   * 
   * @param col
   */
  public void move(int col) {
    this.validateMove(col);
    this.allMoves.add(new Point(this.currentPos.getX(), this.currentPos.getY()));
    this.currentPos.set(GameConfig.ROW - 1 - this.chipsInColumn[col], col); 
    this.chipsInColumn[col]++;
    this.boardState[this.currentPos.getX()][this.currentPos.getY()] = this.nextPlayer;
    this.nextPlayer = this.getOpponent();
    
  }

  

  /**
   * @return state of the board, which player(1/2) occupies which cells
   * unoccupied cells will be 0
   */
  public int[][] getBoardEntries() {
    return boardState;
  }
  /**
   * 
   * @return winner ID
   */
  public int getWinner() {
    return winner;
  }
  /**
   * 
   * @param x 
   * @param y
   * @return true if point lies within the board, false otherwise 
   */
  private boolean isSafe(int x, int y) {
    if (x >= GameConfig.ROW || x < 0) {
      return false;
    }
    if (y >= GameConfig.COL || y < 0) {
      return false;
    }
    return true;
  }
  
 /**
  * 
  * @return the opponent player ID
  */
  private int getOpponent() {
    return (this.nextPlayer == 1) ? 2 : 1;
  }
  
  /**
   * Move in a specific direction in the board
   * @param moveX
   * @param moveY
   */
  private void directinalMove(int moveX, int moveY) {
    int currentPlayer = this.getOpponent();
    int x = this.currentPos.getX();
    int y = this.currentPos.getY();
    
    while (isSafe(x, y) && boardState[x][y] == currentPlayer) {
      Point currentStep = new Point(x, y);
      currentStep.set(x, y);
      traceWinner.add(currentStep);
      x += moveX;
      y += moveY;
    }
  }

  /**
   * 
   * @return true is 4 connected chips are found, false otherwise
   */
  private boolean isConnectFour() {
    if (traceWinner.size() >= 4) {      
      winner = this.getOpponent();
      return true;
    } else {
      return false;
    }
  }
  
  /**
   * Check for 4 connected chips in row/column/diagonal
   * 
   * @return true->win/tie, false otherwise
   */
  public boolean isGameOver() {
	boolean isBoardFull = true;
    for (int i = 0; i < GameConfig.COL; i++) {
      if (boardState[0][i] == 0) {
        isBoardFull = false;
        break;
      }
    }
    if (isBoardFull) {
      return true;
    }
    
    //Check all cells to the left
    traceWinner.clear();
    this.directinalMove(0, -1);
    if (!traceWinner.isEmpty()) {
     //prevent double counting the current cell
     traceWinner.remove(0);
    }
    //Check all cells to the right
    this.directinalMove(0, 1);
    if (isConnectFour()) {
      return true;
    }
    
    //Check all the cells in the same column
    traceWinner.clear();
    this.directinalMove(1, 0);
    if (isConnectFour()) {
      return true;
    }
    
    //Now check diagonals in four directions
    traceWinner.clear();
    this.directinalMove(1, 1);
    if (!traceWinner.isEmpty()) {
    	//prevent double counting the current cell
      traceWinner.remove(0);
    }
    
    this.directinalMove(-1, -1);
    if (isConnectFour()) {
      return true;
    }

    traceWinner.clear();
    this.directinalMove(-1, 1);
    if (!traceWinner.isEmpty()) {
    	//prevent double counting the current cell
      traceWinner.remove(0);
    }
    
    this.directinalMove(1, -1);
    if (isConnectFour()) {
      return true;
    }
    return false;
  }

  /**
   * @return trace of moves the winner took
   */
  public List<Point> traceWinner() {
    return new ArrayList<Point>(traceWinner);
  }
}