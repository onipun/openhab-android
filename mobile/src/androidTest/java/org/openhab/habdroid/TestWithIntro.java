package org.openhab.habdroid;

import android.preference.PreferenceManager;
import android.util.Log;

import androidx.test.InstrumentationRegistry;

import org.openhab.habdroid.util.Constants;

public abstract class TestWithIntro extends ProgressbarAwareTest {
    private static final String TAG = "TestWithIntro";
    @Override
    public void setup() {
        Log.d(TAG, "onTestWithIntro");
        PreferenceManager
                .getDefaultSharedPreferences(InstrumentationRegistry.getTargetContext())
                .edit()
                .putString(Constants.PREFERENCE_SITEMAP_NAME, "")
                .putBoolean(Constants.PREFERENCE_DEMOMODE, true)
                .putBoolean(Constants.PREFERENCE_FIRST_START, true)
                .commit();

        super.setup();
    }
}
