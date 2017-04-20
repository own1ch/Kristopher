import java.io.*;
import java.net.*;
import java.util.ArrayList;

import var_vep.kristopher.*;
public class Main {
	static ArrayList<Integer> usersOnline;
	static BufferedWriter bw;//добавляет новых пользователей в список
	static BufferedReader br;//считывает список пользователей
	static BufferedReader brcID;//считывает currentID из файла
	static BufferedWriter bwcID;//записывает currentID в файл
	static ServerSocket ss;
	static Socket userSocket;
	static ServiceMessage service;
	static int currentID;
	public Main() {
		
		// TODO Auto-generated constructor stub
	}
	public static void main(String[] args) throws IOException, ClassNotFoundException 
	{
		//инициализируем файловые потоки, для работы со списком пользователей
		bw = new BufferedWriter(new FileWriter("users.txt", true));
		br = new BufferedReader(new FileReader("users.txt"));
		brcID = new BufferedReader(new FileReader("currentID.txt"));
		//находим текущий свободный ID для регистрации
		currentID=Integer.parseInt(brcID.readLine());
		
		//запускаем сервер-сокет
		int port = 6666;
		ss = new ServerSocket(port);
		System.out.println("Работаем!=D");
		
		
		while(true)
		{
			userSocket = ss.accept();
			System.out.println("Подключился новый пользователь!");
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
				System.out.println("Ошибка Main: Пришло ServiceMessage type: "+service.getType());
			}
		}		
		//BufferedReader keyboard = new BufferedReader(new InputStreamReader(System.in));
		//s=keyboard.readLine();
	}
	static private ArrayList<Integer> whoIsOnline(ServiceMessage service)
	{
		//возвращает массив с ID контактов онлайн перечиленных в аргументе
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
	static LongMathInt findKey(int machineID) throws IOException//возвращает ключ для заданной машины
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
		//тут мы либо нашли необходимую строку в файле, либо дошли до конца файла
		if(str[0]!=null)//если мы нашли требуемую строку
		{
			res=LongMathInt.fromString(str[1]);
		}
		else
		{
			System.out.println("Ошибка findKey(int machineID): machineID "+machineID+" не найден");
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
				System.out.println("Ошибка Registration: ServiceMessage type!=0: "+service.getType());
			}
		}
		@Override
		public void run() 
		{	
			try 
			{
				//добавляем нового пользователя
				User newUser=new User(currentID, service.getName(), service.getPhone(), Main.findKey(service.getMachineID()) ,this.socket, service.getPassword());
				
				//записываем его в файл
				bw.append(newUser.toString());
				
				//увеличиваем текущий ID на 1
				currentID++;
				
				//записываем обновленный currentID в файл.
				bwcID = new BufferedWriter(new FileWriter("currentID.txt", false));
				bwcID.append(Integer.toString(currentID));
				bwcID.close();
				
				//изменяем тип сервисного сообщения
				service.setType(1);
				
				//запускаем аутентификацию
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
				System.out.println("Ошибка Autentification: ServiceMessage type!=0: "+service.getType());
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
					System.out.println("Ошибка Autentification: отсутствуют имя и телефон в ServiceMessage");
					return;
				}
				
				String[] str;
				do
				{
					str=br.readLine().split("%");
					if(str[0]==null)//значит кончился файл
					{
						if(indexCol==1)
							System.out.println("Имя пользователя не найдено: "+ident);
						else if(indexCol==2)
							System.out.println("Номер телефона не найден: "+ident);
						//дописать отправку информационного сообщения пользователю
						return;
					}
				}
				while(!str[indexCol].equals(ident));//если имя или номер совпал с тем что в файле
				//проверяем пароль
				if(service.getPassword()==Long.parseLong(str[3]))
				{
					//пользователь подтвердил себя
					
					//обработаем его запрос на список онлайн контактов
					ServiceMessage ans=new ServiceMessage(whoIsOnline(service));
					//сформировали ответ
					
					//отправляем ответ
					ObjectOutputStream writer= new ObjectOutputStream(socket.getOutputStream());
					writer.writeObject(ans);
					writer.flush();
					writer.close();
					
					//запускаем работу с клиентом
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
				System.out.println("пользователь прошел авторизацию!");
				
				while (true)
				{
					m =(Message) in.readObject();
					System.out.println(m);
					out.writeObject(m);
					out.flush();//заканчивает передачу данных
					System.out.println("переслали");
				}
				
			} 
			catch (Exception e) {e.printStackTrace();}
		}
	}

}



