package edu.umd.cs.jobi;


import android.support.v4.app.Fragment;

public class BacklogActivity extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment() {
        return BacklogFragment.newInstance();
    }
}
