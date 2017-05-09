package edu.umd.cs.jobi;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class EnterFragment extends Fragment {

    // Fields //
    private Button enterButton;

    public static EnterFragment newInstance() {
        EnterFragment fragment = new EnterFragment();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    // View Management //
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_jobienter, container, false);

        // Cancel Button //
        enterButton = (Button)view.findViewById(R.id.Jobi_Enter);
        enterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent homeIntent = new Intent(getActivity(),
                        HomeActivity.class);
                startActivity(homeIntent);
            }
        });

        return view;
    }

}
