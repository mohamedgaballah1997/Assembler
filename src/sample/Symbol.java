package sample;

public class Symbol {
	public Table type;
	public int adress,id;	//id inside the original table
	public String op;			//size of instruction
	public Symbol(String op, int adress, int id,Table type) {
		super();
		this.type = type;
		this.adress = adress;
		this.id = id;
		this.op = op;
	}
	
}

