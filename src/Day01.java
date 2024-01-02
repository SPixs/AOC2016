import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class Day01 {

	private enum Direction {
		
		UP(0,1), RIGHT(1, 0), DOWN(0,-1), LEFT(-1, 0);

		private int dy;
		private int dx;

		Direction(int dx, int dy) {
			this.dx = dx;
			this.dy = dy;
		}
		
		private Direction turnRight() {
			return Direction.values()[(ordinal()+1)%Direction.values().length];
		} 

		private Direction turnLeft() {
			return Direction.values()[(ordinal()-1+Direction.values().length)%Direction.values().length];
		} 
	}
	
	public static void main(String[] args) throws IOException {

		List<String> lines = Files.readAllLines(Path.of("input/input_day01.txt"));
		
		// Part 1
		long startTime = System.nanoTime();
		int result = 0;
		
		int x = 0;
		int y = 0;
		Direction direction = Direction.UP;
		String[] moves = lines.get(0).split(",");
		for (String move : moves) {
			move = move.trim();
			if (move.startsWith("R")) direction = direction.turnRight();
			if (move.startsWith("L")) direction = direction.turnLeft();
			int steps = Integer.parseInt(move.substring(1));
			for (int i=0;i<steps;i++) {
				x += direction.dx;
				y += direction.dy;
			}
		}
		result = Math.abs(x) + Math.abs(y);
		
		System.out.println("Result part 1 : " + result + " in " + TimeUnit.NANOSECONDS.toMillis((System.nanoTime() - startTime)) + "ms");

		// Part 2
		x = y = 0;
		Set<Point> points = new HashSet<Point>();
		points.add(new Point(x, y));
		direction = Direction.UP;
		boolean visitTwice = false;
		for (int i=0;i<moves.length && !visitTwice;i++) {
			String move = moves[i];
			move = move.trim();
			if (move.startsWith("R")) direction = direction.turnRight();
			if (move.startsWith("L")) direction = direction.turnLeft();
			int steps = Integer.parseInt(move.substring(1));
			for (int j=0;j<steps && !visitTwice;j++) {
				x += direction.dx;
				y += direction.dy;
				Point p = new Point(x, y);
				visitTwice = points.contains(p);
				points.add(p);
			}
		}
		result = Math.abs(x) + Math.abs(y);
		
		startTime = System.nanoTime();
		System.out.println("Result part 2 : " + result + " in " + TimeUnit.NANOSECONDS.toMillis((System.nanoTime() - startTime)) + "ms");
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
