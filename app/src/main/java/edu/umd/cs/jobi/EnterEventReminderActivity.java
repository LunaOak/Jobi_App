package edu.umd.cs.jobi;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import edu.umd.cs.jobi.model.Contact;

/**
 * Created by Juan on 5/6/2017.
 */

public class EnterEventReminderActivity extends SingleFragmentActivity {
    private static final String EXTRA_REMINDER_ID = "REMINDER_ID";

    @Override
    protected Fragment createFragment() {
        String reminderId = getIntent().getStringExtra(EXTRA_REMINDER_ID);

        return EnterEventReminderFragment.newInstance(reminderId);
    }

    public static Intent newIntent(Context context, String reminderId) {
        Intent intent = new Intent(context, EnterEventReminderActivity.class);
        intent.putExtra(EXTRA_REMINDER_ID, reminderId);

        return intent;
    }

    public static Contact getContactCreated(Intent data) {
        //return EnterEventReminderFragment.getReminderCreated(data);
        return null;
    }
}
