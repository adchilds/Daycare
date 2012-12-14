package lib;
/**
 * <p>This class checks the validity of a product key entered by the user. If
 * the product key is valid, the XML file is changed to show that the user has
 * entered their product key.
 * 
 * @author Adam Childs
 * @version 1.00
 */
public class Validation
{
	String[] input = new String[5];

	public Validation(String[] s)
	{
		input[0] = s[0];
		input[1] = s[1];
		input[2] = s[2];
		input[3] = s[3];
		input[4] = s[4];
	}

	public boolean validate()
	{
		for (int i = 0; i < input.length; i++)
			if (input[i].length() < 4)
				return false;

		boolean validKey = false;
		boolean b;
		int a;
		String s;

		// compute 0
		a = Integer.parseInt(input[0]);
		if ((a / 6) % 3 == 0)
			b = true;
		else
			b = false;

		if (b)
			validKey = true;
		else
			return validKey;

		// compute 1
		validKey = false;
		s = input[1];
		char[] key_0 = { 'A', 'G', 'R', 'X', 'J', 'M', 'Q' };
		char[] key_1 = { 'F', 'T', 'E', 'W', 'H', 'D', 'U' };
		char[] key_2 = { 'Z', 'C', 'B', 'N', 'Y', '8', '4' };
		char[] key_3 = { '5', 'G', 'A', '6', '3', 'V', 'I' };
		for (int i = 0; i < key_0.length; i++)
		{
			if (s.charAt(0) == key_0[i])
			{
				validKey = true;
				break;
			} else {
				validKey = false;
			}
		}
		for (int i = 0; i < key_1.length; i++)
		{
			if (s.charAt(1) == key_1[i])
			{
				validKey = true;
				break;
			} else {
				validKey = false;
			}
		}
		for (int i = 0; i < key_2.length; i++)
		{
			if (s.charAt(2) == key_2[i])
			{
				validKey = true;
				break;
			} else {
				validKey = false;
			}
		}
		for (int i = 0; i < key_3.length; i++)
		{
			if (s.charAt(3) == key_3[i])
			{
				validKey = true;
				break;
			} else {
				validKey = false;
			}
		}
		if (validKey)
			validKey = true;
		else
			return validKey;
		validKey = false; // reset

		// compute 2
		String[] sArray = { "YUJI", "HAHA", "KFDG", "LYDJ", "ILHL", "AAFN", "IHVE" };

		for (int i = 0; i < sArray.length; i++)
		{
			if (input[2].equals(sArray[i]))
			{
				validKey = true;
				break;
			}
		}
		if (!validKey)
			return validKey;
		validKey = false; // reset

		// compute 3
		String[] zArray = { "H73H", "F3K5", "K01L", "FFEF", "JAE9", "NVM0", "12C5" };

		for (int i = 0; i < zArray.length; i++)
		{
			if (input[3].equals(zArray[i]))
			{
				validKey = true;
				break;
			}
		}
		if (!validKey)
			return validKey;
		validKey = false; // reset

		// compute 4
		String[] arr = { "DMS5", "DMS4", "DMS8" };
		for (int i = 0; i < arr.length; i++)
		{
			if (input[4].equals(arr[i]))
			{
				validKey = true;
				break;
			}
		}

		return validKey;
	}

}