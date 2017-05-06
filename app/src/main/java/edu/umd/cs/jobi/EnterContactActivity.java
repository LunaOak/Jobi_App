package edu.umd.cs.jobi;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import edu.umd.cs.jobi.model.Contact;

/**
 * Created by Juan on 5/4/2017.
 */

public class EnterContactActivity extends SingleFragmentActivity {

    private static final String EXTRA_CONTACT_ID = "CONTACT_ID";
    private static final String EXTRA_FROM_EVENT = "FROM_EVENT";

    @Override
    protected Fragment createFragment() {
        String contactId = getIntent().getStringExtra(EXTRA_CONTACT_ID);
        Boolean fromEvent = getIntent().getBooleanExtra(EXTRA_FROM_EVENT, true);


        return EnterContactFragment.newInstance(contactId, fromEvent);
    }

    public static Intent newIntent(Context context, String contactId, Boolean fromEvent) {
        Intent intent = new Intent(context, EnterContactActivity.class);
        intent.putExtra(EXTRA_CONTACT_ID, contactId);
        intent.putExtra(EXTRA_FROM_EVENT, fromEvent);

        return intent;
    }

    public static Contact getContactCreated(Intent data) {
        return EnterContactFragment.getContactCreated(data);
    }
}
