package Models;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import lib.OSProperties;

public class File_System_Model
{
	private OSProperties osp = new OSProperties();
	String directory;

	public File_System_Model()
	{
		this.directory = getDefaultDirectory();
	}

	/**
	 * <p>Specifies the default location of the program on the user's system.
	 * 
	 * @return a String containing a file path to the location of the program
	 * on the user's system.
	 */
	public String getDefaultDirectory()
	{
		String result = "";

		if (osp.isUnix())
			result = ""; // TODO: UNIX DIRECTORY
		else if (osp.isMac()) // "/Users/USERNAME/Documents/Daycare Management System"
			result = osp.getSeparator() + "Users" + osp.getSeparator() + osp.getUser() +
					osp.getSeparator() + "Documents" + osp.getSeparator() +
					"Daycare Management System" + osp.getSeparator(); // MAC DIRECTORY
		else if (osp.isWindows()) // "C:\Daycare Management System\"
			result = "C:" + osp.getSeparator() + "Daycare Management System" + osp.getSeparator(); // Windows Directory
		else
			result = "~"; // TODO: Default directory
		return result;
	}

	/**
	 * <p>Finds the default directory for the program depending on the user's operating system
	 * and also the file within that directory.
	 * 
	 * @param f the file to find
	 * @return the absolute path of the file
	 */
	public String findFile(String f)
	{
		return directory + f;
	}

	/**
	 * <p>Finds the given directory, branched off from the program
	 * default directory, depending on the user's operating system.
	 * 
	 * @param d the directory to find
	 * @return the absolute path of the directory
	 */
	public String findDirectory(String d)
	{
		return directory + osp.getSeparator() + d;
	}

	/**
	 * Returns the location of the user's default OS pictures directory.
	 */
	public String getDefaultPictureDirectory()
	{
		if (osp.isUnix())
			return ""; // TODO: UNIX DIRECTORY
		else if (osp.isMac()) // "/Users/USERNAME/Pictures"
			return osp.getSeparator() + "Users" + osp.getSeparator() + osp.getUser() +
					osp.getSeparator() + "Pictures"; // MAC DIRECTORY
		else if (osp.isWindows()) // "C:\Users\USERNAME\Pictures"
			return "C:" + osp.getSeparator() + "Users" + osp.getSeparator() + osp.getUser() +
					osp.getSeparator() + "Pictures";
		else
			return "~"; // TODO: Default
	}

	/**
	 * <p>Returns the absolute directory path containing the folder.
	 * 
	 * @param d the directory to find the absolute path for
	 * @return a String containing the absolute path to the directory d
	 */
	public String getDirectoryPath(String d)
	{
		return getDefaultDirectory() + d + osp.getSeparator();
	}

	/**
	 * <p>Creates a new directory with specified name.
	 * 
	 * @param name the name of the directory to create
	 */
	public boolean createDirectory(String name)
	{
		boolean success = new File(name).mkdir();

		if (!success)
			System.out.println("Error creating directories during installation...");
		else
			System.out.println("Creating directory [" + name + "]");
		
		return success;
	}

	/**
	 * <p>Creates a new file with specified name.
	 * 
	 * @param name the name of the file to create
	 */
	public boolean createFile(String name)
	{
		boolean success = false;

		try {
			success = new File(name).createNewFile();

			if (!success)
				System.out.println("Error creating file [" + name + "] during installation...");
			else
				System.out.println("Creating file [" + name + "]");
		} catch (IOException e) {
			e.printStackTrace();
		}

		return success;
	}

