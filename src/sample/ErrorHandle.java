package sample;

public class ErrorHandle {

	public static String[] errors;
	
	public ErrorHandle()
	{
		init();	
	}

	private void init() {
		// TODO Auto-generated method stub
		errors=new String[19];
		errors[1]="misplaced label";
		errors[2]="missing or misplaced operation mnemonic";
		errors[3]="missing or misplaced operand field";
		errors[4]="duplicate label definition";
		errors[5]="this statement can't have a label";
		errors[6]="this statement can't have an operand";
		errors[7]="wrong operation prefix";
		errors[8]="unrecognized operation code";
		errors[9]="undefined symbol in operand";
		errors[10]="not a hexadecimal string'";
		errors[11]="can�t be format 4 instruction";
		errors[12]="illegal address for a register";
		errors[13]="missing END statement";
		errors[14]="odd length of hexadecimal value";
		errors[15]="missing start at the beginning of the code";
		errors[16]="End is not at the end of the code";
		errors[17]="operand length out of bounds";
		errors[18]="Not ending the starting label";
		
	//error 15 extra characters at end of statement
	}
	public static void getError(int n)
	{
		Assembler.output.add("\t\t***"+" ["+n+"]"+errors[n] );
	}
}
