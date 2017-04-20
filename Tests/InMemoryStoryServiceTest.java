package edu.umd.cs.jobi;

import static org.junit.Assert.*;
import android.support.v4.app.Fragment;
import org.junit.*;

import java.util.ArrayList;
import java.util.List;

import edu.umd.cs.jobi.model.Story;
import edu.umd.cs.jobi.service.impl.InMemoryStoryService;


public class InMemoryStoryServiceTest {


    // This test is using getAllStories assuming is not broken. There is no other way to access //
    // the stories list without using these methods because the fields are private and we cannot add //
    // any getters or setters //
    @Test
    public void testAddStoryToBacklog() {
        InMemoryStoryService service = new InMemoryStoryService(null);

        Story s = new Story();
        s.setId("ID:1");
        s.setSummary("Story 1");
        s.setAcceptanceCriteria("Implementing and Testing!");
        s.setPriorityCurrent();
        s.setStatus(0);

        Story s2 = new Story();
        s2.setId("ID:2");
        s2.setSummary("Story 2");
        s2.setAcceptanceCriteria("Implementing and Testing!");
        s2.setPriorityLater();
        s2.setStatus(0);

        Story s3 = new Story();
        s3.setId("ID:1");
        s3.setSummary("Story 3");
        s3.setAcceptanceCriteria("Implementing!");
        s3.setPriorityNext();
        s3.setStatus(1);

        // Check that the list is empty first //
        assertEquals(service.getAllStories().size(),0);

        // Add story using addStoryToBacklog //
        service.addStoryToBacklog(s);

        /* Adding story does not increase backlog size */
        assertEquals(service.getAllStories().size(),1);

        // Add another story to backlog //
        service.addStoryToBacklog(s2);

        assertEquals(service.getAllStories().size(),2);

        /* Story with same ID added to backlog again */

        // Add story with same ID //
        service.addStoryToBacklog(s3);

        // Make sure size does not change //
        assertEquals(service.getAllStories().size(),2);

        // Make sure it is updated //
        for (Story story : service.getAllStories()) {
            if (story.getId().equals("ID:1")){
                assertEquals("Story 3",story.getSummary());
                assertEquals("Implementing!",story.getAcceptanceCriteria());
                assertEquals(Story.Priority.NEXT,story.getPriority());
                assertEquals(Story.Status.IN_PROGRESS,story.getStatus());
            }
        }

    }

    // This test is using addStoryToBacklog and getAllStories assuming they are not broken. There is no //
    // other way to access the stories list without using these methods because the fields are private //
    // and we cannot add any getters and setters //
    @Test
    public void testGetStoryById() {
        InMemoryStoryService service = new InMemoryStoryService(null);

        Story s = new Story();
        s.setId("ID:1");
        s.setSummary("Story 1");
        s.setAcceptanceCriteria("Implementing and Testing!");
        s.setPriorityCurrent();
        s.setStatus(0);

        Story s2 = new Story();
        s2.setId("ID:2");
        s2.setSummary("Story 2");
        s2.setAcceptanceCriteria("Implementing and Testing!");
        s2.setPriorityLater();
        s2.setStatus(0);

        Story s3 = new Story();
        s3.setId("ID:3");
        s3.setSummary("Story 3");
        s3.setAcceptanceCriteria("Implementing!");
        s3.setPriorityNext();
        s3.setStatus(1);

        service.addStoryToBacklog(s);
        service.addStoryToBacklog(s2);
        service.addStoryToBacklog(s3);

        /* Does not find story with ID in backlog */

        // Make sure it gets all the right stories //
        assertEquals(service.getStoryById("ID:1"),s);
        assertEquals(service.getStoryById("ID:2"),s2);
        assertEquals(service.getStoryById("ID:3"),s3);

        // Make sure it returns null when given a non-existent ID //
        assertEquals(service.getStoryById("ID:4"), null);

        // Make sure it does not return mixed stories //
        assertFalse(service.getStoryById("ID:1").equals(s2));
        assertFalse(service.getStoryById("ID:2").equals(s3));
        assertFalse(service.getStoryById("ID:3").equals(s));
    }

