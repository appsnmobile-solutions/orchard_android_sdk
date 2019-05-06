package appsnmobilesolutions.com.paymentintegrationandroid;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.google.gson.JsonObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutionException;

import appsnmobilesolutions.com.paymentlibrary.PaymentLibrary;


public class MainActivity extends AppCompatActivity {


    WebView webView;
    EditText editAmount, edtReference,edtExtrid,edtCallback_url,edtNickname;
    Button btnSend;
    LinearLayout linearLayout;

    PaymentLibrary paymentLibrary;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        editAmount = findViewById(R.id.edtAmount);
        edtReference = findViewById(R.id.edtReference);
        edtExtrid = findViewById(R.id.edtExtrid);
        edtCallback_url = findViewById(R.id.edtCallback_url);
        edtNickname = findViewById(R.id.edtNickname);
        btnSend = findViewById(R.id.btnSend);

        webView = findViewById(R.id.webView);

        linearLayout = findViewById(R.id.linear);

        paymentLibrary = new PaymentLibrary(MainActivity.this);

        paymentLibrary.add_keys("FjgFTnx6sxIcbA7/emngbs6hePenxlJzZNmbGjKm7T/KRlI+8SB+BPHG3E6yQ7QMt7+Oii0wczueNQJsGevquQ=="
                , "Vzf0beMIKVf5EeU/cVneDOo+Gx3gLHUFsMfHASXd2vUYgbU+k2e2tCj+PSjJPcU8f4dcKAnbPr3/ughyzr70Qw=="
                , "8"
        );
//        paymentLibrary.sendJson("Hellworld");


        // paymentLibrary.sendTokenJson();
        // Log.d("Display", paymentLibrary.getPaymentTokenUrl());

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    sendReq(editAmount.getText().toString().trim(), edtReference.getText().toString().trim(),edtExtrid.getText().toString().trim(),edtCallback_url.getText().toString().trim(),edtNickname.getText().toString().trim());
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void sendReq(String amount, String ref, String exttrid,String callback, String nickname) throws ExecutionException, InterruptedException {
        String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());

        JsonObject object = new JsonObject();
        object.addProperty("service_id", PaymentLibrary.getUserServiceId());
        object.addProperty("reference", ref);
        object.addProperty("amount", amount);
        object.addProperty("exttrid", exttrid);
        object.addProperty("callback_url", callback);
        object.addProperty("trans_type", "DR");
        object.addProperty("ts", timeStamp);
        object.addProperty("nickname", nickname);

        Log.d("Json Objects", String.valueOf(object));


        PaymentLibrary.sendJson(String.valueOf(object));


        doIt();
    }

    private void doIt() {

        webView.setVisibility(View.VISIBLE);
        webView.setWebViewClient(new WebViewClient());

        System.out.println("REDIRECT URL ----------------------------------" + PaymentLibrary.getPaymentTokenUrl() + "--------------------------------------");

        System.out.println("FINAL_URL"+PaymentLibrary.getPaymentTokenUrl());
        webView.loadUrl(PaymentLibrary.getPaymentTokenUrl());
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
    }


    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.canGoBack();
        } else {
            super.onBackPressed();
        }
    }
}
