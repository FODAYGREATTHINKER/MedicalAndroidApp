package jpf5321.cs.psu.edu.medicalapplication;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;

public class ReceiptActivity extends AppCompatActivity {
    private int userId;
    private String reason;
    private int amount;
    private int num;
    private TextView reasonTextView;
    private TextView amountTextView;
    private Button returnHome;
    private Button anotherPayment;
    private String email;
    private String fName;
    private String lName;
    private String uName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receipt);
        Intent intent = getIntent();
        Bundle bd = intent.getExtras();
        if (bd != null) {
            userId = (int) bd.get("KEY_ID");
            reason = (String) bd.get("FOR");
            amount = (int) bd.get("AMOUNT");
            email = (String) bd.get("KEY_EMAIL");
            fName = (String) bd.get("KEY_FNAME");
            lName = (String) bd.get("KEY_LNAME");
            uName = (String) bd.get("KEY_UNAME");
        }

        //new HttpGetTask().execute();
        reasonTextView = findViewById(R.id.reason_text_view);
        amountTextView = findViewById(R.id.amount_paid_text_view);

        reasonTextView.setText("For: " + reason);
        amountTextView.setText("Amount Of: $" + amount );

        returnHome = findViewById(R.id.return_to_home);
        anotherPayment = findViewById(R.id.make_another_payment);

        returnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ReceiptActivity.this, ProfileActivity.class);
                intent.putExtra("KEY_EMAIL", email);
                intent.putExtra("KEY_FNAME", fName);
                intent.putExtra("KEY_LNAME", lName);
                intent.putExtra("KEY_UNAME", uName);
                intent.putExtra("KEY_ID", userId);
                startActivity(intent);
            }
        });

        anotherPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ReceiptActivity.this, PaymentActivity.class);
                intent.putExtra("KEY_EMAIL", email);
                intent.putExtra("KEY_FNAME", fName);
                intent.putExtra("KEY_LNAME", lName);
                intent.putExtra("KEY_UNAME", uName);
                intent.putExtra("KEY_ID", userId);
                startActivity(intent);
            }
        });


    }

    private class HttpGetTask extends AsyncTask<Void, Void, SecurePayments> {

        @Override
        protected SecurePayments doInBackground(Void... params){

            try
            {
                final String URL = "http://10.0.2.2:8080/GetPayments?user=" + Integer.toString(userId);
                RestTemplate restTemplate = new RestTemplate();
                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
                ArrayList<SecurePayments> paymentList = restTemplate.getForObject(URL, ArrayList.class);
                SecurePayments payment = new SecurePayments(paymentList.get(paymentList.size() - 1).getCreditCardHolder(),paymentList.get(paymentList.size() - 1).getCreditCardNumber(), paymentList.get(paymentList.size() - 1).getExpirationDate(), paymentList.get(paymentList.size() - 1).getCvv(), paymentList.get(paymentList.size() - 1).getReason(), paymentList.get(paymentList.size() - 1).getAmount(), paymentList.get(paymentList.size() - 1).getUserID()  );
                return payment;

            } catch (Exception e) {
                Log.e("Record Endpoint GET", e.getMessage(), e);
            }

            return null;
        }

        @Override
        protected void onPostExecute(SecurePayments payment) {

            reason = payment.getReason();
            amount = payment.getAmount();

            reasonTextView = findViewById(R.id.reason_text_view);
            amountTextView = findViewById(R.id.amount_paid_text_view);

            reasonTextView.setText("For: " + reason);
            amountTextView.setText("Amount Of: " + amount );
        }
    }
}
