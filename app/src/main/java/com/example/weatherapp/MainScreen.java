package com.example.weatherapp;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.weatherapp.model.WeatherData;

import org.jetbrains.annotations.NotNull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class MainScreen extends Fragment {
    private TextView cityText;
    private TextView condDescr;
    private TextView temp;
    private TextView press;
    private TextView wind;
    private TextView hum;
    private EditText cityInput;
    private ImageView imgView;
    private EditText countryInput;


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

        cityText = view.findViewById(R.id.cityCountry);
        condDescr = view.findViewById(R.id.condition);
        temp = view.findViewById(R.id.temperature);
        hum = view.findViewById(R.id.humidity);
        press = view.findViewById(R.id.pressure);
        wind = view.findViewById(R.id.windData);
        cityInput = view.findViewById(R.id.cityInput);
        countryInput = view.findViewById(R.id.countryInput);
        imgView = view.findViewById(R.id.conditionImage);
        Button refresh = view.findViewById(R.id.refreshButton);

        refresh.setOnClickListener(view1 -> {
            cityInput.clearFocus();
            countryInput.clearFocus();
            getCurrentData(cityInput.getText().toString(), countryInput.getText().toString());
        });

        cityInput.setOnFocusChangeListener((view12, b) -> {
            if (cityInput.getText().toString().equals("Enter city name..."))
                cityInput.setText("");
        });

        countryInput.setOnFocusChangeListener((view13, b) -> {
            if (countryInput.getText().toString().equals("Enter country name..."))
                countryInput.setText("");
        });


        // Inflate the layout for this fragment
        return view;
    }

    private void getCurrentData(String city, String country) {
        String cityAndCountry = city + "," + country;

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Configuration.getBaseUrl())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        IWeatherService weatherService = retrofit.create(IWeatherService.class);
        Call<WeatherData> call = weatherService.getCurrentWeatherData(cityAndCountry, Configuration.getAppId());

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
        loadBackground(weatherData.getWeather().get(0).getId());

        cityText.setText(weatherData.getName() + ", " + weatherData.getSystemData().getCountry().toUpperCase());

        condDescr.setText(weatherData.getWeather().get(0).getDescription());

        double temperature = weatherData.getMainData().getTemperature() - 273.0;
        temperature = Math.round(temperature * 100.0) / 100.0;
        temp.setText(temperature + "°C");

        press.setText(weatherData.getMainData().getPressure() + " hPa");

        wind.setText(weatherData.getWind().getSpeed().toString() + " units, " + weatherData.getWind().getDirection().toString() + "°");

        hum.setText(weatherData.getMainData().getHumidity() + "%");
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