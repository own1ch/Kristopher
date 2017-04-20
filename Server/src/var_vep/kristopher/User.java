package var_vep.kristopher;

import java.net.Socket;
import java.util.Date;

public class User extends Contact{
	
	long password;
	Socket socket;
	Date activ;// дата последней активности
	
	
	public User(int userID, String name, String phone, LongMathInt key,Socket socket,long password) 
	{
		super(userID, phone, phone, key);
		activ=new Date();
		this.socket=socket;
		this.password=password;
	}
	public String toString()
	{
		return userID+"%"+name+"%"+phone+"%"+password+"%";
	}
}