package ru.mirea.mikhaylenkoma.yandexmaps;

import android.app.Application;

import com.yandex.mapkit.MapKitFactory;

public class App extends Application {
    private final String MAPKIT_API_KEY = "35df923a-b6e1-4739-a8ba-09e0a9edaf3f";

    @Override
    public void onCreate() {
        super.onCreate();
        MapKitFactory.setApiKey(MAPKIT_API_KEY);
    }
}