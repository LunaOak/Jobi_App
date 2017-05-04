package edu.umd.cs.jobi;


import android.support.v4.app.Fragment;

public class SettingsActivity extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment() {
        return SettingsFragment.newInstance();
    }
}