	/**
	 * Appends the given String to the given file and appends a newline afterwards
	 * 
	 * @param str the String to be written
	 * @param file the file to right to
	 * @return whether or not the append has successfully finished
	 */
	public boolean appendLine(String str, String file)
	{
		try {
			BufferedWriter out = new BufferedWriter(new FileWriter(new File(directory + file), true));
			out.newLine();
			out.append(str);
			out.close();
			return true;
		} catch (IOException e) {
			System.out.println( "Could not append line... File not found?" );
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * <p>Copies the text from one file to the other.
	 * 
	 * @param toFile file to write to
	 * @param fromFile file to read from
	 */
	public void populateFileFromFile(String toFile, String fromFile)
	{
		FileWriter fw = null;
		BufferedWriter out = null;
		String[] fromFileText;

		// Read the text in from the first file
		fromFileText = readLines(fromFile);

		// Now write to the new file
		try {
			fw = new FileWriter(toFile);
			out = new BufferedWriter(fw);

			for (int i = 0; i < fromFileText.length; i++)
			{
				out.write(fromFileText[i]); // String from fromFile
				out.newLine();
			}

			out.close();			
			fw.close();

			System.out.println("Populating file [" + toFile + "]");
		} catch (IOException e) {
			System.out.println( "IOException while writing to the file [" + toFile + "]" );
		}
	}

	/**
	 * <p>Reads in the lines from the specified file and places each line into
	 * its own index in a String array.
	 * 
	 * @param filename the name of the file we want to read from
	 * @return a String array containing each line of text in its own index
	 * @throws IOException if the file cannot be found
	 */
	private static String[] readLines(String filename)
	{
		BufferedReader bufferedReader = null;
		List<String> lines = new ArrayList<String>();

		try {
			InputStream pdInputStream = Install_Model.class.getResourceAsStream(filename); 
			InputStreamReader isr = new InputStreamReader(pdInputStream);
			bufferedReader = new BufferedReader(isr);

			String line = null;
			while ((line = bufferedReader.readLine()) != null)
				lines.add(line);
			bufferedReader.close();
		} catch (FileNotFoundException e) {
			System.out.println( "File could not be found [" + filename + "]" );
		} catch (IOException e) {
			System.out.println( "Could not read from the file for some reason..." );
		}

		return lines.toArray(new String[lines.size()]);
    }

	/**
	 * <p>Counts the number of lines given a file. Increments counter
	 * after each newline character is found.
	 * 
	 * @param filename the file to count lines
	 * @return an integer value representing the number of lines in the given file
	 * @throws IOException if the file cannot be found.
	 */
	private int count(String filename) throws IOException
	{
		InputStream is = new BufferedInputStream(new FileInputStream(directory + filename));
		try {
			byte[] c = new byte[1024];
			int count = 0;
			int readChars = 0;
			while ((readChars = is.read(c)) != -1)
			{
				for (int i = 0; i < readChars; ++i)
				{
					if (c[i] == '\n')
						++count;
				}
			}
			return count;
		} finally {
			is.close();
		}
	}

	public void removeLineFromFile(String file, String lineToRemove)
	{
		File inFile = new File(directory + file);
		int totalLines = 0;
		try {
			totalLines = count(file);
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		//Construct the new file that will later be renamed to the original filename.
		File tempFile = new File(inFile.getAbsolutePath() + ".tmp");

		BufferedReader br = null;
		PrintWriter pw = null;

		try {
			br = new BufferedReader(new FileReader(inFile));
			pw = new PrintWriter(new FileWriter(tempFile));

			if (!inFile.isFile())
				return;
			
			String line = null;

			//Read from the original file and write to the new
			//unless content matches data to be removed.
			int count = 0;
			while ((line = br.readLine()) != null)
			{
				if (!line.trim().equals(lineToRemove))
				{
					if (count == totalLines)
						pw.print(line);
					else
						pw.println(line);
					pw.flush();
				}
				count++;
			}
			pw.close();
			br.close();

			//Delete the original file
			if (!inFile.delete())
			{
				System.out.println("Could not delete file");
				return;
			}

			//Rename the new file to the filename the original file had.
			if (!tempFile.renameTo(inFile))
				System.out.println("Could not rename file");
		} catch (FileNotFoundException ex) {
			ex.printStackTrace();
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			if (pw != null)
				pw.close();
			if (br != null)
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
	}

	/**
	 * <p>Returns an array holding all files of the specified directory
	 * <p>Use file[i].getName() to return the name of the file.
	 * 
	 * @param dir The directory name to get files from
	 */
	public File[] getAllFiles(String dir)
	{
		File[] file = new File(getDirectoryPath(dir)).listFiles();

		return file;
	}
}