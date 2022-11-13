package com.example.androidassignments;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.util.Xml;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.xmlpull.v1.XmlPullParser;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

public class WeatherForecast extends AppCompatActivity {
    private final String ACTIVITY_NAME = "WeatherForecastActivity";

    ProgressBar pgb;
    ImageView img;
    TextView currentTempView, minTempView, maxTempView;
    Spinner citySpinner;
    List<String> cityList;
    private String min, max, currentTemp, city;
    private Bitmap weatherPic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_forecast);

        currentTempView = findViewById(R.id.currentTemperature);
        minTempView = findViewById(R.id.minTemperature);
        maxTempView = findViewById(R.id.maxTemperature);
        img = findViewById(R.id.weatherView);
        citySpinner = findViewById(R.id.spinner);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.cities_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        citySpinner.setAdapter(adapter);

        pgb = findViewById(R.id.progressBar);
        pgb.setVisibility(View.VISIBLE);

        ForecastQuery forecast = new ForecastQuery();
        forecast.execute();

        citySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                ForecastQuery forecast = new ForecastQuery();
                forecast.execute();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private class ForecastQuery extends AsyncTask<String, Integer, String> {
        @Override
        protected String doInBackground(String... strings) {
            try {
                city = citySpinner.getSelectedItem().toString();
                URL url = new URL("https://api.openweathermap.org/data/2.5/weather?q=" + city + ",ca&APPID=6bb65e38f4c0c2196fa7e02ef4630e63&mode=xml&units=metric");

                HttpsURLConnection httpsURLConnection = (HttpsURLConnection) url.openConnection();
                httpsURLConnection.setReadTimeout(10000);
                httpsURLConnection.setConnectTimeout(15000);
                httpsURLConnection.setRequestMethod("GET");
                httpsURLConnection.setDoInput(true);
                httpsURLConnection.connect();

                InputStream in = httpsURLConnection.getInputStream();

                try {
                    XmlPullParser parser = Xml.newPullParser();
                    parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
                    parser.setInput(in, null);

                    while ((parser.getEventType()) != XmlPullParser.END_DOCUMENT) {
                        if (parser.getEventType() == XmlPullParser.START_TAG) {
                            if (parser.getName().equals("temperature")) {
                                currentTemp = parser.getAttributeValue(null, "value");
                                publishProgress(25);
                                min = parser.getAttributeValue(null, "min");
                                publishProgress(50);
                                max = parser.getAttributeValue(null, "max");
                                publishProgress(75);
                            } else if (parser.getName().equals("weather")) {
                                String iconName = parser.getAttributeValue(null, "icon");
                                String fileName = iconName + ".png";
                                String iconUrl = "https://openweathermap.org/img/w/" + fileName;

                                Log.i(ACTIVITY_NAME, "Looking for file: " + fileName);

                                if (fileExistance(fileName)) {
                                    FileInputStream fis = null;
                                    try {
                                        fis = openFileInput(fileName);

                                    } catch (FileNotFoundException e) {
                                        e.printStackTrace();
                                    }
                                    Log.i(ACTIVITY_NAME, "Found the file locally");
                                    weatherPic = BitmapFactory.decodeStream(fis);
                                } else {
                                    weatherPic = getImage(new URL(iconUrl));

                                    FileOutputStream outputStream = openFileOutput(fileName, Context.MODE_PRIVATE);
                                    weatherPic.compress(Bitmap.CompressFormat.PNG, 80, outputStream);
                                    Log.i(ACTIVITY_NAME, "Downloaded the file from the Internet");
                                    outputStream.flush();
                                    outputStream.close();
                                }
                                publishProgress(100);
                            }
                        }
                        parser.next();
                    }
                } finally {
                    in.close();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }

                return null;
        }
        public boolean fileExistance(String fname){
            File file = getBaseContext().getFileStreamPath(fname);
            return file.exists();
        }

        public Bitmap getImage(URL url) {
            HttpURLConnection connection = null;
            try {
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                int responseCode = connection.getResponseCode();
                if (responseCode == 200) {
                    return BitmapFactory.decodeStream(connection.getInputStream());
                } else
                    return null;
            } catch (Exception e) {
                return null;
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }
        }

        protected void onProgressUpdate(Integer... values){
            pgb.setVisibility(View.VISIBLE);
            pgb.setProgress(values[0]);
        }

        protected void onPostExecute(String a){
            pgb.setVisibility(View.INVISIBLE);
            img.setImageBitmap(weatherPic);
            currentTempView.setText(currentTemp + "C\u00b0");
            minTempView.setText(min + "C\u00b0");
            maxTempView.setText(max + "C\u00b0");
        }
    }
}