package jpf5321.cs.psu.edu.medicalapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.style.UpdateAppearance;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;


public class RecordActivity extends AppCompatActivity {

    private int userId;
    private int recordId;
    private String fName;
    private String lName;
    private String birthDate;
    private String[] allergies;
    private String[] medications;
    private String[] surgeries;
    private String lastVisit;

    private Button showRecordButton;
    private Button createRecordButton;
    private Button updateRecordButton;
    private Button deleteRecordButton;
    private TextView recordTextView;

    public static final int CREATE_RECORD_REQUEST_CODE=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);

        Intent intent = getIntent();
        Bundle bd = intent.getExtras();
        if(bd != null)
        {
            userId = (int) bd.get("KEY_ID");
            fName = (String) bd.get("KEY_FNAME");
            lName = (String) bd.get("KEY_LNAME");
            recordId = userId;
        }

        showRecordButton = findViewById(R.id.show_record_button);
        showRecordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new HttpGetTask().execute();
            }
        });

        createRecordButton = findViewById(R.id.create_record_button);
        createRecordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(RecordActivity.this, CreateRecordActivity.class);
                intent.putExtra("KEY_ID", userId);
                startActivityForResult(intent, CREATE_RECORD_REQUEST_CODE);
            }
        });

        updateRecordButton = findViewById(R.id.update_record_button);
        updateRecordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RecordActivity.this, UpdaterecordActivity.class);
                intent.putExtra("KEY_ID", userId);
                intent.putExtra("KEY_RECORD_ID", recordId);
                startActivity(intent);
            }
        });

        deleteRecordButton = findViewById(R.id.delete_record_button);
        deleteRecordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new HttpDeleteTask().execute();
            }
        });
    }


    //Get Current Record
    private class HttpGetTask extends AsyncTask<Void, Void, SecureRecords>{

        @Override
        protected SecureRecords doInBackground(Void... params){

            try
            {
                final String URL = "http://10.0.2.2:8080/MedicalRecords?user=" + Integer.toString(recordId);
                RestTemplate restTemplate = new RestTemplate();
                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
                SecureRecords record = restTemplate.getForObject(URL, SecureRecords.class);
                return record;
            } catch (Exception e) {
                Log.e("Record Endpoint GET", e.getMessage(), e);
            }

            return null;
        }
        @Override
        protected void onPostExecute(SecureRecords record) {
            if(record != null){
                birthDate = record.getBirthDate();
                lastVisit = record.getLastVisit();
                allergies = record.getAllergies();
                medications = record.getMedications();
                surgeries = record.getSurgeries();

                recordTextView = findViewById(R.id.record_text_view);
                recordTextView.setText("Medical Record for, " + fName + lName + "\nYour last visit was on: " + lastVisit +
                        "\nBirth date: " + birthDate + "\nKnown allergies: " + Arrays.toString(allergies) + "\nMedications: " + Arrays.toString(medications) +
                        "\nCompleted surgeries: " + Arrays.toString(surgeries));
            }
            else{
                Toast.makeText(RecordActivity.this, R.string.record_not_found , Toast.LENGTH_SHORT). show();
            }
        }
    }




    //Delete Current Record
    private class HttpDeleteTask extends AsyncTask<Void, Void, Integer>{
        @Override
        protected Integer doInBackground(Void... params){
            try{
                final String URL = "http://10.0.2.2:8080/DeleteMedicalRecord?user=" + Integer.toString(recordId);
                RestTemplate restTemplate = new RestTemplate();
                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
                restTemplate.delete(URL);

            } catch (Exception e){
                Log.e("Record Endpoint DELETE", e.getMessage(), e);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            recordTextView.setText("");
            Toast.makeText(RecordActivity.this, R.string.record_deleted , Toast.LENGTH_SHORT). show();

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if (resultCode != Activity.RESULT_OK){
            return;
        }
        if(requestCode == CREATE_RECORD_REQUEST_CODE){
            if(data == null){
                return;
            }
            recordId = CreateRecordActivity.Answer(data);
        }
    }
}




