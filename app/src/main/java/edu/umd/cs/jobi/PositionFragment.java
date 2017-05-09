package edu.umd.cs.jobi;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import edu.umd.cs.jobi.model.Company;
import edu.umd.cs.jobi.model.Contact;
import edu.umd.cs.jobi.model.Event;
import edu.umd.cs.jobi.model.Position;
import edu.umd.cs.jobi.service.CompanyService;
import edu.umd.cs.jobi.service.EventService;
import edu.umd.cs.jobi.service.PositionService;

import static android.R.drawable.btn_star_big_off;
import static android.R.drawable.btn_star_big_on;


public class PositionFragment extends Fragment {

    private final String TAG = getClass().getSimpleName();

    private static final String POSITION_ID = "POSITION_ID";
    private static final int REQUEST_CODE_EDIT_POSITION = 0;
    private static final int REQUEST_CODE_CONTACT = 1;
    private static final int REQUEST_CODE_ADD_EVENT = 2;
    private static final int REQUEST_CODE_VIEW_COMPANY = 11;
    private static final int REQUEST_CODE_EVENT = 3;
    private static final String POSITION_CREATED = "POSITION_CREATED";

    private Position position;

    // TextViews //
    private TextView positionTitle;
    private TextView companyName;
    private TextView companyLocation;
    private TextView positionType;
    private TextView positionStatus;
    private TextView positionDescription;

    // Buttons //
    private ToggleButton favoriteButton;
    private ImageButton editPositionButton;
    private Button addNewContactButton;
    private Button addNewEventButton;

    // Services //
    private PositionService positionService;
    private CompanyService companyService;
    private EventService eventService;

    // RecyclerViews //
    private RecyclerView contactsRecyclerView;
    private RecyclerView eventsRecyclerView;

    // Adapters //
    private ContactAdapter contactAdapter;
    private EventAdapter eventAdapter;

    // Dialog boxes for deletion //
    private AlertDialog.Builder contactDeleteBuilder;

    public static PositionFragment newInstance(String positionId) {
        Bundle args = new Bundle();
        args.putString(POSITION_ID, positionId);

        PositionFragment fragment = new PositionFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String positionId = getArguments().getString(POSITION_ID);
        position = DependencyFactory.getPositionService(getActivity().getApplicationContext()).getPositionById(positionId);
        companyService = DependencyFactory.getCompanyService(getActivity().getApplicationContext());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_position, container, false);

        // Services declaration //
        eventService = DependencyFactory.getEventService(getActivity().getApplication().getApplicationContext());
        positionService = DependencyFactory.getPositionService(getActivity().getApplication().getApplicationContext());

        // Position Title //
        positionTitle = (TextView) view.findViewById(R.id.positionTitle);

        // Position Company //
        companyName = (TextView) view.findViewById(R.id.companyName);

