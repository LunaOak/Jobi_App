package edu.umd.cs.jobi;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import edu.umd.cs.jobi.model.Contact;
import edu.umd.cs.jobi.model.Event;
import edu.umd.cs.jobi.model.Reminder;
import edu.umd.cs.jobi.service.EventService;


public class EventFragment extends Fragment {

    public static final String ARG_EVENT_ID = "ARG_EVENT_ID";

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

    private ImageButton editEventBtn;
    private Button setReminderBtn;
    private Button addContactBtn;

    private static final int SECOND = 1000;
    private static final int MINUTE = 60 * SECOND;
    private static final int HOUR = 60 * MINUTE;
    private static final int DAY = 24 * HOUR;

    private static final String EVENT_CREATED = "EVENT_CREATED";


    public static EventFragment newInstance(String eventId) {
        Bundle args = new Bundle();
        args.putString(ARG_EVENT_ID, eventId);

        EventFragment fragment = new EventFragment();
        fragment.setArguments(args);

        return fragment;
    }
    // RecyclerViews //
    private RecyclerView contactsRecyclerView;
    private RecyclerView remindersRecyclerView;

    // Adapters //
    private ContactAdapter contactAdapter;
    private ReminderAdapter reminderAdapter;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String eventId = getArguments().getString(ARG_EVENT_ID);
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

        editEventBtn = (ImageButton) view.findViewById(R.id.event_edit_event_button);
        editEventBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = EnterEventActivity.newIntentEdit(getActivity().getApplicationContext(), event.getId()); //event might be null. SHOULDNT though
                startActivityForResult(intent, REQUEST_CODE_EDIT_EVENT);
            }
        });

        setReminderBtn = (Button) view.findViewById(R.id.event_set_reminder_button);
        setReminderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Todo
