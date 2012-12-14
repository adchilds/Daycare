package lib;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

/**
 * <p>Forces a component to only allow so many characters
 * 
 * @author Adam Childs
 * @version 1.00
 */
public class FixedSizeDocument extends PlainDocument
{
	private static final long serialVersionUID = 1L;
	int size;

	public FixedSizeDocument(int size)
	{
		this.size = size;
	}

	@Override
	public void insertString(int offs, String str, AttributeSet a) throws BadLocationException
	{
		if (getLength() + str.length() > size)
			str = str.substring(0, size - getLength());
		super.insertString(offs, str, a);
	}
}