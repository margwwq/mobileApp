package ru.mirea.mikhaylenkoma.camera;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import java.io.File;
import java.io.IOError;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import ru.mirea.mikhaylenkoma.camera.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_CODE_PERMISSION = 100;
    private static final int CAMERA_REUEST = 0;
    private boolean _isWork = false;
    private Uri _imageUri;
    private ActivityMainBinding _binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        _binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(_binding.getRoot());

        setPermissions();

        ActivityResultCallback<ActivityResult> _callback = new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult _result) {
                if(_result.getResultCode() == Activity.RESULT_OK) {
                    Intent _data = _result.getData();
                    _binding.imageView.setImageURI(_imageUri);
                }
            }
        };
        ActivityResultLauncher<Intent> _cameraActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(), _callback
        );

        _binding.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent _cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if(_isWork) {
                    try {
                        File _photoFile = createImageFile();
                        String _authorities = getApplicationContext().getPackageName()
                                + ".fileprovider";
                        _imageUri = FileProvider.getUriForFile(MainActivity.this,
                                _authorities, _photoFile);
                        _cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, _imageUri);
                        _cameraActivityResultLauncher.launch(_cameraIntent);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void setPermissions() {
        int _cameraPermissionStatus = ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA);
        int _storagePermissionStatus = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if(_cameraPermissionStatus == PackageManager.PERMISSION_GRANTED &&
                _storagePermissionStatus == PackageManager.PERMISSION_GRANTED) {
            _isWork = true;
        } else {
            ActivityCompat.requestPermissions(this, new String[]
                            {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_CODE_PERMISSION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int _requestCode, @NonNull String[] _permissions,
                                           @NonNull int[] _grantResults) {
        super.onRequestPermissionsResult(_requestCode, _permissions, _grantResults);
        if(_requestCode == REQUEST_CODE_PERMISSION) {
            _isWork = _grantResults.length > 0
                    && _grantResults[0] == PackageManager.PERMISSION_GRANTED;
        }
    }

    private File createImageFile() throws IOException {
        String _timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.ENGLISH)
                .format(new Date());
        String _imageFileName = "IMAGE_" + _timeStamp + "_";

        File _storageDirectory = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        return File.createTempFile(_imageFileName, ".jpg", _storageDirectory);
    }
}