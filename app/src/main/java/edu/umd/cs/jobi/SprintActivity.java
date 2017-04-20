package edu.umd.cs.jobi;


import android.support.v4.app.Fragment;

public class SprintActivity extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment() {
        return SprintFragment.newInstance();
    }
}
