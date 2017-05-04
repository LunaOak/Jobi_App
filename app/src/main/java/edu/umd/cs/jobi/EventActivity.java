package edu.umd.cs.jobi;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

public class EventActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return EventFragment.newInstance();
    }

    public static Intent newIntent(Context context) {
        Intent intent = new Intent(context, EventActivity.class);
        return intent;
    }
}
