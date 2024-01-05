import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Day12 {

	public static void main(String[] args) throws IOException {

		List<String> lines = Files.readAllLines(Path.of("input/input_day12.txt"));

		// Part 1
		long startTime = System.nanoTime();

		Cpu cpu = new Cpu(lines);
		while (cpu.execute()) {}
		
		System.out.println("Result part 1 : " + cpu.a + " in " + TimeUnit.NANOSECONDS.toMillis((System.nanoTime() - startTime)) + "ms");

		// Part 2
		cpu.reset();
		cpu.c = 1;
		while (cpu.execute()) {}
		
		startTime = System.nanoTime();
		System.out.println("Result part 2 : " + cpu.a + " in " + TimeUnit.NANOSECONDS.toMillis((System.nanoTime() - startTime)) + "ms");
	}

	public static class Cpu {

		private long a;
		private long b;
		private long c;
		private long d;

		private int pc;

		private List<String> ram;
		
		public Cpu(List<String> ram) {
			this.ram = ram;
		}
		
		public void reset() {
			a = 0;
			b = 0;
			c = 0;
			d = 0;
			pc = 0;
		}

		public boolean execute() {
			// fetch
			if (pc < 0 || pc >= ram.size()) return false;
			String instruction = ram.get(pc++);
			String opcode = instruction.substring(0, instruction.indexOf(" ")).trim();
			String operand = instruction.substring(instruction.indexOf(" ")).trim();

			// execute
			getInstruction(opcode).execute(this, operand);
			return true;
		}

		private Instruction getInstruction(String opcode) {
			switch (opcode) {
				case "cpy": return new InstructionCPY();
				case "inc": return new InstructionINC();
				case "dec": return new InstructionDEC();
				case "jnz": return new InstructionJNZ();
				default:
					throw new IllegalArgumentException();
			}
		}

		public long getRegisterValue(String reg) {
			switch (reg) {
				case "a": return a;
				case "b": return b;
				case "c": return c;
				case "d": return d;
				default: throw new IllegalArgumentException();
			}
		}

		public void setRegisterValue(String reg, long value) {
			switch (reg) {
				case "a": a = value; break;
				case "b": b = value; break;
				case "c": c = value; break;
				case "d": d = value; break;
				default: throw new IllegalArgumentException();
			}
		}
	}
	
	public static abstract class Instruction {

		public abstract void execute(Cpu cpu, String operand);
	}
	
	public static boolean isNumeric(String str) {
        return str.matches("-?\\d+(\\.\\d+)?");  // Accepte les entiers et les décimaux
    }
	
	public static class InstructionCPY extends Instruction {

		@Override
		public void execute(Cpu cpu, String operand) {
			String src = operand.split(" ")[0];
			String dest = operand.split(" ")[1];
			
			long value = isNumeric(src) ? Integer.parseInt(src) : cpu.getRegisterValue(src);
			cpu.setRegisterValue(dest, value);
		}
	}

	public static class InstructionINC extends Instruction {

		@Override
		public void execute(Cpu cpu, String reg) {
			long value = cpu.getRegisterValue(reg);
			value = (value + 1) & 0x0FFFFFFFFl;
			cpu.setRegisterValue(reg, value);
		}
	}

	public static class InstructionDEC extends Instruction {

		@Override
		public void execute(Cpu cpu, String reg) {
			long value = cpu.getRegisterValue(reg);
			value = (value - 1) & 0x0FFFFFFFFl;
			cpu.setRegisterValue(reg, value);
		}
	}
	
	public static class InstructionJNZ extends Instruction {

		@Override
		public void execute(Cpu cpu, String operand) {

			String src = operand.split(" ")[0];
			int offset = Integer.parseInt(operand.split(" ")[1]);
			
			long value = isNumeric(src) ? Integer.parseInt(src) : cpu.getRegisterValue(src);
			
			if (value != 0) {
				cpu.pc += (offset - 1);
			}
		}
	}
}
