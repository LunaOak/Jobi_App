package edu.umd.cs.jobi;


import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import edu.umd.cs.jobi.model.Settings;

public class SettingsActivity extends SingleFragmentActivity {

    private static final String SETTINGS_ID = "SETTINGS_ID";

    @Override
    protected Fragment createFragment() {
        String settingsId = getIntent().getStringExtra(SETTINGS_ID);
        return SettingsFragment.newInstance();
    }

    public static Intent newIntent(Context context, String settingsId) {
        Intent intent = new Intent(context, SettingsActivity.class);
        intent.putExtra(SETTINGS_ID, settingsId);
        return intent;
    }

    public static Settings getSettingsEdit(Intent data) {
        return SettingsFragment.getSettingsCreated(data);
    }
}
