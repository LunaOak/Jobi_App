package edu.umd.cs.jobi.service;

import java.util.List;

import edu.umd.cs.jobi.model.Company;
import edu.umd.cs.jobi.model.Contact;
import edu.umd.cs.jobi.model.Event;
import edu.umd.cs.jobi.model.Position;
import edu.umd.cs.jobi.model.Reminder;

public interface CompanyService {
    public void addCompanyToDb(Company company);
    public String getCompanyNameById(String id);
    public Company getCompanyById(String id);
    public List<Company> getAllCompanies();
    public List<Company> getCurrentCompanies();
    public List<Company> getFavoriteCompanies();
    public boolean deleteCompanyById(String id);
    public String getCompanyIdWithName(String name);

    public void addEventToDb(Event event);
    public boolean deleteEventById(String id);
    public Event getEventById(String id);
    public List<Event> getAllEvents();
    public List<Event> getEventsByPositionId(String id);
    public List<Event> getEventsByCompanyId(String id);

    public Reminder getReminderById(String id);
    public boolean deleteReminderById(String id);
    public void addReminderToDb(Reminder reminder);
    public List<Reminder> getRemindersByEvent(String eventId);

    public Contact getContactById(String id);
    public boolean deleteContactById(String id);
    public List<Contact> getContactsByCompanyId(String id);
    public List<Contact> getContactsByPositionId(String id);
    public List<Contact> getContactsByEventId(String id);
    public void addContactToDb(Contact contact);

    public void addPositionToDb(Position position);
    public boolean deletePositionById(String id);
    public Position getPositionById(String id);
    public String getPositionNameById(String id);
    public List<Position> getAllPositions();
    public List<Position> getPositionsByCompanyId(String id);
    public String getPositionIdWithName(String name, String companyId);

    /*
    public void addCompanyToDb(Company company);
    public Company getCompanyByName(String name);
    public Company getCompanyById(String id);
    public List<Company> getAllCompanies();
    public List<Company> getCurrentCompanies();
    public List<Company> getFavoriteCompanies();
    public boolean deleteCompanyById(String id);*/

    /*
       public void addEventToDb(Event event);
    public boolean deleteEventById(String id);
    public Event getEventById(String id);
    public List<Event> getAllEvents();
    public Contact getContactById(String id);
    public boolean deleteContactById(String id);
    public Reminder getReminderById(String id);
    public boolean deleteReminderById(String id);
    public List<Event> getEventsByPositionAndCompany(String title, String name);
    public List<Event> getEventsByCompanyName(String name);
     */

    /*

        public void addPositionToDb(Position position);
    public boolean deletePositionById(String id);
    public Position getPositionById(String id);
    public List<Position> getAllPositions();
    public List<Position> getFavoritePositions();
    public List<Position> getPositionsByCompany(String name);
    public List<Contact> getContactsByPosition(Position position);
    public Contact getContactById(String id);
    public boolean deleteContactById(String id);
     */
}
