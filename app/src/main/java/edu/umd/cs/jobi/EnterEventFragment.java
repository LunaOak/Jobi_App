package edu.umd.cs.jobi;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import edu.umd.cs.jobi.model.Event;

/**
 * Created by Pauline on 5/6/2017.
 */

public class EnterEventFragment extends Fragment implements DatePickerDialog.OnDateSetListener{
    private final String TAG = getClass().getSimpleName();

    private static final String EXTRA_EVENT_CREATED = "EXTRA_EVENT_CREATED";
    private static final String ARG_EVENT_ID = "ARG_EVENT_ID";

    private Event event;

    // Interactive Elements //
    private EditText eventName;
    private EditText eventLocation;
    private Spinner eventTypeSpinner;

    // Date and Time pickers //
    private TextView eventDate;
    private TextView eventTime;
    private int mYear;
    private int mMonth;
    private int mDay;

    // Buttons //
    private Button eventDateButton;
    private Button eventTimeButton;
    private Button saveButton;
    private Button cancelButton;

    public static EnterEventFragment newInstance(String eventId) {
        Bundle args = new Bundle();
        args.putString(ARG_EVENT_ID, eventId);

        EnterEventFragment fragment = new EnterEventFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String eventId = getArguments().getString(ARG_EVENT_ID);
        event = DependencyFactory.getEventService(getActivity().getApplicationContext()).getEventById(eventId);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_event_enter, container, false);

        // Event name //
        eventName = (EditText)view.findViewById(R.id.event_name);
        if (event != null) {
            eventName.setText(event.getTitle());
        }

        // Event location //
        eventLocation = (EditText)view.findViewById(R.id.event_location);
        if (event != null) {
            eventLocation.setText(event.getLocation());
        }

        // Event type //
        eventTypeSpinner = (Spinner)view.findViewById(R.id.event_type_spinner);
        ArrayAdapter<CharSequence> statusAdapter = ArrayAdapter.createFromResource(getActivity(), R.array.event_type_array, android.R.layout.simple_spinner_item);
        statusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        eventTypeSpinner.setAdapter(statusAdapter);
        if (event != null) {
            eventTypeSpinner.setSelection(event.getPositionType());
        }

        // Event date text //
        eventDate = (TextView)view.findViewById(R.id.event_date_text);
        if (event != null) {
            eventDate.setText(new SimpleDateFormat("EEE, d MMM YYYY", Locale.ENGLISH).format(event.getDate()));
        }

        // Event date button //
        eventDateButton = (Button)view.findViewById(R.id.event_date_button);
        eventDateButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                DialogFragment newFragment = new DatePickerFragment();
                newFragment.show(getFragmentManager(), "DatePicker");
            }
        });

        // Event time text //
        eventTime = (TextView)view.findViewById(R.id.event_time_text);
        if (event != null) {
            eventTime.setText(new SimpleDateFormat("HH:mm", Locale.ENGLISH).format(event.getDate()));
        }

        // Event time button //
        eventTimeButton = (Button)view.findViewById(R.id.event_time_button);
        eventTimeButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                //Todo
//                DialogFragment newFragment = new TimePickerFragment();
//                newFragment.show(getActivity().getSupportFragmentManager(), "timePicker");
            }
        });

        return view;
    }

    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        mYear = year;
        mMonth = monthOfYear;
        mDay = dayOfMonth;
        updateDisplay();
    }

    // Update the date String in the TextView
    private void updateDisplay() {
        eventDate.setText(new StringBuilder()
        // Month is 0 based so add 1
        .append(mMonth + 1).append("-").append(mDay).append("-")
        .append(mYear).append(" "));
    }

    public static class DatePickerFragment extends DialogFragment implements
            DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {

            // Set the current date in the DatePickerFragment
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);

        }

        // Callback to DatePickerActivity.onDateSet() to update the UI
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            ((DatePickerDialog.OnDateSetListener) getActivity()).onDateSet(view, year,
                    monthOfYear, dayOfMonth);
        }
    }

    public static Event getEventCreated(Intent data) {
        return (Event)data.getSerializableExtra(EXTRA_EVENT_CREATED);
    }
}

// Todo used for date and time
//    String s = "01/02/2017 12:00";
//    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm");
//    try {
//        Date date = simpleDateFormat.parse(s);
//    } catch (ParseException e) {
//        Log.d(TAG, "ParseException");
//    }


//}


