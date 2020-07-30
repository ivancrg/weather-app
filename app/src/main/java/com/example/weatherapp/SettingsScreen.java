package com.example.weatherapp;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import yuku.ambilwarna.AmbilWarnaDialog;

public class SettingsScreen extends PreferenceFragmentCompat {
    private String colorToSave;
    private View preferenceView;

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        preferenceView = view;

        Configuration.refreshPreferences(requireContext());

        assert view != null;
        if (Configuration.isDarkModeEnabled()) {
            view.setBackgroundColor(Color.DKGRAY);
        } else {
            view.setBackgroundColor(Color.WHITE);
        }

        return view;
    }


    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey);

        final Preference primaryAppColor = getPreferenceManager().findPreference("settingsPrimaryColor");
        final Preference primaryDarkAppColor = getPreferenceManager().findPreference("settingsPrimaryDarkColor");
        final Preference darkModeSwitch = getPreferenceManager().findPreference("settingsDarkMode");
        final Preference toolbarSwitch = getPreferenceManager().findPreference("settingsToolbar");
        final Preference navigationTypeSwitch = getPreferenceManager().findPreference("settingsNavigationType");
        final Preference mainNavigationColor = getPreferenceManager().findPreference("settingsNavigationMainColor");
        final Preference iconNavigationColor = getPreferenceManager().findPreference("settingsNavigationIconColor");
        final Preference textNavigationColor = getPreferenceManager().findPreference("settingsNavigationTextColor");

        assert darkModeSwitch != null;
        darkModeSwitch.setOnPreferenceChangeListener((preference, newValue) -> {
            if ((boolean) newValue) {
                ((MainActivity) requireActivity()).applyDarkColors();
                preferenceView.setBackgroundColor(Color.DKGRAY);
            } else {
                ((MainActivity) requireActivity()).applyColors();
                preferenceView.setBackgroundColor(Color.WHITE);
            }

            Configuration.refreshPreferences(requireContext());

            if (Configuration.isNavigationTypeDrawer())
                ((MainActivity) requireActivity()).getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_drawer, new SettingsScreen()).commit();
            else
                ((MainActivity) requireActivity()).getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_bottom, new SettingsScreen()).commit();

            return true;
        });

        assert primaryAppColor != null;
        primaryAppColor.setOnPreferenceClickListener(preference -> {
            Configuration.refreshPreferences(requireContext());
            colorToSave = Configuration.getColorPrimaryPref();
            setColor(Configuration.getColorPrimary());

            return true;
        });

        assert primaryDarkAppColor != null;
        primaryDarkAppColor.setOnPreferenceClickListener(preference -> {
            Configuration.refreshPreferences(requireContext());
            colorToSave = Configuration.getColorPrimaryDarkPref();
            setColor(Configuration.getColorPrimaryDark());

            return true;
        });

        assert mainNavigationColor != null;
        mainNavigationColor.setOnPreferenceClickListener(preference -> {
            Configuration.refreshPreferences(requireContext());
            colorToSave = Configuration.getNavigationBackgroundPref();
            setColor(Configuration.getNavigationBackground());

            return true;
        });

        assert iconNavigationColor != null;
        iconNavigationColor.setOnPreferenceClickListener(preference -> {
            Configuration.refreshPreferences(requireContext());
            colorToSave = Configuration.getNavigationIconPref();
            setColor(Configuration.getNavigationIcon());

            return true;
        });

        assert textNavigationColor != null;
        textNavigationColor.setOnPreferenceClickListener(preference -> {
            Configuration.refreshPreferences(requireContext());
            colorToSave = Configuration.getNavigationTextPref();
            setColor(Configuration.getNavigationText());

            return true;
        });

        assert toolbarSwitch != null;
        toolbarSwitch.setOnPreferenceChangeListener((preference, newValue) -> {
            boolean state = (boolean) newValue;

            if (state)
                Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).show();
            else
                Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).hide();

            return true;
        });

        assert navigationTypeSwitch != null;
        navigationTypeSwitch.setOnPreferenceChangeListener((preference, newValue) -> {
            ((MainActivity) requireActivity()).changeNavigation((boolean) newValue);

            return true;
        });
    }

    private void setColor(int defaultColor) {
        AmbilWarnaDialog colorPicker = new AmbilWarnaDialog(getContext(), defaultColor, new AmbilWarnaDialog.OnAmbilWarnaListener() {
            @Override
            public void onCancel(AmbilWarnaDialog dialog) {

            }

            @Override
            public void onOk(AmbilWarnaDialog dialog, int color) {
                SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(requireContext().getApplicationContext()).edit();
                editor.putInt(colorToSave, color);
                editor.apply();

                Configuration.refreshPreferences(requireContext());

                if (Configuration.isDarkModeEnabled())
                    ((MainActivity) requireActivity()).applyDarkColors();
                else
                    ((MainActivity) requireActivity()).applyColors();
            }
        });

        colorPicker.show();
    }
}