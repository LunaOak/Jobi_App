package edu.umd.cs.jobi;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class CompanyFragment extends Fragment {

    private TabLayout tabLayout;
    private TextView companyList;

    public static CompanyFragment newInstance() {
        CompanyFragment fragment = new CompanyFragment();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_company, container, false);

        tabLayout = (TabLayout)view.findViewById(R.id.company_tab_layout);
        companyList = (TextView)view.findViewById(R.id.company_list);
        companyList.setText("All Companies!"); //TODO change this to be the list of all companies

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getText().equals(getString(R.string.all_companies))) {
                    companyList.setText("All Companies!");
                } else {
                    companyList.setText("Current Companies");
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
    } }
