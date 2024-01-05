import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Day11 {

	public static void main(String[] args) throws IOException {

		List<String> lines = Files.readAllLines(Path.of("input/input_day11.txt"));

		List<Set<String>> generatorsByLevel = new ArrayList<Set<String>>();
		List<Set<String>> chipsByLevel = new ArrayList<Set<String>>();
		
		String generatorRegex = "\\b(\\w+)\\s+generator\\b";
		Pattern generatorPattern = Pattern.compile(generatorRegex);
		
		String regex = "\\b(\\w+)-compatible\\s+microchip\\b";
        Pattern pattern = Pattern.compile(regex);
        
		for (int i=0;i<lines.size();i++) {
	        Matcher matcher = pattern.matcher(lines.get(i));
	        Set<String> chips = new HashSet<String>();
	        while (matcher.find()) {
	        	chips.add(matcher.group(1));
	        }

	        matcher = generatorPattern.matcher(lines.get(i));
	        Set<String> generators = new HashSet<String>();
	        while (matcher.find()) {
	        	generators.add(matcher.group(1));
	        }
	        
	        generatorsByLevel.add(generators);
	        chipsByLevel.add(chips);
		}
		
		// Part 1
		long startTime = System.nanoTime();
		
		Context context = new Context();
		context.chipsByLevel = chipsByLevel;
		context.generatorsByLevel = generatorsByLevel;
		context.elevatorLevel = 0;
		
		int minSteps = bfs(context);

		System.out.println("Result part 1 : " + minSteps + " in " + TimeUnit.NANOSECONDS.toMillis((System.nanoTime() - startTime)) + "ms");

		// Part 2
		startTime = System.nanoTime();
		context.chipsByLevel.get(0).add("elerium");
		context.chipsByLevel.get(0).add("dilithium");
		context.generatorsByLevel.get(0).add("elerium");
		context.generatorsByLevel.get(0).add("dilithium");
		
		minSteps = bfs(context);
		
		System.out.println("Result part 2 : " + minSteps + " in " + TimeUnit.NANOSECONDS.toMillis((System.nanoTime() - startTime)) + "ms");
	}
	
	private static int bfs(Context context) {
		
		Set<Long> alreadProcessed = new HashSet<Long>();
		List<Context> toProcess = new ArrayList<Context>();
		
		alreadProcessed.add(context.getHash());
		toProcess.add(context);
		
		while (!toProcess.isEmpty()) {
			List<Context> newToProcess = new ArrayList<Context>();
			for (Context nextContextToProcess : toProcess)  {
				
				List<Action> actions = nextContextToProcess.generateActions();
				for (Action action : actions) {
					Context newContext = nextContextToProcess.getCopy();
					action.perform(newContext);
					Long newHash = newContext.getHash();
					if (!alreadProcessed.contains(newHash)) {
						if (newContext.isValid()) {
							alreadProcessed.add(newHash);
							if (newContext.isTerminal()) return newContext.steps;
							newToProcess.add(newContext);
						}
					}
				}
			}
			toProcess = newToProcess;
		}
		
		return Integer.MAX_VALUE;
	}

	public static class Context {
		
		public List<Set<String>> generatorsByLevel = new ArrayList<Set<String>>();
		public List<Set<String>> chipsByLevel = new ArrayList<Set<String>>();
		public int elevatorLevel = 0;
		public int steps = 0;
		
		public List<Action> generateActions() {
			List<Action> actions = new ArrayList<Action>();
			List<String> chips = new ArrayList<String>(chipsByLevel.get(elevatorLevel));
			List<String> generators = new ArrayList<String>(generatorsByLevel.get(elevatorLevel));
			for (int i=0;i<chips.size();i++) {
				String chip = chips.get(i);
				
				// Move UP
				if (elevatorLevel <= chipsByLevel.size()-2) {
					if (!(generators.contains(chip) && !generatorsByLevel.get(elevatorLevel+1).isEmpty())) {
						actions.add(new MoveUpAction(Arrays.asList(chip), Collections.emptyList()));
					}
					for (int j=i+1;j<chips.size();j++) {
						actions.add(new MoveUpAction(Arrays.asList(chip, chips.get(j)), Collections.emptyList()));
					}
					if (generators.contains(chip)) {
						actions.add(new MoveUpAction(Arrays.asList(chip), Arrays.asList(chip)));
					}
				}
				
				// Move DOWN
				if (elevatorLevel > 0) {
					// Don 't move chip or generator down if lower floors are empty
					boolean shouldMoveDown = false;
					for (int l=0;l<elevatorLevel && !shouldMoveDown;l++) {
						shouldMoveDown |= !chipsByLevel.get(l).isEmpty() || !generatorsByLevel.get(l).isEmpty();
					}
					if (shouldMoveDown) {
						if (!(generators.contains(chip) && !generatorsByLevel.get(elevatorLevel-1).isEmpty())) {
							actions.add(new MoveDownAction(Arrays.asList(chip), Collections.emptyList()));
						}
						for (int j=i+1;j<chips.size();j++) {
							actions.add(new MoveDownAction(Arrays.asList(chip, chips.get(j)), Collections.emptyList()));
						}
						if (generators.contains(chip)) {
							actions.add(new MoveDownAction(Arrays.asList(chip), Arrays.asList(chip)));
						}
					}
				}
			}
			for (int i=0;i<generators.size();i++) {

				// Move UP
				String generator = generators.get(i);
				if (elevatorLevel <= chipsByLevel.size()-2) {
					if (!(chips.contains(generator) && chips.size() > 1)) {
						actions.add(new MoveUpAction(Collections.emptyList(), Arrays.asList(generator)));
					}
					for (int j=i+1;j<generators.size();j++) {
						actions.add(new MoveUpAction(Collections.emptyList(), Arrays.asList(generator, generators.get(j))));
					}
				}
				
				// Move DOWN
				if (elevatorLevel > 0) {
					// Don 't move chip or generator down if lower floors are empty
					boolean shouldMoveDown = false;
					for (int l=0;l<elevatorLevel && !shouldMoveDown;l++) {
						shouldMoveDown |= !chipsByLevel.get(l).isEmpty() || !generatorsByLevel.get(l).isEmpty();
					}
					if (shouldMoveDown) {
						if (!(chips.contains(generator) && chips.size() > 1)) {
							actions.add(new MoveDownAction(Collections.emptyList(), Arrays.asList(generator)));
						}
						for (int j=i+1;j<generators.size();j++) {
							actions.add(new MoveDownAction(Collections.emptyList(), Arrays.asList(generator, generators.get(j))));
						}
					}
				}
			}

			return actions;
		}

		public long getHash() {
			if (chipsByLevel.size() > 4) throw new IllegalStateException();
			long hash = elevatorLevel & 0x03; // 2 bits
			for (int i=0;i<chipsByLevel.size();i++) {
				hash = (hash << 4) | (chipsByLevel.get(i).size() & 0x0F); // 4 bits for level chips count
				hash = (hash << 4) | (generatorsByLevel.get(i).size() & 0x0F); // 4 bits for level chips count
			}
			return hash;
		}

		public boolean isTerminal() {
			for (int i=0;i<chipsByLevel.size()-1;i++) {
				if (generatorsByLevel.get(i).size() > 0 || chipsByLevel.get(i).size() > 0) return false;
			}
			return true;
		}
		
		public boolean isValid() {
			
			for (int i=0;i<chipsByLevel.size();i++) {
				List<String> chips = new ArrayList<String>(chipsByLevel.get(i));
				List<String> generators = new ArrayList<String>(generatorsByLevel.get(i));

				for (String chip : chips) {
					if (!generators.contains(chip)) {
						for (String generator : generators) {
							if (!generator.equals(chip)) return false; // Fried !!!
						}
					}
				}
			}
			return true;
		}
		
		public Context getCopy() {
			Context context = new Context();
			context.generatorsByLevel = getCopy(generatorsByLevel);
			context.chipsByLevel = getCopy(chipsByLevel);
			context.elevatorLevel = elevatorLevel;
			context.steps = steps;
			return context;
		}

		private List<Set<String>> getCopy(List<Set<String>> item) {
			List<Set<String>> result = new ArrayList<Set<String>>();
			for (Set<String> sublist : item) {
				result.add(new HashSet<String>(sublist));
			}
			return result;
		}
		
		private void dump() {
			for (int i=chipsByLevel.size()-1;i>=0;i--) {
				List<String> items = new ArrayList<String>();
				items.addAll(chipsByLevel.get(i).stream().map(s -> (""+s.charAt(0)+"M").toUpperCase()).collect(Collectors.toList()));
				items.addAll(generatorsByLevel.get(i).stream().map(s -> (""+s.charAt(0)+"G").toUpperCase()).collect(Collectors.toList()));
				System.out.println(items);
			}
			System.out.println("Valid = " + isValid() +", terminal = " + isTerminal());
		}
	}
	
	public static abstract class Action {

		public List<String> generators;
		public List<String> chips;

		public abstract void perform(Context context);
		
	}
	
	public static class MoveUpAction extends Action {
		

		public MoveUpAction(List<String> chips, List<String> generators) {
			if (chips == null || generators == null) throw new IllegalArgumentException();
			this.generators = generators;
			this.chips = chips;
		}
		
		@Override
		public void perform(Context context) {
			if (context.elevatorLevel >= context.chipsByLevel.size()-1) throw new IllegalStateException();
			if (generators.size() + chips.size() == 0) throw new IllegalStateException();
			if (generators.size() + chips.size() > 2) throw new IllegalStateException();
			if (!context.chipsByLevel.get(context.elevatorLevel).containsAll(chips)) throw new IllegalStateException();
			if (!context.generatorsByLevel.get(context.elevatorLevel).containsAll(generators)) throw new IllegalStateException();
			context.chipsByLevel.get(context.elevatorLevel).removeAll(chips);
			context.chipsByLevel.get(context.elevatorLevel+1).addAll(chips);
			context.generatorsByLevel.get(context.elevatorLevel).removeAll(generators);
			context.generatorsByLevel.get(context.elevatorLevel+1).addAll(generators);
			context.elevatorLevel++;
			context.steps++;
			if (chips == null || generators == null) throw new IllegalArgumentException();
		}
		
		@Override
		public String toString() {
			List<String> chipsMoved = chips.stream().map(s -> (""+s.charAt(0)+"M").toUpperCase()).collect(Collectors.toList());
			List<String> generatorsMoved = generators.stream().map(s -> (""+s.charAt(0)+"G").toUpperCase()).collect(Collectors.toList());
			return "Move UP : " + chipsMoved + "," + generatorsMoved;
		}
	}
	
	public static class MoveDownAction extends Action {

		public MoveDownAction(List<String> chips, List<String> generators) {
			if (chips == null || generators == null) throw new IllegalArgumentException();
			this.generators = generators;
			this.chips = chips;
		}
		
		@Override
		public void perform(Context context) {
			if (context.elevatorLevel <= 0) throw new IllegalStateException();
			if (generators.size() + chips.size() == 0) throw new IllegalStateException();
			if (generators.size() + chips.size() > 2) throw new IllegalStateException();
			if (!context.chipsByLevel.get(context.elevatorLevel).containsAll(chips)) throw new IllegalStateException();
			if (!context.generatorsByLevel.get(context.elevatorLevel).containsAll(generators)) throw new IllegalStateException();
			context.chipsByLevel.get(context.elevatorLevel).removeAll(chips);
			context.chipsByLevel.get(context.elevatorLevel-1).addAll(chips);
			context.generatorsByLevel.get(context.elevatorLevel).removeAll(generators);
			context.generatorsByLevel.get(context.elevatorLevel-1).addAll(generators);
			context.elevatorLevel--;
			context.steps++;
			if (chips == null || generators == null) throw new IllegalArgumentException();
		}
		
		@Override
		public String toString() {
			List<String> chipsMoved = chips.stream().map(s -> (""+s.charAt(0)+"M").toUpperCase()).collect(Collectors.toList());
			List<String> generatorsMoved = generators.stream().map(s -> (""+s.charAt(0)+"G").toUpperCase()).collect(Collectors.toList());
			return "Move DOWN : " + chipsMoved + "," + generatorsMoved;
		}
	}
}
