package edu.umd.cs.jobi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import edu.umd.cs.jobi.model.Company;
import edu.umd.cs.jobi.service.CompanyService;


public class CompanyListFragment extends Fragment {

    private TabLayout tabLayout;
    private RecyclerView companyList;
    private Button newCompanyButton;
    private CompanyService companyService;
    private List<Company> allCompanies;
    private List<Company> currentCompanies;
    private List<Company> favoriteCompanies;

    private static final int REQUEST_CODE_CREATE_COMPANY = 0;

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
        currentCompanies = companyService.getCurrentCompanies();
        favoriteCompanies = companyService.getFavoriteCompanies();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_companylist, container, false);

        tabLayout = (TabLayout)view.findViewById(R.id.company_tab_layout);
        companyList = (RecyclerView)view.findViewById(R.id.company_list);

        //companyList.setText(allCompanies.toString()); //TODO change this to be the list of all companies

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getText().equals(getString(R.string.list_all))) {
                    //companyList.setText("All Companies!");
                } else {
                    //companyList.setText("Current Companies");
                }
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                // do nothing
            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                onTabSelected(tab);
            }
        });

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
        if (resultCode != Activity.RESULT_OK){
            return;
        }

        if (requestCode == REQUEST_CODE_CREATE_COMPANY) {
            if (data == null){
                return;
            }

            Company companyCreated = EnterCompanyActivity.getCompanyCreated(data);
            companyService.addCompanyToDb(companyCreated);
            updateUI();
        }
    }


    private void updateUI(){
        //TODO
    }
}
