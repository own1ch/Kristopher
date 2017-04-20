package var_vep.kristopher;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

public class LoginActivity extends AppCompatActivity {
    private EditText name;
    private EditText phone;
    private EditText password;
    private TextView error;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        name = (EditText) findViewById(R.id.name);
        phone = (EditText) findViewById(R.id.phone);
        password = (EditText) findViewById(R.id.password);
        error = (TextView) findViewById(R.id.error);


    }
    public void onClick(View v)
    {
        String n = name.getText().toString();
        String p = phone.getText().toString();
        long pwd = Long.parseLong(password.getText().toString());//переделать!!!
        if(n.length()<3)
        {
            error.setText("Введите полное имя!");
            return;
        }
        if(p.length()!=11) {
            error.setText("Номер телефона должен состоять из 11 цифр!");
            return;
        }
        Connection con = new Connection(n,pwd,new ArrayList<Integer>());
        con.execute();

        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("connection",con);
        startActivity(intent);

    }

}
