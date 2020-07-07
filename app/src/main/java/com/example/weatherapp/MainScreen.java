package com.example.weatherapp;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.weatherapp.model.WeatherData;

import org.jetbrains.annotations.NotNull;

import java.sql.Timestamp;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class MainScreen extends Fragment {
    private ImageView imgView;
    private TextView weatherDescriptionLabel;
    private TextView lastUpdatedLabel;
    private TextView temperatureLabel;
    private TextView feelsLikeLabel;
    private TextView windDegreeLabel;
    private TextView visibilityLabel;
    private TextView pressureLabel;
    private TextView humidityLabel;
    private TextView windSpeedLabel;
    private TextView uvIndexLabel;

    public MainScreen() {
        // Required empty public constructor
    }

    /*
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MainScreen.
     *
    /*public static MainScreen newInstance(String param1, String param2) {
        MainScreen fragment = new MainScreen();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }*/

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main_screen, container, false);

        imgView = view.findViewById(R.id.conditionImage);
        weatherDescriptionLabel  = view.findViewById(R.id.weatherDescriptionLabel);
        lastUpdatedLabel  = view.findViewById(R.id.lastUpdatedLabel);
        temperatureLabel  = view.findViewById(R.id.temperatureLabel);
        feelsLikeLabel = view.findViewById(R.id.feelsLikeLabel);
        windDegreeLabel = view.findViewById(R.id.windDegreeLabel);
        visibilityLabel = view.findViewById(R.id.visibilityLabel);
        pressureLabel = view.findViewById(R.id.pressureLabel);
        humidityLabel = view.findViewById(R.id.humidityLabel);
        windSpeedLabel = view.findViewById(R.id.windSpeedLabel);
        uvIndexLabel = view.findViewById(R.id.uvIndexLabel);

        getCurrentData(Configuration.getZagrebLatitude(), Configuration.getZagrebLongitude());

        // Inflate the layout for this fragment
        return view;
    }

    private void getCurrentData(double lat, double lon) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Configuration.getBaseUrl())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        IWeatherService weatherService = retrofit.create(IWeatherService.class);
        Call<WeatherData> call = weatherService.getCurrentWeatherData(lat, lon, Configuration.getAppId());

        call.enqueue(new Callback<WeatherData>() {
            @Override
            public void onResponse(@NotNull Call<WeatherData> call, @NotNull Response<WeatherData> response) {
                Log.d("Main Screen", "onResponse: Server response: " + response.toString());
                WeatherData weatherData = response.body();
                assert weatherData != null;
                showCurrentData(weatherData);
                Log.d("JSON Data", weatherData.toString());
            }

            @Override
            public void onFailure(@NotNull Call<WeatherData> call, @NotNull Throwable t) {
                Log.e("Main Screen", "onFailure: Something went wrong. " + t.getMessage());
            }
        });
    }

    @SuppressLint("SetTextI18n")
    public void showCurrentData(WeatherData weatherData) {
        if(weatherData == null){
            weatherDescriptionLabel.setText("Weather information not available.");
            return;
        }

        loadBackground(weatherData.getCurrent().getWeather().get(0).getId());

        weatherDescriptionLabel.setText(weatherData.getCurrent().getWeather().get(0).getDescription());

        Timestamp ts = new Timestamp(System.currentTimeMillis());
        lastUpdatedLabel.setText((new Date(ts.getTime()).toString()));

        float t = weatherData.getCurrent().getTemp() - 273.0f;
        t = Math.round(t * 100.0f) / 100.0f;
        temperatureLabel.setText("Temperature: " + t + "°C");

        t = weatherData.getCurrent().getFeels_like() - 273.0f;
        t = Math.round(t * 100.f) / 100.0f;
        feelsLikeLabel.setText("Feels like: " + t + "°C");

        windDegreeLabel.setText("Wind direction: " + weatherData.getCurrent().getWind_deg() + "°");

        visibilityLabel.setText("Visibility: " + weatherData.getCurrent().getVisibility() + "m");

        pressureLabel.setText("Pressure: " + weatherData.getCurrent().getPressure() + " hPa");

        humidityLabel.setText("Humidity: " + weatherData.getCurrent().getHumidity() + "%");

        windSpeedLabel.setText("Wind speed: " + weatherData.getCurrent().getWind_speed() + " m/s");

        uvIndexLabel.setText("UV index: " + weatherData.getCurrent().getUvi());
    }

    private void loadBackground(int desc) {
        Log.d("a", "DESC " + desc);
        if (desc < 300) {
            loadImage(imgView, R.drawable.thunderstorm); //thunderstorm
        } else if (desc < 503) {
            loadImage(imgView, R.drawable.drizzle_light_moderate_rain); //drizzle, light and moderate rain
        } else if (desc >= 503 && desc < 600) {
            loadImage(imgView, R.drawable.rain); //all sorts of rain
        } else if (desc < 700) {
            loadImage(imgView, R.drawable.snow); //snow
        } else if (desc < 800) {
            loadImage(imgView, R.drawable.atmosphere); //atmosphere
        } else if (desc == 800) {
            loadImage(imgView, R.drawable.clear_sky); //clear sky
        } else if (desc == 801) {
            loadImage(imgView, R.drawable.few_clouds); //few clouds
        } else {
            loadImage(imgView, R.drawable.scattered_broken_overcast_clouds); //scattered broken or overcast clouds
        }
    }

    private void loadImage(ImageView img, int imageLocation) {
        Glide.with(this)
                .load(imageLocation)
                .placeholder(R.drawable.ic_launcher_background)
                .into(img);
    }
}