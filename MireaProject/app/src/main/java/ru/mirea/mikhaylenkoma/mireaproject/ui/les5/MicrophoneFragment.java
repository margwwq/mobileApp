package ru.mirea.mikhaylenkoma.mireaproject.ui.les5;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import ru.mirea.mikhaylenkoma.mireaproject.R;
import ru.mirea.mikhaylenkoma.mireaproject.databinding.FragmentMicrophoneBinding;

public class MicrophoneFragment extends Fragment {
    private final String TAG = this.getClass().getSimpleName();
    private FragmentMicrophoneBinding _binding;
    private boolean _isWork = false;
    private String _fileName = null;
    private Button _recordButton = null;
    private Button _playButton = null;
    private MediaRecorder _recorder = null;
    private MediaPlayer _player = null;
    private boolean _isStartRecording = true;
    private boolean _isStartPlaying = true;
    private static int _seconds = 0, _minutes = 0, _hours = 0;
    private Timer _timer = null;
    private TimerTask _task = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        _binding = FragmentMicrophoneBinding.inflate(inflater, container, false);
        return _binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View _view, @Nullable Bundle _savedInstanceState) {

        _recordButton = _binding.buttonMicrophoneRecord;
        _playButton = _binding.buttonMicrophonePlay;
        _playButton.setEnabled(false);
        _fileName = (new File(getContext().getExternalFilesDir(Environment.DIRECTORY_MUSIC),
                "/audiorecordtest.3gp")).getAbsolutePath();

        getPermissions(_view);

        _recordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(_isStartRecording) {
                    _binding.textViewDuration.setText(String.format(Locale.getDefault(),
                            "ДЛИТЕЛЬНОСТЬ\n0:00:00"));
                    _recordButton.setText("СТОП ЗАПИСЬ");
                    _playButton.setEnabled(false);
                    startRecording();
                    startTimer();
                } else {
                    _recordButton.setText("СТАРТ ЗАПИСЬ");
                    _playButton.setEnabled(true);
                    stopRecording();
                    _timer.cancel();
                    _seconds = 0;
                }
                _isStartRecording = !_isStartRecording;
            }
        });

        _playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(_isStartPlaying) {
                    _playButton.setText("СТОП ПРОСЛУШИВАНИЕ");
                    _recordButton.setEnabled(false);
                    startPlaying();
                } else {
                    _playButton.setText("СТАРТ ПРОСЛУШИВАНИЕ");
                    _recordButton.setEnabled(true);
                    stopPlaying();
                }
                _isStartPlaying = !_isStartPlaying;
            }
        });
    }

    private void getPermissions(@NonNull View _view) {
        ActivityResultLauncher<String> _requestPermissionLauncher = registerForActivityResult(
                new ActivityResultContracts.RequestPermission(),
                new ActivityResultCallback<Boolean>() {
                    @Override
                    public void onActivityResult(Boolean _result) {
                        if(_result) {
                            _isWork = true;
                        } else {
                            Toast _toast = Toast.makeText(getContext(),
                                    "Для использования микрофона необходимо разрешение",
                                    Toast.LENGTH_LONG);
                            _toast.show();
                            _recordButton.setEnabled(false);
                        }
                    }
                }
        );

        int _microphonePermissionStatus = ContextCompat.checkSelfPermission(_view.getContext(),
                Manifest.permission.RECORD_AUDIO);

        if(_microphonePermissionStatus != PackageManager.PERMISSION_GRANTED) {
            _requestPermissionLauncher.launch(Manifest.permission.RECORD_AUDIO);
        }
    }

    private void startRecording() {
        _recorder = new MediaRecorder();
        _recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        _recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        _recorder.setOutputFile(_fileName);
        _recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        try {
            _recorder.prepare();
        } catch (IOException e) {
            Log.e(TAG, "prepare() failed");
        }
        _recorder.start();
    }

    private void stopRecording() {
        _recorder.stop();
        _recorder.release();
        _recorder = null;
    }

    private void startPlaying() {
        _player = new MediaPlayer();
        try {
            _player.setDataSource(_fileName);
            _player.prepare();
            _player.start();
        } catch (IOException e) {
            Log.e(TAG, "prepare() failed");
        }
    }

    private void stopPlaying() {
        _player.release();
        _player = null;
    }

    private void startTimer() {
        _timer = new Timer();
        _task = new TimerTask() {
            @Override
            public void run() {
                _seconds++;
                String _minutesString = "";
                String _secondsString = "";
                _hours = _seconds / 3600;
                _minutes = (_seconds % 3600) / 60;
                _seconds %= 60;
                if(_minutes < 10) _minutesString = "0" + Integer.toString(_minutes);
                if(_seconds < 10) _secondsString = "0" + Integer.toString(_seconds);
                _binding.textViewDuration.setText(String.format(Locale.getDefault(),
                        "ДЛИТЕЛЬНОСТЬ\n%d:%s:%s", _hours, _minutesString, _secondsString));
            }
        };
        _timer.scheduleAtFixedRate(_task, 1000, 1000);
    }
}