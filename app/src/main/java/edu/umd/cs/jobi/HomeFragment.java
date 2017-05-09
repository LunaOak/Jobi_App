package edu.umd.cs.jobi;


import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import edu.umd.cs.jobi.model.Event;
import edu.umd.cs.jobi.model.Position;
import edu.umd.cs.jobi.model.Settings;
import edu.umd.cs.jobi.service.CompanyService;
import edu.umd.cs.jobi.service.SettingsService;

public class HomeFragment extends Fragment {

    private final String TAG = getClass().getSimpleName();
    private static final int REQUEST_CODE_EDIT_EVENT = 0;
    private static final int REQUEST_CODE_VIEW_POSITIONS = 3;
    private static final int REQUEST_CODE_VIEW_COMPANIES = 4;
    private static final int REQUEST_CODE_SETTINGS_UPDATED = 5;
    private static final int REQUEST_CODE_POSITION_CREATED = 10;
    private static final String SETTINGS_ID = "SettingsId";


    private CompanyService companyService;
    private SettingsService settingsService;


    private RecyclerView eventRecyclerView;
    private RecyclerView favoritesRecyclerView;
    private EventAdapter adapter;
    private PositionAdapter positionAdapater;
    private Settings settings;

    // Labels and Date
    private TextView day;
    private TextView month;
    private TextView date;
    private TextView year;

    // Companies and Positions List field members
    private Button companyListButton;
    private Button positionListButton;
    private Button createPositionButton;
    private Button eventListButton;
    private ImageView statusColor;
    private TextView statusText;
    private Drawable circle;
    private int interviewColor;
    private int searchingColor;
    private int notSearchingColor;

    // Dialog boxes for deletion //
    private AlertDialog.Builder eventDeleteBuilder;
    private AlertDialog.Builder positionDeleteBuilder;

