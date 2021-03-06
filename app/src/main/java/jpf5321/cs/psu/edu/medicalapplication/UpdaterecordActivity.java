package jpf5321.cs.psu.edu.medicalapplication;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.lang.reflect.Array;
import java.util.Arrays;

public class UpdaterecordActivity extends AppCompatActivity {

    private int userId;

    private int recordId;

    private String birthDate;
    private String[] allergies;
    private String[] medications;
    private String[] surgeries;
    private String lastVisit;

    private String allergiesTemp;
    private String medicationsTemp;
    private String surgeriesTemp;

    private Button updateRecordButton;
    private EditText dob;
    private EditText lastVisitText;
    private EditText medicationText;
    private EditText allergyText;
    private EditText surgeryText;

    private static final String EXTRA_ANSWER_SHOWN = "jpf5321.cs.psu.edu.medical.answer_shown";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_updaterecord);

        Intent intent = getIntent();
        Bundle bd = intent.getExtras();
        if(bd != null) {
            userId = (int) bd.get("KEY_ID");
            recordId = (int) bd.get("KEY_RECORD_ID");
            birthDate = (String) bd.get("KEY_DOB");
            allergies= (String[]) bd.get("KEY_ALLERGIES");
            medications= (String[]) bd.get("KEY_MEDICATIONS");
            surgeries= (String[]) bd.get("KEY_SURGERIES");
            lastVisit = (String) bd.get("KEY_LAST_VISIT");
        }


        dob = findViewById(R.id.update_record_birthdate);
        lastVisitText = findViewById(R.id.update_record_lastvisit);
        medicationText = findViewById(R.id.update_record_medications);
        allergyText = findViewById(R.id.update_record_allergies);
        surgeryText = findViewById(R.id.update_record_surgeries);

        dob.setText(birthDate);
        lastVisitText.setText(lastVisit);
        medicationText.setText(Arrays.toString(medications).replace("[", "").replace("]", ""));
        allergyText.setText(Arrays.toString(allergies).replace("[", "").replace("]", ""));
        surgeryText.setText(Arrays.toString(surgeries).replace("[", "").replace("]", ""));

        updateRecordButton = findViewById(R.id.update_record_submit);
        updateRecordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                birthDate = ((EditText)findViewById(R.id.update_record_birthdate)).getText().toString();
                allergiesTemp = ((EditText)findViewById(R.id.update_record_allergies)).getText().toString();
                allergies = allergiesTemp.split(",");
                medicationsTemp = ((EditText)findViewById(R.id.update_record_medications)).getText().toString();
                medications = medicationsTemp.split(",");
                surgeriesTemp = ((EditText)findViewById(R.id.update_record_surgeries)).getText().toString();
                surgeries = surgeriesTemp.split(",");
                lastVisit = ((EditText)findViewById(R.id.update_record_lastvisit)).getText().toString();

                new HttpPutTask().execute(new SecureRecords(birthDate, allergies, medications, surgeries, lastVisit));
            }
        });

    }

    private class HttpPutTask extends AsyncTask<SecureRecords, Void, Integer>
    {
        @Override
        protected Integer doInBackground(SecureRecords... params) {

            Integer integer = null;

            //CODE FOR PUT (NOTE: Delete is the same thing, but the method is called delete)
            try
            {
                final String URL = "http://10.0.2.2:8080UpdateMedicalRecord?user=" + recordId;
                RestTemplate restTemplate = new RestTemplate();
                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
                restTemplate.put(URL, new SecureRecords( params[0].getBirthDate(), params[0].getAllergies(), params[0].getMedications(), params[0].getSurgeries(), params[0].getLastVisit() ));

                integer = 200; //put won't return a value so have to just say 200 for OK


            } catch (Exception e) {
                Log.e("PostActivity", e.getMessage(), e);
                integer = 404; //something went wrong
            }

            return integer;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            Toast.makeText(UpdaterecordActivity.this, R.string.record_updated , Toast.LENGTH_SHORT). show();
            Intent data = new Intent();
            data.putExtra(EXTRA_ANSWER_SHOWN, recordId);
            setResult(RESULT_OK, data);
            finish();
        }

    }
}
