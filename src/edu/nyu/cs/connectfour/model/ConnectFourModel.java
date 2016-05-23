package edu.nyu.cs.connectfour.model;

/**
 * public class to store the data state/model of the game
 * and set of applicable operations on the state of the game
 * @author abhis_000
 *
 */
public class ConnectFourModel {

  private Player playerOne;
  private Player playerTwo;
  private Player currentPlayer;
  private Player nextPlayer;
  private Board board;

  /**
   * called by controller to change the current player
   * @param player
   */
  public void setCurrentPlayer(Player player) {
    this.currentPlayer = player;
  }

  /**
   * 
   * @return current player
   */
  public Player getCurrentPlayer() {
    return this.currentPlayer;
  }

  /**
   * sets 'Player 1' entity of the model
   * @param playerOne
   */
  public void setPlayerOne(Player playerOne) {
    this.playerOne = playerOne;
  }

  /**
   * 
   * @return Player 1' entity of the model
   */
  public Player getPlayerOne() {
    return this.playerOne;
  }

  /**
   * set 'Player 2' entity of the model
   * @param playerTwo
   */
  public void setPlayerTwo(Player playerTwo) {
    this.playerTwo = playerTwo;
  }

  /**
   * 
   * @return 'Player 2' entity of the model
   */
  public Player getPlayerTwo() {
    return this.playerTwo;
  }

  /**
   * stores a reference of the board used by controller
   * @param board
   */
  public void setBoard(Board board) {
    this.board = board;
  }

  /**
   * 
   * @return reference to the board
   */
  public Board getBoard() {
    return this.board;
  }
}