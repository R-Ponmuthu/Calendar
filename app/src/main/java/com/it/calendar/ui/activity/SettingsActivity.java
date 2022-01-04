package com.it.calendar.ui.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.widget.Toolbar;

import com.it.calendar.R;
import com.it.calendar.dialog.FeedbackDialog;
import com.it.calendar.dialog.RatingDialog;
import com.it.calendar.utils.SharedPreference;

public class SettingsActivity extends AppCompatPreferenceActivity {

    private static final String TAG = SettingsActivity.class.getSimpleName();
    /**
     * A preference value change listener that updates the preference's summary
     * to reflect its new value.
     */
    private static Preference.OnPreferenceChangeListener sBindPreferenceSummaryToValueListener = new Preference.OnPreferenceChangeListener() {
        @Override
        public boolean onPreferenceChange(Preference preference, Object newValue) {
            String stringValue = newValue.toString();


            return true;
        }
    };

    private static void bindPreferenceSummaryToValue(Preference preference) {
        preference.setOnPreferenceChangeListener(sBindPreferenceSummaryToValueListener);

        sBindPreferenceSummaryToValueListener.onPreferenceChange(preference,
                PreferenceManager
                        .getDefaultSharedPreferences(preference.getContext())
                        .getString(preference.getKey(), ""));
    }

    /**
     * Email client intent to send support mail
     * Appends the necessary device information to email body
     * useful when providing support
     */
    public static void sendFeedback(Context context) {
        Drawable drawable1 = context.getResources().getDrawable(R.drawable.icon_logo);
        FeedbackDialog feedbackDialog = new FeedbackDialog.Builder(context)
                .threshold(4)
                .icon(drawable1)
                .titleTextColor(R.color.black)
                .formTitle("Submit Feedback")
                .formHint("Tell us where we can improve")
                .formSubmitText("Submit")
                .formCancelText("Cancel")
                .playstoreUrl("https://play.google.com/store/apps/details?id=com.it.calendar")
                .build();

        feedbackDialog.show();
    }

    public static void sendRating(Context context) {
        Drawable drawable = context.getResources().getDrawable(R.drawable.icon_logo);
        RatingDialog ratingDialog = new RatingDialog.Builder(context)
                .threshold(4)
                .icon(drawable)
                .title("How was your experience with us?")
                .titleTextColor(R.color.black)
                .positiveButtonText("Rate Now")
                .negativeButtonText("Later")
                .formTitle("Submit Feedback")
                .formHint("Tell us where we can improve")
                .formSubmitText("Submit")
                .formCancelText("Cancel")
                .playstoreUrl("https://play.google.com/store/apps/details?id=com.it.calendar")
                .build();

        ratingDialog.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        // load settings fragment
        getFragmentManager().beginTransaction().replace(R.id.container, new MainPreferenceFragment()).commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressLint("ValidFragment")
    public static class MainPreferenceFragment extends PreferenceFragment {


        private SharedPreference sharedPreference = new SharedPreference();

        @Override
        public void onCreate(final Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            addPreferencesFromResource(R.xml.pref_settings);

            Preference versionPref = findPreference(getString(R.string.key_version));
            try {
                versionPref.setSummary(getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0).versionName);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }


            Preference privacyPref = findPreference(getString(R.string.key_privacy));
            privacyPref.setOnPreferenceClickListener(preference -> {

                startActivity(new Intent(getActivity(), PrivacyPolicyActivity.class));
                return true;
            });

            Preference ratingPref = findPreference(getString(R.string.key_rating));
            ratingPref.setOnPreferenceClickListener(preference -> {

                sendRating(getActivity());
                return true;
            });

            // feedback preference click listener
            Preference myPref = findPreference(getString(R.string.key_send_feedback));
            myPref.setOnPreferenceClickListener(preference -> {
                sendFeedback(getActivity());
                return true;
            });
        }
    }
}
