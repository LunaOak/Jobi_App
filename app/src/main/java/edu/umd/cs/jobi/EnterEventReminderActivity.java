package edu.umd.cs.jobi;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import edu.umd.cs.jobi.model.Reminder;

/**
 * Created by Juan on 5/6/2017.
 */

public class EnterEventReminderActivity extends SingleFragmentActivity {
    private static final String EXTRA_REMINDER_ID = "REMINDER_ID";
    private static final String EXTRA_EVENT_TITLE = "EVENT_TITLE";
    private static final String EXTRA_EVENT_DATE_MS = "EVENT_DATE_MS";

    @Override
    protected Fragment createFragment() {
        String reminderId = getIntent().getStringExtra(EXTRA_REMINDER_ID);
        String eventTitle = getIntent().getStringExtra(EXTRA_EVENT_TITLE);
        long eventDateMs = getIntent().getLongExtra(EXTRA_EVENT_DATE_MS, 0);

        return EnterEventReminderFragment.newInstance(reminderId, eventTitle, eventDateMs);
    }

    public static Intent newIntent(Context context, String reminderId, String eventTitle, long eventDateMs) {
        Intent intent = new Intent(context, EnterEventReminderActivity.class);
        intent.putExtra(EXTRA_REMINDER_ID, reminderId);
        intent.putExtra(EXTRA_EVENT_TITLE, eventTitle);
        intent.putExtra(EXTRA_EVENT_DATE_MS, eventDateMs);

        return intent;
    }

    public static Reminder getReminderCreated(Intent data) {
        return EnterEventReminderFragment.getReminderCreated(data);
    }
}
