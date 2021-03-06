package var_vep.kristopher;

import java.io.Serializable;
import java.util.ArrayList;

public class ServiceMessage implements Serializable 
{
	private static final long serialVersionUID = -8482759580960236806L;
	int machineID;
	int type;
//	0 - �����������
// 	1 - ��������������, ������������� �� ����� ��� �������� � ������
	String name;
	String phone;
	long password;
// 	2 - ����������� ����� �� �������
	public ArrayList<Integer> contactsOnline;//������ ID
//	3 - ������ ����� ����� �� �������
	
	public ServiceMessage(String name, String phone, long password)
	{
		this.type=0;//�����������
		this.name=name;
		this.phone=phone;
		this.password=password;
	}
	public ServiceMessage(String idetificator, long password,ArrayList<Integer> contacts)
	{
		type=1;//��������������
		char t=idetificator.toCharArray()[0];
		if(t>47&t<58)
			this.phone=idetificator;
		else
			this.name=idetificator;
		this.password=password;
		this.contactsOnline=contacts;//����� ������ ���� ����� ID ���������
	}
	public ServiceMessage(ArrayList<Integer> contactsOnline)
	{
		this.type=2;
		this.contactsOnline=contactsOnline;
		this.machineID=0;//����������� ��� ������
	}
	public ServiceMessage(String name) 
	{
		type = 3;//������ �����
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
