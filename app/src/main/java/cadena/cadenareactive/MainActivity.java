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
import com.google.gson.JsonParseException;


import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import cadena.cadenareactive.broadcastreceivers.LocationSmsReceiver;
import cadena.cadenareactive.com.cadenareactive.model.Location;
import cadena.cadenareactive.com.cadenareactive.model.LocationServiceResponse;
import cadena.cadenareactive.services.ReactiveRestClient;
import cadena.cadenareactive.services.RestReceiver;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
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
                        System.out.println(" response:" + response);
                        //locationResponse = response;
                        ((TextView) findViewById(R.id.txtService)).setText("res:" + response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("error:" + error);
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
        restReceiver.asyncLocationReceiver();
    }

    private void rxJavaServiceReceive() {  //--Retrofit enabled service receiver.

        //--Here we will have a loop for multiple asynchronous service calls
        subscription = ReactiveRestClient.getInstance()
                .getLocation(27l)
                //.subscribeOn(Schedulers.io())
                .subscribeOn(Schedulers.newThread()) //--Instead of running on normal io, we want our REST calls be made on new thread.
                .observeOn(AndroidSchedulers.mainThread()) //--To consume the results in the main thread to show the results
                .subscribe(new Observer<LocationServiceResponse>() { //Observers are consumers of an Observable’s asynchronous data stream.
                    @Override
                    public void onCompleted() {
                        System.out.println("--------------------------------- On Completed");
                    }

                    @Override
                    public void onError(Throwable e) {
                        System.out.println("---------------------------------------------------");
                        e.printStackTrace();
                        System.out.println("----------- Error Message-----------:"+e.getMessage());
                    }

                    @Override
                    public void onNext(LocationServiceResponse locationServiceResponses) {
                        System.out.println("--------------------------------- Locations: " + locationServiceResponses.getDeviceLocationses().get(0).getDevice().getPhoneNumber());
                    }
                });

    }

    /**
     * Same results with Subscribers and pure RxJava(without using Retrofit)
     * Making Observables(by calling Observable.create()) in the Activity can cause memory leak
     */
    private void pureRxJavaObserv() {
        //--The name “OnSubscribe” provides us with a clue about when this code is typically executed: when an Observer is registered to receive the items emitted by this Observable through a call to Observable.subscribe().
        Observable.create(new Observable.OnSubscribe<Location>() { //--OnSubscribe() Invoked when Observable.subscribe is called.
            @Override
            public void call(Subscriber<? super Location> subscriber) {
                if (!subscriber.isUnsubscribed()) {
                    try {
                        final RestReceiver restReceiver = new RestReceiver();
                        Location location = restReceiver.syncLocationReceiver();//--load from service(this is a sync method call, thread will be blocked until fetch);
                        subscriber.onNext(location); //--Subscriber takes on new data
                        Location newerLocation = restReceiver.syncLocationReceiver();
                        subscriber.onNext(newerLocation);
                        subscriber.onCompleted(); //--No more values will be sent
                    } catch (JsonParseException | IOException e) {
                        subscriber.onError(e);
                    }

                }
            }
        }).subscribeOn(Schedulers.io()); //--The call() method will be called in a separate thread


    }

    /**
     * -Observable: source of data
     * -Observer(subscriber): listeners to the observables: Location in our case
     * -When a data is available on Observable, onNext is called on subscribers.
     */
    private void reactOnCollectedData() {
        //--JavaMag version: (All actions are taken in the main thread)
        List<String> data =
                Arrays.asList("foo", "bar", "baz");
        Random random = new Random();
        Observable<String> _observable =
                Observable.create(subscriber -> {  //--Creating a publisher
                    for (String s : data) {  //--A list of locations can be here(List<String> data is the publisher)
                        if (random.nextInt(6) == 0) {
                            subscriber.onError(
                                    new RuntimeException("Bad luck for you..."));
                        }
                        subscriber.onNext(s); //--New data is sent to the Subscriber
                    }
                    subscriber.onCompleted(); //--No more values will be sent
                });
        //--Testing it 10 times
        for (int i = 0; i < 10; i++) {
            System.out.println("=======================================");
            _observable.subscribe(
                    next -> System.out.println("Next: " + next),
                    error -> System.out.println("Whoops"),
                    () -> System.out.println("Done"));
        }
        //--_observable.retry(5) : Error recovery(5 times try)
        //--*With Observable.merge and Observable.zip We can merge the results from different streams


    }
}
//--Study:[Book: Reactive Java Programming]
/**
 *
 *



 public void subscribeToObservable(Observable<T> observable){
 observable.subscribe(new Subscriber<>() {  //--A subscriber implements Observer
    @Override
    public void onNext(T nextItem) {
    // invoked when Observable emits an item
    // usually you will consume the nextItem here
    }
 });

-Observable.from() , a static factory method that can create an Observable out of an array, an iterable, or a Future.




    -Observable.create() is the method that lets you create an Observable from scratch. For example, if you want to create an observable that emits only one string, “Hello!”
    Observable.create(new Observable.OnSubscribe<String>() {
        @Override
        public void call(Subscriber<? super String> observer) {
            observer.onNext("Hello!");   //--Emission of of string "HELLO" is done by the OBSERVER
            observer.onCompleted();
        }
    }
 );


 -“Suppose now that you want to create an observable that emits a JSON string resulting from a networking operation. If the response is successful, the observable will emit the result and terminate. Otherwise, it will raise an error.

    Observable.create(new Observable.OnSubscribe<String>() {
        @Override
        public void call(Subscriber<? super String> observer) {
            Response response = executeNextworkCall();
            if (observer.isUnsubscribed()) {
                // do not emit the item,”
                // observer is not subscribed anymore
                return;
            }
            if (response != null && response.isSuccessful()) {
            observer.onNext(convertToJson(response));
            observer.onCompleted();
            } else {
                observer.onError(new Exception("network call error"));
            }
        }
    }
    );
 */






