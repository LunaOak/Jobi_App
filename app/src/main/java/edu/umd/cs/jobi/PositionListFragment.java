package edu.umd.cs.jobi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
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

import java.util.List;

import edu.umd.cs.jobi.model.Position;
import edu.umd.cs.jobi.service.PositionService;

public class PositionListFragment extends Fragment {

    private PositionService positionService;
    private List<Position> allPositions;
    private RecyclerView positionList;
    private TabLayout tabLayout;
    private Button newPositionButton;
    private static final int REQUEST_CODE_CREATE_POSITION = 10;
    private PositionAdapter adapter;


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

        // Recycler Views //
        positionList = (RecyclerView)view.findViewById(R.id.position_list);
        positionList.setLayoutManager(new LinearLayoutManager(getActivity()));



        //positionList.setText("All Positions!"); //TODO change this to be the list of all companies

        newPositionButton = (Button)view.findViewById(R.id.add_new_position_button);
        newPositionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent enterPositionIntent = new Intent(getActivity(),
                        EnterPositionActivity.class);
                startActivityForResult(enterPositionIntent,REQUEST_CODE_CREATE_POSITION);
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

        updateUI();
        return view;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }

        if (requestCode == REQUEST_CODE_CREATE_POSITION) {
            if (data == null) {
                return;
            }

            Position positionCreated = PositionActivity.getPositionEdit(data);
            positionService.addPositionToDb(positionCreated);
            updateUI();
        }
    }

    private void updateUI() {

        List<Position> positions = positionService.getAllPositions();

        if (adapter == null) {
            adapter = new PositionAdapter(positions);
            positionList.setAdapter(adapter);
        } else {
            adapter.setStories(positions);
            adapter.notifyDataSetChanged();
        }
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

            positionTitle = (TextView)itemView.findViewById(R.id.list_item_position_title);
            positionSummary = (TextView)itemView.findViewById(R.id.list_item_position_summary);
            positionCompany = (TextView)itemView.findViewById(R.id.list_item_position_company);
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
            startActivityForResult(intent, REQUEST_CODE_CREATE_POSITION);
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

    // Menu Bar Management ///////////////////////////////////////////////////////////////////
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
}

