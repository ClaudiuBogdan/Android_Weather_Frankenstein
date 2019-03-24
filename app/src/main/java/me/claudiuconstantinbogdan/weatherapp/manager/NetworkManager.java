package me.claudiuconstantinbogdan.weatherapp.manager;

import android.support.annotation.WorkerThread;

import com.google.gson.Gson;

import java.io.IOException;

import me.claudiuconstantinbogdan.weatherapp.network.WeatherService;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class NetworkManager implements WeatherService{

    private static NetworkManager mInstance;
    private OkHttpClient client = new OkHttpClient();

    private NetworkManager(){}

    public static NetworkManager getInstance(){
        if(mInstance == null)
            mInstance = new NetworkManager();
        return mInstance;
    }

    public static WeatherService getWeatherService() {
        return NetworkManager.getInstance();
    }

    @Override
    @WorkerThread
    public String getWeatherData(double longitude, double latitude) throws IOException{
        String url = "https://api.darksky.net/forecast/2bb07c3bece89caf533ac9a5d23d8417/" + longitude +"," + latitude;
        Request request = new Request.Builder()
                .url(url)
                .build();

        Response response = client.newCall(request).execute();
        String weatherJsonData = response.body().string();
        return weatherJsonData;
    }

    private void cancelNetworkRequests(){
        //When you want to cancel:
        //A) go through the queued calls and cancel if the tag matches:
        for (Call call : client.dispatcher().queuedCalls()) {
            call.cancel();
        }

        //B) go through the running calls and cancel if the tag matches:
        for (Call call : client.dispatcher().runningCalls()) {
            call.cancel();
        }
    }

    public void destroy(){
        cancelNetworkRequests();
    }
}
