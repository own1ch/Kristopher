package var_vep.kristopher;
import java.io.Serializable;
import java.util.Date;


public class Message implements Serializable
{
	private static final long serialVersionUID = 951825875010359963L;
	int id_Sen; // id ����������� 
	int id_Res; // id ����������  
	Date time; // ���� � ����� �������� ���������
	String text;// ����� ���������
	private long sum; //����������� �����
	boolean sent = false; // ���� - ��������� ������ �� �������
	boolean receive = false; //��������� �������� ���������
	boolean read = false; // ��������� ���������
	
	public Message() 
	{
		this.id_Sen = 0;//0 - ������
		this.id_Res = 0;//0 - ������
		this.time = new Date();//�������������� ������� �����
		this.text = "��� ������ ���\n";
		this.sum = this.hash();
	}

	public Message(int idSender, int idReceiver, String textOfMessage)
	{
		this.id_Sen = idSender;
		this.id_Res = idReceiver;
		this.text = textOfMessage;
		this.time = new Date();//�������������� ������� �����
		this.sum = this.hash();
	}
	void setText(String text)
	{
		this.text = text; 
		this.sum = this.hash();
	}
	
	void setTime(Date date)//���������� ����
	{
		this.time = date;
		this.sum = this.hash();
	}
	void setTime()
	{
		this.time = new Date();//�������������� ������� �����
		this.sum = this.hash();
	}
	void sent()//�������� ���������, ��� ������������
	{
		if(!(this.receive)&&!(this.read))
			this.sent = true;
		else 
		{
			//�������� � ���-����
			System.out.println("���������� ��������� ���� ��������");	
		}
	}
	void received()//���������� ���� ���������
	{
		if (sent&&!(read))
			this.receive = true;
		else 
		{
			//�������� � ���-����
			System.out.println("���������� ��������� ���� ���������");
		}
	}
	void read()//���������� ���� ���������
	{
		if(receive&&sent)
		{
			this.read = true;
		}
		else 
		{
			//�������� � ���-����
			System.out.println("���������� ��������� ���� ��������");
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
		//����� �� ������ ����������� � ����������� �����
		long res;
		res=((long) (Math.cos(this.id_Res)+Math.sin(this.id_Sen)*0xdfffffffffffffffl));
		// ��������!!!
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
		//��� �� ������, � ��� ������=)
	}
	
	public void decrypt()
	{
		//��� �� ������, � ��� ������=)
	}
	
}
