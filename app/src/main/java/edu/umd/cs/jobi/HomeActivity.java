package edu.umd.cs.jobi;


import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

public class HomeActivity extends SingleFragmentActivity {

    private static final String SETTINGS_ID = "SETTINGS_ID";

    @Override
    protected Fragment createFragment() {
        String settingsId = getIntent().getStringExtra(SETTINGS_ID);
        return HomeFragment.newInstance(settingsId);
    }

//    public static Intent newIntent(Context context, String settingsId) {
//        Intent intent = new Intent(context, HomeActivity.class);
//        intent.putExtra(SETTINGS_ID, settingsId);
//        return intent;
//    }


}
