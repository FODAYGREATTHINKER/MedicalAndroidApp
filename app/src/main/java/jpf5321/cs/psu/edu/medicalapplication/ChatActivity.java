package jpf5321.cs.psu.edu.medicalapplication;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.style.UpdateAppearance;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import android.support.v7.app.AppCompatActivity;

public class ChatActivity extends AppCompatActivity {

    private int userId;
    private String fName;
    private String lName;
    private Button sendButton;
    public EditText messageText;
    public String messageToSend;
    public String messageToReceive;
    public int messageToReceiveNumber = 0; // arbitrary message


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        Intent intent = getIntent();
        Bundle bd = intent.getExtras();
        if (bd != null) {
            userId = (int) bd.get("KEY_ID");
            fName = (String) bd.get("KEY_FNAME");
            lName = (String) bd.get("KEY_LNAME");
        }

        messageText = (EditText) findViewById(R.id.message_text);

        sendButton = (Button) findViewById(R.id.send_button);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // send text in message_text field
                messageToSend = messageText.getText().toString();
                Chat chat = new Chat();
                chat.setMessage(messageToSend);
                new HttpPostTask().execute(chat);
            }
        });
        new HttpGetTask().execute();
        updateDisplay(messageToReceive, 0);
    }

    public void updateDisplay(String message, int sentOrReceived)
    {
        // show message on screen -- left side if received -- right side if sent

    }

    private class HttpPostTask extends AsyncTask<Chat,Void, Integer>
    {
        @Override
        protected Integer doInBackground(Chat... params) {

            Integer integer = null;

            try
            {
                final String URL = "http://10.0.2.2:8080/SendMessage";
                RestTemplate restTemplate = new RestTemplate();
                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
                integer = restTemplate.postForObject(URL, params[0], Integer.class);

            } catch (Exception e) {
                Log.e("Login Endpoint", e.getMessage(), e);
            }

            return integer;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            if (integer == -1) {
                int messageResId = R.string.failed_send_toast;
                Toast.makeText(ChatActivity.this, messageResId, Toast.LENGTH_SHORT).show();

            } else {
                int messageResId = R.string.successful_send_toast;
                Toast.makeText(ChatActivity.this, messageResId, Toast.LENGTH_SHORT).show();
                updateDisplay(messageToSend, 1);
            }
        }

    }

    private class HttpGetTask extends AsyncTask<Void, Void, Chat>{

        @Override
        protected Chat doInBackground(Void... params){

            try
            {
                final String URL = "http://10.0.2.2:8080/GetMessage?MessageNumber=" + Integer.toString(messageToReceiveNumber);
                RestTemplate restTemplate = new RestTemplate();
                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
                Chat chat = restTemplate.getForObject( URL, Chat.class);
                return chat;
            } catch (Exception e) {
                Log.e("Login Endpoint", e.getMessage(), e);
            }

            return null;
        }

        @Override
        protected void onPostExecute(Chat chat) {
            if(chat != null){
                messageToReceive = chat.getMessage();
            }
            else{
                Toast.makeText(ChatActivity.this, R.string.message_not_found, Toast.LENGTH_SHORT).show();
            }
        }
    }
}
