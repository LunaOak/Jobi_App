package edu.umd.cs.jobi;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class PositionListFragment extends Fragment {

    private TabLayout tabLayout;
    private TextView positionList;

    public static PositionListFragment newInstance() {
        PositionListFragment fragment = new PositionListFragment();
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

        View view = inflater.inflate(R.layout.fragment_positionlist, container, false);

        tabLayout = (TabLayout)view.findViewById(R.id.position_tab_layout);
        positionList = (TextView)view.findViewById(R.id.position_list);
        positionList.setText("All Positions!"); //TODO change this to be the list of all companies

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                String tabText = tab.getText().toString();

                if (tabText.equals(getString(R.string.list_all))) {
                    positionList.setText("All positions!");
                } else if (tabText.equals(getString(R.string.positions_todo))) {
                    positionList.setText("Need to do these applications");
                } else if (tabText.equals(getString(R.string.positions_ongoing))) {
                    positionList.setText("These are in progress");
                } else {
                    // R.string.positions_done
                    positionList.setText("These are all done!");
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

}

