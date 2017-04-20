package var_vep.kristopher;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;



public class LongMathInt implements Serializable
{
	static int defaultLength=32;// ���������� ������ ���������� � ���������� ���������� �� 4� (LongMathInt(long)) �� 16001 (������������� ��)
	static LongMathInt zero=new LongMathInt();
	//static LongMathInt ten=new LongMathInt(10l);
	static LongMathInt minusOne = new LongMathInt(-1l);
	static LongMathInt one = new LongMathInt(1l);
	static LongMathInt pi = LongMathInt.constant("pi");
	static LongMathInt min= LongMathInt.cr();
	int point;//��������� �� ������ ����� ����� �����
	boolean negat;//�������� �� ����� �������������
	int [] numbers=new int[LongMathInt.defaultLength];//����� � 65 536 ����� ������� ���������
	int factLength;//��������� �� ��������� �������� ������ +1 
	private static int correctOfSin=LongMathInt.correctOf();// 1/(correctOfSin!)=0 ����������� ������!
	public LongMathInt(int[] num,boolean negat,int point)//������� ������ � ������
	{	
		if(point<0||point>num.length)
		{
			System.out.println("������ ����������� LongMathInt(int[],boolean,int): ������������ �������� point");
			return;
		}
		int n=0;
		int m=num.length;
		int r=0;
		while(n<point&&num[n]==0)
		{	
			n++;//������� ������� �������� �������
		}
		while(m>point&&num[m-1]==0)
			m--;//������� ������� �������� ������
		 
		if (m-n>LongMathInt.defaultLength)//�������� �� ����������
		{
			//System.out.println("�������������� ������������ LongMathInt(int[]): ������ �������� ������� ����� �� ������� ���������!");
			n=0;
			r=m-n-defaultLength;
			while(n+r<num.length&&num[n+r]==0)//����� ���� ��� ��������� r ����� ��������� ���� � ������, �� ����� ������
			{	
				n++;//������� ������� �������� �������
			}
		}
		
		this.negat=negat;//���������� ���� �����
		this.point=point-n-r;
		while(this.point<0)
		{
			n--;
			this.point++;
		}
		int i=0;
		while(i<LongMathInt.defaultLength&n+r<m)//�������������� �������� �� m
		{
			if((num[n+r]&0x0000ffff)!=num[n+r])//�������� �� ������� ������������ ����� 16
			{
				System.out.println("������: ����� ������, ���������� �������� ������������ ������ 16");//����� ���� ������ 65535, ���� ������ 0
				return;
			}
			this.numbers[i]=num[n+r];
			i++;
			n++;
		}
		if(i>this.point)
			this.factLength=i;//������������� ���� factLength
		else
			this.factLength=this.point;
	}
	public LongMathInt(long number)//������� ������ �� ����� ����������� � ���������
	{
		long arg=number;//�������� ���������� �����, ����� ����� ���� ��� ��������
		this.point=0;//������������� ����� �� ������� ���������, ��� ��� ����� �����
		this.negat=(number<0);//��������� ���� �����
		arg=abs(arg);//������ ����� �������������
		int i=0;
		while(arg!=0)//����� ���� �� 4 ����� �� 16 ���
		{
			this.numbers[i]=(int) (arg&0x000000000000ffff);//arg%65536
			arg>>>=16;//�������� ������ �� 16 ���
			i++;
		}
		this.factLength=i;//������������� ���� factLength
	}
	public LongMathInt() //������� ������ � ������ 0
	{
		this.point=0;
		this.factLength=0;
		this.negat=false;
	}	
//��������������� �������
	static LongMathInt constant(String s)//��������!!!
	{
		if(s.equalsIgnoreCase("pi"))
		{
			LongMathInt res =new LongMathInt();
			FileInputStream fromFile;
			try 
			{
				fromFile = new FileInputStream(new File("F:\\������ mail\\���������\\workspace\\Shifrovanie\\src\\tests2\\pi_int.txt"));
			}
			catch (FileNotFoundException e) 
			{
				System.out.println("LongMathInt constant(String s) ������: ���������� ������ �� �����");
				e.printStackTrace();
				return res;
			}
			res.point=defaultLength-1;
			res.factLength=defaultLength;
			for(int i =defaultLength-1;i>-1;i--)
			{
				try 
				{
					res.numbers[i]=fromFile.read();
					res.numbers[i]=res.numbers[i]<<8;
					res.numbers[i]=res.numbers[i]+fromFile.read();
				} 
				catch (IOException e) 
				{
					System.out.println("LongMathInt constant(String s) ������ ������ ������� "+i);
					e.printStackTrace();
				}
			}
			try {
				fromFile.close();
			} catch (IOException e) {
				System.out.println("LongMathInt constant(String s) ������ ��� �������� �����");
				e.printStackTrace();
			}
			return res;
		}
		else
		{
			System.out.println("������ ������������ ��� ���������");
			return new LongMathInt();
		}
	}
	static int correctOf() 
	{
		int t=0;
		try 
		{
			int k=48;
			BufferedReader fromFile;
			fromFile = new BufferedReader(new FileReader("F:\\������ mail\\���������\\workspace\\Shifrovanie\\src\\tests2\\memory.txt"));
			do
			{
				k=k-48;
				t=t*10+k;
				k=fromFile.read();
			}while(k!='\n');
			if(t==defaultLength)
			{
				t=0;
				k=48;
				do
				{
					k=k-48;
					t=t*10+k;
					k=fromFile.read();
				}while(k!='\n');
				fromFile.close();
			}
			else
			{
				fromFile.close();
				LongMathInt b;
				LongMathInt c = new LongMathInt(1l);
				System.out.println("��������� ����� ��������� ��� ������������������ �������...");
				for(t=1;!c.equals(LongMathInt.zero);t++)
				{
					b= new LongMathInt((long)t);
					c=c.div(b).mult(pi);
					if(t<0)
						System.out.println("������� defaultlength ������");
				}
				if((t&0x1)==1)
					t++;
				System.out.println("������! "+t);
				FileWriter writeFile= new FileWriter( new File("F:\\������ mail\\���������\\workspace\\Shifrovanie\\src\\tests2\\memory.txt"));
				writeFile.append(defaultLength+"\n");
				writeFile.append(t+"\n");
				writeFile.close();
			}
		} 
		catch (FileNotFoundException e){e.printStackTrace();}
		catch (IOException e) {e.printStackTrace();}
		return t;
		
	}
	private static LongMathInt cr()
	{
		LongMathInt r =new LongMathInt();
		r.factLength=r.point=defaultLength;
		r.numbers[0]=1;
		return r;
	}
	static int abs(int arg)//���������� ������ �����
	{
		if(arg>-1)
			return arg;
		else
			return -arg;
	}
	static long abs(long arg)//���������� ������ �����
	{
		if(arg>-1)
			return arg;
		else
			return -arg;
	}
	LongMathInt mod (LongMathInt mod)// ���������� ����� �� ���������� (-mod;mod)
	{
		
		LongMathInt t=this.div(mod);
		int i=0;
		for(;i+t.point<t.factLength;i++)//��������� ������ ����� �����
		{
			t.numbers[i]=t.numbers[i+t.point];
		}
		while(i<t.factLength)
		{
			t.numbers[i]=0;
			i++;
		}
		t.factLength=t.factLength-t.point;
		t.point=0;
//		if(this.negat)
//			t.add(one.minus());
		return this.add(t.mult(mod).minus());	
	}
//����������� ������
	protected LongMathInt clone()//������� ����� ����� �������
	{		
		LongMathInt result = new LongMathInt();
		int min=this.factLength;
		if(this.factLength>defaultLength)
			min=defaultLength;
		result.factLength = min;
		int d=this.factLength-defaultLength;
		if(d<0)
			d=0;
		result.negat=this.negat;//���������� ���� �����
		result.point=this.point-d;//���������� �����
		
		for(int i=min-1;i>-1;i--)
			result.numbers[i]=this.numbers[i+d];
		return result;
	}
	public static LongMathInt fromString(String number)//�������� ������ ��� ����� ����� ��������������� �����
	{
		LongMathInt res = new LongMathInt();
		String[] t=number.split(" ");
		if(t.length>defaultLength)
		{
			System.out.println("LongMathInt fromString(String number): �������� ������ ���������� ������� ����� ���������");
			return res;
		}
		int i=t.length-1;
		res.factLength=t.length;
		for(String s:t)
		{
			res.numbers[i]=Integer.parseInt(s);
		}
		return res;
	}
	public String toString()//����������� ������ � ������, ������� ��� ����������
	{
		String result=new String();
		if (this.factLength==0&&this.point==0)
			return "0.";
		if (this.negat)
			result="-";
		if(point==factLength)
			result=result+".";
		for(int i=this.factLength-1;i>=0;i--)
		{
			result=result+this.numbers[i];
			if(this.point==i)
			{
				result=result+".";
				continue;
			}
			result=result+" ";
		}
		return result;
	}
	boolean equals(LongMathInt arg)//1 ���� ����� 0 �����
	{
		if(this.negat!=arg.negat)
			return false;
		if (this.factLength==arg.factLength)
		{
			for(int i=0;i<this.factLength;i++)
				if(this.numbers[i]!=arg.numbers[i])
					return false;
			return true;
		}
		return false;
	}
//������ ��� ������ ��� ���������
	LongMathInt minus()//���������� ������ �� ������ -
	{
		LongMathInt res= this.clone();
		if(this.negat)
			res.negat=false;
		else
			res.negat=true;
		return res;
	}
	void vminus()//������ ���� � �������
	{
		if(this.negat)
			this.negat=false;
		else
			this.negat=true;
	}
	LongMathInt abs()//���������� ����� ��� ����� �����
	{
		LongMathInt res=this.clone();
		res.negat=false;
		return res;
	}
	void vabs()//������ ����� �������������
	{
		this.negat=false;
	}
	boolean bigger(LongMathInt arg)//���������� 1 ���� ������� ������ ���������, 0 ���� ������ ���� �����, ����� �������� ��� ������ ��������� 
	{
		if(this.negat&!arg.negat)//������ �������, ������ �������
			return false;
		else if((!this.negat)&arg.negat)//������ �������, ������ �������
			return true;
		else if(!(this.negat&arg.negat))//��� ������������
		{
			//���������� �� ����� ����� �����
			if(this.factLength-this.point>arg.factLength-arg.point)//���� ����� ����� ����� ������ � �������
				return true;
			else if(this.factLength-this.point<arg.factLength-arg.point)//���� ����� ����� ����� ������ � �������
				return false;
			else//���� ����� ����� ����� �����
			{
				for(int i=this.factLength-1,j=arg.factLength-1;(i>-1)&(j>-1);i--,j--)
				{
					if(this.numbers[i]>arg.numbers[j])
						return true;
					else if((this.numbers[i]<arg.numbers[j]))
						return false;
				}//���� ����� �� ����� ������ �� ��������
				if(this.factLength>arg.factLength)
					return true;
				else
					return false;
			}
		}
		else//��� ������������
		{
			//���������� �� ����� ����� �����
			if(this.factLength-this.point>arg.factLength-arg.point)//���� ����� ����� ����� ������ � �������
				return false;
			else if(this.factLength-this.point<arg.factLength-arg.point)//���� ����� ����� ����� ������ � �������
				return true;
			else//���� ����� ����� ����� �����
			{
				for(int i=this.factLength-1,j=arg.factLength-1;(i>-1)&(j>-1);i--,j--)
				{
					if(this.numbers[i]>arg.numbers[j])
						return false;
					else if((this.numbers[i]<arg.numbers[j]))
						return true;
				}//���� ����� �� ����� ������ �� ��������
				if(this.factLength>=arg.factLength)
					return false;
				else
					return true;
			}
		}
	}
//�������������� ��������
	LongMathInt add(LongMathInt arg)
	{
		if(!(this.negat^arg.negat))//���� ���������� �����
		{
			if(!arg.bigger(this))//this ������ ���� ����� arg 
			{
				if(arg.factLength==0)
					return this.clone();
				LongMathInt res = new LongMathInt();
				int r=this.point-arg.point;//������� ����� ������� ������ 
				int l = this.factLength-this.point-arg.factLength+arg.point;//������� ���� ����� ������ ������ >= 0
				int i=0, t=0;
				int p=0;//����� ����� ������������ this
				
				if(r>=0)
				{
					//�������� �� ����� ������� ���������� ���������� ������? ������ ������� ��� ��� �� ����������� ���������� �������
					//���� �� ��������
					if(!((l==0)&&((this.numbers[this.factLength-1]+arg.numbers[arg.factLength-1]>>>16!=0))&&(this.factLength==defaultLength)))
					{
						for(;i<r;i++)
						{
							res.numbers[i]=this.numbers[i];
						}
						for(;i<this.factLength-l;i++)
						{
							res.numbers[i]=this.numbers[i]+arg.numbers[i-r]+t;
							t=res.numbers[i]>>>16;
							res.numbers[i]=res.numbers[i]&0x0000ffff;
						}
						while(t!=0)
						{
							if(i<defaultLength)
							{
								res.numbers[i]=this.numbers[i]+t;
								t=res.numbers[i]>>>16;
								res.numbers[i]=res.numbers[i]&0x0000ffff;
								i++;
							}
							else
							{
								for(i=0;i<defaultLength-1;i++)//�������� ���� � �������� ��������� 1
									res.numbers[i]=res.numbers[i+1];
								res.numbers[defaultLength-1]=t;
								i++;
								t=0;
								p++;
							}
						}
						for(;i<this.factLength;i++)
						{
							res.numbers[i]=this.numbers[i];
						}
					}
					else//��������� �������� ��-������ ������ �� �������; l=0, this.factLength=defaultLength
					{
						 p++;
						 if(r>0)
							 r--;
						 
						 for(;i<r;i++)
							{
								res.numbers[i]=this.numbers[i+p];
							}
							for(;i<this.factLength-1;i++)
							{
								res.numbers[i]=this.numbers[i+p]+arg.numbers[i+r+p]+t;
								t=res.numbers[i]>>>16;
								res.numbers[i]=res.numbers[i]&0x0000ffff;
							}
							res.numbers[defaultLength-1]=t;
							i++;
							t=0;
					}
					res.factLength=i;
					res.point=this.point-p;
					res.negat=this.negat;
					return res;
					
				}
				else//r<0
				{
					r=-r;
					//���� ��������� ����������
					if(!((this.factLength+r>defaultLength)||((this.factLength+r==defaultLength)&(l==0)&(this.numbers[this.factLength-1]+arg.numbers[arg.factLength-1]>>>16!=0))))
					{
						for(;i<r;i++)
							res.numbers[i]=arg.numbers[i];
						for(;i<arg.factLength;i++)
						{
							res.numbers[i]=this.numbers[i-r]+arg.numbers[i]+t;
							t=res.numbers[i]>>>16;
							res.numbers[i]=res.numbers[i]&0x0000ffff;
						}
						while(t!=0)
						{
							if(i<defaultLength)
							{
								res.numbers[i]=this.numbers[i-r]+t;
								t=res.numbers[i]>>>16;
								res.numbers[i]=res.numbers[i]&0x0000ffff;
								i++;
							}
							else
							{
								for(;i<defaultLength-1;i++)//�������� ���� � �������� ��������� 1
									res.numbers[i]=res.numbers[i+1];
								res.numbers[defaultLength-1]=t;
								i++;
								t=0;
								p++;
							}
						}
						for(;i<this.factLength+r;i++)
							res.numbers[i]=this.numbers[i-r];
					}
					else//��������� �������������� ������� �������
					{
						p=this.factLength+r-defaultLength;//�� ������������
						
						//���� ������ ���������� � ���������� ��������
						if((l==0)&(this.numbers[this.factLength-1]+arg.numbers[arg.factLength-1]>>>16!=0))
							p++;
						r=r-p;//�� ������������ ��� ��� r-p=defaultLength-this.length
						for(;i<r;i++)
							res.numbers[i]=arg.numbers[i+p];
						for(;i+p<arg.factLength;i++)
						{
							res.numbers[i]=this.numbers[i-r]+arg.numbers[i+p]+t;
							t=res.numbers[i]>>>16;
							res.numbers[i]=res.numbers[i]&0x0000ffff;
						}
						while(t!=0)
						{
							if(i<defaultLength)
							{
								res.numbers[i]=this.numbers[i-r]+t;
								t=res.numbers[i]>>>16;
								res.numbers[i]=res.numbers[i]&0x0000ffff;
								i++;
							}
							else
							{
								for(;i<defaultLength-1;i++)//�������� ���� � �������� ��������� 1
									res.numbers[i]=res.numbers[i+1];
								res.numbers[defaultLength-1]=t;
								i++;
								t=0;
								p++;
							}
						}
						for(;i<defaultLength;i++)
							res.numbers[i]=this.numbers[i-r];
					}
					res.factLength=i;
					res.point=arg.point-p;
					res.negat=this.negat;
					return res;
				}
				
			}
			else
				return arg.add(this);
		}
		else//����� ������
		{
			if(!arg.abs().bigger(this.abs()))//this ������ �� ��������
			{
				LongMathInt res = new LongMathInt();
				int r=this.point-arg.point;//������� ����� ������� ������ 
				//int l = this.factLength-this.point-arg.factLength+arg.point;//������� ���� ����� ������ ������ >= 0
				int i=0, t=0;
				int p=0;//����� ����� ������������ this
				if(r>=0)
				{
					for(;i<r;i++)
						res.numbers[i]=this.numbers[i];
					for(;i-r<arg.factLength;i++)
					{
						res.numbers[i]=this.numbers[i]-arg.numbers[i-r]+t;
						if(res.numbers[i]<0)
						{
							res.numbers[i]=res.numbers[i]+0x00010000;
							t=-1;
						}
						else
							t=0;
					}
					while(t!=0)
					{
						res.numbers[i]=this.numbers[i]+t;
						if(res.numbers[i]<0)
						{
							res.numbers[i]=res.numbers[i]+0x00010000;
							t=-1;
						}
						else
							t=0;
						i++;
					}
					for(;i<this.factLength;i++)
						res.numbers[i]=this.numbers[i];
					while(i>0&&res.numbers[i-1]==0)//���������� ����������� ������
						i--;
						
				}
				else//r<0
				{
					r=-r;
					if(this.factLength+r<=defaultLength)
					{
						for(;i<r;i++)
						{
							res.numbers[i]=0x00010000-arg.numbers[i]+t;
							t=-1;
						}
						for(;i<arg.factLength;i++)
						{
							res.numbers[i]=this.numbers[i-r]-arg.numbers[i]+t;
							if(res.numbers[i]<0)
							{
								res.numbers[i]=res.numbers[i]+0x00010000;
								t=-1;
							}
							else
								t=0;
						}
						while(t!=0)
						{
							res.numbers[i]=this.numbers[i-r]+t;
							if(res.numbers[i]<0)
							{
								res.numbers[i]=res.numbers[i]+0x00010000;
								t=-1;
							}
							else
								t=0;
							i++;
						}
						for(;i-r<this.factLength;i++)
							res.numbers[i]=this.numbers[i-r];
						while(i>0&&res.numbers[i-1]==0)
							i--;//������� ������� �������� ������
						p=r;
					}
					else//����� ������ ���������� ������ ��� ����
					{
						p=this.factLength+r-defaultLength;//�� ������������
						r=r-p;//�� ������������ ��� ��� r-p=defaultLength-this.length
						for(;i<r;i++)
						{
							res.numbers[i]=0x00010000-arg.numbers[i+p]+t;
							t=-1;
						}
						for(;i<arg.factLength-p;i++)
						{
							res.numbers[i]=this.numbers[i-r]-arg.numbers[i+p]+t;
							if(res.numbers[i]<0)
							{
								res.numbers[i]=res.numbers[i]+0x00010000;
								t=-1;
							}
							else
								t=0;
						}
						while(t!=0)
						{
							res.numbers[i]=this.numbers[i-r]+t;
							if(res.numbers[i]<0)
							{
								res.numbers[i]=res.numbers[i]+0x00010000;
								t=-1;
							}
							else
								t=0;
							i++;
						}
						for(;i-r<this.factLength;i++)
							res.numbers[i]=this.numbers[i-r];
						p=r;
						while(i!=0&&res.numbers[i-1]==0)
						{
							i--;//������� ������� �������� ������
						}
											
					}
						
				}
				
				res.point=this.point+p;
				if(i>res.point)
					res.factLength=i;
				else
					res.factLength=res.point;
				
				res.negat=this.negat;
				if(res.numbers[0]==0)//������� ���� �������
				{
					if(0!=res.factLength)
					{
						i=1;
						while(i!=res.factLength&&res.numbers[i]==0)
						{
							i++;
						}
					}
					for(p=0;p+i<res.factLength;p++)
						res.numbers[p]=res.numbers[p+i];
					for(;p<res.factLength;p++)
						res.numbers[p]=0;
					res.factLength=res.factLength-i;
					res.point=res.point-i;
					if(res.point<0)
						res.point=0;
				}
				return res;
			}
			else
				return arg.add(this);
		}
	}
	LongMathInt mult(LongMathInt arg)
	{	
		int res[] = new int[this.factLength+arg.factLength];
		int t=0,i,j;
		for(i=0;i<arg.factLength;i++)
		{
			for(j=0;j<this.factLength;j++)
			{
				res[i+j]=res[i+j]+arg.numbers[i]*this.numbers[j]+t;
				t=res[i+j]>>>16;
				res[i+j]=res[i+j]&0x0000ffff;
			}
			res[j+i]=t;
			t=0;
		}		
		return new LongMathInt(res,this.negat^arg.negat,this.point+arg.point);
	}
	LongMathInt div(LongMathInt arg)// ����� �������
	{
		if(arg.equals(zero))
		{
			System.out.println("������ ����� div(): ������� �� ����!!!");
			return new LongMathInt();
		}
		if(this.equals(arg))
		{
			return one.clone();
		}
		LongMathInt p=new LongMathInt();// ��� ����������� �����������
		LongMathInt t =new LongMathInt(new int[] {32768,1},false,1);//	1/arg
		LongMathInt two = new LongMathInt(2l);
		LongMathInt arg1 = arg.clone().abs();
		
		//����������� ������� �� 2^N
		int shift=arg1.factLength-arg1.point;//�������� ������ � �����, ���� � ����� ���� ����� ����� ����� >0 ����� =0
		if(shift==0)
		{
			while(arg1.numbers[arg1.factLength+shift-1]==0)//���������� ������������� �������� ������ ��� �������������
				shift--;
			if(shift<0)
			{
				arg1.point=arg1.factLength=arg1.factLength+shift;
			}
		}
		else//shift>0
		{
			arg1.point=arg1.factLength;
		}//������ ����� �� ���������� [0;1]
		int bit=0;
		while(((arg1.numbers[arg1.factLength-1]<<bit)&0x00008000)!=0x8000)//���� 15� ��� �� ����� ����� 1, ��� ������������� ����, ��� ����� >=0.5
			bit++;
		if(bit>0)
		{
			int temp=0;
			
			int i=0;
			for(;i<arg1.factLength;i++)
			{
				arg1.numbers[i]=(arg1.numbers[i]<<bit)+temp;
				temp=arg1.numbers[i]>>>16;
				arg1.numbers[i]=arg1.numbers[i]&0x0000ffff;
			}
			if(temp!=0)//�������, ���� ��� ������=)
			{
				System.out.println("������ ���� �����������!!!");
			}
		}
		
		
		LongMathInt.defaultLength+=2;
		LongMathInt m;
		t=t.clone();
		p=p.clone();
		two=two.clone();
		arg1=arg1.clone();
		
		do
		{
			m=t.add(p.minus()).abs();
			p=t.clone();
			t=two.mult(p).add(arg1.mult(p.mult(p)).minus());
		}while(m.bigger(t.add(p.minus()).abs()));// p-t!=0 ��������� 1/arg
		
		
		
		//�������
/*			if(!t.add(p.minus()).abs().equals(zero))//���� ����������� �� ����� 0
		{
			m=t.add(p.minus()).abs();
			p=t.clone();
			t=two.mult(p).add(arg1.mult(p.mult(p)).minus());
			t.numbers[0]--;
		}
*/			
		if(shift<0)
		{
			t.point=t.point+shift;
//			if(t.factLength>defaultLength)
//			{
//				int r = t.factLength-defaultLength;
//				t.factLength=defaultLength;
//				int i=0;
//				for(;i+r<t.factLength;i++)
//				{
//					t.numbers[i]=t.numbers[i+r];
//				}
//				for(;i<t.factLength;i++)
//					t.numbers[i]=0;
//			}
			
			//������� �����
		}
		else if(shift>0)
		{
			t.point=t.point+shift;
						
			if(t.point>defaultLength)
			{
				int r=t.point-defaultLength;
				t.point=defaultLength;
				int i=0;
				for(;i+r<t.factLength;i++)
				{
					t.numbers[i]=t.numbers[i+r];
				}
				for(;i<t.factLength;i++)
				{
					t.numbers[i]=0;
				}
			}
		}
		if(bit>0)
		{
			int temp=0;
			int r=0;
			int i=0;
//			if(t.factLength==defaultLength)
//				r=1;
			for(;i+r<t.factLength;i++)
			{
				t.numbers[i]=(t.numbers[i]<<bit)+temp;
				temp=t.numbers[i]>>>16;
				t.numbers[i]=t.numbers[i]&0x0000ffff;
			}
			if(temp!=0)
			{
				if(i<defaultLength)
				{
					t.numbers[i]=temp;
					i++;
				}
				else
				{
					for(i=0;i<defaultLength-1;i++)
					{
						t.numbers[i]=t.numbers[i+1];
					}
					t.numbers[i]=temp;
				}
				
			}
			if(shift>0&&t.numbers[0]==0)
			{
				t=new LongMathInt(t.numbers,t.negat,t.point);//�� ����� t.point ������ i
				return t.mult(this);
			}
			t.factLength=i;
			if(t.factLength<t.point)
				t.factLength=t.point;
			t.negat=arg.negat;
		}
		LongMathInt.defaultLength-=2;
		t=t.clone();
		
		return t.mult(this);
	}
	LongMathInt sin()//������ ��������� �� ������ ������������ ������ ��
	{
		LongMathInt arg=this.clone();//������ ��� ���������� ������ ����������
		if(this.abs().bigger(pi))//������ ��� ���������� ������ ����������
		{
			arg=arg.mod(pi);
		}
		LongMathInt res = new LongMathInt(1l);
		arg =arg.mult(arg);// x^2
		long i=correctOfSin+1;
		while(i>1)
		{
			res.vminus();
			res=arg.div(new LongMathInt(i*(i-1))).mult(res).add(one);
			i=i-2;
		}
		res=res.mult(this);
		return res;
	}
	LongMathInt cos()//������ ��������� �� ������ ������������ ������ ��
	{
		LongMathInt arg=this.clone();//������ ��� ���������� ������ ����������
		if(this.abs().bigger(pi))//������ ��� ���������� ������ ����������
		{
			arg=arg.mod(pi);
		}
		LongMathInt res = new LongMathInt(1l);
		arg =arg.mult(arg);// x^2
		long i=correctOfSin+2;
		while(i>0)
		{
			res.vminus();
			res=arg.div(new LongMathInt(i*(i-1))).mult(res).add(one);
			i=i-2;
		}
		return res;
	}
}