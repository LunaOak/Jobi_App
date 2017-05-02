package edu.umd.cs.jobi;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import java.util.*;
import java.text.*;

import java.util.List;

import edu.umd.cs.jobi.model.Event;
import edu.umd.cs.jobi.service.StoryService;

public class HomeFragment extends Fragment {

    private final String TAG = getClass().getSimpleName();
    private static final int REQUEST_CODE_CREATE_EVENT = 0;

    private StoryService storyService;

    private RecyclerView eventRecyclerView;
    private EventAdapter adapter;


    // Labels and Date
    private TextView day;
    private TextView month;
    private TextView date;
    private TextView year;

    // Companies and Positions List field members
    private Button companyListButton;
    private Button positionListButton;
    private Button createPositionButton;

    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        //storyService = DependencyFactory.getStoryService(getActivity().getApplicationContext());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_backlog, container, false);

        eventRecyclerView = (RecyclerView)view.findViewById(R.id.story_recycler_view);
        eventRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        day = (TextView) view.findViewById(R.id.day);
        month = (TextView) view.findViewById(R.id.month);
        date = (TextView) view.findViewById(R.id.date);
        year = (TextView) view.findViewById(R.id.year);

        final Calendar c = Calendar.getInstance();
        SimpleDateFormat dayFormat = new SimpleDateFormat("EEEE", Locale.US);

        day.setText(new StringBuilder().append(dayFormat.format(c.getTime())));
        month.setText(new StringBuilder().append(getMonthShortName(c.get(Calendar.MONTH))).append(" "));
        date.setText(new StringBuilder().append(c.get(Calendar.DATE)).append(" "));
        year.setText(new StringBuilder().append(c.get(Calendar.YEAR)));


        companyListButton = (Button)view.findViewById(R.id.company_list_button);
        companyListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent companyListIntent = new Intent(getActivity(),
                        CompanyActivity.class);
                startActivity(companyListIntent);
            }
        });
        positionListButton = (Button)view.findViewById(R.id.position_list_button);
        positionListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent positionListIntent = new Intent(getActivity(),
                        PositionActivity.class);
                startActivity(positionListIntent);
            }
        });

        //updateUI();

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }

        if (requestCode == REQUEST_CODE_CREATE_EVENT) {
            if (data == null) {
                return;
            }

            //Event eventCreated = EventActivity.getEventCreated(data);
            //storyService.addStoryToBacklog(eventCreated);
            updateUI();
        }
    }

    private void updateUI() {
        Log.d(TAG, "updating UI all stories");

        //List<Event> events = eventService.getAllStories();

        if (adapter == null) {
            //adapter = new EventAdapter(events);
            eventRecyclerView.setAdapter(adapter);
        } else {
            //adapter.setEvents(events);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_backlog, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_create_story:
                Intent createStoryIntent = new Intent(getActivity(), StoryActivity.class);
                startActivityForResult(createStoryIntent, REQUEST_CODE_CREATE_EVENT);
                return true;
            case R.id.menu_item_active_sprint:
                Intent activeSprintIntent = new Intent(getActivity(), SprintActivity.class);
                startActivity(activeSprintIntent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private class EventHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView summaryTextView;
        private TextView criteria;
        private TextView priorityTextView;
        private TextView pointsTextView;

        private Event event;

        public EventHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);

//            summaryTextView = (TextView)itemView.findViewById(R.id.list_item_story_summary);
//            criteria = (TextView)itemView.findViewById(R.id.list_item_story_criteria);
//            priorityTextView = (TextView)itemView.findViewById(R.id.list_item_story_priority);
//            pointsTextView = (TextView)itemView.findViewById(R.id.list_item_story_points);
        }

        public void bindEvent(Event event) {
            this.event = event;

//            summaryTextView.setText(story.getSummary());
//            criteria.setText(story.getAcceptanceCriteria());
//            priorityTextView.setText(story.getPriority().toString());
//            pointsTextView.setText("" + story.getStoryPoints());
        }

        @Override
        public void onClick(View view) {
            //Intent intent = EventActivity.newIntent(getActivity(), event.getId());
            //startActivityForResult(intent, REQUEST_CODE_CREATE_EVENT);
        }
    }
 
    private class EventAdapter extends RecyclerView.Adapter<EventHolder> {
        private List<Event> events;

        public EventAdapter(List<Event> events) {
            this.events = events;
        }

        public void setEvents(List<Event> events) {
            this.events = events;
        }

        @Override
        public EventHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(R.layout.list_item_story, parent, false);
            return new EventHolder(view);
        }

        @Override
        public void onBindViewHolder(EventHolder holder, int position) {
            Event event = events.get(position);
            holder.bindEvent(event);
        }

        @Override
        public int getItemCount() {
            return events.size();
        }
    }


    public static String getMonthShortName(int monthNumber) {
        String monthName="";

        if(monthNumber>=0 && monthNumber<12)
            try
            {
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.MONTH, monthNumber);

                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMM");
                simpleDateFormat.setCalendar(calendar);
                monthName = simpleDateFormat.format(calendar.getTime());
            }
            catch (Exception e)
            {
                if(e!=null)
                    e.printStackTrace();
            }
        return monthName;
    }
}
