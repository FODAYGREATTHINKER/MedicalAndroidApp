package jpf5321.cs.psu.edu.medicalapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class SignUpActivity extends AppCompatActivity {
    private Button signUpButton;
    private EditText editUsername;
    private EditText editPassword;
    private EditText editConfirmPassword;
    private TextView returnToLoginTextView;
    private String username;
    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        editUsername = (EditText)findViewById(R.id.editUsernameText);
        editPassword = (EditText)findViewById(R.id.editPasswordText);
        editConfirmPassword = (EditText)findViewById(R.id.editConfirmPasswordText);

        signUpButton = (Button)findViewById(R.id.sign_up_button);
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // create user
                username = editUsername.getText().toString();
                password = editPassword.getText().toString();
                User user = new User(username, password);
                int messageResId = R.string.sign_up_toast;
                Toast.makeText(SignUpActivity.this, messageResId, Toast.LENGTH_SHORT).show();
            }
        });

        returnToLoginTextView = (TextView)findViewById(R.id.return_to_login_text_view);
        returnToLoginTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
}
