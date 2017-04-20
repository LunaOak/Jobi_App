package edu.umd.cs.jobi;


import android.support.v4.app.Fragment;

public class CompanyActivity extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment() {
       return CompanyFragment.newInstance();
    }

}