    public static HomeFragment newInstance(String settingsId) {
        Bundle args = new Bundle();
        args.putString(SETTINGS_ID, settingsId);

        HomeFragment fragment = new HomeFragment();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        companyService = DependencyFactory.getCompanyService(getActivity().getApplicationContext());
        if (savedInstanceState !=null) {
            String settingsId = getArguments().getString(SETTINGS_ID);
            settings = DependencyFactory.getSettingsService(getActivity().getApplicationContext()).getSettings(settingsId);
        } else {
            settingsService = DependencyFactory.getSettingsService(getActivity().getApplicationContext());
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,  Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        eventRecyclerView = (RecyclerView)view.findViewById(R.id.home_event_recycler_view);
        eventRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        favoritesRecyclerView = (RecyclerView)view.findViewById(R.id.favorite_recycler_view);
        favoritesRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        day = (TextView) view.findViewById(R.id.day);
        month = (TextView) view.findViewById(R.id.month);
        date = (TextView) view.findViewById(R.id.date);
        year = (TextView) view.findViewById(R.id.year);

        final Calendar c = Calendar.getInstance();
        SimpleDateFormat dayFormat = new SimpleDateFormat("EEEE", Locale.US);

        day.setText(new StringBuilder().append(dayFormat.format(c.getTime())));
        month.setText(new StringBuilder().append(getMonthShortName(c.get(Calendar.MONTH))).append(" "));
        date.setText(new StringBuilder().append(c.get(Calendar.DATE)).append(" "));
        year.setText(new StringBuilder().append(c.get(Calendar.YEAR)));


        circle = getActivity().getApplicationContext().getDrawable(R.drawable.circle);
        statusColor = (ImageView) view.findViewById(R.id.status_color);
        statusColor.setImageDrawable(circle);

        interviewColor = getActivity().getApplicationContext().getColor(R.color.interviewing);
        searchingColor = getActivity().getApplicationContext().getColor(R.color.searching);
        notSearchingColor = getActivity().getApplicationContext().getColor(R.color.not_searching);

        statusText = (TextView) view.findViewById(R.id.status_text);

        if (settings != null) {

            // Status //
            switch (settings.getStatus()) {
                case INTERVIEWING:
                    statusColor.getDrawable().setColorFilter(interviewColor,PorterDuff.Mode.SRC_ATOP);
                    statusText.setText(R.string.status_interviewing);
                    break;
                case SEARCHING:
                    statusColor.setColorFilter(searchingColor,PorterDuff.Mode.SRC_ATOP);
                    statusText.setText(R.string.status_searching);
                    break;
                case NOT_SEARCHING:
                    statusColor.setColorFilter(notSearchingColor,PorterDuff.Mode.SRC_ATOP);
                    statusText.setText(R.string.status_not_searching);
                    break;
                default:
                    statusColor.setColorFilter(interviewColor,PorterDuff.Mode.SRC_ATOP);
                    statusText.setText(R.string.status_interviewing);
                    break;
            }
        } else {
            statusColor.setColorFilter(interviewColor,PorterDuff.Mode.SRC_ATOP);
            statusText.setText(R.string.status_interviewing);
        }

        eventListButton = (Button)view.findViewById(R.id.event_list_button);
        eventListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent eventListIntent = new Intent(getActivity(),
                        EventListActivity.class);
                startActivityForResult(eventListIntent, REQUEST_CODE_EDIT_EVENT);
            }
        });
        companyListButton = (Button)view.findViewById(R.id.company_list_button);
        companyListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent companyListIntent = new Intent(getActivity(),
                        CompanyListActivity.class);
                startActivityForResult(companyListIntent, REQUEST_CODE_VIEW_COMPANIES);
            }
        });
        positionListButton = (Button)view.findViewById(R.id.position_list_button);
        positionListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent positionListIntent = new Intent(getActivity(),
                        PositionListActivity.class);
                startActivityForResult(positionListIntent, REQUEST_CODE_VIEW_POSITIONS);
            }
        });
        createPositionButton = (Button)view.findViewById(R.id.create_position_button);
        createPositionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent createPositionIntent = new Intent(getActivity(),
                        EnterPositionActivity.class);
                startActivityForResult(createPositionIntent,REQUEST_CODE_POSITION_CREATED);
            }
        });

        updateUI();

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Update view from settings //
        if (requestCode == REQUEST_CODE_SETTINGS_UPDATED) {
            if (data == null) {
                return;
            }

            Settings settingsCreated = SettingsActivity.getSettingsEdit(data);
            settingsService.updateSettings(settingsCreated);

            settings = settingsCreated;

            if (settings != null) {

                // Status //
                switch (settings.getStatus()) {
                    case INTERVIEWING:
                        statusColor.getDrawable().setColorFilter(interviewColor,PorterDuff.Mode.SRC_ATOP);
                        statusText.setText(R.string.status_interviewing);
                        break;
                    case SEARCHING:
                        statusColor.setColorFilter(searchingColor,PorterDuff.Mode.SRC_ATOP);
                        statusText.setText(R.string.status_searching);
                        break;
                    case NOT_SEARCHING:
                        statusColor.setColorFilter(notSearchingColor,PorterDuff.Mode.SRC_ATOP);
                        statusText.setText(R.string.status_not_searching);
                        break;
                    default:
                        statusColor.setColorFilter(interviewColor,PorterDuff.Mode.SRC_ATOP);
                        statusText.setText(R.string.status_interviewing);
                        break;
                }
            }

        }

        if (requestCode == REQUEST_CODE_POSITION_CREATED) {
            if (data == null) {
                return;
            }
            Position positionCreated = PositionActivity.getPositionEdit(data);
            companyService.addPositionToDb(positionCreated);
        }

        updateUI();
    }

    private void updateUI() {
        List<Event> events = companyService.getAllEvents();
        List<Position> positions = companyService.getFavoritePositions();

        if (adapter == null) {
            adapter = new EventAdapter(events);
            eventRecyclerView.setAdapter(adapter);
        } else {
            adapter.setEvents(events);
            adapter.notifyDataSetChanged();
        }

        if (positionAdapater == null) {
            positionAdapater = new PositionAdapter(positions);
            favoritesRecyclerView.setAdapter(positionAdapater);
        } else {
            positionAdapater.setPositions(positions);
            positionAdapater.notifyDataSetChanged();
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_home, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_home:
//               Don't do anything, you're already here!
                return true;
            case R.id.menu_item_settings:
                Intent settingsIntent = new Intent(getActivity(), SettingsActivity.class);
                startActivityForResult(settingsIntent,REQUEST_CODE_SETTINGS_UPDATED);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
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

            positionTitle = (TextView)itemView.findViewById(R.id.list_item_position_title);
            positionSummary = (TextView)itemView.findViewById(R.id.list_item_position_summary);
            positionCompany = (TextView)itemView.findViewById(R.id.list_item_position_company);

            // Delete Alert Dialog //
            itemView.setOnLongClickListener(new View.OnLongClickListener(){
                @Override
                public boolean onLongClick(View view){
                    positionDeleteBuilder = new AlertDialog.Builder(getActivity());
                    positionDeleteBuilder.setTitle("Delete Position?");
                    positionDeleteBuilder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int which) {
                            companyService.deletePositionById(position.getId());
                            Toast.makeText(getActivity().getApplicationContext(), "Position deleted!", Toast.LENGTH_SHORT).show();
                            favoritesRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
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
            positionCompany.setText(companyService.getCompanyNameById(position.getCompany()));
        }

        @Override
        public void onClick(View view) {
            Intent intent = PositionActivity.newIntent(getActivity(), position.getId());
            startActivityForResult(intent, REQUEST_CODE_VIEW_POSITIONS);
        }
    }

    // Position Adapter ///////////////////////////////////////////////////////////////
    private class PositionAdapter extends RecyclerView.Adapter<PositionHolder> {

        private List<Position> positions;

        public PositionAdapter(List<Position> positions) {
            this.positions = positions;
        }

        public void setPositions(List<Position> positions) {
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

    private class EventHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView dateTextView;
        private TextView titleTextView;
        private TextView typeTextView;

        private Event event;

        public EventHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);

            dateTextView = (TextView)itemView.findViewById(R.id.list_item_event_date);
            titleTextView = (TextView)itemView.findViewById(R.id.list_item_event_title);
            typeTextView = (TextView)itemView.findViewById(R.id.list_item_event_type);

            // Delete Alert Dialog //
            itemView.setOnLongClickListener(new View.OnLongClickListener(){
                @Override
                public boolean onLongClick(View view){
                    eventDeleteBuilder = new AlertDialog.Builder(getActivity());
                    eventDeleteBuilder.setTitle("Delete Event?");
                    eventDeleteBuilder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int which) {
                            companyService.deleteEventById(event.getId());
                            Toast.makeText(getActivity().getApplicationContext(), "Event deleted!", Toast.LENGTH_SHORT).show();
                            eventRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
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

            dateTextView.setText(new SimpleDateFormat("EEE, MMM d", Locale.ENGLISH).format(event.getDate()) + " - ");
            titleTextView.setText(event.getTitle());
            typeTextView.setText(event.getType().name());

        }

        @Override
        public void onClick(View view) {
            Intent intent = EventActivity.newIntent(getActivity(), event.getId());
            startActivityForResult(intent, REQUEST_CODE_EDIT_EVENT);
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


    public static String getMonthShortName(int monthNumber) {
        String monthName="";

        if(monthNumber>=0 && monthNumber<12)
            try
            {
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.MONTH, monthNumber);

                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMM");
                simpleDateFormat.setCalendar(calendar);
                monthName = simpleDateFormat.format(calendar.getTime());
            }
            catch (Exception e)
            {
                if(e!=null)
                    e.printStackTrace();
            }
        return monthName;
    }

}

