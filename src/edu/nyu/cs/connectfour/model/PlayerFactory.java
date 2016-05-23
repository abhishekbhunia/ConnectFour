package edu.nyu.cs.connectfour.model;

/**
 * Factory class for creating human and bot players
 * @author abhishek bhunia
 *
 */
public class PlayerFactory {
  
	/**
	 * 
	 * @param id, "Human" refers to human players, "Bot" to computer players
	 * @param board, every player object has a reference of the board for quick lookup
	 * @return Player object
	 */
  public static Player getPlayer(String id, Board board) {
    if (id.equals("Bot")) {
      return new BotPlayer(board);
    }
    else if (id.equals("Human")) {
      return new HumanPlayer(board);
    }
    return new HumanPlayer(board);
  }
}