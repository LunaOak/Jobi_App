package edu.umd.cs.jobi;


import android.content.Intent;
import android.support.v4.app.Fragment;

import edu.umd.cs.jobi.model.Settings;

public class SettingsActivity extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment() {
        return SettingsFragment.newInstance();
    }

    public static Settings getSettingsEdit(Intent data) {
        return SettingsFragment.getSettingsCreated(data);
    }
}
