package gui;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.LinkedList;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JTextField;

import problemDomain.*;

import javax.swing.JTextArea;
import java.awt.Font;
import java.awt.HeadlessException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.Observable;
import java.util.Observer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import utilities.InputListener;
import utilities.Message;

public class GUI implements Observer {

    private Socket socket;
    public JFrame frame;
    private JTextField textField;
    private JLabel player1CardImg;
    private JLabel player2CardImg;
    private JPanel chatPanel;
    private JTextArea textArea;
    private JButton connectBtn;
    private JButton disconnectBtn;
    private JLabel player2CardDraw;
    private JLabel player1CardDraw;
    private JButton btnNextHand;
    private JLabel p1TotalCards;
    private JLabel p2TotalCards;
    private JButton sendButton;

    public CardManager cardManager;

    private Card card1 = null;
    private Card card2 = null;
    private int value1 = 0;
    private int value2 = 0;
    private int topCard = 0;
    private JLabel gameInfoLbl;
    private JPanel gamePanel;

    private boolean newSession;
    private ObjectOutputStream oos;
    private InputListener inputListener;
    private String userName;
    private String player;

    /**
     * Create the application.
     */
    public GUI() {

        initialize();
        cardManager = new CardManager();
    }

    /**
     * Initialize the contents of the frame.
     */
    private void initialize() {

        frame = new JFrame("THIS IS WAR!");
        frame.getContentPane().setBackground(new Color(50, 205, 50));
        frame.setSize(700, 500);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        try {
            frame.setIconImage(ImageIO.read(new File("res/images/icon.png")));
            frame.getContentPane().setLayout(null);

            chatPanel = new JPanel();
            chatPanel.setBackground(new Color(34, 139, 34));
            chatPanel.setBounds(431, 11, 253, 450);
            frame.getContentPane().add(chatPanel);
            chatPanel.setLayout(null);

            textField = new JTextField();
            textField.setBounds(10, 11, 233, 25);
            chatPanel.add(textField);
            textField.setColumns(10);

            sendButton = new JButton("Send");
            sendButton.setBounds(154, 47, 89, 23);
            chatPanel.add(sendButton);
            sendButton.addActionListener(new MyActionListener());

            textArea = new JTextArea();
            textArea.setBounds(10, 81, 233, 287);
            chatPanel.add(textArea);

            connectBtn = new JButton("Connect");
            connectBtn.setBounds(10, 416, 89, 23);
            chatPanel.add(connectBtn);
            connectBtn.addActionListener(new MyActionListener());

            disconnectBtn = new JButton("Disconnect");
            disconnectBtn.setBounds(130, 416, 113, 23);
            chatPanel.add(disconnectBtn);
            disconnectBtn.addActionListener(new MyActionListener());

            gamePanel = new JPanel();
            gamePanel.setBackground(new Color(0, 100, 0));
            gamePanel.setBounds(10, 11, 414, 450);
            frame.getContentPane().add(gamePanel);
            gamePanel.setLayout(null);

            JLabel player1Lbl = new JLabel("Player 1");
            player1Lbl.setFont(new Font("Tahoma", Font.PLAIN, 13));
            player1Lbl.setBounds(176, 11, 46, 14);
            gamePanel.add(player1Lbl);

            JLabel player2Lbl = new JLabel("Player 2");
            player2Lbl.setFont(new Font("Tahoma", Font.PLAIN, 13));
            player2Lbl.setBounds(176, 425, 46, 14);
            gamePanel.add(player2Lbl);

            player1CardImg = new JLabel();
            player1CardImg.setIcon(new ImageIcon("res/images/cardBack.jpg"));
            player1CardImg.setBounds(163, 36, 72, 96);
            player1CardImg.addMouseListener(new MyMouseListener());
            gamePanel.add(player1CardImg);

            player2CardImg = new JLabel();
            player2CardImg.setIcon(new ImageIcon("res/images/cardBack.jpg"));
            player2CardImg.setBounds(163, 318, 72, 96);
            player2CardImg.addMouseListener(new MyMouseListener());
            gamePanel.add(player2CardImg);

            player2CardDraw = new JLabel();
            player2CardDraw.setIcon(null);
            player2CardDraw.setBounds(213, 175, 72, 96);
            gamePanel.add(player2CardDraw);

            player1CardDraw = new JLabel();
            player1CardDraw.setIcon(null);
            player1CardDraw.setBounds(112, 175, 72, 96);
            gamePanel.add(player1CardDraw);

            gameInfoLbl = new JLabel();
            gameInfoLbl.setBounds(130, 282, 190, 25);
            gamePanel.add(gameInfoLbl);

            btnNextHand = new JButton("Next Hand!");
            btnNextHand.setVisible(false);
            btnNextHand.setBounds(305, 197, 99, 51);
            btnNextHand.addMouseListener(new MyMouseListener());
            gamePanel.add(btnNextHand);

            p1TotalCards = new JLabel("");
            p1TotalCards.setBounds(245, 118, 125, 14);
            gamePanel.add(p1TotalCards);

            p2TotalCards = new JLabel("");
            p2TotalCards.setBounds(245, 400, 125, 14);
            gamePanel.add(p2TotalCards);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void win(String winningPlayer) {

        gamePanel.setEnabled(false);
        JOptionPane.showConfirmDialog(null, winningPlayer + " has won this game. \n Another round?!");
    }

    /**
     * Begins WAR if both players draw a card of equal value.
     */
    private void startWar() {

        gameInfoLbl.setText("WAR! 3 CARDS EACH WERE ADDED TO THE POT");
        gamePanel.setBackground(new Color(158, 1, 12));

        if (cardManager.p1.size() < 4) {

            win("Player 2");
        } else if (cardManager.p2.size() < 4) {

            win("Player 1");
        } else {

            for (int i = 0; i < 3; i++) {

                cardManager.pot.add(cardManager.p1.remove(0));
                cardManager.pot.add(cardManager.p2.remove(0));
            }
        }

        value1 = 0;
        value2 = 0;
    }

    /**
     * Add the total pot to the winners hand.
     *
     * @param winnerHand
     * @param pot
     */
    private void winnerWinner(LinkedList winnerHand, LinkedList pot) {

        while (pot.size() > 0) {

            winnerHand.add(pot.remove(0));
        }
        gamePanel.setBackground(new Color(0, 100, 0));

        if (cardManager.p1.size() == 0) {
            win("Player 2");
        } else if (cardManager.p2.size() == 0) {
            win("Player 1");
        }
    }

    /**
     * Compares the value of the next drawn card by each player
     *
     * @param value1 card value of Player 1 card
     * @param value2 card value of Player 2 card
     */
    private void compareValues(int value1, int value2) {

        if (value1 != 0 && value2 != 0) {

            if (value1 > value2) {

                gameInfoLbl.setText("Player 1 Wins The Pot!");
                btnNextHand.setVisible(true);
                winnerWinner(cardManager.p1, cardManager.pot);
            } else if (value2 > value1) {

                gameInfoLbl.setText("Player 2 Wins The Pot!");
                btnNextHand.setVisible(true);
                winnerWinner(cardManager.p2, cardManager.pot);
            } else {

                startWar();
            }
        }
    }

    @Override
    public void update(Observable observable, Object arg) {
        if (arg instanceof Message) {

            Message message = (Message) arg;

            if (message.getList()!= null) {

                this.cardManager.pot = (LinkedList<Card>) message.getList();
                card2 = cardManager.pot.getLast();
                System.out.println("here: " + cardManager.pot.getLast());
            }
            
            String msg = message.getUser() + ": " + message.getMessage() + " (" + message.getTimeStamp() + ")";
            textArea.append(msg + "\n");

            // connected to another person
            if (message.getMessage().compareTo("You can start chatting!") == 0) {
                player = message.getUser();
                sendButton.setEnabled(true); // now i can TALK!
                disconnectBtn.setEnabled(true); // and if i don't like the other person, i can run away!
            }

            // the other person ran away!
            if (message.getMessage().compareTo("has disconnected.") == 0) {
                // i didn't quit the session, so i get to keep my username
                newSession = false;
                disconnectMe(); // drop current session
                connectMe(); // start new session
            }
        }

    }

    private void connectMe() {
        try {
            socket = new Socket("localhost", 5555);
            // if i didn't disconnect, i want to keep my username
            if (newSession) {
                userName = JOptionPane.showInputDialog("Enter user name");
            }
            connectBtn.setEnabled(false);

            oos = new ObjectOutputStream(socket.getOutputStream());
            textArea.append("Connected! Waiting for partner response...\n");

            //start an input listener thread
            inputListener = new InputListener(socket, this);
            Thread t1 = new Thread(inputListener);
            t1.start();
        } catch (HeadlessException e1) {
            e1.printStackTrace();
        } catch (UnknownHostException e1) {
            e1.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    private void disconnectMe() {
        textArea.append("Disconnected.\n");
        disconnectBtn.setEnabled(false);
        sendButton.setEnabled(false);
        connectBtn.setEnabled(true);
        try {
            oos.close();
            socket.close();
            inputListener = null;
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    /**
     * Click events for the card labels and buttons.
     *
     * @author 645011
     *
     */
    private class MyActionListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == connectBtn) {
                // if i can get to this button, it means i want to start a new chat session
                newSession = true;
                connectMe();
            }
            if (e.getSource() == sendButton) {
                Message message = new Message(userName, textField.getText(), new Date(), null);

                try {
                    oos.writeObject(message);
                    textArea.append("Me: " + message.getMessage() + " (" + message.getTimeStamp() + ")\n");
                    textField.setText("");
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }

            if (e.getSource() == disconnectBtn) {
                // if i hit this button, it means i want to quit and next time i connect,
                // i will need to provide my username again
                newSession = true;
                Message message = new Message(userName, "has disconnected.", new Date(), null);
                try {
                    oos.writeObject(message);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                disconnectMe();
            }
        }
        
        public void update(){
            card1 =  cardManager.pot.getLast();
            card2 = cardManager.pot.getLast();
        }
    }
    

    public class MyMouseListener implements MouseListener {

        @Override
        public void mouseClicked(MouseEvent e) {
            if (!connectBtn.isEnabled()) { //where the user draws a card
                if (e.getSource() == player1CardImg) {

                    if (cardManager.p1.size() > 0) {

                        if (value1 == 0) {
                            card1 = cardManager.p1.remove(topCard);
                            player1CardDraw.setIcon(new ImageIcon(card1.getCardImage()));
                            value1 = card1.getFaceValue();
                            cardManager.pot.add(card1);
                            System.out.println(card1);
                            System.out.println(cardManager.pot.toString());
                            try {
                                Message shit = new Message("", "", null, cardManager.pot);
                                oos.writeObject(shit);
                            } catch (IOException ex) {
                                Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
                            }

                            if (player.contains("Player 1")) {

                                player2CardImg.setEnabled(false);
                            } else if (player.contains("Player 2")) {

                                player1CardImg.setEnabled(false);
                            }
                        }
                    }
                }
                if (!(card2 == null)) { //only print a card for player 2 if its not the first turn
                    card2 = cardManager.pot.get(cardManager.pot.size() - 1);
                    player2CardDraw.setIcon(new ImageIcon(card2.getCardImage()));
                }
            }

            /*         if (e.getSource() == player2CardImg) { //where player2 would have clicked before this was an online game

                if (cardManager.p2.size() > 0) {

                    if (value2 == 0) {
                        card2 = cardManager.p2.remove(topCard);
                        player2CardDraw.setIcon(new ImageIcon(card2.getCardImage()));
                        value2 = card2.getFaceValue();
                        cardManager.pot.add(card2);
                        System.out.println(card2);
                    }
                }
            }
             */
            if (e.getSource() == btnNextHand) {

                card1 = null;
                card2 = null;
                value1 = 0;
                value2 = 0;

                player1CardDraw.setIcon(null);
                player2CardDraw.setIcon(null);
                btnNextHand.setVisible(false);
                gameInfoLbl.setText("");

                p1TotalCards.setText(cardManager.p1.size() + " Cards!");
                p2TotalCards.setText(cardManager.p2.size() + " Cards!");
            }
           // value1 = card1.getFaceValue();
            value2 = card2.getFaceValue();
            compareValues(value1, value2);
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            // TODO Auto-generated method stub
        }

        @Override
        public void mouseExited(MouseEvent e) {
            // TODO Auto-generated method stub

        }

        @Override
        public void mousePressed(MouseEvent e) {
            // TODO Auto-generated method stub

        }

        @Override
        public void mouseReleased(MouseEvent e) {

        }

    }
}
