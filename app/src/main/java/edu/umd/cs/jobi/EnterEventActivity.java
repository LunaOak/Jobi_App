package edu.umd.cs.jobi;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import edu.umd.cs.jobi.model.Event;

/**
 * Created by Pauline on 5/6/2017.
 */

public class EnterEventActivity extends SingleFragmentActivity {
    private final String TAG = getClass().getSimpleName();

    private static final String EXTRA_EVENT_ID = "EXTRA_EVENT_ID";

    @Override
    protected Fragment createFragment() {
        String eventId = getIntent().getStringExtra(EXTRA_EVENT_ID);
        return EnterEventFragment.newInstance(eventId);
    }

    public static Intent newIntent(Context context, String eventId) {
        Intent intent = new Intent(context, EnterEventActivity.class);
        intent.putExtra(EXTRA_EVENT_ID, eventId);
        return intent;
    }

    public static Event getEventCreated(Intent data) {
        return EnterEventFragment.getEventCreated(data);
    }
}
