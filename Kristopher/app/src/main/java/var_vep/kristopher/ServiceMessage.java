package var_vep.kristopher;

import java.io.Serializable;
import java.util.ArrayList;

public class ServiceMessage implements Serializable
{
	private static final long serialVersionUID = -8482759580960236806L;
	int machineID;
	int type;
	//	0 - регистрация
// 	1 - аутентификация, идентификация по имени или телефону и паролю
	String name;
	String phone;
	long password;
	// 	2 - авторизация ответ от сервера
	public ArrayList<Integer> contactsOnline;//список ID
//	3 - ошибка входа ответ от сервера

	public ServiceMessage(String name, String phone, long password)
	{
		this.type=0;//регистрация
		this.name=name;
		this.phone=phone;
		this.password=password;
	}
	public ServiceMessage(String identificator, long password,ArrayList<Integer> contacts)
	{
		type=1;//аутентификация
		char t=identificator.toCharArray()[0];
		if(t>47&t<58)
			this.phone=identificator;
		else
			this.name=identificator;
		this.password=password;
		this.contactsOnline=contacts;//чтобы сервер знал какие ID проверять
	}
	public ServiceMessage(ArrayList<Integer> contactsOnline)
	{
		this.type=2;
		this.contactsOnline=contactsOnline;
		this.machineID=0;//подписались как сервер
	}
	public ServiceMessage(String name)
	{
		type = 3;//ошибка входа
		this.name=name;
	}
	public String toString()
	{
		return "machineID= "+machineID+" type= "+type+" name= "+name+" phone= "+phone+" pwd= "+password;
	}
	public int getType()
	{
		return this.type;
	}
	public String getName()
	{
		return this.name;
	}
	public String getPhone()
	{
		return this.phone;
	}
	public long getPassword()
	{
		return this.password;
	}
	public void setMachineID(int machineID)
	{
		this.machineID=machineID;
	}
	public void setType(int type)
	{
		this.type=type;
	}
	public int getMachineID()
	{
		return this.machineID;
	}
}
