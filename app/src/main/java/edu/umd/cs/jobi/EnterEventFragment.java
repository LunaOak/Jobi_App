package edu.umd.cs.jobi;

import android.os.Bundle;
import android.support.annotation.Nullable;
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

public class EnterEventFragment extends Fragment {
    private final String TAG = getClass().getSimpleName();

    private static final String EVENT_CREATED = "EVENT_CREATED";
    private static final String EVENT_ID = "EVENT_ID";

    private Event event;

    // Interactive Elements //
    private EditText eventName;
    private EditText eventLocation;
    private Spinner eventTypeSpinner;
    private TextView eventDate;
    private TextView eventTime;

    // Date and Time pickers //
    public static DatePicker datePicker;
    public static TimePicker timePicker;
    private Calendar calendar;
    private int year,month,day;
    private int hour,minute;
    private String datetime;

    // Buttons //
    private Button eventDateButton;
    private Button eventTimeButton;
    private Button saveButton;
    private Button cancelButton;

    public static EnterEventFragment newInstance(String eventId) {
        Bundle args = new Bundle();
        args.putString(EVENT_ID, eventId);

        EnterEventFragment fragment = new EnterEventFragment();
        fragment.setArguments(args);
        return fragment;
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
//                DialogFragment newFragment = new TimePickerFragment();
//                newFragment.show(getActivity().getSupportFragmentManager(), "timePicker");
            }
        });

        return view;
    }

//    public static class TimePickerFragment extends DialogFragment
//            implements TimePickerDialog.OnTimeSetListener {
//
//        @Override
//        public Dialog onCreateDialog(Bundle savedInstanceState) {
//            // Use the current time as the default values for the picker
//            final Calendar c = Calendar.getInstance();
//            int hour = c.get(Calendar.HOUR_OF_DAY);
//            int minute = c.get(Calendar.MINUTE);
//
//            // Create a new instance of TimePickerDialog and return it
//            return new TimePickerDialog(getActivity(), this, hour, minute, DateFormat.is24HourFormat(getActivity()));
//        }

//        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
//            timePicker.setHour(hourOfDay);
//            timePicker.setMinute(minute);
//            timePicker.setIs24HourView(false);
//        }
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


