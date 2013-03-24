package models;

/**
 * <p>Contains all methods related to checking children
 * and/or employees in/out of the daycare daily.
 * 
 * @author Adam Childs
 * @since 03/22/2013
 */
public class Check_In_Model
{
	
	public Check_In_Model()
	{
		
	}

	/**
	 * <p>Sets the maximum time allowed that a user who has
	 * checked in may stay checked in. This method is available
	 * so that checked in users will automatically be checked out
	 * after the specified time frame so that daycare providers
	 * will not over-charge families or over-pay employees who have
	 * forgotten to check out for the day.
	 * 
	 * @param h Maximum hours allowed
	 * @param m Maximum minutes allowed
	 * @param s Maximum seconds allowed
	 */
	public void setMaxTimeAllowed(int h, int m, int s)
	{
		
	}

}