package jpf5321.cs.psu.edu.medicalapplication;

import android.content.ClipData;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class ProfileActivity extends AppCompatActivity {

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;
    private NavigationView navigationView;
    private String uName;
    private String fName;
    private String lName;
    private String email;
    private int userId;

    private TextView welcomeTextView;
    private ImageButton recordButton;
    private ImageButton chatButton;
    private ImageButton payButton;
    private ImageButton settingsButton;
    private ImageButton logoutButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Intent intent = getIntent();
        Bundle bd = intent.getExtras();
        if(bd != null)
        {
            email = (String) bd.get("KEY_EMAIL");
            fName = (String) bd.get("KEY_FNAME");
            lName = (String) bd.get("KEY_LNAME");
            uName = (String) bd.get("KEY_UNAME");
            userId = (int) bd.get("KEY_ID");
        }

        welcomeTextView = findViewById(R.id.welcome_text_view);
        welcomeTextView.setText("Welcome Back, " + uName + ". We hope you're still feeling well since your last visit!");

        recordButton = findViewById(R.id.record_button);
        chatButton = findViewById(R.id.chat_button);
        payButton = findViewById(R.id.pay_button);
        settingsButton = findViewById(R.id.settings_button);
        logoutButton = findViewById(R.id.logout_button);

        recordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent recordIntent = new Intent(ProfileActivity.this, RecordActivity.class);
                recordIntent.putExtra("KEY_ID", userId);
                recordIntent.putExtra("KEY_FNAME", fName);
                recordIntent.putExtra("KEY_LNAME", lName);
                startActivity(recordIntent);
            }
        });
        chatButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent chatIntent = new Intent(ProfileActivity.this, ChatActivity.class);
                        chatIntent.putExtra("KEY_ID", userId);
                        chatIntent.putExtra("KEY_FNAME", fName);
                        chatIntent.putExtra("KEY_LNAME", lName);
                        startActivity(chatIntent);
                    }
                });
        payButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent fitIntent = new Intent(ProfileActivity.this, GoogleFitActivity.class);
                        startActivity(fitIntent);
                    }
                });
        settingsButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(ProfileActivity.this, "Not Yet Implemented", Toast.LENGTH_SHORT). show();
                    }
                });
        logoutButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                });

        mDrawerLayout = findViewById(R.id.drawer_layout);
        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.menu_open, R.string.menu_close);

        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        navigationView = findViewById(R.id.profile_nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.nav_records:
                            Intent recordIntent = new Intent(ProfileActivity.this, RecordActivity.class);
                            recordIntent.putExtra("KEY_ID", userId);
                            recordIntent.putExtra("KEY_FNAME", fName);
                            recordIntent.putExtra("KEY_LNAME", lName);
                            startActivity(recordIntent);
                        break;

                    case R.id.nav_chat:
                            Intent chatIntent = new Intent(ProfileActivity.this, ChatActivity.class);
                            chatIntent.putExtra("KEY_ID", userId);
                            chatIntent.putExtra("KEY_FNAME", fName);
                            chatIntent.putExtra("KEY_LNAME", lName);
                            startActivity(chatIntent);
                        break;

                    case R.id.nav_settings:

                        break;

                    case R.id.nav_logout:
                            finish();
                        break;

                }
                return true;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){

        if(mToggle.onOptionsItemSelected(item)){
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
