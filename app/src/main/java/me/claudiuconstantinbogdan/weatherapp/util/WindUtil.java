package me.claudiuconstantinbogdan.weatherapp.util;

public abstract class WindUtil {
    public static String getWindDirection(double windDegrees){
        String[] cardinals = new String[]{ "North", "North-East", "East", "South-East", "South", "South-West", "West", "North-West", "North" };
        return cardinals[(int)Math.round((windDegrees % 360) / 45)];
    }

    public static String getWindSpeedInMetricUnits(double windSpeedInMph) {
        double conversionFactor = 1.60934;
        return Math.round(windSpeedInMph * conversionFactor) + " km/h";
    }
}
