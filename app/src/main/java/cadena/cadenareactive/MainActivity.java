package cadena.cadenareactive;

import android.Manifest;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.provider.Telephony;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;


import cadena.cadenareactive.broadcastreceivers.LocationSmsReceiver;
import cadena.cadenareactive.com.cadenareactive.model.Location;
import cadena.cadenareactive.services.ReactiveRestClient;
import cadena.cadenareactive.services.RestReceiver;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    //Volley request queue
    public static RequestQueue requestQueue;
    private Subscription subscription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Button btnFindCadena = findViewById(R.id.btnFindCadena);
        btnFindCadena.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendSmsBackground();
            }
        });

        Button btnReceiveService = findViewById(R.id.btnReceiveService);
        btnReceiveService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //receiveTestService();  //--Async REST request in separated thread
                rxJavaServiceReceive();  //--RxJava service receiver.
            }
        });

        try {
            BroadcastReceiver locationSmsReceiver = new LocationSmsReceiver();
            IntentFilter intentFilter = new IntentFilter(Telephony.Sms.Intents.SMS_RECEIVED_ACTION);
            this.registerReceiver(locationSmsReceiver, intentFilter);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //-Volley service receiver:
        String url = "http://10.0.2.2:8080/CadenaServerJAXRS/rest/location/get";
        requestQueue = Volley.newRequestQueue(this.getApplicationContext());
        StringRequest locationRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println("++++++++++++++ response:" + response);
                        //locationResponse = response;
                        ((TextView) findViewById(R.id.txtService)).setText("res:" + response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("++++++++++ error:" + error);
            }
        });

    }

    @Override
    protected void onDestroy() {
        if (subscription != null && !subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }
        super.onDestroy();
        //unregisterReceiver();
    }

    private void sendSmsBackground() {
        try {

            int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS);
            if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
                String strPhone = getString(R.string.CADENA_PHONE_NUMBER);
                String strMessage = "GPS_REQUEST";
                SmsManager smsManager = SmsManager.getDefault();
                PendingIntent sentPI;
                String SENT = "SMS_SENT";
                sentPI = PendingIntent.getBroadcast(this, 0, new Intent(SENT), 0);
                smsManager.sendTextMessage(strPhone, null, strMessage, sentPI, null);
                Toast.makeText(this, "Message sent. Waiting for the response...", Toast.LENGTH_SHORT).show();

                System.out.println(" Sms with Pending Intent sent...");
            } else {
                System.out.println(" **** permission has to be asked. asking for permission *** ");
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.SEND_SMS},
                        1);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void receiveTestService() {
        RestReceiver restReceiver = new RestReceiver();
        restReceiver.locationReceiver();
    }

    private void rxJavaServiceReceive() {
        subscription = ReactiveRestClient.getInstance()
                .getLocation()
                //.subscribeOn(Schedulers.io())
                .subscribeOn(Schedulers.newThread()) //--Instead of running on normal io, we want our REST calls be made on new thread.
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Location>() {
                    @Override
                    public void onCompleted() {
                        System.out.println("---------------------------------On Completed");
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        System.out.println("---------------------------------On Error");
                    }

                    @Override
                    public void onNext(Location location) { //--When REST call receives data.
                        System.out.println("---------------------------------location: " + location.getgMapGps());

                    }
                });
    }
}



