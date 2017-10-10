package utilities;

import java.io.*;
import java.util.*;

/**
 * @author dwatson, Maryam
 * @version 2.0
 * 
 * Represents a Serialized Message object to be transported using streams.
 * 
 */

public class Message implements Serializable
{	
	//Constants
	private static final long serialVersionUID = -6635341076366698125L;
	
	//Attributes
	private String 				user;
	private String				message;
	private Date				timeStamp;
        private Object  object;
	
	//Constructors
	/**
	 * User defined constructor
	 * @param user user name
	 * @param message message being transported
	 * @param timeStamp date and time stamp put on message
	 */
	public Message(String user, String message, Date timeStamp, Object card)
	{
		this.user = user;
		this.message = message;
		this.timeStamp = timeStamp;
                this.object = card;
	}
        
	
	//Getter and Setter Methods
	/**
	 * @return the user
	 */
	public String getUser()
	{
		return user;
	}

	/**
	 * @param user the user to set
	 */
	public void setUser(String user)
	{
		this.user = user;
	}

	/**
	 * @return the message
	 */
	public String getMessage()
	{
		return message;
	}
	
	/**
	 * @param message the message to set
	 */
	public void setMsg(String message)
	{
		this.message = message;
	}

	/**
	 * @return the timeStamp
	 */
	public Date getTimeStamp()
	{
		return timeStamp;
	}

	/**
	 * @param timeStamp the timeStamp to set
	 */
	public void setTimeStamp(Date timeStamp)
	{
		this.timeStamp = timeStamp;
	}

	//Operational Methods
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		return "Message [message=" + message + ", timeStamp=" + timeStamp + ", user="
				+ user + "]";
	}
}
