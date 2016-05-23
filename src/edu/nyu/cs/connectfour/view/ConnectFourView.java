package edu.nyu.cs.connectfour.view;

import javax.swing.*;

import edu.nyu.cs.connectfour.controller.*;
import edu.nyu.cs.connectfour.model.*;
import edu.nyu.cs.connectfour.GameConfig;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * User interface class for the game
 * 
 * @author abhishek bhunia
 *
 */
public class ConnectFourView implements ConnectFourObserver {

  private Board board;
  private Player player1;
  private Player player2;
  private ConnectFourController controller;
  private static final String defaultPlayer = "human";
  private JFrame frameMain;
  private JRadioButton player1_op1;
  private JRadioButton player1_op2;
  private JRadioButton player2_op1;
  private JRadioButton player2_op2;
  private JButton start;
  private JButton[][] boardCells;
  private JPanel centerPanel;
  private JPanel boardPanel = new JPanel(new GridLayout(GameConfig.ROW, GameConfig.COL));
  private JPanel playerPanel;
  private JPanel welcomePanel;
  private JPanel topPanelOne;
  private JPanel topPanelTwo;

  /**
   * Build a board, create to players using factory methods and
   * register as an observer with the controller
   * 
   * @param controller object
   */
  public ConnectFourView(ConnectFourController controller) {
    this.board = new BoardBuilder().buildFromRow(GameConfig.ROW).buildFromCol(GameConfig.COL).build();
    this.controller = controller;
    this.controller.registerObserver(this);
    this.refresh();
  }

  public ConnectFourView getView() {
    return this;
  }

  /**
   * 
   * @return stored board reference
   */
  public Board getBoard() {
    return this.board;
  }

  /**
   * 
   * @return current player1
   */
  public Player getPlayer1() {
    return this.player1;
  }

  /**
   * 
   * @return current player2
   */
  public Player getPlayer2() {
    return this.player2;
  }

  /**
   * Paints a selected cell with player's representative symbol
   * @param row
   * @param col
   * @param color
   */
  private void paintButton(int row, int col, String color){
    ImageIcon img;
    if(color == "Red") {
      img = new ImageIcon("images/Red.gif");
    }
    else if(color == "Blue"){
      img = new ImageIcon("images/Blue.gif");
    }
    else {
      img = new ImageIcon("images/Winner.gif");
    }
    boardCells[GameConfig.ROW - row - 1][col].setIcon(new ImageIcon(img.getImage()));
  }

  public void hideView() {
    this.frameMain.setVisible(false);
  }

  @Override
  public void update() {
    int[][] boardState = board.getBoardEntries();
    int row = board.currentPos.getX();
    int column = board.currentPos.getY();
    int player = boardState[row][column];
    paintButton(row, column, (player == 1) ? ("Red") : ("Blue"));
  }

  @Override
  public void gameOver() {
    java.util.List<Board.Point> traceWinner = board.traceWinner();
    if (traceWinner.size() >= 4) {
      for (Board.Point i : traceWinner) {
        int drawX = i.getX();
        int drawY = i.getY();
        this.paintButton(drawX, drawY, "Win");
      }
    }

    String msg;
    if (board.getWinner() == 1) {
      msg = "Red Player won!";
    }
    else if (board.getWinner() == 2) {
      msg = "Blue Player won!";
    }
    else {
      msg = "It's a tie!";
    }

    JPanel panel = new JPanel();
    JLabel label = new JLabel(msg);
    label.setFont(new Font("Tahoma",1,20));
    panel.add(label);
    JOptionPane.showMessageDialog(null, panel, "Thanks for playing!", JOptionPane.INFORMATION_MESSAGE);
    refresh();
  }

  private JPanel getCenterPanel() {
    for (int r = GameConfig.ROW - 1; r >= 0; r--) {
      for (int c = 0; c < GameConfig.COL; c++) {
        boardCells[r][c].setFocusPainted(false);
        boardPanel.add(boardCells[r][c]);
      }
    }
    return boardPanel;
  }

