package edu.umd.cs.jobi;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;

import edu.umd.cs.jobi.R;
import edu.umd.cs.jobi.model.Story;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;


public class StoryFragment extends Fragment {
    private final String TAG = getClass().getSimpleName();

    private static final String EXTRA_STORY_CREATED = "StoryCreated";
    private static final String ARG_STORY_ID = "StoryId";

    private Story story;

    private EditText summaryEditText;
    private EditText acceptanceCriteriaEditText;
    private EditText storyPointsEditText;
    private RadioGroup priorityRadioGroup;
    private Spinner statusSpinner;

    private Button saveButton;
    private Button cancelButton;

    public static StoryFragment newInstance(String storyId) {
        Bundle args = new Bundle();
        args.putString(ARG_STORY_ID, storyId);

        StoryFragment fragment = new StoryFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String storyId = getArguments().getString(ARG_STORY_ID);
        story = DependencyFactory.getStoryService(getActivity().getApplicationContext()).getStoryById(storyId);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_story, container, false);

//        summaryEditText = (EditText)view.findViewById(R.id.summary);
//        if (story != null) {
//            summaryEditText.setText(story.getSummary());
//        }
//
//        acceptanceCriteriaEditText = (EditText)view.findViewById(R.id.criteria);
//        if (story != null) {
//            acceptanceCriteriaEditText.setText(story.getAcceptanceCriteria());
//        }
//
//        storyPointsEditText = (EditText)view.findViewById(R.id.points);
//        if (story != null) {
//            storyPointsEditText.setText("" + story.getStoryPoints());
//        }
//
//        priorityRadioGroup = (RadioGroup)view.findViewById(R.id.radio_group);
//        if (story != null) {
//            switch (story.getPriority()) {
//                case CURRENT:
//                    priorityRadioGroup.check(R.id.radio_current);
//                    break;
//                case NEXT:
//                    priorityRadioGroup.check(R.id.radio_next);
//                    break;
//                case LATER:
//                    priorityRadioGroup.check(R.id.radio_later);
//                    break;
//                default:
//                    priorityRadioGroup.check(R.id.radio_later);
//                    break;
//            }
//        } else {
//            priorityRadioGroup.check(R.id.radio_later);
//        }
//
//        statusSpinner = (Spinner)view.findViewById(R.id.spinner);
//        ArrayAdapter<CharSequence> statusAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.status_array, android.R.layout.simple_spinner_item);
//        statusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        statusSpinner.setAdapter(statusAdapter);
//        if (story != null) {
//            statusSpinner.setSelection(story.getStatusPosition());
//        }
//
//        saveButton = (Button)view.findViewById(R.id.save_story_button);
//        saveButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (inputsAreValid()) {
//                    if (story == null) {
//                        story = new Story();
//                    }
//
//                    story.setSummary(summaryEditText.getText().toString());
//                    story.setAcceptanceCriteria(acceptanceCriteriaEditText.getText().toString());
//                    story.setStoryPoints(Double.parseDouble(storyPointsEditText.getText().toString()));
//
//                    int priorityId = priorityRadioGroup.getCheckedRadioButtonId();
//                    switch (priorityId) {
//                        case R.id.radio_current:
//                            story.setPriorityCurrent();
//                            break;
//                        case R.id.radio_next:
//                            story.setPriorityNext();
//                            break;
//                        case R.id.radio_later:
//                            story.setPriorityLater();
//                            break;
//                        default:
//                            story.setPriorityLater();
//                            break;
//                    }
//
//                    story.setStatus(statusSpinner.getSelectedItemPosition());
//
//                    Intent data = new Intent();
//                    data.putExtra(EXTRA_STORY_CREATED, story);
//                    getActivity().setResult(RESULT_OK, data);
//                    getActivity().finish();
//                }
//            }
//        });
//
//        cancelButton = (Button)view.findViewById(R.id.cancel_story_button);
//        cancelButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                getActivity().setResult(RESULT_CANCELED);
//                getActivity().finish();
//            }
//        });

        return view;
    }

    public static Story getStoryCreated(Intent data) {
        return (Story)data.getSerializableExtra(EXTRA_STORY_CREATED);
    }

    private boolean inputsAreValid() {
        return
                summaryEditText.getText().toString().length() > 0 &&
                acceptanceCriteriaEditText.getText().toString().length() > 0 &&
                storyPointsEditText.getText().toString().length() > 0 &&
                statusSpinner.getSelectedItemPosition() >= 0;
    }
}
