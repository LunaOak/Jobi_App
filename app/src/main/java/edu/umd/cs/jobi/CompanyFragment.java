package edu.umd.cs.jobi;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import edu.umd.cs.jobi.model.Company;
import edu.umd.cs.jobi.model.Contact;
import edu.umd.cs.jobi.model.Event;
import edu.umd.cs.jobi.model.Position;
import edu.umd.cs.jobi.service.CompanyService;
import edu.umd.cs.jobi.service.EventService;
import edu.umd.cs.jobi.service.PositionService;

import static android.content.ContentValues.TAG;


public class CompanyFragment extends Fragment {

    private Company company;
    private TextView companyNameLabel;
    private TextView companyLocationLabel;
    private TextView companyDescriptionLabel;
    private PositionService positionService;
    private CompanyService companyService;
    private EventService eventService;
    private static final String COMPANY_ID = "COMPANY_ID";
    private static final int REQUEST_CODE_CREATE_POSITION = 10;
    private static final int REQUEST_CODE_VIEW_POSITION = 11;
    private static final int REQUEST_CODE_CONTACT = 3;
    private static final int REQUEST_CODE_EDIT_COMPANY = 5;
    private static final int REQUEST_CODE_VIEW_EVENT = 4;

    private ImageButton editButton;
    //private Button addContact;
    private Button addPosition;

    private RecyclerView contactList;
    private RecyclerView positionList;
    private RecyclerView eventList;

    private PositionAdapter posAdapter;
    private ContactAdapter contactAdapter;
    private EventAdapter eventAdapter;
    // Dialog boxes for deletion //
    private AlertDialog.Builder positionDeleteBuilder;
    private AlertDialog.Builder contactDeleteBuilder;
    private AlertDialog.Builder eventDeleteBuilder;

    public static CompanyFragment newInstance(String companyId){
        Bundle args = new Bundle();
        args.putString(COMPANY_ID, companyId);
        CompanyFragment fragment = new CompanyFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        String companyId = getArguments().getString(COMPANY_ID);
        Log.d(TAG, "We are about to query the database");
        positionService = DependencyFactory.getPositionService(getActivity().getApplicationContext());
        companyService = DependencyFactory.getCompanyService(getActivity().getApplicationContext());
        eventService = DependencyFactory.getEventService(getActivity().getApplicationContext());
        company = companyService.getCompanyById(companyId);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_company, container, false);


        companyNameLabel = (TextView)view.findViewById(R.id.company_name_label);
        companyLocationLabel = (TextView)view.findViewById(R.id.company_location_label);
        companyDescriptionLabel = (TextView)view.findViewById(R.id.company_description_label);

        if (company != null){
            companyNameLabel.setText(company.getName());
            companyLocationLabel.setText(company.getLocation());
            companyDescriptionLabel.setText(company.getDescription());
        }

        editButton = (ImageButton)view.findViewById(R.id.edit_company_info_button);
        editButton.setOnClickListener(new View.OnClickListener() {

            @Override
        public void onClick(View view) {
                Intent intent = new Intent(getActivity(), EnterCompanyActivity.class);
                intent.putExtra(COMPANY_ID, company.getId());

         //   Intent intent = EnterCompanyActivity.newIntent(getActivity().getApplicationContext(), company.getId());
            startActivityForResult(intent, REQUEST_CODE_EDIT_COMPANY);
        }}

        );
        //addContact = (Button)view.findViewById(R.id.company_add_contact_button);


