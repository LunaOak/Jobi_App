package edu.umd.cs.jobi;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import edu.umd.cs.jobi.model.Contact;

/**
 * Created by Juan on 5/4/2017.
 */

public class EnterContactActivity extends SingleFragmentActivity {

    private static final String EXTRA_ID = "ID";

    @Override
    protected Fragment createFragment() {
        String id = getIntent().getStringExtra(EXTRA_ID);

        return EnterContactFragment.newInstance(id);
    }

    public static Intent newIntent(Context context, String id) {
        Intent intent = new Intent(context, EnterContactActivity.class);
        intent.putExtra(EXTRA_ID, id);
        return intent;
    }

    public static Contact getContactCreated(Intent data) {
        return EnterContactFragment.getContactCreated(data);
    }
}
