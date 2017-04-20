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
	static int defaultLength=32;// количаство байтов получаемых в результате вычислений от 4х (LongMathInt(long)) до 16001 (инициализация пи)
	static LongMathInt zero=new LongMathInt();
	//static LongMathInt ten=new LongMathInt(10l);
	static LongMathInt minusOne = new LongMathInt(-1l);
	static LongMathInt one = new LongMathInt(1l);
	static LongMathInt pi = LongMathInt.constant("pi");
	static LongMathInt min= LongMathInt.cr();
	int point;//указывает на первое число целой части
	boolean negat;//является ли число отрицательным
	int [] numbers=new int[LongMathInt.defaultLength];//числа в 65 536 ичной системе счисления
	int factLength;//указывает на последный непустой индекс +1
	private static int correctOfSin=LongMathInt.correctOf();// 1/(correctOfSin!)=0 обязательно четное!
	public LongMathInt(int[] num,boolean negat,int point)//создает объект с полями
	{
		if(point<0||point>num.length)
		{
			System.out.println("Ошибка конструкора LongMathInt(int[],boolean,int): некорректное значение point");
			return;
		}
		int n=0;
		int m=num.length;
		int r=0;
		while(n<point&&num[n]==0)
		{
			n++;//убираем нулевые элементы вначале
		}
		while(m>point&&num[m-1]==0)
			m--;//убираем нулевые элементы вконце

		if (m-n>LongMathInt.defaultLength)//проверка на количество
		{
			//System.out.println("Предупреждение конструктора LongMathInt(int[]): Массив содержит слишком много не нулевых элементов!");
			n=0;
			r=m-n-defaultLength;
			while(n+r<num.length&&num[n+r]==0)//после того как назначили r могли появиться нули в начале, их нужно убрать
			{
				n++;//убираем нулевые элементы вначале
			}
		}

		this.negat=negat;//установили знак числа
		this.point=point-n-r;
		while(this.point<0)
		{
			n--;
			this.point++;
		}
		int i=0;
		while(i<LongMathInt.defaultLength&n+r<m)//инициализируем массивом до m
		{
			if((num[n+r]&0x0000ffff)!=num[n+r])//проверка на элемнты разрядностью более 16
			{
				System.out.println("Ошибка: задан массив, содержащий элементы разрядностью больше 16");//число либо больше 65535, либо меньше 0
				return;
			}
			this.numbers[i]=num[n+r];
			i++;
			n++;
		}
		if(i>this.point)
			this.factLength=i;//устанавливаем поле factLength
		else
			this.factLength=this.point;
	}
	public LongMathInt(long number)//создает объект по числу переданному в аргументе
	{
		long arg=number;//копируем переданное число, чтобы можно было его изменять
		this.point=0;//устанавливаем точку на нулевом положении, так как число целое
		this.negat=(number<0);//считываем знак числа
		arg=abs(arg);//делаем число положительным
		int i=0;
		while(arg!=0)//делим лонг на 4 куска по 16 бит
		{
			this.numbers[i]=(int) (arg&0x000000000000ffff);//arg%65536
			arg>>>=16;//сдвигаем вправо на 16 бит
			i++;
		}
		this.factLength=i;//устанавливаем поле factLength
	}
	public LongMathInt() //создает объект с числом 0
	{
		this.point=0;
		this.factLength=0;
		this.negat=false;
	}
	//вспомогательные функции
	static LongMathInt constant(String s)//ДОПИСАТЬ!!!
	{
		if(s.equalsIgnoreCase("pi"))
		{
			LongMathInt res =new LongMathInt();
			FileInputStream fromFile;
			try
			{
				fromFile = new FileInputStream(new File("F:\\Облако mail\\Документы\\workspace\\Shifrovanie\\src\\tests2\\pi_int.txt"));
			}
			catch (FileNotFoundException e)
			{
				System.out.println("LongMathInt constant(String s) Ошибка: невозможно чтение из файла");
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
					System.out.println("LongMathInt constant(String s) Ошибка чтения символа "+i);
					e.printStackTrace();
				}
			}
			try {
				fromFile.close();
			} catch (IOException e) {
				System.out.println("LongMathInt constant(String s) Ошибка при закрытии файла");
				e.printStackTrace();
			}
			return res;
		}
		else
		{
			System.out.println("Задано некорректное имя константы");
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
			fromFile = new BufferedReader(new FileReader("F:\\Облако mail\\Документы\\workspace\\Shifrovanie\\src\\tests2\\memory.txt"));
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
				System.out.println("Вычисляем число слагаемых для тригонометрических функций...");
				for(t=1;!c.equals(LongMathInt.zero);t++)
				{
					b= new LongMathInt((long)t);
					c=c.div(b).mult(pi);
					if(t<0)
						System.out.println("Задайте defaultlength меньше");
				}
				if((t&0x1)==1)
					t++;
				System.out.println("Готово! "+t);
				FileWriter writeFile= new FileWriter( new File("F:\\Облако mail\\Документы\\workspace\\Shifrovanie\\src\\tests2\\memory.txt"));
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
	static int abs(int arg)//возвращает модуль числа
	{
		if(arg>-1)
			return arg;
		else
			return -arg;
	}
	static long abs(long arg)//возвращает модуль числа
	{
		if(arg>-1)
			return arg;
		else
			return -arg;
	}
	LongMathInt mod (LongMathInt mod)// возвращает число из промежутка (-mod;mod)
	{

		LongMathInt t=this.div(mod);
		int i=0;
		for(;i+t.point<t.factLength;i++)//оставляем только целую часть
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
	//стандартные методы
	protected LongMathInt clone()//создает новую копию объекта
	{
		LongMathInt result = new LongMathInt();
		int min=this.factLength;
		if(this.factLength>defaultLength)
			min=defaultLength;
		result.factLength = min;
		int d=this.factLength-defaultLength;
		if(d<0)
			d=0;
		result.negat=this.negat;//выставляем знак числа
		result.point=this.point-d;//выставляем точку

		for(int i=min-1;i>-1;i--)
			result.numbers[i]=this.numbers[i+d];
		return result;
	}
	public static LongMathInt fromString(String number)//работает только для целых чисел неотрицательных чисел
	{
		LongMathInt res = new LongMathInt();
		String[] t=number.split(" ");
		if(t.length>defaultLength)
		{
			System.out.println("LongMathInt fromString(String number): передана строка содержащая слишком много элементов");
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
	public String toString()//преобразует объект в строку, удобную для восприятия
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
	boolean equals(LongMathInt arg)//1 если равны 0 иначе
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
	//методы для работы над объектами
	LongMathInt minus()//возвращает объект со знаком -
	{
		LongMathInt res= this.clone();
		if(this.negat)
			res.negat=false;
		else
			res.negat=true;
		return res;
	}
	void vminus()//меняет знак у объекта
	{
		if(this.negat)
			this.negat=false;
		else
			this.negat=true;
	}
	LongMathInt abs()//возвращает число без учета знака
	{
		LongMathInt res=this.clone();
		res.negat=false;
		return res;
	}
	void vabs()//делает число положительным
	{
		this.negat=false;
	}
	boolean bigger(LongMathInt arg)//возвращает 1 если элемент больше аргумента, 0 если меньше либо равно, долго работает для равных элементов
	{
		if(this.negat&!arg.negat)//первый отрицат, второй положит
			return false;
		else if((!this.negat)&arg.negat)//первый положит, второй отрицат
			return true;
		else if(!(this.negat&arg.negat))//оба положительны
		{
			//сравниваем по длине целой части
			if(this.factLength-this.point>arg.factLength-arg.point)//если длина целой части больше у первого
				return true;
			else if(this.factLength-this.point<arg.factLength-arg.point)//если длина целой части больше у второго
				return false;
			else//если длины целой части равны
			{
				for(int i=this.factLength-1,j=arg.factLength-1;(i>-1)&(j>-1);i--,j--)
				{
					if(this.numbers[i]>arg.numbers[j])
						return true;
					else if((this.numbers[i]<arg.numbers[j]))
						return false;
				}//если дошли до конца одного из массивов
				if(this.factLength>arg.factLength)
					return true;
				else
					return false;
			}
		}
		else//оба отрицательны
		{
			//сравниваем по длине целой части
			if(this.factLength-this.point>arg.factLength-arg.point)//если длина целой части больше у первого
				return false;
			else if(this.factLength-this.point<arg.factLength-arg.point)//если длина целой части больше у второго
				return true;
			else//если длины целой части равны
			{
				for(int i=this.factLength-1,j=arg.factLength-1;(i>-1)&(j>-1);i--,j--)
				{
					if(this.numbers[i]>arg.numbers[j])
						return false;
					else if((this.numbers[i]<arg.numbers[j]))
						return true;
				}//если дошли до конца одного из массивов
				if(this.factLength>=arg.factLength)
					return false;
				else
					return true;
			}
		}
	}
	//арифметические операции
	LongMathInt add(LongMathInt arg)
	{
		if(!(this.negat^arg.negat))//если одинаковые знаки
		{
			if(!arg.bigger(this))//this больше либо равно arg
			{
				if(arg.factLength==0)
					return this.clone();
				LongMathInt res = new LongMathInt();
				int r=this.point-arg.point;//разница длинн дробных частей
				int l = this.factLength-this.point-arg.factLength+arg.point;//разница длин целых частей всегда >= 0
				int i=0, t=0;
				int p=0;//сдвиг точки относительно this

				if(r>=0)
				{
					//превысит ли число размеры максимальо допустимый размер? только прогноз так как не учитываются предыдущие разряды
					//если не превысит
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
								for(i=0;i<defaultLength-1;i++)//подвинем горы и поместим злощасную 1
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
					else//результат сложения по-любому выйдет за границу; l=0, this.factLength=defaultLength
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
					//если результат поместится
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
								for(;i<defaultLength-1;i++)//подвинем горы и поместим злощасную 1
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
					else//результат гарантированно слишком большой
					{
						p=this.factLength+r-defaultLength;//не отрицательно

						//если размер увеличится в результате сложения
						if((l==0)&(this.numbers[this.factLength-1]+arg.numbers[arg.factLength-1]>>>16!=0))
							p++;
						r=r-p;//не отрицательно так как r-p=defaultLength-this.length
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
								for(;i<defaultLength-1;i++)//подвинем горы и поместим злощасную 1
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
		else//знаки разные
		{
			if(!arg.abs().bigger(this.abs()))//this больше по значению
			{
				LongMathInt res = new LongMathInt();
				int r=this.point-arg.point;//разница длинн дробных частей
				//int l = this.factLength-this.point-arg.factLength+arg.point;//разница длин целых частей всегда >= 0
				int i=0, t=0;
				int p=0;//сдвиг точки относительно this
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
					while(i>0&&res.numbers[i-1]==0)//определяем фактическую длинну
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
							i--;//убираем нулевые элементы вконце
						p=r;
					}
					else//длина ответа получается больше чем надо
					{
						p=this.factLength+r-defaultLength;//не отрицательно
						r=r-p;//не отрицательно так как r-p=defaultLength-this.length
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
							i--;//убираем нулевые элементы вконце
						}

					}

				}

				res.point=this.point+p;
				if(i>res.point)
					res.factLength=i;
				else
					res.factLength=res.point;

				res.negat=this.negat;
				if(res.numbers[0]==0)//убираем нули вначале
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
	LongMathInt div(LongMathInt arg)// через обртный
	{
		if(arg.equals(zero))
		{
			System.out.println("Ошибка метод div(): Деление на ноль!!!");
			return new LongMathInt();
		}
		if(this.equals(arg))
		{
			return one.clone();
		}
		LongMathInt p=new LongMathInt();// для определения погрешности
		LongMathInt t =new LongMathInt(new int[] {32768,1},false,1);//	1/arg
		LongMathInt two = new LongMathInt(2l);
		LongMathInt arg1 = arg.clone().abs();

		//реализовать деление на 2^N
		int shift=arg1.factLength-arg1.point;//величина сдвига в интах, если у числа есть целая часть будет >0 иначе =0
		if(shift==0)
		{
			while(arg1.numbers[arg1.factLength+shift-1]==0)//выставляем отрицательное значение сдвига при необходимости
				shift--;
			if(shift<0)
			{
				arg1.point=arg1.factLength=arg1.factLength+shift;
			}
		}
		else//shift>0
		{
			arg1.point=arg1.factLength;
		}//теперь число из промежутка [0;1]
		int bit=0;
		while(((arg1.numbers[arg1.factLength-1]<<bit)&0x00008000)!=0x8000)//пока 15й бит не будет равен 1, что соответствует тому, что число >=0.5
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
			if(temp!=0)//удалить, если все хорошо=)
			{
				System.out.println("Кривые руки програмиста!!!");
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
		}while(m.bigger(t.add(p.minus()).abs()));// p-t!=0 вычисляем 1/arg



		//костыль
/*			if(!t.add(p.minus()).abs().equals(zero))//если погрешность не равна 0
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

			//сделать сдвиг
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
				t=new LongMathInt(t.numbers,t.negat,t.point);//на месте t.point стояло i
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
	LongMathInt sin()//модуль аргумента не должен превосходить модуль пи
	{
		LongMathInt arg=this.clone();//убрать при реализации частей окружности
		if(this.abs().bigger(pi))//убрать при реализации частей окружности
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
	LongMathInt cos()//модуль аргумента не должен превосходить модуль пи
	{
		LongMathInt arg=this.clone();//убрать при реализации частей окружности
		if(this.abs().bigger(pi))//убрать при реализации частей окружности
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