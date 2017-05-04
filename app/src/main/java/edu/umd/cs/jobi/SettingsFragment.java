package edu.umd.cs.jobi;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import edu.umd.cs.jobi.model.Story;
import edu.umd.cs.jobi.service.StoryService;

public class SettingsFragment extends Fragment {

    public static SettingsFragment newInstance() {
        return new SettingsFragment();
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

       // storyService = DependencyFactory.getStoryService(getActivity().getApplicationContext());

//        LinearLayout todoColumn = (LinearLayout)view.findViewById(R.id.todo_column);
//        LinearLayout inProgressColumn = (LinearLayout)view.findViewById(R.id.inprogress_column);
//        LinearLayout doneColumn = (LinearLayout)view.findViewById(R.id.done_column);
//
//        List<Story> stories = storyService.getCurrentSprintStories();
//        for (final Story story : stories) {
//            TextView textView = new TextView(getActivity());
//            textView.setText(story.getSummary());
//            textView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    Toast.makeText(getActivity(), story.toString(), Toast.LENGTH_SHORT).show();
//                }
//            });
//
//            if (story.getStatus().equals(Story.Status.TODO)) {
//                todoColumn.addView(textView);
//            } else if (story.getStatus().equals(Story.Status.IN_PROGRESS)) {
//                inProgressColumn.addView(textView);
//            } else {
//                doneColumn.addView(textView);
//            }
//        }

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_home, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
//            case R.id.menu_item_create_story:
//                Intent createStoryIntent = new Intent(getActivity(), StoryActivity.class);
//                startActivityForResult(createStoryIntent, REQUEST_CODE_CREATE_EVENT);
//                return true;
            case R.id.menu_item_home:
                Intent homeIntent = new Intent(getActivity(), HomeActivity.class);
                startActivity(homeIntent);
                return true;
            case R.id.menu_item_settings:
//                Intent settingsIntent = new Intent(getActivity(), SettingsActivity.class);
//                startActivity(settingsIntent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
