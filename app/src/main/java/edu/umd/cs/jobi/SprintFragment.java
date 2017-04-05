package edu.umd.cs.jobi;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import edu.umd.cs.agileandroid.R;
import edu.umd.cs.jobi.model.Story;
import edu.umd.cs.jobi.service.StoryService;

public class SprintFragment extends Fragment {

    private StoryService storyService;

    public static SprintFragment newInstance() {
        return new SprintFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sprint, container, false);

        storyService = DependencyFactory.getStoryService(getActivity().getApplicationContext());

        LinearLayout todoColumn = (LinearLayout)view.findViewById(R.id.todo_column);
        LinearLayout inProgressColumn = (LinearLayout)view.findViewById(R.id.inprogress_column);
        LinearLayout doneColumn = (LinearLayout)view.findViewById(R.id.done_column);

        List<Story> stories = storyService.getCurrentSprintStories();
        for (final Story story : stories) {
            TextView textView = new TextView(getActivity());
            textView.setText(story.getSummary());
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(getActivity(), story.toString(), Toast.LENGTH_SHORT).show();
                }
            });

            if (story.getStatus().equals(Story.Status.TODO)) {
                todoColumn.addView(textView);
            } else if (story.getStatus().equals(Story.Status.IN_PROGRESS)) {
                inProgressColumn.addView(textView);
            } else {
                doneColumn.addView(textView);
            }
        }

        return view;
    }
}
