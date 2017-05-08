package edu.umd.cs.jobi;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;


import edu.umd.cs.jobi.model.Company;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;


public class EnterCompanyFragment extends Fragment {

    private Company company;

    private EditText companyName;
    private EditText companyLocation;
    private EditText companyDescription;

    private Button saveButton;
    private Button cancelButton;

    private Button newContactButton;

    private final String TAG = getClass().getSimpleName();

    private static final String COMPANY_CREATED = "CompanyCreated";
    private static final String COMPANY_ID = "CompanyId";



    public static EnterCompanyFragment newInstance(String companyId){
        Bundle args = new Bundle();
        args.putString(COMPANY_ID, companyId);
        EnterCompanyFragment fragment = new EnterCompanyFragment();
        fragment.setArguments(args);
        return fragment;

    }

    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String companyId = getArguments().getString(COMPANY_ID);
        Log.d(TAG, "The company id is " + companyId);
        company = DependencyFactory.getCompanyService(getActivity().getApplicationContext()).getCompanyById(companyId);



    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_company_enter, container, false);

        companyName = (EditText)view.findViewById(R.id.company_name);
        companyLocation = (EditText)view.findViewById(R.id.company_location);
        companyDescription=(EditText)view.findViewById(R.id.company_description);

        if (company != null){
            companyName.setText(company.getName());
            companyLocation.setText(company.getLocation());
            companyDescription.setText(company.getDescription());
        }

        saveButton = (Button)view.findViewById(R.id.save_company_button);
        cancelButton = (Button)view.findViewById(R.id.cancel_company_button);

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().setResult(RESULT_CANCELED);
                getActivity().finish();
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (inputsAreValid()) {
                    if (company == null) {
                        company = new Company(companyName.getText().toString(), true);
                    }
                    company.setName(companyName.getText().toString());
                    company.setLocation(companyLocation.getText().toString());
                    company.setDescription(companyDescription.getText().toString());

                    Intent data = new Intent();
                    data.putExtra(COMPANY_CREATED, company);
                    getActivity().setResult(RESULT_OK, data);
                    getActivity().finish();
                }
            }
        });



        return view;
    }

    // Currently, nothing can be left blank for name, location, or description
    private boolean inputsAreValid() {
        return
                companyName.getText().toString().length() > 0 &&
                        companyLocation.getText().toString().length() > 0 &&
                        companyDescription.getText().toString().length() > 0;
    }

    public static Company getCompanyCreated(Intent data){
        return (Company)data.getSerializableExtra(COMPANY_CREATED);
    }

}