    // This test is using addStoryToBacklog assuming it is not broken. There is no other way to access //
    // the stories list without using these methods because the fields are private and we cannot add //
    // any getters or setters //
    @Test
    public void testGetAllStories() {
        InMemoryStoryService service = new InMemoryStoryService(null);

        Story s = new Story();
        s.setId("ID:1");
        s.setSummary("Story 1");
        s.setAcceptanceCriteria("Implementing and Testing!");
        s.setPriorityCurrent();
        s.setStatus(0);

        Story s2 = new Story();
        s2.setId("ID:2");
        s2.setSummary("Story 2");
        s2.setAcceptanceCriteria("Implementing and Testing!");
        s2.setPriorityLater();
        s2.setStatus(0);

        Story s3 = new Story();
        s3.setId("ID:3");
        s3.setSummary("Story 3");
        s3.setAcceptanceCriteria("Implementing!");
        s3.setPriorityNext();
        s3.setStatus(1);

        Story s4 = new Story();
        s4.setId("ID:4");
        s4.setSummary("Story 4");
        s4.setAcceptanceCriteria("Implementing!");
        s4.setPriorityCurrent();
        s4.setStatus(1);

        Story s5 = new Story();
        s5.setId("ID:5");
        s5.setSummary("Story 5");
        s5.setAcceptanceCriteria("Implementing!");
        s5.setPriorityLater();
        s5.setStatus(2);

        Story s6 = new Story();
        s6.setId("ID:6");
        s6.setSummary("Story 6");
        s6.setAcceptanceCriteria("Implementing!");
        s6.setPriorityNext();
        s6.setStatus(0);

        service.addStoryToBacklog(s);
        service.addStoryToBacklog(s2);
        service.addStoryToBacklog(s3);
        service.addStoryToBacklog(s4);
        service.addStoryToBacklog(s5);
        service.addStoryToBacklog(s6);

        /* Does not return entire backlog */
        // Check that the backlog is the right size //
        assertEquals(service.getAllStories().size(),6);

        /* Stories not returned in correct sort order */
        // Check that stories are being returned in the correct order //
        // 1 4 6 3 2 5 //
        assertEquals(service.getAllStories().get(0),s);
        assertEquals(service.getAllStories().get(1),s4);
        assertEquals(service.getAllStories().get(2),s6);
        assertEquals(service.getAllStories().get(3),s3);
        assertEquals(service.getAllStories().get(4),s2);
        assertEquals(service.getAllStories().get(5),s5);

        // Double check with toString representation of expected list //
        assertEquals("[Story 1 : 0.0 : CURRENT : TODO, Story 4 : 0.0 : CURRENT : IN_PROGRESS, Story 6 : 0.0 : NEXT : TODO, Story 3 : 0.0 : NEXT : IN_PROGRESS, Story 2 : 0.0 : LATER : TODO, Story 5 : 0.0 : LATER : DONE]"
        , service.getAllStories().toString());

    }

    // This test is using addStoryToBacklog assuming it is not broken. There is no other way to access //
    // the stories list without using these methods because the fields are private and we cannot add //
    // any getters or setters //
    @Test
    public void testGetCurrentSprint() {
        InMemoryStoryService service = new InMemoryStoryService(null);

        Story s = new Story();
        s.setId("ID:1");
        s.setSummary("Story 1");
        s.setAcceptanceCriteria("Implementing and Testing!");
        s.setPriorityCurrent();
        s.setStatus(0);

        Story s2 = new Story();
        s2.setId("ID:2");
        s2.setSummary("Story 2");
        s2.setAcceptanceCriteria("Implementing and Testing!");
        s2.setPriorityLater();
        s2.setStatus(0);

        Story s3 = new Story();
        s3.setId("ID:3");
        s3.setSummary("Story 3");
        s3.setAcceptanceCriteria("Implementing!");
        s3.setPriorityNext();
        s3.setStatus(1);

        Story s4 = new Story();
        s4.setId("ID:4");
        s4.setSummary("Story 4");
        s4.setAcceptanceCriteria("Implementing!");
        s4.setPriorityCurrent();
        s4.setStatus(1);

        Story s5 = new Story();
        s5.setId("ID:5");
        s5.setSummary("Story 5");
        s5.setAcceptanceCriteria("Implementing!");
        s5.setPriorityLater();
        s5.setStatus(2);

        Story s6 = new Story();
        s6.setId("ID:6");
        s6.setSummary("Story 6");
        s6.setAcceptanceCriteria("Implementing!");
        s6.setPriorityNext();
        s6.setStatus(0);

        service.addStoryToBacklog(s);
        service.addStoryToBacklog(s2);
        service.addStoryToBacklog(s3);
        service.addStoryToBacklog(s4);
        service.addStoryToBacklog(s5);
        service.addStoryToBacklog(s6);

        /* Returns stories with Next/Later priority */
        // Make sure that no story contains the NEXT or LATER priority //
        for (Story story : service.getCurrentSprintStories()) {
            assertFalse(story.getPriority().equals(Story.Priority.NEXT));
            assertFalse(story.getPriority().equals(Story.Priority.LATER));
        }

        /* Returns subset of all current priority stories */

        // Make sure that all the stories that are returned have the CURRENT priority //
        for (Story story : service.getCurrentSprintStories()) {
            assertTrue(story.getPriority().equals(Story.Priority.CURRENT));
        }

        // Check that all were collected //
        assertEquals(service.getCurrentSprintStories().size(),2);

    }
}
