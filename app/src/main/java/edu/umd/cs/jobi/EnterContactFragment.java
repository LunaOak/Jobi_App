package edu.umd.cs.jobi;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import edu.umd.cs.jobi.model.Contact;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

/**
 * Created by Juan on 5/4/2017.
 */

public class EnterContactFragment extends Fragment {

    private static final String EXTRA_CONTACT_CREATED = "EXTRA_CONTACT_CREATED";
    private static final String ARG_CONTACT_ID = "ARG_CONTACT_ID";
    private static final String ARG_FROM_EVENT = "ARG_FROM_EVENT";

    private EditText nameEdit;
    private EditText emailEdit;
    private EditText jobTitleEdit;
    private EditText phoneEdit;

    private Button saveBtn;
    private Button cancelBtn;

    private Contact contact;

    public static EnterContactFragment newInstance(String contactId, Boolean fromEvent) {
        Bundle args = new Bundle();
        args.putString(ARG_CONTACT_ID, contactId);
        args.putBoolean(ARG_FROM_EVENT, fromEvent);

        EnterContactFragment fragment = new EnterContactFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String contactId = getArguments().getString(ARG_CONTACT_ID);
        Boolean fromEvent = getArguments().getBoolean(ARG_FROM_EVENT);

        if(fromEvent) {
            contact = DependencyFactory.getEventService(getActivity().getApplicationContext()).getContactById(contactId);
        } else {
            contact = DependencyFactory.getPositionService(getActivity().getApplicationContext()).getContactById(contactId);
        }

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contact, container, false);

        nameEdit = (EditText) view.findViewById(R.id.contact_name);
        if (contact != null) {
            nameEdit.setText(contact.getName());
        }

        emailEdit = (EditText) view.findViewById(R.id.contact_email);
        if (contact != null) {
            emailEdit.setText(contact.getEmail());
        }

        jobTitleEdit = (EditText) view.findViewById(R.id.contact_jobtitle);
        if (contact != null) {
            jobTitleEdit.setText(contact.getJobTitle());
        }

        phoneEdit = (EditText) view.findViewById(R.id.contact_phone);
        if (contact != null) {
            phoneEdit.setText(contact.getPhone());
        }

        saveBtn = (Button) view.findViewById(R.id.save_contact_button);
        saveBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String name = nameEdit.getText().toString();
                String email = emailEdit.getText().toString();
                String jobTitle = jobTitleEdit.getText().toString();
                String phone = phoneEdit.getText().toString();


                if (contact == null) {
                    contact = new Contact(name, jobTitle, email, phone);
                } else {
                    contact.setName(name);
                    contact.setEmail(email);
                    contact.setPhone(phone);
                    contact.setJobTitle(jobTitle);
                }

                Intent data = new Intent();
                data.putExtra(EXTRA_CONTACT_CREATED, contact);
                getActivity().setResult(RESULT_OK, data);
                getActivity().finish();
            }
        });

        cancelBtn = (Button) view.findViewById(R.id.cancel_contact_button);
        cancelBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                getActivity().setResult(RESULT_CANCELED);
                getActivity().finish();
            }
        });

        return view;
    }

    public static Contact getContactCreated(Intent data) {
        return (Contact)data.getSerializableExtra(EXTRA_CONTACT_CREATED);
    }
}
