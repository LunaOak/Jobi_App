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
    private static final String EXTRA_POSITION_TITLE = "EXTRA_POSITION_TITLE";
    private static final String EXTRA_COMPANY_NAME = "EXTRA_COMPANY_NAME";
    private static final String EXTRA_CREATE_BOOLEAN = "EXTRA_CREATE_BOOLEAN";

    @Override
    protected Fragment createFragment() {
        Boolean create = getIntent().getBooleanExtra(EXTRA_CREATE_BOOLEAN, true);
        String positionTitle = getIntent().getStringExtra(EXTRA_POSITION_TITLE);
        String companyName = getIntent().getStringExtra(EXTRA_COMPANY_NAME);
        String eventId = getIntent().getStringExtra(EXTRA_EVENT_ID);

        return EnterEventFragment.newInstance(create, positionTitle, companyName, eventId);
    }

    public static Intent newIntentEdit(Context context, String eventId) {
        Intent intent = new Intent(context, EnterEventActivity.class);
        intent.putExtra(EXTRA_EVENT_ID, eventId);
        intent.putExtra(EXTRA_CREATE_BOOLEAN, false);

        return intent;
    }

    public static Intent newIntentCreate(Context context, String positionTitle, String companyName) {
        Intent intent = new Intent(context, EnterEventActivity.class);
        intent.putExtra(EXTRA_CREATE_BOOLEAN, true);
        intent.putExtra(EXTRA_POSITION_TITLE, positionTitle);
        intent.putExtra(EXTRA_COMPANY_NAME, companyName);

        return intent;
    }

    public static Event getEventCreated(Intent data) {
        return EnterEventFragment.getEventCreated(data);
    }
}
