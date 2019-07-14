package by.litvin.activity;

import android.os.Bundle;
import android.preference.PreferenceActivity;

import by.litvin.fragment.SettingsFragment;

public class PreferencesActivity extends PreferenceActivity {

    public static final String APP_PREFERENCES_FILE = "appPreferences";
    public static final String VIBRATION_LENGTH = "vibrationLength";
    public static final String LOADED_CHARACTERS_NUMBER = "loadedCharactersNumber";
    public static final String DEFAULT_VIBRATION_LENGTH = "30"; //TODO Add default preferences using PreferencesManager
    public static final String DEFAULT_OFFSET = "10";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new SettingsFragment())
                .commit();

    }
}
