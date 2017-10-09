package utilities;

import java.util.*;
import java.io.*;
import java.net.*;

/**
 * @author dwatson, kitty, Maryam
 * @version 2.0
 * 
 * Represents an InputListener that would be running as a thread and always listen for input coming in. 
 * 
 */
public class InputListener extends Observable implements Runnable
{
	//Attributes
	private int 				listenerNumber;
	private Socket				socket;
	private ObjectInputStream	ois;
	
	// Constructors
	/**
	 * @param socket the socket being monitored
	 * @param observer class to be notified when something changes
	 */
	public InputListener(Socket socket,Observer observer)
	{
		listenerNumber = 0;
		this.socket = socket;
		this.addObserver(observer);
	}
	/**
	 * @param listenerNumber number assigned to this listener
	 * @param socket the socket being monitored
	 * @param observer class to be notified when something changes
	 */
	public InputListener(int listenerNumber, Socket socket, Observer observer)
	{
		this.listenerNumber = listenerNumber;
		this.socket = socket;
		this.addObserver(observer);
	}
	//Getter and Setter Methods
	/**
	 * @return the listenerNumber
	 */
	public int getListenerNumber()
	{
		return listenerNumber;
	}
	/**
	 * @param listenerNumber the listenerNumber to set
	 */
	public void setListenerNumber(int listenerNumber)
	{
		this.listenerNumber = listenerNumber;
	}
	
	//Operational Methods
	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run()
	{		
		try
		{
			ois = new ObjectInputStream(socket.getInputStream());
			
			while(true)
			{
				Debug.output("input listener number "+listenerNumber);
				Object o = ois.readObject();
				setChanged();
				notifyObservers(o);
				
				Message m = (Message) o;
				if (m.getMessage().compareTo("has disconnected.") == 0)
				{
					Debug.output(m.getUser()+" is disconnecting...");
					ois.close();
					socket.close();
				}
					
			}
		}
		catch (SocketException e)
		{
			// not all exceptions are errors, just handle them gracefully!
			Debug.output("input listener number "+listenerNumber+": Socket has been closed.");
		}
		catch (EOFException e)
		{
			// not all exceptions are errors, just handle them gracefully!
			Debug.output("input listener number "+listenerNumber+": No stream available.");
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		catch (ClassNotFoundException e)
		{
			e.printStackTrace();
		}
	}
}