        companyName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Company companyToEnter = companyService.getCompanyByName(position.getCompany());
                Intent intent = CompanyActivity.newIntent(getActivity(), companyToEnter.getId());
                startActivityForResult(intent,REQUEST_CODE_VIEW_COMPANY);
            }
        });



        // Position Location //
        companyLocation = (TextView) view.findViewById(R.id.companyLocation);

        // Position Type //
        positionType = (TextView) view.findViewById(R.id.positionType);

        // Position Status //
        positionStatus = (TextView) view.findViewById(R.id.positionStatus);

        // Position Description //
        positionDescription = (TextView) view.findViewById(R.id.positionDescription);

        //// Button Actions

        // Edit Position Button //
        editPositionButton = (ImageButton)view.findViewById(R.id.edit_position_button);
        editPositionButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = EnterPositionActivity.newIntent(getActivity().getApplicationContext(), position.getId());
                startActivityForResult(intent, REQUEST_CODE_EDIT_POSITION);
            }
        });

        // Favorite Button //
        favoriteButton = (ToggleButton)view.findViewById(R.id.favorite_button);

        if (position.getFavorite() == Position.Favorite.YES) {
            favoriteButton.setChecked(true);
            favoriteButton.setBackgroundDrawable(ContextCompat.getDrawable(getActivity().getApplicationContext(), btn_star_big_on));
        } else {
            favoriteButton.setChecked(false);
            favoriteButton.setBackgroundDrawable(ContextCompat.getDrawable(getActivity().getApplicationContext(), btn_star_big_off));
        }
        favoriteButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    favoriteButton.setBackgroundDrawable(ContextCompat.getDrawable(getActivity().getApplicationContext(), btn_star_big_on));
                    position.setFavorite(Position.Favorite.YES);
                } else {
                    favoriteButton.setBackgroundDrawable(ContextCompat.getDrawable(getActivity().getApplicationContext(), btn_star_big_off));
                    position.setFavorite(Position.Favorite.NO);
                }
                positionService.addPositionToDb(position);
            }

        });

        // Add New Contact Button //
        addNewContactButton = (Button)view.findViewById(R.id.add_contact_button);
        addNewContactButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(getActivity().getApplicationContext(), EnterContactActivity.class);
                startActivityForResult(intent, REQUEST_CODE_CONTACT);
            }
        });

        // Add New Event Button //
        addNewEventButton = (Button)view.findViewById(R.id.add_new_event_button);
        addNewEventButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = EnterEventActivity.newIntentCreate(getActivity().getApplicationContext(), position.getTitle(), position.getCompany());
                startActivityForResult(intent, REQUEST_CODE_ADD_EVENT);
            }
        });

        // Recycler Views //
        contactsRecyclerView = (RecyclerView)view.findViewById(R.id.position_contact_recycler_view);
        contactsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        eventsRecyclerView = (RecyclerView)view.findViewById(R.id.position_event_recycler_view);
        eventsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));



        updateUI();

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK || data == null) {
            return;
        }

        if (requestCode == REQUEST_CODE_EDIT_POSITION) {
            position = EnterPositionActivity.getPositionCreated(data);
            positionService.addPositionToDb(position);
        } else if (requestCode == REQUEST_CODE_CONTACT) {
            Contact newContact = EnterContactActivity.getContactCreated(data);
            Contact remContact = null;

            for (Contact c: position.getContacts()) {
                if (c.getId().equals(newContact.getId())){
                    remContact = c;
                }
            }

            if (remContact != null) {
                position.getContacts().remove(remContact);
            }

            position.getContacts().add(newContact);
            positionService.addPositionToDb(position);
        } else if (requestCode == REQUEST_CODE_ADD_EVENT) {
            Event newEvent = EnterEventActivity.getEventCreated(data);
            eventService.addEventToDb(newEvent);

            // If a company was specified under the event
            if (newEvent.getCompany() != null && newEvent.getCompany() != "") {

                // Update company database
                if (companyService.getCompanyByName(newEvent.getCompany()) == null) {
                    // If there is no company with the name specified on the event make it
                    Company newCompany = new Company(newEvent.getCompany(), true);
                    companyService.addCompanyToDb(newCompany);
                }

                // Update position database
                if (newEvent.getPosition() != null && newEvent.getPosition() !="") {
                    // If a position was specified
                    List<Position> possiblePositions = positionService.getPositionsByCompany(newEvent.getCompany());
                    boolean posExists = false;
                    for (Position p:possiblePositions){
                        if (p.getTitle().equals(newEvent.getPosition())){
                            posExists = true;
                        }
                    }

                    if (posExists == false){
                        // If position doesnt exist create it
                        Position position = new Position();
                        position.setTitle(newEvent.getPosition());
                        position.setCompany(newEvent.getCompany());
                        positionService.addPositionToDb(position);
                    }
                }
            }
        }
        updateUI();
    }

    private void updateUI() {
        positionTitle.setText(position.getTitle());
        companyName.setText(position.getCompany());
        companyLocation.setText(position.getLocation());
        positionType.setText(position.getType().name());
        positionStatus.setText(position.getStatus().name());
        positionDescription.setText(position.getDescription());

        List<Contact> contacts = positionService.getPositionById(position.getId()).getContacts();
        List<Event> events = eventService.getEventsByPositionTitle(position.getTitle());

        if (contactAdapter == null) {
            contactAdapter = new ContactAdapter(contacts);
            contactsRecyclerView.setAdapter(contactAdapter);
        } else {
            contactAdapter.setContacts(contacts);
            contactAdapter.notifyDataSetChanged();
        }

        if (eventAdapter == null) {
            eventAdapter = new EventAdapter(events);
            eventsRecyclerView.setAdapter(eventAdapter);
        } else {
            eventAdapter.setEvents(events);
            eventAdapter.notifyDataSetChanged();
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

            // Delete Alert Dialog //
            contactDeleteBuilder = new AlertDialog.Builder(getActivity());
            contactDeleteBuilder.setTitle("Delete Contact?");
            contactDeleteBuilder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int which) {
                    positionService.deleteContactById(contact.getId());
                    Toast.makeText(getActivity().getApplicationContext(), "Contact deleted!", Toast.LENGTH_SHORT).show();
                    updateUI();
                    dialog.dismiss();
                }
            });

            contactDeleteBuilder.setNegativeButton("NO", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener(){
                @Override
                public boolean onLongClick(View view){
                    AlertDialog alert = contactDeleteBuilder.create();
                    alert.show();
                    return true;
                }
            });
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
            startActivityForResult(intent, REQUEST_CODE_CONTACT);
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
            View view = layoutInflater.inflate(R.layout.list_item_event_position, parent, false);
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

    private class EventHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView eventDate;
        private TextView eventTitle;
        private TextView eventType;

        private Event event;

        public EventHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);

            eventDate = (TextView)itemView.findViewById(R.id.list_item_event_date);
            eventTitle = (TextView)itemView.findViewById(R.id.list_item_event_title);
            eventType = (TextView)itemView.findViewById(R.id.list_item_event_type);
        }

        public void bindEvent(Event event) {
            this.event = event;

            eventDate.setText(new SimpleDateFormat("EEE, MMM d", Locale.ENGLISH).format(event.getDate()) + " - ");
            eventTitle.setText(event.getTitle());
            eventType.setText(event.getType().name());
        }

        @Override
        public void onClick(View view) {
            Intent intent = EnterEventActivity.newIntentEdit(getActivity(), event.getId());
            startActivityForResult(intent, REQUEST_CODE_EVENT);
        }
    }

    public static Position getPositionCreated(Intent data) {
        return (Position)data.getSerializableExtra(POSITION_CREATED);
    }
}
