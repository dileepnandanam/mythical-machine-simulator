import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;


public class simulator 
{
	
	public static void main(String args[])
	{
		
		gui simulatorgui=new gui();
		
	}
	
	
		
}
class gui extends JFrame implements ActionListener
{	JFrame frame;
	Container workspace;
	JTextArea assArea,opArea,memArea;
	JTextField asslab,reglab,memlab;
	Button runButton;
	assumbler asm;
	processor p4;
	public gui()
	{	
		asm=new assumbler(100);
		p4=new processor();
		frame=new JFrame("Mythical machine simulator");
		workspace=getContentPane();
		
		assArea=new JTextArea("",20,50);
		assArea.setEditable(true);
		
		assArea.setText("lda r1,123 #load number to register r1\nlda r2,234\nadd r1,r2 #add contents of r2 to r1\nstm r1,500 #store result to location 500");
		
		
		
		
		
		
		
		
		opArea=new JTextArea("",3,50);
		opArea.setEditable(true);
		memArea=new JTextArea("",11,50);
		memArea.setEditable(true);
		
		asslab=new JTextField("Assumbly code here:",0);
		reglab=new JTextField("Register contents:",0);
		memlab=new JTextField("memmory contents:",0);
		asslab.setEditable(false);
		reglab.setEditable(false);
		memlab.setEditable(false);
		runButton=new Button("run");
		runButton.addActionListener(this);
		
		
		JScrollPane assScrollpan = new JScrollPane(assArea,
		JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
		JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		JScrollPane opScrollpan = new JScrollPane(opArea,
		JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
		JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		JScrollPane memScrollpan = new JScrollPane(memArea,
		JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
		JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		
		
	
		GridBagLayout gridBag = new GridBagLayout();
		workspace.setLayout(gridBag);
		GridBagConstraints gridCons1 = new GridBagConstraints();
		gridCons1.gridwidth = GridBagConstraints.REMAINDER;
		gridCons1.fill = GridBagConstraints.VERTICAL;

		workspace.add(asslab,gridCons1);
		workspace.add(assScrollpan,gridCons1);
		gridCons1.fill = GridBagConstraints.VERTICAL;
		workspace.add(runButton,gridCons1);
		gridCons1.fill = GridBagConstraints.VERTICAL;
		workspace.add(reglab,gridCons1);
		workspace.add(opScrollpan,gridCons1);
		
		workspace.add(memlab,gridCons1);
		workspace.add(memScrollpan,gridCons1);
		
		
		
		
		
		
		frame.add(workspace);
		frame.pack();
		frame.addWindowListener(new WindowAdapter() {public void windowClosing(WindowEvent e){System.exit(0);}});
		frame.setVisible(true);
	
	}
	public void actionPerformed(ActionEvent evt)
	{	int i;
		String program,reportr,reportm;
		
		program=assArea.getText();
		ArrayList<Integer> mac=new ArrayList<Integer>();
		mac=asm.assumble(program);
		p4.run(mac,100);
		reportr="";
		reportm="";
		for(i=0;i<=9;i++)
		{
			reportr+="r"+i+":"+p4.regFile[i]+"  ";
		}
		for(i=0;i<1000;i++)
		{	
			if(p4.ram.location[i]>0)
				reportm+=+i+": "+p4.ram.location[i]+"\n";
		}
		
		
		opArea.setText(reportr);
		memArea.setText(reportm);
		
	}
	
}
class assumbler
{	
	String program[];
	
	
	int fpc;
	int nol;
	public assumbler(int f)
	{	
		program=new String[100];
		
		nol=0;
		
		fpc=f;
	}
	public ArrayList<Integer> assumble(String prog)
	{	
		String line;
		lookupTable lt=new lookupTable();
		nol=0;
		int l,i=0;
		char c;
		int f;
		ArrayList<String> label=new ArrayList<String>();
		ArrayList<String> program=new ArrayList<String>();
		ArrayList<String> stmt=new ArrayList<String>();
		ArrayList<Integer> mprogram=new ArrayList<Integer>();
		program=split(prog,"\n");
		nol=program.size();	
		
		
		
		
		for(l=0;l<program.size();l++)
		{
			label=split(program.get(l)," ,");
			if(label.get(0)!=null)
			{
				if(label.size()==1)
				{	
					program.remove(l);
					
					program.set(l,(label.get(0)+" "+program.get(l)));
					
					label=split(program.get(l)," ,");
					
				}
				if(lt.search(label.get(0))==-1)
				{
					lt.addentry(label.get(0),l+fpc);
					line="";
					for(f=1;f<label.size();f++)
						line+=label.get(f)+" ";
					program.remove(l);
					program.add(l,line);
				}
				
			}
		}
		
		
		int opc,op1,op2;
		for(l=0;l<program.size();l++)
		{	
			
			
			stmt=split(program.get(l)," ,");
			f=stmt.size();
			op1=0;
			op2=0;
			opc=0;
			
			if(f==1)
			{
				opc=lt.search(stmt.get(0));
			}
			if(f==2)
			{	opc=lt.search(stmt.get(0));
				op2=lt.search(stmt.get(1));
			}
			if(f==3)
			{	opc=lt.search(stmt.get(0));
				op1=lt.search(stmt.get(1));
				op2=lt.search(stmt.get(2));
				if(op2==-1)
					op2=Integer.parseInt(stmt.get(2));
					
			}	
			
			mprogram.add(opc*10000+op1*1000+op2);
			
			
			
		}
		return (mprogram);
		
		
	}
	Boolean element(char c,String seps)
	{
		int i=0;
		while(i<seps.length())
		{
			if(c==seps.charAt(i))
				return true;
				
			i++;
		}
		return false;
	}
	ArrayList<String> split(String s,String b)
	{	
		ArrayList<String> list=new ArrayList<String>();
		String l;
		int i=0;
		while(i<s.length()&&s.charAt(i)!='#')
		{	
			l="";
			while(i<s.length() && !element(s.charAt(i),b))
			{
				l+=s.charAt(i);
				i++;
			}
			if(!l.equals(""))
				list.add(l);
			i++;
		}
		
		return list;
	}
	
	
	
}


class lookupTable
{
	String name[];
	int opc[];
	int noe;
	public lookupTable()
	{
		name=new String[30];
		opc=new int[30];
		
		
		name[0]="hlt";opc[0]=0;
		name[1]="ldm";opc[1]=1;
		name[2]="stm";opc[2]=2;
		name[3]="lda";opc[3]=3;
		name[4]="ldi";opc[4]=4;
		name[5]="add";opc[5]=5;
		name[6]="sub";opc[6]=6;
		name[7]="mul";opc[7]=7;
		name[8]="div";opc[8]=8;
		name[9]="jmp";opc[9]=10;
		name[10]="jmr";opc[10]=11;
		name[11]="r1";opc[11]=1;
		name[12]="r2";opc[12]=2;
		name[13]="r3";opc[13]=3;
		name[14]="r4";opc[14]=4;
		name[15]="r5";opc[15]=5;
		name[16]="r6";opc[16]=6;
		name[17]="r7";opc[17]=7;
		name[18]="r8";opc[18]=8;
		name[19]="r9";opc[19]=9;
		name[20]="r0";opc[20]=0;
		
		
		
		
		
		
		
		noe=20;
	}
	public int search(String key)
	{	
		if(key.length()==0)
			return -1;
		int i;
		for(i=0;i<noe;i++)
		{
			if(name[i].equals(key))
				return opc[i];
		}
		return -1;
	}
	public void addentry(String key,int val)
	{
		
		name[noe]=key;
		opc[noe]=val;
		noe++;
	}
}

