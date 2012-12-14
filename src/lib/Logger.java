package lib;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;

import Controllers.File_System_Controller;

/**
 * <p>Facilitates the logging of warning, error, etc., messages which
 * can be used by the developer or user to debug errors within the
 * program.
 * 
 * @author Adam Childs
 */
public class Logger
{
	String toFile = null;

	/**
	 * <p>General logging constructor when simply trying to write
	 * out a log message to the log file.
	 */
	public Logger()
	{
		toFile = new File_System_Controller().getModel().findFile("logs/log.txt");
	}

	/**
	 * <p>Writes the given message to the program log file, as well as
	 * stating the date, time, and status of the message (error, warning, etc.).
	 * 
	 * @param message The message to write to the log file
	 * @param level The message status level (WARNING, ERROR, INFO, etc.)
	 */
	public void write(String message, String level)
	{
		try {
			FileWriter fw = new FileWriter(toFile, true);
			BufferedWriter out = new BufferedWriter(fw);

			Date date = new Date();

			String logMessage = "[" + date.toString() + "] [" + level + "]: " + message;

			out.append(logMessage);
			out.newLine();

			out.close();			
			fw.close();

			System.out.println(logMessage);
		} catch (IOException e) {
			System.out.println( "IOException while writing to the file [" + toFile + "]" );
		}
	}
}