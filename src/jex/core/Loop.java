package rivet.program;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Reader;
import java.io.Serializable;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.ArrayUtils;

public final class Loop
{
	private final Reader input;
	private final ReadTable table;
	private final List<Method> index;

	public Loop(final Reader input, List<Method> index) {
		this.input = input;
		this.table = new ReadTable(this.input);
		this.index = index;
	}

	public final Pair<String, Boolean> run(boolean prompt) {
		if (prompt) System.out.print("=> ");
		while(true) {
			final Boolean hasMore = this.table.read();
			final String buffer = this.table.get();
			if (!hasMore) return Pair.make(buffer, hasMore);
			final Pair<String, Boolean> res = this.read(buffer);
			if (res.right()) return res;
		}
	}

	private final Pair<String, Boolean> read(final String buffer) {
		if (buffer.equals("(")) {
			this.table.clear();
			return new Loop(this.input, index).run(false);
		}
		
		
		if (StringUtils.endsWith(buffer, ")")) {
			String line = buffer.substring(0, buffer.length() - 1);
			this.table.clear();
			return Pair.make(eval(line, index), true);
		}

		return Pair.make("", false);
	}
	
	public static final String eval(String line, List<Method> methodIndex) {
		return eval(line.split(" "), methodIndex);
	}
	
	public static final String eval(String[] args, List<Method> methodIndex) {
		System.out.println("Attempting to evaluate statement...");
		String cmd = args[0];
		
		List<Method> possibles = methodIndex.stream()
				.filter((m) -> m.getName().equalsIgnoreCase(cmd))
				.collect(Collectors.toList());
		if (possibles.size() < 1) 
			return String.format("Command '%s' not found.", cmd);
		
		List<Object> params = (args.length < 1) 
				? new ArrayList<>()
						: Arrays.stream(ArrayUtils.subarray(args, 1, args.length))
								.map(Loop::deserialize)
								.collect(Collectors.toList());
		int exp = params.size();
		
		possibles.removeIf((m) -> m.getParameters().length != exp);
		if (possibles.size() < 1)
			return String.format("Command '%s' not found accepting %d args",
					cmd, exp);
		
		possibles.removeIf((m) -> !matchTypes(m, params));
		if (possibles.size() < 1)
			return String.format("Command '%s' not found accepting arg types: %s",
					cmd, params.stream()
							.map((s) -> s.getClass().getSimpleName().toString()));
		
		MethodHandle method = null;
		try {
			method = MethodHandles.lookup().unreflect(possibles.get(0));
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			return e.getMessage();
		}
		
		Serializable res;
		try {
			res = (Serializable) method.invokeWithArguments(params);
		} catch (Throwable e) {
			e.printStackTrace();
			return e.getMessage();
		}
		return serialize(res);
	}
	
	private static boolean matchTypes(Method method, List<Object> params) {
		List<String> types = params.stream()
								.map((p) -> p.getClass().getSimpleName())
								.collect(Collectors.toList());
		List<String> expectedTypes = Arrays.stream(method.getParameterTypes())
												.map((p) -> p.getSimpleName())
												.collect(Collectors.toList());
		for (int i = 0; i < types.size(); i++)
			if (!expectedTypes.get(i).equals(types.get(i)))
				return false;
		return true;
	}
	
	private static Object deserialize (String string) {
		if (!StringUtils.startsWith(string, "decode:|:")) return string;
		System.out.println("Attempting to decode string: " + string);
		byte[] data = Base64.getDecoder().decode(string);
		Object obj;
		try (ObjectInputStream ois = new ObjectInputStream( 
				new ByteArrayInputStream(data))) {
			obj = ois.readObject();
		} catch (ClassNotFoundException | IOException e) {
			e.printStackTrace();
			obj = null;
		}
		return (Serializable) obj;
	}

	/** Write the object to a Base64 string. */
	private static String serialize (Serializable object) {
		if (object.equals(object.toString())) return object.toString();
		try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
			try (ObjectOutputStream oos = new ObjectOutputStream(baos)) {
				oos.writeObject(object);
			}
			return String.format("decode:|:%s",
					Base64.getEncoder().encodeToString(baos.toByteArray())); 
		} catch (IOException e) {
			e.printStackTrace();
			return "ERROR!";
		}
	}
}