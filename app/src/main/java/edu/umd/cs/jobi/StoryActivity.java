package edu.umd.cs.jobi;


import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import edu.umd.cs.jobi.model.Story;

public class StoryActivity extends SingleFragmentActivity {
    private final String TAG = getClass().getSimpleName();

    private static final String EXTRA_STORY_ID = "STORY_ID";

    @Override
    protected Fragment createFragment() {
        String storyId = getIntent().getStringExtra(EXTRA_STORY_ID);

        return StoryFragment.newInstance(storyId);
    }

    public static Intent newIntent(Context context, String storyId) {
        Intent intent = new Intent(context, StoryActivity.class);
        intent.putExtra(EXTRA_STORY_ID, storyId);
        return intent;
    }

    public static Story getStoryCreated(Intent data) {
        return StoryFragment.getStoryCreated(data);
    }
}
