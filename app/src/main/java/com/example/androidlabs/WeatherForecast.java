package com.example.androidlabs;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.graphics.BitmapFactory;
import android.content.Context;
import android.util.Log;
import android.widget.Button;
import android.widget.ProgressBar;
import android.view.View;
import android.widget.TextView;

import org.json.JSONException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import 	android.widget.ImageView;
import 	java.io.BufferedInputStream;

import static org.xmlpull.v1.XmlPullParser.END_TAG;
import static org.xmlpull.v1.XmlPullParser.START_TAG;
import static org.xmlpull.v1.XmlPullParser.TEXT;


public class WeatherForecast extends AppCompatActivity {

    TextView tempCorrent, tempMin, tempMax, UVvalue;
    ProgressBar progressBar;
    ImageView myImage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_forecast);
        tempCorrent = findViewById(R.id.currentTemp);
        tempMin = findViewById(R.id.minTemp);
        tempMax = findViewById(R.id.maxTemp);
        UVvalue = findViewById(R.id.UVRating);
        progressBar =(ProgressBar)findViewById(R.id.progressBar);
        myImage = (ImageView) findViewById(R.id.imageView);
        doQuery();
    }

    private void doQuery() {
        ForecastQuery theQuery = new ForecastQuery();
        theQuery.execute( );
    }

    private class ForecastQuery extends AsyncTask<String, Integer, String> {
        String windUV = null;
        String minTemp = null;
        String maxTemp = null;
        String currentTemp = null;
        Bitmap imageWeather = null ;
        String uvURL="http://api.openweathermap.org/data/2.5/uvi?appid=7e943c97096a9784391a981c4d878b22&lat=45.348945&lon=-75.759389";

        public boolean fileExistance(String fName){
            File file = getBaseContext().getFileStreamPath(fName);
            return file.exists();   }
        @Override
        protected String doInBackground(String... args){

            String ret = null;
            String weatherURL = "http://api.openweathermap.org/data/2.5/weather?q=ottawa,ca&APPID=7e943c97096a9784391a981c4d878b22&mode=xml&units=metric";
            String params = "q=ottawa,ca&APPID=7e943c97096a9784391a981c4d878b22&mode=xml&units=metric";

            try {       // Connect to the server:
                URL url = new URL(weatherURL);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                InputStream inStream = urlConnection.getInputStream();

                //Set up the XML parser:
                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                factory.setNamespaceAware(false);
                XmlPullParser xpp = factory.newPullParser();
                xpp.setInput( inStream  , "UTF-8");

                //Iterate over the XML tags:
                int EVENT_TYPE;         //While not the end of the document:
                while((EVENT_TYPE = xpp.getEventType()) != XmlPullParser.END_DOCUMENT)
                {
                    switch(EVENT_TYPE)
                    {
                        case START_TAG:         //This is a start tag < ... >
                            String tagName = xpp.getName(); // What kind of tag?

                            if(tagName.equals("temperature"))
                            {
                                currentTemp = xpp.getAttributeValue(null, "value");
                                publishProgress(25);

                                minTemp = xpp.getAttributeValue(null,"min");
                                publishProgress(50);

                                maxTemp = xpp.getAttributeValue(null, "max");
                                publishProgress(75);

                            }else if (tagName.equals("weather"))
                            {
                                String iconName = xpp.getAttributeValue(null,"icon");

                                Log.i(null, "Finding image " + iconName + ".png");

                                if(fileExistance(iconName + ".png")){
                                    FileInputStream fis = null;
                                    try {fis = openFileInput(iconName + ".png");   }
                                    catch (FileNotFoundException e) {    e.printStackTrace();  }
                                    imageWeather = BitmapFactory.decodeStream(fis);
                                    Log.i(null, "Found image " + iconName + ".png from local");

                                }else {
                                    URL urlImage = new URL("http://openweathermap.org/img/w/" + iconName + ".png");
                                    HttpURLConnection connection = (HttpURLConnection) urlImage.openConnection();
                                    connection.connect();
                                    int responseCode = connection.getResponseCode();
                                    if (responseCode == 200) {
                                        InputStream is = connection.getInputStream();
                                        imageWeather = BitmapFactory.decodeStream(is);
                                    }
                                    File file = new File(iconName + ".png");
                                    FileOutputStream outputStream = openFileOutput( iconName + ".png", Context.MODE_PRIVATE);
                                    //OutputStream outputStream = new FileOutputStream(file);

                                    imageWeather.compress(Bitmap.CompressFormat.PNG, 80, outputStream);
                                    outputStream.flush();
                                    outputStream.close();
                                    Log.i(null, "Found image " + iconName + ".png from URL, and download it.");
                                }
                                publishProgress(100);

                            }
                            break;
                        case END_TAG:           //This is an end tag: </ ... >
                            break;
                        case TEXT:              //This is text between tags < ... > Hello world </ ... >
                            break;
                    }
                    xpp.next(); // move the pointer to next XML element
                }

                      // Connect to the server:
                    URL url2 = new URL(uvURL);
                    HttpURLConnection uvUrlConnection = (HttpURLConnection) url2.openConnection();

                    InputStream inStream2 = uvUrlConnection.getInputStream();
                     //Set up the XML parser:
                    BufferedReader reader = new BufferedReader(new InputStreamReader(inStream2, "UTF-8"), 8);
                    StringBuilder sb = new StringBuilder();

                    String line = null;
                    while ((line = reader.readLine()) != null)
                    {
                        sb.append(line + "\n");
                    }
                    String result = sb.toString();

                    JSONObject jObject = new JSONObject(result);
                    double value = jObject.getDouble("value");
                    windUV = String.valueOf(value);
            }
            catch(MalformedURLException mfe){ ret = "Malformed URL exception"; }
            catch(IOException ioe)          { ret = "IO Exception. Is the Wifi connected?";}
            catch(XmlPullParserException pe){ ret = "XML Pull exception. The XML is not properly formed" ;}
            catch(JSONException JSONeX){ret = "Json Exception. The Json is not properly formed";}
            //What is returned here will be passed as a parameter to onPostExecute:
            return ret;
        }

        @Override                       //Type 2
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            //Update GUI stuff only:
            try {
                progressBar.setVisibility(View.VISIBLE);
                progressBar.setProgress(values[0]);
                Thread.sleep(1000);
            }catch (Exception e) {}
        }

        @Override                   //Type 3
        protected void onPostExecute(String sentFromDoInBackground) {
            super.onPostExecute(sentFromDoInBackground);
            //update GUI Stuff:
            tempCorrent.setText("Corrent Temperature:"+currentTemp);
            tempMin.setText("Min Temperature:"+minTemp);
            tempMax.setText("Max Temperature:"+maxTemp);
            UVvalue.setText("UV Value:"+windUV);
            myImage.setImageBitmap(imageWeather);
            progressBar.setVisibility(View.INVISIBLE );
        }

        public ForecastQuery()
        {
        }
    }
}
