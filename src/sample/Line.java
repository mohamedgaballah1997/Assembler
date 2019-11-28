package sample;
public class Line {
	String linevalue;
	int length;
	public static int pc;
	String label;
	String opcode;
	Operation operation;
	int format;
	String operand1 = null;
	String operand2 = null;
	String comment;
	int address;
	int index;
	int addressingmode;
	boolean isCorrect;

	public Line(String linevalue) {
		super();
		isCorrect = true;
		this.linevalue = linevalue;
		address = pc;
		splitLine();
		if (!isCorrect) return;
		length=pc;
		if (!operation.type.equals(Table.directive)) {
			//Line l=Assembler.Lines.get(arg0)
			if (opcode.equalsIgnoreCase("resw")) {
				pc += 3 * Integer.parseInt(operand1);
			} else if (opcode.equalsIgnoreCase("resb")) {
				pc += 1 * Integer.parseInt(operand1);
			} else if (opcode.equalsIgnoreCase("word")) {
				pc += 3;
			} else if (opcode.equalsIgnoreCase("byte")) {
				if (addressingmode == 5)
					pc += (operand1.length());
				else
					pc += (operand1.length()) / 2;
			} else if (opcode.equalsIgnoreCase("equ")) {
				pc = pc;
			}else if(opcode.equalsIgnoreCase("LT"))
			{
				extractLiterals();
				pc+=Assembler.literalsSize;
				Assembler.literalsSize=0;
			}

			else
				pc += format;
		}
		if (!label.equals("")) {
			if (!Data.addSymbol(label, address)) {
				isCorrect = false;
				return;
			}
		}
length=pc-length;
	}

	public void spaceCheck() {
		if ((linevalue.charAt(0) == ' ') && !label.trim().equals("")) {
			isCorrect = false;
			ErrorHandle.getError(1);
		} else if (linevalue.length() >= 9 && !((linevalue.charAt(8) == '+') || (linevalue.charAt(8) == ' '))) {
			isCorrect = false;
			ErrorHandle.getError(7);

		} else if (linevalue.length() < 10 || (linevalue.charAt(9) == ' ')) {

			isCorrect = false;
			ErrorHandle.getError(2);
		} else if ( format != 1 &&(linevalue.length() < 17 || !((linevalue.charAt(15) == ' ') || (linevalue.charAt(16) == ' ')))) {
			isCorrect = false;
			ErrorHandle.getError(3);
		}
	}

	public void opcodeCheck() {

		new Data();
		operation = Data.map.get(opcode.toUpperCase());

		if (operation == null) {
			isCorrect = false;
			ErrorHandle.getError(8);
			return;
		} else if ((operation.op.equalsIgnoreCase("start") ||
				operation.op.equalsIgnoreCase("end") ||
				operation.op.equalsIgnoreCase("org")) && !label.equals("")) {
			ErrorHandle.getError(5);
			isCorrect = false;
		}
		format = operation.format;
	}

