package ru.mirea.mikhaylenkoma.mireaproject.ui.les8;

import android.app.Application;

import com.yandex.mapkit.MapKitFactory;

public class App extends Application {
    private final String MAPKIT_API_KEY = "48ea2f69-8680-44ab-afe1-8bd16b86c632";

    @Override
    public void onCreate() {
        super.onCreate();
        MapKitFactory.setApiKey(MAPKIT_API_KEY);
    }
}