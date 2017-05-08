package edu.umd.cs.jobi;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.List;
import java.util.concurrent.RecursiveAction;
import android.content.DialogInterface;
import android.widget.Toast;

import edu.umd.cs.jobi.model.Company;
import edu.umd.cs.jobi.model.Position;
import edu.umd.cs.jobi.service.CompanyService;
import edu.umd.cs.jobi.service.PositionService;

import static android.content.ContentValues.TAG;
import static edu.umd.cs.jobi.R.id.position;


public class CompanyFragment extends Fragment {

    private Company company;
    private TextView companyNameLabel;
    private TextView companyLocationLabel;
    private TextView companyDescriptionLabel;
    private PositionService positionService;
    private CompanyService companyService;
    private static final String COMPANY_ID = "COMPANY_ID";
    private static final int REQUEST_CODE_CREATE_POSITION = 10;
    private static final int REQUEST_CODE_VIEW_POSITION = 11;

    private ImageButton editButton;
    private Button addContact;
    private Button addPosition;

    private RecyclerView contactList;
    private RecyclerView positionList;

    private PositionAdapter posAdapter;
    // Dialog boxes for deletion //
    private AlertDialog.Builder positionDeleteBuilder;

    public static CompanyFragment newInstance(String companyId){
        Bundle args = new Bundle();
        args.putString(COMPANY_ID, companyId);
        CompanyFragment fragment = new CompanyFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        String companyId = getArguments().getString(COMPANY_ID);
        Log.d(TAG, "We are about to query the database");
        positionService = DependencyFactory.getPositionService(getActivity().getApplicationContext());
        companyService = DependencyFactory.getCompanyService(getActivity().getApplicationContext());
        company = companyService.getCompanyById(companyId);
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
        addPosition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent enterPositionIntent = new Intent(getActivity(),
                        EnterPositionActivity.class);
                startActivityForResult(enterPositionIntent,REQUEST_CODE_CREATE_POSITION);
            }
        });

        positionList = (RecyclerView)view.findViewById(R.id.company_position_list);
        positionList.setLayoutManager(new LinearLayoutManager(getActivity()));


        contactList = (RecyclerView)view.findViewById(R.id.company_contact_list);
        contactList.setLayoutManager(new LinearLayoutManager(getActivity()));

        updateUI();
        return view;
    }

    private void updateUI() {

        List<Position> positions = positionService.getPositionsByCompany(company.getName());

        if (posAdapter == null) {
            posAdapter = new PositionAdapter(positions);
            positionList.setAdapter(posAdapter);
        } else {
            posAdapter.setStories(positions);
            posAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {


        if (requestCode == REQUEST_CODE_CREATE_POSITION) {
            if (data == null) {
                return;
            }

            Position positionCreated = PositionActivity.getPositionEdit(data);
            positionService.addPositionToDb(positionCreated);
            String companyName = positionCreated.getCompany();
            if (companyService.getCompanyByName(companyName) == null){
                // If there is no company with the name specified on the position, make a new company
                Company newCompany = new Company(companyName, true);
                companyService.addCompanyToDb(newCompany);
            }
        }

        updateUI();
    }
    // Recycler Views Adapters & Holders ////////////////////////////////////////////////////////
    private class PositionHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView positionTitle;
        private TextView positionSummary;
        private TextView positionCompany;

        private Position position;

        public PositionHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);

            positionTitle = (TextView) itemView.findViewById(R.id.list_item_position_title);
            positionSummary = (TextView) itemView.findViewById(R.id.list_item_position_summary);
            positionCompany = (TextView) itemView.findViewById(R.id.list_item_position_company);

            // Delete Alert Dialog //
            positionDeleteBuilder = new AlertDialog.Builder(getActivity());
            positionDeleteBuilder.setTitle("Delete Position?");
            positionDeleteBuilder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int which) {
                    positionService.deletePositionById(position.getId());
                    Toast.makeText(getActivity().getApplicationContext(), "Position deleted!", Toast.LENGTH_SHORT).show();
                    updateUI();
                dialog.dismiss();
            }
        });

        positionDeleteBuilder.setNegativeButton("NO", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        itemView.setOnLongClickListener(new View.OnLongClickListener(){
            @Override
            public boolean onLongClick(View view){
                AlertDialog alert = positionDeleteBuilder.create();
                alert.show();
                return true;
            }
        });
    }

    public void bindPosition(Position position) {

        this.position = position;

        positionTitle.setText(position.getTitle());
        positionSummary.setText(position.getDescription());
        positionCompany.setText(position.getCompany());
    }

    @Override
    public void onClick(View view) {
        Intent intent = PositionActivity.newIntent(getActivity(), position.getId());
        startActivityForResult(intent, REQUEST_CODE_VIEW_POSITION);
    }
}

    // Position Adapter ///////////////////////////////////////////////////////////////
    private class PositionAdapter extends RecyclerView.Adapter<PositionHolder> {

        private List<Position> positions;

        public PositionAdapter(List<Position> positions) {
            this.positions = positions;
        }

        public void setStories(List<Position> positions) {
            this.positions = positions;
        }

        @Override
        public PositionHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(R.layout.list_item_position, parent, false);
            return new PositionHolder(view);
        }

        @Override
        public void onBindViewHolder(PositionHolder holder, int list_position) {
            Position position = positions.get(list_position);
            holder.bindPosition(position);
        }

        @Override
        public int getItemCount() {
            return positions.size();
        }
    }




}