	public void splitLine() {
		//**WAITING FOR IMM check if operand exists or not
		// **DONEE** remove spaces between .trim();
		// **Done** check that it starts with Start

		//length = linevalue.length();
		String operands;
		if (linevalue.length() < 9) {
			label = linevalue;
			label = label.trim();
			isCorrect = false;
			ErrorHandle.getError(2);
			return;
		} else if (linevalue.length() < 16) {
			// check if it is END or ORG
			label = linevalue.substring(0, 7);
			label = label.trim();
			opcode = linevalue.substring(9, linevalue.length());
			opcode = opcode.trim();
			if (linevalue.charAt(8) == '+') {
				format = 4;
				String plus = "";
				plus += "+";
				String[] splitted1 = opcode.split(plus);
				opcode = splitted1[1];
			}
			opcodeCheck();
			if (!isCorrect) return;
			spaceCheck();
			if (!isCorrect) return;
		} else if (linevalue.length() < 36) {
			label = linevalue.substring(0, 7);
			label = label.trim();
			opcode = linevalue.substring(9, 14);
			opcode = opcode.trim();
			opcodeCheck();

			if (!isCorrect) return;
			spaceCheck();

			if (!isCorrect) return;
			if (linevalue.charAt(8) == '+' && operation.format == 3) {
				format = 4;
			} else if (linevalue.charAt(8) == '+' && operation.format != 3) {
				isCorrect = false;
				ErrorHandle.getError(11);
				return;
			}

			if (format != 1 && (linevalue.length() <= 17 || linevalue.charAt(17) == ' ')) {
				if (!opcode.equalsIgnoreCase("End")) {
					isCorrect = false;
					ErrorHandle.getError(3);
					return;
				}
			}
			operand1 = linevalue.substring(17, linevalue.length());
			operand1 = operand1.trim();
			if (opcode.equals("START")) {
				try {
					pc = Integer.parseInt(operand1, 16);
					address = pc;
				} catch (Exception e) {
					ErrorHandle.getError(2);
					isCorrect = false;
					return;
				}
			} else if (opcode.equals("ORG")) {
				try {
					pc = Integer.parseInt(operand1);
				} catch (Exception e) {
					if (Data.symTab.containsValue(operand1) && Data.symTab.get(operand1).adress != -1) {
						pc = Data.symTab.get(operand1).adress;
					} else {
						ErrorHandle.getError(2);
						isCorrect = false;
					}
					return;
				}
			}
		} else {

			label = linevalue.substring(0, 7);

			label = label.trim();
			opcode = linevalue.substring(9, 14);
			opcode = opcode.trim();
			opcodeCheck();
			if (!isCorrect) return;
			spaceCheck();
			if (!isCorrect) return;
			if (linevalue.charAt(8) == '+' && operation.format == 3) {
				format = 4;
			} else if (linevalue.charAt(8) == '+' && operation.format != 3) {
				isCorrect = false;
				ErrorHandle.getError(11);
				return;
			}

			if ((linevalue.charAt(17) == ' ') && format != 1) {
				isCorrect = false;
				ErrorHandle.getError(3);
				return;
			}
			operand1 = linevalue.substring(17, 34);
			if (opcode.equalsIgnoreCase("START") || opcode.equalsIgnoreCase("org")) {
				try {
					pc = Integer.parseInt(operand1);
				} catch (Exception e) {
					ErrorHandle.getError(2);
					isCorrect = false;
					return;
				}
			}
			comment = linevalue.substring(35, linevalue.length());
			comment = comment.trim();
		}
		if (linevalue.length() > 16) {
			operands = operand1;
			if (operands.contains(",")) {
				String[] splitted = operands.split(",");
				operand1 = splitted[0];
				operand2 = splitted[1];
				operand2 = operand2.trim();

			}
			operand1 = operand1.trim();
			operandCheck();

			if (opcode.equalsIgnoreCase("BYTE")) {
				if (operand1.toLowerCase().startsWith("x'") && operand1.endsWith("'")) {
					String str = operand1.substring(2, operand1.length() - 1);
					addressingmode = 4;
					try {
						int value = Integer.parseInt(str, 16);
						operand1 = str;

						if (operand1.length() > 14) {
							ErrorHandle.getError(17);
							isCorrect = false;
							return;
						}
						if (operand1.length() % 2 == 1) {
							ErrorHandle.getError(14);
							isCorrect = false;
							return;
						}
						//Data.addSymbol(label, address);
					} catch (Exception e) {
						ErrorHandle.getError(10);
						isCorrect = false;
						return;
					}
				} else if (operand1.toLowerCase().startsWith("c'") && operand1.endsWith("'")) {
					String str = operand1.substring(2, operand1.length() - 1);
					addressingmode = 5;
					if (str.length() > 15) {
						ErrorHandle.getError(17);
					} else {
						operand1 = str;
					}

				}
			} else if (opcode.equalsIgnoreCase("WORD") || opcode.equalsIgnoreCase("RESW") || opcode.equalsIgnoreCase("RESB")) {
				try {
					int value = Integer.parseInt(operand1);
				} catch (Exception e) {
					ErrorHandle.getError(9);
					isCorrect = false;
					return;
				}
			} else if (opcode.equalsIgnoreCase("START")) {
				if (Assembler.Lines.size() > 0) {
					isCorrect = false;
					ErrorHandle.getError(15);
				}

				try {
					pc = Integer.parseInt(operand1, 16);
				} catch (Exception e) {
					ErrorHandle.getError(2);
					isCorrect = false;
					return;
				}
			} else if (opcode.equalsIgnoreCase("End")) {
				if (Assembler.hasNext()) {
					ErrorHandle.getError(16);
					isCorrect = false;
					return;
				}
				if (Assembler.Lines.size() >= 2 && !operand1.equals(Assembler.Lines.get(1).label)) {

					if (!operand1.equals("") && operand1 != null) {
						ErrorHandle.getError(18);
						isCorrect = false;
						return;
					}
				}
			} else if (opcode.equalsIgnoreCase("EQU")) {
				String arr[];

				if (!label.equals("")) {
					// if not included or included and adress=-1
					if (!Data.symTab.containsValue(operand1) || Data.symTab.get(operand1).adress == -1) {
						isCorrect = false;
						return;
					}
					if (operand1.contains("+")) {
						int value;
						arr = operand1.split("'+'");
						operand1 = arr[0];
						operand2 = arr[1];

						try {
							value = Integer.parseInt(operand2);
						} catch (Exception e) {
							ErrorHandle.getError(9);
							isCorrect = false;
							return;
						}
						boolean t = Data.addSymbol(label, Data.symTab.get(operand1).adress + value);
						//error
						if (!t)
							return;

					} else if (operand1.contains("-")) {
						int value;
						arr = operand1.split("-");
						operand1 = arr[0];
						operand2 = arr[1];
						try {
							value = Integer.parseInt(operand2);
							//value*=-1;
							operand2 = Integer.toString(value);
						} catch (Exception e) {
							ErrorHandle.getError(9);
							isCorrect = false;
							return;
						}

						boolean t = Data.addSymbol(label, Data.symTab.get(operand1).adress - value);
						if (!t)
							return;
					} else if (operand2 == null) {
						boolean t = Data.addSymbol(label, Data.symTab.get(operand1).adress);
						if (!t)
							return;
					} else {
						ErrorHandle.getError(9);
					}
				} else
					ErrorHandle.getError(19);
			}


		}

	}

