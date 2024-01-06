import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

public class Day16 {

	public static void main(String[] args) throws IOException {

		List<String> lines = Files.readAllLines(Path.of("input/input_day16.txt"));

		String input = lines.get(0);
		
		// Part 1
		long startTime = System.nanoTime();
		int result = 0;

		int length = 272;
		String checksum = expandAndComputeChecksum(input, length);
		
		System.out.println("Result part 1 : " + checksum + " in " + TimeUnit.NANOSECONDS.toMillis((System.nanoTime() - startTime)) + "ms");

		// Part 2
		startTime = System.nanoTime();
		length = 35651584;
		checksum = expandAndComputeChecksum(input, length);
		System.out.println("Result part 2 : " + checksum + " in "	+ TimeUnit.NANOSECONDS.toMillis((System.nanoTime() - startTime)) + "ms");
	}

	private static String expandAndComputeChecksum(String input, int length) {
		while (input.length() < length) {
			StringBuffer appendBuffer = new StringBuffer(input).reverse();
			String buffer = appendBuffer.toString().replace('0', '#');
			buffer = buffer.replace('1', '0');
			buffer = buffer.replace('#', '1');
			input = input+'0'+buffer;
		}
		input = input.substring(0, length);
		
		String checksum = computeChecksum(input);
		while(checksum.length() % 2 == 0) {
			checksum = computeChecksum(checksum);
		}
		return checksum;
	}

	private static String computeChecksum(String input) {
		StringBuffer checksum = new StringBuffer();
		for (int i=0;i<input.length()-1;i+=2) {
			checksum.append(input.charAt(i) == input.charAt(i+1) ? '1' : '0');
		}
		return checksum.toString();
	}
}
