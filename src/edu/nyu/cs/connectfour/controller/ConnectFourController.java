package edu.nyu.cs.connectfour.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import edu.nyu.cs.connectfour.model.*;
import edu.nyu.cs.connectfour.view.*;

/**
 * This class implements the controller element of the application.
 * It is responsible for passing passing information between 
 * controller and view
 * 
 * @author abhishek bhunia
 *
 */
public class ConnectFourController {

  private List<ConnectFourObserver> observers;
  private ConnectFourView view;
  private ConnectFourModel model;
  
  /**
   * This method initiates the game
   * 
   * @param model
   * @param view
   */
  public void run(ConnectFourModel model, ConnectFourView view) {
    this.model = model;
    this.view = view;
    this.updateModel(this.view.getBoard(), this.view.getPlayer1(), this.view.getPlayer2());
    this.switchPlayer();
  }
  
  /**
   * 
   * @return model currently being used by the controller
   */
  public ConnectFourModel getModel() {
    return this.model;
  }
  
  /**
   * 
   * @param model that resets the current model of the controller to the new one
   */
  public void setModel(ConnectFourModel model) {
    this.model = model;
  }

  /**
   * 
   * @return view managed by the controller
   */
  public ConnectFourView getView() {
    return this.view;
  }
  
  /**
   * 
   * @param view to set the view object of the controller
   */
  public void setView(ConnectFourView view) {
    this.view = view;
  }
  
  /**
   * 
   * @return borad reference held by the controller
   */
  public Board getBoard() {
    return this.getView().getBoard();
  }
  
  /**
   * overloaded method to handle model update
   * @param board
   * @param player1
   * @param player2
   */
  public void updateModel(Board board, Player player1, Player player2) {
    this.model.setBoard(this.view.getBoard());
    this.model.setPlayerOne(player1);
    this.model.setPlayerTwo(player2);
  }
  
  /**
   * overloaded method to handle model update
   * @param player1
   * @param player2
   * @param currentPlayer
   */
  public void updateModel(Player player1, Player player2, Player currentPlayer) {
    this.model.setPlayerOne(player1);
    this.model.setPlayerTwo(player2);
    this.model.setCurrentPlayer(currentPlayer);
  }
  
  /**
   * registration method called by external observers 
   * so that they can get updates from the controller
   * @param observer
   */
  public void registerObserver(ConnectFourObserver observer) {
    if(observer instanceof ConnectFourObserver) {
      observers.add(observer);
    }
  }
  
  /**
   * 
   * @return list of observers currently registered
   */
  public List<ConnectFourObserver> getObservers() {
    return this.observers;
  }
  
  /**
   * whenever there's an update to be made,
   * call the update function on all observers
   */
  private void notifyObservers() {
    for(ConnectFourObserver observer : this.observers) {
      observer.update();
    }
  }
  
  /**
   * notify observers the game is over
   */
  private void notifyGameOver() {
    for(ConnectFourObserver observer : this.observers) {
      observer.gameOver();
    }
  }

  public ConnectFourController() {
    this.observers = new ArrayList<ConnectFourObserver>();
  }

  /**
   * class that simulates a random delay by the bot player
   * without this, two bot player matches will instantly
   * draw all the winning moves
   * 
   * @author abhishek bhunia
   *
   */

  private class FakeThinker extends TimerTask {

    ConnectFourController obj;
    
    FakeThinker(ConnectFourController obj) {
      this.obj = obj;
    }

    public void run() {
      obj.nextHand();
    }
  }

  /**
   * overloaded method(human player) to make the next move
   * @param humanMove, the column number selected by player
   */
  public void nextHand(int humanMove) {
    this.model.getCurrentPlayer().nextMove(humanMove);
    this.model.getCurrentPlayer().play();
    this.notifyObservers();
    this.switchPlayer();
  }

  /**
   * overloaded method to handle bot player moves
   */
  public void nextHand() {
    this.model.getCurrentPlayer().play();
    this.notifyObservers();
    this.switchPlayer();
  }

  private Player getNextPlayer() {
    if(this.model.getCurrentPlayer() == this.model.getPlayerOne()) 
      return this.model.getPlayerTwo();
    return this.model.getPlayerOne();
  }

  /**
   * Keep switching players until gameover
   */
  public void switchPlayer() {
    
    if (this.model.getBoard().isGameOver()) {
      this.notifyGameOver();
    } 
    else {
      this.model.setCurrentPlayer(this.getNextPlayer());

      if(this.model.getCurrentPlayer() instanceof BotPlayer) {
        Random randomGenerator = new Random();
        int timeToThink = randomGenerator.nextInt(400) + 400;
        Timer timer = new Timer();
        timer.schedule(new FakeThinker(this), timeToThink);
      }
    }
  }
}