	public void operandCheck() {
		// check opcode validity first and put format
		// check 2 formats, already got from + and what is send
		if (operand1 != null) {
			switch (format) {
				case 1:
					ErrorHandle.getError(6);
					isCorrect = false;
					return;
				//	 break;
				case 2:

					if (Data.registers.get(operand1) == null || Data.registers.get(operand2) == null) {
		
						ErrorHandle.getError(3);
						isCorrect = false;
						return;
					}
					break;
				case 3:
					if (operand2 != null) {
						if (!operand2.equalsIgnoreCase("x")) {
							ErrorHandle.getError(3);
							isCorrect = false;
							return;
						} else {
							addressingmode = 2;
						}

					}

					if (operand1.startsWith("#")) {
						addressingmode = 0;
						operand1 = operand1.replace("#", "");
					} else if (operand1.startsWith("@")) {
						 addressingmode = 1;
						operand1 = operand1.replace("@", "");
					}
					else {
						if(addressingmode!=2)
						 addressingmode = 3;
					}
					try {
						Integer.parseInt(operand1, 16);
					} catch (Exception e) {

						if (!operation.type.equals(Table.storage)) {

							// ah shit here we go again
							int x;
							if((x=isLiteral(operand1))>0)
							{
								if(Data.symTab.containsKey(operand1))
								{
									int value=Data.symTab.get(operand1).adress;
									if(value==-1)
									{
										if(!Assembler.literals.contains(operand1))
										{
											Assembler.literalsSize+=x;
											Assembler.literals.add(operand1);
										}
									}
								}else {
									Assembler.literalsSize+=x;
									Assembler.literals.add(operand1);
								}
							}
							if (!Data.addSymbol(operand1, -1)) {
								isCorrect = false;
								return;
							}
						}
					}
					break;
			}

		}

		// check .format
		if ((Data.registers.containsKey(operand1.toUpperCase()) == false)) {
			if (operand1.startsWith("#")) {
				String[] splitted1 = operand1.split("#");
				operand1 = splitted1[1];
				addressingmode = 0;
				// check if !is number check present in symtb if format 3 || regtable if format 2or3
			} else if (operand1.startsWith("@")) {
				String[] splitted1 = operand1.split("@");
				operand1 = splitted1[1];
				addressingmode = 1;
			} else {
				//error 09 undefined symbol in operand
			}
		}
		if (operand2 != null) { // check .format
			if ((Data.registers.containsKey(operand2.toUpperCase()) == false)) {
				if (operand2.startsWith("#")) {
					String[] splitted1 = operand2.split("#");
					operand2 = splitted1[1];
					addressingmode = 0;
				} else if (operand2.startsWith("@")) {
					String[] splitted1 = operand2.split("@");
					operand2 = splitted1[1];
					addressingmode = 1;
				}
			}
		}
	}


	private int isLiteral(String operand)
	{
		char a = '\'';

		if(operand.charAt(0)!='=' || operand.charAt(operand.length()-1) != a){
			return -1;
		}
		switch (operand.charAt(1)) {
			case 'X':

				return (int) Math.ceil((operand.length()-3)/2);
			case 'W':

				return 3;
			case 'C':

				return operand.length()-3;

			default:
				return -1;
		}

	}
	public static void extractLiterals(){
		int oldpc=pc;
		while (Assembler.literals.size()>0)
		{
			String temp=Assembler.literals.get(0);
			Data.addSymbol(temp,oldpc);
			switch (temp.charAt(1)) {
				case 'X':
					oldpc += (int) Math.ceil((temp.length() - 3) / 2);
					break;
				case 'W':
					oldpc += 3;
					break;
				case 'C':
					oldpc += temp.length() - 3;
					break;
			}
			Assembler.literals.remove(0);
		}
	}
	@Override
	public String toString() {
		return "Line [opcode=" + opcode + "]";
	}

}