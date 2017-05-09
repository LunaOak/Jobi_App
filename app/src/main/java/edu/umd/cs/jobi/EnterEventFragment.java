package edu.umd.cs.jobi;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import edu.umd.cs.jobi.model.Company;
import edu.umd.cs.jobi.model.Event;
import edu.umd.cs.jobi.model.Position;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

/**
 * Created by Pauline on 5/6/2017.
 */

public class EnterEventFragment extends Fragment {
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
    private Company company;
    private Position position;

    // Interactive Elements //
    private EditText eventName;
    private EditText eventLocation;
    private Spinner eventTypeSpinner;
    private EditText eventCompany;
    private EditText eventPosition;

    // Date and Time pickers //
    private TextView eventDate;
    private TextView eventTime;
    private Date newDate;
    private int mYear;
    private int mMonth;
    private int mDay;
    private int mHour;
    private int mMinute;
    private DatePickerDialog.OnDateSetListener listenerDate;
    private TimePickerDialog.OnTimeSetListener listenerTime;

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

        View view = inflater.inflate(R.layout.fragment_event_enter, container, false);

        // Event name //
        eventName = (EditText)view.findViewById(R.id.event_name);
        if (event != null) {
            eventName.setText(event.getTitle());
        }

        // Event company
        eventCompany = (EditText)view.findViewById(R.id.event_company);
        if (event != null) {
            eventCompany.setText(companyName);
        }

        // Event position
        eventPosition = (EditText)view.findViewById(R.id.event_position);
        if (event != null) {
            eventPosition.setText(positionTitle);
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
            eventDate.setText(new SimpleDateFormat("EEE, d MMM yyyy", Locale.ENGLISH).format(event.getDate()));
        }

        // Event date button //
        eventDateButton = (Button)view.findViewById(R.id.event_date_button);
        eventDateButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), listenerDate, year, month, day);
                datePickerDialog.show();
            }
        });

        listenerDate = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                mYear = year;
                mMonth = monthOfYear;
                mDay = dayOfMonth;

                ((TextView)getActivity().findViewById(R.id.event_date_text)).setText(new StringBuilder()
                        .append(monthOfYear + 1).append("-").append(dayOfMonth).append("-").append(year));
            }
        };

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
                final Calendar c = Calendar.getInstance();
                int hourOfDay = c.get(Calendar.HOUR_OF_DAY);
                int minute = c.get(Calendar.MINUTE);

                TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(), listenerTime, hourOfDay, minute, false);
                timePickerDialog.show();
            }
        });

        listenerTime = new TimePickerDialog.OnTimeSetListener() {

            @Override
            public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
                mHour = hourOfDay;
                mMinute = minute;

                ((TextView)getActivity().findViewById(R.id.event_time_text)).setText(new StringBuilder()
                        .append(pad(hourOfDay)).append(":").append(pad(minute)));
            }
        };



        saveButton = (Button)view.findViewById(R.id.save_event_button);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (create) {
                    event = new Event();
                }
                event.setTitle(eventName.getText().toString());

                event.setCompany(eventCompany.getText().toString());
                event.setPosition(eventPosition.getText().toString());
                event.setType(eventTypeSpinner.getSelectedItemPosition());
                newDate = getNewDate(mMonth, mDay, mYear, mHour, mMinute);
                event.setDate(newDate);
                event.setLocation(eventLocation.getText().toString());

                Intent data = new Intent();
                data.putExtra(EXTRA_EVENT_CREATED, event);
                getActivity().setResult(RESULT_OK, data);
                getActivity().finish();
            }
        });

        cancelButton = (Button)view.findViewById(R.id.cancel_event_button);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().setResult(RESULT_CANCELED);
                getActivity().finish();
            }
        });

        return view;
    }

    // Prepends a "0" to 1-digit minutes
    private static String pad(int c) {
        if (c >= 10)
            return String.valueOf(c);
        else
            return "0" + String.valueOf(c);
    }

    public Date getNewDate(int month, int day, int year, int hour, int min) {
        String s = new String();
        Date date = new Date();
        s += String.valueOf(month + 1) + "/" + String.valueOf(day) + "/" + String.valueOf(year)
                + " " + String.valueOf(hour) + ":" + String.valueOf(min);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm");
        try {
            date = simpleDateFormat.parse(s);
        } catch (ParseException e) {
            Log.d(TAG, "ParseException");
        }
        return date;
    }

    public static Event getEventCreated(Intent data) {
        return (Event)data.getSerializableExtra(EXTRA_EVENT_CREATED);
    }
}




