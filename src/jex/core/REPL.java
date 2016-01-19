package rivet.program;

import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;


import static java.util.stream.Collectors.toList;

public final class REPL {

	public static void main(String[] args) {
		List<Method> methodIndex = 
				Arrays.stream(Program.class.getMethods())
				.filter((m) -> Modifier.isPublic(m.getModifiers()))
				.collect(toList());
		String res;
		if (args.length > 0) {
			System.out.println("Attempting to evaluate single command...");
			res = Loop.eval(args, methodIndex);
		} else {
			try (Reader input = System.console().reader()) {
				System.out.println("Attempting to start repl...");
				res = repl(input, methodIndex);
			} catch (IOException e) {
				e.printStackTrace();
				res = "ERROR!";
			} 
		}
		System.out.println(res);
	}

	public static String repl (final Reader input, final List<Method> methodIndex) {
		System.out.println("Entering Repl loop...");
		while(true) {
			final Pair<String, Boolean> print = new Loop(input, methodIndex).run(true);
			if (print.right() == false) break;
			else System.out.println(print.left());
		}
		return "wHEEEEEE!";
	}
}