//                Intent intent = new Intent(getActivity().getApplicationContext(), EnterEventReminderActivity.class);
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

        contactsRecyclerView = (RecyclerView)view.findViewById(R.id.event_contact_recycler_view);
        contactsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        remindersRecyclerView = (RecyclerView)view.findViewById(R.id.event_reminder_recycler_view);
        remindersRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        updateUI();

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (resultCode != Activity.RESULT_OK) {
//            return;
//        }

        if (requestCode == REQUEST_CODE_EDIT_EVENT) {
            if (data == null) {
                return;
            }
            //Todo
            event = EnterEventActivity.getEventCreated(data);
            eventService.addEventToDb(event);
        } else if(requestCode == REQUEST_CODE_ADD_REMINDER) {
            //Todo
//            if (data == null) {
//                return;
//            }
//
//            Reminder newReminder = EnterEventReminderActivity.getReminderCreated(data);
//            Reminder remReminder = null;
//
//            for (Reminder r : event.getReminders()) {
//                if (r.getId().equals(newReminder.getId)) {
//                    remReminder = r;
//                }
//            }
//
//            if (remContact != null) {
//                event.getReminders().remove(remReminder);
//            }
//
//            event.getReminders().add(newReminder);
//            eventService.addEventToDb(event);
        } else if(requestCode == REQUEST_CODE_ADD_CONTACT) {
            if (data == null) {
                return;
            }

            Contact newContact = EnterContactActivity.getContactCreated(data);
            Contact remContact = null;

            for (Contact c : event.getContacts()) {
                if (c.getId().equals(newContact.getId())) {
                    remContact = c;
                }
            }

            if (remContact != null) {
                event.getContacts().remove(remContact);
            }

            event.getContacts().add(newContact);
            eventService.addEventToDb(event);
        }
        updateUI();
    }

    private void updateUI() {
        if (event != null) {
            titleText.setText(event.getTitle());
            typeText.setText(event.getType().name());
            companyText.setText(event.getCompany());
            locationText.setText(event.getLocation());
            positionText.setText(event.getPosition());
            dateText.setText(new SimpleDateFormat("EEE, d MMM yyyy", Locale.ENGLISH).format(event.getDate())); // Need to format date prettier!
            //"EEE, d MMM yyyy HH:mm:ss Z"
            timeText.setText(new SimpleDateFormat("HH:mm:ss", Locale.ENGLISH).format(event.getDate()));

            List<Reminder> reminders = eventService.getEventById(event.getId()).getReminders();
            List<Contact> contacts = eventService.getEventById(event.getId()).getContacts();

            if (reminderAdapter == null) {
                reminderAdapter = new ReminderAdapter(reminders);
                remindersRecyclerView.setAdapter(reminderAdapter);
            } else {
                reminderAdapter.setReminders(reminders);
                reminderAdapter.notifyDataSetChanged();
            }

            if (contactAdapter == null) {
                contactAdapter = new ContactAdapter(contacts);
                contactsRecyclerView.setAdapter(contactAdapter);
            } else {
                contactAdapter.setContacts(contacts);
                contactAdapter.notifyDataSetChanged();
            }
        }
    }

    private class ReminderAdapter extends RecyclerView.Adapter<ReminderHolder> {
        private List<Reminder> reminders;

        public ReminderAdapter(List<Reminder> reminders) {
            this.reminders = reminders;
        }

        public void setReminders(List<Reminder> reminders) {
            this.reminders = reminders;
        }

        @Override
        public ReminderHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(R.layout.list_item_reminder, parent, false);
            return new ReminderHolder(view);
        }

        @Override
        public void onBindViewHolder(ReminderHolder holder, int position) {
            Reminder reminder = reminders.get(position);
            holder.bindReminder(reminder);
        }

        @Override
        public int getItemCount() {
            return reminders.size();
        }
    }

    private class ReminderHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView reminderText;

        private Reminder reminder;

        public ReminderHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);

            reminderText = (TextView)itemView.findViewById(R.id.list_item_reminder);
        }

        public void bindReminder(Reminder reminder) {
            this.reminder = reminder;

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

            reminderText.setText(timeBefore + "before");
        }

        @Override
        public void onClick(View view) {
//            Intent intent = EnterEventReminderActivity.newIntent(getActivity().getApplicationContext(), reminder.getId(), event.getTitle(), eventDateMs);
//            startActivityForResult(intent, REQUEST_CODE_ADD_REMINDER);
        }
    }

    private class ContactAdapter extends RecyclerView.Adapter<ContactHolder> {
        private List<Contact> contacts;

        public ContactAdapter(List<Contact> contacts) {
            this.contacts = contacts;
        }

        public void setContacts(List<Contact> contacts) {
            this.contacts = contacts;
        }

        @Override
        public ContactHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(R.layout.list_item_contact, parent, false);
            return new ContactHolder(view);
        }

        @Override
        public void onBindViewHolder(ContactHolder holder, int position) {
            Contact contact = contacts.get(position);
            holder.bindContact(contact);
        }

        @Override
        public int getItemCount() {
            return contacts.size();
        }
    }

    private class ContactHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView contactName;
        private TextView contactTitle;
        private TextView contactEmail;
        private TextView contactPhone;

        private Contact contact;

        public ContactHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);

            contactName = (TextView)itemView.findViewById(R.id.list_item_contact_name);
            contactTitle = (TextView)itemView.findViewById(R.id.list_item_contact_title);
            contactEmail = (TextView)itemView.findViewById(R.id.list_item_contact_email);
            contactPhone = (TextView)itemView.findViewById(R.id.list_item_contact_phone);
        }

        public void bindContact(Contact contact) {
            this.contact = contact;

            contactName.setText(contact.getName());
            contactTitle.setText(contact.getJobTitle());
            contactEmail.setText(contact.getEmail());
            contactPhone.setText(contact.getPhone());
        }

        @Override
        public void onClick(View view) {
            Intent intent = EnterContactActivity.newIntent(getActivity(), contact.getId(), false);
            startActivityForResult(intent, REQUEST_CODE_ADD_CONTACT);
        }
    }

    public static Event getEventCreated(Intent data) {
        return (Event)data.getSerializableExtra(EVENT_CREATED);
    }

}
