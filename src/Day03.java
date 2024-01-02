import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

public class Day03 {

	public static void main(String[] args) throws IOException {

		List<String> lines = Files.readAllLines(Path.of("input/input_day03.txt"));
		
		// Part 1
		long startTime = System.nanoTime();
		long result = lines.stream().filter(s -> isValidTriangle(s)).count();
		
		System.out.println("Result part 1 : " + result + " in " + TimeUnit.NANOSECONDS.toMillis((System.nanoTime() - startTime)) + "ms");

		// Part 2
		startTime = System.nanoTime();
		result = 0;
		for (int i=0;i<lines.size();i+=3) {
			Object[] p1 = Arrays.stream(lines.get(i).split(" ")).map(s -> s.trim()).filter(s -> !s.isBlank()).toArray();
			Object[] p2 = Arrays.stream(lines.get(i+1).split(" ")).map(s -> s.trim()).filter(s -> !s.isBlank()).toArray();
			Object[] p3 = Arrays.stream(lines.get(i+2).split(" ")).map(s -> s.trim()).filter(s -> !s.isBlank()).toArray();
			String[] triangles = new String[] {
					"" + p1[0] + " " + p2[0] + " " + p3[0], 
					"" + p1[1] + " " + p2[1] + " " + p3[1], 
					"" + p1[2] + " " + p2[2] + " " + p3[2]
			};
			result += Arrays.stream(triangles).filter(s -> isValidTriangle(s)).count();
		}
		System.out.println("Result part 2 : " + result + " in " + TimeUnit.NANOSECONDS.toMillis((System.nanoTime() - startTime)) + "ms");
	}

	private static boolean isValidTriangle(String s) {
		int[] edges = Arrays.stream(s.split(" ")).filter(str -> !str.isBlank()).mapToInt(Integer::parseInt).toArray();
		return edges[2] < edges[0] + edges[1] && edges[1] < edges[2] + edges[0] && edges[0] < edges[2] + edges[1];
	}
}
