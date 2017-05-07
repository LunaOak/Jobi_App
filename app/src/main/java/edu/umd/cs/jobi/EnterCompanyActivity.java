package edu.umd.cs.jobi;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import edu.umd.cs.jobi.model.Company;

public class EnterCompanyActivity extends SingleFragmentActivity {

    private static final String EXTRA_COMPANY_ID = "COMPANY_ID";


    @Override
    protected Fragment createFragment() {

        String companyId = getIntent().getStringExtra(EXTRA_COMPANY_ID);
        return EnterCompanyFragment.newInstance(companyId);
    }

    public static Intent newIntent(Context context, String companyId) {
        Intent intent = new Intent(context, EnterCompanyActivity.class);
        intent.putExtra(EXTRA_COMPANY_ID, companyId);
        return intent;
    }

    public static Company getCompanyCreated(Intent data){
        return EnterCompanyFragment.getCompanyCreated(data);
    }
}
