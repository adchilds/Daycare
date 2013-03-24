package lib;

/**
 * <p> A simple timer class that holds the individuals
 * name for which the timer corresponds, and the beginning
 * time that the timer was instantiated.
 * 
 * @author Adam Childs
 */
public class Timer
{
	private String name;
	private long startTime;

	public Timer(String n)
	{
		this.name = n;
		this.setStartTime();
	}

	public long getStartTime()
	{
		return this.startTime;
	}

	public void setStartTime()
	{
		this.startTime = System.currentTimeMillis();
	}

	public String getName()
	{
		return this.name;
	}

	public void setName(String n)
	{
		this.name = n;
	}
}