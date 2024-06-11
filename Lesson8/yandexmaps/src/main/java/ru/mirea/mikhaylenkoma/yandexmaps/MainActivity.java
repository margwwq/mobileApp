package ru.mirea.mikhaylenkoma.yandexmaps;


import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.PointF;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.yandex.mapkit.Animation;
import com.yandex.mapkit.MapKit;
import com.yandex.mapkit.MapKitFactory;
import com.yandex.mapkit.geometry.Point;
import com.yandex.mapkit.layers.ObjectEvent;
import com.yandex.mapkit.map.CameraPosition;
import com.yandex.mapkit.map.CompositeIcon;
import com.yandex.mapkit.map.IconStyle;
import com.yandex.mapkit.map.RotationType;
import com.yandex.mapkit.mapview.MapView;
import com.yandex.mapkit.user_location.UserLocationLayer;
import com.yandex.mapkit.user_location.UserLocationObjectListener;
import com.yandex.mapkit.user_location.UserLocationView;
import com.yandex.runtime.image.ImageProvider;

import ru.mirea.mikhaylenkoma.yandexmaps.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity implements UserLocationObjectListener {
    private static final int REQUEST_CODE_PERMISSION = 100;
    private ActivityMainBinding binding;
    private MapView mapView;
    private boolean isWork = false;
    private UserLocationLayer userLocationLayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setPermissions();

        if(isWork) {
            MapKitFactory.initialize(this);
            mapView = binding.mapview;
            mapView.getMap().move(
                    new CameraPosition(new Point(55.751574, 37.573856), 11.0f,
                            0.0f, 0.0f),
                    new Animation(Animation.Type.SMOOTH, 0),
                    null
            );
            loadUserLocationLayer();
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

    private void loadUserLocationLayer() {
        MapKit mapkit = MapKitFactory.getInstance();
        mapkit.resetLocationManagerToDefault();
        userLocationLayer = mapkit.createUserLocationLayer(mapView.getMapWindow());
        userLocationLayer.setVisible(true);
        userLocationLayer.setHeadingEnabled(true);
        userLocationLayer.setObjectListener(this);
    }
}