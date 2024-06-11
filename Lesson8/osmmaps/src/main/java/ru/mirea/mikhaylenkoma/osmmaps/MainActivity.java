package ru.mirea.mikhaylenkoma.osmmaps;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.preference.PreferenceManager;

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.ScaleBarOverlay;
import org.osmdroid.views.overlay.compass.CompassOverlay;
import org.osmdroid.views.overlay.compass.InternalCompassOrientationProvider;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import ru.mirea.mikhaylenkoma.osmmaps.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_CODE_PERMISSION = 100;
    private ActivityMainBinding binding;
    private MapView mapView;
    private boolean isWork = false;
    private MyLocationNewOverlay locationNewOverlay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Configuration.getInstance().load(getApplicationContext(),
                PreferenceManager.getDefaultSharedPreferences(getApplicationContext()));

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mapView = binding.mapView;

        mapView.setZoomRounding(true);
        mapView.setMultiTouchControls(true);

        IMapController mapController = mapView.getController();
        mapController.setZoom(15.0);
        GeoPoint startPoint = new GeoPoint(55.801121, 37.80568);
        mapController.setCenter(startPoint);

        setPermissions();

        if(isWork) {
            locationNewOverlay = new MyLocationNewOverlay(
                    new GpsMyLocationProvider(getApplicationContext()), mapView);
            locationNewOverlay.enableMyLocation();
            mapView.getOverlays().add(this.locationNewOverlay);
        } else {
            Toast.makeText(this, "Необходимо разрешение", Toast.LENGTH_SHORT).show();
        }

        CompassOverlay compassOverlay = new CompassOverlay(getApplicationContext(),
                new InternalCompassOrientationProvider(getApplicationContext()), mapView);
        compassOverlay.enableCompass();
        mapView.getOverlays().add(compassOverlay);

        final Context context = this.getApplicationContext();
        final DisplayMetrics dm = context.getResources().getDisplayMetrics();
        ScaleBarOverlay scaleBarOverlay = new ScaleBarOverlay(mapView);
        mapView.getOverlays().add(scaleBarOverlay);

        setMarker(new GeoPoint(45.84903,40.122192),
                "Дом", "Место, по которому я очень скучаю");

        setMarker(new GeoPoint(55.742372,37.629241),
                "Новокузнецкая", "Самый красивый район Москвы");

        setMarker(new GeoPoint(55.61266,37.684102),
                "Царицыно", "Самый красивый парк");

        setMarker(new GeoPoint(55.754005,37.636823),
                "Pasta", "Самая вкусная паста");
    }

    @Override
    public void onResume() {
        super.onResume();
        Configuration.getInstance().load(getApplicationContext(),
                PreferenceManager.getDefaultSharedPreferences(getApplicationContext()));
        if(mapView != null) {
            mapView.onResume();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        Configuration.getInstance().save(getApplicationContext(),
                PreferenceManager.getDefaultSharedPreferences(getApplicationContext()));
        if(mapView != null) {
            mapView.onPause();
        }
    }

    private void setPermissions() {
        int mapPermissionStatus = ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION);
        if(mapPermissionStatus == PackageManager.PERMISSION_GRANTED) {
            isWork = true;
        } else {
            ActivityCompat.requestPermissions(this, new String[]
                    {android.Manifest.permission.ACCESS_FINE_LOCATION}, 100);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == REQUEST_CODE_PERMISSION) {
            isWork = grantResults.length > 0 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED;
        }
    }

    private void setMarker(GeoPoint point, String title, String description) {
        Marker marker = new Marker(mapView);
        marker.setPosition(point);
        marker.setOnMarkerClickListener(new Marker.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker, MapView mapView) {
                Toast.makeText(getApplicationContext(), description, Toast.LENGTH_SHORT).show();
                return false;
            }
        });
        mapView.getOverlays().add(marker);
        marker.setIcon(ResourcesCompat.getDrawable(getResources(),
                org.osmdroid.library.R.drawable.osm_ic_follow_me_on, null));
        marker.setTitle(title);
    }
}