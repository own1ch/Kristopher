package var_vep.kristopher;

/**
 * Created by Евгений on 08.04.2017.
 */
import java.io.Serializable;
import java.util.Date;


public class Message implements Serializable
{
    private static final long serialVersionUID=951825875010359963l;
    int id_Sen; // id отправителя
    int id_Res; // id получателя
    Date time; // дата и время отправки сообщения
    String text;// текст сообщения
    private long sum; //контрольная сумма
    boolean sent = false; // флаг - сообщение пришло на ссервер
    boolean receive = false; //сообщение получено адресатом
    boolean read = false; // сообщение прочитано

    public Message()
    {
        this.id_Sen = 0;//0 - сервер
        this.id_Res = 0;//0 - сервер
        this.time = new Date();//инициализирует текущей датой
        this.text = "тут ничего нет\n";
        this.sum = this.hash();
    }

    public Message(int idSender, int idReceiver, String textOfMessage)
    {
        this.id_Sen = idSender;
        this.id_Res = idReceiver;
        this.text = textOfMessage;
        this.time = new Date();//инициализирует текущей датой
        this.sum = this.hash();
    }
    void setText(String text)
    {
        this.text = text;
        this.sum = this.hash();
    }

    void setTime(Date date)//установить дату
    {
        this.time = date;
        this.sum = this.hash();
    }
    void setTime()
    {
        this.time = new Date();//инициализирует текущей датой
        this.sum = this.hash();
    }
    void sent()//помечает сообщение, как отправленное
    {
        if(!(this.receive)&&!(this.read))
            this.sent = true;
        else
        {
            //записать в лог-файл
            System.out.println("Невозомжно выставить флаг отправки");
        }
    }
    void received()//выставляет флаг получения
    {
        if (sent&&!(read))
            this.receive = true;
        else
        {
            //записать в лог-файл
            System.out.println("Невозомжно выставить флаг получения");
        }
    }
    void read()//выставляет флаг прочтения
    {
        if(receive&&sent)
        {
            this.read = true;
        }
        else
        {
            //записать в лог-файл
            System.out.println("Невозомжно выставить флаг доставки");
        }
    }
    void setId_Sen(int id)
    {
        this.id_Sen = id;
        this.sum=this.hash();
    }
    void setId_Res(int id)
    {
        this.id_Res = id;
        this.sum=this.hash();
    }

    public long hash()
    {
        //флаги не должны учитываться в контрольной сумме
        long res;
        res=((long) (Math.cos(this.id_Res)+Math.sin(this.id_Sen)*0xdfffffffffffffffl));
        // дописать!!!
        res^=(((long)(this.time.hashCode()))<<32)^this.text.hashCode();
        return res;
    }
    public void set_sum()
    {
        this.sum=this.hash();
    }
    public String toString()
    {
          return "from: "+this.id_Sen+"\n"+time.toString()+"\n"+this.text;
    }
    public void encrypt()
    {
        //это не трогай, Я сам сделаю=)
    }

    public void decrypt()
    {
        //это не трогай, Я сам сделаю=)
    }
}