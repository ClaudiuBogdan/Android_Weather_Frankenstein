package me.claudiuconstantinbogdan.weatherapp.data;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class DailyWeatherData implements Parcelable {
    private String summary;
    private List<DailyItemWeatherData> data;


    protected DailyWeatherData(Parcel in) {
        summary = in.readString();
        data = in.createTypedArrayList(DailyItemWeatherData.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(summary);
        dest.writeTypedList(data);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<DailyWeatherData> CREATOR = new Creator<DailyWeatherData>() {
        @Override
        public DailyWeatherData createFromParcel(Parcel in) {
            return new DailyWeatherData(in);
        }

        @Override
        public DailyWeatherData[] newArray(int size) {
            return new DailyWeatherData[size];
        }
    };

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public List<DailyItemWeatherData> getData() {
        return data;
    }

    public void setData(List<DailyItemWeatherData> data) {
        this.data = data;
    }
}
