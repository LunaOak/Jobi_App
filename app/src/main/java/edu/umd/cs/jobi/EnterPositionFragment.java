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
import edu.umd.cs.jobi.service.CompanyService;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;


public class EnterPositionFragment extends Fragment {

    private final String TAG = getClass().getSimpleName();

    private static final String POSITION_CREATED = "POSITION_CREATED";
    private static final String POSITION_ID = "POSITION_ID";
    private static final String COMPANY_NAME = "COMPANY_NAME";
    private static final String FROM_COMPANY_BOOLEAN = "FROM_COMPANY_BOOLEAN";

    private Position position;
    private String companyName;
    private Boolean fromCompany;

    // Interactive Elements //
    private EditText positionTitleEditText;
    private RadioGroup statusRadioGroup;
    private EditText companyEditText;
    private EditText locationEditText;
    private EditText descriptionEditText;
    private Spinner positionTypeSpinner;

    // Buttons //
    private Button saveButton;
    private Button cancelButton;

    private CompanyService companyService;


    public static EnterPositionFragment newInstance(String positionId, String companyName, Boolean fromCompany) {
        Bundle args = new Bundle();
        args.putString(POSITION_ID, positionId);
        args.putString(COMPANY_NAME, companyName);
        args.putBoolean(FROM_COMPANY_BOOLEAN, fromCompany);

        EnterPositionFragment fragment = new EnterPositionFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String positionId = getArguments().getString(POSITION_ID);
        fromCompany = getArguments().getBoolean(FROM_COMPANY_BOOLEAN);
        companyName = getArguments().getString(COMPANY_NAME);

        companyService = DependencyFactory.getCompanyService(getActivity().getApplicationContext());
        position = companyService.getPositionById(positionId);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_position_enter, container, false);

        // Position Title //
        positionTitleEditText = (EditText)view.findViewById(R.id.position);
        if (position != null) {
            positionTitleEditText.setText(position.getTitle());
        }

        // Position Company //
        companyEditText = (EditText)view.findViewById(R.id.position_company);
        if (position != null) {
            companyEditText.setText(companyService.getCompanyNameById(position.getCompany()));
        } else if (fromCompany) {
            companyEditText.setText(companyName);
        }

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

        saveButton = (Button)view.findViewById(R.id.save_position_button);
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
                    position.setCompany(companyEditText.getText().toString());
                    position.setLocation(locationEditText.getText().toString());
                    position.setDescription(descriptionEditText.getText().toString());
                    position.setType(positionTypeSpinner.getSelectedItemPosition());


                    Intent data = new Intent();
                    data.putExtra(POSITION_CREATED, position);
                    getActivity().setResult(RESULT_OK, data);
                    getActivity().finish();
                }
            }
        });

        cancelButton = (Button)view.findViewById(R.id.cancel_position_button);
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

    // ToDo: Check that the error messages work
    private boolean inputsAreValid() {
        if (positionTitleEditText.getText().toString().length() < 0) {
            // Error message
            positionTitleEditText.setError("You must enter a position title");
            return false;
        }
        if (companyEditText.getText().toString().length() < 0) {
            // Error message
            companyEditText.setError("You must enter a company");
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
