package me.claudiuconstantinbogdan.weatherapp.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import me.claudiuconstantinbogdan.weatherapp.R;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}
