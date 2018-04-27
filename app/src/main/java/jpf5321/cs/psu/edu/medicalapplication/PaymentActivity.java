package jpf5321.cs.psu.edu.medicalapplication;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

public class PaymentActivity extends AppCompatActivity{

    private String cardNumber;
    private String expirationDate;
    private String cardHolder;
    private String cvv;
    private String reason;
    private int amount;
    private int userId;
    private Button submitButton;
    private String email;
    private String fName;
    private String lName;
    private String uName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        Intent intent = getIntent();
        Bundle bd = intent.getExtras();
        if(bd != null)
        {
            userId = (int) bd.get("KEY_ID");
            email = (String) bd.get("KEY_EMAIL");
            fName = (String) bd.get("KEY_FNAME");
            lName = (String) bd.get("KEY_LNAME");
            uName = (String) bd.get("KEY_UNAME");
        }

        submitButton = findViewById(R.id.submit_payment_button);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cardHolder = ((EditText)findViewById(R.id.card_holder_name)).getText().toString();
                cardNumber = ((EditText)findViewById(R.id.card_number)).getText().toString();
                expirationDate = ((EditText)findViewById(R.id.card_expiration_date)).getText().toString();
                cvv = ((EditText)findViewById(R.id.card_cvv)).getText().toString();
                reason = ((EditText)findViewById(R.id.payment_description)).getText().toString();
                amount = Integer.parseInt(((EditText)findViewById(R.id.payment_amount)).getText().toString());

                new HttpPostTask().execute(new SecurePayments(cardHolder, cardNumber, expirationDate, cvv, reason, amount, userId));
            }
        });
    }

    private class HttpPostTask extends AsyncTask<SecurePayments, Void, Integer>
    {
        @Override
        protected Integer doInBackground(SecurePayments... params) {

            Integer integer = null;

            try
            {
                final String URL = "http://10.0.2.2:8080/CreatePayment?user="+Integer.toString(userId);
                RestTemplate restTemplate = new RestTemplate();
                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
                integer = restTemplate.postForObject( URL, params[0], Integer.class);

            } catch (Exception e) {
                Log.e("PostActivity", e.getMessage(), e);
            }
            return integer;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            Toast.makeText(PaymentActivity.this, R.string.payment_created, Toast.LENGTH_SHORT). show();
            Intent receiptIntent = new Intent(PaymentActivity.this, ReceiptActivity.class);
            receiptIntent.putExtra("KEY_ID", userId);
            receiptIntent.putExtra("AMOUNT", amount);
            receiptIntent.putExtra("FOR", reason);
            receiptIntent.putExtra("KEY_EMAIL", email);
            receiptIntent.putExtra("KEY_FNAME", fName);
            receiptIntent.putExtra("KEY_UNAME", uName);
            receiptIntent.putExtra("KEY_LNAME", lName);
            startActivity(receiptIntent);
        }
    }
}
