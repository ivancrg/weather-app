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

public class ForecastWeekScreen extends Fragment implements View.OnClickListener {
    private WeatherData weekWeatherData;
    private int weekDay = 0;
    private ImageView imgView;
    private TextView weatherDescriptionLabel;
    private TextView weekDayLabel;
    private TextView temperatureLabel;
    private TextView feelsLikeLabel;
    private TextView windDegreeLabel;
    private TextView rainLabel;
    private TextView pressureLabel;
    private TextView humidityLabel;
    private TextView windSpeedLabel;
    private TextView uvIndexLabel;

    public ForecastWeekScreen() {
        // Required empty public constructor
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_forecast_week_screen, container, false);

        imgView = view.findViewById(R.id.conditionImage);
        weatherDescriptionLabel = view.findViewById(R.id.weatherDescriptionLabel);
        TextView lastUpdatedLabel = view.findViewById(R.id.lastUpdatedLabel);
        weekDayLabel = view.findViewById(R.id.weekDayLabel);
        temperatureLabel = view.findViewById(R.id.temperatureLabel);
        feelsLikeLabel = view.findViewById(R.id.feelsLikeLabel);
        windDegreeLabel = view.findViewById(R.id.windDegreeLabel);
        rainLabel = view.findViewById(R.id.rainLabel);
        pressureLabel = view.findViewById(R.id.pressureLabel);
        humidityLabel = view.findViewById(R.id.humidityLabel);
        windSpeedLabel = view.findViewById(R.id.windSpeedLabel);
        uvIndexLabel = view.findViewById(R.id.uvIndexLabel);

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
                if (dayBack())
                    showCurrentData();
                else
                    Toast.makeText(getActivity(), "Beginning of week.", Toast.LENGTH_SHORT).show();
                break;
            case R.id.forecastNavigationForward:
                if (dayForward())
                    showCurrentData();
                else
                    Toast.makeText(getActivity(), "End of week.", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
    }

    private boolean dayBack() {
        if (weekDay <= 0)
            return false;
        weekDay--;
        return true;
    }

    private boolean dayForward() {
        if (weekDay >= 7)
            return false;
        weekDay++;
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
                weekWeatherData = weatherData;
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
        if (weekWeatherData == null) {
            weatherDescriptionLabel.setText("Weather information not available.");
            return;
        }

        loadBackground(weekWeatherData.getDaily().get(weekDay).getWeather().get(0).getId());

        weatherDescriptionLabel.setText(weekWeatherData.getDaily().get(weekDay).getWeather().get(0).getDescription());

        int CONVERT_TO_MILLIS = 1000;
        Timestamp ts = new Timestamp((long) weekWeatherData.getDaily().get(weekDay).getDt() * CONVERT_TO_MILLIS);
        weekDayLabel.setText("Data for " + getDayText(ts, false));

        float t = weekWeatherData.getDaily().get(weekDay).getTemp().getDay() - 273.0f;
        t = Math.round(t * 100.0f) / 100.0f;
        temperatureLabel.setText("Temperature: " + t + "°C");

        t = weekWeatherData.getDaily().get(weekDay).getFeels_like().getDay() - 273.0f;
        t = Math.round(t * 100.f) / 100.0f;
        feelsLikeLabel.setText("Feels like: " + t + "°C");

        windDegreeLabel.setText("Wind direction: " + weekWeatherData.getDaily().get(weekDay).getWind_deg() + "°");

        rainLabel.setText("Rain: " + weekWeatherData.getDaily().get(weekDay).getRain() + "mm");

        pressureLabel.setText("Pressure: " + weekWeatherData.getDaily().get(weekDay).getPressure() + " hPa");

        humidityLabel.setText("Humidity: " + weekWeatherData.getDaily().get(weekDay).getHumidity() + "%");

        windSpeedLabel.setText("Wind speed: " + weekWeatherData.getDaily().get(weekDay).getWind_speed() + " m/s");

        uvIndexLabel.setText("UV index: " + weekWeatherData.getDaily().get(weekDay).getUvi());
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

        return new SimpleDateFormat("EEEE, dd. MMMM yyyy.").format(date);
    }
}