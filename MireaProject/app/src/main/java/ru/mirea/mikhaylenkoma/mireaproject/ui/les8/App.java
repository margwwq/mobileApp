package ru.mirea.mikhaylenkoma.mireaproject.ui.les8;

import android.app.Application;

import com.yandex.mapkit.MapKitFactory;

public class App extends Application {
    private final String MAPKIT_API_KEY = "4622de22-fd2c-4d45-ab4d-90034abe8151";

    @Override
    public void onCreate() {
        super.onCreate();
        MapKitFactory.setApiKey(MAPKIT_API_KEY);
    }
}