package edu.umd.cs.jobi;

import android.support.v4.app.Fragment;

    public class PositionListActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return PositionListFragment.newInstance();
    }

}