package var_vep.kristopher;

import android.os.AsyncTask;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Created by Евгений on 09.04.2017.
 */
public class Connection extends AsyncTask implements Serializable
{
    Socket socket;
    ObjectInputStream in;
    ObjectOutputStream out;
    String IP ="192.168.1.18";
    int port = 6666;
    Message message;
    public ArrayList<Integer> contacts;
    public ServiceMessage service;
    boolean authentication;//зарегестрирован ли пользователь?
    String error;
    public Connection(String identificator, long password, ArrayList<Integer> contacts)
    {
        service = new ServiceMessage(identificator,password,contacts);
        authentication = false;
        error="";
    }
    public Connection(String name, String phone, long password)
    {
        service = new ServiceMessage(name, phone, password);
        authentication = false;
        error="";
    }
    public void setAuthentication(boolean arg)
    {
        this.authentication=arg;
    }

    @Override
    protected Object doInBackground(Object[] params) {
        this.error = "So good!";
        try
        {
            socket = new Socket(IP, port);
            this.in = new ObjectInputStream(socket.getInputStream());
            this.out = new ObjectOutputStream(socket.getOutputStream());
            out.writeObject(service);
            service = (ServiceMessage) in.readObject();
            if (service.getType() == 3) {
                //неверный логин или пароль
                return null;
            } else if (service.getType() == 2) {
                contacts = service.contactsOnline;
                authentication=true;
            } else {
                //Все плохо=(( как такое могло произойти??
                return null;
            }
        }
        catch (Exception e) {error=e.toString();}
        return null;
    }
}
