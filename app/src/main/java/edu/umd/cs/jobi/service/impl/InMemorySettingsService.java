package edu.umd.cs.jobi.service.impl;


import android.content.Context;

import edu.umd.cs.jobi.model.Settings;
import edu.umd.cs.jobi.service.SettingsService;

public class InMemorySettingsService implements SettingsService {

    private Context context;
    private Settings settings;

    public InMemorySettingsService(Context context) {
        this.context = context;
        this.settings = new Settings();
    }

    @Override
    public boolean getStatus(Settings settings) {
        return false;
    }

    @Override
    public Settings getSettings() {
        return this.settings;
    }
}
