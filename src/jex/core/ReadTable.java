package rivet.program;

import java.lang.StringBuffer;
import java.io.IOException;
import java.io.Reader;

public final class ReadTable {
	private StringBuffer buffer;
	private final Reader input;
	/** Constructor. */
	public ReadTable(Reader input) {
		this.buffer = new StringBuffer();
		this.input = input;
	}

	public Boolean read() {
		int i = -1;
		try {
			i = input.read();
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (i == -1)
			return false;
		else {
			char c = (char) i;
			if (c != '\n')
				this.put((char) i);
			return true;
		}
	}

	public char current() {
		return this.buffer.charAt(buffer.length() - 1);
	}

	public String get() {return buffer.toString();}

	public void put(Object object) {buffer.append(object);}

	public void clear() {buffer = new StringBuffer();}

	public Reader reader() {return input;}
}
