package ru.ilnurgi1.weatherstation.activities;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import ru.ilnurgi1.weatherstation.db.CityWeatherDBHelper;
import ru.ilnurgi1.weatherstation.helpers.URLDownloader;
import ru.ilnurgi1.weatherstation.models.CityWeather;
import ru.ilnurgi1.weatherstation.R;

public class CityShowActivity extends AppCompatActivity {

    private static String LOG_TAG = "ILNURGI_LOG";

    public CityWeather city;

    TextView cityNameView;
    TextView cityTemperatureView;
    TextView cityPressureView;
    TextView cityHumidityView;
    TextView cityTimestampView;

    ImageView imageWeatherView;
    ImageView imageMapView;

    CityWeatherDBHelper db;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        setContentView(R.layout.city_show);

        db = new CityWeatherDBHelper(getApplicationContext());

        city = getIntent().getParcelableExtra("city");

        updateContentView(city);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.cities_show_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        Toast.makeText(this, city.getCityName(), Toast.LENGTH_SHORT).show();

        switch (item.getItemId()){
            case R.id.update_cities:
                Toast.makeText(this, city.getCityName(), Toast.LENGTH_SHORT).show();
                new UpdateCitiAsyncTask().execute();

        }
        return super.onOptionsItemSelected(item);
    }

    private void updateContentView(CityWeather city){

        cityNameView = (TextView) findViewById(R.id.city_name);
        cityNameView.setText(city.getCityName());

        cityTemperatureView = (TextView) findViewById(R.id.temperature);
        cityTemperatureView.setText("Температура: " + city.getTemperatureString() + " \u00b0C");

        cityPressureView = (TextView) findViewById(R.id.pressure);
        cityPressureView.setText("Давление: " + city.getPressureString() + " hPa");

        cityHumidityView = (TextView) findViewById(R.id.humidity);
        cityHumidityView.setText("Влажность: " + city.getHumidityString() + " %");

        cityTimestampView = (TextView) findViewById(R.id.timestamp);
        cityTimestampView.setText("Актуально на: \n" + city.getDateTimeString());

        imageWeatherView = (ImageView) findViewById(R.id.weather_image);
        imageWeatherView.setImageResource(
            getResources().getIdentifier(city.getWeatherIcon(), "drawable", getPackageName()));

        imageMapView = (ImageView) findViewById(R.id.map_image);
        imageMapView.setImageBitmap(city.getMapImage());

    }

    public class UpdateCitiAsyncTask extends AsyncTask<String, Void, String> {

        ProgressDialog pDialog;

        @Override
        protected String doInBackground(String... path) {

            String content;
            try {
                content = URLDownloader.download(
                        "http://api.openweathermap.org/data/2.5/weather?q=" +
                                city.getCityNameTranslit() +
                                "&APPID=d8df8cc522a0bed67cdf6a3a77690f13&units=metric"
                );
            } catch (IOException e) {
                content = e.getMessage();
            }
            return content;
        }

        @Override
        protected void onPostExecute(String content) {

            JSONObject resultJson;
            try {
                resultJson = new JSONObject(content);

                JSONObject main = resultJson.getJSONObject("main");

                JSONArray weather = resultJson.getJSONArray("weather");
                JSONObject weatherObj = weather.getJSONObject(0);

                JSONObject coord = resultJson.getJSONObject("coord");
                float lat = Float.parseFloat(coord.getString("lat"));
                float lon = Float.parseFloat(coord.getString("lon"));

                float temperature = Float.parseFloat(main.getString("temp"));
                int pressure = main.getInt("pressure");
                int humidity = main.getInt("humidity");
                long timestamp = resultJson.getLong("dt");

                String weatherIcon = "a" + weatherObj.getString("icon");

                CityWeather new_city = new CityWeather(
                    city.getCityName(),
                        temperature,
                        pressure,
                        humidity,
                        timestamp,
                        weatherIcon,
                        lon,
                        lat);

                db.update(city, new_city);
                updateContentView(db.getById(city.getDb_id()));

            } catch (JSONException e) {
                e.printStackTrace();
            }

            if (pDialog.isShowing())
                pDialog.dismiss();

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(CityShowActivity.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

    }

}
