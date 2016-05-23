import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import edu.nyu.cs.connectfour.GameConfig;
import edu.nyu.cs.connectfour.controller.ConnectFourController;
import edu.nyu.cs.connectfour.model.ConnectFourModel.*;
import edu.nyu.cs.connectfour.model.*;
import edu.nyu.cs.connectfour.view.*;

public class ConnectFourModelTest {
  
  private ConnectFourController controller;
  private Player player1;
  private Player player2;
  private Board board;
  
  private void reset() {
    controller = new ConnectFourController();
    controller.setModel(new ConnectFourModel());
    controller.setView(new ConnectFourView(controller));
    controller.updateModel(controller.getView().getBoard(),
      controller.getView().getPlayer1(), controller.getView().getPlayer2());
    player1 = PlayerFactory.getPlayer("Human", controller.getModel().getBoard());
    player2 = PlayerFactory.getPlayer("Bot", controller.getModel().getBoard());
    controller.getView().hideView();
  }

  @Before
  public void setUp() throws Exception {
    reset();
  }

  @Test
  public void testPlayerFactory() {
    Player p = PlayerFactory.getPlayer("Human", controller.getBoard());
    assertTrue(p instanceof HumanPlayer);
    assertFalse(p instanceof BotPlayer);
    p = PlayerFactory.getPlayer("Bot", controller.getBoard());
    assertTrue(p instanceof BotPlayer);
    assertFalse(p instanceof HumanPlayer);
    p = PlayerFactory.getPlayer("God", controller.getBoard());
    assertTrue(p instanceof HumanPlayer);
    assertFalse(p instanceof BotPlayer);
  }

  @Test
  public void testPlayers() {
    controller.getModel().setPlayerOne(player1);
    controller.getModel().setPlayerTwo(player2);
    controller.getModel().setCurrentPlayer(player1);
    assertTrue(controller.getModel().getPlayerOne() instanceof HumanPlayer);
    assertTrue(controller.getModel().getPlayerTwo() instanceof BotPlayer);
    assertTrue(controller.getModel().getCurrentPlayer() instanceof HumanPlayer);
    reset();
  }

  @Test
  public void testHumanPlayerMove() {
    player1.nextMove(5);
    assertEquals(player1.getMove(),5);
  }

  @Test(expected = UnsupportedOperationException.class)
  public void setMoveTest() {
    player2.nextMove(0);
    player2.getMove();
  }

  @Test(expected = RuntimeException.class)
  public void testFullColumn() {
    for (int i = 0; i < GameConfig.COL - 1; i++) {
      controller.getBoard().move(GameConfig.COL - 1);
    }
    controller.getBoard().move(GameConfig.COL - 1);
  }

  @Test
  public void testSingleton() {
    Board newBoard = Board.getBoard(6, 7);
    assertEquals(newBoard, controller.getBoard());
  }

  @Test
  public void testChipsInColumn(){
    controller.getBoard().move(0);
    controller.getBoard().move(0);
    controller.getBoard().move(1);
    assertEquals(controller.getBoard().chipsInColumn(0),2);
    assertEquals(controller.getBoard().chipsInColumn(1),1);
    assertEquals(controller.getBoard().chipsInColumn(3),0);
  }

  @Test
  public void testBotWinMove() {
    for(int i = 0; i < 5; ++i) {
      controller.getBoard().move(0);
    }
    for(int i = 0; i < 5; ++i) {
      controller.getBoard().move(1);
    }
    for(int i = 0; i < 5; ++i) {
      controller.getBoard().move(2);
    }
    for(int i = 0; i < 5; ++i) {
      controller.getBoard().move(3);
    }
    player2.play();
    assertEquals(controller.getBoard().getBoardEntries()[5][0], 1);
  }

  @Test
  public void testBoardUpdate() {
    controller.getBoard().move(5);
    assertEquals(controller.getBoard().chipsInColumn(5),1);
    reset();
  }
}
