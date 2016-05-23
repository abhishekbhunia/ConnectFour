package edu.nyu.cs.connectfour.model;

public interface Player {
	/**
	 * only applicable for human players
	 * @param nextMove is the column number for the next move
	 */
  public void nextMove(int nextMove);
  
  /**
   * only applicable for human player
   * @return stored move number
   */
  public int getMove();
  public void play();
}