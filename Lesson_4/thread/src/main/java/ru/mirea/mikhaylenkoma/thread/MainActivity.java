package ru.mirea.mikhaylenkoma.thread;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.Arrays;

import ru.mirea.mikhaylenkoma.thread.databinding.ActivityMainBinding;

public	class	MainActivity	extends	AppCompatActivity {
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        TextView infoTextView = findViewById(R.id.textView);
        Thread mainThread = Thread.currentThread();
        infoTextView.setText("Имя текущего потока: " + mainThread.getName());
        mainThread.setName("МОЙ НОМЕР ГРУППЫ: 10, НОМЕР ПО СПИСКУ: 15, МОЙ ЛЮБИИМЫЙ ФИЛЬМ: Мастер и Маргарита");
        infoTextView.append("\n Новое имя потока: " + mainThread.getName());
        Log.d(MainActivity.class.getSimpleName(),	"Stack:	"	+	Arrays.toString(mainThread.getStackTrace()));


        binding.button.setOnClickListener(new  View.OnClickListener()  {
            @Override
            public  void onClick(View v)  {
                new  Thread(new  Runnable()  {
                    public  void run()  {
                        if (binding.days.getText().toString().length() < 1 || binding.lessons.getText().toString().length() < 1)
                            return;

                        int days = Integer.parseInt(binding.days.getText().toString());
                        int lessons = Integer.parseInt(binding.lessons.getText().toString());

                        runOnUiThread(() -> {binding.textView.setText(String.valueOf(((float)lessons / (float)days)));});
                    }
                }).start();
            }
        });
    }
}