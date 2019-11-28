package sample;
import java.util.HashMap;

public class Data {
	
	public static double f;
//	public static HashMap < String , Integer > map = new HashMap <String, Integer>();
//	public static HashMap < String , Integer > registers = new HashMap <String, Integer>();
//	public static HashMap < String , Integer > symTab = new HashMap <String, Integer>();
	public static HashMap < String , Operation > map = new HashMap <String, Operation>();
	public static HashMap < String , Integer > registers = new HashMap <String, Integer>();
	public static HashMap < String , Symbol > symTab = new HashMap <String, Symbol>();
	public static int counter;
	//public static HashMap < String , Operation >  = new HashMap <String, Operation>();
	public Data()
	{
		this.f=0;
		init();
		counter=0;
		
	}
	
	//map.contain to check weather the element is found or not 
	// return true if found
	private void init ()
	{
		//INITIAT REGISTERS
		registers.put("A",0);
		registers.put("B",3);
		registers.put("X",1);
		registers.put("T",5);
		registers.put("L",2);
		registers.put("S",4);
		registers.put("F",6);
		//INITIAT INSTRUCTIONS
		Operation STA = new Operation("STA","OC",3,Table.instruction);
		map.put("STA", STA);
		Operation STL = new Operation("STL","14",3,Table.instruction);
		map.put("STL", STL);
		Operation STT = new Operation("STT","40",3,Table.instruction);
		map.put("STT", STT);
		Operation STS = new Operation("STS","39",3,Table.instruction);
		map.put("STS", STS);
		Operation STX = new Operation("STX","10",3,Table.instruction);
		map.put("STX", STX);
		Operation STB = new Operation("STB","13",3,Table.instruction);
		map.put("STB", STB);
		Operation LDA = new Operation("LDA","00",3,Table.instruction);
		map.put("LDA", LDA);
		Operation LDL = new Operation("LDL","08",3,Table.instruction);
		map.put("LDL", LDL);
		Operation LDT = new Operation("LDT","24",3,Table.instruction);
		map.put("LDT", LDT);
		Operation LDS = new Operation("LDS","31",3,Table.instruction);
		map.put("LDS", LDS);
		Operation LDX = new Operation("LDX","04",3,Table.instruction);
		map.put("LDX", LDX);
		Operation LDB = new Operation("LDB","11",3,Table.instruction);
		map.put("LDB", LDB);
		Operation RMO = new Operation("RMO","AC",2,Table.instruction);
		map.put("RMO",RMO);
		Operation LDCH = new Operation("LDCH","20",3,Table.instruction);
		map.put("LDCH", LDCH);
		Operation STCH = new Operation("STCH","37",3,Table.instruction);
		map.put("STCH", STCH);
		Operation ADD = new Operation("ADD","18",3,Table.instruction);
		map.put("ADD",ADD);
		Operation SUB = new Operation("SUB","1C",3,Table.instruction);
		map.put("SUB",SUB);
		Operation ADDR = new Operation("ADDR","90",2,Table.instruction);
		map.put("ADDR",ADDR);
		Operation SUBR = new Operation("SUBR","94",2,Table.instruction);
		map.put("SUBR",SUBR);
		Operation COMP = new Operation("COMP","28",3,Table.instruction);
		map.put("COMP",COMP);
		Operation COMPR = new Operation("COMPR","A0",2,Table.instruction);
		map.put("COMPR",COMPR);
		Operation J = new Operation("J","3C",3,Table.instruction);
		map.put("J",J);

		Operation JEQ = new Operation("JEQ","30",3,Table.instruction);
		map.put("JEQ",JEQ);		

		Operation JGT = new Operation("JGT","34",3,Table.instruction);
		map.put("JGT",JGT);		

		Operation JLT = new Operation("JLT","38",3,Table.instruction);
		map.put("JLT",JLT);


		Operation TIX = new Operation("TIX","2C",3,Table.instruction);
		map.put("TIX",TIX);
		Operation TIXR = new Operation("TIXR","B8",2,Table.instruction);
		map.put("TIXR",TIXR);

		Operation START = new Operation("START","",3,Table.directive);
		map.put("START",START);


		map.put("END",new Operation("End","",3,Table.directive));
		map.put("LT",new Operation("LT","",1,Table.directive));
		map.put("EQU",new Operation("EQU","",3,Table.directive));
		map.put("ORG",new Operation("ORG","",3,Table.directive));
		map.put("BYTE",new Operation("BYTE","",3,Table.storage));
		map.put("WORD",new Operation("WORD","",3,Table.storage));	
		map.put("RESB",new Operation("RESW","",3,Table.storage));
		map.put("RESW",new Operation("RESB","",3,Table.storage));	
	}
	
	public static boolean addSymbol(String label, int address)
	{
		
		if(label.equals("")) return true;
		if(symTab.get(label)!=null && symTab.get(label).adress!=-1 && address!=-1) {
			ErrorHandle.getError(4);
			return false;
		}
		//System.out.println("+++++"+label + address);
		if( ( symTab.get(label)==null )|| (symTab.get(label).adress==-1))
			symTab.put(label,new Symbol(label,address, ++counter,Table.symbol )); // the address from the PC
		return true;
	}
}
