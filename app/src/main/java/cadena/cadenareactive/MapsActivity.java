package cadena.cadenareactive;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private String rawCadenaLocation = ""; //format: "5957.2109N,1043.8246E";
    private Double GglLat;
    private Double GglLng;
    private boolean locationDetermined = false;
    private String errorMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        Intent mapsIntent = getIntent();
        rawCadenaLocation = mapsIntent.getStringExtra(this.getString(R.string.CADENA_LOCATION_SMS_CONTAINER));
        translateLocationMessage();
    }


    /*
    * To convert standard DDMM.mmmm location format from GPS,
    * we have to concatenate DD with MM.mmmm/60 to convert into
    * Google map api compatible format
    */


    private void translateLocationMessage() {


        rawCadenaLocation = rawCadenaLocation.trim();
        if (!rawCadenaLocation.matches("\\d{4}\\.\\d{4}[NS],\\d{4}\\.\\d{4}[EW]")) {
            locationDetermined = false;
            errorMessage = "Error: Location format is not correct";
        }
        String raw = rawCadenaLocation;
        String[] latLong = raw.split(",");
        String lat = latLong[0];
        String lon = latLong[1];
        boolean northern = lat.substring(lat.length() - 1, lat.length()).equals("N");
        boolean eastern = lon.substring(lon.length() - 1, lon.length()).equals("E");
        lat = lat.substring(0, lat.length() - 1); // 5957.2109
        lon = lon.substring(0, lon.length() - 1);
        String latDeg = lat.substring(0, 2); // 59
        String lonDeg = lon.substring(0, 2);
        String latMin = lat.substring(2); // 57.2109
        String lonMin = lon.substring(2);
        GglLat = (Integer.parseInt(latDeg) + Double.parseDouble(latMin) / 60) * (northern ? 1 : -1);
        GglLng = (Integer.parseInt(lonDeg) + Double.parseDouble(lonMin) / 60) * (eastern ? 1 : -1);
        System.out.println(" *** Translated location format: " + GglLat + "," + GglLng);
        locationDetermined = true;

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (locationDetermined) {
            LatLng CadenaLocation = new LatLng(GglLat, GglLng);
            mMap.addMarker(new MarkerOptions().position(CadenaLocation).title("Location of Cadena"));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(CadenaLocation));

            final Button btnMyLocation = (Button) findViewById(R.id.btnMyLocation);
            btnMyLocation.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    LatLng Oslo = new LatLng(GglLat, GglLng);
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(Oslo));
                    mMap.animateCamera(CameraUpdateFactory.zoomTo(14.0f));
                }
            });
        } else {
            System.out.println("\n\n" + errorMessage);
        }

        final Button btnZoomIn = (Button) findViewById(R.id.btnZoomIn);
        btnZoomIn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mMap.animateCamera(CameraUpdateFactory.zoomIn());
            }
        });

        final Button btnZoomOut = (Button) findViewById(R.id.btnZoomOut);
        btnZoomOut.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mMap.animateCamera(CameraUpdateFactory.zoomOut());
            }
        });
    }
}
