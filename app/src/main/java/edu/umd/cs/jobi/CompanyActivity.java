package edu.umd.cs.jobi;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;



public class CompanyActivity extends SingleFragmentActivity {

    private static final String COMPANY_ID = "COMPANY_ID";


    @Override
    protected Fragment createFragment() {
        String companyId = getIntent().getStringExtra(COMPANY_ID);
        return CompanyFragment.newInstance(companyId);
    }

    public static Intent newIntent(Context context, String companyId){
        Intent intent = new Intent(context, CompanyActivity.class);
        intent.putExtra(COMPANY_ID, companyId);
        return intent;
    }
}
