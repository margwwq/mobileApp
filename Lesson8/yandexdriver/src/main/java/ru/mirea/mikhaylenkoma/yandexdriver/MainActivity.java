package ru.mirea.mikhaylenkoma.yandexdriver;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.PointF;
import android.os.Bundle;
import android.widget.Toast;

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
import com.yandex.mapkit.layers.ObjectEvent;
import com.yandex.mapkit.map.CameraPosition;
import com.yandex.mapkit.map.CompositeIcon;
import com.yandex.mapkit.map.IconStyle;
import com.yandex.mapkit.map.MapObject;
import com.yandex.mapkit.map.MapObjectCollection;
import com.yandex.mapkit.map.MapObjectTapListener;
import com.yandex.mapkit.map.PlacemarkMapObject;
import com.yandex.mapkit.map.RotationType;
import com.yandex.mapkit.mapview.MapView;
import com.yandex.mapkit.user_location.UserLocationLayer;
import com.yandex.mapkit.user_location.UserLocationObjectListener;
import com.yandex.mapkit.user_location.UserLocationView;
import com.yandex.runtime.Error;
import com.yandex.runtime.image.ImageProvider;
import com.yandex.runtime.network.NetworkError;
import com.yandex.runtime.network.RemoteError;

import java.util.ArrayList;
import java.util.List;

import ru.mirea.mikhaylenkoma.yandexdriver.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity implements DrivingSession.DrivingRouteListener,
        UserLocationObjectListener {
    private static final int REQUEST_CODE_PERMISSION = 100;
    private ActivityMainBinding binding;
    private final Point ROUTE_START_LOCATION = new Point(55.801121, 37.80568);
    private final Point ROUTE_END_LOCATION = new Point(45.84903, 40.122192);
    private final Point SCREEN_CENTER = new Point(
            (ROUTE_START_LOCATION.getLatitude() + ROUTE_END_LOCATION.getLatitude()) / 2,
            (ROUTE_START_LOCATION.getLongitude() + ROUTE_END_LOCATION.getLongitude()) / 2
    );
    private MapView mapView;
    private MapObjectCollection mapObjects;
    private DrivingRouter drivingRouter;
    private DrivingSession drivingSession;
    private int[] colors = {0xFFFF0000, 0xFF00FF00, 0x00FFBBBB, 0xFF0000FF};
    private boolean isWork = false;
    private UserLocationLayer userLocationLayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MapKitFactory.initialize(this);
        DirectionsFactory.initialize(this);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setPermissions();

        if(isWork) {
            mapView = binding.mapview;
            mapView.getMap().setRotateGesturesEnabled(false);
            mapView.getMap().move(new CameraPosition(SCREEN_CENTER, 6, 0, 0));

            drivingRouter = DirectionsFactory.getInstance().createDrivingRouter();
            mapObjects = mapView.getMap().getMapObjects().addCollection();

            final Point userLoc = new Point(55.801121, 37.80568);
            submitRequest(userLoc);

            PlacemarkMapObject marker = mapView.getMap().getMapObjects().addPlacemark(new
                            Point(45.84903, 40.122192),
                    ImageProvider.fromResource(this, R.drawable.geometka));
            IconStyle iconStyle = new IconStyle();
            iconStyle.setScale(0.1f);
            marker.setIconStyle(iconStyle);
            marker.addTapListener(new MapObjectTapListener() {
                @Override
                public boolean onMapObjectTap(@NonNull MapObject mapObject, @NonNull Point point) {
                    Toast.makeText(MainActivity.this, "Любимый дом", Toast.LENGTH_SHORT).show();
                    return false;
                }
            });
        } else {
            Toast.makeText(this, "Необходимо разрешение", Toast.LENGTH_SHORT).show();
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
    }

    private void submitRequest(Point start) {
        DrivingOptions drivingOptions = new DrivingOptions();
        VehicleOptions vehicleOptions = new VehicleOptions();

        drivingOptions.setRoutesCount(4);

        ArrayList<RequestPoint> requestPoints = new ArrayList<>();
        requestPoints.add(new RequestPoint(start,
                RequestPointType.WAYPOINT,
                null));
        requestPoints.add(new RequestPoint(ROUTE_END_LOCATION,
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
    public void onObjectAdded(@NonNull UserLocationView userLocationView) {
        userLocationLayer.setAnchor(
                new PointF((float) (mapView.getWidth() * 0.5),
                        (float) (mapView.getHeight() * 0.5)),
                new PointF((float) (mapView.getWidth() * 0.5),
                        (float) (mapView.getHeight() * 0.83))
        );

        userLocationView.getArrow().setIcon(ImageProvider.fromResource(this,
                android.R.drawable.arrow_up_float));

        CompositeIcon pinIcon = userLocationView.getPin().useCompositeIcon();
        pinIcon.setIcon(
                "pin",
                ImageProvider.fromResource(this, R.drawable.ic_launcher_background),
                new IconStyle().setAnchor(new PointF(0.5f, 0.5f))
                        .setRotationType(RotationType.ROTATE)
                        .setZIndex(1f)
                        .setScale(0.5f)
        );

        userLocationView.getAccuracyCircle().setFillColor(Color.BLUE & 0x99ffffff);
    }

    @Override
    public void onObjectRemoved(@NonNull UserLocationView userLocationView) {
    }

    @Override
    public void onObjectUpdated(@NonNull UserLocationView userLocationView,
                                @NonNull ObjectEvent objectEvent) {
    }

}