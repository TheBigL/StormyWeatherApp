package com.lebanmohamed.stormy;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "TAG";

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String apiKey = "23e4048bbc3f485cd62e9933f3c93e6c";

        double latitude = 53.631611;
        double longitude = -113.323975;

        //Getting the String URL
        String URL = "https://api.darksky.net/forecast/" + apiKey + "/" + latitude + ", " + -longitude;

        if(isNetworkAvailable()) {

            OkHttpClient client = new OkHttpClient();

            Request request = new Request.Builder().url(URL).build();
            Call call = client.newCall(request);

            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {


                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    try {
                        if (response.isSuccessful()) {
                            Log.v(TAG, response.body().string());
                        }

                    } catch (IOException e) {
                        Log.e(TAG, "Exception Caught:", e);
                        alertUserError();

                    }


                }
            });
        }




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
}
