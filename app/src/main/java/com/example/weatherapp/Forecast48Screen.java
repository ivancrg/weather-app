package com.example.weatherapp;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.weatherapp.model.WeatherData;

import org.jetbrains.annotations.NotNull;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Forecast48Screen extends Fragment implements View.OnClickListener {
    private WeatherData daysWeatherData;
    private int hour = 0;
    private ImageView imgView;
    private TextView weatherDescriptionLabel;
    private TextView hourLabel;
    private TextView temperatureLabel;
    private TextView feelsLikeLabel;
    private TextView windDegreeLabel;
    private TextView cloudsLabel;
    private TextView pressureLabel;
    private TextView humidityLabel;
    private TextView windSpeedLabel;
    private TextView dewPointLabel;

    public Forecast48Screen() {
        // Required empty public constructor
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_forecast48_screen, container, false);

        imgView = view.findViewById(R.id.conditionImage);
        weatherDescriptionLabel = view.findViewById(R.id.weatherDescriptionLabel);
        TextView lastUpdatedLabel = view.findViewById(R.id.lastUpdatedLabel);
        hourLabel = view.findViewById(R.id.hourLabel);
        temperatureLabel = view.findViewById(R.id.temperatureLabel);
        feelsLikeLabel = view.findViewById(R.id.feelsLikeLabel);
        windDegreeLabel = view.findViewById(R.id.windDegreeLabel);
        cloudsLabel = view.findViewById(R.id.cloudsLabel);
        pressureLabel = view.findViewById(R.id.pressureLabel);
        humidityLabel = view.findViewById(R.id.humidityLabel);
        windSpeedLabel = view.findViewById(R.id.windSpeedLabel);
        dewPointLabel = view.findViewById(R.id.dewPointLabel);

        ImageView backImage = view.findViewById(R.id.forecastNavigationBack);
        ImageView forwardImage = view.findViewById(R.id.forecastNavigationForward);
        backImage.setOnClickListener(this);
        forwardImage.setOnClickListener(this);

        Timestamp ts = new Timestamp(System.currentTimeMillis());
        lastUpdatedLabel.setText("Updated: " + getDayText(ts, true));

        getCurrentData(Configuration.getZagrebLatitude(), Configuration.getZagrebLongitude());

        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.forecastNavigationBack:
                if (hourBack())
                    showCurrentData();
                else
                    Toast.makeText(getActivity(), "Beginning of 48h forecast.", Toast.LENGTH_SHORT).show();
                break;
            case R.id.forecastNavigationForward:
                if (hourForward())
                    showCurrentData();
                else
                    Toast.makeText(getActivity(), "End of 48h forecast.", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
    }

    private boolean hourBack() {
        if (hour <= 0)
            return false;
        hour--;
        return true;
    }

    private boolean hourForward() {
        if (hour >= 47)
            return false;
        hour++;
        return true;
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
                daysWeatherData = weatherData;
                showCurrentData();
                Log.d("JSON Data", weatherData.toString());
            }

            @Override
            public void onFailure(@NotNull Call<WeatherData> call, @NotNull Throwable t) {
                Log.e("Main Screen", "onFailure: Something went wrong. " + t.getMessage());
            }
        });
    }

    @SuppressLint("SetTextI18n")
    public void showCurrentData() {
        if (daysWeatherData == null) {
            weatherDescriptionLabel.setText("Weather information not available.");
            return;
        }


        loadBackground(daysWeatherData.getHourly().get(hour).getWeather().get(0).getId());

        weatherDescriptionLabel.setText(daysWeatherData.getHourly().get(hour).getWeather().get(0).getDescription());

        int CONVERT_TO_MILLIS = 1000;
        Timestamp ts = new Timestamp((long) daysWeatherData.getHourly().get(hour).getDt() * CONVERT_TO_MILLIS);
        hourLabel.setText("Data for " + getDayText(ts, false));

        float t = daysWeatherData.getHourly().get(hour).getTemp() - 273.0f;
        t = Math.round(t * 100.0f) / 100.0f;
        temperatureLabel.setText("Temperature: " + t + "°C");

        t = daysWeatherData.getHourly().get(hour).getFeels_like() - 273.0f;
        t = Math.round(t * 100.f) / 100.0f;
        feelsLikeLabel.setText("Feels like: " + t + "°C");

        windDegreeLabel.setText("Wind direction: " + daysWeatherData.getHourly().get(hour).getWind_deg() + "°");

        cloudsLabel.setText("Clouds: " + daysWeatherData.getHourly().get(hour).getClouds() + "%");

        pressureLabel.setText("Pressure: " + daysWeatherData.getHourly().get(hour).getPressure() + " hPa");

        humidityLabel.setText("Humidity: " + daysWeatherData.getHourly().get(hour).getHumidity() + "%");

        windSpeedLabel.setText("Wind speed: " + daysWeatherData.getHourly().get(hour).getWind_speed() + " m/s");

        t = daysWeatherData.getHourly().get(hour).getDew_point();
        t = Math.round(t * 100.f) / 100.0f;
        dewPointLabel.setText("Atmospheric temperature: " + t + "°C");
    }

    private void loadBackground(int desc) {
        Log.d("a", "DESC " + desc);
        if (desc < 300) {
            loadImage(imgView, R.drawable.thunderstorm); //thunderstorm
        } else if (desc < 503) {
            loadImage(imgView, R.drawable.drizzle_light_moderate_rain); //drizzle, light and moderate rain
        } else if (desc < 600) {
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

    @SuppressLint("SimpleDateFormat")
    private String getDayText(Timestamp ts, boolean detailed) {
        Date date = new Date(ts.getTime());

        if (detailed)
            return new SimpleDateFormat("HH:mm:ss, dd.MM.yyyy").format(date);

        return new SimpleDateFormat("EEEE, HH:mm:ss, dd. MMMM yyyy.").format(date);
    }
}