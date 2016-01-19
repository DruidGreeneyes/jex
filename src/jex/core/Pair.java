package rivet.program;

public final class Pair<L, R> {
	   private final L left;
	   private final R right;

	   public Pair(final L left, final R right) {
	      this.left = left;
	      this.right = right;
	   }

	   public static final <L, R> Pair<L, R> make (final L left, final R right) {return new Pair<L, R>(left, right);}

	   public final L left() {return this.left;}
	   public final R right() {return this.right;}
}
