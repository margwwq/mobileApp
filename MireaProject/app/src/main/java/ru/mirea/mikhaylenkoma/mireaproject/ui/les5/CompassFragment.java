package ru.mirea.mikhaylenkoma.mireaproject.ui.les5;

import static androidx.core.content.ContextCompat.getSystemService;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Locale;

import ru.mirea.mikhaylenkoma.mireaproject.databinding.FragmentCompassBinding;

public class CompassFragment extends Fragment implements SensorEventListener {
    private final String TAG = this.getClass().getSimpleName();
    private FragmentCompassBinding _binding;
    private Sensor _compassSensor;
    private float[] _rotationMatrix = new float[9];
    private float[] _orientation = new float[3];
    private float _degrees = 0;
    private String _sideOfTheWorld = "";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        _binding = FragmentCompassBinding.inflate(inflater, container, false);
        return _binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View _view, @Nullable Bundle _savedInstanceState) {
        SensorManager _sensorManager = (SensorManager)
                getContext().getSystemService(Context.SENSOR_SERVICE);
        _compassSensor = _sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
        _sensorManager.registerListener(this, _compassSensor,
                SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    public void onSensorChanged(SensorEvent _event) {
        if(_event.sensor.getType() == Sensor.TYPE_ROTATION_VECTOR) {
            SensorManager.getRotationMatrixFromVector(_rotationMatrix, _event.values);
            SensorManager.getOrientation(_rotationMatrix, _orientation);
            _degrees = (float) Math.toDegrees(_orientation[0]);
            if(_degrees < 0) _degrees += 360;

            setText();

            _binding.compassImage.setRotation(-_degrees);
            _binding.compassImage.invalidate();
        }
    }

    @Override
    public void onAccuracyChanged(Sensor _sensor, int _accuracy) {

    }

    private void setText() {
        if (_degrees <= 22.5 || _degrees >= 337.5) _sideOfTheWorld = "N";
        else if (_degrees > 22.5 && _degrees <= 67.5) _sideOfTheWorld = "NE";
        else if (_degrees > 67.5 && _degrees <= 112.5) _sideOfTheWorld = "E";
        else if (_degrees > 112.5 && _degrees <= 157.5) _sideOfTheWorld = "SE";
        else if (_degrees > 157.5 && _degrees <= 202.5) _sideOfTheWorld = "S";
        else if (_degrees > 202.5 && _degrees <= 247.5) _sideOfTheWorld = "SW";
        else if (_degrees > 247.5 && _degrees <= 292.5) _sideOfTheWorld = "W";
        else _sideOfTheWorld = "NW";


        _binding.textViewShowInfoNorth.setText(String.format(Locale.getDefault(),
                "%s%d°", _sideOfTheWorld, (int)_degrees));
    }
}