package edu.umd.cs.jobi;


import android.support.v4.app.Fragment;

public class HomeActivity extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment() {
        return HomeFragment.newInstance();
    }


}
