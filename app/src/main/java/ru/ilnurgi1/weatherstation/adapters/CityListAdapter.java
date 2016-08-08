package ru.ilnurgi1.weatherstation.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import ru.ilnurgi1.weatherstation.models.CityWeather;
import ru.ilnurgi1.weatherstation.R;


public class CityListAdapter extends ArrayAdapter<CityWeather> {

    private Context context;
    private ArrayList<CityWeather> values;
    private LayoutInflater lInflater;

    public CityListAdapter(Context context, ArrayList<CityWeather> values) {
        super(context, R.layout.cities_list_item, values);

        this.context = context;
        this.values = values;

        lInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = lInflater.inflate(R.layout.cities_list_item, parent, false);

        TextView cityNameView = (TextView) view.findViewById(R.id.cityName);
        TextView temperatureView = (TextView) view.findViewById(R.id.temperature);
        ImageView imageWeather = (ImageView) view.findViewById(R.id.weather_image);
        TextView dateView = (TextView) view.findViewById(R.id.date);

        CityWeather city = values.get(position);
        cityNameView.setText(city.getCityName());
        temperatureView.setText(city.getTemperatureString() + " \u00b0C");
        dateView.setText(city.getDateTimeString());

        imageWeather.setImageResource(
                context.getResources().getIdentifier(
                        city.getWeatherIcon(), "drawable", context.getPackageName()));

        return view;
    }

}
