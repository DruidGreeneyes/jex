package jex.core;

public final class Pair<L, R> {
   private final L left;
   private final R right;

   public final Pair(final L left, final R right) {
      this.left = left;
      this.right = right;
   }

   private final Pair(final L left) {this.left = left;}
   private final Pair(final R right) {this.right = right;}

   public static final Pair partial (final L left) {return new Pair(left);}
   public static final Pair partial (final R right) {return new Pair(right);}

   public static final Pair make (final L left, final R right) {return new Pair(left, right);}

   public final L left() {return this.left;}
   public final R right() {return this.right;}
}