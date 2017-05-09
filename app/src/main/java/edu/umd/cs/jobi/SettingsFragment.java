package edu.umd.cs.jobi;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Button;
import android.widget.Switch;
import android.widget.Toolbar;
import static android.app.Activity.RESULT_OK;

import java.util.ArrayList;
import java.util.List;

import edu.umd.cs.jobi.model.Position;
import edu.umd.cs.jobi.model.Settings;

public class SettingsFragment extends Fragment {

    // Fields //
    private Settings settings;
    private RadioGroup statusRadioGroup;
    private Button saveButton;
    private Button cancelButton;
    private Switch notificationsSwitch;
    private CheckBox notificationsInterviews;
    private CheckBox notificationsEmails;
    private CheckBox notificationsDeadlines;
    private List<Settings.Notifications> notificationsList;
    private static final String SETTINGS_UPDATED= "SettingsUpdated";


    public static SettingsFragment newInstance() {
        return new SettingsFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        settings = DependencyFactory.getSettingsService(getActivity().getApplicationContext()).getSettings();
        setHasOptionsMenu(true);
    }

    // View Management //
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        // Status Radio Group //
        statusRadioGroup = (RadioGroup)view.findViewById(R.id.settings_status_radio_group);

        // Notifications Switch //
        notificationsSwitch = (Switch) view.findViewById(R.id.settings_notification_switch);

        // Notifications List //
        notificationsInterviews = (CheckBox) view.findViewById(R.id.settings_up_interviews);
        notificationsEmails = (CheckBox) view.findViewById(R.id.settings_emails);
        notificationsDeadlines = (CheckBox) view.findViewById(R.id.settings_deadlines);

        notificationsList = new ArrayList<>();

        if (settings != null) {

            // Status //
            switch (settings.getStatus()) {
                case INTERVIEWING:
                    statusRadioGroup.check(R.id.settings_status_interviewing);
                    break;
                case SEARCHING:
                    statusRadioGroup.check(R.id.settings_status_searching);
                    break;
                case NOT_SEARCHING:
                    statusRadioGroup.check(R.id.settings_status_not_searching);
                    break;
                default:
                    statusRadioGroup.check(R.id.settings_status_interviewing);
                    break;
            }

            // Notifications Switch //
            switch (settings.getSwitch()) {
                case ON:
                    notificationsSwitch.setChecked(true);
                    break;
                case OFF:
                    notificationsSwitch.setChecked(false);
                    break;
                default:
                    notificationsSwitch.setChecked(true);
                    break;
            }


            notificationsInterviews.setChecked(false);
            notificationsEmails.setChecked(false);
            notificationsDeadlines.setChecked(false);

            // Notifications List //
            for (Settings.Notifications notif : settings.getNotifications()) {

                if (notif.equals(Settings.Notifications.INTERVIEWS)) {
                    notificationsInterviews.setChecked(true);
                } else if (notif.equals(Settings.Notifications.EMAILS)) {
                    notificationsEmails.setChecked(true);
                } else if (notif.equals(Settings.Notifications.DEADLINES)) {
                    notificationsDeadlines.setChecked(true);
                }

            }

        } else {
            statusRadioGroup.check(R.id.settings_status_interviewing);
            notificationsSwitch.setChecked(true);
            notificationsInterviews.setChecked(true);
            notificationsEmails.setChecked(true);
            notificationsDeadlines.setChecked(true);
        }


        // Save Button //
        saveButton = (Button)view.findViewById(R.id.settings_save_button);
        saveButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if (settings == null) {
                    settings = new Settings();
                }

                // Status //
                int statusId = statusRadioGroup.getCheckedRadioButtonId();
                switch (statusId) {
                    case R.id.settings_status_interviewing:
                        settings.setStatus(Settings.Status.INTERVIEWING);
                        break;
                    case R.id.settings_status_searching:
                        settings.setStatus(Settings.Status.SEARCHING);
                        break;
                    case R.id.settings_status_not_searching:
                        settings.setStatus(Settings.Status.NOT_SEARCHING);
                        break;
                    default:
                        settings.setStatus(Settings.Status.INTERVIEWING);
                        break;
                }

                // Notifications Switch //
                if(notificationsSwitch.isChecked()){
                    settings.setSwitch(Settings.NotificationSwitch.ON);
                }
                else {
                    settings.setSwitch(Settings.NotificationSwitch.OFF);
                }

                // Notifications CheckList //
                if (notificationsInterviews.isChecked()) {
                    notificationsList.add(Settings.Notifications.INTERVIEWS);
                }

                if (notificationsEmails.isChecked()) {
                    notificationsList.add(Settings.Notifications.EMAILS);
                }

                if (notificationsDeadlines.isChecked()) {
                    notificationsList.add(Settings.Notifications.DEADLINES);
                }

                settings.setNotifications(notificationsList);

                Intent data = new Intent();
                data.putExtra(SETTINGS_UPDATED, settings);
                getActivity().setResult(RESULT_OK, data);
                getActivity().finish();
            }

        });

        // Cancel Button //
        cancelButton = (Button)view.findViewById(R.id.settings_cancel_button);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();
            }
        });

        return view;
    }

    public static Settings getSettingsCreated(Intent data) {
        return (Settings) data.getSerializableExtra(SETTINGS_UPDATED);
    }

    // Menu Bar Management //
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_home, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_home:
                Intent homeIntent = new Intent(getActivity(), HomeActivity.class);
                startActivity(homeIntent);
                return true;
            case R.id.menu_item_settings:
//                Don't do anything, you're already in this activity!
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


}
