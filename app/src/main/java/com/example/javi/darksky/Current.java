package com.example.javi.darksky;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class Current {
    private String icon;
    private long time;
    private double temperature;
    private double humidty;
    private double precipChance;
    private String summary;
    private String timeZone;

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public double getHumidty() {
        return humidty;
    }

    public void setHumidty(double humidty) {
        this.humidty = humidty;
    }

    public double getPrecipChance() {
        return precipChance;
    }

    public void setPrecipChance(double precipChance) {
        this.precipChance = precipChance;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }

    public String getFormattedTime(){
        // seleccion del formato deseado
        SimpleDateFormat formatter = new SimpleDateFormat("h:mm a");

        // determina la hora segun la zona horaria
        formatter.setTimeZone(TimeZone.getTimeZone(getTimeZone()));

        // consigue la hora usando Date. x1000 porque se encuentra en ms
        Date dateTime = new Date(getTime() * 1000);

        // almacena la hora con el formato indicado
        String timeString = formatter.format(dateTime);
        return timeString;
    }

    public int getIconId(){
        // consigue un id cualquiera que nos sirva para comparar
        int iconId = R.drawable.clear_day;

        // asigna el icono segund el Id
        if(icon.equals("clear_day")){
            iconId = R.drawable.clear_day;
        } else if(icon.equals("rain")){
            iconId = R.drawable.rain;
        } else if (icon.equals("snow")) {
            iconId = R.drawable.snow;
        }
        else if (icon.equals("sleet")) {
            iconId = R.drawable.sleet;
        }
        else if (icon.equals("wind")) {
            iconId = R.drawable.wind;
        }
        else if (icon.equals("fog")) {
            iconId = R.drawable.fog;
        }
        else if (icon.equals("cloudy")) {
            iconId = R.drawable.cloudy;
        }
        else if (icon.equals("partly-cloudy-day")) {
            iconId = R.drawable.partly_cloudy;
        }
        else if (icon.equals("partly-cloudy-night")) {
            iconId = R.drawable.cloudy_night;
        }

        return iconId;
    }
}
