package edu.umd.cs.jobi;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import edu.umd.cs.jobi.model.Company;
import edu.umd.cs.jobi.model.Event;
import edu.umd.cs.jobi.model.Position;
import edu.umd.cs.jobi.service.CompanyService;


public class EventListFragment extends Fragment {

    private CompanyService companyService;
    private List<Event> allEvents;
    private RecyclerView eventList;
    private EventAdapter adapter;
    private Button newEventButton;
    private String currentTab;
    private TabLayout tabLayout;


    private static final int REQUEST_CODE_CREATE_EVENT = 12;
    private static final int REQUEST_CODE_VIEW_EVENT = 13;

    private AlertDialog.Builder eventDeleteBuilder;

    public static EventListFragment newInstance() {
        EventListFragment fragment = new EventListFragment();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        companyService = DependencyFactory.getCompanyService(getActivity().getApplicationContext());
        allEvents = companyService.getAllEvents();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_eventlist, container, false);

        eventList = (RecyclerView)view.findViewById(R.id.event_list);
        eventList.setLayoutManager(new LinearLayoutManager(getActivity()));
        tabLayout = (TabLayout)view.findViewById(R.id.event_tab_layout);

        newEventButton = (Button)view.findViewById(R.id.add_new_event_button);
        newEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent enterEventIntent = new Intent(getActivity(),
                        EnterEventActivity.class);
                startActivityForResult(enterEventIntent,REQUEST_CODE_CREATE_EVENT);
            }
        });

        currentTab = "All";
        updateUI();
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                String tabText = tab.getText().toString();

                if (tabText.equals(getString(R.string.list_all))) {
                    currentTab = "All";
                    updateUI();
                    //positionList.setText("All positions!");
                } else if (tabText.equals("Interview")) {
                    currentTab = "Interview";
                    updateUI();
                    //positionList.setText("Need to do these applications");
                } else if (tabText.equals("Email")) {
                    currentTab = "Email";
                    updateUI();
                    //positionList.setText("These are in progress");
                } else {
                    currentTab = "Deadline";
                    updateUI();
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == REQUEST_CODE_CREATE_EVENT) {
            if (data == null) {
                return;
            }

            Event eventCreated = EnterEventActivity.getEventCreated(data);
            String companyName = eventCreated.getCompany();
            companyService.addEventToDb(eventCreated);
            String companyId = companyService.getCompanyIdWithName(companyName);
            if (companyId == null){ // If a company with the name doesn't exist, create it
                Company newCompany = new Company(companyName, true);
                companyId = newCompany.getId();
                companyService.addCompanyToDb(newCompany);
            }
            eventCreated.setCompany(companyId);
            String positionName = eventCreated.getPosition();
            String positionId = companyService.getPositionIdWithName(positionName, companyId);
            if (positionId == null){ // If a position with the name does't exist, create it
                Position position = new Position();
                position.setTitle(eventCreated.getPosition());
                position.setCompany(companyId);
                positionId = position.getId();
                companyService.addPositionToDb(position);
            }
            eventCreated.setPosition(positionId);

            companyService.addEventToDb(eventCreated);




        }

        updateUI();
    }

    private void updateUI() {

        List<Event> allEvents = companyService.getAllEvents();

        List<Event> events = new ArrayList<Event>();
        List<Event> interview_events = new ArrayList<Event>();
        List<Event> email_events = new ArrayList<Event>();
        List<Event> deadline_events = new ArrayList<Event>();

        if (currentTab.equals("Interview") == true) {

            for (Event e : allEvents) {
                if (e.getType() == Event.Type.INTERVIEW) {
                    interview_events.add(e);
                }
            }
            events = interview_events;

        } else if (currentTab.equals("Email") == true) {

            for (Event e : allEvents) {
                if (e.getType() == Event.Type.EMAIL) {
                    email_events.add(e);
                }
            }
            events = email_events;

        } else if (currentTab.equals("Deadline") == true) {

            for (Event e : allEvents) {
                if (e.getType() == Event.Type.DEADLINE){
                    deadline_events.add(e);
                }
            }
            events = deadline_events;

        } else {
            events = allEvents;
        }

        if (adapter == null) {
            adapter = new EventAdapter(events);
            eventList.setAdapter(adapter);
        } else {
            adapter.setStories(events);
            adapter.notifyDataSetChanged();
        }
    }

    // Recycler Views Adapters & Holders ////////////////////////////////////////////////////////
    private class EventHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView eventTitleText;
        private TextView typeText;
        private TextView companyText;
        private TextView locationText;
        private TextView positionText;
        private TextView dateText;
        private TextView timeText;

        private Event event;

        public EventHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);

            eventTitleText = (TextView)itemView.findViewById(R.id.list_item_event_title);
            typeText = (TextView)itemView.findViewById(R.id.list_item_event_type);
            companyText = (TextView)itemView.findViewById(R.id.list_item_event_company);
            positionText = (TextView)itemView.findViewById(R.id.list_item_event_position);
            dateText = (TextView)itemView.findViewById(R.id.list_item_event_date);

            // Delete Alert Dialog //
            itemView.setOnLongClickListener(new View.OnLongClickListener(){
                @Override
                public boolean onLongClick(View view){
                    eventDeleteBuilder = new AlertDialog.Builder(getActivity());
                    eventDeleteBuilder.setTitle("Delete Event?");
                    eventDeleteBuilder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int which) {
                            companyService.deleteEventById(event.getId());
                            Toast.makeText(getActivity().getApplicationContext(), "Event deleted!", Toast.LENGTH_SHORT).show();
                            eventList.setLayoutManager(new LinearLayoutManager(getActivity()));
                            updateUI();
                            dialog.dismiss();
                        }
                    });

                    eventDeleteBuilder.setNegativeButton("NO", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });

                    AlertDialog alert = eventDeleteBuilder.create();
                    alert.show();
                    return true;
                }
            });

        }


        public void bindEvent(Event event) {

            this.event = event;
            eventTitleText.setText(event.getTitle());
            typeText.setText(event.getType().toString());
            companyText.setText(companyService.getCompanyNameById(event.getCompany()));
            positionText.setText(companyService.getPositionNameById(event.getPosition()));
            dateText.setText(new SimpleDateFormat("EEE, d MMM yyyy, HH:mm a", Locale.ENGLISH).format(event.getDate()));
        }

        @Override
        public void onClick(View view) {
            Intent intent = EventActivity.newIntent(getActivity(), event.getId());
            startActivityForResult(intent, REQUEST_CODE_VIEW_EVENT);
        }
    }

    // Event Adapter ///////////////////////////////////////////////////////////////
    private class EventAdapter extends RecyclerView.Adapter<EventHolder> {

        private List<Event> events;

        public EventAdapter(List<Event> events) {
            this.events = events;
        }

        public void setStories(List<Event> events) {
            this.events = events;
        }

        @Override
        public EventHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(R.layout.list_item_event, parent, false);
            return new EventHolder(view);
        }

        @Override
        public void onBindViewHolder(EventHolder holder, int list_event) {
            Event event = events.get(list_event);
            holder.bindEvent(event);
        }

        @Override
        public int getItemCount() {
            return events.size();
        }
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
}
