package edu.umd.cs.jobi;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.List;

import edu.umd.cs.jobi.model.Position;
import edu.umd.cs.jobi.service.PositionService;

public class PositionListFragment extends Fragment {

    private PositionService positionService;
    private List<Position> allPositions;
    private RecyclerView positionList;
    private TabLayout tabLayout;
    private Button newPositionButton;

    public static PositionListFragment newInstance() {
        PositionListFragment fragment = new PositionListFragment();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        positionService = DependencyFactory.getPositionService(getActivity().getApplicationContext());
        allPositions = positionService.getAllPositions();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_positionlist, container, false);

        tabLayout = (TabLayout)view.findViewById(R.id.position_tab_layout);
        positionList = (RecyclerView)view.findViewById(R.id.position_list);
        //positionList.setText("All Positions!"); //TODO change this to be the list of all companies

        newPositionButton = (Button)view.findViewById(R.id.add_new_position_button);
        newPositionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent enterPositionIntent = new Intent(getActivity(),
                        EnterPositionActivity.class);
                startActivity(enterPositionIntent);
            }
        });

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                String tabText = tab.getText().toString();

                if (tabText.equals(getString(R.string.list_all))) {
                    //positionList.setText("All positions!");
                } else if (tabText.equals(getString(R.string.positions_todo))) {
                    //positionList.setText("Need to do these applications");
                } else if (tabText.equals(getString(R.string.positions_ongoing))) {
                    //positionList.setText("These are in progress");
                } else {
                    // R.string.positions_done
                    //positionList.setText("These are all done!");
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

