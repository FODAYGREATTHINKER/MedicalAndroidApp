package jpf5321.cs.psu.edu.medicalapplication;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.style.UpdateAppearance;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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

    private Boolean hasRecord = false;

    private TextView recordNameTextView;
    private TextView recordBirthTextView;
    private TextView recordLastVisitTextView;
    private TextView recordMedicatioTextView;
    private TextView recordAllergyTextView;
    private TextView recordSurgeryTextView;

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
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.medical_record, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        Intent intent;
        switch(item.getItemId()){
            case R.id.show_record_menu:
                new HttpGetTask().execute();
                return true;
            case R.id.new_record_menu:
                intent = new Intent(RecordActivity.this, CreateRecordActivity.class);
                intent.putExtra("KEY_ID", userId);
                startActivityForResult(intent, CREATE_RECORD_REQUEST_CODE);
                return true;
            case R.id.update_record_menu:
                intent = new Intent(RecordActivity.this, UpdaterecordActivity.class);
                intent.putExtra("KEY_ID", userId);
                intent.putExtra("KEY_DOB", birthDate);
                intent.putExtra("KEY_LAST_VISIT", lastVisit);
                intent.putExtra("KEY_MEDICATIONS", medications);
                intent.putExtra("KEY_ALLERGIES", allergies);
                intent.putExtra("KEY_SURGERIES", surgeries);
                intent.putExtra("KEY_RECORD_ID", recordId);
                startActivityForResult(intent, CREATE_RECORD_REQUEST_CODE);
                return true;
            case R.id.delete_record_menu:
                new HttpDeleteTask().execute();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if(hasRecord == false){
            menu.findItem(R.id.update_record_menu).setEnabled(false);
            menu.findItem(R.id.delete_record_menu).setEnabled(false);
        }
        else{
            menu.findItem(R.id.update_record_menu).setEnabled(true);
            menu.findItem(R.id.delete_record_menu).setEnabled(true);
        }

        return super.onPrepareOptionsMenu(menu);

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

                recordNameTextView = findViewById(R.id.record_name_text_view);
                recordBirthTextView = findViewById(R.id.record_birth_text_view);
                recordLastVisitTextView = findViewById(R.id.record_last_visit_text_view);
                recordMedicatioTextView = findViewById(R.id.record_medications_contents_text_view);
                recordAllergyTextView = findViewById(R.id.record_allergies_contents_text_view);
                recordSurgeryTextView = findViewById(R.id.record_surgery_contents_text_view);

                recordNameTextView.setText("Medical Record for, " + fName + " " + lName);
                recordBirthTextView.setText("\tDate of Birth: " + birthDate );
                recordLastVisitTextView.setText("\tLast Visit: " + lastVisit);
                recordMedicatioTextView.setText(Arrays.toString(medications).replace("[", "").replace("]", ""));
                recordAllergyTextView.setText(Arrays.toString(allergies).replace("[", "").replace("]", ""));
                recordSurgeryTextView.setText(Arrays.toString(surgeries).replace("[", "").replace("]", ""));

                hasRecord = true;
                invalidateOptionsMenu();
            }
            else{
                hasRecord = false;
                invalidateOptionsMenu();
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
            recordNameTextView.setText("Medical Record for, ");
            recordBirthTextView.setText("\tDate of Birth: ");
            recordLastVisitTextView.setText("\tLast Visit: ");
            recordMedicatioTextView.setText("");
            recordAllergyTextView.setText("");
            recordSurgeryTextView.setText("");
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
            new HttpGetTask().execute();
        }
    }
}




