import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day10 {

	public static class Robot {

		private int number;
		private int lowRobot;
		private int highRobot;
		
		private List<Integer> chips = new ArrayList<Integer>();
		private boolean isLowOutput;
		private boolean isHighOutput;
		private AtomicReference<Integer> responsibleRobotIndex;
		private Map<Integer, Integer> outputMap;

		public Robot(int robotNumber, int lowRobot, int highRobot, boolean isLowOutput, boolean isHighOutput, 
				AtomicReference<Integer> responsibleRobotIndex, Map<Integer, Integer> outputMap) {
			this.number = robotNumber;
			this.lowRobot = lowRobot;
			this.highRobot = highRobot;
			this.isLowOutput = isLowOutput;
			this.isHighOutput = isHighOutput;
			this.responsibleRobotIndex = responsibleRobotIndex;
			this.outputMap = outputMap;
		}

		public void addChip(int value, Map<Integer, Robot> robotsMap) {
			chips.add(value);
			if (chips.size() > 2) throw new IllegalStateException();
			if (chips.size() == 2) {
				if (chips.contains(61) && chips.contains(17)) responsibleRobotIndex.set(number);
				if (!isLowOutput) {
					robotsMap.get(lowRobot).addChip(Collections.min(chips), robotsMap);
				}
				else {
					outputMap.put(lowRobot, Collections.min(chips));
				}
				if (!isHighOutput) robotsMap.get(highRobot).addChip(Collections.max(chips), robotsMap);
				else {
					outputMap.put(highRobot, Collections.max(chips));
				}
				chips.clear();
			}
		}
	}

	public static class Affectation {

		private int value;
		private int robotNumber;

		public Affectation(int value, int robotNumber) {
			this.value = value;
			this.robotNumber = robotNumber;
		}

	}

	public static void main(String[] args) throws IOException {

		List<String> lines = Files.readAllLines(Path.of("input/input_day10.txt"));

		Pattern affectationPattern = Pattern.compile("value (\\d+) goes to bot (\\d+)"); 
		Pattern distributionPattern = Pattern.compile("bot (\\d+) gives low to (bot|output) (\\d+) and high to (bot|output) (\\d+)"); 
		
		// Part 1
		long startTime = System.nanoTime();
		int result = 0;
		
		List<Affectation> affectations = new ArrayList<Affectation>();
		Map<Integer, Robot> robotsMap = new HashMap<Integer, Robot>();
		
		AtomicReference<Integer> responsibleRobotIndex = new AtomicReference<Integer>(null);
		Map<Integer, Integer> outputMap = new HashMap<Integer, Integer>();
		
		for (String line : lines) {
			Matcher matcher = affectationPattern.matcher(line);
			if (matcher.find()) {
				int value = Integer.parseInt(matcher.group(1));
				int robotNumber = Integer.parseInt(matcher.group(2));
				affectations.add(new Affectation(value, robotNumber));
			}
			else {
				matcher = distributionPattern.matcher(line);
				if (matcher.find()) {
					int lowRobot = Integer.parseInt(matcher.group(3));
					int highRobot = Integer.parseInt(matcher.group(5));
					int robotNumber = Integer.parseInt(matcher.group(1));
					boolean isLowOutput = matcher.group(2).equals("output");
					boolean isHighOutput = matcher.group(4).equals("output");
					robotsMap.put(robotNumber, new Robot(robotNumber, lowRobot, highRobot, isLowOutput, isHighOutput, responsibleRobotIndex, outputMap));
				}
			}
		}

		for (Affectation affectation : affectations) {
			Robot robot = robotsMap.get(affectation.robotNumber);
			robot.addChip(affectation.value, robotsMap);
		}
		
		System.out.println("Result part 1 : " + responsibleRobotIndex.get() + " in " + TimeUnit.NANOSECONDS.toMillis((System.nanoTime() - startTime)) + "ms");

		// Part 2
		startTime = System.nanoTime();
		result = outputMap.get(0) * outputMap.get(1) * outputMap.get(2);
		System.out.println("Result part 2 : " + result + " in " + TimeUnit.NANOSECONDS.toMillis((System.nanoTime() - startTime)) + "ms");
	}
}
