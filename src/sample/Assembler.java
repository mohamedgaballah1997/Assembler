package sample;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.Map.Entry;

public class Assembler {
	static ArrayList < Line > Lines = new ArrayList < > ();
	public static ArrayList < String > output = new ArrayList < > ();
	public static ArrayList < String > symT = new ArrayList < > ();
	public static String[] code;
	public static boolean noErrors;
	static int cout = 0, size = 0;
	public static ArrayList<String> literals=new ArrayList<>();
	public static int literalsSize=0;
	public static void setCode(String c) {
		code = c.split("\n");
	}

	public static void refresh() {
		symT.clear();
		output.clear();
		Lines.clear();
		cout = 0;
		size = 0;
	}
	public static void readFile() {
		int i = -1;
		noErrors = true;
		//File file = new File("codelines.txt");

		{
			try {
				//sc = new Scanner(file);
				//while(sc.hasNextLine())

				size = code.length - 1;
				for (String a: code) {
					cout++;
					String linevalue = a;
					if (linevalue.startsWith(".")) {
						printLine(linevalue);
						continue;
					} else {
						if (i == -1 && linevalue.length() >= 9) {
							
							String temp = linevalue.substring(9, 14);
							if (!temp.equalsIgnoreCase("start")) {
								ErrorHandle.getError(15);
								return;
							}
						}
						//System.out.println(linevalue);
						linevalue = linevalue.toUpperCase();
						Line l = new Line(linevalue);
						
						if (l.isCorrect == true) {
							i++;
							
							Lines.add(l);
						printAddress(linevalue, i);
							//change pc
						} else {
							printLine(linevalue);
							noErrors = false;
						}
					}
				}
				PhaseII.pass2(Lines);
				Line.extractLiterals();
				if (!(Lines.get(Lines.size() - 1).opcode.equalsIgnoreCase("END"))) {
					ErrorHandle.getError(13);
				}
				Iterator it = Data.symTab.entrySet().iterator();
				while (it.hasNext()) {
					Entry pair = (Entry) it.next();
					if (((Symbol) pair.getValue()).adress == -1) {

						ErrorHandle.getError(9);
						int j = Assembler.output.size() - 1;
						String s = Assembler.output.get(j) + " [" + pair.getKey() + "]";
						Assembler.output.remove(j);
						Assembler.output.add(j, s);
					}
					System.out.println(pair.getKey() + "= " + Integer.toHexString(((Symbol) pair.getValue()).adress));
					String sym;
					if (((Symbol) pair.getValue()).adress == -1) {
						sym = "\t\t" + pair.getKey() + "\t\t=\t\tUndefined";
					} else sym = "\t\t" + pair.getKey() + "\t\t=\t\t" + Integer.toHexString(((Symbol) pair.getValue()).adress);
					symT.add(sym);
					it.remove();
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	public static void assemble() {
		new ErrorHandle();
		String header = "Line no\taddress\tlabel \tOpcode\toperands comments";
		output.add(header);
		readFile();
		//		if(noErrors)
		//			PhaseII.pass2(Lines);
		//		String name="#gaballah";
		//		name=name.replace("#", "");
		//		System.out.println(name);
	}
	public static boolean hasNext() {
		return cout < size;
	}
	public static void printAddress(String lineval, int index) {
		String[] arrOfStr = lineval.split(" ");

		lineval = "";
		for (String a: arrOfStr)
			lineval += a + "  ";

		output.add(index + 1 + "\t" + "\t" + Integer.toHexString(Lines.get(index).address) + "\t\t" + lineval);
	}
	public static void printLine(String line) {
		output.add(line);
	}


}