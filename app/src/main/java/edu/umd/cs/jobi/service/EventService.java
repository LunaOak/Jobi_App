package edu.umd.cs.jobi.service;

import java.util.List;

import edu.umd.cs.jobi.model.Contact;
import edu.umd.cs.jobi.model.Event;

/**
 * Created by Juan on 5/2/2017.
 */

public interface EventService {
    public void addEventToDb(Event event);
    public Event getEventById(String id);
    public List<Event> getAllEvents();
    public Contact getContactById(String id);
    public List<Event> getEventsByPositionTitle(String title);
}
