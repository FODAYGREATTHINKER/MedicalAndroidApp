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
import org.json.JSONObject;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;

public class ChatActivity extends AppCompatActivity {

    private int userId;
    private String fName;
    private String lName;
    private Button sendButton;
    public EditText messageText;
    public String messageToSend;
    public String messageToReceive;
    public int messageToReceiveNumber = 1; // arbitrary message

    private TextView mTextView1;
    private TextView mTextView2;


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
                messageText.setText("");
            }
        });

    }



    public void updateDisplay(String message, int sentOrReceived)
    {
       mTextView1 = findViewById(R.id.chat_text_view1);
       mTextView2 = findViewById(R.id.chat_text_view2);
       if(sentOrReceived == 0){
           mTextView1.append(String.format("\n%-1s\n", message));
       }
       else{
           mTextView2.append(String.format("%1s\n\n", message));
       }

    }

    private class HttpPostTask extends AsyncTask<Chat,Void, Integer>
    {
        @Override
        protected Integer doInBackground(Chat... params) {

            Integer integer = null;

            try
            {
                final String URL = "http://10.0.2.2:8080/SendMessage?user="+Integer.toString(userId);
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

            }else {
                int messageResId = R.string.successful_send_toast;
                Toast.makeText(ChatActivity.this, messageResId, Toast.LENGTH_SHORT).show();
                messageToReceiveNumber = integer;
                updateDisplay(messageToSend, 1);
                new HttpGetTask().execute();
            }
        }

    }

    private class HttpGetTask extends AsyncTask<Void, Void, Chat>{

        @Override
        protected Chat doInBackground(Void... params){

            try
            {
                final String URL = "http://10.0.2.2:8080/GetMessage?user=" + Integer.toString(userId);
                RestTemplate restTemplate = new RestTemplate();
                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
                ArrayList<String> msgList = restTemplate.getForObject(URL, ArrayList.class);
                Chat chat = new Chat();
                chat.setMessage(msgList.get(msgList.size()-1).replace("{\"message\":\"", "").replace("\"}", ""));
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
                updateDisplay(messageToReceive, 0);
            }
            else{
                Toast.makeText(ChatActivity.this, R.string.message_not_found, Toast.LENGTH_SHORT).show();
            }

        }
    }
}