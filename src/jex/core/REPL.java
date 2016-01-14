package jex.core;

import java.io.BufferedReader;
import java.io.Reader;

/** class REPL.
*/
public final class REPL {

   public static void main(final String[] args) throws Exception {

   }

   public static void repl (final Reader reader) {
      try (final BufferedReader input = new BufferedReader(reader)) {
         while(true) {
            final Pair<String, Boolean> print = new Loop(input).run();
            if (print.right == false) break;
            else System.out.println(print.left);
         }
      } catch (final IOException e) {
         e.printStackTrace();
      }
   }
}