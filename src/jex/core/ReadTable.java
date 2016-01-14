package jex.core;

import java.lang.StringBuffer;
import java.io.BufferedReader;

public final class ReadTable {
   private final StringBuffer buffer;
   private final BufferedReader input;
   /** Constructor. */
   public ReadTable(BufferedReader input) {
      this.buffer = new StringBuffer();
      this.input = input;
   }

    public Boolean read(BufferedReader input) {
       int i = -1;
       try {
          i = input.read();
       } catch (IOException e) {
          e.printStackTrace();
       }
       if (i == -1)
          return false;
       else {
          this.put((char) i);
          return true;
       }
    }

    public String get() {return this.buffer.toString();}

    public void put(Object object) {this.buffer.append(object);}

    public void clear() {this.buffer.delete(0);}

    public BufferedReader reader() {return this.input;}
}