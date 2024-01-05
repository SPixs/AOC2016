import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class Day13 {

	public static void main(String[] args) throws IOException {

		List<String> lines = Files.readAllLines(Path.of("input/input_day13.txt"));
		
		int favoriteNumber = Integer.parseInt(lines.get(0));
		
		// Part 1
		long startTime = System.nanoTime();
		
		int result = bfs(new Point(1, 1), new Point(31, 39), favoriteNumber);
		
		System.out.println("Result part 1 : " + result + " in " + TimeUnit.NANOSECONDS.toMillis((System.nanoTime() - startTime)) + "ms");

		// Part 2
		startTime = System.nanoTime();
		result = bfsPart2(new Point(1, 1), new Point(31, 39), favoriteNumber);
		System.out.println("Result part 2 : " + result + " in " + TimeUnit.NANOSECONDS.toMillis((System.nanoTime() - startTime)) + "ms");
	}

	private static int bfs(Point src, Point dest, int favoriteNumber) {
		
		Set<Point> toProcess = new LinkedHashSet<Point>();
		toProcess.add(src);
		
		int step = 0;
		
		Set<Point> processedPoints = new HashSet<Point>();
		processedPoints.add(src);

		while (!toProcess.isEmpty()) {

			step++;
			Set<Point> newToProcess = new LinkedHashSet<Point>();
			
			for (Point p : toProcess) {
				processedPoints.add(p);
				if (p.x > 0) {
					if (!isWall(p.x-1, p.y, favoriteNumber)) { newToProcess.add(new Point(p.x-1, p.y)); }
				}
				if (p.y > 0) {
					if (!isWall(p.x, p.y-1, favoriteNumber)) { newToProcess.add(new Point(p.x, p.y-1)); }
				}

				if (!isWall(p.x+1, p.y, favoriteNumber)) { newToProcess.add(new Point(p.x+1, p.y)); }
				if (!isWall(p.x, p.y+1, favoriteNumber)) { newToProcess.add(new Point(p.x, p.y+1)); }
				
				if (newToProcess.contains(dest)) return step;
			}
			newToProcess.removeAll(processedPoints);
			
			toProcess = newToProcess;
		}
		
		return Integer.MAX_VALUE;
	}
	
	private static int bfsPart2(Point src, Point dest, int favoriteNumber) {
		
		Set<Point> toProcess = new LinkedHashSet<Point>();
		toProcess.add(src);
		
		int step = 0;
		
		Set<Point> processedPoints = new HashSet<Point>();
		processedPoints.add(src);
		
		while (step <= 50) {

			step++;
			Set<Point> newToProcess = new LinkedHashSet<Point>();
			
			for (Point p : toProcess) {
				processedPoints.add(p);
				if (p.x > 0) {
					if (!isWall(p.x-1, p.y, favoriteNumber)) { newToProcess.add(new Point(p.x-1, p.y)); }
				}
				if (p.y > 0) {
					if (!isWall(p.x, p.y-1, favoriteNumber)) { newToProcess.add(new Point(p.x, p.y-1)); }
				}

				if (!isWall(p.x+1, p.y, favoriteNumber)) { newToProcess.add(new Point(p.x+1, p.y)); }
				if (!isWall(p.x, p.y+1, favoriteNumber)) { newToProcess.add(new Point(p.x, p.y+1)); }
				
				if (newToProcess.contains(dest)) return step;
			}
			newToProcess.removeAll(processedPoints);
			
			toProcess = newToProcess;
		}
		
		return processedPoints.size();
	}

	public static boolean isWall(int x, int y, int favoriteNumber) {
		int val = x*x + 3*x + 2*x*y + y + y*y + favoriteNumber;
		String binaryString = Integer.toBinaryString(val);
		return binaryString.chars().filter(c -> c == '1').count() % 2 == 1;
	}
	
	public static class Point {
		
		private int x;
		private int y;

		public Point(int x, int y) {
			super();
			this.x = x;
			this.y = y;
		}

		public Point(Point location) {
			x = location.x;
			y = location.y;
		}
		
		public Point getTranslated(int dx, int dy) {
			return new Point(x+dx, y+dy);
		}
		
		public int getX() {
			return x;
		}
		
		public int getY() {
			return y;
		}
		
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + x;
			result = prime * result + y;
			return result;
		}
		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Point other = (Point) obj;
			if (x != other.x)
				return false;
			if (y != other.y)
				return false;
			return true;
		}
		
		@Override
		public String toString() {
			return "["+x+","+y+"]";
		}
	}
}
