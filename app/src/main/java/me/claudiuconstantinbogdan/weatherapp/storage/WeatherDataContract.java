package me.claudiuconstantinbogdan.weatherapp.storage;


import android.provider.BaseColumns;

public final class WeatherDataContract {
    private WeatherDataContract(){}

    public static class WeatherDataEntry implements BaseColumns {
        public static final String TABLE_NAME = "weatherdata";
        public static final String COLUMN_NAME_ROW_DATA = "rowdata";
    }
}
