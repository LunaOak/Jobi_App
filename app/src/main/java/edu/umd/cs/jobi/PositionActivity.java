package edu.umd.cs.jobi;


import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import edu.umd.cs.jobi.model.Position;

public class PositionActivity extends SingleFragmentActivity {
    
    private final String TAG = getClass().getSimpleName();

    private static final String POSITION_ID = "POSITION_ID";

    @Override
    protected Fragment createFragment() {
        String positionId = getIntent().getStringExtra(POSITION_ID);
        return PositionFragment.newInstance(positionId);
    }

    public static Intent newIntent(Context context, String positionId) {
        Intent intent = new Intent(context, PositionActivity.class);
        intent.putExtra(POSITION_ID, positionId);
        return intent;
    }

    public static Position getPositionEdit(Intent data) {
        //Todo
//        return PositionFragment.getPositionEdit(data);
        return PositionFragment.getPositionCreated(data);

        //return  null;
    }
}
