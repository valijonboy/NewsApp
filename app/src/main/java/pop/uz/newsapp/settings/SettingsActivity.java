package pop.uz.newsapp.settings;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;

import pop.uz.newsapp.R;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);

    }

    public static class SettingsFragment extends PreferenceFragmentCompat
            implements Preference.OnPreferenceChangeListener {

        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey);

            Preference orderBy = findPreference(getString(R.string.settings_order_by_key));
            if (orderBy != null) {
                bindPreferenceSummaryToValue(orderBy);
            }

            Preference pageSize = findPreference(getString(R.string.settings_page_size_key));
            if (pageSize != null) {
                bindPreferenceSummaryToValue(pageSize);
            }

            Preference category = findPreference(getString(R.string.settings_category_key));
            if (category != null) {
                bindPreferenceSummaryToValue(category);
            }
        }

        private void bindPreferenceSummaryToValue(Preference preference) {
            preference.setOnPreferenceChangeListener(this);
            SharedPreferences preferences =
                    PreferenceManager.getDefaultSharedPreferences(preference.getContext());
            String preferenceString = preferences.getString(preference.getKey(), "");
            onPreferenceChange(preference, preferenceString);
        }

        @Override
        public boolean onPreferenceChange(Preference preference, Object newValue) {
            // The code in this method takes care of updating the displayed preference summary after it has been changed
            String stringValue = newValue.toString();

            if (preference instanceof ListPreference) {
                ListPreference listPreference = (ListPreference) preference;
                int prefIndex = listPreference.findIndexOfValue(stringValue);
                if (prefIndex >= 0) {
                    CharSequence[] labels = listPreference.getEntries();
                    preference.setSummary(labels[prefIndex]);
                }
            } else {
                preference.setSummary(stringValue);
            }
            return true;
        }
    }
}