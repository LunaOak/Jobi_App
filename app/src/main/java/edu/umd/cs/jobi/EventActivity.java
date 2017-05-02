package edu.umd.cs.jobi;

import android.support.v4.app.Fragment;

public class EventActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return EventFragment.newInstance();
    }
}
