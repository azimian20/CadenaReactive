package cadena.cadenareactive.services;

import java.util.List;

import cadena.cadenareactive.com.cadenareactive.model.LocationServiceResponse;
import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

//--REST API call: We pass this interface to retrofit(in ReactiveRestClient) and it makes the implementation:
public interface ReactiveRestService {
    //@GET("get")
    //Observable<Location> getLocation(); //--It must be Observable<List<Location>> to take effect: https://medium.com/3xplore/handling-api-calls-using-retrofit-2-and-rxjava-2-1871c891b6ae
    //--http://randomdotnext.com/retrofit-rxjava/

    @GET("get/{userId}")
    Observable<LocationServiceResponse> getLocations(@Path("userId") Long userId);
}
