package edu.umd.cs.jobi;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Spinner;

import edu.umd.cs.jobi.model.Reminder;

/**
 * Created by Juan on 5/6/2017.
 */

public class EnterEventReminderFragment extends Fragment {

    private static final String ARG_REMINDER_ID = "ARG_REMINDER_ID";
    private static final String EXTRA_REMINDER_CREATED = "EXTRA_REMINDER_CREATED";

    private Reminder reminder;

    private Button saveBtn;
    private Button cancelBtn;
    private Spinner spinner;

    public static EnterEventReminderFragment newInstance(String reminderId) {
        Bundle args = new Bundle();
        args.putString(ARG_REMINDER_ID, reminderId);

        EnterEventReminderFragment fragment = new EnterEventReminderFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String reminderId = getArguments().getString(ARG_REMINDER_ID);

        reminder = DependencyFactory.getEventService(getActivity().getApplicationContext()).getReminderById(reminderId);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_event_enter, container, false);

        saveBtn = (Button) view.findViewById(R.id.save_reminder_button);
        saveBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

            }
        });

        cancelBtn = (Button) view.findViewById(R.id.cancel_reminder_button);
        cancelBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

            }
        });

        spinner = (Spinner) view.findViewById(R.id.reminder_spinner);

        //Todo Pauline write here
        return view;
    }

    public static Reminder getReminderCreated(Intent data) {
        return (Reminder)data.getSerializableExtra(EXTRA_REMINDER_CREATED);
    }
}
