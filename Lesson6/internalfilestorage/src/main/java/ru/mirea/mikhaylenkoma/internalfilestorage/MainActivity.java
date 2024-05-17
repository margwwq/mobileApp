package ru.mirea.mikhaylenkoma.internalfilestorage;

import android.content.Context;
import android.os.Bundle;
import android.os.ConditionVariable;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import ru.mirea.mikhaylenkoma.internalfilestorage.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private static final String TAG = MainActivity.class.getSimpleName();
    private String fileName = "memorable_date.txt";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setTextFromFile();
        binding.buttonWriteText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String memorableDateText = binding.editTextMemorableDate.getText().toString();
                FileOutputStream outputStream;

                try {
                    outputStream = openFileOutput(fileName, Context.MODE_PRIVATE);
                    outputStream.write(memorableDateText.getBytes());
                    outputStream.close();
                }
                catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
    }

    private void setTextFromFile() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(5000);
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
                binding.editTextMemorableDate.post(new Runnable() {
                    @Override
                    public void run() {
                        binding.editTextMemorableDate.setText(getTextFromFile());
                    }
                });
            }
        }).start();
    }


    private String getTextFromFile() {
        FileInputStream fin = null;
        try {
            fin = openFileInput(fileName);
            byte[] bytes = new byte[fin.available()];
            fin.read(bytes);
            String text = new String(bytes);
            Log.d(TAG, text);
            return text;
        }
        catch (IOException ex) {
            Toast.makeText( this, ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
        finally {
            try {
                if (fin != null) fin.close();
            }
            catch (IOException ex) {
                Toast.makeText( this, ex.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
        return null;
    }
}