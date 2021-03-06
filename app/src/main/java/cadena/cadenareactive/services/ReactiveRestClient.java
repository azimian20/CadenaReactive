package cadena.cadenareactive.services;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import cadena.cadenareactive.R;
import cadena.cadenareactive.com.cadenareactive.model.LocationServiceResponse;
import cadena.cadenareactive.util.DateDeserializer;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;

public class ReactiveRestClient {

    private static ReactiveRestClient instance;
    private ReactiveRestService reactiveRestService;

    private ReactiveRestClient(String baseUrl) {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.HEADERS);
        final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .addInterceptor(interceptor)
                .build();

        //--Custom Date serializer
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Date.class, new DateDeserializer());
        Gson gson = gsonBuilder.create();

        final Retrofit retrofit = new Retrofit.Builder().client(okHttpClient).baseUrl(baseUrl)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))  //--From Retrofit 2.0.0 and later the default converter is ResponseBody. We need this line to convert to String(or any other type)
                .build();

        reactiveRestService = retrofit.create(ReactiveRestService.class);
    }

    public static ReactiveRestClient getInstance(String baseUrl) {
        if (instance == null) {
            instance = new ReactiveRestClient(baseUrl);
        }
        return instance;
    }

    //--This method can accept parameters for Location object:
    public Observable<LocationServiceResponse> getLocation(Long userId) {
        return reactiveRestService.getLocations(userId);
    }
}