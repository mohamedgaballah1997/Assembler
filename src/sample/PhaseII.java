package sample;

import java.util.ArrayList;

public class PhaseII {
	public static void pass2(ArrayList<Line> l) {
		
		for (int i=0;i<l.size();i++) {
			if(l.get(i).opcode.equalsIgnoreCase("start")) {
				int totalLength=0;
				for(Line j: l)
					totalLength+=j.length;
				System.out.print("H"+l.get(1).label);
				for(int j=l.get(1).label.length();j<=6;j++)
					System.out.print(" ");
						
						System.out.println(fixString(Integer.toHexString(l.get(i).address),6)+fixString(Integer.toHexString(totalLength), 6));
			}
			else if(l.get(i).opcode.equalsIgnoreCase("end")) {
				System.out.println("E"+fixString(Integer.toHexString(l.get(1).address),6));
			}
			else {
				//System.out.println(i);
				String instructions="";
				String address=Integer.toHexString(l.get(i).address);
				String recordAddress=fixString(address, 6);
				int recordLength=0;
				while(true) {
					 if(l.get(i).opcode.equalsIgnoreCase("end")) {
						 writeTextRecord(recordLength, recordAddress, instructions);
						i--;
						break;
					}
					String objectCode="";
					String binaryCode="";
					
					if(l.get(i).opcode.equalsIgnoreCase("resw") || l.get(i).opcode.equalsIgnoreCase("resb")) {
						i++;
						continue;
					}
					else if(l.get(i).opcode.equalsIgnoreCase("org")) {
						writeTextRecord(recordLength, recordAddress, instructions);
						
						break;
					}
					else if(l.get(i).opcode.equalsIgnoreCase("word")) {
						
						if(l.get(i).length+recordLength>60) {
							writeTextRecord(recordLength, recordAddress, instructions);
							i--;
							break;
						}
						recordLength+=l.get(i).length;
						int n=Integer.parseInt(l.get(i).operand1);
						objectCode=objectCode.concat(Integer.toHexString(n));
						objectCode=fixString(objectCode, 6);
						i++;
					}
					else if(l.get(i).opcode.equalsIgnoreCase("byte")) {
						
						if(l.get(i).length+recordLength>60) {
							writeTextRecord(recordLength, recordAddress, instructions);
							i--;
							break;
						}
						recordLength+=l.get(i).length;
						if(l.get(i).addressingmode==5) {
							for(int j=0;j<l.get(i).operand1.length();j++) {
							String ascii=Integer.toHexString((int) l.get(i).operand1.charAt(j));
							objectCode=objectCode.concat(ascii);
							}
						}
						else {
							objectCode=objectCode.concat(l.get(i).operand1);
						}
					i++;
					}
					else if(l.get(i).operation.type.equals(Table.instruction)){
						
						if(l.get(i).length+recordLength>60) {
							//write record
							i--;
							break;
						}
						
						recordLength+=l.get(i).length;
				int ta;

				//objectCode=objectCode.concat(fixString(String.valueOf(l.get(i).format),2));
				int opcode=Integer.parseInt(l.get(i).operation.opcode,16);
				
				
			
				switch(l.get(i).format) {
				case 1:
					objectCode=objectCode.concat(String.valueOf(l.get(i).operation.op));
					break;
				case 2:
					binaryCode=binaryCode.concat(fixString(Integer.toBinaryString(opcode),8));
					objectCode=objectCode.concat(Integer.toHexString(Integer.parseInt(binaryCode,2)));
					objectCode=fixString(objectCode, 2);
					String n1=Integer.toString(Data.registers.get(l.get(i).operand1),16);
					n1=fixString(n1, 1);
					String n2=Integer.toString(Data.registers.get(l.get(i).operand2),16);
					n2=fixString(n2, 1);
					objectCode=objectCode.concat(n1);
					objectCode=objectCode.concat(n2);
					break;
					
			case 3:
			case 4:
				binaryCode=binaryCode.concat(fixString(Integer.toBinaryString(opcode),6));
				//System.out.println("====="+l.get(i).addressingmode);
				int disp = 0;
				switch(l.get(i).addressingmode) {
				case 0:
					binaryCode=binaryCode.concat("010");
					try {
						 ta=Integer.parseInt(l.get(i).operand1);
						 disp =ta;
					 }
					 catch(Exception e){
						 ta=Data.symTab.get(l.get(i).operand1).adress;
						 disp =ta;
					 }
					break;
				case 1:
					binaryCode=binaryCode.concat("100");
					 try {
						 ta=Integer.parseInt(l.get(i).operand1);
						 disp =ta;
					 }
					 catch(Exception e){
						 ta=Data.symTab.get(l.get(i).operand1).adress;
						 int j;
						 for( j=1;j<l.size();j++) {
							 if(l.get(j).address==ta) break;
						 }
						 ta=Integer.parseInt(l.get(j).operand1);
					 }
					
					break;
				case 2:
					binaryCode=binaryCode.concat("111");
					 try {
						 ta=Integer.parseInt(l.get(i).operand1);
						 disp =ta;
					 }
					 catch(Exception e){
						 ta=Data.symTab.get(l.get(i).operand1).adress;
					 }
					break;
				default:
					binaryCode=binaryCode.concat("110");
					 try {
						
						 ta=Integer.parseInt(l.get(i).operand1);
						 disp =ta;
					 }
					 catch(Exception e){
				//		 System.out.println(".........."+l.get(i).opcode+"   "+l.get(i).operand1+"    "+Data.symTab.get(l.get(i).operand1).adress);
						 ta=Data.symTab.get(l.get(i).operand1).adress;
					 }
					break;
				}
				if(l.get(i).opcode.equalsIgnoreCase("equ")) {
					try {
						ta+=Integer.parseInt(l.get(i).operand2);
					}
					catch (Exception e) {
						ta+=Data.symTab.get(l.get(i).operand2).adress;
					}
				}
				if(disp==ta) {
					binaryCode=binaryCode.concat("00");
				}
				else {
					
				 disp=ta-l.get(i+1).address;
				if(disp>=-2048 && disp<=2047)
					binaryCode=binaryCode.concat("01");
				else
					binaryCode=	binaryCode.concat("10");
				}
				String  displacement=Integer.toBinaryString(disp);
				if(l.get(i).format==4) {
					binaryCode=binaryCode.concat("1");
					displacement=fixString(displacement, 24);
				}
				else {
					binaryCode=binaryCode.concat("0");
					displacement=fixString(displacement, 12);
				}
				
				//
				binaryCode=binaryCode.concat(displacement);
				//System.out.println("------"+binaryCode);
				binaryCode=Integer.toHexString(Integer.parseInt(binaryCode,2));
				objectCode=objectCode.concat(binaryCode);
				
				break;
				
			}
				i++;
				
				}
					
					instructions=instructions.concat(objectCode);
				
				}
				
				//String displacment=In
				//System.out.println("T"+Integer.toHexString(l.get(i).address)+Integer.toHexString(l.get(i+1).address-l.get(i).address));
			}
		}
		
	}
	public static void writeTextRecord(int length,String startAddress,String instructions) {
		String lengthSTR=Integer.toHexString(length);
		lengthSTR=fixString(lengthSTR, 2);
		System.out.println("T"+startAddress+lengthSTR+instructions);
	}
	public static String fixString(String str,int n) {
		String string="";
		if(n>str.length()) {
			while(n>str.length()) {
				string=string.concat("0");
				n--;
			}
		string=string.concat(str);
		}
		else {
			string=str.substring(str.length()-n);
		}
		return string;
	}
}