  /**
   * private class for detecting invalid column selections
   * 1. user can't play when two bots are playing
   * 2. Putting more than 6 chips in a column is not allowed
   * @author abhishek bhunia
   *
   */
  private class BoardButtonActionListener implements ActionListener {
    @Override
    public void actionPerformed(ActionEvent e) {
      JButton btn = (JButton) e.getSource();
      int c = (int) btn.getClientProperty("column");
      try {
        controller.nextHand(c);
      } catch (Exception ex) {
        JPanel panel = new JPanel();
        if(ex.getMessage() == "Column Overflow") {
          JLabel label = new JLabel("Try a new column");
          label.setFont(new Font("Tahoma",1,20));
          label.setPreferredSize(new Dimension(200,50));
          panel.add(label);
          JOptionPane.showMessageDialog(null, label, "Column Full", JOptionPane.INFORMATION_MESSAGE);
        }
        else if(player1 instanceof BotPlayer && player2 instanceof BotPlayer){
          JLabel label = new JLabel("Two Bots are playing. You can't participate!");
          label.setFont(new Font("Tahoma",1,20));
          panel.add(label);
          JOptionPane.showMessageDialog(null, panel, "You are not a participant!", JOptionPane.INFORMATION_MESSAGE);
        }
        else {
          JLabel label = new JLabel("Wait for your opponent's move!");
          label.setFont(new Font("Tahoma",1,20));
          panel.add(label);
          JOptionPane.showMessageDialog(null, panel, "Wait!", JOptionPane.INFORMATION_MESSAGE);
        }
      }
    }
  }

  private void setboardCells() {
    boardCells = new JButton[GameConfig.ROW][GameConfig.COL];
    for (int r = GameConfig.ROW - 1; r >= 0; r--) {
      for (int c = 0; c < GameConfig.COL; c++) {
        boardCells[r][c] = new JButton();
        boardCells[r][c].setPreferredSize(new Dimension(80, 80));
        boardCells[r][c].putClientProperty("column", c);
        boardCells[r][c].addActionListener(new BoardButtonActionListener());
      }
    }
  }

  private class SelectedPlayerListener implements ActionListener{

    @Override
    public void actionPerformed(ActionEvent e) {
      // TODO Auto-generated method stub
      AbstractButton aButton = (AbstractButton) e.getSource();
      String option = aButton.getClientProperty("ID").toString();
      if(option == "human1") {
        System.out.println("Selected: human1");
        player1 = PlayerFactory.getPlayer("Human", board);
      }
      else if(option == "human2") {
        System.out.println("Selected: human2");
        player2 = PlayerFactory.getPlayer("Human", board);
      }
      else if(option == "bot1") {
        System.out.println("Selected: bot1");
        player1 = PlayerFactory.getPlayer("Bot", board);
      }
      else if(option == "bot2") {
        System.out.println("Selected: bot2");
        player2 = PlayerFactory.getPlayer("Bot", board);
      }
    }
  }

  private JPanel getTopPanelOne() {
    topPanelOne = new JPanel();

    player1_op1 = new JRadioButton("Human");
    player1_op1.putClientProperty("ID", "human1");
    player1_op1.addActionListener(new SelectedPlayerListener());
    player1_op1.setSelected(true);
    player1_op1.setBackground(new Color(225,50,50));
    player1_op1.setForeground(Color.WHITE);
    player1_op1.setFont(new Font("Tahoma",1,20));
    
    player1_op2 = new JRadioButton("Bot");
    player1_op2.putClientProperty("ID", "bot1");
    player1_op2.addActionListener(new SelectedPlayerListener());
    player1_op2.setBackground(new Color(225,50,50));
    player1_op2.setForeground(Color.WHITE);
    player1_op2.setFont(new Font("Tahoma",1,20));

    ButtonGroup player1 = new ButtonGroup();
    player1.add(player1_op1);
    player1.add(player1_op2);

    JLabel label = new JLabel("Player RED");
    label.setForeground(Color.WHITE);
    label.setFont(new Font("Tahoma",1,20));
    label.setPreferredSize(new Dimension(130,80));

    topPanelOne.add(label);
    topPanelOne.add(player1_op1);
    topPanelOne.add(player1_op2);
    topPanelOne.setBorder(BorderFactory.createEtchedBorder());
    topPanelOne.setBackground(new Color(225,50,50));

    return topPanelOne;
  }

