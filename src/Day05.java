import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

public class Day05 {

	public static void main(String[] args) throws IOException {

		List<String> lines = Files.readAllLines(Path.of("input/input_day05.txt"));
		
		// Part 1
		long startTime = System.nanoTime();
		
		String input = lines.get(0);
		
		StringBuffer code = new StringBuffer();
		BigInteger index = BigInteger.ZERO;
		while (code.length() < 8) {
			char[] hash = computeMD5(input+index);
			if (IntStream.range(0, 5).map(i -> hash[i]).allMatch(i -> i == '0')) {
				code.append(String.valueOf(hash[5]));
			}
			index = index.add(BigInteger.ONE);
		}
		
		System.out.println("Result part 1 : " + code + " in " + TimeUnit.NANOSECONDS.toMillis((System.nanoTime() - startTime)) + "ms");

		// Part 2
		startTime = System.nanoTime();
		code = new StringBuffer("########");
		index = BigInteger.ZERO;
		while (code.indexOf("#") >= 0) {
			char[] hash = computeMD5(input+index);
			if (IntStream.range(0, 5).map(i -> hash[i]).allMatch(i -> i == '0')) {
				int pos = hash[5]-'0';
				if (pos < 8 && code.charAt(pos) == '#') {
					code.setCharAt(hash[5]-'0', hash[6]);
				}
			}
			index = index.add(BigInteger.ONE);
		}
		
		System.out.println("Result part 2 : " + code + " in " + TimeUnit.NANOSECONDS.toMillis((System.nanoTime() - startTime)) + "ms");
	}
	
	private static MessageDigest md5 = null;
	
	public static char[] computeMD5(String message) {
		// Get a MessageDigest object for the MD5 algorithm
		try {
			if (md5 == null) {
				md5 = MessageDigest.getInstance("MD5");
			}
			md5.reset();
			md5.update(message.getBytes());
			
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
