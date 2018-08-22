package com.lebanmohamed.stormy.ui;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.lebanmohamed.stormy.R;
import com.lebanmohamed.stormy.databinding.ActivityMainBinding;
import com.lebanmohamed.stormy.weather.Current;
import com.lebanmohamed.stormy.weather.Forecast;
import com.lebanmohamed.stormy.weather.Hour;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "TAG";
    Forecast forecast;
    double latitude = 53.631611;
    double longitude = -113.323975;

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

        String apiKey = "23e4048bbc3f485cd62e9933f3c93e6c";



        //Getting the String URL
        String URL = "https://api.darksky.net/forecast/" + apiKey + "/" + latitude + "," + longitude;

        if(isNetworkAvailable()) {

            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder().url(URL).build();
            Call call = client.newCall(request);

            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e)
                {
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

                           runOnUiThread(new Runnable()
                           {
                               @Override
                               public void run()
                                {
                                    Drawable drawable = ResourcesCompat.getDrawable(getResources(), current.getIconByID(), null);

                                    iconImageView.setImageDrawable(drawable);

                                }
                            });


                        }

                    } catch (IOException e)
                    {
                        alertUserError();

                    } catch (JSONException e) {
                        alertUserError();

                    }


                }
            });
        }
    }

    private Forecast parseForecastData(String jsonData) throws JSONException
    {
        Forecast forecast = new Forecast();

        forecast.setCurrent(getCurrentDetails(jsonData));

        return forecast;
    }

    private Hour[] getHourlyForecast(String JSONData) throws JSONException {
        JSONObject forecast = new JSONObject(JSONData);
        String timezone = forecast.getString("timezone");

        JSONObject hourly = forecast.getJSONObject("hourly");
        JSONArray data = forecast.getJSONArray("data");

        Hour[] hours = new Hour[data.length()];

        for(int i = 0; i < data.length(); i++)
        {
            JSONObject jsonHour = data.getJSONObject(i);

            Hour hour = new Hour();

            hour.setSummary(jsonHour.getString("summay"));
            hour.setTime(jsonHour.getLong("time"));
            hour.setTemperature(jsonHour.getDouble("temperature"));
            hour.setTimezone(timezone);

            hours[i] = hour;
        }

        return hours;

    }

    private Current getCurrentDetails(String jsonData) throws JSONException {
        JSONObject forecast = new JSONObject(jsonData);

        String timezone = forecast.getString("timezone");
        Log.i(TAG, "JSON Data: " + jsonData);

        JSONObject currently = forecast.getJSONObject("currently");
        Current current = new Current();

        current.setHumidity(currently.getDouble("humidity") * 100);
        current.setTime(currently.getLong("time"));
        current.setIcon(currently.getString("icon"));
        current.setLocationLabel("Edmonton, AB");
        current.setPrecipChance(currently.getDouble("precipProbability") * 100);
        current.setSummary(currently.getString("summary"));
        current.setTemperature(Math.round(currently.getDouble("temperature")));
        current.setTimezone(timezone);

        return current;

    }

    private boolean isNetworkAvailable()
    {
        ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();

        boolean isConnected = false;

        if(networkInfo != null && networkInfo.isConnected())
        {
            return true;
        }

        else
        {
            Toast.makeText(this, R.string.network_not_available_message, Toast.LENGTH_SHORT).show();
        }

        return isConnected;
    }

    private void alertUserError()
    {
        AlertDialogFragment dialog = new AlertDialogFragment();
        dialog.show(getFragmentManager(), "error_dialog");


    }

    public void refreshOnClick(View view)
    {
        Toast.makeText(this, "Refreshing Info...", Toast.LENGTH_SHORT).show();
        getForecast(latitude, longitude);
        Toast.makeText(this, "Done!", Toast.LENGTH_SHORT).show();

    }
}
