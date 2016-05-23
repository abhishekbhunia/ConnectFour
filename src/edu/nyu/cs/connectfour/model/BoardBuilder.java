package edu.nyu.cs.connectfour.model;

/**
 * Builder class for board object that takes row, column and returns a board
 * 
 * @author abhishek bhunia
 *
 */

public class BoardBuilder {
  private int row;
  private int col;

  public BoardBuilder buildFromRow(int row) {
    this.row = row;
    return this;
  }

  public BoardBuilder buildFromCol(int col) {
    this.col = col;
    return this;
  }

  /**
   * builder function for the board class
   * @return board object
   */
  public Board build() {
    return Board.getBoard(row, col);
  }
}