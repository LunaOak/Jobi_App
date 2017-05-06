package edu.umd.cs.jobi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Locale;

import edu.umd.cs.jobi.model.Contact;
import edu.umd.cs.jobi.model.Event;
import edu.umd.cs.jobi.model.Reminder;
import edu.umd.cs.jobi.service.EventService;


public class EventFragment extends Fragment {

    public static final String EVENT_ID = "EVENT_ID";

    private static final int REQUEST_CODE_EDIT_EVENT = 0;
    private static final int REQUEST_CODE_ADD_REMINDER = 1;
    private static final int REQUEST_CODE_ADD_CONTACT = 2;

    private Event event;

    private EventService eventService;

    private TextView titleText;
    private TextView typeText;
    private TextView companyText;
    private TextView locationText;
    private TextView positionText;
    private TextView dateText;
    private TextView timeText;
    private TextView remindersText;
    private TextView contactsText;

    private ImageButton editEventBtn;
    private Button setReminderBtn;
    private Button addContactBtn;

    private static final int SECOND = 1000;
    private static final int MINUTE = 60 * SECOND;
    private static final int HOUR = 60 * MINUTE;
    private static final int DAY = 24 * HOUR;

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
        View view = inflater.inflate(R.layout.fragment_event, container, false);

        eventService = DependencyFactory.getEventService(getActivity().getApplication().getApplicationContext());

        titleText = (TextView) view.findViewById(R.id.event_title);
        typeText = (TextView) view.findViewById(R.id.event_type);
        companyText = (TextView) view.findViewById(R.id.event_company);
        locationText = (TextView) view.findViewById(R.id.event_location);
        positionText = (TextView) view.findViewById(R.id.event_position);
        dateText = (TextView) view.findViewById(R.id.event_date);
        timeText = (TextView) view.findViewById(R.id.event_time);
        remindersText = (TextView) view.findViewById(R.id.event_reminders);
        contactsText = (TextView) view.findViewById(R.id.event_contacts);

        editEventBtn = (ImageButton) view.findViewById(R.id.event_edit_event_button);
        editEventBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = EventCreateActivity.newIntent(getActivity(), event.getId()); //event might be null. SHOULDNT though
//
//                startActivityForResult(intent, REQUEST_CODE_EDIT_EVENT);
            }
        });

        setReminderBtn = (Button) view.findViewById(R.id.event_set_reminder_button);
        setReminderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = EventReminderCreateActivity.newIntent(getActivity(), event.getId()); //event might be null. SHOULDNT though
//
//                startActivityForResult(intent, REQUEST_CODE_ADD_REMINDER);
            }
        });

        addContactBtn = (Button) view.findViewById(R.id.event_add_contact_button);
        addContactBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =  new Intent(getActivity().getApplicationContext(), EnterContactActivity.class);

                startActivityForResult(intent, REQUEST_CODE_ADD_CONTACT);
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

        if (requestCode == REQUEST_CODE_EDIT_EVENT) {
            if (data == null) {
                return;
            }

//            event = EventCreateActivity.getEventCreated(data);
//            eventService.addEventToDb(event);
//            updateUI();
        } else if(requestCode == REQUEST_CODE_ADD_REMINDER) {
//            event.getReminders().add(EventReminderCreateActivity.getReminderCreated(data));
//            eventService.addEventToDb(event);
//            updateUI();
        } else if(requestCode == REQUEST_CODE_ADD_CONTACT) {
            event.getContacts().add(EnterContactActivity.getContactCreated(data));
            eventService.addEventToDb(event);
            updateUI();
        }
    }

    private void updateUI() {
        if (event != null) {
            titleText.setText(event.getTitle());
            typeText.setText(event.getType());
            companyText.setText(event.getCompany());
            locationText.setText(event.getLocation());
            positionText.setText(event.getPosition());
            dateText.setText(new SimpleDateFormat("EEE, d MMM yyyy", Locale.ENGLISH).format(event.getDate())); // Need to format date prettier!
            //"EEE, d MMM yyyy HH:mm:ss Z"
            timeText.setText(new SimpleDateFormat("HH:mm:ss", Locale.ENGLISH).format(event.getDate()));

            remindersText.setText("");
            for (Reminder reminder : event.getReminders()) {
                long ms = event.getDate().getTime() - reminder.getDate().getTime();
                String timeBefore= "";

                if (ms > DAY) {
                    timeBefore += (Long.toString(ms/DAY) + " days ");
                    ms %= DAY;
                }

                if (ms > HOUR) {
                    timeBefore += (Long.toString(ms/HOUR) + " hours ");
                    ms %= HOUR;
                }

                if (ms > MINUTE) {
                    timeBefore += (Long.toString(ms/MINUTE) + " minutes ");
                    ms %= MINUTE;
                }

                remindersText.append(reminder.getTitle() + ":" + timeBefore + "before");
                remindersText.append(System.getProperty("line.separator"));
            }

            contactsText.setText("");
            for (Contact contact: event.getContacts()) {
                contactsText.append(contact.getName() + " " + contact.getPhone() + " " + contact.getEmail());
                contactsText.append(System.getProperty("line.separator"));
            }
        }
    }
}