        addPosition = (Button) view.findViewById(R.id.company_add_position_button);
        addPosition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent enterPositionIntent = EnterPositionActivity.newIntentFromCompany(getActivity(), company.getName());
                startActivityForResult(enterPositionIntent,REQUEST_CODE_CREATE_POSITION);
            }
        });

        positionList = (RecyclerView)view.findViewById(R.id.company_position_list);
        positionList.setLayoutManager(new LinearLayoutManager(getActivity()));


        contactList = (RecyclerView)view.findViewById(R.id.company_contact_list);
        contactList.setLayoutManager(new LinearLayoutManager(getActivity()));

        eventList = (RecyclerView)view.findViewById(R.id.company_event_list);
        eventList.setLayoutManager(new LinearLayoutManager(getActivity()));

        updateUI();
        return view;
    }

    private void updateUI() {
        if (company != null){
            companyNameLabel.setText(company.getName());
            companyLocationLabel.setText(company.getLocation());
            companyDescriptionLabel.setText(company.getDescription());
        }

        List<Position> positions = positionService.getPositionsByCompany(company.getName());

        if (posAdapter == null) {
            posAdapter = new PositionAdapter(positions);
            positionList.setAdapter(posAdapter);
        } else {
            posAdapter.setStories(positions);
            posAdapter.notifyDataSetChanged();
        }

        List<Contact> all_contacts = new ArrayList<Contact>();
        for (Position p:positions){
            List<Contact> posContacts = positionService.getContactsByPosition(p);
            all_contacts.addAll(posContacts);
        }

        if (contactAdapter == null) {
            contactAdapter = new ContactAdapter(all_contacts);
            contactList.setAdapter(contactAdapter);
        } else {
            contactAdapter.setContacts(all_contacts);
            contactAdapter.notifyDataSetChanged();
        }

        List<Event> events = eventService.getEventsByCompanyName(company.getName());
        if (eventAdapter == null){
            eventAdapter = new EventAdapter(events);
            eventList.setAdapter(eventAdapter);
        } else {
            eventAdapter.setStories(events);
            eventAdapter.notifyDataSetChanged();
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {


        if (resultCode != Activity.RESULT_OK || data == null) {
            return;
        }

        if (requestCode == REQUEST_CODE_CREATE_POSITION) {
            if (data == null) {
                return;
            }

            Position positionCreated = PositionActivity.getPositionEdit(data);
            positionService.addPositionToDb(positionCreated);
            String companyName = positionCreated.getCompany();
            if (companyService.getCompanyByName(companyName) == null){
                // If there is no company with the name specified on the position, make a new company
                Company newCompany = new Company(companyName, true);
                companyService.addCompanyToDb(newCompany);
            }
        } else if (requestCode == REQUEST_CODE_EDIT_COMPANY){
            company = EnterCompanyActivity.getCompanyCreated(data);
            companyService.addCompanyToDb(company);
        }

        updateUI();
    }
    // Recycler Views Adapters & Holders ////////////////////////////////////////////////////////
    private class PositionHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView positionTitle;
        private TextView positionSummary;
        private TextView positionCompany;

        private Position position;

        public PositionHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);

            positionTitle = (TextView) itemView.findViewById(R.id.list_item_position_title);
            positionSummary = (TextView) itemView.findViewById(R.id.list_item_position_summary);
            positionCompany = (TextView) itemView.findViewById(R.id.list_item_position_company);

            // Delete Alert Dialog //
            itemView.setOnLongClickListener(new View.OnLongClickListener(){
                @Override
                public boolean onLongClick(View view){
                    positionDeleteBuilder = new AlertDialog.Builder(getActivity());
                    positionDeleteBuilder.setTitle("Delete Position?");
                    positionDeleteBuilder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int which) {
                            positionService.deletePositionById(position.getId());
                            Toast.makeText(getActivity().getApplicationContext(), "Position deleted!", Toast.LENGTH_SHORT).show();
                            updateUI();
                            dialog.dismiss();
                        }
                    });

                    positionDeleteBuilder.setNegativeButton("NO", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });

                    AlertDialog alert = positionDeleteBuilder.create();
                    alert.show();
                    return true;
                }
            });
        }

    public void bindPosition(Position position) {

        this.position = position;

        positionTitle.setText(position.getTitle());
        positionSummary.setText(position.getDescription());
        positionCompany.setText(position.getCompany());
    }

    @Override
    public void onClick(View view) {
        Intent intent = PositionActivity.newIntent(getActivity(), position.getId());
        startActivityForResult(intent, REQUEST_CODE_VIEW_POSITION);
    }
}

    // Position Adapter ///////////////////////////////////////////////////////////////
    private class PositionAdapter extends RecyclerView.Adapter<PositionHolder> {

        private List<Position> positions;

        public PositionAdapter(List<Position> positions) {
            this.positions = positions;
        }

        public void setStories(List<Position> positions) {
            this.positions = positions;
        }

        @Override
        public PositionHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(R.layout.list_item_position, parent, false);
            return new PositionHolder(view);
        }

        @Override
        public void onBindViewHolder(PositionHolder holder, int list_position) {
            Position position = positions.get(list_position);
            holder.bindPosition(position);
        }

        @Override
        public int getItemCount() {
            return positions.size();
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


}
