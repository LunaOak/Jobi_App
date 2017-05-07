package edu.umd.cs.jobi;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.concurrent.RecursiveAction;

import edu.umd.cs.jobi.model.Company;


public class CompanyFragment extends Fragment {

    private Company company;
    private TextView companyNameLabel;
    private TextView companyLocationLabel;
    private TextView companyDescriptionLabel;

    private ImageButton editButton;
    private Button addContact;
    private Button addPosition;

    private RecyclerView contactList;
    private RecyclerView.Recycler positionList;

    public static CompanyFragment newInstance(){
        CompanyFragment fragment = new CompanyFragment();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_company, container, false);

        companyNameLabel = (TextView)view.findViewById(R.id.company_name_label);
        companyLocationLabel = (TextView)view.findViewById(R.id.company_location_label);
        companyDescriptionLabel = (TextView)view.findViewById(R.id.company_description_label);

        if (company != null){
            companyNameLabel.setText(company.getName());
            companyLocationLabel.setText(company.getLocation());
            companyDescriptionLabel.setText(company.getDescription());
        }

        editButton = (ImageButton)view.findViewById(R.id.edit_company_info_button);
        addContact = (Button)view.findViewById(R.id.company_add_contact_button);
        addPosition = (Button) view.findViewById(R.id.company_add_position_button);

        return view;
    }




}
