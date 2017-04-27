package edu.umd.cs.jobi;


import android.support.v4.app.Fragment;

public class CompanyListActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
       return CompanyListFragment.newInstance();
    }

}
