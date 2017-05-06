package edu.umd.cs.jobi;

import android.support.v4.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
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

    private static final String EXTRA_CONTACT_CREATED = "ContactCreated";
    private static final String ARG_ID = "ID";

    private EditText nameEdit;
    private EditText emailEdit;
    private EditText jobTitleEdit;
    private EditText phoneEdit;

    private Button saveBtn;
    private Button cancelBtn;

    private Contact contact;

    public static EnterContactFragment newInstance(String id) {
        Bundle args = new Bundle();
        args.putString(ARG_ID, id);

        EnterContactFragment fragment = new EnterContactFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contact, container, false);

        nameEdit = (EditText) view.findViewById(R.id.contact_name);
        emailEdit = (EditText) view.findViewById(R.id.contact_email);
        jobTitleEdit = (EditText) view.findViewById(R.id.contact_jobtitle);
        phoneEdit = (EditText) view.findViewById(R.id.contact_phone);

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
