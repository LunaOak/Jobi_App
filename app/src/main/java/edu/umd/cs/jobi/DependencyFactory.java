package edu.umd.cs.jobi;


import android.content.Context;

import edu.umd.cs.jobi.service.CompanyService;
import edu.umd.cs.jobi.service.SettingsService;
import edu.umd.cs.jobi.service.impl.SQLiteCompanyService;

import edu.umd.cs.jobi.service.impl.SQLiteSettingsService;

public class DependencyFactory {


    private static CompanyService companyService;
    private static SettingsService settingsService;



    public static CompanyService getCompanyService(Context context) {
        if (companyService == null) {
            companyService = new SQLiteCompanyService(context);
        }
        return companyService;
    }


    public static SettingsService getSettingsService(Context context) {
        if (settingsService == null) {
            settingsService = new SQLiteSettingsService(context);
            //settingsService = new InMemorySettingsService(context);
        }
        return settingsService;
    }
}
