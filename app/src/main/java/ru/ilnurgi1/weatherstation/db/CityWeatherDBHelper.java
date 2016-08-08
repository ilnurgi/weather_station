package ru.ilnurgi1.weatherstation.db;

import android.content.ContentValues;
import android.content.Context;
import android.content.ContextWrapper;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import ru.ilnurgi1.weatherstation.helpers.GoogleMapImage;
import ru.ilnurgi1.weatherstation.models.CityWeather;


public class CityWeatherDBHelper extends SQLiteOpenHelper {

    private static String LOG_TAG = "ILNURGI_LOG";

    String TABLENAME = "cityweathers";

    String COLUMN_ID = "id";
    String COLUMN_CITY_NAME = "cityname";
    String COLUMN_TEMPERATURE = "temperature";
    String COLUMN_PRESSURE = "pressure";
    String COLUMN_HUMIDITY = "humidity";
    String COLUMN_TIMESTAMP = "timestamp";
    String COLUMN_WEATHER_ICON = "weathericon";
    String COLUMN_LON = "lon";
    String COLUMN_LAT = "lat";
    String COLUMN_MAP_IMAGE_PATH = "map_image_path";

    Context context;

    public CityWeatherDBHelper(Context context) {
        super(context, "CityWeatherDB", null, 1);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        sqLiteDatabase.execSQL(
                "create table " + TABLENAME + "("
                + COLUMN_ID + " integer primary key autoincrement,"
                + COLUMN_CITY_NAME + " char(255),"
                + COLUMN_TEMPERATURE + " float,"
                + COLUMN_PRESSURE + " integer,"
                + COLUMN_HUMIDITY + " integer,"
                + COLUMN_TIMESTAMP + " long,"
                + COLUMN_WEATHER_ICON + " char(255),"
                + COLUMN_LON + " float,"
                + COLUMN_LAT + " float,"
                + COLUMN_MAP_IMAGE_PATH + " char(255))"
        );
        sqLiteDatabase.execSQL(
                "insert into " + TABLENAME + " values (1, 'Казань', 0, 0, 0, 0, 'a50d', 0, 0, '')");
        sqLiteDatabase.execSQL(
                "insert into " + TABLENAME + " values (2, 'Шемордан', 0, 0, 0, 0, 'a50d', 0, 0, '')");
        sqLiteDatabase.execSQL(
                "insert into " + TABLENAME + " values (3, 'Москва', 0, 0, 0, 0, 'a50d', 0, 0, '')");
        sqLiteDatabase.execSQL(
                "insert into " + TABLENAME + " values (4, 'Красноярск', 0, 0, 0, 0, 'a50d', 0, 0, '')");
        sqLiteDatabase.execSQL(
                "insert into " + TABLENAME + " values (5, 'Новосибирск', 0, 0, 0, 0, 'a50d', 0, 0, '')");
        sqLiteDatabase.execSQL(
                "insert into " + TABLENAME + " values (6, 'Екатеринбург', 0, 0, 0, 0, 'a50d', 0, 0, '')");
        sqLiteDatabase.execSQL(
                "insert into " + TABLENAME + " values (7, 'Уфа', 0, 0, 0, 0, 'a50d', 0, 0, '')");
        sqLiteDatabase.execSQL(
                "insert into " + TABLENAME + " values (8, 'Челябинск', 0, 0, 0, 0, 'a50d', 0, 0, '')");
        sqLiteDatabase.execSQL(
                "insert into " + TABLENAME + " values (9, 'Омск', 0, 0, 0, 0, 'a50d', 0, 0, '')");
        sqLiteDatabase.execSQL(
                "insert into " + TABLENAME + " values (10, 'Самара', 0, 0, 0, 0, 'a50d', 0, 0, '')");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {

    }

    public ArrayList<CityWeather> getAll() {
        ArrayList<CityWeather> records = new ArrayList<>();

        SQLiteDatabase db = getWritableDatabase();
        Cursor c = db.query(TABLENAME, null, null, null, null, null, null);
        if (c != null){
            if (c.moveToFirst()){
                do {
                    Integer db_id = c.getInt(c.getColumnIndex(COLUMN_ID));
                    String cityName = c.getString(c.getColumnIndex(COLUMN_CITY_NAME));
                    Float temperature = c.getFloat(c.getColumnIndex(COLUMN_TEMPERATURE));
                    Integer pressure = c.getInt(c.getColumnIndex(COLUMN_PRESSURE));
                    Integer humidity = c.getInt(c.getColumnIndex(COLUMN_HUMIDITY));
                    Long timestamp = c.getLong(c.getColumnIndex(COLUMN_TIMESTAMP));
                    String weatherIcon = c.getString(c.getColumnIndex(COLUMN_WEATHER_ICON));
                    Float lon = c.getFloat(c.getColumnIndex(COLUMN_LON));
                    Float lat = c.getFloat(c.getColumnIndex(COLUMN_LAT));
                    String map_image_path = c.getString(c.getColumnIndex(COLUMN_MAP_IMAGE_PATH));

                    CityWeather city = new CityWeather(
                            cityName,
                            temperature,
                            db_id,
                            pressure,
                            humidity,
                            timestamp,
                            weatherIcon,
                            lon,
                            lat,
                            map_image_path);
                    records.add(city);
                } while (c.moveToNext());
            }
        }
        c.close();
        return records;
    }

    public CityWeather getById(int row_id){

        CityWeather city = null;

        SQLiteDatabase db = getWritableDatabase();
        Cursor c = db.query(TABLENAME, null, "id = ?", new String[] {String.valueOf(row_id)}, null, null, null, null);
        if (c != null){
            if (c.moveToFirst()){
                do {
                    Integer db_id = c.getInt(c.getColumnIndex(COLUMN_ID));
                    String cityName = c.getString(c.getColumnIndex(COLUMN_CITY_NAME));
                    Float temperature = c.getFloat(c.getColumnIndex(COLUMN_TEMPERATURE));
                    Integer pressure = c.getInt(c.getColumnIndex(COLUMN_PRESSURE));
                    Integer humidity = c.getInt(c.getColumnIndex(COLUMN_HUMIDITY));
                    Long timestamp = c.getLong(c.getColumnIndex(COLUMN_TIMESTAMP));
                    String weatherIcon = c.getString(c.getColumnIndex(COLUMN_WEATHER_ICON));
                    Float lon = c.getFloat(c.getColumnIndex(COLUMN_LON));
                    Float lat = c.getFloat(c.getColumnIndex(COLUMN_LAT));
                    String map_image_path = c.getString(c.getColumnIndex(COLUMN_MAP_IMAGE_PATH));

                    city = new CityWeather(
                            cityName,
                            temperature,
                            db_id,
                            pressure,
                            humidity,
                            timestamp,
                            weatherIcon,
                            lon,
                            lat,
                            map_image_path);
                } while (c.moveToNext());
            }
        }
        return city;
    }

    public void add(CityWeather record) {

        SQLiteDatabase db = getWritableDatabase();

        ContentValues cv = new ContentValues();

        cv.put(COLUMN_CITY_NAME, record.getCityName());
        cv.put(COLUMN_TEMPERATURE, record.getTemperature());
        cv.put(COLUMN_PRESSURE, record.getPressure());
        cv.put(COLUMN_HUMIDITY, record.getHumidity());
        cv.put(COLUMN_TIMESTAMP, record.getTimestamp());
        cv.put(COLUMN_WEATHER_ICON, record.getWeatherIcon());
        cv.put(COLUMN_LON, record.getLon());
        cv.put(COLUMN_LAT, record.getLat());
        cv.put(COLUMN_MAP_IMAGE_PATH, record.getMapImagePath());

        db.insert(TABLENAME, null, cv);

    }

    public void update(
            CityWeather record, String cityName, Float temperatue, int pressure, int humidity,
            long timestamp, String weatherIcon, Float lon, Float lat, String map_image_path){

        SQLiteDatabase db = getWritableDatabase();

        ContentValues cv = new ContentValues();

        if (cityName != null) {
            cv.put(COLUMN_CITY_NAME, cityName);
        }

        if (map_image_path != null) {
            cv.put(COLUMN_MAP_IMAGE_PATH, map_image_path);
        }

        if (temperatue != null) {
            cv.put(COLUMN_TEMPERATURE, temperatue);
        }

        if (lon != null) {
            cv.put(COLUMN_LON, lon);
        }

        if (lat != null) {
            cv.put(COLUMN_LAT, lat);
        }

        if (pressure > 0) {
            cv.put(COLUMN_PRESSURE, pressure);
        }

        if (humidity > 0) {
            cv.put(COLUMN_HUMIDITY, humidity);
        }

        if (timestamp > 0) {
            cv.put(COLUMN_TIMESTAMP, timestamp);
        }

        if (weatherIcon != null) {
            cv.put(COLUMN_WEATHER_ICON, weatherIcon);
        }

        db.update(TABLENAME, cv, "id = ?", new String[] {String.valueOf(record.getDb_id())});
        createImage(record, lon, lat, record.getDb_id());
    }

    public void update(CityWeather old_city, CityWeather new_city){
        this.update(
                old_city,
                new_city.getCityName(),
                new_city.getTemperature(),
                new_city.getPressure(),
                new_city.getHumidity(),
                new_city.getTimestamp(),
                new_city.getWeatherIcon(),
                new_city.getLon(),
                new_city.getLat(),
                new_city.getMapImagePath()
                );
    }

    public void delete(CityWeather record){

        SQLiteDatabase db = getWritableDatabase();

        db.delete(TABLENAME, "id = ?", new String[] {String.valueOf(record.getDb_id())});
    }

    public void createImage(CityWeather city, float lon, float lat, long row_id){
        ContextWrapper cw = new ContextWrapper(context);

        if (lat != CityWeather.DEFAULT_LAT && lon != CityWeather.DEFAULT_LON) {

            File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);

            File mypath = new File(directory, row_id + city.getCityNameTranslit() + ".png");
            Bitmap bitmapImage = GoogleMapImage.getGoogleMapThumbnail(lat, lon);

            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(mypath);
                // Use the compress method on the BitMap object to write image to the OutputStream
                bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            updateImagePath(row_id, mypath.getAbsolutePath());
        }
    }

    public void updateImagePath(long row_id, String newPath){

        SQLiteDatabase db = getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(COLUMN_MAP_IMAGE_PATH, newPath);

        db.update(TABLENAME, cv, "id = ?", new String[] {String.valueOf(row_id)});
    }
}
