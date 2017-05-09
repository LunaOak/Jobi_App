package edu.umd.cs.jobi.service;

import edu.umd.cs.jobi.model.Settings;

public interface SettingsService {
    public boolean getStatus(Settings settings);
    public Settings getSettings(String id);
    public void updateSettings(Settings settings);
}
