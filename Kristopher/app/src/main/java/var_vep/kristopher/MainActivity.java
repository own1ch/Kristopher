package var_vep.kristopher;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class MainActivity extends AppCompatActivity {
    //private static BreakIterator history;
    static EditText text;
    static TextView history;
    static Connection connection;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;
    public static void setText(String s)
    {
        history.setText(s);
    }
    public static String getText()
    {
        return text.getText().toString();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        history = (TextView) findViewById(R.id.history);
        text = (EditText) findViewById(R.id.text);
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();

        // new Connect().execute();

    }

    @Override
    protected void onStart() {
        super.onStart();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://var_vep.kristopher/http/host/path")
        );

        connection = (Connection) getIntent().getSerializableExtra("connection");//получаем соединение из предыдущего активити
        AppIndex.AppIndexApi.start(client, viewAction);

    }

    @Override
    protected void onResume()
    {
        super.onResume();
    }

    public void onClick(View v)
    {
        new Click().execute();
    }

    class Click extends AsyncTask
    {
        Message m;
        @Override
        protected void onPostExecute(Object o) {

            super.onPostExecute(o);
            if(m!=null)
                MainActivity.setText("It sent me this : " + m.toString());
            else
                MainActivity.setText("null=(");
        }

        @Override
        protected Object doInBackground(Object[] params)
        {
            try {
                // MainActivity.setText("1");
                m = new Message(1,1,MainActivity.getText().toString());
                connection.out.writeObject(m); // отсылаем введенную строку текста серверу.
                connection.out.flush(); // заставляем поток закончить передачу данных.
                m = (Message) connection.in.readObject(); // ждем пока сервер отошлет строку текста.

            } catch (Exception e)
            {
                m.setText("Ошибка: " + e.toString());
            }
            return null;
        }
    }
    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://var_vep.kristopher/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }

/*    class Connect extends AsyncTask
    {

        @Override
        protected Void doInBackground(Object[] params)
        {
            try
            {

                in = new ObjectInputStream(socket.getInputStream());
                out = new ObjectOutputStream(socket.getOutputStream());
            }
            catch (IOException e){MainActivity.setText(e.toString());}
           return null;
        }
    }
*/

}
