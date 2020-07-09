package com.example.weatherapp;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.weatherapp.model.WeatherData;

import org.jetbrains.annotations.NotNull;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SearchScreen extends Fragment implements AdapterView.OnItemSelectedListener {
    private ImageView imgView;
    private TextView cityLabel;
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

    public SearchScreen() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search_screen, container, false);

        imgView = view.findViewById(R.id.conditionImage);
        cityLabel = view.findViewById(R.id.cityLabel);
        weatherDescriptionLabel = view.findViewById(R.id.weatherDescriptionLabel);
        lastUpdatedLabel = view.findViewById(R.id.lastUpdatedLabel);
        temperatureLabel = view.findViewById(R.id.temperatureLabel);
        feelsLikeLabel = view.findViewById(R.id.feelsLikeLabel);
        windDegreeLabel = view.findViewById(R.id.windDegreeLabel);
        visibilityLabel = view.findViewById(R.id.visibilityLabel);
        pressureLabel = view.findViewById(R.id.pressureLabel);
        humidityLabel = view.findViewById(R.id.humidityLabel);
        windSpeedLabel = view.findViewById(R.id.windSpeedLabel);
        uvIndexLabel = view.findViewById(R.id.uvIndexLabel);
        Spinner spinner = view.findViewById(R.id.citiesSpinner);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(Objects.requireNonNull(getActivity()),
                R.array.availableSearchCities, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        return view;
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        String city = adapterView.getItemAtPosition(i).toString();
        Pair<Float, Float> coordinates = Configuration.getCoordinatesMap().get(city);

        if (coordinates != null) {
            getCurrentData(city, coordinates);
        } else {
            Toast.makeText(getActivity(), "ERROR", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    private void getCurrentData(String city, Pair<Float, Float> coordinates) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Configuration.getBaseUrl())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        IWeatherService weatherService = retrofit.create(IWeatherService.class);
        Call<WeatherData> call = weatherService.getCurrentWeatherData((float) coordinates.first, (float) coordinates.second, Configuration.getAppId());

        call.enqueue(new Callback<WeatherData>() {
            @Override
            public void onResponse(@NotNull Call<WeatherData> call, @NotNull Response<WeatherData> response) {
                Log.d("Main Screen", "onResponse: Server response: " + response.toString());
                WeatherData weatherData = response.body();
                assert weatherData != null;
                showCurrentData(city, weatherData);
                Log.d("JSON Data", weatherData.toString());
            }

            @Override
            public void onFailure(@NotNull Call<WeatherData> call, @NotNull Throwable t) {
                Log.e("Main Screen", "onFailure: Something went wrong. " + t.getMessage());
            }
        });
    }

    @SuppressLint("SetTextI18n")
    public void showCurrentData(String city, WeatherData weatherData) {
        if (weatherData == null) {
            weatherDescriptionLabel.setText("Weather information not available.");
            return;
        }

        loadBackground(weatherData.getCurrent().getWeather().get(0).getId());

        cityLabel.setText(city);

        weatherDescriptionLabel.setText(weatherData.getCurrent().getWeather().get(0).getDescription());

        Timestamp ts = new Timestamp(System.currentTimeMillis());
        lastUpdatedLabel.setText("Updated: " + getDayText(ts, true));

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