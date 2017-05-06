package edu.umd.cs.jobi;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.ToggleButton;

import edu.umd.cs.jobi.model.Position;

import static android.R.drawable.btn_star_big_off;
import static android.R.drawable.btn_star_big_on;


public class PositionFragment extends Fragment {

    private final String TAG = getClass().getSimpleName();

    private static final String POSITION_ID = "PositionId";
    private static final int REQUEST_CODE_EDIT_POSITION = 0;
    private static final int REQUEST_CODE_ADD_NEW_EVENT = 1;

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
    private Button addNewEventButton;


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
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_position, container, false);

        // Position Title //
        positionTitle = (TextView) view.findViewById(R.id.positionTitle);
        positionTitle.setText(position.getTitle());

        // Position Company //
        companyName = (TextView) view.findViewById(R.id.companyName);
        companyName.setText(position.getCompany());

        // Position Location //
        companyLocation = (TextView) view.findViewById(R.id.companyLocation);
        companyLocation.setText(position.getLocation());

        // Position Type //
        positionType = (TextView) view.findViewById(R.id.positionType);
        positionType.append(position.getType().name());

        // Position Status //
        positionStatus = (TextView) view.findViewById(R.id.positionStatus);
        positionStatus.append(position.getStatus().name());

        // Position Description //
        positionDescription = (TextView) view.findViewById(R.id.positionDescription);
        positionDescription.setText(position.getDescription());


        //// Button Actions

        // Edit Position Button //
        editPositionButton = (ImageButton)view.findViewById(R.id.edit_position_button);
        editPositionButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = EnterPositionActivity.newIntent(getActivity(), position.getId());
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
            }

        });

        // Add New Event Button //
        addNewEventButton = (Button)view.findViewById(R.id.add_new_event_button);
        addNewEventButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = EventActivity.newIntent(getActivity()); //ToDo: Ask Juan
                startActivityForResult(intent, REQUEST_CODE_EDIT_POSITION);
            }
        });

        return view;
    }

    public static Position getPositionCreated(Intent data) {
        return (Position) data.getSerializableExtra(POSITION_ID);
    }
}
