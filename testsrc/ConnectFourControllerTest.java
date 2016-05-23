import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import edu.nyu.cs.connectfour.GameConfig;
import edu.nyu.cs.connectfour.controller.ConnectFourController;
import edu.nyu.cs.connectfour.model.ConnectFourModel.*;
import edu.nyu.cs.connectfour.model.*;
import edu.nyu.cs.connectfour.view.*;

public class ConnectFourControllerTest {

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
  public void testObserver() {
    ConnectFourView newView = new ConnectFourView(controller);
    assertTrue(controller.getObservers().contains(newView));
  }

  @Test
  public void testHumanPlay() {
    controller.getModel().setPlayerOne(player1);
    controller.getModel().setPlayerTwo(player2);
    controller.getModel().setCurrentPlayer(player1);
    controller.nextHand(2);
    assertEquals(controller.getBoard().getBoardEntries()[5][2], 1);
    reset();
  }

  @Test
  public void testPlayerSwitch() {
    controller.getModel().setPlayerOne(player1);
    controller.getModel().setPlayerTwo(player2);
    controller.getModel().setCurrentPlayer(player1);
    controller.nextHand(2);
    assertTrue(controller.getModel().getCurrentPlayer() instanceof BotPlayer);
    reset();
  }

  @Test
  public void testBotPlay() {
    controller.getModel().setPlayerOne(player2);
    controller.getModel().setPlayerTwo(player2);
    controller.getModel().setCurrentPlayer(player2);
    controller.nextHand();
    controller.nextHand();
    int botMoves = 0;

    for(int[] i: controller.getBoard().getBoardEntries()) {
      for(int j: i) {
        if(j != 0) {
          botMoves++;
        }
      }
    }
    assertEquals(botMoves, 2);
    reset();
  }
}
