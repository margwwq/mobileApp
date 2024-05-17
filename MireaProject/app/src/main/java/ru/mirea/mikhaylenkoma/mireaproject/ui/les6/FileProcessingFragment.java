package ru.mirea.mikhaylenkoma.mireaproject.ui.les6;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Base64;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import ru.mirea.mikhaylenkoma.mireaproject.databinding.FragmentFileProcessingBinding;

public class FileProcessingFragment extends Fragment {
    private final String TAG = this.getClass().getSimpleName();
    private static final int REQUEST_CODE_PERMISSION = 200;

    private FragmentFileProcessingBinding binding;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentFileProcessingBinding.inflate(inflater, container, false);

        if (!ExternalStorage.isExternalStorageReadable() || !ExternalStorage.isExternalStorageWritable()) {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE_PERMISSION);
        }

        binding.buttonCrypt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String inputText = binding.editTextCrypt.getText().toString();
                SecretKey secretKey = FileProcessingEncrypt.generateKey();
                byte[] encryptedText = FileProcessingEncrypt.encryptMsg(inputText, secretKey);
                @SuppressLint({"NewApi", "LocalSuppress"}) String stringEncryptedText = Base64.getEncoder().encodeToString(encryptedText);

                @SuppressLint({"NewApi", "LocalSuppress"}) String resultMessage = Base64.getEncoder().encodeToString(secretKey.getEncoded()) +
                        "\n" + stringEncryptedText;

                String filename = binding.editTextFilenameCrypt.getText().toString();

                if(filename.isEmpty()) {
                    AlertDialogs.showAlertErrorSave(getActivity());
                } else {
                    filename += ".txt";
                    ExternalStorage.writeFileToExternalStorage(filename, resultMessage);
                    AlertDialogs.showAlertSave(getActivity(), filename, stringEncryptedText);
                }
            }
        });

        binding.buttonDecrypt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String filename = binding.editTextFilenameDecrypt.getText().toString();
                if(filename.isEmpty()) {
                    AlertDialogs.showAlertEmptyFilenameToRead(getActivity());
                } else {
                    filename += ".txt";
                    String result = ExternalStorage.readFileFromExternalStorage(filename);

                    if(result.equals("FileIsNotExist")) {
                        AlertDialogs.showAlertFileNotExist(getActivity(), filename);
                    } else {
                        String[] parts = result.split("\n");
                        String keyString = parts[0];
                        String messageString = parts[1];

                        @SuppressLint({"NewApi", "LocalSuppress"}) byte[] keyBytes = Base64.getDecoder().decode(keyString);
                        SecretKey secretKey = new SecretKeySpec(keyBytes, "AES");
                        @SuppressLint({"NewApi", "LocalSuppress"}) byte[] messageBytes = Base64.getDecoder().decode(messageString);

                        String decodedMessage = FileProcessingEncrypt.decryptMsg(messageBytes, secretKey);

                        AlertDialogs.showAlertFileContent(getActivity(), filename, decodedMessage);
                    }
                }
            }
        });

        return binding.getRoot();
    }
}