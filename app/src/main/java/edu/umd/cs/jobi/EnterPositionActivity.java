package edu.umd.cs.jobi;


import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import edu.umd.cs.jobi.model.Position;

public class EnterPositionActivity extends SingleFragmentActivity {
    
    private final String TAG = getClass().getSimpleName();

    private static final String POSITION_ID = "POSITION_ID";

    @Override
    protected Fragment createFragment() {
        String positionId = getIntent().getStringExtra(POSITION_ID);
        return EnterPositionFragment.newInstance(positionId);
    }

    public static Intent newIntent(Context context, String positionId) {
        Intent intent = new Intent(context, EnterPositionActivity.class);
        intent.putExtra(POSITION_ID, positionId);
        return intent;
    }

    public static Position getPositionCreated(Intent data) {
        return EnterPositionFragment.getPositionCreated(data);
    }
}
