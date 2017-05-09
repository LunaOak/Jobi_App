package edu.umd.cs.jobi;


import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import edu.umd.cs.jobi.model.Settings;

public class EnterActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return EnterFragment.newInstance();
    }

}
