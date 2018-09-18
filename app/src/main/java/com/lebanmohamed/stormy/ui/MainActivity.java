package com.lebanmohamed.stormy.ui;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.graphics.drawable.Drawable;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.lebanmohamed.stormy.R;
import com.lebanmohamed.stormy.databinding.ActivityMainBinding;
import com.lebanmohamed.stormy.weather.Current;
import com.lebanmohamed.stormy.weather.Day;
import com.lebanmohamed.stormy.weather.Forecast;
import com.lebanmohamed.stormy.weather.Hour;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity implements LocationListener, GoogleApiClient.ConnectionCallbacks {

    private static final int MY_PERMISSION_REQUEST_FINE_LOCATION = 101;
    private static final int MY_PERMISSION_REQUEST_COARSE_LOCATION = 102;


    private FusedLocationProviderClient mFusedLocationClient;
    private LocationManager locationManager;
    public LocationRequest locationRequest;
    public GoogleApiClient apiClient;
    private Task<Location> currentLocation;


    private static final String TAG = "TAG";
    public static final int PRIORITY_BALANCED_POWER_ACCURACY = 102;
    private boolean permission = false;
    private Forecast forecast;
    private double latitude = 53.631611;
    private double longitude = -113.323975;
    boolean toCelsius = true;

    String URL;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);


        getForecast(latitude, longitude);
    }




    private void getForecast(double latitude, double longitude) {
        final ActivityMainBinding binding = DataBindingUtil.setContentView(MainActivity.this, R.layout.activity_main);


        final ImageView iconImageView;

        //Setting the Link and activating it.
        TextView darkSkyAttribution = findViewById(R.id.darkSkyAttribution);
        darkSkyAttribution.setMovementMethod(LinkMovementMethod.getInstance());

        iconImageView = findViewById(R.id.iconImageView);


        String apiKey = "23e4048bbc3f485cd62e9933f3c93e6c?units=si";


        //Getting the String URL
        URL = "https://api.darksky.net/forecast/" + apiKey + "/" + latitude + "," + longitude;

        if (!toCelsius) {
            URL += "23e4048bbc3f485cd62e9933f3c93e6c";
        }


        if (isNetworkAvailable()) {

            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder().url(URL).build();
            Call call = client.newCall(request);

            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    e.printStackTrace();


                }

                @Override
                public void onResponse(Call call, Response response) {
                    try {
                        String JSONdata = response.body().string();
                        if (response.isSuccessful()) {
                            forecast = parseForecastData(JSONdata);

                            final Current current = forecast.getCurrent();


                            final Current displayWeather = new Current
                                    (
                                            current.getLocationLabel(),
                                            current.getIcon(),
                                            current.getTime(),
                                            current.getTemperature(),
                                            current.getHumidity(),
                                            current.getPrecipChance(),
                                            current.getSummary(),
                                            current.getTimezone()
                                    );

                            binding.setWeather(current);
                            iconImageView.setImageDrawable(ResourcesCompat.getDrawable(getResources(), displayWeather.getIconByID(), null));

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Drawable drawable = ResourcesCompat.getDrawable(getResources(), current.getIconByID(), null);

                                    iconImageView.setImageDrawable(drawable);

                                }
                            });


                        }

                    } catch (IOException e) {
                        alertUserError();

                    } catch (JSONException e) {
                        JSONUserERROR();

                    }


                }
            });
        }
    }

    private Forecast parseForecastData(String jsonData) throws JSONException {
        Forecast forecast = new Forecast();

        forecast.setCurrent(getCurrentDetails(jsonData));
        forecast.setHourlyForecast(getHourlyForecast(jsonData));
        forecast.setDailyForecast(getDailyForecast(jsonData));


        return forecast;
    }

    private Hour[] getHourlyForecast(String JSONData) throws JSONException {
        JSONObject forecast = new JSONObject(JSONData);
        String timezone = forecast.getString("timezone");

        JSONObject hourly = forecast.getJSONObject("hourly");
        JSONArray data = hourly.getJSONArray("data");

        Hour[] hours = new Hour[data.length()];

        for (int i = 0; i < data.length(); i++) {
            JSONObject jsonHour = data.getJSONObject(i);

            Hour hour = new Hour();

            hour.setSummary(jsonHour.getString("summary"));
            hour.setTime(jsonHour.getLong("time"));
            hour.setTemperature(Math.round(jsonHour.getDouble("temperature")));
            hour.setIcon(jsonHour.getString("icon"));
            hour.setTimezone(timezone);

            hours[i] = hour;
        }

        return hours;

    }


    private JSONObject createJSONObject(String JSONData) throws JSONException {
        return new JSONObject(JSONData);
    }

    private Day[] getDailyForecast(String JSONData) throws JSONException {
        JSONObject forecast = createJSONObject(JSONData);
        String timeZone = forecast.getString("timezone");

        JSONObject daily = forecast.getJSONObject("daily");
        JSONArray data = daily.getJSONArray("data");

        Day[] days = new Day[data.length()];

        for (int i = 0; i < data.length(); i++) {
            JSONObject jsonDay = data.getJSONObject(i);

            Day day = new Day();

            day.setSummary(jsonDay.getString("summary"));
            day.setTime(jsonDay.getLong("time"));
            day.setIcon(jsonDay.getString("icon"));
            day.setTemperature(jsonDay.getDouble("temperatureMin") + jsonDay.getDouble("temperatureMax") / 2);
            day.setTimezone(timeZone);

            days[i] = day;
        }

        return days;
    }

    private Current getCurrentDetails(String jsonData) throws JSONException {
        JSONObject forecast = new JSONObject(jsonData);

        String timezone = forecast.getString("timezone");
        Log.i(TAG, "JSON Data: " + jsonData);

        JSONObject currently = forecast.getJSONObject("currently");
        Current current = new Current();

        current.setHumidity(currently.getDouble("humidity") / 100);
        current.setTime(currently.getLong("time"));
        current.setIcon(currently.getString("icon"));
        current.setLocationLabel("Edmonton, AB");
        current.setPrecipChance(currently.getDouble("precipProbability") / 100);
        current.setSummary(currently.getString("summary"));
        current.setTemperature(Math.round(currently.getDouble("temperature")));
        current.setTimezone(timezone);

        return current;

    }

    private boolean isNetworkAvailable() {
        ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();

        boolean isConnected = false;

        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        } else {
            Toast.makeText(this, R.string.network_not_available_message, Toast.LENGTH_SHORT).show();
        }

        return isConnected;
    }

    protected void M() {

    }

    private void alertUserError() {
        AlertDialogFragment dialog = new AlertDialogFragment();
        dialog.show(getFragmentManager(), "error_dialog");


    }

    private void JSONUserERROR() {
        AlertDialogFragment dialog = new AlertDialogFragment();
        dialog.show(getFragmentManager(), "We've got a JSON exception over here! Give a shout to the us, we'll take care of the rest.");
    }

    public void refreshOnClick(View view) {
        getForecast(latitude, longitude);
    }

    public void hourlyOnClick(View view) {
        List<Hour> hours = Arrays.asList(forecast.getHourlyForecast());
        Intent intent = new Intent(this, HourlyForecastActivity.class);
        intent.putExtra("hourlyList", (Serializable) hours);
        startActivity(intent);
    }

    public void dailyOnClick(View view) {
        List<Day> days = Arrays.asList(forecast.getDailyForecast());
        Intent intent = new Intent(this, DailyForecastActivity.class);
        intent.putExtra("dailyList", (Serializable) days);
        startActivity(intent);
    }

    public void convertOnClick(View view) {
        if (!toCelsius) {
            toCelsius = true;
            getForecast(latitude, longitude);
            Toast.makeText(this, "Converted to Celsius", Toast.LENGTH_SHORT);
        } else {
            toCelsius = false;
            getForecast(latitude, longitude);
            Toast.makeText(this, "Converted to Farenheit", Toast.LENGTH_SHORT);
        }

    }




    public void buildNoGPSAlertMessage() {
        AlertDialogFragment dialog = new AlertDialogFragment();
        dialog.show(getFragmentManager(), "We don't have GPS permission to use this application");

    }


    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider)
    {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Geocoder gcd = new Geocoder(this, Locale.getDefault());
        if (Build.VERSION.SDK_INT > 23)
        {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.INTERNET}, 101);
                return;
            }
            currentLocation = mFusedLocationClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if(location != null)
                    {
                        longitude = location.getLongitude();
                        latitude = location.getLatitude();
                        getForecast(latitude, longitude);
                    }

                    else
                    {
                        Toast.makeText(MainActivity.this, "Sorry, we couldn't get the location.", Toast.LENGTH_LONG).show();
                        latitude = 53.631611;
                        longitude = -113.323975;
                    }
                }
            });
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }
}




