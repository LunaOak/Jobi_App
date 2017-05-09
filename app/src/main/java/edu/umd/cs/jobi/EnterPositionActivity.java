package edu.umd.cs.jobi;


import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import edu.umd.cs.jobi.model.Position;

public class EnterPositionActivity extends SingleFragmentActivity {

    private final String TAG = getClass().getSimpleName();

    private static final String POSITION_ID = "POSITION_ID";
    private static final String FROM_COMPANY_BOOLEAN = "FROM_COMPANY_BOOLEAN";
    private static final String COMPANY_NAME = "COMPANY_NAME";

    @Override
    protected Fragment createFragment() {
        String positionId = getIntent().getStringExtra(POSITION_ID);
        String companyName = getIntent().getStringExtra(COMPANY_NAME);
        Boolean fromCompany = getIntent().getBooleanExtra(FROM_COMPANY_BOOLEAN, false);

        return EnterPositionFragment.newInstance(positionId, companyName, fromCompany);
    }

    public static Intent newIntent(Context context, String positionId) {
        Intent intent = new Intent(context, EnterPositionActivity.class);
        intent.putExtra(POSITION_ID, positionId);
        intent.putExtra(FROM_COMPANY_BOOLEAN, false);
        return intent;
    }

    public static Intent newIntentFromCompany(Context context, String company) {
        Intent intent = new Intent(context, EnterPositionActivity.class);
        intent.putExtra(COMPANY_NAME, company);
        intent.putExtra(FROM_COMPANY_BOOLEAN, true);

        return intent;
    }

    public static Position getPositionCreated(Intent data) {
        return EnterPositionFragment.getPositionCreated(data);
    }
}
