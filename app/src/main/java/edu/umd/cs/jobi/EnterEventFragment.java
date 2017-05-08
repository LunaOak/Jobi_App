package edu.umd.cs.jobi;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
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
import android.widget.TimePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import edu.umd.cs.jobi.model.Event;

/**
 * Created by Pauline on 5/6/2017.
 */

public class EnterEventFragment extends Fragment implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener{
    private final String TAG = getClass().getSimpleName();

    private static final String EXTRA_EVENT_CREATED = "EXTRA_EVENT_CREATED";

    private static final String ARG_CREATE_BOOLEAN = "ARG_CREATE_BOOLEAN";
    private static final String ARG_POSITION_TITLE = "ARG_POSITION_TITLE";
    private static final String ARG_COMPANY_NAME = "ARG_COMPANY_NAME";
    private static final String ARG_EVENT_ID = "ARG_EVENT_ID";

    private Event event;
    private Boolean create;
    private String companyName;
    private String positionTitle;

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
    private int mHour;
    private int mMinute;

    // Buttons //
    private Button eventDateButton;
    private Button eventTimeButton;
    private Button saveButton;
    private Button cancelButton;

    public static EnterEventFragment newInstance(Boolean create, String positionTitle, String companyName, String eventId) {
        Bundle args = new Bundle();
        args.putBoolean(ARG_CREATE_BOOLEAN, create);
        args.putString(ARG_POSITION_TITLE, positionTitle);
        args.putString(ARG_COMPANY_NAME, companyName);
        args.putString(ARG_EVENT_ID, eventId);

        EnterEventFragment fragment = new EnterEventFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        create = getArguments().getBoolean(ARG_CREATE_BOOLEAN);
        positionTitle = getArguments().getString(ARG_POSITION_TITLE);
        companyName = getArguments().getString(ARG_COMPANY_NAME);

        String eventId = getArguments().getString(ARG_EVENT_ID);
        event = DependencyFactory.getEventService(getActivity().getApplicationContext()).getEventById(eventId);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_position_enter, container, false);

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
                DialogFragment newFragment = new TimePickerFragment();
                newFragment.show(getFragmentManager(), "TimePicker");
            }
        });

        return view;
    }

    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        mYear = year;
        mMonth = monthOfYear;
        mDay = dayOfMonth;
        updateDateDisplay();
    }

    // Update the date String in the TextView
    private void updateDateDisplay() {
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

    // Callback called when user sets the time
    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        mHour = hourOfDay;
        mMinute = minute;
        updateTimeDisplay();
    }

    // Update the time String in the TextView
    private void updateTimeDisplay() {
        eventTime.setText(new StringBuilder().append(pad(mHour)).append(":")
                .append(pad(mMinute)));
    }

    // Prepends a "0" to 1-digit minutes
    private static String pad(int c) {
        if (c >= 10)
            return String.valueOf(c);
        else
            return "0" + String.valueOf(c);
    }

    public static class TimePickerFragment extends DialogFragment implements
            TimePickerDialog.OnTimeSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {

            final Calendar c = Calendar.getInstance();
            int hourOfDay = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);

            // Create a new instance of TimePickerDialog and return it
            return new TimePickerDialog(getActivity(), this, hourOfDay, minute,
                    false);

        }

        // Callback to TimePickerFragmentActivity.onTimeSet() to update the UI
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            ((TimePickerDialog.OnTimeSetListener) getActivity()).onTimeSet(view, hourOfDay,
                    minute);

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



