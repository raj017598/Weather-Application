package com.example.weather;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.weather.Common.Common;
import com.example.weather.Helper.Helper;
import com.example.weather.Model.OpenWeatherMap;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;


public class MainActivity extends Activity {

    TextView txtCity, txtLastUpdate, txtDescription, txtHumidity, txtTime, txtCelsius;
    TextView imageView, txtTempMax, txtTempMin, txtPressure, txtWind;
    Button refresh, enter;
    EditText city;
    OpenWeatherMap openweathermap;
    LinearLayout layout, layout0, firstlayer;
    int MY_PERMISSION;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        openweathermap = new OpenWeatherMap();
        MY_PERMISSION = 0;
        //Control
        txtCity = findViewById(R.id.txtCity);
        txtLastUpdate = findViewById(R.id.txtLastUpdate);
        txtDescription = findViewById(R.id.txtDescription);
        txtHumidity = findViewById(R.id.txtHumidity);
        txtTime = findViewById(R.id.txtTime);
        txtCelsius = findViewById(R.id.txtCelsius);
        txtTempMax = findViewById(R.id.txtTempMax);
        txtTempMin = findViewById(R.id.txtTempMin);
        imageView = findViewById(R.id.imageView);
        txtPressure = findViewById(R.id.txtPressure);
        txtWind = findViewById(R.id.txtwind);
        layout = findViewById(R.id.layout);
        layout.setVisibility(View.GONE);
        layout0 = findViewById(R.id.layout0);
        layout0.setVisibility(View.GONE);
        firstlayer = findViewById(R.id.firstlayer);
        firstlayer.setVisibility(View.GONE);
        refresh = findViewById(R.id.refresh);
        refresh.setVisibility(View.GONE);
        city = findViewById(R.id.city);
        enter = findViewById(R.id.enter);
        enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtCity.setText(city.getText().toString());
                city.setText(null);
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                String loc = txtCity.getText().toString();
                new GetWeather().execute(Common.apiRequest(loc));
            }
        });

        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String loc = txtCity.getText().toString();
                new GetWeather().execute(Common.apiRequest(loc));
            }
        });
    }
    private class GetWeather extends  AsyncTask<String,Void,String>
    {
        ProgressDialog progressDialog = new ProgressDialog(MainActivity.this);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setMessage("Please Wait...");
            progressDialog.show();
        }

        @SuppressLint("WrongThread")
        @Override
        protected String doInBackground(String... strings) {
            String stream = null;
            String urlString = "https://api.openweathermap.org/data/2.5/weather?q="+txtCity.getText().toString()+"&appid=d42d9bef55483c183e2f271b459d6f26&units=metric&mode=json";

            Helper http = new Helper();
            stream = http.getHTTPData(urlString);
            return  stream;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Gson gson = new Gson();
            Type mtype = new TypeToken<OpenWeatherMap>(){}.getType();
            openweathermap = gson.fromJson(s, mtype);
            progressDialog.dismiss();

            try {
                    layout.setVisibility(View.VISIBLE);
                    layout0.setVisibility(View.VISIBLE);
                    firstlayer.setVisibility(View.VISIBLE);
                    txtCity.setText(String.format("%s,%s",openweathermap.getName(),openweathermap.getSys().getCountry()));
                    txtLastUpdate.setText(String.format("Last Updated: %s", Common.getDateNow()));
                    txtDescription.setText(String.format("%s",openweathermap.getWeather().get(0).getDescription()));
                    txtHumidity.setCompoundDrawablesWithIntrinsicBounds(0,R.drawable.humidity,0,0);
                    txtHumidity.setText(String.format("Humidity \n %d%%",openweathermap.getMain().getHumidity()));
                    txtTime.setText(String.format("%s/%s",Common.unixTimeStampToDateTime(openweathermap.getSys().getSunrise()),Common.unixTimeStampToDateTime(openweathermap.getSys().getSunset())));
                    txtCelsius.setText(String.format("%.0f°",openweathermap.getMain().getTemp()));
                    txtTempMax.setText(String.format("%.0f°",openweathermap.getMain().getTemp_max()));
                    txtTempMin.setText(String.format("%.0f°",openweathermap.getMain().getTemp_min()));
                    txtPressure.setCompoundDrawablesWithIntrinsicBounds(0,R.drawable.pressure,0,0);
                    txtPressure.setText(String.format("Pressure \n %.0f",openweathermap.getMain().getPressure()));
                    txtWind.setCompoundDrawablesWithIntrinsicBounds(0,R.drawable.wind,0,0);
                    txtWind.setText(String.format("Wind \n %.0f km/h",openweathermap.getWind().getSpeed()));
                    refresh.setVisibility(View.VISIBLE);
                    String icon = openweathermap.getWeather().get(0).getIcon();
                    if("01d".equals(icon) || "01n".equals(icon))
                    {
                        imageView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.clearsky,0,0,0);
                        imageView.setText("Clear");
                    }
                    else if("02d".equals(icon) || "02n".equals(icon))
                    {
                        imageView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.fewclouds,0,0,0);
                        imageView.setText("Few Clouds");
                    }
                    else if("03d".equals(icon) || "03n".equals(icon))
                    {
                        imageView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.scatteredcloud,0,0,0);
                        imageView.setText("Scattered Clouds");
                    }
                    else if("04d".equals(icon) || "04n".equals(icon))
                    {
                        imageView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.brokencloud,0,0,0);
                        imageView.setText("Broken Clouds");
                    }
                    else if("09d".equals(icon) || "09n".equals(icon))
                    {
                        imageView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.showerrain,0,0,0);
                        imageView.setText("Shower Rain");
                    }
                    else if("10d".equals(icon) || "10n".equals(icon))
                    {
                        imageView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.rain,0,0,0);
                        imageView.setText("Rain");
                    }
                    else if("11d".equals(icon) || "11n".equals(icon))
                    {
                        imageView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.thunderstorm,0,0,0);
                        imageView.setText("Thunder Storm");
                    }
                    else if("13d".equals(icon) || "13n".equals(icon))
                    {
                        imageView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.snow,0,0,0);
                        imageView.setText("Snow");
                    }
                    else if("50d".equals(icon) || "50n".equals(icon))
                    {
                        imageView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.mist,0,0,0);
                        imageView.setText("Mist");
                    }
                }
                catch (Exception e)
                {
                    txtCity.setText("");
                    layout.setVisibility(View.GONE);
                    layout0.setVisibility(View.GONE);
                    firstlayer.setVisibility(View.GONE);
                    refresh.setVisibility(View.GONE);
                    Toast.makeText(MainActivity.this,"Error: City Not Found",Toast.LENGTH_SHORT).show();
            }
        }
    }
}
