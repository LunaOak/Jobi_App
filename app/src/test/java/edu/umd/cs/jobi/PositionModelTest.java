package edu.umd.cs.jobi;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import edu.umd.cs.jobi.model.Contact;
import edu.umd.cs.jobi.model.Position;
import edu.umd.cs.jobi.model.Position.Favorite;
import edu.umd.cs.jobi.model.Position.Status;
import edu.umd.cs.jobi.model.Position.Type;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;


public class PositionModelTest {

    private Position position = null;
    private Contact contact = new Contact("Sergey", "Founder", "SB@gmail.com", "123-456-7890");

    @Before
    public void setUp() throws Exception {
        position = new Position();
    }

    @After
    public void tearDown() throws Exception {
        position = null;
    }

    @Test
    public void testPositionConstructor() {

        // Assert position was assigned an id and empty contact list
        assertTrue(position != null);
        assertTrue(position.getId() != null);
        assertTrue(position.getContacts() != null);
    }

    @Test
    public void testSetStringFields() {

        position.setTitle("Software Engineer");
        assertEquals(position.getTitle(), "Software Engineer");

        position.setCompany("Google");
        assertEquals(position.getCompany(), "Google");

        position.setLocation("NY");
        assertEquals(position.getLocation(), "NY");

        position.setDescription("Really want this one!");
        assertEquals(position.getDescription(), "Really want this one!");

    }

    @Test
    public void testOtherFields() {

        position.setStatus(Status.IN_PROGRESS);
        assertEquals(position.getStatus(), Status.IN_PROGRESS);

        position.setFavorite(Favorite.YES);
        assertEquals(position.getFavorite(), Favorite.YES);

        position.setType(Type.INTERNSHIP);
        assertEquals(position.getType(), Type.INTERNSHIP);

        position.addContact(contact);
        List<Contact> contactList = position.getContacts();
        Contact currContact = contactList.get(0);
        assertEquals(currContact.getName(), "Sergey");
        assertEquals(currContact.getJobTitle(), "Founder");
        assertEquals(currContact.getEmail(), "SB@gmail.com");
        assertEquals(currContact.getPhone(), "123-456-7890");

    }

}
