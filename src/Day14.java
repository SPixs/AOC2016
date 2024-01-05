import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class Day14 {

	public static void main(String[] args) throws IOException {

		List<String> lines = Files.readAllLines(Path.of("input/input_day14.txt"));
		
		String salt = lines.get(0);
		
		// Part 1
		long startTime = System.nanoTime();
		
		BigInteger index = BigInteger.ZERO;
		List<BigInteger> keysIndex = new ArrayList<BigInteger>();
		
		while (keysIndex.size() < 64) {
			char[] hash = computeMD5(salt + index, false);
			char tripleChar = getTripleChar(hash);
			if (tripleChar != Character.MIN_VALUE) {
				BigInteger fiveInARowIndex = checkForFiveInARow(index, tripleChar, salt, false);
				if (fiveInARowIndex != null) {
					keysIndex.add(index);
				}
			}
			index = index.add(BigInteger.ONE);
		}
		
		System.out.println("Result part 1 : " + keysIndex.get(keysIndex.size()-1) + " in " + TimeUnit.NANOSECONDS.toMillis((System.nanoTime() - startTime)) + "ms");

		// Part 2
		startTime = System.nanoTime();
		index = BigInteger.ZERO;
		keysIndex = new ArrayList<BigInteger>();
		while (keysIndex.size() < 64) {
			char[] hash = computeMD5(salt + index, true);
			char tripleChar = getTripleChar(hash);
			if (tripleChar != Character.MIN_VALUE) {
				BigInteger fiveInARowIndex = checkForFiveInARow(index, tripleChar, salt, true);
				if (fiveInARowIndex != null) {
					keysIndex.add(index);
				}
			}
			index = index.add(BigInteger.ONE);
		}
		
		System.out.println("Result part 2 : " + keysIndex.get(keysIndex.size()-1) + " in " + TimeUnit.NANOSECONDS.toMillis((System.nanoTime() - startTime)) + "ms");
	}
	
	private static BigInteger checkForFiveInARow(BigInteger index, char tripleChar, String salt, boolean stretched) {
		for (int i=1;i<=1000;i++) {
			char[] hash = computeMD5(salt+(index.add(BigInteger.valueOf(i))), stretched);
			if (hasFiveInARow(hash, tripleChar)) return index.add(BigInteger.valueOf(i));
		}
		return null;
	}

	private static boolean hasFiveInARow(char[] hash, char c) {
		for (int i=0;i<hash.length-4;i++) {
			if (hash[i] == c && hash[i+1] == c && hash[i+2] == c && hash[i+3] == c && hash[i+4] == c) {
				return true;
			}
		}
		return false;
	}

	private static char getTripleChar(char[] hash) {
		for (int i=0;i<hash.length-2;i++) {
			if (hash[i] == hash[i+1] & hash[i+1] == hash[i+2]) {
				return hash[i];
			}
		}
		return Character.MIN_VALUE;
	}
	
	private static MessageDigest md5 = null;
	
	private static Map<String, String> cache = new HashMap<String, String>();
	
	public static char[] computeMD5(String message, boolean streched) {
		
		if (streched && cache.containsKey(message)) return cache.get(message).toCharArray();
		
		// Get a MessageDigest object for the MD5 algorithm
		try {
			if (md5 == null) {
				md5 = MessageDigest.getInstance("MD5");
			}
			md5.reset();
			md5.update(message.getBytes());
			if (streched) {
				String s = new String(encodeHex(md5.digest()));
				for (int i=0;i<2016;i++) {
					md5.reset();
					md5.update(s.getBytes());
					s = new String(encodeHex(md5.digest()));
				}
				cache.put(message, s);
				return s.toCharArray();
			}
			
		   return encodeHex(md5.digest());
		} 
		catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return null;
		}
	}
	
    private static final char[] DIGITS_LOWER = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    protected static char[] encodeHex(final byte[] data) {
        final int l = data.length;
        final char[] out = new char[l << 1];
        int j = 0;
        for (byte b : data) {
            out[j++] = DIGITS_LOWER[(0xF0 & b) >>> 4];
            out[j++] = DIGITS_LOWER[0x0F & b];
        }
        return out;
    }

}
