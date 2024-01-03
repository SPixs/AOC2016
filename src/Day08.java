import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Day08 {

	public static void main(String[] args) throws IOException {

		List<String> lines = Files.readAllLines(Path.of("input/input_day08.txt"));

		char[][] screen = new char[50][6];
		for (int y=0;y<6;y++) {
			for (int x=0;x<50;x++) {
				screen[x][y] = '.';
			}			
		}
		
		// Part 1
		long startTime = System.nanoTime();
		int result = 0;

		for (String line : lines) {
		 	if (line.contains("rect")) {
		 		int width = Integer.parseInt(line.split(" ")[1].split("x")[0]);
		 		int height = Integer.parseInt(line.split(" ")[1].split("x")[1]);
		 		fillRect(screen, width, height);
		 	}
		 	else if (line.contains("rotate row")) {
		 		int row = Integer.parseInt(line.substring(13).split(" by ")[0]);
		 		int count = Integer.parseInt(line.substring(13).split(" by ")[1]);
		 		rotateRow(screen, row, count);
		 	}
		 	else if (line.contains("rotate column")) {
		 		int col = Integer.parseInt(line.substring(16).split(" by ")[0]);
		 		int count = Integer.parseInt(line.substring(16).split(" by ")[1]);
		 		rotateCol(screen, col, count);
		 	}
		}
		
		for (int y=0;y<6;y++) {
			for (int x=0;x<50;x++) {
				result += (screen[x][y] == '#') ? 1 : 0;
			}			
		}
		System.out.println("Result part 1 : " + result + " in " + TimeUnit.NANOSECONDS.toMillis((System.nanoTime() - startTime)) + "ms");

		// Part 2
		startTime = System.nanoTime();
		dump(screen);
		System.out.println("Result part 2 in " + TimeUnit.NANOSECONDS.toMillis((System.nanoTime() - startTime)) + "ms");
	}

	public static void dump(char[][] screen) {
		for (int y=0;y<6;y++) {
			for (int x=0;x<50;x++) {
				System.out.print(screen[x][y]);
			}			
			System.out.println();
		}
	}
	
	private static void rotateCol(char[][] screen, int col, int count) {
		for (int i=0;i<count;i++) {
			char last = screen[col][5];
			for (int y=5;y>0;y--) {
				screen[col][y] = screen[col][y-1];
			}
			screen[col][0] = last;
		}
	}

	private static void rotateRow(char[][] screen, int row, int count) {
		for (int i=0;i<count;i++) {
			char last = screen[49][row];
			for (int x=49;x>0;x--) {
				screen[x][row] = screen[x-1][row];
			}
			screen[0][row] = last;
		}
	}

	private static void fillRect(char[][] screen, int width, int height) {
		for (int y=0;y<height;y++) {
			for (int x=0;x<width;x++) {
				screen[x][y] = '#';
			}			
		}
	}
}
