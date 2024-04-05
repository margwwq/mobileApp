package ru.mirea.mikhaylenkoma.favoritebook;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class SecondActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        Intent intent = getIntent();

        TextView textView = findViewById(R.id.textView);
        String text = textView.getText().toString();
        text+= intent.getStringExtra(MainActivity.KEY);
        textView.setText(text);
    }

    public void sendData(View view) {
        EditText textOut = findViewById(R.id.textOut);

        Intent data = new Intent();
        data.putExtra(MainActivity.USER_MESSAGE, textOut.getText().toString());
        setResult(Activity.RESULT_OK, data);
        finish();
    }
}
