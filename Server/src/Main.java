import java.io.*;
import java.net.*;
import java.util.ArrayList;

import var_vep.kristopher.*;
public class Main {
	static ArrayList<Integer> usersOnline;
	static BufferedWriter bw;//��������� ����� ������������� � ������
	static BufferedReader br;//��������� ������ �������������
	static BufferedReader brcID;//��������� currentID �� �����
	static BufferedWriter bwcID;//���������� currentID � ����
	static ServerSocket ss;
	static Socket userSocket;
	static ServiceMessage service;
	static int currentID;
	public Main() {
		
		// TODO Auto-generated constructor stub
	}
	public static void main(String[] args) throws IOException, ClassNotFoundException 
	{
		//�������������� �������� ������, ��� ������ �� ������� �������������
		bw = new BufferedWriter(new FileWriter("users.txt", true));
		br = new BufferedReader(new FileReader("users.txt"));
		brcID = new BufferedReader(new FileReader("currentID.txt"));
		//������� ������� ��������� ID ��� �����������
		currentID=Integer.parseInt(brcID.readLine());
		
		//��������� ������-�����
		int port = 6666;
		ss = new ServerSocket(port);
		System.out.println("��������!=D");
		
		
		while(true)
		{
			userSocket = ss.accept();
			System.out.println("����������� ����� ������������!");
			ObjectInputStream in = new ObjectInputStream(userSocket.getInputStream());
			service =(ServiceMessage) in.readObject();
			if(service.getType()==0)
			{
				new Registration(service,userSocket).run();
			}
			else if(service.getType()==1)
			{
				new Autentification(service,userSocket).run();
			}
			else
			{
				System.out.println("������ Main: ������ ServiceMessage type: "+service.getType());
			}
		}		
		//BufferedReader keyboard = new BufferedReader(new InputStreamReader(System.in));
		//s=keyboard.readLine();
	}
	static private ArrayList<Integer> whoIsOnline(ServiceMessage service)
	{
		//���������� ������ � ID ��������� ������ ������������ � ���������
		ArrayList<Integer> res = new ArrayList<Integer>();
		for(int i:service.contactsOnline)
		{
			if(Main.usersOnline.contains(i))
			{
				res.add(i);
			}
		}		
		return res;
	}
	static LongMathInt findKey(int machineID) throws IOException//���������� ���� ��� �������� ������
	{
		LongMathInt res;
		BufferedReader b = new BufferedReader(new FileReader("keys.txt"));
		String str[]=b.readLine().split("%");
		while(Integer.parseInt(str[0])!=machineID)
		{
			str=b.readLine().split("%");
			if (str[0]==null)
				break;
		}
		b.close();
		//��� �� ���� ����� ����������� ������ � �����, ���� ����� �� ����� �����
		if(str[0]!=null)//���� �� ����� ��������� ������
		{
			res=LongMathInt.fromString(str[1]);
		}
		else
		{
			System.out.println("������ findKey(int machineID): machineID "+machineID+" �� ������");
			System.out.println(service.toString());
			res=new LongMathInt();
		}
		return res;
	}
	public static class Registration implements Runnable
	{
		Socket socket;
		ServiceMessage service;
		
		public Registration(ServiceMessage service,Socket socket)
		{
			if(service.getType()==0)
			{
				this.service=service;
				this.socket=socket;			
			}
			else
			{
				System.out.println("������ Registration: ServiceMessage type!=0: "+service.getType());
			}
		}
		@Override
		public void run() 
		{	
			try 
			{
				//��������� ������ ������������
				User newUser=new User(currentID, service.getName(), service.getPhone(), Main.findKey(service.getMachineID()) ,this.socket, service.getPassword());
				
				//���������� ��� � ����
				bw.append(newUser.toString());
				
				//����������� ������� ID �� 1
				currentID++;
				
				//���������� ����������� currentID � ����.
				bwcID = new BufferedWriter(new FileWriter("currentID.txt", false));
				bwcID.append(Integer.toString(currentID));
				bwcID.close();
				
				//�������� ��� ���������� ���������
				service.setType(1);
				
				//��������� ��������������
				new Autentification(service,socket).run();
				
			} 
			catch (Exception e) {e.printStackTrace();}
			
		}
	}	
	
	public static class Autentification implements Runnable
	{
		Socket socket;
		ServiceMessage service;
		public Autentification(ServiceMessage service,Socket socket)
		{
			if(service.getType()==1)
			{
				this.service=service;
				this.socket=socket;			
			}
			else
			{
				System.out.println("������ Autentification: ServiceMessage type!=0: "+service.getType());
			}
		}

		@Override
		public void run() 
		{
			try 
			{
				br.reset();
				String ident =service.getName();
				int indexCol=1;
				if(ident==null)
				{
					ident=service.getPhone();
					indexCol=2;
				}	
				if(ident==null)
				{
					System.out.println("������ Autentification: ����������� ��� � ������� � ServiceMessage");
					return;
				}
				
				String[] str;
				do
				{
					str=br.readLine().split("%");
					if(str[0]==null)//������ �������� ����
					{
						if(indexCol==1)
							System.out.println("��� ������������ �� �������: "+ident);
						else if(indexCol==2)
							System.out.println("����� �������� �� ������: "+ident);
						//�������� �������� ��������������� ��������� ������������
						return;
					}
				}
				while(!str[indexCol].equals(ident));//���� ��� ��� ����� ������ � ��� ��� � �����
				//��������� ������
				if(service.getPassword()==Long.parseLong(str[3]))
				{
					//������������ ���������� ����
					
					//���������� ��� ������ �� ������ ������ ���������
					ServiceMessage ans=new ServiceMessage(whoIsOnline(service));
					//������������ �����
					
					//���������� �����
					ObjectOutputStream writer= new ObjectOutputStream(socket.getOutputStream());
					writer.writeObject(ans);
					writer.flush();
					writer.close();
					
					//��������� ������ � ��������
					new Working(this.socket.getOutputStream(),this.socket.getInputStream()).run();
				}
			} 
			catch (IOException e) {e.printStackTrace();}
		}
	}	
	public static class Working implements Runnable
	{
		ObjectOutputStream out;
		ObjectInputStream in;
		public Working(OutputStream out,InputStream in) throws IOException
		{
			this.out=new ObjectOutputStream(out);
			this.in=new ObjectInputStream(in);
		}
		
		public void run() 
		{
			try 
			{
				Message m;
				System.out.println("������������ ������ �����������!");
				
				while (true)
				{
					m =(Message) in.readObject();
					System.out.println(m);
					out.writeObject(m);
					out.flush();//����������� �������� ������
					System.out.println("���������");
				}
				
			} 
			catch (Exception e) {e.printStackTrace();}
		}
	}

}



