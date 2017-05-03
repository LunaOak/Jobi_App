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

import edu.umd.cs.jobi.model.Position;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;


public class PositionFragment extends Fragment {

    private final String TAG = getClass().getSimpleName();

    private static final String POSITION_CREATED = "PositionCreated";
    private static final String POSITION_ID = "PositionId";

    private Position position;

    // Interactive Elements //
    private EditText positionTitleEditText;
    private RadioGroup statusRadioGroup;
    private EditText locationEditText;
    private EditText descriptionEditText;
    private Spinner positionTypeSpinner;

    // Buttons //
    private Button saveButton;
    private Button cancelButton;

    public static PositionFragment newInstance(String positionId) {
        Bundle args = new Bundle();
        args.putString(POSITION_ID, positionId);

        PositionFragment fragment = new PositionFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String positionId = getArguments().getString(POSITION_ID);
        position = DependencyFactory.getPositionService(getActivity().getApplicationContext()).getPositionById(positionId);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_position, container, false);

        // Position Title //
        positionTitleEditText = (EditText)view.findViewById(R.id.position);
        if (position != null) {
            positionTitleEditText.setText(position.getTitle());
        }

        // TODO ///////

        // Position Company //
//        companyEditText = (EditText)view.findViewById(R.id.position_company);
//        if (position != null) {
//            companyEditText.setText(position.getCompany());
//        }

        // Position Location //
        locationEditText = (EditText)view.findViewById(R.id.position_location);
        if (position != null) {
            locationEditText.setText(position.getLocation());
        }

        // Position Type //
        positionTypeSpinner = (Spinner)view.findViewById(R.id.position_type_spinner);
        ArrayAdapter<CharSequence> statusAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.position_type_array, android.R.layout.simple_spinner_item);
        statusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        positionTypeSpinner.setAdapter(statusAdapter);
        if (position != null) {
            positionTypeSpinner.setSelection(position.getPositionType());
        }

        // Position Status //
        statusRadioGroup = (RadioGroup)view.findViewById(R.id.position_radio_group);
        if (position != null) {
            switch (position.getStatus()) {
                case TODO:
                    statusRadioGroup.check(R.id.position_todo);
                    break;
                case IN_PROGRESS:
                    statusRadioGroup.check(R.id.position_inProgress);
                    break;
                case DONE:
                    statusRadioGroup.check(R.id.position_done);
                    break;
                default:
                    statusRadioGroup.check(R.id.position_todo);
                    break;
            }
        } else {
            statusRadioGroup.check(R.id.position_todo);
        }

        // Position Description //
        descriptionEditText = (EditText)view.findViewById(R.id.position_description);
        if (position != null) {
            descriptionEditText.setText(position.getDescription());
        }

        // TODO ///////

//        // Position Contact //
//        typeEditText = (EditText)view.findViewById(R.id.position_type);
//        if (position != null) {
//            typeEditText.setText(position.getType());
//        }
//

        saveButton = (Button)view.findViewById(R.id.save_story_button);
        saveButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if (inputsAreValid()) {
                    if (position == null) {
                        position = new Position();
                    }

                    position.setTitle(positionTitleEditText.getText().toString());
                    
                    int statusId = statusRadioGroup.getCheckedRadioButtonId();
                    switch (statusId) {
                        case R.id.position_todo:
                            position.setStatus(Position.Status.TODO);
                            break;
                        case R.id.position_inProgress:
                            position.setStatus(Position.Status.IN_PROGRESS);
                            break;
                        case R.id.position_done:
                            position.setStatus(Position.Status.DONE);
                            break;
                        default:
                            position.setStatus(Position.Status.TODO);
                            break;
                    }
                    
                    position.setLocation(locationEditText.getText().toString());
                    position.setDescription(descriptionEditText.getText().toString());

                    position.setType(positionTypeSpinner.getSelectedItemPosition());

                    // We need to add the contact and company update here too and add the company to the
                    // database

                    Intent data = new Intent();
                    data.putExtra(POSITION_CREATED, position);
                    getActivity().setResult(RESULT_OK, data);
                    getActivity().finish();
                }
            }
        });

        cancelButton = (Button)view.findViewById(R.id.cancel_story_button);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().setResult(RESULT_CANCELED);
                getActivity().finish();
            }
        });

        return view;
    }

    public static Position getPositionCreated(Intent data) {
        return (Position) data.getSerializableExtra(POSITION_CREATED);
    }

    // TODO ////////
    private boolean inputsAreValid() {
        // For reference
//        private EditText positionTitleEditText;
//        private RadioGroup statusRadioGroup;
//        private EditText locationEditText;
//        private EditText descriptionEditText;
//        private Spinner positionTypeSpinner;

        if (positionTitleEditText.getText().toString().length() < 0) {
            // Error message
            positionTitleEditText.setError("You must enter a position title");
            return false;
        }
        if (locationEditText.getText().toString().length() < 0) {
            // Error message
            locationEditText.setError("You must enter a location");
            return false;
        }

        return true;
    }
}
