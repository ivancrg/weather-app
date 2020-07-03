package com.example.weatherapp;

import android.annotation.SuppressLint;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.transition.Transition;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.example.weatherapp.model.WeatherData;

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
    private ImageView imgView;
    private EditText cityInput;
    private EditText countryInput;
    private Button refresh;


    public MainScreen() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MainScreen.
     */
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

        cityText = (TextView) view.findViewById(R.id.cityCountry);
        condDescr = (TextView) view.findViewById(R.id.condition);
        temp = (TextView) view.findViewById(R.id.temperature);
        hum = (TextView) view.findViewById(R.id.humidity);
        press = (TextView) view.findViewById(R.id.pressure);
        wind = (TextView) view.findViewById(R.id.windData);
        imgView = (ImageView) view.findViewById(R.id.conditionImage);
        cityInput = (EditText) view.findViewById(R.id.cityInput);
        countryInput = (EditText) view.findViewById(R.id.countryInput);


        refresh = view.findViewById(R.id.refreshButton);
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cityInput.clearFocus();
                countryInput.clearFocus();
                getCurrentData(cityInput.getText().toString(), countryInput.getText().toString());
            }
        });

        cityInput.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (cityInput.getText().toString().equals("Enter city name..."))
                    cityInput.setText("");
            }
        });

        countryInput.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (countryInput.getText().toString().equals("Enter country name..."))
                    countryInput.setText("");
            }
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
            public void onResponse(Call<WeatherData> call, Response<WeatherData> response) {
                Log.d("Main Screen", "onResponse: Server response: " + response.toString());
                WeatherData weatherData = response.body();
                showCurrentData(weatherData);
                Log.d("JSON Data", weatherData.toString());
            }

            @Override
            public void onFailure(Call<WeatherData> call, Throwable t) {
                Log.e("Main Screen", "onFailure: Something went wrong. " + t.getMessage());
            }
        });
    }

    @SuppressLint("SetTextI18n")
    public void showCurrentData(WeatherData weatherData) {
        cityText.setText(weatherData.getName() + ", " + weatherData.getSystemData().getCountry().toUpperCase());

        condDescr.setText(weatherData.getWeather().get(0).getDescription());

        Double temperature = weatherData.getMainData().getTemperature() - 273.0;
        temperature = Math.round(temperature * 100.0) / 100.0;
        temp.setText(temperature.toString() + "°C");

        press.setText(weatherData.getMainData().getPressure() + " hPa");

        wind.setText(weatherData.getWind().getSpeed().toString() + " units, " + weatherData.getWind().getDirection().toString() + "°");

        hum.setText(weatherData.getMainData().getHumidity() + "%");
    }
}