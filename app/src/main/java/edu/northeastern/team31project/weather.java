package edu.northeastern.team31project;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class weather extends AppCompatActivity implements View.OnClickListener {

    private TextView temperature, humidity, pressure, bodyTemperature;
    private EditText cityName;
    private static final String TAG = "WebServiceActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        setContentView(R.layout.activity_weather);

        cityName = findViewById(R.id.cityName);
        temperature = findViewById(R.id.temp);
        humidity = findViewById(R.id.humid);
        pressure = findViewById(R.id.pressure);
        bodyTemperature = findViewById(R.id.bodyTemperature);
        Button getWeather = findViewById(R.id.getWeather);
        getWeather.setOnClickListener(weather.this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.getWeather) {
            beginFindWeather();
        }
    }

    private void beginFindWeather() {
        String city = cityName.getText().toString();
        inProcessThread threadInProcess = new inProcessThread(city);
        findWeatherThread threadFindWeather = new findWeatherThread(city);
        threadFindWeather.run();
        threadInProcess.run();
    }

    class inProcessThread extends Thread{
        String city;

        inProcessThread(String city) {
            if (city.isEmpty()) {
                this.city = "San Jose";
            } else {
                this.city = city;
            }
        }

        @Override
        public void run() {
            super.run();
            ProgressDialog loading = new ProgressDialog(weather.this);
            loading.setTitle("Loading...");
            loading.setMessage("Loading the weather of " + city + "...");
            loading.show();

            Runnable loadingRunnable = loading::cancel;
            Handler canceller = new Handler();
            canceller.postDelayed(loadingRunnable, 1000);
        }
    }

    class findWeatherThread extends Thread {
        String city;

        findWeatherThread(String city) {
            if (city.isEmpty()) {
                this.city = "San Jose";
            } else {
                this.city = city;
            }
        }

        @Override
        public void run() {
            super.run();
            try
            {
                URL url = new URL("https://api.openweathermap.org/data/2.5/weather?q=" + city + "&appid=954f4759ee35ccce3fc3d2243e5e893d&units=imperial");
                String resp = httpResponse(url);

                JSONObject jObject = new JSONObject(resp);
                String vTemp = jObject.getJSONObject("main").getString("temp");
                String vHumidity = jObject.getJSONObject("main").getString("humidity");
                String vPressure = jObject.getJSONObject("main").getString("pressure");
                String vBodyTemperature = jObject.getJSONObject("main").getString("feels_like");

                runOnUiThread(() -> {
                    temperature.setText(vTemp);
                    humidity.setText(vHumidity);
                    pressure.setText(vPressure);
                    bodyTemperature.setText(vBodyTemperature);
                });

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static String httpResponse(URL url) throws IOException {
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");

        conn.setDoInput(true);
        conn.setConnectTimeout(5000);
        conn.connect();

        InputStream inputStream = conn.getInputStream();
        String resp = convertStreamToString(inputStream);

        inputStream.close();
        conn.disconnect();

        return resp;
    }

    private static String convertStreamToString(InputStream inputStream) {
        StringBuilder stringBuilder = new StringBuilder();
        try
        {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line;

            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }
            bufferedReader.close();
            return stringBuilder.toString().replace(",", ",\n");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
}
