package edu.umd.cs.jobi;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import java.util.Date;

import edu.umd.cs.jobi.model.Reminder;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

/**
 * Created by Juan on 5/6/2017.
 */

public class EnterEventReminderFragment extends Fragment {

    private static final String ARG_REMINDER_ID = "ARG_REMINDER_ID";
    private static final String ARG_EVENT_TITLE = "ARG_EVENT_TITLE";
    private static final String ARG_EVENT_DATE_MS = "ARG_EVENT_DATE_MS";
    private static final String EXTRA_REMINDER_CREATED = "EXTRA_REMINDER_CREATED";

    private Reminder reminder;
    private String eventTitle;
    private long eventDateMs;


    // Interactive Elements //
    private Spinner reminderSpinner;

    // Buttons //
    private Button saveButton;
    private Button cancelButton;

    // Constants
    private static final int SECOND = 1000;
    private static final int MINUTE = 60 * SECOND;
    private static final int REMINDER_ARRAY_ELEM_1 = 10; // 10 minutes before
    private static final int REMINDER_ARRAY_ELEM_2 = 30; // 30 minutes before
    private static final int REMINDER_ARRAY_ELEM_3 = 60; // 1 hour before


    public static EnterEventReminderFragment newInstance(String reminderId, String eventTitle, long eventDateMs) {
        Bundle args = new Bundle();
        args.putString(ARG_REMINDER_ID, reminderId);
        args.putString(ARG_EVENT_TITLE, eventTitle);
        args.putLong(ARG_EVENT_DATE_MS, eventDateMs);

        EnterEventReminderFragment fragment = new EnterEventReminderFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String reminderId = getArguments().getString(ARG_REMINDER_ID);
        eventTitle = getArguments().getString(ARG_EVENT_TITLE);
        eventDateMs = getArguments().getLong(ARG_EVENT_DATE_MS);

        reminder = DependencyFactory.getEventService(getActivity().getApplicationContext()).getReminderById(reminderId);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reminder, container, false);

        // Reminder Spinner //
        reminderSpinner = (Spinner)view.findViewById(R.id.reminder_spinner);
        ArrayAdapter<CharSequence> statusAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.reminder_array, android.R.layout.simple_spinner_item);
        statusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        reminderSpinner.setAdapter(statusAdapter);
        if (reminder != null) {
            int reminderTime = (int)((eventDateMs - reminder.getDate().getTime()) / MINUTE);

            switch(reminderTime) {
                case REMINDER_ARRAY_ELEM_1:
                    reminderSpinner.setSelection(0);
                    break;
                case REMINDER_ARRAY_ELEM_2:
                    reminderSpinner.setSelection(1);
                    break;
                case REMINDER_ARRAY_ELEM_3:
                    reminderSpinner.setSelection(2);
                    break;
                default:
                    reminderSpinner.setSelection(0);
                    break;
            }
        }

        saveButton = (Button)view.findViewById(R.id.save_story_button);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (reminder == null) {
                    reminder = new Reminder();
                }
                reminder.setTitle(eventTitle);

                int pos = reminderSpinner.getSelectedItemPosition();
                Date newDate = new Date();
                switch(pos) {
                    case 0:
                        newDate.setTime(REMINDER_ARRAY_ELEM_1 * MINUTE);
                        reminder.setDate(newDate);
                        break;
                    case 1:
                        newDate.setTime(REMINDER_ARRAY_ELEM_2 * MINUTE);
                        reminder.setDate(newDate);
                        break;
                    case 2:
                        newDate.setTime(REMINDER_ARRAY_ELEM_3 * MINUTE);
                        reminder.setDate(newDate);
                        break;
                    default:
                        newDate.setTime(REMINDER_ARRAY_ELEM_1 * MINUTE);
                        reminder.setDate(newDate);
                        break;
                }

                Intent data = new Intent();
                data.putExtra(EXTRA_REMINDER_CREATED, reminder);
                getActivity().setResult(RESULT_OK, data);
                getActivity().finish();
            }
        });

        cancelButton = (Button)view.findViewById(R.id.cancel_reminder_button);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().setResult(RESULT_CANCELED);
                getActivity().finish();
            }
        });

        return view;
    }

    public static Reminder getReminderCreated(Intent data) {
        return (Reminder)data.getSerializableExtra(EXTRA_REMINDER_CREATED);
    }
}
