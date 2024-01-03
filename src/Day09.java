import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Day09 {

	public static void main(String[] args) throws IOException {

		List<String> lines = Files.readAllLines(Path.of("input/input_day09.txt"));

		// Part 1
		long startTime = System.nanoTime();
		long result = 0;

		result = uncompress(lines.get(0)).length();
		System.out.println("Result part 1 : " + result + " in "	+ TimeUnit.NANOSECONDS.toMillis((System.nanoTime() - startTime)) + "ms");

		// Part 2
		startTime = System.nanoTime();
		String compressed = lines.get(0);
		result = uncompressPart2(compressed);
		System.out.println("Result part 2 : " + result + " in "	+ TimeUnit.NANOSECONDS.toMillis((System.nanoTime() - startTime)) + "ms");
	}
	
	public static String uncompress(String compressed) {
		int index = 0;
		StringBuffer uncompressed = new StringBuffer();
		
		String state = "copy";
		int charCount = 0;
		int copyCount = 0;
		int elapsedTextToCopy = 0;
		StringBuffer textToCopy = new StringBuffer();
		
		while (index < compressed.length()) {
			char c = compressed.charAt(index++);
			if (state.equals("parseCharCount")) {
				if (c == 'x') {
					state = "parseCopyCount";
					continue;
				}
				charCount = charCount * 10 + (c - '0');
				continue;
			}
			if (state.equals("parseCopyCount")) {
				if (c == ')') {
					state = "parseTextToCopy";
					elapsedTextToCopy = 0;
					textToCopy.setLength(0);
					continue;
				}
				copyCount = copyCount * 10 + (c - '0');
				continue;
			}
			if (state.equals("parseTextToCopy")) {
				if (elapsedTextToCopy < charCount) {
					textToCopy.append(c);
				}
				if (++elapsedTextToCopy == charCount) {
					for (int i=0;i<copyCount;i++) { uncompressed.append(textToCopy); }
					state = "copy";
				}
				continue;
			}
			else if (c != '(') {
				uncompressed.append(c);
				continue;
			}
			else {
				state = "parseCharCount";
				charCount = copyCount = 0;
				continue;
			}
		}
		
		return uncompressed.toString();
	}
	
	public static long uncompressPart2(String compressed) {
		int index = 0;
		long count = 0;
		
		String state = "copy";
		int charCount = 0;
		int copyCount = 0;
		int elapsedTextToCopy = 0;
		StringBuffer textToCopy = new StringBuffer();
		
		while (index < compressed.length()) {
			char c = compressed.charAt(index++);
			if (state.equals("parseCharCount")) {
				if (c == 'x') {
					state = "parseCopyCount";
					continue;
				}
				charCount = charCount * 10 + (c - '0');
				continue;
			}
			if (state.equals("parseCopyCount")) {
				if (c == ')') {
					state = "parseTextToCopy";
					elapsedTextToCopy = 0;
					textToCopy.setLength(0);
					continue;
				}
				copyCount = copyCount * 10 + (c - '0');
				continue;
			}
			if (state.equals("parseTextToCopy")) {
				if (elapsedTextToCopy < charCount) {
					textToCopy.append(c);
				}
				if (++elapsedTextToCopy == charCount) {
					count += copyCount * uncompressPart2(textToCopy.toString());
					state = "copy";
				}
				continue;
			}
			else if (c != '(') {
				count++;
				continue;
			}
			else {
				state = "parseCharCount";
				charCount = copyCount = 0;
				continue;
			}
		}
		
		return count;
	}
}
