package cadena.cadenareactive.services;

import cadena.cadenareactive.com.cadenareactive.model.Location;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;

public class ReactiveRestClient {

    private static final String LOCATION_BASE_URL = "http://10.0.2.2:8080/CadenaServerJAXRS/rest/location/";

    private static ReactiveRestClient instance;
    private ReactiveRestService reactiveRestService;

    private ReactiveRestClient() {
        final Retrofit retrofit = new Retrofit.Builder().baseUrl(LOCATION_BASE_URL)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())  //--From Retrofit 2.0.0 and later the default converter is ResponseBody. We need this line to convert to String
                .build();
        reactiveRestService = retrofit.create(ReactiveRestService.class);
    }

    public static ReactiveRestClient getInstance() {
        if (instance == null) {
            instance = new ReactiveRestClient();
        }
        return instance;
    }

    //--This method can accept parameters:
    public Observable<Location> getLocation() {
        return reactiveRestService.getLocation();
    }
}