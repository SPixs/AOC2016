import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Day04 {

	public static void main(String[] args) throws IOException {

		List<String> lines = Files.readAllLines(Path.of("input/input_day04.txt"));

		// Part 1
		long startTime = System.nanoTime();
		long result = 0;
		
		Pattern pattern = Pattern.compile("(.*)-(\\d+)\\[(.*?)\\]");
		for(String line : lines) {
			Matcher matcher = pattern.matcher(line);
			matcher.find();
			String encryptedName = matcher.group(1);
			long sectorID = Long.parseLong(matcher.group(2));
			String checksum = matcher.group(3);
			
			if (isReal(encryptedName, checksum)) {
				result += sectorID;
			}
		}

		System.out.println("Result part 1 : " + result + " in "	+ TimeUnit.NANOSECONDS.toMillis((System.nanoTime() - startTime)) + "ms");

		// Part 2
		startTime = System.nanoTime();
		for(String line : lines) {
			Matcher matcher = pattern.matcher(line);
			matcher.find();
			String encryptedName = matcher.group(1);
			long sectorID = Long.parseLong(matcher.group(2));
			String uncryptedName = uncrypt(encryptedName, sectorID);
			if (uncryptedName.contains("north")) result = sectorID;
		}
		
		System.out.println("Result part 2 : " + result + " in "	+ TimeUnit.NANOSECONDS.toMillis((System.nanoTime() - startTime)) + "ms");
	}

	private static String uncrypt(String encryptedName, long sectorID) {
		StringBuffer result = new StringBuffer();
		for(char c : encryptedName.toCharArray()) {
			result.append((char) ('a'+(c-'a'+sectorID)%26));
		}
		return result.toString();
	}

	private static boolean isReal(String encryptedName, String checksum) {
		encryptedName = encryptedName.replace("-", "");
		Map<Character, List<Character>> collect = encryptedName.chars().mapToObj(c -> (char) c).collect(Collectors.groupingBy(c -> c));
		List<Character> charactersByFrequency = collect.values().stream().sorted((l1, l2) -> {
			int result = -Integer.compare(l1.size(), l2.size());
			if (result == 0) result = Integer.compare(l1.get(0), l2.get(0));
			return result;
		}).map(l -> l.get(0)).collect(Collectors.toList());

		String expectedChecksum = charactersByFrequency.subList(0, 5).stream().map(c -> String.valueOf(c)).reduce((s1,  s2) -> s1+s2).orElseThrow(); 
		
		return expectedChecksum.equals(checksum);
	}
}
