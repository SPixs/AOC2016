import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class Day06 {

	public static void main(String[] args) throws IOException {

		List<String> lines = Files.readAllLines(Path.of("input/input_day06.txt"));
		
		// Part 1
		long startTime = System.nanoTime();

		StringBuffer message = new StringBuffer();
		for (int i=0;i<lines.get(0).length();i++) {
			final int pos = i;
			Map<Character, List<Character>> charOccurences = lines.stream().map(l -> l.charAt(pos)).collect(Collectors.groupingBy(c -> c));
			message.append(charOccurences.values().stream().sorted((l1, l2) -> -Integer.compare(l1.size(), l2.size())).findFirst().orElseThrow().get(0));
		}
		
		System.out.println("Result part 1 : " + message + " in " + TimeUnit.NANOSECONDS.toMillis((System.nanoTime() - startTime)) + "ms");

		// Part 2
		startTime = System.nanoTime();
		
		message = new StringBuffer();
		for (int i=0;i<lines.get(0).length();i++) {
			final int pos = i;
			Map<Character, List<Character>> charOccurences = lines.stream().map(l -> l.charAt(pos)).collect(Collectors.groupingBy(c -> c));
			message.append(charOccurences.values().stream().sorted((l1, l2) -> Integer.compare(l1.size(), l2.size())).findFirst().orElseThrow().get(0));
		}
		
		System.out.println("Result part 2 : " + message + " in " + TimeUnit.NANOSECONDS.toMillis((System.nanoTime() - startTime)) + "ms");
	}

}
