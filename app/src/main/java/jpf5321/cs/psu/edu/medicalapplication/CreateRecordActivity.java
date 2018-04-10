package jpf5321.cs.psu.edu.medicalapplication;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

public class CreateRecordActivity extends AppCompatActivity {

    private int userId;

    private String birthDate;
    private String[] allergies;
    private String[] medications;
    private String[] surgeries;
    private String lastVisit;

    private String allergiesTemp;
    private String medicationsTemp;
    private String surgeriesTemp;

    private Button createRecordButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_record);

        Intent intent = getIntent();
        Bundle bd = intent.getExtras();
        if(bd != null) {
            userId = (int) bd.get("KEY_ID");
        }

        createRecordButton = findViewById(R.id.create_record_submit);
        createRecordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                birthDate = ((EditText)findViewById(R.id.create_record_birthdate)).getText().toString();
                allergiesTemp = ((EditText)findViewById(R.id.create_record_allergies)).getText().toString();
                allergies = allergiesTemp.split(",");
                medicationsTemp = ((EditText)findViewById(R.id.create_record_medications)).getText().toString();
                medications = medicationsTemp.split(",");
                surgeriesTemp = ((EditText)findViewById(R.id.create_record_surgeries)).getText().toString();
                surgeries = surgeriesTemp.split(",");
                lastVisit = ((EditText)findViewById(R.id.create_record_lastvisit)).getText().toString();

                new HttpPostTask().execute(new SecureRecords(birthDate, allergies, medications, surgeries, lastVisit));
            }
        });

    }


    private class HttpPostTask extends AsyncTask<SecureRecords, Void, Integer>
    {
        @Override
        protected Integer doInBackground(SecureRecords... params) {

            Integer integer = null;

            //CODE FOR POST
            try
            {
                final String URL = "http://10.0.2.2:8080/CreateMedicalRecord?user=" + userId;
                RestTemplate restTemplate = new RestTemplate();
                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
                integer = restTemplate.postForObject( URL, new SecureRecords(params[0].getBirthDate(), params[0].getAllergies(), params[0].getMedications(), params[0].getSurgeries(), params[0].getLastVisit() ), Integer.class);


            } catch (Exception e) {
                Log.e("PostActivity", e.getMessage(), e);
            }


            return integer;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            Toast.makeText(CreateRecordActivity.this, R.string.record_created, Toast.LENGTH_SHORT). show();
        }
    }

}
