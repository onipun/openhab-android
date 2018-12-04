package org.openhab.habdroid;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import androidx.test.InstrumentationRegistry;

import org.junit.Before;
import org.openhab.habdroid.util.Constants;

import static com.googlecode.eyesfree.utils.LogUtils.TAG;

public abstract class TestWithoutIntro extends ProgressbarAwareTest {
    @Override
    @Before
    public void setup() {
        Log.d(TAG, "onTestWithoutIntro.java");
        SharedPreferences.Editor edit = PreferenceManager
                .getDefaultSharedPreferences(InstrumentationRegistry.getTargetContext())
                .edit();

        edit.putString(Constants.PREFERENCE_SITEMAP_NAME, "");
        if (preselectSitemap()) {
            edit.putString(Constants.PREFERENCE_SITEMAP_NAME, "demo");
            edit.putString(Constants.PREFERENCE_SITEMAP_LABEL, "Main Menu");
        }

        edit.putBoolean(Constants.PREFERENCE_DEMOMODE, true);
        edit.putBoolean(Constants.PREFERENCE_FIRST_START, false).commit();


        super.setup();
        setupRegisterIdlingResources();
    }

    protected boolean preselectSitemap() {
        return false;
    }
}
