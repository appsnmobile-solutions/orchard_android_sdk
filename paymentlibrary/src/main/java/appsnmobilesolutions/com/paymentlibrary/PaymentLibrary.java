package appsnmobilesolutions.com.paymentlibrary;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.koushikdutta.ion.Ion;

import org.apache.commons.codec.binary.Hex;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import static appsnmobilesolutions.com.paymentlibrary.APILinks.PAYMENT_PAGE_URL;
import static appsnmobilesolutions.com.paymentlibrary.APILinks.SEND_REQUEST_URL;

public class PaymentLibrary {
    public static Mac sha256_HMAC;
    public static SecretKeySpec secret_;
    public static String hash = null;

    public static String user_secret_key;
    public static String user_client_key;
    public static String user_service_id;

    public static String paymentTokenUrl;
    public static String paymentT;

    public static Context context;

    public PaymentLibrary(Context context) {
        PaymentLibrary.context = context;
    }

    public static void add_keys(String secret_key, String client_key, String service_id) {
        user_secret_key = secret_key;
        user_client_key = client_key;
        user_service_id = service_id;
    }


    public static void sendJson(final String jsonString) throws ExecutionException, InterruptedException {

        String hmac_hex = generate_signature(jsonString);

        JsonParser jsonParser = new JsonParser();
        JsonObject jObject = (JsonObject) jsonParser.parse(jsonString);

        String request_response = String.valueOf(Ion.with(context)
                .load(SEND_REQUEST_URL)
                .setLogging("payment request logs", Log.DEBUG)
                .addHeader("sdk", "SDK")
                .addHeader("Authorization", user_client_key + ":" + hmac_hex)
                .setJsonObjectBody(jObject)
                .asJsonObject()
                .get());

        try {
            JSONObject object = new JSONObject(request_response);

            if (object.getString("resp_code").equals("000")) {
               // paymentTokenUrl = sendTokenJson(object.getString("redirect_url"));
                paymentTokenUrl = object.getString("redirect_url");
                Toast.makeText(context, object.getString("resp_desc"), Toast.LENGTH_LONG).show();
            } else{
                //paymentTokenUrl = sendTokenJson("222222");
                Toast.makeText(context, object.getString("resp_desc"), Toast.LENGTH_LONG).show();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d("hex_received", hmac_hex);
        System.out.println("----------------Response Received------------------" + request_response + "--------------------------------------");
    }


    private static String generate_signature(String jsonString) {
        try {

            sha256_HMAC = Mac.getInstance("HmacSHA256");
            secret_ = new SecretKeySpec(user_secret_key.getBytes("UTF-8"), "HmacSHA256");
            sha256_HMAC.init(secret_);

            hash = new String(Hex.encodeHex(sha256_HMAC.doFinal(jsonString.getBytes("UTF-8"))));

            Log.d("newHash", hash);
            //System.out.println(hash);
        } catch (Exception e) {
            System.out.println("Error");
        }
        return hash;
    }


    private static String sendTokenJson(String passedToken) {


        paymentTokenUrl = PAYMENT_PAGE_URL + passedToken;


        System.out.println("-------------------+++++++++---------------" + paymentTokenUrl + "----------------------++++++++----------------");


        Log.d("payUrl", PAYMENT_PAGE_URL);
        Log.d("payToken", passedToken);
        //Log.d("payFinalUrl", paymentTokenUrl);

        return paymentTokenUrl;
    }

    public static String getPaymentTokenUrl() {
        return paymentTokenUrl;
    }

    public static String getUserSecretKey() {
        return user_secret_key;
    }

    public static String getUserClientKey() {
        return user_client_key;
    }

    public static String getUserServiceId() {
        return user_service_id;
    }
}
