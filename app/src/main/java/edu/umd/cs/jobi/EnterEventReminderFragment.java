package edu.umd.cs.jobi;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

/**
 * Created by Juan on 5/6/2017.
 */

public class EnterEventReminderFragment extends Fragment {

    private static final String ARG_REMINDER_ID = "ARG_REMINDER_ID";


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

        //reminder = DependencyFactory.getEventService(getActivity().getApplicationContext()).getReminderById(reminderId);

    }
}
