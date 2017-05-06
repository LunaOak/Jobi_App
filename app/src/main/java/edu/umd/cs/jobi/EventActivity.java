package edu.umd.cs.jobi;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

public class EventActivity extends SingleFragmentActivity {

    private static final String EXTRA_EVENT_ID = "EVENT_ID";

    @Override
    protected Fragment createFragment() {
        String eventId = getIntent().getStringExtra(EXTRA_EVENT_ID);

        return EventFragment.newInstance(eventId);
    }

    public static Intent newIntent(Context context, String eventId) {
        Intent intent = new Intent(context, EventActivity.class);
        intent.putExtra(EXTRA_EVENT_ID, eventId);

        return intent;
    }
}
