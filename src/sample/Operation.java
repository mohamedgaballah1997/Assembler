package sample;

public class Operation {

	public Table type;
	public int id;	//id inside the original table
	public String op;		//operation
	public int format; 		//size of instruction
	public String opcode;
	public Operation (String s ,String opcode , int f , Table t)
	{
		op=s;
		format=f;
		type=t;
		this.opcode=opcode;
	}
	
}


