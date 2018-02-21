package jpf5321.cs.psu.edu.medicalapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private Button signInButton;
    private TextView signUpTextView;
    private EditText editUsername;
    private EditText editPassword;
    private String username;
    private String password;

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
                int messageResId = R.string.log_in_toast;
                Toast.makeText(MainActivity.this, messageResId, Toast.LENGTH_SHORT).show();
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
}
