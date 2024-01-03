import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day07 {
	
	public static void main(String[] args) throws IOException {

		List<String> lines = Files.readAllLines(Path.of("input/input_day07.txt"));
		
		// Part 1
		long startTime = System.nanoTime();
		
		long result = lines.stream().filter(Day07::doesSupportTLS).count();
		
		System.out.println("Result part 1 : " + result + " in " + TimeUnit.NANOSECONDS.toMillis((System.nanoTime() - startTime)) + "ms");

		// Part 2
		startTime = System.nanoTime();
		result = lines.stream().filter(Day07::doesSupportSSL).count();
		System.out.println("Result part 2 : " + result + " in " + TimeUnit.NANOSECONDS.toMillis((System.nanoTime() - startTime)) + "ms");
	}
	
	static Pattern bracketPattern = Pattern.compile("\\[([^\\]]+)\\]");
	static Pattern nonBracketPattern = Pattern.compile("(?<=\\]|^)[^\\[]+");
	
	public static boolean doesSupportTLS(String address) {
		Matcher matcher = bracketPattern.matcher(address);
		while (matcher.find()) {
        	for (int i=0;i<matcher.groupCount();i++) {
        		if (isABBA(matcher.group(1))) return false;
        	}
    	}
		
		matcher = nonBracketPattern.matcher(address);
    	while (matcher.find()) {
    		if (isABBA(matcher.group())) return true;
    	}
    	
    	return false;
	}
	
	public static boolean doesSupportSSL(String address) {
		List<String> allABA = new ArrayList<String>();
		Matcher matcher = nonBracketPattern.matcher(address);
    	while (matcher.find()) {
    		allABA.addAll(findAllABA(matcher.group()));
    	}

    	matcher = bracketPattern.matcher(address);
		while (matcher.find()) {
        	for (int i=0;i<matcher.groupCount();i++) {
        		for (String ABA : allABA) {
            		if (hasBAB(matcher.group(1), ABA)) return true;
        		}
        	}
    	}
		
		return false;
	}

	private static List<String> findAllABA(String text) {
		List<String> result = new ArrayList<String>();
		for (int i=1;i<text.length()-1;i++) {
			if (text.charAt(i-1) == text.charAt(i+1) && text.charAt(i-1) != text.charAt(i)) {
				result.add(text.substring(i-1,i+2));
			}
		}
		return result;
	}
	
	private static boolean hasBAB(String text, String ABA) {
		String BAB = "" + ABA.charAt(1) +  ABA.charAt(0) + ABA.charAt(1);
		return text.contains(BAB);
	}

	private static boolean isABBA(String text) {
		for (int i=1;i<text.length()-2;i++) {
			if (text.charAt(i) == text.charAt(i+1) && text.charAt(i-1) == text.charAt(i+2) && text.charAt(i) != text.charAt(i-1)) return true;
		}
		return false;
	}

}
