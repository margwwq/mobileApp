package ru.mirea.mikhaylenkoma.httpurlconnection;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import ru.mirea.mikhaylenkoma.httpurlconnection.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private final String TAG = this.getClass().getSimpleName();
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.buttonGetInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConnectivityManager connectivityManager =
                        (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = null;
                if(connectivityManager != null) {
                    networkInfo = connectivityManager.getActiveNetworkInfo();
                }
                if(networkInfo != null && networkInfo.isConnected()) {
                    new DownloadPageTask().execute("https://ipinfo.io/json");
                } else {
                    Toast.makeText(MainActivity.this, "NO INTERNET",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private class DownloadPageTask extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... urls) {
            try {
                return downloadIpInfo(urls[0]);
            } catch (IOException e) {
                e.printStackTrace();
                return "error";
            }
        }

        @Override
        protected void onPostExecute(String result) {
            try {
                JSONObject responseJson = new JSONObject(result);
                if(responseJson.has("current_weather")) {
                    JSONObject currentWeatherUnits = responseJson.getJSONObject("current_weather_units");
                    JSONObject currentWeather = responseJson.getJSONObject("current_weather");

                    Map<String, Object> weatherData = new HashMap<>();
                    weatherData.put("temperature", currentWeather.getDouble("temperature") +
                            currentWeatherUnits.getString("temperature"));
                    weatherData.put("windspeed", currentWeather.getDouble("windspeed") +
                            currentWeatherUnits.getString("windspeed"));

                    for (Map.Entry<String, Object> entry : weatherData.entrySet()) {
                        String value = entry.getValue().toString();
                        if(entry.getKey().equals("temperature")) {
                            binding.textViewTemperature.setText(String.format(Locale.getDefault(),
                                    "ТЕМПЕРАТУРА: %s", value));
                        } else {
                            binding.textViewWindSpeed.setText(String.format(Locale.getDefault(),
                                    "СКОРОСТЬ ВЕТРА: %s", value));
                        }
                    }
                } else {
                    String city = responseJson.getString("city");
                    String region = responseJson.getString("region");
                    String country = responseJson.getString("country");
                    String postal = responseJson.getString("postal");
                    String timezone = responseJson.getString("timezone");
                    binding.textViewCity.setText("ГОРОД: " + city);
                    binding.textViewRegion.setText("РЕГИОН: " + region);
                    binding.textViewCountry.setText("СТРАНА: " + country);
                    binding.textViewPostalCode.setText("ПОЧТОВЫЙ КОД: " + postal);
                    binding.textViewTimezone.setText("ВРЕМЕННАЯ ЗОНА: " + timezone);

                    String latitudeLongitude = responseJson.getString("loc");
                    String[] parts = latitudeLongitude.split(",");
                    if(parts.length == 2) {
                        float latitude = Float.parseFloat(parts[0].trim());
                        float longitude = Float.parseFloat(parts[1].trim());
                        new DownloadPageTask().execute(String.format(Locale.getDefault(),
                                "https://api.open-meteo.com/v1/forecast?latitude=%f&longitude=%f&current_weather=true",
                                latitude, longitude));
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            super.onPreExecute();
        }

        private String downloadIpInfo(String address) throws IOException {
            InputStream inputStream = null;
            String data = "";
            try {
                URL url = new URL(address);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setReadTimeout(100000);
                connection.setConnectTimeout(100000);
                connection.setRequestMethod("GET");
                connection.setInstanceFollowRedirects(true);
                connection.setUseCaches(false);
                connection.setDoInput(true);
                int responseCode = connection.getResponseCode();

                if(responseCode == HttpURLConnection.HTTP_OK) {
                    inputStream = connection.getInputStream();
                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    int read = 0;
                    while((read = inputStream.read()) != -1) {
                        bos.write(read);
                    }
                    bos.close();
                    data = bos.toString();
                } else {
                    data = connection.getResponseMessage() + ". Error code: " + responseCode;
                }
                connection.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if(inputStream != null) {
                    inputStream.close();
                }
            }
            return data;
        }
    }

}