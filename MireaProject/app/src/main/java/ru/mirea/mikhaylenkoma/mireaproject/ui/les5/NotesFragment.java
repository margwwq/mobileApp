package ru.mirea.mikhaylenkoma.mireaproject.ui.les5;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import ru.mirea.mikhaylenkoma.mireaproject.databinding.FragmentNotesBinding;

public class NotesFragment extends Fragment {
    private FragmentNotesBinding _binding;
    private static boolean _isWork = false;
    private Uri _imageUri;

    private List<ImageView> _allImageViews;
    private List<ActivityResultCallback<ActivityResult>> _callbacks;
    private List<ActivityResultLauncher<Intent>> _cameraLaunchers;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        _binding = FragmentNotesBinding.inflate(inflater, container, false);
        return _binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View _view, @Nullable Bundle _savedInstanceState) {
        getPermissions(_view);
        makeHandlers(_binding.getRoot());
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
                                    "Для использования камеры необходимо разрешение",
                                    Toast.LENGTH_LONG);
                            _toast.show();
                        }
                    }
                }
        );

        int _cameraPermissionStatus = ContextCompat.checkSelfPermission(_view.getContext(),
                Manifest.permission.CAMERA);

        if(_cameraPermissionStatus != PackageManager.PERMISSION_GRANTED) {
            _requestPermissionLauncher.launch(Manifest.permission.CAMERA);
        }
    }

    private void findAllImageViews(View _view, List<ImageView> _allImageViews) {
        if(_view instanceof ViewGroup) {
            ViewGroup _viewGroup = (ViewGroup) _view;
            for (int i = 0; i < _viewGroup.getChildCount(); i++) {
                findAllImageViews(_viewGroup.getChildAt(i), _allImageViews);
            }
        } else if (_view instanceof ImageView) {
            ImageView imageView = (ImageView) _view;
            _allImageViews.add(imageView);
        }
    }

    private void makeHandlers(View _root) {
        _allImageViews = new ArrayList<>();
        _callbacks = new ArrayList<>();
        _cameraLaunchers = new ArrayList<>();
        findAllImageViews(_root, _allImageViews);

        for(int i = 0; i < _allImageViews.size(); i++) {
            _callbacks.add(createCallback(_allImageViews.get(i)));
            _cameraLaunchers.add(registerCameraActivityResultLauncher(_callbacks.get(i)));
            setupImageViewClickListener(_allImageViews.get(i), _callbacks.get(i), _cameraLaunchers.get(i));
        }
    }

    private File createImageFile() throws IOException {
        String _timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.ENGLISH)
                .format(new Date());
        String _imageFileName = "IMAGE_" + _timeStamp + "_";

        File _storageDirectory = getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        return File.createTempFile(_imageFileName, ".jpg", _storageDirectory);
    }

    private ActivityResultCallback<ActivityResult> createCallback(ImageView _imageView) {
        return new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult _result) {
                if(_result.getResultCode() == Activity.RESULT_OK) {
                    Intent _data = _result.getData();
                    _imageView.setImageURI(_imageUri);
                }
            }
        };
    }
    private ActivityResultLauncher<Intent>
    registerCameraActivityResultLauncher(ActivityResultCallback<ActivityResult> _callback) {
        return registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                _callback);
    }

    private void setupImageViewClickListener(ImageView imageView,
                                             ActivityResultCallback<ActivityResult> callback,
                                             ActivityResultLauncher<Intent> _launcher) {
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent _cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if(_isWork) {
                    try {
                        File _photoFile = createImageFile();
                        String _authorities = getContext().getApplicationContext()
                                .getPackageName() + ".fileprovider";
                        _imageUri = FileProvider.getUriForFile(getActivity(), _authorities, _photoFile);
                        _cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, _imageUri);
                        _launcher.launch(_cameraIntent);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
}