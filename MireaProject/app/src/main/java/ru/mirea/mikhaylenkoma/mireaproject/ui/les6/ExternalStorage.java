package ru.mirea.mikhaylenkoma.mireaproject.ui.les6;

import android.os.Environment;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ExternalStorage {
    static boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state)
                || Environment.MEDIA_MOUNTED_READ_ONLY.equals(state);
    }

    static boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }

    static void writeFileToExternalStorage(String filename, String message) {
        File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
        File file = new File(path, filename);
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file.getAbsoluteFile());
            OutputStreamWriter output = new OutputStreamWriter(fileOutputStream);
            output.write(message);
            output.close();
        } catch(IOException e) {
            Log.w("ExternalStorage", "Error writing file " + filename);
        }
    }

    static String readFileFromExternalStorage(String filename) {
        String resultString = "FileIsNotExist";
        File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
        File file = new File(path, filename);
        if(file.exists()) {
            try {
                FileInputStream fileInputStream = new FileInputStream(file.getAbsoluteFile());
                InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream,
                        StandardCharsets.UTF_8);
                List<String> lines = new ArrayList<>();
                BufferedReader reader = new BufferedReader(inputStreamReader);
                String line = reader.readLine();
                while(line != null) {
                    lines.add(line);
                    line = reader.readLine();
                }
                resultString = String.join("\n", lines);
                return resultString;
            } catch (Exception e) {
                Log.w("ExternalStorage", String.format(Locale.getDefault(),
                        "Read from file %s failed", filename));
            }
        }
        return resultString;
    }
}