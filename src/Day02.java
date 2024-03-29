import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.function.BinaryOperator;

public class Day02 {

	public static void main(String[] args) throws IOException {

		List<String> lines = Files.readAllLines(Path.of("input/input_day02.txt"));
		
		
		// Part 1
		long startTime = System.nanoTime();
		int start = 5;
		String code = "";
		for (String line : lines) {
			start = computeDigit(start, line);
			code += start;
		}
		System.out.println("Result part 1 : " + code + " in " + TimeUnit.NANOSECONDS.toMillis((System.nanoTime() - startTime)) + "ms");

		// Part 2
		startTime = System.nanoTime();
		code = "";
		for (String line : lines) {
			code += computeDigitPart2(start, line);
		}
		System.out.println("Result part 2 : " + code + " in " + TimeUnit.NANOSECONDS.toMillis((System.nanoTime() - startTime)) + "ms");
	}

	private static int computeDigit(int start, String line) {
		int digit = start;
		for (char c : line.toCharArray()) {
			switch (c) {
				case 'U': if (digit > 3) digit -= 3; break;
				case 'D': if (digit < 7) digit += 3; break;
				case 'L': if (digit % 3 != 1) digit--; break;
				case 'R': if (digit % 3 != 0) digit++; break;
				default: throw new IllegalStateException(""+c);
			}
		}
		return digit;
	}
	
	private static char computeDigitPart2(int start, String line) {
		
		String[] map = new String[] {
				"#######",
				"###1###",
				"##234##",
				"#56789#",
				"##ABC##",
				"###D###",
				"#######",
		};
		
		int x = 1;
		int y = 3;
		
		for (char c : line.toCharArray()) {
			switch (c) {
				case 'U': if (map[y-1].charAt(x) != '#') y--; break;
				case 'D': if (map[y+1].charAt(x) != '#') y++; break;
				case 'L': if (map[y].charAt(x-1) != '#') x--; break;
				case 'R': if (map[y].charAt(x+1) != '#') x++; break;
				default: throw new IllegalStateException();
			}
		}
		return map[y].charAt(x);
	}
}
