package jpf5321.cs.psu.edu.medicalapplication;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

public class MainActivity extends AppCompatActivity {

    private Button signInButton;
    private TextView signUpTextView;
    private EditText editUsername;
    private EditText editPassword;
    private String username;
    private String password;
    private int userId;

    public String uName;
    public String fName;
    public String lName;
    public String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);

        editUsername = (EditText)findViewById(R.id.editUsernameText);
        editPassword = (EditText)findViewById(R.id.editPasswordText);

        signInButton = (Button)findViewById(R.id.sign_in_button);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // take credentials
                username = editUsername.getText().toString();
                password = editPassword.getText().toString();
                SecureUserPassword user = new SecureUserPassword(username, password);
                new HttpPostTask().execute(new SecureUserPassword(username, password));
                editPassword.setText("");

            }
        });

        signUpTextView = (TextView)findViewById(R.id.sign_up_text_view);
        signUpTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // goto sign up activity
                Intent intent = new Intent(MainActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });

    }

    private class HttpPostTask extends AsyncTask<SecureUserPassword,Void, Integer>
    {
        @Override
        protected Integer doInBackground(SecureUserPassword... params) {

            Integer integer = null;

            //CODE FOR POST
            try
            {
                final String URL = "http://10.0.2.2:8080/Login";
                RestTemplate restTemplate = new RestTemplate();
                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
                integer = restTemplate.postForObject( URL, new SecureUserPassword( params[0].getUsername(), params[0].getPassword() ), Integer.class);

            } catch (Exception e) {
                Log.e("Login Endpoint", e.getMessage(), e);
            }

            return integer;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            if (integer == -1) {
                int messageResId = R.string.failed_log_in_toast;
                Toast.makeText(MainActivity.this, messageResId, Toast.LENGTH_SHORT).show();
                userId = integer;

            } else {
                int messageResId = R.string.successful_log_in_toast;
                Toast.makeText(MainActivity.this, messageResId, Toast.LENGTH_SHORT).show();
                userId = integer;
                new HttpGetTask().execute();
            }
        }

    }

    private class HttpGetTask extends AsyncTask<Void, Void, User>{

        @Override
        protected User doInBackground(Void... params){

            try
            {
                final String URL = "http://10.0.2.2:8080/getUserData?user=" + Integer.toString(userId);
                RestTemplate restTemplate = new RestTemplate();
                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
                User user = restTemplate.getForObject( URL, User.class);
                return user;
            } catch (Exception e) {
                Log.e("Login Endpoint", e.getMessage(), e);
            }

            return null;
        }
        @Override
        protected void onPostExecute(User user) {
            email = user.getEmail();
            fName = user.getFirstname();
            lName = user.getLastname();
            uName = user.getUsername();
            Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
            intent.putExtra("KEY_EMAIL", email);
            intent.putExtra("KEY_FNAME", fName);
            intent.putExtra("KEY_LNAME", lName);
            intent.putExtra("KEY_UNAME", uName);
            intent.putExtra("KEY_ID", userId);
            startActivity(intent);

        }
    }
}


//CODE FOR PUT (NOTE: Delete is the same thing, but the method is called delete)
//            try
//            {
//                final String URL = "http://10.0.2.2:8080/put?id=" + params[0].getId();
//                RestTemplate restTemplate = new RestTemplate();
//                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
//                restTemplate.put(URL, new Greeting( params[0].getId(), params[0].getContent() ));
//
//                integer = 200; //put won't return a value so have to just say 200 for OK
//
//
//            } catch (Exception e) {
//                Log.e("PostActivity", e.getMessage(), e);
//                integer = 404; //something went wrong
//            }