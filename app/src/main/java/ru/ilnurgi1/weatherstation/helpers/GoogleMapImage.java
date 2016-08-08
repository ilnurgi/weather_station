package ru.ilnurgi1.weatherstation.helpers;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class GoogleMapImage {

    private static String LOG_TAG = "ILNURGI_LOG";

    public static Bitmap getGoogleMapThumbnail(float lati, float longi){
        String path =
                "http://maps.google.com/maps/api/staticmap?center="
                + lati + "," + longi
                + "&zoom=10&size=500x500&sensor=false";

        Bitmap bmp = null;

        try {
            java.net.URL url = new URL(path);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setRequestMethod("GET");
            connection.setReadTimeout(10000);
            connection.connect();

            bmp = BitmapFactory.decodeStream(connection.getInputStream());
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return bmp;
    }
}
