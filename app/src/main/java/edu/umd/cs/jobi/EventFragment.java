package edu.umd.cs.jobi;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import edu.umd.cs.jobi.model.Event;


public class EventFragment extends Fragment {

    public static final String EVENT_ID = "EVENT_ID";

    private Event event;

    public static EventFragment newInstance() {
        return new EventFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String eventId = getArguments().getString(EVENT_ID);
        event = DependencyFactory.getEventService(getActivity().getApplicationContext()).getEventById(eventId);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sprint, container, false);


        return view;
    }
}
