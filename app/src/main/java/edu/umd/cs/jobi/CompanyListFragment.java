package edu.umd.cs.jobi;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import edu.umd.cs.jobi.model.Company;
import edu.umd.cs.jobi.service.CompanyService;

import static android.content.ContentValues.TAG;


public class CompanyListFragment extends Fragment {

    private RecyclerView companyList;
    private Button newCompanyButton;
    private CompanyService companyService;
    private List<Company> allCompanies;

    private CompanyAdapter adapter;

    private AlertDialog.Builder companyDeleteBuilder;

    private static final int REQUEST_CODE_CREATE_COMPANY = 0;
    private static final int REQUEST_CODE_VIEW_COMPANY = 11;

    public static CompanyListFragment newInstance() {
        CompanyListFragment fragment = new CompanyListFragment();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);


        companyService = DependencyFactory.getCompanyService(getActivity().getApplicationContext());
        allCompanies = companyService.getAllCompanies();
       // adapter = new CompanyAdapter(allCompanies);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_companylist, container, false);

        companyList = (RecyclerView)view.findViewById(R.id.company_list);
        companyList.setLayoutManager(new LinearLayoutManager(getActivity()));

        updateUI();

        newCompanyButton = (Button)view.findViewById(R.id.add_new_company_button);

        newCompanyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent companyIntent = new Intent(getActivity(),
                        EnterCompanyActivity.class);
                startActivityForResult(companyIntent, REQUEST_CODE_CREATE_COMPANY);
            }
        });
        return view;
    }

    private void setListAsText(List<Company> companyList, TextView textView) {

    }

    // Menu Bar Management //
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_home, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_home:
                Intent homeIntent = new Intent(getActivity(), HomeActivity.class);
                startActivity(homeIntent);
                return true;
            case R.id.menu_item_settings:
                Intent settingsIntent = new Intent(getActivity(), SettingsActivity.class);
                startActivity(settingsIntent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        /*if (resultCode != Activity.RESULT_OK){
            return;
        }*/

        if (requestCode == REQUEST_CODE_CREATE_COMPANY) {
            if (data == null){
                return;
            }

            Company companyCreated = EnterCompanyActivity.getCompanyCreated(data);
            companyService.addCompanyToDb(companyCreated);

        }
        updateUI();
    }


    private void updateUI(){
        Log.d(TAG, "updating UI of companies");
        allCompanies = companyService.getAllCompanies();

        if (adapter == null){
            adapter = new CompanyAdapter(allCompanies);
            companyList.setAdapter(adapter);
        } else {
            adapter.setStories(allCompanies);
            adapter.notifyDataSetChanged();
        }

        }

    // Recycler Views Adapters & Holders ////////////////////////////////////////////////////////
    private class CompanyHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView companyName;
        private Company company;

        public CompanyHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);

            companyName = (TextView)itemView.findViewById(R.id.list_item_company_name);

            companyDeleteBuilder = new AlertDialog.Builder(getActivity());
            companyDeleteBuilder.setTitle("Delete Company?");
            companyDeleteBuilder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int which) {
                    companyService.deleteCompanyById(company.getId());
                    Toast.makeText(getActivity().getApplicationContext(), "Company deleted!", Toast.LENGTH_SHORT).show();
                    companyList.setLayoutManager(new LinearLayoutManager(getActivity()));
                    updateUI();
                    dialog.dismiss();
                }
            });

            companyDeleteBuilder.setNegativeButton("NO", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener(){
                @Override
                public boolean onLongClick(View view){
                    AlertDialog alert = companyDeleteBuilder.create();
                    alert.show();
                    return true;
                }
            });


        }


        public void bindCompany(Company company) {

            this.company = company;
            if (company != null) {
                companyName.setText(company.getName());
            }

        }

        @Override
        public void onClick(View view) {
            Log.d(TAG, "The id of this company is" + company.getId());
            Log.d(TAG, "The name of this company is" + company.getName());
           Intent intent = CompanyActivity.newIntent(getActivity(), company.getId());
            startActivityForResult(intent, REQUEST_CODE_VIEW_COMPANY);
        }
    }

    // Company Adapter ///////////////////////////////////////////////////////////////
    private class CompanyAdapter extends RecyclerView.Adapter<CompanyHolder> {

        private List<Company> companies;

        public CompanyAdapter(List<Company> companies) {
            this.companies = companies;
        }

        public void setStories(List<Company> companies) {
            this.companies = companies;
        }

        @Override
        public CompanyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(R.layout.list_item_company, parent, false);
            return new CompanyHolder(view);
        }

        @Override
        public void onBindViewHolder(CompanyHolder holder, int list_position) {
            Company position = companies.get(list_position);
            holder.bindCompany(position);
        }

        @Override
        public int getItemCount() {
            return companies.size();
        }
    }
}
