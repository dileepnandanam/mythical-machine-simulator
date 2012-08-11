import java.util.*;
public class processor
{
	int pc,npc;
	int regFile[];
	int instrn;
	int opcd;
	int op1,op2;
	Boolean haltFlag;
	memmory ram;
	public processor()
	{	
		pc=0;
		regFile=new int[10];
		ram=new memmory();
		haltFlag=false;
	}
	public void run(ArrayList<Integer> mac,int progcountr)
	{
		int i;
		for(i=0;i<mac.size();i++)
		{
			ram.write(i+progcountr,mac.get(i));
		}
		
		
		pc=progcountr;
		haltFlag=false;
		while(!haltFlag)
		{	
			instrnFetch();
			instrnDecode();
			execute();
			
		}
		
	}
	public void stop()
	{
		haltFlag=true;
	}
	public void instrnFetch()
	{
		instrn=ram.location[pc];
		return;
	}
	public void instrnDecode()
	{
		opcd=instrn/10000;
		op1=(instrn/1000)%10;
		op2=instrn%1000;		
		return;
	}
	public void execute()
	{
		if(opcd==0)
		{
			haltFlag=true;
			
			System.out.println("pc="+pc+" Halting");
		}
		else if(opcd==1)
		{
			regFile[op1]=ram.read(op2);
			
			System.out.println("pc="+pc+" Load register "+op1+" with contents of address "+op2);
			pc++;
		}
		else if(opcd==2)
		{
			ram.write(op2,regFile[op1]);
			
			System.out.println("pc="+pc+" Store the contents of register "+op1+" at address "+op2);
			pc++;
		}
		else if(opcd==3)
		{
			regFile[op1]=op2;
			
			System.out.println("pc="+pc+" Load register "+op1+" with the number "+op2);
			pc++;
		}
		else if(opcd==4)
		{
			regFile[op1]=ram.read(regFile[op2]);
			
			System.out.println("pc="+pc+" Load register "+op1+" with the memory word addressed by register "+op2);
			pc++;
		}
		else if(opcd==5)
		{
			regFile[op1]+=regFile[op2];
			
			System.out.println("pc="+pc+" Add contents of register "+op2+" to register "+op1);
			pc++;
		}
		else if(opcd==6)
		{
			regFile[op1]-=regFile[op2];
			
			System.out.println("pc="+pc+" Sub contents of register "+op2+" from register "+op1);
			pc++;
		}
		else if(opcd==7)
		{
			regFile[op1]*=regFile[op2];
			
			System.out.println("pc="+pc+" Mul contents of register "+op1+" by register "+op2);
			pc++;
		}
		else if(opcd==8)
		{
			regFile[op1]/=regFile[op2];
			
			System.out.println("pc="+pc+" Div contents of register "+op1+" by register "+op2);
			pc++;
			
		}
		else if(opcd==10)
		{
			
			System.out.println("pc="+pc+" Jump to location "+op2);
			pc=op2;
		}
		else if(opcd==11)
		{
			System.out.println("pc="+pc+" Jump to location "+op2+" if register "+op1+" is zero");
			if(regFile[op1]==0)
				pc=op2;
			else
				pc++;
			
		}
		
		return;
	}
	void loadmodule(String file)
	{
		
		ram.write(100,31012);
		ram.write(101,32013);
		ram.write(102,51002);
		ram.write(103,0);
	}
	
}
class memmory
{	
	int location[];
	public memmory()
	{
		location=new int[1000];
		
	}
	public int read(int addr)
	{	System.out.println("memmory read at "+addr);
		return location[addr];
	}
	public void write(int addr,int data)
	{	System.out.println("memmory write at "+addr);
		location[addr]=data;
		

	}
}
