
package clientSide;

import javax.swing.*;
import javax.swing.border.*;
import java.util.*;
import java.net.*;
import java.io.*;
import java.awt.*;
import java.awt.event.*;
import utilities.*;

/**
 * @author dwatson, kitty, Maryam
 * @version 3.0
 * 
 * Represents a ClientGUI object that allows a user to connect to a server and chat with another user.
 * 
 */
@SuppressWarnings("serial")
public class ClientGUI extends JFrame implements Observer
{
	//Attributes
	private JTextField 			txtSend;
	private JTextArea			txtDisplay;
	private JButton				btnSend, btnConnect, btnDisconnect;
	private Socket				socket;
	private String 				userName;
	private ObjectOutputStream 	oos;
	private InputListener		inputListener;
	private boolean				newSession;
	
	//Constructor
	public ClientGUI()
	{
		createClientGUI();
	}

	private void createClientGUI()
	{
		setTitle("GUI Message Client");
		setBounds(400, 150, 500, 700);
		
		setLayout(new BorderLayout(5,5));
		Border buttonEdge = BorderFactory.createRaisedBevelBorder();
		
		//Code block that builds the GUI components to send messages
		JPanel pnlNorth = new JPanel(new GridLayout(3,1));
		JLabel lblSend = new JLabel("Message to Send:");
		txtSend = new JTextField();
		JPanel pnlSend = new JPanel();
		btnSend = new JButton("Send");
		btnSend.setBorder(buttonEdge);
		btnSend.addActionListener(new MyActionListener());
		btnSend.setEnabled(false);
		pnlSend.add(btnSend);
		pnlNorth.add(lblSend);
		pnlNorth.add(txtSend);
		pnlNorth.add(pnlSend);
		add(pnlNorth, BorderLayout.NORTH);
		
		//Code block to display any message sent from the client
		JPanel pnlCenter = new JPanel(new BorderLayout());
		JLabel lblDisplay  = new JLabel("Message Board");
		txtDisplay = new JTextArea();
		txtDisplay.setBorder(BorderFactory.createEtchedBorder());
		txtDisplay.setEditable(false);
		
		//displayMessage.setBounds(0, 0, 1024, 768);
		pnlCenter.add(lblDisplay, BorderLayout.NORTH);
		pnlCenter.add(txtDisplay, BorderLayout.CENTER);
		add(pnlCenter,BorderLayout.CENTER);
		
		//Code block to place buttons to connect and disconnect
		JPanel pnlSouth = new JPanel(new GridLayout(1,2));
		JPanel pnlLeft = new JPanel();
		btnConnect = new JButton("Connect");
		btnConnect.setBorder(buttonEdge);
		btnConnect.addActionListener(new MyActionListener());
		btnConnect.setEnabled(true);
		pnlLeft.add(btnConnect);
		JPanel pnlRight = new JPanel();
		btnDisconnect = new JButton("Disconnect");
		btnDisconnect.setBorder(buttonEdge);
		btnDisconnect.addActionListener(new MyActionListener());
		btnDisconnect.setEnabled(false);
		pnlRight.add(btnDisconnect);
		
		pnlSouth.add(pnlLeft);
		pnlSouth.add(pnlRight);
		add(pnlSouth, BorderLayout.SOUTH);
		
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//this.pack();
		this.setVisible(true);
	}
	
	@Override
	public void update(Observable observable, Object arg)
	{
		Message message = (Message)arg;		
		String msg = message.getUser()+": "+message.getMessage()+" ("+message.getTimeStamp()+")";
		txtDisplay.append(msg+"\n");
		
		// connected to another person
		if (message.getMessage().compareTo("You can start chatting!") == 0)
		{
			btnSend.setEnabled(true); // now i can TALK!
			btnDisconnect.setEnabled(true); // and if i don't like the other person, i can run away!
		}
		
		// the other person ran away!
		if (message.getMessage().compareTo("has disconnected.") == 0)
		{
			// i didn't quit the session, so i get to keep my username
			newSession = false;
			disconnectMe(); // drop current session
			connectMe(); // start new session
		}
		
	}
	
	private void connectMe()
	{
		try
		{
			socket = new Socket("localhost",5555);
			// if i didn't disconnect, i want to keep my username
			if(newSession)
			{
				userName = JOptionPane.showInputDialog("Enter user name");
			}
			btnConnect.setEnabled(false);
			
			oos = new ObjectOutputStream(socket.getOutputStream());
			txtDisplay.append("Connected! Waiting for a chat partner...\n");
			
			//start an input listener thread
			inputListener = new InputListener(socket,this);
			Thread t1 = new Thread(inputListener);
			t1.start();
		}
		catch (HeadlessException e1)
		{
			e1.printStackTrace();
		}
		catch (UnknownHostException e1)
		{
			e1.printStackTrace();
		}
		catch (IOException e1)
		{
			e1.printStackTrace();
		}
	}
	
	private void disconnectMe()
	{
		txtDisplay.append("Disconnected.\n");
		btnDisconnect.setEnabled(false);
		btnSend.setEnabled(false);
		btnConnect.setEnabled(true);
		try
		{
			oos.close();
			socket.close();
			inputListener = null;
		}
		catch (IOException e1)
		{
			e1.printStackTrace();
		}
	}
	
//***************************************************
	private class MyActionListener implements ActionListener
	{

		@Override
		public void actionPerformed(ActionEvent e)
		{
			if(e.getSource() == btnConnect)
			{
				// if i can get to this button, it means i want to start a new chat session
				newSession = true;
				connectMe();
			}
			if(e.getSource() == btnSend)
			{
				Message message = new Message(userName,txtSend.getText(),new Date());
				
				try
				{
					oos.writeObject(message);
					txtDisplay.append("Me: "+message.getMessage()+" ("+message.getTimeStamp()+")\n");
					txtSend.setText("");
				}
				catch (IOException e1)
				{
					e1.printStackTrace();
				}
			}
			
			if(e.getSource() == btnDisconnect)
			{
				// if i hit this button, it means i want to quit and next time i connect,
				// i will need to provide my username again
				newSession = true;
				Message message = new Message(userName,"has disconnected.",new Date());
				try
				{
					oos.writeObject(message);
				}
				catch (IOException e1)
				{
					e1.printStackTrace();
				}
				disconnectMe();
			}
		}
	}
	
	// Main to run client GUI.
	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		new ClientGUI();
	}
}
