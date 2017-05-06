package edu.umd.cs.jobi;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
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
        return view;
    }

    private void setListAsText(List<Company> companyList, TextView textView) {

    }

}
