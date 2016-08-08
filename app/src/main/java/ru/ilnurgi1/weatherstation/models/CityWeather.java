package ru.ilnurgi1.weatherstation.models;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Parcel;
import android.os.Parcelable;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Date;

import ru.ilnurgi1.weatherstation.helpers.DateTime;
import ru.ilnurgi1.weatherstation.helpers.Translit;

/*
 * модель прогноза для города
 */
public class CityWeather implements Parcelable{

    public static int DEFAULT_DB_ID = 0;
    public static int DEFAULT_TIMESTAMP = 0;
    public static int DEFAULT_PRESSURE = 0;
    public static int DEFAULT_HUMIDITY = 0;
    public static float DEFAULT_TEMPERATURE = 0f;
    public static float DEFAULT_LON = 0f;
    public static float DEFAULT_LAT = 0f;
    public static String DEFAULT_WEATHER_ICON = "a50d";
    public static String DEFAULT_MAP_IMAGE_PATH = "";

    private int db_id;

    // название города
    private String cityName;

    // температура в городе
    private float temperature;

    // давление в городе
    private int pressure;

    // влажность в городе
    private int humidity;

    // дата актуализации
    private long timestamp;

    // картинка, состояние погоды
    private String weatherIcon;

    // lon
    private float lon;

    // lat
    private float lat;

    // map_image_path
    private String mapImagePath;

    public CityWeather(
            String cityName, Float temperature, int db_id, int pressure, int humidity,
            long timestamp, String weatherIcon, float lon, float lat, String mapImagePath) {
        this.cityName = cityName;
        this.temperature = temperature;
        this.pressure = pressure;
        this.db_id = db_id;
        this.humidity = humidity;
        this.timestamp = timestamp;
        this.weatherIcon = weatherIcon;
        this.lon = lon;
        this.lat = lat;
        this.mapImagePath = mapImagePath;
    }

    public CityWeather(
            String cityName, Float temperature, int pressure, int humidity,
            long timestamp, String weatherIcon, float lon, float lat) {
        this(
                cityName,
                temperature,
                DEFAULT_DB_ID,
                pressure,
                humidity,
                timestamp,
                weatherIcon,
                lon,
                lat,
                DEFAULT_MAP_IMAGE_PATH);
    }

    public CityWeather(String cityName) {
        this(
                cityName,
                DEFAULT_TEMPERATURE,
                DEFAULT_DB_ID,
                DEFAULT_PRESSURE,
                DEFAULT_HUMIDITY,
                DEFAULT_TIMESTAMP,
                DEFAULT_WEATHER_ICON,
                DEFAULT_LON,
                DEFAULT_LAT,
                DEFAULT_MAP_IMAGE_PATH);
    }

    // name
    public String getCityName() {
        return cityName;
    }

    public String getCityNameTranslit(){
        return Translit.toTranslit(this.getCityName());
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    // temperature
    public Float getTemperature() {
        return temperature;
    }

    public String getTemperatureString(){
        return String.valueOf(temperature);
    }

    public void setTemperature(Float temperature) {
        this.temperature = temperature;
    }

    // db_id
    public int getDb_id() {
        return db_id;
    }

    public void setDb_id(int db_id) {
        this.db_id = db_id;
    }

    // pressure
    public int getPressure() {
        return pressure;
    }
    public String getPressureString() {
        return String.valueOf(pressure);
    }

    public void setPressure(int pressure) {
        this.pressure = pressure;
    }

    // humidity
    public int getHumidity() {
        return humidity;
    }

    public String getHumidityString() {
        return String.valueOf(humidity);
    }

    public void setHumidity(int humidity) {
        this.humidity = humidity;
    }

    // actual_date
    public long getTimestamp() {
        return timestamp;
    }
    public String getTimestampString() {
        return String.valueOf(timestamp);
    }

    public String getDateTimeString() {
        return DateTime.getStringFromDate(new Date(timestamp * 1000));
    }


    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    // weatherIcon
    public String getWeatherIcon() {
        return weatherIcon;
    }

    public void setWeatherIcon(String weatherIcon) {
        this.weatherIcon = weatherIcon;
    }

    // lat
    public float getLat() {
        return lat;
    }

    public void setLat(float lat) {
        this.lat = lat;
    }

    // lon
    public float getLon() {
        return lon;
    }

    public void setLon(float lon) {
        this.lon = lon;
    }

    // map image
    public String getMapImagePath() {
        return mapImagePath;
    }

    public void setMapImagePath(String mapImagePath) {
        this.mapImagePath = mapImagePath;
    }

    public Bitmap getMapImage(){

        Bitmap bitmap = null;

        if (getMapImagePath() != DEFAULT_MAP_IMAGE_PATH){

            try {
                File file = new File(getMapImagePath());
                bitmap = BitmapFactory.decodeStream(new FileInputStream(file));

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        return bitmap;
    }

    public CityWeather(Parcel in) {
        db_id = in.readInt();
        cityName = in.readString();
        temperature = in.readFloat();
        pressure = in.readInt();
        humidity = in.readInt();
        timestamp = in.readLong();
        weatherIcon = in.readString();
        lon = in.readFloat();
        lat = in.readFloat();
        mapImagePath = in.readString();
    }

    public static final Creator<CityWeather> CREATOR = new Creator<CityWeather>() {
        @Override
        public CityWeather createFromParcel(Parcel in) {
            return new CityWeather(in);
        }

        @Override
        public CityWeather[] newArray(int size) {
            return new CityWeather[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(getDb_id());
        parcel.writeString(getCityName());
        parcel.writeFloat(getTemperature());
        parcel.writeInt(getPressure());
        parcel.writeInt(getHumidity());
        parcel.writeLong(getTimestamp());
        parcel.writeString(getWeatherIcon());
        parcel.writeFloat(getLon());
        parcel.writeFloat(getLat());
        parcel.writeString(getMapImagePath());
    }
}
