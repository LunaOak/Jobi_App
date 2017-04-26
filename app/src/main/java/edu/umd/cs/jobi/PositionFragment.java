package edu.umd.cs.jobi;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ToggleButton;

import edu.umd.cs.jobi.model.Position;

import static android.R.drawable.btn_star_big_off;
import static android.R.drawable.btn_star_big_on;
import static android.app.Activity.RESULT_OK;

public class PositionFragment extends Fragment{

    private final String TAG = getClass().getSimpleName();

    private static final String POSITION_ID = "PositionId";
    private static final String POSITION_FAVORITE_EDITED = "PositionFavoriteEdited";
    private static final int REQUEST_CODE_EDIT_POSITION = 0;

    private Position position;

    private ToggleButton favoriteButton;
    private ImageButton editButton;

    public static PositionFragment newInstance() {
        PositionFragment fragment = new PositionFragment();
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

        favoriteButton = (ToggleButton)view.findViewById(R.id.favorite_button);
        if (position == null) {
            position = new Position();
        }

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

                Intent data = new Intent();
                data.putExtra(POSITION_FAVORITE_EDITED, position);
                getActivity().setResult(RESULT_OK, data);
                getActivity().finish();
            }
        });

        editButton = (ImageButton)view.findViewById(R.id.edit_position_button);
        editButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view){
                Intent intent = EnterPositionActivity.newIntent(getActivity(), position.getId());
                startActivityForResult(intent, REQUEST_CODE_EDIT_POSITION);
            }
        });

        return view;
    }
}