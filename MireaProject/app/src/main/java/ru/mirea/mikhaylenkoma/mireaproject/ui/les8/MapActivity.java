package ru.mirea.mikhaylenkoma.mireaproject.ui.les8;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.yandex.mapkit.MapKitFactory;
import com.yandex.mapkit.RequestPoint;
import com.yandex.mapkit.RequestPointType;
import com.yandex.mapkit.directions.DirectionsFactory;
import com.yandex.mapkit.directions.driving.DrivingOptions;
import com.yandex.mapkit.directions.driving.DrivingRoute;
import com.yandex.mapkit.directions.driving.DrivingRouter;
import com.yandex.mapkit.directions.driving.DrivingSession;
import com.yandex.mapkit.directions.driving.VehicleOptions;
import com.yandex.mapkit.geometry.Point;
import com.yandex.mapkit.map.CameraPosition;
import com.yandex.mapkit.map.MapObject;
import com.yandex.mapkit.map.MapObjectCollection;
import com.yandex.mapkit.map.MapObjectTapListener;
import com.yandex.mapkit.map.PlacemarkMapObject;
import com.yandex.mapkit.mapview.MapView;
import com.yandex.runtime.Error;
import com.yandex.runtime.image.ImageProvider;
import com.yandex.runtime.network.NetworkError;
import com.yandex.runtime.network.RemoteError;
import com.yandex.mapkit.map.IconStyle;

import java.util.ArrayList;
import java.util.List;

import ru.mirea.mikhaylenkoma.mireaproject.R;
import ru.mirea.mikhaylenkoma.mireaproject.databinding.ActivityMapBinding;

public class MapActivity extends AppCompatActivity implements DrivingSession.DrivingRouteListener {
    private static final int REQUEST_CODE_PERMISSION = 100;
    private final Point SCREEN_CENTER = new Point(55.755864, 37.617698);
    private ActivityMapBinding binding;
    private MapView mapView;
    private MapObjectCollection mapObjects;
    private DrivingRouter drivingRouter;
    private DrivingSession drivingSession;
    private boolean isWork = false;
    private final Point USER_LOC = new Point(55.801121,37.80568);
    private PlacemarkMapObject markerHome, markerPlace;
    private MapObjectTapListener homeTapListener, placeTapListener;
    private int[] colors = {0xFFFF0000, 0xFF00FF00};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        MapKitFactory.initialize(this);
        DirectionsFactory.initialize(this);

        binding = ActivityMapBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setPermissions();

        if(isWork) {
            mapView = binding.mapview;
            mapView.getMap().setRotateGesturesEnabled(false);
            mapView.getMap().move(new CameraPosition(SCREEN_CENTER, 10, 0, 0));

            drivingRouter = DirectionsFactory.getInstance().createDrivingRouter();
            mapObjects = mapView.getMap().getMapObjects().addCollection();

            addHomeMarker();

            addPlaceMarker();
        } else {
            Toast.makeText(this, "Необходимо разрешение", Toast.LENGTH_SHORT).show();
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

    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();
        MapKitFactory.getInstance().onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
        MapKitFactory.getInstance().onStop();

        if (markerHome != null && homeTapListener != null) {
            markerHome.removeTapListener(homeTapListener);
        }
        if(markerPlace != null && placeTapListener != null) {
            markerPlace.removeTapListener(placeTapListener);
        }
    }

    private void addHomeMarker() {
        markerHome = mapView.getMap().getMapObjects().addPlacemark(USER_LOC,
                ImageProvider.fromResource(this, R.drawable.geometka));
        IconStyle iconStyle = new IconStyle();
        iconStyle.setScale(0.1f);
        markerHome.setIconStyle(iconStyle);
        homeTapListener = (mapObject, point) -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(MapActivity.this)
                    .setTitle("ДОМ")
                    .setMessage("11-я парковая 36")
                    .setPositiveButton("OK", null);
            builder.show();
            return false;
        };
        markerHome.addTapListener(homeTapListener);
    }

    private void addPlaceMarker() {
        String title = (String) getIntent().getSerializableExtra("title");
        String description = (String) getIntent().getSerializableExtra("description");
        Double latitude = (Double) getIntent().getSerializableExtra("latitude");
        Double longitude = (Double) getIntent().getSerializableExtra("longitude");

        if(latitude != null && longitude != null) {
            markerPlace = mapView.getMap().getMapObjects().addPlacemark(new Point(latitude, longitude),
                    ImageProvider.fromResource(this, R.drawable.geometka));
            IconStyle iconStyle = new IconStyle();
            iconStyle.setScale(0.1f);
            markerPlace.setIconStyle(iconStyle);
            placeTapListener = (mapObject, point) -> {
                AlertDialog.Builder builder = new AlertDialog.Builder(MapActivity.this)
                        .setTitle(title)
                        .setMessage(description)
                        .setPositiveButton("OK", null)
                        .setPositiveButton("ПОСТРОИТЬ МАРШРУТ",
                                (dialog, which) -> submitRequest(new Point(latitude, longitude)));
                builder.show();
                return false;
            };
            markerPlace.addTapListener(placeTapListener);
        }
    }

    private void submitRequest(Point place) {
        DrivingOptions drivingOptions = new DrivingOptions();
        VehicleOptions vehicleOptions = new VehicleOptions();

        drivingOptions.setRoutesCount(2);

        ArrayList<RequestPoint> requestPoints = new ArrayList<>();
        requestPoints.add(new RequestPoint(USER_LOC,
                RequestPointType.WAYPOINT,
                null));
        requestPoints.add(new RequestPoint(place,
                RequestPointType.WAYPOINT,
                null));

        drivingSession = drivingRouter.requestRoutes(requestPoints, drivingOptions,
                vehicleOptions, this);
    }

    @Override
    public void onDrivingRoutes(@NonNull List<DrivingRoute> list) {
        int color;
        for (int i = 0; i < list.size(); i++) {
            color = colors[i];
            mapObjects.addPolyline(list.get(i).getGeometry()).setStrokeColor(color);
        }
    }

    @Override
    public void onDrivingRoutesError(@NonNull Error error) {
        String errorMessage = "UnknownError";
        if (error instanceof RemoteError) {
            errorMessage = "RemoteError";
        } else if(error instanceof NetworkError) {
            errorMessage = "NetworkError";
        }
        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
    }

}