  private JPanel getTopPanelTwo() {
    topPanelTwo = new JPanel();
    
    player2_op1 = new JRadioButton("Human");
    player2_op1.putClientProperty("ID", "human2");
    player2_op1.addActionListener(new SelectedPlayerListener());
    player2_op1.setSelected(true);
    player2_op1.setBackground(new Color(50,50,225));
    player2_op1.setForeground(Color.WHITE);
    player2_op1.setFont(new Font("Tahoma",1,20));

    player2_op2 = new JRadioButton("Bot");
    player2_op2.putClientProperty("ID", "bot2");
    player2_op2.addActionListener(new SelectedPlayerListener());
    player2_op2.setBackground(new Color(50,50,225));
    player2_op2.setForeground(Color.WHITE);
    player2_op2.setFont(new Font("Tahoma",1,20));

    ButtonGroup player2 = new ButtonGroup();
    player2.add(player2_op1);
    player2.add(player2_op2);

    JLabel label = new JLabel("Player BLUE");
    label.setForeground(Color.WHITE);
    label.setFont(new Font("Tahoma",1,20));
    label.setPreferredSize(new Dimension(130,80));

    topPanelTwo.add(label);
    topPanelTwo.add(player2_op1);
    topPanelTwo.add(player2_op2);
    topPanelTwo.setBorder(BorderFactory.createEtchedBorder());
    topPanelTwo.setBackground(new Color(50,50,225));
    return topPanelTwo;
  }

  private JPanel padPanel(int width, int height) {
    JPanel newPanel = new JPanel();
    newPanel.setPreferredSize(new Dimension(width,height));
    return newPanel;
  }

  private JPanel getPlayerSelection() {
    playerPanel = new JPanel();
    playerPanel.setPreferredSize(new Dimension(GameConfig.FRAMEWIDTH,GameConfig.FRAMEHEIGHT));

    start = new JButton("Start");
    start.setPreferredSize(new Dimension(300,300));
    start.setFont(new Font("Tahoma",1,40));
    start.setBackground(new Color(80,220,80));
    start.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        frameMain.getContentPane().remove(welcomePanel);
        welcomePanel.setVisible(false);
        welcomePanel.removeAll();
        centerPanel.setVisible(true);
        frameMain.getContentPane().add(centerPanel);
        frameMain.invalidate();
        frameMain.validate();
        controller.updateModel(player1, player2, player2);
        controller.switchPlayer();
      }
    });

    playerPanel.add(getTopPanelOne(), BorderLayout.WEST);
    playerPanel.add(padPanel(2,50),BorderLayout.CENTER);
    playerPanel.add(getTopPanelTwo(), BorderLayout.EAST);
    playerPanel.add(padPanel(400,75),BorderLayout.SOUTH);
    playerPanel.add(start, BorderLayout.SOUTH);
    return playerPanel;
  }

  private void refresh() {
    this.player1 = PlayerFactory.getPlayer(defaultPlayer, board);
    this.player2 = PlayerFactory.getPlayer(defaultPlayer, board); 
    this.board = new BoardBuilder().buildFromRow(GameConfig.ROW).buildFromCol(GameConfig.COL).build();
    if (frameMain != null) {
      frameMain.dispose();
    }
    try {
      UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    } catch (ClassNotFoundException e1) {
        e1.printStackTrace();
      } 
      catch (InstantiationException e1) {
        e1.printStackTrace();
      }
      catch (IllegalAccessException e1) {
        e1.printStackTrace();
      }
      catch (UnsupportedLookAndFeelException e1) {
        e1.printStackTrace();
    }

    this.setboardCells();
    frameMain = new JFrame("Connect Four by Abhishek Bhunia");
    frameMain.setBounds(100, 100, GameConfig.FRAMEWIDTH, GameConfig.FRAMEHEIGHT);
    frameMain.setResizable(false);
    boardPanel.removeAll();
    boardPanel.revalidate();
    centerPanel = this.getCenterPanel();
    centerPanel.setVisible(false);
    frameMain.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    welcomePanel = new JPanel();
    welcomePanel.add(getPlayerSelection(), BorderLayout.CENTER);
    frameMain.add(welcomePanel, BorderLayout.NORTH);
    frameMain.pack();
    frameMain.setVisible(true);
  }
}