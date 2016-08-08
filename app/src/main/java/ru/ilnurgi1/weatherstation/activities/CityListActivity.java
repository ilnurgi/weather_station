package ru.ilnurgi1.weatherstation.activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import ru.ilnurgi1.weatherstation.adapters.CityListAdapter;
import ru.ilnurgi1.weatherstation.db.CityWeatherDBHelper;
import ru.ilnurgi1.weatherstation.helpers.URLDownloader;
import ru.ilnurgi1.weatherstation.models.CityWeather;
import ru.ilnurgi1.weatherstation.R;


public class CityListActivity extends AppCompatActivity {

    CityWeatherDBHelper db;

    private ArrayList<CityWeather> cityWeathers = new ArrayList<>();
    private CityListAdapter cityArrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        deleteDatabase("/data/data/ru.ilnurgi1.weatherstation/databases/CityWeatherDB");

        setContentView(R.layout.cities_list);

        cityArrayAdapter = new CityListAdapter(this, cityWeathers);

        db = new CityWeatherDBHelper(getApplicationContext());

        updateAdapter();

        ListView cityListView = (ListView) findViewById(R.id.list);
        cityListView.setAdapter(cityArrayAdapter);

        cityListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                CityWeather city = cityWeathers.get(position);

                Intent intent = new Intent(getApplicationContext(), CityShowActivity.class);
                intent.putExtra("city", city);

                startActivity(intent);
            }
        });

        cityListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int pos, long id) {
                deleteRecord(pos);
                return true;
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        updateAdapter();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.cities_list_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.update_cities:
                Toast.makeText(this, "Обновление температур городов", Toast.LENGTH_SHORT).show();
                new UpdateCitiesAsyncTask().execute();
                break;
            case R.id.add_citi:
                Toast.makeText(this, "Добавление города", Toast.LENGTH_SHORT).show();
                addRecord();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    public void addRecord() {

        LayoutInflater li = LayoutInflater.from(getApplicationContext());
        View promptsView = li.inflate(R.layout.query, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        alertDialogBuilder.setView(promptsView);

        final EditText input = (EditText) promptsView.findViewById(R.id.city_name_input);

        alertDialogBuilder
            .setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(
                            getApplicationContext(), input.getText().toString(), Toast.LENGTH_SHORT
                        ).show();

                        new AddCitiyDBAsyncTask().execute(input.getText().toString());
                        updateAdapter();
                    }
                })
            .setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
            });

        AlertDialog alertDialog = alertDialogBuilder.create();

        alertDialog.show();

    }

    public void deleteRecord(Integer pos){

        final CityWeather city = cityArrayAdapter.getItem(pos);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Удаление записи");
        builder.setMessage("Удалить запись, " + city.getCityName() + "?");

        builder.setPositiveButton("Да", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {

                db.delete(city);
                dialog.dismiss();
                updateAdapter();
            }
        });

        builder.setNegativeButton("Нет", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog alert = builder.create();
        alert.show();
    }



    void updateAdapter(){
        new GetCitiesDBAsyncTask().execute();
    }

    public class GetCitiesDBAsyncTask extends AsyncTask<Void, Void, ArrayList> {

        @Override
        protected ArrayList doInBackground(Void... voids) {
            return db.getAll();
        }

        @Override
        protected void onPostExecute(ArrayList list) {

            cityArrayAdapter.clear();
            cityArrayAdapter.addAll(list);
            cityArrayAdapter.notifyDataSetChanged();

        }

    }

    public class AddCitiyDBAsyncTask extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... items) {
            db.add(new CityWeather(items[0]));
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            updateAdapter();
        }

    }

    public class UpdateCitiesAsyncTask extends AsyncTask<Void, String, Void> {

        ProgressDialog pDialog;

        @Override
        protected Void doInBackground(Void... path) {

            Integer count = cityArrayAdapter.getCount();

            for (int i=0; i<count; i++){
                CityWeather city = cityArrayAdapter.getItem(i);
                publishProgress(city.getCityName());
                String content;

                try {
                    content = URLDownloader.download(
                        "http://api.openweathermap.org/data/2.5/weather?q=" +
                                city.getCityNameTranslit() +
                                "&APPID=d8df8cc522a0bed67cdf6a3a77690f13&units=metric");
                } catch (IOException e) {
                    content = e.getMessage();
                }
                update(city, content);
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void content) {
            updateAdapter();

            if (pDialog.isShowing())
                pDialog.dismiss();

            Toast.makeText(
                    getApplicationContext(), "Все данные успешно обновлены", Toast.LENGTH_SHORT
            ).show();
        }

        @Override
        protected void onProgressUpdate(String... progress) {
            pDialog.setMessage("Update " + progress[0]);
        }

        private void update(CityWeather city, String jsonStr){

            JSONObject resultJson;
            float temp = CityWeather.DEFAULT_TEMPERATURE;
            int pressure = CityWeather.DEFAULT_PRESSURE;
            int humidity = CityWeather.DEFAULT_HUMIDITY;
            long timestamp = CityWeather.DEFAULT_TIMESTAMP;
            String weatherIcon = CityWeather.DEFAULT_WEATHER_ICON;
            float lon = CityWeather.DEFAULT_LON;
            float lat = CityWeather.DEFAULT_LAT;

            try {
                resultJson = new JSONObject(jsonStr);

                JSONObject main = resultJson.getJSONObject("main");
                JSONArray weather = resultJson.getJSONArray("weather");
                JSONObject weatherObj = weather.getJSONObject(0);

                temp = Float.parseFloat(main.getString("temp"));
                pressure = main.getInt("pressure");
                humidity= main.getInt("humidity");

                timestamp = resultJson.getLong("dt");

                JSONObject coord = resultJson.getJSONObject("coord");
                lat = Float.parseFloat(coord.getString("lat"));
                lon = Float.parseFloat(coord.getString("lon"));

                weatherIcon = "a" + weatherObj.getString("icon");


            } catch (JSONException e) {
                e.printStackTrace();
            }

            db.update(
                    city,
                    new CityWeather(
                            city.getCityName(),
                            temp,
                            pressure,
                            humidity,
                            timestamp,
                            weatherIcon,
                            lon,
                            lat));

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(CityListActivity.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

    }

}
