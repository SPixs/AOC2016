import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

public class Day15 {

	public static void main(String[] args) throws IOException {

		List<String> lines = Files.readAllLines(Path.of("input/input_day15.txt"));

		// Part 1
		long startTime = System.nanoTime();

		Pattern pattern = Pattern.compile("Disc #(\\d+) has (\\d+) positions; at time=0, it is at position (\\d+).");
		
		int[] reminders = new int[lines.size()];
		int[] modulo = new int[lines.size()];
		int index = 1;
		
		// Module seem to be all primes. We can use Chinese reminders theorem.
		for (String line : lines) {
			Matcher matcher = pattern.matcher(line);
			matcher.find();
			
			int positions = Integer.parseInt(matcher.group(2));
			int startPosition = Integer.parseInt(matcher.group(3));
			
			int pos = getInRangeRotated(0 - startPosition - index, positions);
			reminders[index-1] = pos;
			modulo[index-1] = positions;

			index++;
		}
		
		long result = chineseRemainder(reminders, modulo);
		
		System.out.println("Result part 1 : " + result + " in "	+ TimeUnit.NANOSECONDS.toMillis((System.nanoTime() - startTime)) + "ms");

		// Part 2
		startTime = System.nanoTime();
		int[] newReminders = new int[lines.size()+1];
		int[] newModulo = new int[lines.size()+1];
		System.arraycopy(reminders, 0, newReminders, 0, lines.size());
		System.arraycopy(modulo, 0, newModulo, 0, lines.size());
		newReminders[lines.size()] = getInRangeRotated(0 - (lines.size() + 1), 11);	
		newModulo[lines.size()] = 11;		
		result = chineseRemainder(newReminders, newModulo);
		
		System.out.println("Result part 2 : " + result + " in " + TimeUnit.NANOSECONDS.toMillis((System.nanoTime() - startTime)) + "ms");
	}
	
	public static int getInRangeRotated(int x, int width) {
		return ((x % width) + width) % width;
	}
	
	public static long chineseRemainder(int[] a, int[] m) {
		
        if (a.length != m.length) {
            throw new IllegalArgumentException("arrays 'a' and 'm' must be the same length");
        }

        // Compute M = m1*m2*...*mn (product of all modulo)
        long M = Arrays.stream(m).reduce(1, (x,y) -> x*y);

        long result = (IntStream.range(0, m.length).mapToLong(i -> {

        	// For each equation, computes Mi which is M divided by the current modulo.
        	 long Mi = M / m[i];
        	
        	 // Compute the multiplicative inverse of each Mi modulo mi. 
        	 // That is, it finds a number yi such that (Mi*yi) is congruent to 1 modulo mi.
             long MiInverse = modInverse(Mi, m[i]);
        	 return a[i] * Mi * MiInverse;
        	 
        }).sum()) % M;

        return result;
    }

    /**
     * Compute the modular inverse using the Extended Euclidean Algorithm.
     * @param a
     * @param m
     * @return
     */
    private static long modInverse(long a, long m) {
        long m0 = m;
        long y = 0, x = 1;

        if (m == 1) {
            return 0;
        }

        while (a > 1) {
            long q = a / m;
            long t = m;

            m = a % m;
            a = t;
            t = y;

            y = x - q * y;
            x = t;
        }

        if (x < 0) {
            x += m0;
        }

        return x;
    }
}
