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

import java.util.ArrayList;
import java.util.List;

import edu.umd.cs.jobi.model.Company;
import edu.umd.cs.jobi.model.Event;
import edu.umd.cs.jobi.model.Position;
import edu.umd.cs.jobi.service.CompanyService;
import edu.umd.cs.jobi.service.EventService;
import edu.umd.cs.jobi.service.PositionService;

public class EventListFragment extends Fragment {

    private EventService eventService;
    private PositionService positionService;
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

        eventService = DependencyFactory.getEventService(getActivity().getApplicationContext());
        companyService = DependencyFactory.getCompanyService(getActivity().getApplicationContext());
        positionService = DependencyFactory.getPositionService(getActivity().getApplicationContext());
        allEvents = eventService.getAllEvents();
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
            eventService.addEventToDb(eventCreated);

            // If a company was specified under the event
            if (eventCreated.getCompany() != null && eventCreated.getCompany() != "") {

                // Update company database
                if (companyService.getCompanyByName(eventCreated.getCompany()) == null) {
                    // If there is no company with the name specified on the event make it
                    Company newCompany = new Company(eventCreated.getCompany(), true);
                    companyService.addCompanyToDb(newCompany);
                }

                // Update position database
                if (eventCreated.getPosition() != null && eventCreated.getPosition() !="") {
                    // If a position was specified
                    List<Position> possiblePositions = positionService.getPositionsByCompany(eventCreated.getCompany());
                    boolean posExists = false;
                    for (Position p:possiblePositions){
                        if (p.getTitle().equals(eventCreated.getPosition())){
                            posExists = true;
                        }
                    }

                    if (posExists == false){
                        // If position doesnt exist create it
                        Position position = new Position();
                        position.setTitle(eventCreated.getPosition());
                        position.setCompany(eventCreated.getCompany());
                        positionService.addPositionToDb(position);
                    }
                }
            }

        }

        updateUI();
    }

    private void updateUI() {

        List<Event> allEvents = eventService.getAllEvents();

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
                            eventService.deleteEventById(event.getId());
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
            companyText.setText(event.getCompany());
            positionText.setText(event.getPosition());
            dateText.setText(event.getDate().toString());
        }

        @Override
        public void onClick(View view) {
            Intent intent = EventActivity.newIntent(getActivity(), event.getId());
            startActivityForResult(intent, REQUEST_CODE_VIEW_EVENT);
        }
    }

    // Position Adapter ///////////////////////////////////////////////////////////////
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
