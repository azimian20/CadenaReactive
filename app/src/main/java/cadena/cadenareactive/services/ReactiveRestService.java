package cadena.cadenareactive.services;

import cadena.cadenareactive.com.cadenareactive.model.Location;
import retrofit2.http.GET;
import rx.Observable;

//--REST API call: We pass this interface to retrofit(in ReactiveRestClient) and it makes the implementation:
public interface ReactiveRestService {
    @GET("get")
    Observable<Location> getLocation();
}
