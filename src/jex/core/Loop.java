package jex.core;

import java.io.BufferedReader;

public final class Loop
{
   private final ReadTable table;

   public final Loop(final BufferedReader input)
   {
      this.table = new ReadTable(input);
   }

   public final Pair<String, Boolean> run() {
      while(true) {
         final Boolean hasMore = this.table.next();
         final String object = this.table.get();
         if (!hasMore) return Pair.make(object, HasMore);
         final Pair<String, Boolean> res = this.read(object);
         if (res.right) return res;
      }
   }

   private final Pair<String, Boolean> read(final String object) {
      if (object == "(") {
         this.table.clear();
         return new Loop(this.table.reader()).run();
      }

      if (object == ")") {
         final string line = object.substring(0, object.length() - 1);
         this.table.clear();
         return Pair.make(this.eval(line), true);
      }

      return Pair.make("", false);
   }

   private final String eval(String line) {
      //TODO: figure out how to compile on the fly.
   }
}