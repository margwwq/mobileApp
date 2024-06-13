package ru.mirea.mikhaylenkoma.mireaproject.ui.les5;

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

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import ru.mirea.mikhaylenkoma.mireaproject.R;

public class Camera extends Fragment {
    private static final int REQUEST_CODE_PERMISSION = 100;
    private static final int CAMERA_REQUEST = 0;
    private boolean isWork = false;
    private Uri imageUri;
    private ImageView imageView;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public static Camera newInstance(String param1, String param2) {
        Camera fragment = new Camera();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_camera, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        int  cameraPermissionStatus = ContextCompat.checkSelfPermission(view.getContext(),  android.Manifest.permission.CAMERA);
        int  storagePermissionStatus  = ContextCompat.checkSelfPermission(view.getContext(),  android.Manifest.permission.
                WRITE_EXTERNAL_STORAGE);

        if  (cameraPermissionStatus  ==  PackageManager.PERMISSION_GRANTED  &&  storagePermissionStatus
                ==  PackageManager.PERMISSION_GRANTED)  {
            isWork = true;
        }  else  {
            requestPermissions(  new  String[]  {
                    android.Manifest.permission.CAMERA,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE
            },  REQUEST_CODE_PERMISSION);
        }

        imageView = view.findViewById(R.id.imageView);

        ActivityResultCallback<ActivityResult> callback  = result -> {
            if  (result.getResultCode()  ==  Activity.RESULT_OK)  {
                Intent data = result.getData();
                imageView.setImageURI(imageUri);
            }
        };
        ActivityResultLauncher<Intent> cameraActivityResultLauncher  = registerForActivityResult(
                new  ActivityResultContracts.StartActivityForResult(),
                callback);

        imageView.setOnClickListener(v -> {
            Intent  cameraIntent = new  Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if  (isWork)  {
                try  {
                    File photoFile = createImageFile();
                    String  authorities  = getContext().getApplicationContext().getPackageName()  + ".fileprovider";
                    imageUri = FileProvider.getUriForFile(getActivity(), authorities,  photoFile);
                    cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT,  imageUri);
                    cameraActivityResultLauncher.launch(cameraIntent);
                }  catch (IOException e)  {
                    e.printStackTrace();
                }
            }
        });

    }
    private	File	createImageFile()	throws	IOException	{
        String	timeStamp	=	new	SimpleDateFormat("yyyyMMdd_HHmmss",	Locale.ENGLISH).format(new Date());
        String	imageFileName	=	"IMAGE_"	+	timeStamp	+	"_";
        File	storageDirectory	=	getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        return	File.createTempFile(imageFileName,	".jpg",	storageDirectory);
    }

    @Override
    public	void	onRequestPermissionsResult(int	requestCode, @NonNull String[]	permissions, @NonNull	int[]	grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_PERMISSION) {
            isWork = grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED;
        }
    }
}