package by.litvin.fragment;


import android.os.Bundle;
import android.preference.PreferenceFragment;

import androidx.annotation.Nullable;

import by.litvin.R;

public class SettingsFragment extends PreferenceFragment {


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.application_preferences);

    }
}
