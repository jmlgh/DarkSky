package com.example.javi.darksky.weather;

public class Forecast {
    private Current mCurrent;
    private Hour[] mHourlyForecast;
    private Day[] mDaylyForecast;

    public Forecast() {}

    public Current getmCurrent() {
        return mCurrent;
    }

    public void setmCurrent(Current mCurrent) {
        this.mCurrent = mCurrent;
    }

    public Hour[] getmHourlyForecast() {
        return mHourlyForecast;
    }

    public void setmHourlyForecast(Hour[] mHourlyForecast) {
        this.mHourlyForecast = mHourlyForecast;
    }

    public Day[] getmDaylyForecast() {
        return mDaylyForecast;
    }

    public void setmDaylyForecast(Day[] mDaylyForecast) {
        this.mDaylyForecast = mDaylyForecast;
    }
}
