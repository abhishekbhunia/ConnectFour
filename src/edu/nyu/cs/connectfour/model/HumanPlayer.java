package edu.nyu.cs.connectfour.model;

/**
 * Class representing a human player, implements the Player interface
 * @author abhishek bhunia
 *
 */

public class HumanPlayer implements Player {
  
  private Board board;
  private int nextMove;

  HumanPlayer(Board board) {
    this.board = board;
    this.nextMove = -1;
  }

  @Override
  public void nextMove(int nextMove) {
    this.nextMove = nextMove;
  }

  @Override
  public void play() {
    this.board.move(nextMove);
  }

  @Override
  public int getMove() {
    return nextMove;
  }
}