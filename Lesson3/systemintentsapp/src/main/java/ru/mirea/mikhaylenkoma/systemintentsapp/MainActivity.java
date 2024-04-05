package ru.mirea.mikhaylenkoma.systemintentsapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onClickCall(View view) {
        Intent _intent = new Intent(Intent.ACTION_DIAL);
        _intent.setData(Uri.parse("tel:89528135126"));
        startActivity(_intent);
    }

    public void onClickOpenBrowser(View view) {
        Intent _intent = new Intent(Intent.ACTION_VIEW);
        _intent.setData(Uri.parse("http://developer.android.com"));
        startActivity(_intent);
    }

    public void onClickOpenMaps(View view) {
        Intent _intent = new Intent(Intent.ACTION_VIEW);
        _intent.setData(Uri.parse("geo:55.749479,37.613944"));
        startActivity(_intent);
    }
}