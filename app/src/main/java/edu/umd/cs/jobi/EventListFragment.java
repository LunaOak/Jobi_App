package edu.umd.cs.jobi;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import edu.umd.cs.jobi.model.Event;
import edu.umd.cs.jobi.service.EventService;

public class EventListFragment extends Fragment {

    private EventService eventService;
    private List<Event> allEvents;
    private RecyclerView eventList;

    public static EventListFragment newInstance() {
        EventListFragment fragment = new EventListFragment();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        eventService = DependencyFactory.getEventService(getActivity().getApplicationContext());
        allEvents = eventService.getAllEvents();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_eventlist, container, false);

        eventList = (RecyclerView)view.findViewById(R.id.event_list);
        //eventList.setText("List of events"); // TODO : make this and all lists RecyclerViews

        return view;
    }
}
