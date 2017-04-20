package edu.umd.cs.jobi.service.impl;

import android.content.Context;
import android.net.Uri;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import edu.umd.cs.jobi.service.TeamService;

public class WebTeamService implements TeamService {

    private Context context;

    public WebTeamService(Context context) {
        this.context = context;
    }

    public List<String> getDefinitionOfDone(String teamId) {

        String TEAM_URL = "http://www.csfalcon.com/team/dod?teamId=112322946";
        String urlString = Uri.parse(TEAM_URL).buildUpon().appendQueryParameter(TEAM_URL, teamId).build().toString();
        List<String> list = new ArrayList<String>();

        try {

            URL url = new URL(TEAM_URL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            try {

                InputStream inputStream = connection.getInputStream();
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                int bytesRead = 0;
                byte[] buffer = new byte[1024];

                while ((bytesRead = inputStream.read(buffer)) > 0) {
                    outputStream.write(buffer, 0, bytesRead);
                }

                try {
                    outputStream.close();
                } catch (IOException f) {

                }

                String jsonString = new String(outputStream.toByteArray());
                try {
                    JSONArray jsonDefinition = new JSONArray(jsonString);

                    for (int i = 0; i < jsonDefinition.length(); i++) {
                        list.add(jsonDefinition.getString(i));
                    }

                } catch (JSONException j) {

                }

            } finally {
                connection.disconnect();
            }

        } catch (MalformedURLException e) {

        } catch (IOException f) {

        }

        return list;
    }
}
