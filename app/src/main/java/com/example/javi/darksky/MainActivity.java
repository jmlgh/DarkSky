package com.example.javi.darksky;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.UiThread;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    String userKey = "9ef5b773c3879fa3466a00131662eaa5";
    String lat = "40.5358";
    String lon = "-3.61661";
    String baseRequest = "https://api.darksky.net/forecast/";
    String forecastURL = "";

    // elementos de layout
    TextView tempLabel, timeLabel, locationLabel, humidityValue, precipValue,sumaryLabel;
    ImageView iconImage, refreshImage;
    ProgressBar refreshProgress;

    // usado para la comunicacion entre la app y la api
    OkHttpClient cliente;

    Current current;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // elem layout init
        iconImage = (ImageView)findViewById(R.id.iconImageView);
        tempLabel = (TextView)findViewById(R.id.temperatureLabel);
        timeLabel = (TextView)findViewById(R.id.timeLabel);
        locationLabel = (TextView)findViewById(R.id.locationLabel);
        humidityValue = (TextView)findViewById(R.id.humidityValue);
        precipValue = (TextView)findViewById(R.id.precipValue);
        sumaryLabel = (TextView)findViewById(R.id.textView);
        refreshImage = (ImageView)findViewById(R.id.refreshImageView);
        refreshImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                obtenerForecast();
            }
        });

        forecastURL = baseRequest + userKey + "/" + lat + "," + lon;
        refreshProgress = (ProgressBar)findViewById(R.id.refreshProgressBar);
        refreshProgress.setVisibility(View.INVISIBLE);
        obtenerForecast();

    }

    private void cargarDatos() {
        tempLabel.setText(current.getTemperature()+"");
        humidityValue.setText(current.getHumidty()+"%");
        precipValue.setText(current.getPrecipChance()+"%");

        // crea una imagen Drawable a partir de Drawable resource
        Drawable imgWeather = getResources().getDrawable(current.getIconId());
        iconImage.setImageDrawable(imgWeather);

        timeLabel.setText(current.getFormattedTime());
        sumaryLabel.setText(current.getSummary());
        locationLabel.setText(current.getTimeZone());

    }

    private Current getCurrentDetails(String jsonData) throws JSONException{
        // forecast almacena el objeto raiz del archivo JSON
        JSONObject forecast = new JSONObject(jsonData);

        // recupera timezone de la raiz
        String timeZone = forecast.getString("timezone");

        // almacena el objeto JSON currently usando un nuevo JSONObject que recuperaremos de la raiz
        JSONObject currently = forecast.getJSONObject("currently");

        // creamos un objeto Current que almacenara los datos que hemos definido
        Current currentWeather = new Current();
        // recupera los datos, esta vez no de la raiz si no del objeto JSON currently
        currentWeather.setHumidty(currently.getDouble("humidity"));
        currentWeather.setTime(currently.getLong("time"));
        currentWeather.setSummary(currently.getString("summary"));
        currentWeather.setTemperature(currently.getDouble("temperature"));
        currentWeather.setPrecipChance(currently.getDouble("precipProbability"));
        currentWeather.setIcon(currently.getString("icon"));
        currentWeather.setTimeZone(timeZone);

        // logea los resultados
        Log.i(TAG, "Obtendo desde JSON: " + timeZone);
        Log.i(TAG, "Hora formateada: " + currentWeather.getFormattedTime());
        return currentWeather;
    }

    private boolean hayInternetDisponible() {
        boolean disponible = false;
        ConnectivityManager conManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = conManager.getActiveNetworkInfo();

        // si es diferente de null y esta conectado --> hay internet
        if(netInfo != null && netInfo.isConnected()){
            disponible = true;
        }

        return disponible;
    }

    private void mostrarResponseError() {
        AlertDialogFragment alerta = new AlertDialogFragment();
        alerta.show(getFragmentManager(), "Error: ");
    }

    public void obtenerForecast(){

        refreshImage.setVisibility(View.INVISIBLE);
        refreshProgress.setVisibility(View.VISIBLE);
        if(hayInternetDisponible()){
            // establece la conexion
            cliente = new OkHttpClient();

            // crea la peticion que se lanzara a la web
            Request request = new Request.Builder().url(forecastURL).build();

            // usa la peticion en el llamador (caller)
            Call call = cliente.newCall(request);
            // lo pone en la cola para que no haya conflicto
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            refreshImage.setVisibility(View.VISIBLE);
                            refreshProgress.setVisibility(View.INVISIBLE);
                        }
                    });
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    /*refreshImage.setVisibility(View.VISIBLE);
                    refreshProgress.setVisibility(View.INVISIBLE);*/// --> vuelve a dar error, usa un UIThread para "obligar" a ejecutarse aqui
                    // ejecuta la llamada y la recoge
                    try {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                refreshImage.setVisibility(View.VISIBLE);
                                refreshProgress.setVisibility(View.INVISIBLE);
                            }
                        });
                        //Response response = call.execute(); --> incorrecto, internet debe ir en un hilo aparte, de ahi el enqueue
                        String jsonData = response.body().string();
                        Log.i(TAG, jsonData);
                        if(response.isSuccessful()){
                            // si existe respuesta satisfactoria, empieza a recuperar datos
                            current = getCurrentDetails(jsonData);
                            //cargarDatos(); --> las tareas del interfaz no se pueden hacer en el callback, se deben hacer en el hilo principal
                            /*Existe un método llamado runUiThread() que nos permite “obligar” a la aplicación a ejecutar
                            fragmentos del código en el hilo principal.*/
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    cargarDatos();
                                }
                            });

                        } else{
                            // si algo no va bien muestra al usuario un error
                            mostrarResponseError();
                        }
                    } catch (IOException e) {
                        Log.e(TAG, "Excepcion: ", e);
                    } catch (JSONException e){
                        Log.e(TAG, "Excepcion: ", e);
                    }
                }
            });
            // si no hay internet
        } else {
            Toast.makeText(getBaseContext(), "Error de conexion", Toast.LENGTH_SHORT).show();
        }
    }
}
