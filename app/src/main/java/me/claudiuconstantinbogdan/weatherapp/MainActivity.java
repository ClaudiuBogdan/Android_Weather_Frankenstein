package me.claudiuconstantinbogdan.weatherapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.gson.Gson;

import java.io.IOException;

import me.claudiuconstantinbogdan.weatherapp.data.WeatherData;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        testOkHTTP();
    }

    private void testOkHTTP(){
        try{
            run("https://api.darksky.net/forecast/2bb07c3bece89caf533ac9a5d23d8417/59.337239,18.062381");
        }
        catch (IOException ex){}

    }

    OkHttpClient client = new OkHttpClient();
    Gson gson = new Gson();
    private void  run(String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("OKHTTP", "Error: " + e.toString());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String weatherJsonData = response.body().string();
                WeatherData weatherData = gson.fromJson(weatherJsonData, WeatherData.class);
                Log.d("OKHTTP", gson.toJson(weatherData));
                Log.d("OKHTTP", response.toString());
            }
        });
    }
}
